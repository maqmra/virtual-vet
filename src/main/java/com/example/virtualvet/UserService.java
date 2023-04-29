package com.example.virtualvet;

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
        List<User> users = new ArrayList<>(userRepository.findAll());
        if (users.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return users;
    }

    public User getById(Long id) {
        Optional<User> foundUser = userRepository.findById(id.toString());
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
        if (!userRepository.existsById(id.toString())) {
            throw new ResourceNotFoundException();
        }
        User updatedUser = userRepository.getReferenceById(id.toString());
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        updatedUser = userRepository.save(updatedUser);
        return updatedUser;
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id.toString())) {
            throw new ResourceNotFoundException();
        }
        userRepository.deleteById(id.toString());
    }

}
