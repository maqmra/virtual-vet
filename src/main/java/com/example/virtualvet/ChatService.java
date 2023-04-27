package com.example.virtualvet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VetResponder vetResponder;


    public Optional<Message> addMessage(Long chatId, Message message) {
        Optional<Chat> foundChat = chatRepository.findById(chatId);
        if (foundChat.isEmpty()) {
            return Optional.empty();
        }
        Chat chat = foundChat.get();
        boolean messageAdded = chat.addMessage(message);

        if (messageAdded) {//TODO: reverse logic
            Message answer = vetResponder.answer(chat, message);
            chat.addMessage(answer);
            chatRepository.save(chat);

            return Optional.ofNullable(answer);
        }
        return Optional.empty();
    }
}
