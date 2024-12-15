package org.example.strategies.retrystrategy;

import org.example.Models.Message;
import org.example.Models.Subscription;

public class ContantIntervalRetryStrategy implements RetryStrategy {


    @Override
    public void push(Subscription subscription, Message message, int maxAttempts) {
        int currentAttempt = 0;
        while (currentAttempt < maxAttempts) {
            try{
               subscription.getCallBack().call(subscription, message);
               return;
            } catch(Exception e) {
               currentAttempt++;
            }
        }
    }
}
