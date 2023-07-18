package com.example.virtualvet.responder;

import com.example.virtualvet.model.Chat;
import com.example.virtualvet.model.Message;
import com.example.virtualvet.model.Type;

//@Component
public class StubVetResponder implements VetResponder {
    @Override
    public Message answer(Chat chat, String question) {
        return new Message("I don't know", Type.RESPONSE);
    }
}
