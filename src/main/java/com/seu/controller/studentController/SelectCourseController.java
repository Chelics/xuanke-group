package com.seu.controller.studentController;

import com.seu.dto.request.CourseSelection;
import com.seu.exception.InvalidInputException;
import com.seu.exception.SelectCourseException;
import com.seu.exception.SelectCourseRuntimeException;
import com.seu.pojo.Result;
import com.seu.service.studentService.SelectCourseService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping("/student/course/select")
public class SelectCourseController {
    @Autowired
    SelectCourseService studentSelectCourseService;

    // 创建一个大小为10的线程池
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    // 创建一个哈希表, 使每个课程id对应一个消息队列
    private final ConcurrentHashMap<Integer, BlockingQueue<Callable>> courseQueues = new ConcurrentHashMap<>();

    /**
     * 学生选课
     * @param courseSelection
     * @return
     * @throws InvalidInputException
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<Result>> selectCourse(@RequestBody CourseSelection courseSelection, @RequestAttribute Claims claims) throws Exception {

        // 从claims中获取studentId
        String studentIdStr = claims.get("id").toString();

        // 将studentIdStr转换为整数
        Integer studentId;
        try {
            studentId = Integer.parseInt(studentIdStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("获取失败: 非法的学生id");
        }

        Integer courseId = courseSelection.getCourseId();

        //检查输入
        if(courseId == null || courseId < 0 || studentId < 0) {
            throw new InvalidInputException("选课失败: 课程id或学生id为空");
        }
        log.info("学生: id{} 选课: id{}", studentId, courseId);

        //获取对应课程id的消息队列
        BlockingQueue<Callable> courseQueue = courseQueues.computeIfAbsent(courseId, k -> new LinkedBlockingQueue<>());

        //将选课任务加入消息队列
        courseQueue.offer(() -> studentSelectCourseService.selectCourse(courseId, studentId));

        // 异步执行选课任务, 执行完后返回结果
        return CompletableFuture.supplyAsync(() -> processTask(courseQueue), executorService);

    }

    /**
     * 从队列中取出线程并执行任务
     * @param courseQueue
     * @return
     */
    private ResponseEntity<Result> processTask(BlockingQueue<Callable> courseQueue){
        try{
            Callable task = courseQueue.take();
            task.call();
        } catch (InterruptedException e) {
            throw new SelectCourseRuntimeException("线程中断");
        } catch (SelectCourseException e) {
            throw new SelectCourseRuntimeException(e.getMessage(), e.getStatus());
        } catch (Exception e){
            throw new SelectCourseRuntimeException("未知错误, 请联系管理员", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Result.success("选课成功"), HttpStatus.OK);
    }

    /**
     * 关闭线程池
     */
    @PreDestroy
    public void shutdownThreadPool() {
        try {
            log.info("尝试关闭线程池...");
            executorService.shutdown();
            if (!executorService.awaitTermination(2000, TimeUnit.MILLISECONDS)) {
                log.warn("线程池没有在指定时间内关闭，正在尝试立即关闭...");
                List<Runnable> droppedTasks = executorService.shutdownNow();
                log.warn("关闭线程池后，有 " + droppedTasks.size() + " 个未执行的任务");
            }
            log.info("线程池已在指定时间内成功关闭");
        } catch (InterruptedException e) {
            log.error("关闭线程池时被中断", e);
            Thread.currentThread().interrupt();
        }
    }

}
