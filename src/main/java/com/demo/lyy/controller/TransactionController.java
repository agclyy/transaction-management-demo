package com.demo.lyy.controller;

import com.demo.lyy.model.ErrorResponse;
import com.demo.lyy.model.Page;
import com.demo.lyy.model.dto.TransactionAddDTO;
import com.demo.lyy.model.dto.TransactionListDTO;
import com.demo.lyy.model.dto.TransactionUpdateDTO;
import com.demo.lyy.model.entity.Transaction;
import com.demo.lyy.model.vo.TransactionVO;
import com.demo.lyy.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@RestController
@RequestMapping("/api/v1/transaction")
@Tag(name = "transactions", description = "Financial transaction management API")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    @Operation(summary = "Search transactions", description = "Searches transactions based on criteria with pagination")
    @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(schema = @Schema(implementation =
            ErrorResponse.class))})
    public ResponseEntity<Page<TransactionVO>> page(@Valid TransactionListDTO transactionListDTO) {
        return ResponseEntity.ok(transactionService.page(transactionListDTO));
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Get a transaction by ID", description = "Returns a specific transaction by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(schema = @Schema(implementation =
            ErrorResponse.class))})
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }

    @PostMapping
    @Operation(summary = "Create a new transaction", description = "Creates a new transaction with the provided details")
    @ApiResponse(responseCode = "201", description = "Transaction created successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(schema = @Schema(implementation =
            ErrorResponse.class))})
    public ResponseEntity<Transaction> createTransaction(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Transaction object to be created",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionAddDTO.class))
            ) @Valid @RequestBody TransactionAddDTO transactionAddDTO) {
        Transaction createdTransaction = transactionService.createTransaction(transactionAddDTO);
        return ResponseEntity.status(201).body(createdTransaction);
    }

    @PutMapping("/{transactionId}")
    @Operation(summary = "Update an existing transaction", description = "Updates all details of an existing transaction")
    @ApiResponse(responseCode = "200", description = "Transaction updated successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(schema = @Schema(implementation =
            ErrorResponse.class))})
    public ResponseEntity<Transaction> updateTransaction(
            @Parameter(name = "transactionId", in = ParameterIn.PATH, description = "Unique identifier of the transaction to " +
                    "update")
            @PathVariable String transactionId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated transaction data",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            TransactionUpdateDTO.class))
            ) @Valid @RequestBody TransactionUpdateDTO transactionUpdateDTO) {
        transactionUpdateDTO.setTransactionId(transactionId);
        Transaction updatedTransaction = transactionService.updateTransaction(transactionId, transactionUpdateDTO);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{transactionId}")
    @Operation(summary = "Delete a transaction", description = "Marks a transaction as deleted by setting deletedAt and " +
            "deletedUser fields")
    @ApiResponse(responseCode = "204", description = "Transaction deleted successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(schema = @Schema(implementation =
            ErrorResponse.class))})
    public ResponseEntity<Void> deleteTransaction(@PathVariable String transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }
}