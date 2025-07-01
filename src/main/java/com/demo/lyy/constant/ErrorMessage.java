package com.demo.lyy.constant;

import lombok.Getter;

/**
 * @Author LYY
 * @Date 2025/7/1
 **/
public enum ErrorMessage {







    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND","The transaction you requested was not found."),
    CONFLICT("CONFLICT","Conflict: Resource has been updated by someone else."),
    ;

    @Getter
    private String errorCode;
    @Getter
    private String errorMessage;

    ErrorMessage(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
