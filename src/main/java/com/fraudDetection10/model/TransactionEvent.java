package com.fraudDetection10.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEvent {
    private String transactionId;
    private String userId;
    private double amount;
    private String merchant;
    private String location;
    private long timestamp;

    // Fields that will be populated after processing/scoring
    private double fraudScore;
    private boolean isFraudulent;
}