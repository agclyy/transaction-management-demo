package com.demo.lyy.service.convert;


import com.demo.lyy.model.dto.TransactionAddDTO;
import com.demo.lyy.model.entity.Transaction;
import com.demo.lyy.model.vo.TransactionVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@Mapper(componentModel = "spring")
public interface TransactionConvert {

    TransactionVO toVo(Transaction transaction);
    Transaction toDo(TransactionAddDTO transactionAddDTO);

    List<TransactionVO> toVo(List<Transaction> transactions);


}

