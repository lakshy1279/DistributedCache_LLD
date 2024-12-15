package org.example.Controllers;

import org.example.Models.Message;
import org.example.Models.Subscription;
import org.example.Models.Topic;
import org.example.Service.MessageQueueService;

import java.util.concurrent.Future;

public class MessageQueue {

    private MessageQueueService messageQueueService;

    public MessageQueue(MessageQueueService messageQueueService) {
        this.messageQueueService = messageQueueService;
    }

    public Future<Void> publish(Topic topic, Message message) {
      return messageQueueService.publish(topic, message);
    }


    public Future<Void> subscribe(Subscription subscription, Topic topic) {
        return messageQueueService.subscribe(topic, subscription);
    }
}
