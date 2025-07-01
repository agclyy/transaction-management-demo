package com.demo.lyy.controller;

import com.demo.lyy.model.Page;
import com.demo.lyy.model.dto.TransactionAddDTO;
import com.demo.lyy.model.dto.TransactionListDTO;
import com.demo.lyy.model.dto.TransactionUpdateDTO;
import com.demo.lyy.model.entity.Transaction;
import com.demo.lyy.model.vo.TransactionVO;
import com.demo.lyy.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPage() {
        TransactionListDTO dto = new TransactionListDTO();
        Page<TransactionVO> pageResult = new Page<>();
        pageResult.setRecords(Collections.singletonList(new TransactionVO()));

        when(transactionService.page(dto)).thenReturn(pageResult);

        ResponseEntity<Page<TransactionVO>> response = transactionController.page(dto);
        assertEquals(200, response.getStatusCodeValue());
        verify(transactionService, times(1)).page(dto);
    }

    @Test
    void testGetTransactionById() {
        String transactionId = "1751254691073020";
        Transaction transaction = new Transaction();
        when(transactionService.getTransactionById(transactionId)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.getTransactionById(transactionId);
        assertEquals(200, response.getStatusCodeValue());
        verify(transactionService, times(1)).getTransactionById(transactionId);
    }

    @Test
    void testCreateTransaction() {
        TransactionAddDTO dto = new TransactionAddDTO();
        Transaction created = new Transaction();

        when(transactionService.createTransaction(dto)).thenReturn(created);

        ResponseEntity<Transaction> response = transactionController.createTransaction(dto);
        assertEquals(201, response.getStatusCodeValue());
        verify(transactionService, times(1)).createTransaction(dto);
    }

    @Test
    void testUpdateTransaction() {
        String transactionId = "123";
        TransactionUpdateDTO dto = new TransactionUpdateDTO();
        dto.setTransactionId(transactionId);
        Transaction updated = new Transaction();

        when(transactionService.updateTransaction(transactionId, dto)).thenReturn(updated);

        ResponseEntity<Transaction> response = transactionController.updateTransaction(transactionId, dto);
        assertEquals(200, response.getStatusCodeValue());
        verify(transactionService, times(1)).updateTransaction(transactionId, dto);
    }

    @Test
    void testDeleteTransaction() {
        String transactionId = "1751254691073090";

        ResponseEntity<Void> response = transactionController.deleteTransaction(transactionId);
        assertEquals(204, response.getStatusCodeValue());
        verify(transactionService, times(1)).deleteTransaction(transactionId);
    }
}
