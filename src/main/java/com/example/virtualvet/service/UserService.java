package com.example.virtualvet.service;

import com.example.virtualvet.exception.ExceptionMessage;
import com.example.virtualvet.exception.NullReferenceException;
import com.example.virtualvet.exception.ResourceAlreadyExistsException;
import com.example.virtualvet.exception.ResourceNotFoundException;
import com.example.virtualvet.model.User;
import com.example.virtualvet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    public User getById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException(ExceptionMessage.forUserNotFoundById(id));
        }
        return foundUser.get();
    }

    public User create(User user) {
        if (user.getFirstName() == null) {
            throw new NullReferenceException(ExceptionMessage.forUserFirstNameIsNull());
        }
        if (user.getEmail() == null) {
            throw new NullReferenceException(ExceptionMessage.forUserEmailIsNull());
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException(ExceptionMessage.forUserEmailAlreadyExists());
        }
        return userRepository.save(user);
    }

    public User updateById(Long id, User user) {
        if (user.getFirstName() == null) {
            throw new NullReferenceException(ExceptionMessage.forUserFirstNameIsNull());
        }
        if (user.getEmail() == null) {
            throw new NullReferenceException(ExceptionMessage.forUserEmailIsNull());
        }
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException(ExceptionMessage.forUserNotFoundById(id));
        }

        User updatedUser = foundUser.get();
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setEmail(user.getEmail());
        updatedUser = userRepository.save(updatedUser);
        return updatedUser;
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(ExceptionMessage.forUserNotFoundById(id));
        }
        userRepository.deleteById(id);
    }

}
