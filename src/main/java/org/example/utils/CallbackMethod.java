package org.example.utils;

import org.example.Models.Message;
import org.example.Models.Subscription;

public interface CallbackMethod {
    void call(Subscription subscription, Message message);
}
