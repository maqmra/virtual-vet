package com.example.virtualvet;

import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ChatService chatService;

    @PostMapping("/chats/{userId}/{petId}")// TODO: Add to http status error message USerId/PetId not found(later)
    public ResponseEntity<Chat> createChat(@PathVariable(name = "userId") Long userId, @PathVariable(name = "petId") Long petId) {
        Chat chat = chatService.createChat(userId, petId);
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @PostMapping("/chats/{id}/messages")
    public ResponseEntity<Chat> addMessage(@PathVariable(name = "id") Long chatId, @RequestBody Message message) {
        Chat chat = chatService.addMessage(chatId, message);
        return new ResponseEntity<>(chat, HttpStatus.CREATED);

    }
}
