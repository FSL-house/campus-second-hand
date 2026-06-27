package com.campus.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类。
 *
 * 作用：
 * 当业务代码抛出异常时，统一返回给前端友好的错误信息。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获运行时异常。
     *
     * @param e 异常对象
     * @return 统一失败结果
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        return Result.fail(e.getMessage());
    }

    /**
     * 捕获所有其他异常。
     *
     * @param e 异常对象
     * @return 统一失败结果
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        return Result.fail("系统异常，请稍后重试");
    }
}
