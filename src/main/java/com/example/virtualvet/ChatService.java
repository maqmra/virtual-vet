package com.example.virtualvet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VetResponder vetResponder;


    public Chat createChat(Long userId, Long petId) {
        Optional<User> foundUser = userRepository.findById(userId.toString());
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

    public Chat addMessage(Long chatId, Message message) {
        UUID.randomUUID();
        Optional<Chat> foundChat = chatRepository.findById(chatId);
        if (foundChat.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        Chat chat = foundChat.get();
        boolean messageAdded = chat.addMessage(message);
        if (!messageAdded) {
           throw new ResourceAlreadyExistsException();
        }
        Message answer = vetResponder.answer(chat, message);
        chat.addMessage(answer);
        chatRepository.save(chat);
        return chat; // TODO: check if there is a solution to return answer
    }
}
