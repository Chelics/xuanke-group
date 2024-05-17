package com.seu.exception;

import com.seu.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result ex(Exception ex){
        log.error("捕获到异常: ", ex);
        return Result.error("对不起, 操作失败, 请联系管理员");
    }

    /**
     * 非法输入异常
     * @param ex
     * @return
     */
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> ex(InvalidInputException ex){
        log.error("捕获到错误请求异常: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 选课失败异常
     * @param ex
     * @return
     */
    @ExceptionHandler(SelectCourseFailureException.class)
    public ResponseEntity<String> ex(SelectCourseFailureException ex){
        log.error("捕获到选课失败异常", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 空查询结果异常
     * @param ex
     * @return
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> ex(EntityNotFoundException ex){
        log.error("捕获到空查询结果异常", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 退课失败异常
     * @param ex
     * @return
     */
    @ExceptionHandler(DropCourseFailureException.class)
    public ResponseEntity<String> ex(DropCourseFailureException ex){
        log.error("捕获到退课失败异常", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 空指针异常
     * @param ex
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        return new ResponseEntity<>("Null Pointer Exception", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
