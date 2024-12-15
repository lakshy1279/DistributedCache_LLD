package org.example.Service;

import org.example.Controllers.MessageQueue;
import org.example.Models.Message;
import org.example.Models.Subscription;
import org.example.Models.Topic;
import org.example.exception.RetryLimitExhaustException;
import org.example.strategies.retrystrategy.ExponentialBackOffRetryStrategy;
import org.example.strategies.retrystrategy.RetryStrategy;
import org.example.utils.StripedExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class MessageQueueService {
    private StripedExecutor stripedExecutor;
    private int numberOfThreads = 16;
    private int retryCount = 3;
    private ConcurrentHashMap<Topic, List<Subscription>> subscriptionMap  = new ConcurrentHashMap<>();
    RetryStrategy retryStrategy;
    MessageQueue dlq;
    Topic dlqErrorTopic;
    MessageQueueService() {
        this.stripedExecutor = new StripedExecutor(numberOfThreads);
        this.dlq = new MessageQueue(new MessageQueueService());
        this.dlqErrorTopic = new Topic();
        subscriptionMap = new ConcurrentHashMap<>();
        this.retryStrategy = new ExponentialBackOffRetryStrategy();
    }
    private void publishMessageToQueue(Topic topic, Message message) {
        topic.getMessages().add(message);
        for(Subscription subscription : subscriptionMap.get(topic)) {
            try {
                retryStrategy.push(subscription, message, retryCount);
            } catch (RetryLimitExhaustException | InterruptedException e) {
               dlq.publish(dlqErrorTopic, message);
            } finally {
                subscription.getOffset().incrementAndGet();
            }
        }
    }
    public Future<Void> publish(Topic topic, Message message) {
      return stripedExecutor.submit(topic.getId().hashCode() % numberOfThreads, () -> publishMessageToQueue(topic, message));
    }

    private void handleSubscription(Topic topic, Subscription subscription) {
      subscriptionMap.putIfAbsent(topic, new ArrayList<>());
      subscriptionMap.get(topic).add(subscription);
      for(Message message : topic.getMessages())
      {
          subscription.getCallBack().call(subscription, message);
          subscription.getOffset().incrementAndGet();
      }
    }

    public Future<Void> subscribe(Topic topic, Subscription subscription) {
       return stripedExecutor.submit(topic.getId().hashCode() % numberOfThreads, () -> handleSubscription(topic, subscription));
    }
}
