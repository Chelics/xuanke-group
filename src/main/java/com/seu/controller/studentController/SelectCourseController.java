package com.seu.controller.studentController;

import com.seu.dto.request.CourseSelection;
import com.seu.exception.InvalidInputException;
import com.seu.exception.SelectCourseException;
import com.seu.pojo.Result;
import com.seu.service.studentService.SelectCourseService;
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
    SelectCourseService selectCourseService;
    @Autowired
    ExecutorService executorService;

    // 创建一个哈希表, 使每个课程id对应一个消息队列
    // 注意, 在当前代码的逻辑中, 队列中的任务不遵循严格的串行执行, 同一队列中的几个任务可能会被多个线程同时执行, 这是可接受的
    private final ConcurrentHashMap<Integer, BlockingQueue<Callable>> courseQueues = new ConcurrentHashMap<>();

    /**
     * 学生选课
     * @param courseSelection
     * @return
     * @throws InvalidInputException
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<Result>> selectCourse(@RequestBody CourseSelection courseSelection, @RequestAttribute Integer userId) throws Exception {

        Integer courseId = courseSelection.getCourseId();
        Integer studentId = userId;

        //检查输入
        if(courseId == null || courseId < 0 || studentId < 0) {
            throw new InvalidInputException("选课失败: 课程id或学生id为空");
        }
        log.info("学生: {} 选课: {}", studentId, courseId);

        //获取对应课程id的消息队列
        BlockingQueue<Callable> courseQueue = courseQueues.computeIfAbsent(courseId, k -> new LinkedBlockingQueue<>());

        //将选课任务加入消息队列, 消息队列中的任务会排队执行
        courseQueue.offer(() -> selectCourseService.selectCourse(courseId, studentId));

        //创建CompletableFuture异步处理任务
        CompletableFuture<ResponseEntity<Result>> future = new CompletableFuture<>();

        //将任务分配给线程池
        try {
            executorService.submit(() -> processTask(courseQueue, future));
        } catch (RejectedExecutionException e) { //如果线程池队列已满, 返回错误信息
            log.warn("任务被线程池拒绝执行");
            future.completeExceptionally(new SelectCourseException("选课失败: 服务器繁忙，请稍后重试", HttpStatus.SERVICE_UNAVAILABLE));
        }

        // 主线程不会在此处阻塞, 即使线程池中的线程全部被占用, 任务会排队等待执行, 而主线程不受影响
        return future.exceptionally(e -> {
            if(e instanceof SelectCourseException){
                return new ResponseEntity<>(Result.error(e.getMessage()), ((SelectCourseException) e).getStatus());
            }else{
                return new ResponseEntity<>(Result.error("选课失败!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
    }

    /**
     * 从队列中取出线程并执行任务
     * @param courseQueue
     * @return
     */
    private void processTask(BlockingQueue<Callable> courseQueue, CompletableFuture<ResponseEntity<Result>> future){
        try{
            Callable task = courseQueue.take();
            task.call();
            future.complete(new ResponseEntity<>(Result.success(), HttpStatus.OK));
        } catch (Exception e){
            log.warn("处理选课任务时发生异常");
            future.completeExceptionally(e);
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
            if (!executorService.awaitTermination(2000, TimeUnit.MILLISECONDS)) {
                log.warn("线程池没有在指定时间内关闭，正在尝试立即关闭...");
                List<Runnable> droppedTasks = executorService.shutdownNow();
                log.warn("关闭线程池后，有{}个未执行的任务", droppedTasks.size());
            }
            log.info("线程池已在指定时间内成功关闭");
        } catch (InterruptedException e) {
            log.error("关闭线程池时被中断", e);
            Thread.currentThread().interrupt();
        } finally{
            log.info("开始清空消息队列");
            courseQueues.clear();
            log.info("消息队列清空完成");
        }
    }
}
