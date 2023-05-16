package com.example.virtualvet.service;

import com.example.virtualvet.model.User;
import com.example.virtualvet.repository.UserRepository;
import com.example.virtualvet.exception.ResourceAlreadyExistsException;
import com.example.virtualvet.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    public User getById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return foundUser.get();
    }

    public User create(User user) {
        if (userRepository.findAll().stream().map(User::getEmail).anyMatch(email -> email.equals(user.getEmail()))) {
            throw new ResourceAlreadyExistsException();
        }
        return userRepository.save(user);
    }

    public User updateById(Long id, User user) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }
        User updatedUser = userRepository.getReferenceById(id);
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        updatedUser = userRepository.save(updatedUser);
        return updatedUser;
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }
        userRepository.deleteById(id);
    }

}
