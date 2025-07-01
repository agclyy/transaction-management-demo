package com.demo.lyy.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * @Author LYY
 * @Date 2025/6/30
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionIdGenerator {

    private final DataSource dataSource;

    private static final int TIMESTAMP_LENGTH = 17;
    private static final int SEQUENCE_NUMBER_LENGTH = 16;

    public String generateFixedLengthId() {

        long sequenceNumber = -1;
        try (final Connection connection = dataSource.getConnection();
             final Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT NEXT VALUE FOR transaction_seq");
            if (rs.next()) {
                sequenceNumber = rs.getLong(1);
            }
        } catch (Exception e) {
            log.error("sequence generation failed:", e);
        }


//        long timestamp = Instant.now().toEpochMilli();
//        String timestampPart = String.format("%017d", timestamp);
        //format it to fix length of 13
        String sequencePart = String.format("%0"+SEQUENCE_NUMBER_LENGTH+"d", sequenceNumber);

        // timestampPart + sequencePart
        return sequencePart;
    }
}

