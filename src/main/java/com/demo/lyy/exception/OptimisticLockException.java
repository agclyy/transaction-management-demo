package com.demo.lyy.exception;


/**
 * @Author LYY
 * @Date 2025/6/30
 **/
public class OptimisticLockException extends AbstractBizException {
    public OptimisticLockException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}