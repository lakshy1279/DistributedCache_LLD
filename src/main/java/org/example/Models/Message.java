package org.example.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Message {
    private long id;
    private Map<String, String> properties;
}
