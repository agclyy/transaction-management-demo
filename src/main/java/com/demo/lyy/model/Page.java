package com.demo.lyy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {

    private List<T> records;
    private Integer total;
    private Integer currentPage;
    private Integer totalPages;
    private Integer size;

}
