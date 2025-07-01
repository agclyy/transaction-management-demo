package com.demo.lyy.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionConvert transactionConvert;

    @Mock
    private TransactionIdGenerator transactionIdGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransactionById_Success() {
        String transactionId = "123";
        Transaction expected = new Transaction();
        when(transactionMapper.findById(transactionId)).thenReturn(expected);

        Transaction result = transactionService.getTransactionById(transactionId);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(transactionMapper, times(1)).findById(transactionId);
    }

    @Test
    void testGetTransactionById_NotFound_ThrowsResourceNotFoundException() {
        String transactionId = "456";
        when(transactionMapper.findById(transactionId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransactionById(transactionId));
    }

    @Test
    void testCreateTransaction_Success() {
        TransactionAddDTO dto = new TransactionAddDTO();
        dto.setRemitterAccount("ABC123");
        dto.setFromCurrency("USD");
        dto.setFromAmount(BigDecimal.valueOf(100.0));
        dto.setBeneficiaryAccount("BEN123");
        dto.setToCurrency("CNY");
        dto.setToAmount(BigDecimal.valueOf(720.0));
        dto.setExchangeRate(BigDecimal.valueOf(7.2));
        dto.setFeeCurrency("USD");
        dto.setFee(BigDecimal.valueOf(1.5));
        dto.setTransactionType("TRANSFER");
        dto.setComments("Monthly salary transfer");

        Transaction transaction = new Transaction();
        String generatedId = "0000000000000005";

        when(transactionIdGenerator.generateFixedLengthId()).thenReturn(generatedId);
        when(transactionConvert.toDo(dto)).thenReturn(transaction); // 模拟转换逻辑
        when(transactionMapper.insert(any(Transaction.class))).thenReturn(1);

        Transaction result = transactionService.createTransaction(dto);

        assertNotNull(result);
        assertEquals(generatedId, result.getTransactionId());
        verify(transactionMapper, times(1)).insert(result);
    }



    @Test
    void testUpdateTransaction_Success() {
        String transactionId = "T123";
        LocalDateTime lastUpdatedAt = LocalDateTime.now().minusMinutes(1);
        Transaction existing = new Transaction();
        existing.setTransactionId(transactionId);
        existing.setUpdatedAt(lastUpdatedAt);

        TransactionUpdateDTO updateDTO = new TransactionUpdateDTO();
        updateDTO.setUpdatedUser("admin");
        updateDTO.setRemitterAccount("NEW_ACCOUNT");

        when(transactionMapper.findById(transactionId)).thenReturn(existing);
        when(transactionMapper.update(any(Transaction.class), eq(lastUpdatedAt))).thenReturn(1);

        Transaction result = transactionService.updateTransaction(transactionId, updateDTO);

        assertNotNull(result);
        assertEquals("NEW_ACCOUNT", result.getRemitterAccount());
        assertEquals("admin", result.getUpdatedUser());
        verify(transactionMapper, times(1)).update(any(Transaction.class), eq(lastUpdatedAt));
    }

    @Test
    void testUpdateTransaction_ResourceNotFound_ThrowsResourceNotFoundException() {
        String transactionId = "T123";
        TransactionUpdateDTO updateDTO = new TransactionUpdateDTO();

        when(transactionMapper.findById(transactionId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> transactionService.updateTransaction(transactionId, updateDTO));
    }

    @Test
    void testUpdateTransaction_Conflict_ThrowsOptimisticLockException() {
        String transactionId = "T123";
        LocalDateTime lastUpdatedAt = LocalDateTime.now().minusMinutes(1);
        Transaction existing = new Transaction();
        existing.setTransactionId(transactionId);
        existing.setUpdatedAt(lastUpdatedAt);

        TransactionUpdateDTO updateDTO = new TransactionUpdateDTO();

        when(transactionMapper.findById(transactionId)).thenReturn(existing);
        when(transactionMapper.update(any(Transaction.class), eq(lastUpdatedAt))).thenReturn(0);

        assertThrows(OptimisticLockException.class, () -> transactionService.updateTransaction(transactionId, updateDTO));
    }

    @Test
    void testDeleteTransaction_Success() {
        String transactionId = "T123";
        LocalDateTime lastUpdatedAt = LocalDateTime.now().minusMinutes(1);
        Transaction existing = new Transaction();
        existing.setTransactionId(transactionId);
        existing.setUpdatedAt(lastUpdatedAt);

        when(transactionMapper.findById(transactionId)).thenReturn(existing);
        when(transactionMapper.delete(eq(transactionId), eq(lastUpdatedAt))).thenReturn(1);

        assertDoesNotThrow(() -> transactionService.deleteTransaction(transactionId));
        verify(transactionMapper, times(1)).delete(eq(transactionId), eq(lastUpdatedAt));
    }

    @Test
    void testDeleteTransaction_ResourceNotFound_ThrowsResourceNotFoundException() {
        String transactionId = "T123";
        when(transactionMapper.findById(transactionId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> transactionService.deleteTransaction(transactionId));
    }

    @Test
    void testDeleteTransaction_Conflict_ThrowsOptimisticLockException() {
        String transactionId = "T123";
        LocalDateTime lastUpdatedAt = LocalDateTime.now().minusMinutes(1);
        Transaction existing = new Transaction();
        existing.setTransactionId(transactionId);
        existing.setUpdatedAt(lastUpdatedAt);

        when(transactionMapper.findById(transactionId)).thenReturn(existing);
        when(transactionMapper.delete(eq(transactionId), eq(lastUpdatedAt))).thenReturn(0);

        assertThrows(OptimisticLockException.class, () -> transactionService.deleteTransaction(transactionId));
    }

    @Test
    void testPage_ReturnsPagedResult() {
        TransactionListDTO dto = new TransactionListDTO();
        dto.setPage(1);
        dto.setSize(10);
        dto.setOffset(0);

        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        int total = 2;

        when(transactionMapper.searchTransactions(dto)).thenReturn(transactions);
        when(transactionMapper.countTransactions(dto)).thenReturn(total);
        when(transactionConvert.toVo(transactions)).thenReturn(Collections.singletonList(new TransactionVO()));

        Page<TransactionVO> result = transactionService.page(dto);

        assertNotNull(result);
        assertEquals(2, result.getTotal());
        assertEquals(1, result.getCurrentPage());
        assertEquals(10, result.getSize());
        assertEquals(1, result.getTotalPages());
        verify(transactionMapper, times(1)).searchTransactions(dto);
        verify(transactionMapper, times(1)).countTransactions(dto);
    }
}
