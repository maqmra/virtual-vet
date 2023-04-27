package com.example.virtualvet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.print.DocFlavor;

@Controller
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/messages")
    public ResponseEntity<Message> addMessage() {
        return null;
    }
}
