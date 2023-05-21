package com.example.virtualvet.service;

import com.example.virtualvet.exception.ExceptionMessage;
import com.example.virtualvet.model.Chat;
import com.example.virtualvet.model.Type;
import com.example.virtualvet.repository.ChatRepository;
import com.example.virtualvet.model.Message;
import com.example.virtualvet.model.Pet;
import com.example.virtualvet.model.User;
import com.example.virtualvet.repository.UserRepository;
import com.example.virtualvet.responder.OpenAIVetResponder;
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
    private OpenAIVetResponder vetResponder;


    public Chat createChat(Long userId, Long petId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException(ExceptionMessage.forChatNotFoundById(userId));
        }
        User user = foundUser.get();
        List<Pet> pets = user.getPets();
        if (pets.isEmpty() || pets.stream().map(Pet::getId).noneMatch(foundPetId -> foundPetId.equals(petId))) {
            throw new ResourceNotFoundException(ExceptionMessage.forPetNotFoundById(petId));
        }
        Pet pet = user.findPetById(petId);
        Chat chat = new Chat(user, pet);
        return chatRepository.save(chat);
    }

    public Chat ask(Long chatId, String question) {
        Optional<Chat> foundChat = chatRepository.findById(chatId);
        if (foundChat.isEmpty()) {
            throw new ResourceNotFoundException(ExceptionMessage.forChatNotFoundById(chatId));
        }
        Chat chat = foundChat.get();

        Message newQuestion = new Message(question, Type.QUESTION);

        boolean messageAdded = chat.addMessage(newQuestion);
        if (!messageAdded) {
           throw new ResourceAlreadyExistsException(ExceptionMessage.forMessageIdAlreadyExists());
        }
        Message answer = vetResponder.answer(chat, newQuestion.getMessage());
        chat.addMessage(answer);
        chatRepository.save(chat);
        return chat;
    }

    public List<Chat> getAllChats() {
        return new ArrayList<>(chatRepository.findAll());
    }

    public Chat getById(Long id) {
        Optional<Chat> foundChat = chatRepository.findById(id);
        if (foundChat.isEmpty()) {
            throw new ResourceNotFoundException(ExceptionMessage.forChatNotFoundById(id));
        }
        return foundChat.get();
    }

    public List<Chat> getAllChatsForUserByUserId(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException(ExceptionMessage.forUserNotFoundById(id));
        }
        return chatRepository.findAll().stream().filter(chat -> Objects.equals(chat.getUser().getId(), id)).toList(); //todo replace to custom findBy****
    }

}

