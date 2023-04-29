package com.example.virtualvet;

import org.springframework.stereotype.Component;

@Component //TODO: read about it
public class StubVetResponder implements VetResponder {
    @Override
    public Message answer(Chat chat, Message message) {
        return new Message("I don't know", Type.RESPONSE);
    }
}
