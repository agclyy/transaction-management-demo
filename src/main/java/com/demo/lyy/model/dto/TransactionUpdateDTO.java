package com.demo.lyy.model.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@Data
public class TransactionUpdateDTO {

    @Pattern(regexp = "^[0-9]{16}$", message = "Invalid transactionId")
    private String transactionId;
    @Pattern(regexp = "^[A-Za-z0-9]{10,20}$", message = "Invalid beneficiaryAccount")
    private String beneficiaryAccount;
    @Pattern(regexp = "^[A-Za-z0-9]{10,20}$", message = "Invalid remitterAccount")
    private String remitterAccount;
    private String transactionStatus = "PENDING";
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid fromCurrency")
    private String fromCurrency;
    private BigDecimal fromAmount;
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid toCurrency")
    private String toCurrency;
    private BigDecimal toAmount;
    private BigDecimal exchangeRate;
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid feeCurrency")
    private String feeCurrency;
    private BigDecimal fee;

    private String transactionType;
    @Pattern(regexp = "^[A-Za-z0-9]{5,20}$", message = "Invalid updatedUser")
    private String updatedUser = "SYSTEM";
    private String comments;


}
