package com.auth.service.exceptions;

public class KafkaConsumerException extends RuntimeException {
    public KafkaConsumerException(String message) {
        super(message);
    }

    public KafkaConsumerException(String message, Throwable cause) {
        super(message, cause);
    }
}