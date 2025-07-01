package com.demo.lyy.constant;

import lombok.Getter;

/**
 * @Author LYY
 * @Date 2025/6/30
 **/
public enum TransactionStatus {


    PENDING("Pending","add new transaction"),

    SUBMITTED("Submitted","checked by one user, and submitted"),

    COMPLETED("Completed","authorized by another user, and completed"),

    DELETED("Deleted","transaction has been deleted by user");

    @Getter
    private String status;
    @Getter
    private String description;

    TransactionStatus(String status,String description) {
        this.status = status;
        this.description = description;
    }



}
