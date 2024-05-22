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
        return Result.error("未知异常, 请联系管理员");
    }

    /**
     * 非法输入异常
     * @param ex
     * @return
     */
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> ex(InvalidInputException ex){
        log.warn("捕获到错误请求异常: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 选课失败异常
     * @param ex
     * @return
     */
    @ExceptionHandler(SelectCourseException.class)
    public ResponseEntity<String> ex(SelectCourseException ex){
        log.warn("捕获到选课失败异常", ex);
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
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * 空指针异常
     * @param ex
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        log.error("捕获到空指针异常", ex);
        return new ResponseEntity<>("Null Pointer Exception", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 登录校验异常
     * @param ex
     * @return
     */
    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<String> handleUserNotLoginException(UserNotLoggedInException ex){
        log.warn("捕获到登录校验异常", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * 分配课程异常
     * @param ex
     * @return
     */
    @ExceptionHandler(AllocateCourseException.class)
    public ResponseEntity<Object> handlerAllocateFailureException(AllocateCourseException ex) {
        log.warn("捕获到自动排课异常", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 登录异常
     * @param ex
     * @return
     */
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Object> handleLoginException(LoginException ex){
        log.warn("捕获到用户登录异常: ", ex);
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(SelectCourseRuntimeException.class)
    public ResponseEntity<Object> handleSelectCourseRuntimeException(SelectCourseRuntimeException ex){
        log.warn("捕获到选课失败异常: ", ex);
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

}
