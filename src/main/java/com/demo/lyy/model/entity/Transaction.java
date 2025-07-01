package com.demo.lyy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private String transactionId;
    private String remitterAccount;
    private String fromCurrency;
    private BigDecimal fromAmount;
    private String beneficiaryAccount;
    private String toCurrency;
    private BigDecimal toAmount;
    private BigDecimal exchangeRate;
    private String feeCurrency;
    private BigDecimal fee;

    private String transactionStatus;
    private String transactionType;
    private LocalDateTime createdAt;
    private String createdUser;
    private LocalDateTime updatedAt;
    private String updatedUser;
    private LocalDateTime deletedAt;
    private String deletedUser;
    private String comments;

}