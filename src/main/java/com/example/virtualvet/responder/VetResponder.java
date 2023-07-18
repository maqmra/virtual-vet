package com.example.virtualvet.responder;

import com.example.virtualvet.model.Chat;
import com.example.virtualvet.model.Message;

public interface VetResponder {
    Message answer(Chat chat, String question);
}
