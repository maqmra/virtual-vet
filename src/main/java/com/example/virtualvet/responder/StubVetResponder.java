package com.example.virtualvet.responder;

import com.example.virtualvet.model.Chat;
import com.example.virtualvet.model.Message;
import com.example.virtualvet.enums.Type;

//@Component
public class StubVetResponder implements VetResponder {
    @Override
    public Message answer(Chat chat, Message question) {
        return new Message("I don't know", Type.RESPONSE);
    }
}
