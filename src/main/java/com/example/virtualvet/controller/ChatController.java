package com.example.virtualvet.controller;

import com.example.virtualvet.model.Chat;
import com.example.virtualvet.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/{userId}/{petId}/chats")
    public ResponseEntity<Chat> createChat(@PathVariable(name = "userId") Long userId, @PathVariable(name = "petId") Long petId) {
        Chat chat = chatService.createChat(userId, petId);
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @PostMapping("/chats/{id}/messages")
    public ResponseEntity<Chat> ask(@PathVariable(name = "id") Long chatId, @RequestBody String question) {
        Chat chat = chatService.ask(chatId, question);
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @GetMapping("/chats")
    public ResponseEntity<List<Chat>> getAll() {
        List<Chat> chats = chatService.getAllChats();
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    @GetMapping("/chats/{id}")
    public ResponseEntity<Chat> getById(@PathVariable(name = "id") Long id) {
        Chat chat = chatService.getById(id);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/users/{id}/chats")
    public ResponseEntity<List<Chat>> getAllForUserByUserId(@PathVariable(name = "id") Long id) {
        List<Chat> chats = chatService.getAllChatsForUserByUserId(id);
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

}
