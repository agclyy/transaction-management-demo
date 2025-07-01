package com.demo.lyy.model.dto;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@Data
public class TransactionListDTO {
    @Pattern(regexp = "^[0-9]{16}$", message = "Invalid transactionId")
    private String transactionId;
    @Pattern(regexp = "^[A-Za-z0-9]{10,20}$", message = "Invalid beneficiaryAccount")
    private String beneficiaryAccount;
    @Pattern(regexp = "^[A-Za-z0-9]{10,20}$", message = "Invalid remitterAccount")
    private String remitterAccount;
    @Pattern(regexp = "^[A-Z]{0,10}$", message = "Invalid transactionStatus")
    private String transactionStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;
    private int page = 0;
    private int size = 10;
    @Hidden
    private int offset;

}
