package org.example.strategies.retrystrategy;

import org.example.Models.Message;
import org.example.Models.Subscription;
import org.example.exception.RetryLimitExhaustException;

public interface RetryStrategy {
    void push(Subscription subscription, Message message, int maxAttempts) throws InterruptedException, RetryLimitExhaustException;
}
