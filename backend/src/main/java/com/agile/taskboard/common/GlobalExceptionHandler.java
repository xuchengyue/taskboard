package com.agile.taskboard.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDenied(AccessDeniedException e) {
        return Result.fail(403, "没有权限执行该操作");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<Void> handleValidException(Exception e) {
        String msg;
        if (e instanceof MethodArgumentNotValidException manv) {
            msg = manv.getBindingResult().getFieldError() != null
                    ? manv.getBindingResult().getFieldError().getDefaultMessage()
                    : "参数校验失败";
        } else {
            BindException be = (BindException) e;
            msg = be.getBindingResult().getFieldError() != null
                    ? be.getBindingResult().getFieldError().getDefaultMessage()
                    : "参数校验失败";
        }
        return Result.fail(400, msg);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.fail(500, "系统异常，请联系管理员：" + e.getMessage());
    }
}
