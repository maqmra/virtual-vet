package com.example.virtualvet.service;

import com.example.virtualvet.exception.ResourceAlreadyExistsException;
import com.example.virtualvet.exception.ResourceNotFoundException;
import com.example.virtualvet.model.Pet;
import com.example.virtualvet.model.Sex;
import com.example.virtualvet.model.User;
import com.example.virtualvet.repository.PetRepository;
import com.example.virtualvet.repository.UserRepository;
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
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PetRepository petRepositoryMock;

    @InjectMocks
    private PetService petService;

    @Test
    void shouldReturnPet_whenUserIdExistsAndPetNameNotExists_forCreatePet() {
        //given
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(createTestUser()));
        when(userRepositoryMock.save(any()))
                .then(returnsFirstArg());

        //when
        petService.createPet(1L, createTestPet());

        //then
        ArgumentCaptor<User> savedUserCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryMock).save(savedUserCaptor.capture());
        Pet capturedPet = savedUserCaptor.getValue().getPets().get(0);
        assertEquals("Molly", capturedPet.getName());
        assertEquals("cat", capturedPet.getSpecies());
        assertEquals("Ragdoll", capturedPet.getBreed());
        assertEquals(LocalDate.of(2020, 1, 30), capturedPet.getDateOfBirth());
        assertEquals(Sex.FEMALE, capturedPet.getSex());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUserIdNotExists_forCreatePet() {
        //given
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.createPet(1L, createTestPet());
        });

        //then
        assertEquals("User with id 1 not found", exception.getMessage());
    }

    @Test
    void shouldThrowResourceAlreadyExistsException_whenPetNameExists_forCreatePet() {
        //given
        User testUser = createTestUser();
        testUser.addPet(createTestPet());
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testUser));

        //when
        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            petService.createPet(1L, new Pet(
                    "Molly",
                    "dog",
                    "Greyhound",
                    LocalDate.of(2018, 5, 10),
                    Sex.FEMALE)
            );
        });

        //then
        assertEquals("Pet name already exists", exception.getMessage());
    }

    @Test
    void shouldReturnPet_whenPetIdExists_forGetById() {
        //given
        when(petRepositoryMock.findById(any()))
                .thenReturn(Optional.of(createTestPet()));

        //when
        Pet foundPet = petService.getById(1L);

        //then
        assertEquals(createTestPet(), foundPet);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenPetIdNotExists_forGetById() {
        //given
        when(petRepositoryMock.findById(any()))
                .thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.getById(1L);
        });

        //then
        assertEquals("Pet with id 1 not found", exception.getMessage());
    }

    @Test
    void shouldReturnPets_whenUserIdExists_forGerAllByOwnerId() {
        //given
        User testUser = createTestUser();
        testUser.addPet(createTestPet());
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testUser));

        //when
        Pet foundPet = petService.getAllByOwnerId(1L).get(0);

        //then
        assertEquals(createTestPet(), foundPet);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUserIdNotFound_forGetAllByOwnerId() {
        //given
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.getAllByOwnerId(1L);
        });

        //then
        assertEquals("User with id 1 not found", exception.getMessage());
    }

    @Test
    void shouldReturnPet_whenOwnerIdAndPetNameExist_forGetByOwnerIdAndPetName() {
        //given
        User testUser = createTestUser();
        testUser.addPet(createTestPet());
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testUser));

        //when
        Pet foundPet = petService.getByOwnerIdAndPetName(1L, "Molly");

        //then
        assertEquals(createTestPet(), foundPet);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUserIdNotExists_forGetByOwnerIdAndPetName() {
        //given
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.getByOwnerIdAndPetName(1L, "Molly");
        });

        //then
        assertEquals("User with id 1 not found", exception.getMessage());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenPetNameNotExists_forGetByOwnerIdAndPetName() {
        //given
        User testUser = createTestUser();
        testUser.addPet(createTestPet());
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testUser));

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.getByOwnerIdAndPetName(1L, "Rex");
        });

        //then
        assertEquals("Pet named Rex not found", exception.getMessage());
    }

    @Test
    void shouldUpdatePet_whenPetIdExists_forUpdateById() {
        //given
        when(petRepositoryMock.findById(any()))
                .thenReturn(Optional.of(createTestPet()));

        //when
        petService.updateById(1L, new Pet(
                "Molly",
                "cat",
                "Ragdoll",
                LocalDate.of(2020, 1, 30),
                Sex.FEMALE));

        //then
        ArgumentCaptor<Pet> savedPetCaptor = ArgumentCaptor.forClass(Pet.class);
        verify(petRepositoryMock).save(savedPetCaptor.capture());
        Pet capturedPet = savedPetCaptor.getValue();
        assertEquals("Polly", capturedPet.getName());
        assertEquals("cat", capturedPet.getSpecies());
        assertEquals("Ragdoll", capturedPet.getBreed());
        assertEquals(LocalDate.of(2020, 1, 30), capturedPet.getDateOfBirth());
        assertEquals(Sex.FEMALE, capturedPet.getSex());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenPetIdNotExists_forUpdateById() {
        //given
        when(petRepositoryMock.findById(any()))
                .thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.updateById(1L, new Pet(
                    "Polly",
                    "cat",
                    "Ragdoll",
                    LocalDate.of(2020, 1, 30),
                    Sex.FEMALE));
        });

        //then
        assertEquals("Pet with id 1 not found", exception.getMessage());
    }

    @Test
    void shouldUpdatePet_whenOwnerIdAndPetNameExist_forUpdateByOwnerIdAndPetName() {
        //given
        User testUser = createTestUser();
        testUser.addPet(createTestPet());
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testUser));

        //when
        petService.updateByOwnerIdAndPetName(1L, "Molly", new Pet(
                "Polly",
                "cat",
                "Ragdoll",
                LocalDate.of(2020, 1, 30),
                Sex.FEMALE));

        //then
        ArgumentCaptor<Pet> savedPetCaptor = ArgumentCaptor.forClass(Pet.class);
        verify(petRepositoryMock).save(savedPetCaptor.capture());
        Pet capturedPet = savedPetCaptor.getValue();
        assertEquals("Polly", capturedPet.getName());
        assertEquals("cat", capturedPet.getSpecies());
        assertEquals("Ragdoll", capturedPet.getBreed());
        assertEquals(LocalDate.of(2020, 1, 30), capturedPet.getDateOfBirth());
        assertEquals(Sex.FEMALE, capturedPet.getSex());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUSerIdNotExists_forUpdateByOwnerIdAndPetName() {
        //given
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.updateByOwnerIdAndPetName(1L, "Molly", new Pet(
                    "Polly",
                    "cat",
                    "Ragdoll",
                    LocalDate.of(2020, 1, 30),
                    Sex.FEMALE));
        });

        //then
        assertEquals("User with id 1 not found", exception.getMessage());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenPetNameNotExists_forUpdateByOwnerIdAndPetName() {
        //given
        User testUser = createTestUser();
        testUser.addPet(createTestPet());
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testUser));

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.updateByOwnerIdAndPetName(1L, "Rex", new Pet(
                    "Polly",
                    "cat",
                    "Ragdoll",
                    LocalDate.of(2020, 1, 30),
                    Sex.FEMALE));
        });

        //then
        assertEquals("Pet named Rex not found", exception.getMessage());
    }

    @Test
    void shouldDeletePet_whenPetIdExists_forDeleteById() {
        //given
        when(petRepositoryMock.findById(any()))
                .thenReturn(Optional.of(createTestPet()));

        //when
        petService.deleteById(1L);

        //then
        verify(petRepositoryMock).deleteById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenPetIdNotExist_forDeleteById() {
        //given
        when(petRepositoryMock.findById(any()))
                .thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.deleteById(1L);
        });

        //then
        assertEquals("Pet with id 1 not found", exception.getMessage());
    }

    @Test
    void shouldDeletePet_whenUserIdAndPetNameExist_forDeleteByUserIdAndPetName() {
        //given
        User testUser = createTestUser();
        testUser.addPet(createTestPet());
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testUser));

        //when
        petService.deleteByUserIdAndPetName(1L, "Molly");

        //then
        ArgumentCaptor<User> savedUserCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryMock).save(savedUserCaptor.capture());
        User capturedUser = savedUserCaptor.getValue();
        assertEquals(List.of(), capturedUser.getPets());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUserIdNotExist_forDeleteByUserIdAndPetName() {
        //given
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.empty());

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.deleteByUserIdAndPetName(1L, "Molly");
        });

        //then
        assertEquals("User with id 1 not found", exception.getMessage());
    }

    @Test
    void shouldReturnResourceNotFoundException_whenPetNameNotExist_forDeleteByUserIdAndPetName() {
        //given
        User testUser = createTestUser();
        testUser.addPet(createTestPet());
        when(userRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testUser));

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.deleteByUserIdAndPetName(1L, "Polly");
        });

        //then
        assertEquals("Pet named Polly not found", exception.getMessage());
    }

    private static User createTestUser() {
        return new User("John", "Lock", "jlock@mail.com");
    }

    private static Pet createTestPet() {
        return new Pet("Molly", "cat", "Ragdoll", LocalDate.of(2020, 1, 30), Sex.FEMALE);
    }

}