package com.example.virtualvet.service;

import com.example.virtualvet.model.Chat;
import com.example.virtualvet.model.Type;
import com.example.virtualvet.repository.ChatRepository;
import com.example.virtualvet.model.Message;
import com.example.virtualvet.model.Pet;
import com.example.virtualvet.model.User;
import com.example.virtualvet.repository.UserRepository;
import com.example.virtualvet.responder.VetResponder;
import com.example.virtualvet.exception.ResourceAlreadyExistsException;
import com.example.virtualvet.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VetResponder vetResponder;


    public Chat createChat(Long userId, Long petId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        User user = foundUser.get();
        List<Pet> pets = user.getPets();
        if (pets.isEmpty() || pets.stream().map(Pet::getId).noneMatch(foundPetId -> foundPetId.equals(petId))) {
            throw new ResourceNotFoundException();
        }
        Pet pet = user.findPetById(petId);
        Chat chat = new Chat(user, pet);
        return chatRepository.save(chat);
    }

    public Chat ask(Long chatId, String question) {
        Optional<Chat> foundChat = chatRepository.findById(chatId);
        if (foundChat.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        Chat chat = foundChat.get();

        Message newQuestion = new Message(question, Type.QUESTION);

        boolean messageAdded = chat.addMessage(newQuestion);
        if (!messageAdded) {
           throw new ResourceAlreadyExistsException();
        }
        Message answer = vetResponder.answer(chat, newQuestion);
        chat.addMessage(answer);
        chatRepository.save(chat);
        return chat;
    }

    public List<Chat> getAllChats() {
        return new ArrayList<>(chatRepository.findAll());
    }

    public Chat getAllChatsById(Long id) {
        Optional<Chat> foundChat = chatRepository.findById(id);
        if (foundChat.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return foundChat.get();
    }

    public List<Chat> getAllChatsForUserByUserId(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return chatRepository.findAll().stream().filter(chat -> Objects.equals(chat.getUser().getId(), id)).toList();
    }
}

