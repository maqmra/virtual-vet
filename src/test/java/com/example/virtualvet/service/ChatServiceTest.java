package com.example.virtualvet.service;

import com.example.virtualvet.exception.ResourceNotFoundException;
import com.example.virtualvet.model.Chat;
import com.example.virtualvet.model.Message;
import com.example.virtualvet.model.Pet;
import com.example.virtualvet.model.Sex;
import com.example.virtualvet.model.Type;
import com.example.virtualvet.model.User;
import com.example.virtualvet.repository.ChatRepository;
import com.example.virtualvet.repository.UserRepository;
import com.example.virtualvet.responder.OpenAIVetResponder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private OpenAIVetResponder vetResponderMock;

    @InjectMocks
    private ChatService chatService;


    @Test
    void shouldReturnChat_whenUserIdAndPetIdExist_forCreateChat() {
        //given
        User testUser = createTestUser();
        testUser.setId(1L);
        Pet testPet = createTestPet();
        testPet.setId(1L);
        testUser.addPet(testPet);
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testUser));
        //when
        chatService.createChat(1L, 1L);

        //then
        ArgumentCaptor<Chat> savedChatCaptor = ArgumentCaptor.forClass(Chat.class);
        verify(chatRepositoryMock).save(savedChatCaptor.capture());
        Chat capturedChat = savedChatCaptor.getValue();
        assertEquals(1L, capturedChat.getUser().getId());
        assertEquals(1L, capturedChat.getPet().getId());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUserIdNotExist_forCreateChat() {
        //given
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            chatService.createChat(1L, 1L);
        });

        //then
        assertEquals("Chat with id 1 not found", exception.getMessage());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenPetIdNotExist_forCreateChat() {
        //given
        User testUser = createTestUser();
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testUser));

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            chatService.createChat(1L, 1L);
        });

        //then
        assertEquals("Pet with id 1 not found", exception.getMessage());
    }

    @Test
    void shouldReturnAnswer_whenChatIdExists_forAsk() {
        //given
        Chat testChat = createTestChat();
        testChat.setId(1L);
        Message testResponse = new Message("Test response message", Type.RESPONSE);
        when(chatRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testChat));
        when(vetResponderMock.answer(any(), any()))
                .thenReturn(testResponse);

        //when
        chatService.ask(1L, "Test question");

        //then
        ArgumentCaptor<Chat> savedChatCaptor = ArgumentCaptor.forClass(Chat.class);
        verify(chatRepositoryMock).save(savedChatCaptor.capture());
        Chat capturedChat = savedChatCaptor.getValue();
        assertEquals(
                List.of(
                        new Message("Test question", Type.QUESTION),
                        new Message("Test response message", Type.RESPONSE)),
                capturedChat.getMessages()
        );
    }

    @Test
    void shouldThrowResourceNotFoundException_whenChatIdNotExist_forAsk() {
        //given
        when(chatRepositoryMock.findById(any()))
                .thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            chatService.ask(1L, "Test question");
        });

        //then
        assertEquals("Chat with id 1 not found", exception.getMessage());
    }

    @Test
    void shouldThrowResourceAlreadyExistsException_when_forAsk() { //TODO: finish
        //given

        //when

        //then
    }

    @Test
    void shouldReturnChat_whenChatIdExists_forGtById() {
        //given
        Chat testChat = createTestChat();
        when(chatRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testChat));

        //when
        Chat foundChat = chatService.getById(1L);

        //then
        assertEquals(testChat, foundChat);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenChatIdNotExist_forGtById() {
        //given
        when(chatRepositoryMock.findById(any()))
                .thenReturn(Optional.of(createTestChat()));

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            Chat foundChat = chatService.getById(1L);
        });

        //then
        assertEquals("Chat with id 1 not found", exception.getMessage());
    }


    @Test
    void shouldReturnChatList_whenUserIdExist_forGetAllChatsForUserByUserId() {
        //given
        User testUser = createTestUser();
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testUser));
        Chat testChat = createTestChat();
        when(chatRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testChat));

        //when
        chatService.getAllChatsForUserByUserId(1L);

        //then

    }

    @Test
    void teest() {
        //given

        //when

        //then
    }

    private static Chat createTestChat() {
        return new Chat(createTestUser(), createTestPet());
    }

    private static User createTestUser() {
        return new User("John", "Lock", "jlock@mail.com");
    }

    private static Pet createTestPet() {
        return new Pet("Molly", "cat", "Ragdoll", LocalDate.of(2020, 1, 30), Sex.FEMALE);
    }

}