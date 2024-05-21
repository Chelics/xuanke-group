package com.seu.service.impl.studentServiceImpl;

import com.seu.exception.EntityNotFoundException;
import com.seu.exception.SelectCourseException;
import com.seu.mapper.CourseMapper;
import com.seu.mapper.CourseStudentMapper;
import com.seu.mapper.StageMapper;
import com.seu.mapper.StudentMapper;
import com.seu.pojo.FullCourse;
import com.seu.pojo.Users.Student;
import com.seu.service.CheckStageService;
import com.seu.service.studentService.SelectCourseService;
import com.seu.utils.CheckTimeConflictsUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Service
@Slf4j
public class SelectCourseServiceImpl implements SelectCourseService {

    @Autowired
    CourseMapper courseMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    CourseStudentMapper courseStudentMapper;
    @Autowired
    StageMapper stageMapper;
    @Autowired
    CheckStageService checkStageService;

    //锁哈希表, 为每个课程id储存锁
    private final ConcurrentHashMap<Integer, Lock> lockMap = new ConcurrentHashMap<>();

    //消息队列哈希表, 为每个课程id创建消息队列
    private final ConcurrentHashMap<Integer, BlockingQueue<Runnable>> courseQueues = new ConcurrentHashMap<>();

    //线程池, 大小为10, 线程池会自动处理线程的分配和任务的执行
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 学生选课
     * @param courseId
     * @param studentId
     * @return
     */
    @Override
    //@Retryable(value = { SQLException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public boolean selectCourse(Integer courseId, Integer studentId) throws SelectCourseException, EntityNotFoundException {

        FullCourse fullCourse = courseMapper.getCourseById(courseId);

        //输入检查 & 时间冲突检查
        checkInput(fullCourse, studentId);

        //boolean result = false;
        //由于lambda表达式不支持对非final变量修改, 将上面注释掉的语句改为下面这行, 意思不变
        AtomicBoolean result = new AtomicBoolean(false);    //AtomicBoolean是一个原子类, 可以在lambda表达式内部安全地修改它

        //如果courseQueues中没有courseId对应的消息队列, 创建一个新的队列
        //该操作是一个原子操作, 不需要额外同步
        //offer()将一个新的任务lambda表达式的形式添加到队列中, 这个任务将在未来由一个线程执行
        courseQueues.computeIfAbsent(courseId, k -> new LinkedBlockingQueue<>()).offer(() -> {

            //获取对应courseId的锁, 如果锁被占用, 等待直到锁被释放
            Lock lock = lockMap.computeIfAbsent(courseId, k -> new ReentrantLock());
            lock.lock();    //加锁

            try {
                //检查课程是否已满
                checkStorage(courseId, fullCourse);
                //将课程-学生加入数据库
                //注意: 在入库时发生异常会返回课程人数已满的响应信息, 由于目前看来不会发生入库时的异常, 故没有处理
                insertSelectedCourse(courseId, studentId);
                result.set(true);
            } catch (SelectCourseException ex){
                log.warn(studentId + "选课失败, 课程人数已满: " + courseId);
            } finally{
                lockMap.get(courseId).unlock(); //释放锁
            }
        });

        processQueue(courseId);

        return result.get();
    }

    /**
     * 确认学生和课程都存在, 且待选课程与已选课程没有时间冲突
     * @param fullCourse
     * @param studentId
     * @return
     */
    private void checkInput(FullCourse fullCourse, Integer studentId) throws SelectCourseException, EntityNotFoundException {

        //判断是否处在可以选课的阶段
        checkStageService.checkStage();

        if (fullCourse == null) {
            throw new SelectCourseException("课程不存在", HttpStatus.BAD_REQUEST);
        }

        Student student = studentMapper.getStudentById(studentId);
        if(student == null){
            throw new SelectCourseException("学生不存在: " + studentId, HttpStatus.BAD_REQUEST);
        }

        if (hasTimeConflicts(studentId, fullCourse)){
            throw new SelectCourseException("课程已选或与其它已选课程存在时间冲突", HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 检查待选课程是否与已选课程存在时间冲突
     * @param studentId
     * @param fullCourse
     * @return
     */
    private boolean hasTimeConflicts(Integer studentId, FullCourse fullCourse){
        List<Integer> selectedCourseIds = courseStudentMapper.getCourseIdsByStudentId(studentId);

        if(selectedCourseIds == null){
            return false;
        }

        List<FullCourse> courseList = courseMapper.getCoursesByIds(selectedCourseIds);
        return CheckTimeConflictsUtils.checkCoursesAndCourse(courseList, fullCourse);
    }

    /**
     * 检查课程容量是否已满
     * @param courseId
     * @param fullCourse
     * @throws SelectCourseException
     */
    private void checkStorage(Integer courseId, FullCourse fullCourse) throws SelectCourseException {
        Integer studentsCountForCourse = courseStudentMapper.getStudentCountForCourse(courseId);
        int count = (studentsCountForCourse == null) ? 0 : studentsCountForCourse;
        if (count >= fullCourse.getCourseStorage()) {
            throw new SelectCourseException("课程已满: " + courseId, HttpStatus.FORBIDDEN);
        }
    }

    /**
     * 向关联表中插入记录
     * @param courseId
     * @param studentId
     * @throws SelectCourseException
     */
    private void insertSelectedCourse(Integer courseId, Integer studentId) throws SelectCourseException {
        if(courseStudentMapper.selectCourse(courseId, studentId) <= 0){
            throw new SelectCourseException("数据库操作失败: 学生id: " + studentId + "课程id: " + courseId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 创建守护线程
     */
    @PostConstruct
    private void createDaemonThread(){
        // 创建线程
        Thread daemonThread = new Thread(() -> {
            while (true) {
                for (Integer id : courseQueues.keySet()) {  //遍历courseQueue中每个courseId对应的队列
                    processQueue(id); // 处理队列中的任务
                }
                try {
                    Thread.sleep(1000); // 每秒检查一次
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 恢复中断状态
                    break; // 如果线程被中断，退出循环
                }
            }
        });
        daemonThread.setDaemon(true); // 设置为守护线程
        daemonThread.start(); // 启动线程
    }

    /**
     * 处理消息队列
     * @param courseId
     */
    private void processQueue(Integer courseId) {
        BlockingQueue<Runnable> queue = courseQueues.get(courseId);

        if (queue != null) {    //若队列不为空
            Runnable task;
            while ((task = queue.poll()) != null) { //从队列头部取出任务
                executorService.submit(task);   //将取出的任务交给线程池执行
            }
        }
    }

    /**
     * 关闭线程池
     */
    @PreDestroy
    public void shutdownThreadPool() {
        try {
            log.info("尝试关闭线程池...");
            executorService.shutdown();
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                log.warn("线程池没有在指定时间内关闭，正在尝试立即关闭...");
                List<Runnable> droppedTasks = executorService.shutdownNow();
                log.warn("关闭线程池后，有 " + droppedTasks.size() + " 个未执行的任务");
            }
        } catch (InterruptedException e) {
            log.error("关闭线程池时被中断", e);
            Thread.currentThread().interrupt();
        }
    }

}
