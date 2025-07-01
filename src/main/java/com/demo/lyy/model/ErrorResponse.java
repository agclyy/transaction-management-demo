package com.demo.lyy.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;
    public ErrorResponse(String error){
        this.error = error;
    }
}