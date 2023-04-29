package com.example.virtualvet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    public Pet createPet(Long ownerId, Pet pet) {
        Optional<User> foundOwner = userRepository.findById(ownerId.toString());
        if (foundOwner.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        User owner = foundOwner.get();
        boolean petAdded = owner.addPet(pet);
        if (!petAdded) {
            throw new ResourceAlreadyExistsException();
        }
        List<Pet> petsFromUpdatedListFromUser = userRepository.save(owner).getPets();
        Pet createdPet = null;
        for (Pet petFound : petsFromUpdatedListFromUser) {
            if (Objects.equals(petFound.getName(), pet.getName())) {
                createdPet = petFound;
            }
        }
        return createdPet;
    }

    public List<Pet> getAll() {
        List<Pet> pets = petRepository.findAll();
        if (pets.isEmpty()) {
            throw new ResourceNotFoundException();
        } else {
            return pets;

        }
    }

    public Pet getById(Long id) {
        Optional<Pet> foundPet = petRepository.findById(id);
        if (foundPet.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return foundPet.get();
    }

    public List<Pet> getAllByOwnerId(Long id) {
        Optional<User> foundOwner = userRepository.findById(id.toString());
        if (foundOwner.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        User owner = foundOwner.get();
        return owner.getPets().stream().toList();
    }

    public Pet getByOwnerIdAndPetName(Long ownerId, String petName) {
        Optional<User> foundUser = userRepository.findById(ownerId.toString());
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        List<Pet> pets = foundUser.get().getPets().stream().toList();
        for (Pet pet : pets) {
            if (pet.getName().equalsIgnoreCase(petName)) {
                return pet;
            }
        }
        throw new ResourceNotFoundException();
    }

    public Pet updateById(Long id, Pet pet) {
        Optional<Pet> foundPet = petRepository.findById(id);
        if (foundPet.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        Pet updatedPet = foundPet.get();
        updatedPet.setName(pet.getName());
        updatedPet.setSpecies(pet.getSpecies());
        updatedPet.setBreed(pet.getBreed());
        updatedPet.setDateOfBirth(pet.getDateOfBirth());
        updatedPet.setSex(pet.getSex());
        petRepository.save(updatedPet);
        return updatedPet;
    }

    public Pet updateByOwnerIdAndPetName(Long id, String name, Pet pet) {
        Optional<User> foundUser = userRepository.findById(id.toString());
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        User owner = foundUser.get();
        List<Pet> pets = owner.getPets().stream().toList();
        if (pets.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        for (Pet updatedPet : pets) {
            if (updatedPet.getName().equalsIgnoreCase(name)) {
                updatedPet.setName(pet.getName());
                updatedPet.setSpecies(pet.getSpecies());
                updatedPet.setBreed(pet.getBreed());
                updatedPet.setDateOfBirth(pet.getDateOfBirth());
                updatedPet.setSex(pet.getSex());
                updatedPet = petRepository.save(updatedPet);
                return updatedPet;
            }
        }
        throw new ResourceNotFoundException();
    }

    public void deleteById(Long id) {
        Optional<Pet> foundPet = petRepository.findById(id);
        if (foundPet.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        petRepository.deleteById(id);
    }

    public Long deleteByUserIdAndPetName(Long id, String name) {
        Optional<User> foundUser = userRepository.findById(id.toString());
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException();

        }
        User owner = foundUser.get();
        List<Pet> pets = owner.getPets().stream().toList();
        if (pets.isEmpty()) {
            throw new ResourceNotFoundException();

        }
        Long deletedPetId;
        for (Pet pet : pets) {
            if (pet.getName().equalsIgnoreCase(name)) {
                deletedPetId = pet.getId();
                owner.deletePet(pet);
                userRepository.save(owner);
                return deletedPetId;
            }
        }
        throw new ResourceNotFoundException();
    }
}

