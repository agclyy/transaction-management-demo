package com.demo.lyy.exception;


/**
 * @Author LYY
 * @Date 2025/6/30
 **/
public class ResourceNotFoundException extends AbstractBizException {

    public ResourceNotFoundException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}