package com.ssafy.home.external.openai.dto.request;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatRequest {
    private String model;
    private List<Message> messages;
    private int n;
//    private double temperature;

    public ChatRequest(String model, String prompt) {
        this.model = model;

        this.n = 1;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }
}
