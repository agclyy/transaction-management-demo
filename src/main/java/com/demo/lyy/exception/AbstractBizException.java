package com.demo.lyy.exception;


import lombok.Data;

/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@Data
public class AbstractBizException extends RuntimeException {
    private final String errorCode;

    public AbstractBizException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}