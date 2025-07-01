package com.demo.lyy.mapper;

import com.demo.lyy.model.dto.TransactionListDTO;
import com.demo.lyy.model.entity.Transaction;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@Mapper
public interface TransactionMapper {

    @Select("<script>" +
            "SELECT * FROM transactions " +
            "<where>" +
            "  <if test='transactionListDTO.remitterAccount != null'>" +
            "    AND remitter_account = #{transactionListDTO.remitterAccount} " +
            "  </if>" +
            "  <if test='transactionListDTO.beneficiaryAccount != null'>" +
            "    AND beneficiary_account = #{transactionListDTO.beneficiaryAccount} " +
            "  </if>" +
            "  <if test='transactionListDTO.transactionStatus != null'>" +
            "    AND transaction_status = #{transactionListDTO.transactionStatus} " +
            "  </if>" +
            "  <if test='transactionListDTO.startDateTime != null'>" +
            "    AND created_at &gt;= #{transactionListDTO.startDateTime} " +
            "  </if>" +
            "  <if test='transactionListDTO.endDateTime != null'>" +
            "    AND created_at &lt;= #{transactionListDTO.endDateTime} " +
            "  </if>" +
            "</where>" +
            "ORDER BY created_at DESC " +
            "LIMIT #{transactionListDTO.size} OFFSET #{transactionListDTO.offset}" +
            "</script>")
    @Results(id = "transactionMap", value = {
            @Result(property = "transactionId", column = "transaction_id"),
            @Result(property = "remitterAccount", column = "remitter_account"),
            @Result(property = "fromCurrency", column = "from_currency"),
            @Result(property = "fromAmount", column = "from_amount"),
            @Result(property = "beneficiaryAccount", column = "beneficiary_account"),
            @Result(property = "toCurrency", column = "to_currency"),
            @Result(property = "toAmount", column = "to_amount"),
            @Result(property = "exchangeRate", column = "exchange_rate"),
            @Result(property = "feeCurrency", column = "fee_currency"),
            @Result(property = "fee", column = "fee"),
            @Result(property = "transactionStatus", column = "transaction_status"),
            @Result(property = "transactionType", column = "transaction_type"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "createdUser", column = "created_user"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "updatedUser", column = "updated_user"),
            @Result(property = "deletedAt", column = "deleted_at"),
            @Result(property = "deletedUser", column = "deleted_user"),
            @Result(property = "comments", column = "comments")
    })
    List<Transaction> searchTransactions(@Param("transactionListDTO") TransactionListDTO transactionListDTO);

    @Select("<script>" +
            "SELECT COUNT(*) FROM transactions " +
            "<where>" +
            "  <if test='transactionListDTO.remitterAccount != null'>" +
            "    AND remitter_account = #{transactionListDTO.remitterAccount} " +
            "  </if>" +
            "  <if test='transactionListDTO.beneficiaryAccount != null'>" +
            "    AND beneficiary_account = #{transactionListDTO.beneficiaryAccount} " +
            "  </if>" +
            "  <if test='transactionListDTO.transactionStatus != null'>" +
            "    AND transaction_status = #{transactionListDTO.transactionStatus} " +
            "  </if>" +
            "  <if test='transactionListDTO.startDateTime != null'>" +
            "    AND created_at &gt;= #{transactionListDTO.startDateTime} " +
            "  </if>" +
            "  <if test='transactionListDTO.endDateTime != null'>" +
            "    AND created_at &lt;= #{transactionListDTO.endDateTime} " +
            "  </if>" +
            "</where>" +
            "</script>")
    int countTransactions(@Param("transactionListDTO") TransactionListDTO transactionListDTO);

    @Select("SELECT * FROM transactions WHERE transaction_id = #{transactionId}")
    @ResultMap(value = "transactionMap")
    Transaction findById(String transactionId);

