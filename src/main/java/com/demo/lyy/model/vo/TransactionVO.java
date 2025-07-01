package com.demo.lyy.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@Data
public class TransactionVO {

    private String transactionId;
    private String remitterAccount;
    private String fromCurrency;
    private BigDecimal fromAmount;
    private String beneficiaryAccount;
    private String toCurrency;
    private BigDecimal toAmount;
    private String transactionStatus;
    private LocalDateTime createdAt;
    private String createdUser;
    private String comments;
}
