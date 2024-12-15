package org.example.Models;

import lombok.Getter;
import lombok.Setter;
import org.example.utils.CallbackMethod;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
public class Subscription {
    private long id;
    private Topic topic;
    private AtomicLong offset;
    private CallbackMethod callBack;

}