    @Insert({
            "INSERT INTO transactions(transaction_id, remitter_account, from_currency, from_amount, beneficiary_account, to_currency, to_amount, exchange_rate, fee_currency, fee, transaction_status, transaction_type, created_at, created_user, updated_at, updated_user, deleted_at, deleted_user, comments)",
            "VALUES(#{transactionId}, #{remitterAccount}, #{fromCurrency}, #{fromAmount}, #{beneficiaryAccount}, #{toCurrency}, #{toAmount}, #{exchangeRate}, #{feeCurrency}, #{fee}, #{transactionStatus}, #{transactionType}, #{createdAt}, #{createdUser}, #{updatedAt}, #{updatedUser}, #{deletedAt}, #{deletedUser}, #{comments})"
    })
    int insert(Transaction transaction);

//    @Update({
//            "<script>",
//            "UPDATE transactions",
//            "<set>",
//            "  <if test='remitterAccount != null'>remitter_account = #{transaction.remitterAccount},</if>",
//            "  <if test='fromCurrency != null'>from_currency = #{transaction.fromCurrency},</if>",
//            "  <if test='fromAmount != null'>from_amount = #{transaction.fromAmount},</if>",
//            "  <if test='beneficiaryAccount != null'>beneficiary_account = #{transaction.beneficiaryAccount},</if>",
//            "  <if test='toCurrency != null'>to_currency = #{transaction.toCurrency},</if>",
//            "  <if test='toAmount != null'>to_amount = #{transaction.toAmount},</if>",
//            "  <if test='exchangeRate != null'>exchange_rate = #{transaction.exchangeRate},</if>",
//            "  <if test='feeCurrency != null'>fee_currency = #{transaction.feeCurrency},</if>",
//            "  <if test='fee != null'>fee = #{transaction.fee},</if>",
//            "  <if test='transactionStatus != null'>transaction_status = #{transaction.transactionStatus},</if>",
//            "  <if test='transactionType != null'>transaction_type = #{transaction.transactionType},</if>",
//            "  <if test='updatedAt != null'>updated_at = #{transaction.updatedAt},</if>",
//            "  <if test='updatedUser != null'>updated_user = #{transaction.updatedUser},</if>",
//            "  <if test='deletedAt != null'>deleted_at = #{transaction.deletedAt},</if>",
//            "  <if test='deletedUser != null'>deleted_user = #{transaction.deletedUser},</if>",
//            "  <if test='comments != null'>comments = #{transaction.comments},</if>",
//            "</set>",
//            "WHERE transaction_id = #{transaction.transactionId} AND updated_at = #{lastUpdatedAt} </script>"
//    })
//
//    @Update({
//            "<script>",
//            "UPDATE transactions",
//            "<set>",
//            "  <if test='transaction.remitterAccount != null'>remitter_account = #{transaction.remitterAccount},</if>",
//            "  <if test='transaction.fromCurrency != null'>from_currency = #{transaction.fromCurrency},</if>",
//            "  <if test='transaction.fromAmount != null'>from_amount = #{transaction.fromAmount},</if>",
//            "  <if test='transaction.beneficiaryAccount != null'>beneficiary_account = #{transaction.beneficiaryAccount},</if>",
//            "  <if test='transaction.toCurrency != null'>to_currency = #{transaction.toCurrency},</if>",
//            "  <if test='transaction.toAmount != null'>to_amount = #{transaction.toAmount},</if>",
//            "  <if test='transaction.exchangeRate != null'>exchange_rate = #{transaction.exchangeRate},</if>",
//            "  <if test='transaction.feeCurrency != null'>fee_currency = #{transaction.feeCurrency},</if>",
//            "  <if test='transaction.fee != null'>fee = #{transaction.fee},</if>",
//            "  <if test='transaction.transactionStatus != null'>transaction_status = #{transaction.transactionStatus},</if>",
//            "  <if test='transaction.transactionType != null'>transaction_type = #{transaction.transactionType},</if>",
//            "  <if test='transaction.updatedAt != null'>updated_at = #{transaction.updatedAt},</if>",
//            "  <if test='transaction.updatedUser != null'>updated_user = #{transaction.updatedUser},</if>",
//            "  <if test='transaction.deletedAt != null'>deleted_at = #{transaction.deletedAt},</if>",
//            "  <if test='transaction.deletedUser != null'>deleted_user = #{transaction.deletedUser},</if>",
//            "  <if test='transaction.comments != null'>comments = #{transaction.comments},</if>",
//            "</set>",
//            "WHERE transaction_id = #{transaction.transactionId} AND updated_at = #{lastUpdatedAt}",
//            "</script>"
//    })
//    int update(@Param("transaction") Transaction transaction, @Param("lastUpdatedAt") LocalDateTime lastUpdatedAt);



    @Update({
            "<script>",
            "UPDATE transactions",
            "<set>",
            "  <if test='transaction.remitterAccount != null'>remitter_account = #{transaction.remitterAccount},</if>",
            "  <if test='transaction.fromCurrency != null'>from_currency = #{transaction.fromCurrency},</if>",
            "  <if test='transaction.fromAmount != null'>from_amount = #{transaction.fromAmount},</if>",
            "  <if test='transaction.beneficiaryAccount != null'>beneficiary_account = #{transaction.beneficiaryAccount},</if>",
            "  <if test='transaction.toCurrency != null'>to_currency = #{transaction.toCurrency},</if>",
            "  <if test='transaction.toAmount != null'>to_amount = #{transaction.toAmount},</if>",
            "  <if test='transaction.exchangeRate != null'>exchange_rate = #{transaction.exchangeRate},</if>",
            "  <if test='transaction.feeCurrency != null'>fee_currency = #{transaction.feeCurrency},</if>",
            "  <if test='transaction.fee != null'>fee = #{transaction.fee},</if>",
            "  <if test='transaction.transactionStatus != null'>transaction_status = #{transaction.transactionStatus},</if>",
            "  <if test='transaction.transactionType != null'>transaction_type = #{transaction.transactionType},</if>",
            "  <if test='transaction.updatedAt != null'>updated_at = #{transaction.updatedAt},</if>",
            "  <if test='transaction.updatedUser != null'>updated_user = #{transaction.updatedUser},</if>",
            "  <if test='transaction.deletedAt != null'>deleted_at = #{transaction.deletedAt},</if>",
            "  <if test='transaction.deletedUser != null'>deleted_user = #{transaction.deletedUser},</if>",
            "  <if test='transaction.comments != null'>comments = #{transaction.comments},</if>",
            "</set>",
            "WHERE transaction_id = #{transaction.transactionId} AND updated_at = #{lastUpdatedAt}",
            "</script>"
    })
    int update(@Param("transaction") Transaction transaction, @Param("lastUpdatedAt") LocalDateTime lastUpdatedAt);


    // logic to delete?
    @Delete("DELETE FROM transactions WHERE transaction_id = #{transactionId} AND updated_at = #{lastUpdatedAt}")
    int delete(String transactionId, LocalDateTime lastUpdatedAt);

}