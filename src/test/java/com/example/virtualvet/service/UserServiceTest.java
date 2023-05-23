package com.example.virtualvet.service;

import com.example.virtualvet.exception.ResourceAlreadyExistsException;
import com.example.virtualvet.exception.ResourceNotFoundException;
import com.example.virtualvet.model.User;
import com.example.virtualvet.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserService userService;


//    static class TestUser extends {
//
//        private String firstName;
//        private String email;
//
//        public TestUser(String firstName, String email) {
//            this.firstName = firstName;
//            this.email = email;
//        }
//    }


    @Test
    void shouldReturnUser_whenUserIdExists_forGetById() {
        //given
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(createTestUser()));

        //when
        User foundUser = userService.getById(1L);

        //then
        assertEquals(createTestUser(), foundUser);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUserIdNotExists_forGetById() { //TODO: double check tests names
        //given
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getById(1L);
        });

        //then
        assertEquals("User with id 1 not found", exception.getMessage());
    }


    @Test
    void shouldSaveUser_whenUserNotExists_forCreate() {
        //given
        when(userRepositoryMock.findByEmail(any()))
                .thenReturn(Optional.empty());

        //when
        User userToSave = createTestUser();
        userService.create(userToSave);

        //then
        verify(userRepositoryMock).save(userToSave);
    }

    @Test
    void shouldThrowResourceAlreadyExistsException_whenUserExists_forCreate() {
        //given
        when(userRepositoryMock.findByEmail(any()))
                .thenReturn(Optional.of(createTestUser()));

        //when
        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.create(createTestUser());
        });

        //then
        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void shouldUpdateUser_whenUserIdExists_forUpdateById() {
        //given
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(createTestUser()));
        User updatedUser = updateTestUser();

        //when
        userService.updateById(1L, updatedUser);

        //then
        ArgumentCaptor<User> savedUserCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryMock).save(savedUserCaptor.capture());
        User capturedUser = savedUserCaptor.getValue();
        assertEquals("Adam", capturedUser.getFirstName());
        assertEquals("abrown@mail.com", capturedUser.getEmail());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUserIdNotExists_forUpdateById() {
        //given
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateById(1L, updateTestUser());
        });

        //then
        assertEquals("User with id 1 not found", exception.getMessage());
    }

    @Test
    void shouldDeleteUser_whenUserIdExists_forDeleteById() {
        //given
        when(userRepositoryMock.existsById(any()))
                .thenReturn(true);

        //when
        userService.deleteById(1L);

        //then
        verify(userRepositoryMock).deleteById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUserIdNotExists_forDeleteById() { //todo check name
        //given
        when(userRepositoryMock.existsById(any()))
                .thenReturn(false);

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteById(1L);
        });

        //then
        assertEquals("User with id 1 not found", exception.getMessage());

    }

    private static User createTestUser() {
        return new User("John", "jlock@mail.com");
    }

    private static User updateTestUser() {
        return new User("Adam", "abrown@mail.com");
    }

}