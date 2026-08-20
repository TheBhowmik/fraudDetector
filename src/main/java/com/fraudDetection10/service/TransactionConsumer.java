package com.fraudDetection10.service;

import com.fraudDetection10.model.TransactionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionConsumer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "raw-transactions", groupId = "fraud-detection-group")
    public void consumeTransaction(TransactionEvent event) {
        // --- Simulated ML / Heuristic Scoring Engine ---
        double fraudScore = calculateFraudScore(event);
        event.setFraudScore(fraudScore);
        event.setFraudulent(fraudScore > 0.75);

        if (event.isFraudulent()) {
            System.out.println("🚨 FRAUD ALERT DETECTED! Transaction ID: " + event.getTransactionId() + " with score: " + fraudScore);
            // Broadcast live alert to frontend WebSockets
            messagingTemplate.convertAndSend("/topic/fraud-alerts", event);
        } else {
            System.out.println("✅ Clean transaction processed: " + event.getTransactionId());
        }

        // Optionally broadcast all transactions to a separate live feed topic
        messagingTemplate.convertAndSend("/topic/live-transactions", event);
    }

    private double calculateFraudScore(TransactionEvent event) {
        double score = 0.1; // Base score

        // Rule 1: High amount increases fraud score significantly
        if (event.getAmount() > 1000) {
            score += 0.6;
        } else if (event.getAmount() > 500) {
            score += 0.3;
        }

        // Rule 2: Specific high-risk locations (mock logic)
        if ("Luxury Boutique".equals(event.getMerchant())) {
            score += 0.2;
        }

        return Math.min(score, 1.0); // Cap at 1.0
    }
}