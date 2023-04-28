package com.example.virtualvet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Controller
public class ChatController {



    @Autowired
    private ChatService chatService;

    @PostMapping("/chats/{userId}/{petId}") // TODO: move to chatService
    public ResponseEntity<Chat> createChat(@PathVariable(name = "userId") Long userId, @PathVariable(name = "petId") Long petId) {
        Optional<User> foundUser = userRepository.findById(userId.toString());
        if (foundUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = foundUser.get();
        List<Pet> pets = user.getPets();
        if (pets.isEmpty() || pets.stream().map(Pet::getId).noneMatch(foundPetId -> foundPetId.equals(petId))) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Pet pet = user.findPetById(petId);
        Chat chat = new Chat(user, pet);
        Chat chatFromRepository = chatRepository.save(chat);
        return new ResponseEntity<>(chatFromRepository, HttpStatus.CREATED);
    }

    @PostMapping("/chats/{id}/messages")
    public ResponseEntity<Message> addMessage(@PathVariable(name = "id") Long chatId, @RequestBody Message message) {
        Optional<Message> addedMessage = chatService.addMessage(chatId, message);
        if (addedMessage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // TODO: how to return (HttpStatus.NOT_FOUND) and (HttpStatus.CONFLICT)
        }
        return new ResponseEntity<>(addedMessage.get(), HttpStatus.CREATED);

//        Optional<Chat> foundChat = chatRepository.findById(chatId);
//        if (foundChat.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        boolean messageAdded = foundChat.get().addMessage(message);
//        if (messageAdded) {
//            chatRepository.save(foundChat.get());
//            return new ResponseEntity<>(message, HttpStatus.CREATED);
//        }
//        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
