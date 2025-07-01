package com.demo.lyy.service;

import com.demo.lyy.constant.CommonConstant;
import com.demo.lyy.constant.ErrorMessage;
import com.demo.lyy.exception.OptimisticLockException;
import com.demo.lyy.exception.ResourceNotFoundException;
import com.demo.lyy.mapper.TransactionMapper;
import com.demo.lyy.model.Page;
import com.demo.lyy.model.dto.TransactionAddDTO;
import com.demo.lyy.model.dto.TransactionListDTO;
import com.demo.lyy.model.dto.TransactionUpdateDTO;
import com.demo.lyy.model.entity.Transaction;
import com.demo.lyy.model.vo.TransactionVO;
import com.demo.lyy.service.convert.TransactionConvert;
import com.demo.lyy.util.TransactionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@Service
public class TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionConvert transactionConvert;
    @Autowired
    private TransactionIdGenerator transactionIdGenerator;

    public Transaction getTransactionById(String transactionId) {
        Transaction transaction = transactionMapper.findById(transactionId);
        if (transaction == null) {
            throw new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND.getErrorCode(), ErrorMessage.RESOURCE_NOT_FOUND.getErrorMessage());
        }
        return transaction;
    }

    public List<Transaction> searchTransactions(TransactionListDTO transactionListDTO) {
        // calculate offset
        int offset = Math.max(0, (transactionListDTO.getPage() - 1) * transactionListDTO.getSize());
        transactionListDTO.setOffset(offset);
        return transactionMapper.searchTransactions(transactionListDTO);
    }

    public int countTransactions(TransactionListDTO transactionListDTO) {
        return transactionMapper.countTransactions(transactionListDTO);
    }

    public Transaction createTransaction(TransactionAddDTO transactionAddDTO) {
        //todo possible duplicate check
        Transaction transaction = transactionConvert.toDo(transactionAddDTO);
        transaction.setTransactionId(transactionIdGenerator.generateFixedLengthId());
        transactionMapper.insert(transaction);
        return transaction;
    }

    public Transaction updateTransaction(String transactionId, TransactionUpdateDTO transactionUpdateDTO) {
        Transaction transaction = transactionMapper.findById(transactionId);
        if (transaction == null) {
            throw new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND.getErrorCode(), ErrorMessage.RESOURCE_NOT_FOUND.getErrorMessage());
        }
        LocalDateTime lastUpdatedAt = transaction.getUpdatedAt();
        transaction.setTransactionId(transactionId);
        transaction.setRemitterAccount(transactionUpdateDTO.getRemitterAccount());
        transaction.setFromCurrency(transactionUpdateDTO.getFromCurrency());
        transaction.setFromAmount(transactionUpdateDTO.getFromAmount());
        transaction.setBeneficiaryAccount(transactionUpdateDTO.getBeneficiaryAccount());
        transaction.setToCurrency(transactionUpdateDTO.getToCurrency());
        transaction.setToAmount(transactionUpdateDTO.getToAmount());
        transaction.setExchangeRate(transactionUpdateDTO.getExchangeRate());
        transaction.setFeeCurrency(transactionUpdateDTO.getFeeCurrency());
        transaction.setFee(transactionUpdateDTO.getFee());
        transaction.setTransactionStatus(transactionUpdateDTO.getTransactionStatus());
        transaction.setTransactionType(transactionUpdateDTO.getTransactionType());
        transaction.setComments(transactionUpdateDTO.getComments());
        transaction.setUpdatedAt(LocalDateTime.now(ZoneId.of(CommonConstant.GMT_PLUS_8)));
        transaction.setUpdatedUser(transactionUpdateDTO.getUpdatedUser());
        int updatedRows = transactionMapper.update(transaction,lastUpdatedAt);
        if (updatedRows == 0) {
            throw new OptimisticLockException(ErrorMessage.CONFLICT.getErrorCode(), ErrorMessage.CONFLICT.getErrorMessage());
        }
        // todo journal entry
        return transaction;
    }

    public void deleteTransaction(String transactionId) {
        Transaction transaction = transactionMapper.findById(transactionId);
        if (transaction == null) {
            throw new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND.getErrorCode(), ErrorMessage.RESOURCE_NOT_FOUND.getErrorMessage());
        }
        LocalDateTime lastUpdatedAt = transaction.getUpdatedAt();
        int deletedRows = transactionMapper.delete(transactionId,lastUpdatedAt);
        if (deletedRows == 0) {
            throw new OptimisticLockException(ErrorMessage.CONFLICT.getErrorCode(), ErrorMessage.CONFLICT.getErrorMessage());
        }
    }


    public Page<TransactionVO> page(TransactionListDTO transactionListDTO) {
        List<Transaction> transactions = this.searchTransactions(transactionListDTO);
        int total = this.countTransactions(transactionListDTO);
        Page<TransactionVO> result = Page.<TransactionVO>builder()
                .records(transactionConvert.toVo(transactions))
                .total(total)
                .currentPage(transactionListDTO.getPage())
                .totalPages((int) Math.ceil((double) total / transactionListDTO.getSize()))
                .size(transactionListDTO.getSize())
                .build();
        return result;
    }
}