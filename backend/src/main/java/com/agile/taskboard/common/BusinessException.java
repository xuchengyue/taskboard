package com.agile.taskboard.common;

/**
 * 业务异常，controller层不用手动try-catch，交给GlobalExceptionHandler统一处理
 */
public class BusinessException extends RuntimeException {

    private int code = 500;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
