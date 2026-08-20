package com.fraudDetection10.service;

import com.fraudDetection10.model.TransactionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Random;

@Service
public class TransactionProducer {

    private static final String TOPIC = "raw-transactions";

    @Autowired
    private KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    private final Random random = new Random();
    private final String[] merchants = {"Amazon", "Walmart", "Apple Store", "Local Gas Station", "Luxury Boutique"};
    private final String[] locations = {"New York", "London", "Tokyo", "Mumbai", "San Francisco"};

    // Runs automatically every 2 seconds to simulate a live stream of transactions
    @Scheduled(fixedRate = 2000)
    public void sendTransaction() {
        TransactionEvent event = new TransactionEvent();
        event.setTransactionId(UUID.randomUUID().toString());
        event.setUserId("user_" + random.nextInt(100));

        // Occasionally generate a suspiciously large amount to trigger fraud later
        double amount = random.nextDouble() > 0.8 ? random.nextDouble() * 5000 : random.nextDouble() * 200;
        event.setAmount(Math.round(amount * 100.0) / 100.0);

        event.setMerchant(merchants[random.nextInt(merchants.length)]);
        event.setLocation(locations[random.nextInt(locations.length)]);
        event.setTimestamp(System.currentTimeMillis());
        event.setFraudScore(0.0);
        event.setFraudulent(false);

        kafkaTemplate.send(TOPIC, event.getTransactionId(), event);
        System.out.println("Produced transaction: " + event.getTransactionId() + " for amount: $" + event.getAmount());
    }
}