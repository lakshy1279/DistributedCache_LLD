package org.example.strategies.retrystrategy;

import org.example.Models.Message;
import org.example.Models.Subscription;
import org.example.exception.RetryLimitExhaustException;

public class ExponentialBackOffRetryStrategy implements RetryStrategy {


    @Override
    public void push(Subscription subscription, Message message, int maxAttempts) throws RetryLimitExhaustException, InterruptedException {
        int currentAttempt = 0;
        int previousSleepMillis = 500;
        while (currentAttempt < maxAttempts) {
            try {
                subscription.getCallBack().call(subscription, message);
                return;
            } catch (Exception e)
            {
                currentAttempt += 1;
                previousSleepMillis *= 2;
                Thread.sleep(previousSleepMillis);
            }
        }
    }
}
