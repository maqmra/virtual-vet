package com.example.virtualvet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:8080")
@Controller
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/users/{ownerId}/pets")
    public ResponseEntity<Pet> createPet(@PathVariable(value = "ownerId") Long ownerId, @RequestBody Pet pet) {
        Optional<User> foundOwner = userRepository.findById(ownerId.toString());
        if (foundOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User owner = foundOwner.get();
        boolean added = owner.addPet(pet);
        if (added)
        {
            userRepository.save(owner);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }


    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getAll() {
        List<Pet> pets = petRepository.findAll();
        if (!pets.isEmpty()) {
            return new ResponseEntity<>(pets, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/pets/{petId}")
    public ResponseEntity<Pet> getById(@PathVariable(value = "petId") Long petId) {
        Optional<Pet> foundPet = petRepository.findById(petId.toString());
        if (foundPet.isPresent()) {
            Pet pet = foundPet.get();
            return new ResponseEntity<>(pet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{ownerId}/pets")
    public ResponseEntity<List<Pet>> getAllByOwnerId(@PathVariable(value = "ownerId") Long ownerId) {
        Optional<User> foundOwner = userRepository.findById(ownerId.toString());
        if (foundOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User owner = foundOwner.get();
        List<Pet> pets = owner.getPets().stream().toList();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{ownerId}/pets", params = "name")
    public ResponseEntity<Pet> getByPetNameAndOwnerId(@PathVariable(value = "ownerId") Long ownerId, @RequestParam(name = "name") String petName) {
        Optional<User> foundUser = userRepository.findById(ownerId.toString());
        if (foundUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User owner = foundUser.get();
        List<Pet> pets = owner.getPets().stream().toList();
        if (pets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (Pet pet : pets) {
            if (pet.getName().equalsIgnoreCase(petName)) {
                return new ResponseEntity<>(pet, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/pets/{petId}")
    public ResponseEntity<Pet> updateById(@PathVariable(value = "petId") Long petId, @RequestBody Pet pet) {
        Optional<Pet> foundPet = petRepository.findById(petId.toString());
        if (foundPet.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Pet updatedPet = foundPet.get();
        updatedPet.setName(pet.getName());
        updatedPet.setSpecies(pet.getSpecies());
        updatedPet.setBreed(pet.getBreed());
        updatedPet.setDateOfBirth(pet.getDateOfBirth());
        updatedPet.setSex(pet.getSex());
        petRepository.save(updatedPet);
        return new ResponseEntity<>(updatedPet, HttpStatus.OK);
    }

    @PutMapping(value = "/users/{userId}/pets", params = "name")
    public ResponseEntity<Pet> updateByPetNameAndOwnerId(@PathVariable(value = "userId") Long userId, @RequestParam(name = "name") String petName, @RequestBody Pet pet) {
        Optional<User> foundUser = userRepository.findById(userId.toString());
        if (foundUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User owner = foundUser.get();
        List<Pet> pets = owner.getPets().stream().toList();
        if (pets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (Pet updatedPet : pets) {
            if (updatedPet.getName().equalsIgnoreCase(petName)) {
                updatedPet.setName(pet.getName());
                updatedPet.setSpecies(pet.getSpecies());
                updatedPet.setBreed(pet.getBreed());
                updatedPet.setDateOfBirth(pet.getDateOfBirth());
                updatedPet.setSex(pet.getSex());
                petRepository.save(updatedPet);
                return new ResponseEntity<>(updatedPet, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/pets/{petId}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable(name = "petId") Long petId) {
        Optional<Pet> foundPet = petRepository.findById(petId.toString());
        if (foundPet.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        petRepository.deleteById(petId.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{userId}/pets", params = "name")
    public ResponseEntity<HttpStatus> deleteByPetIdAndUserName(@PathVariable(value = "userId") Long userId, @RequestParam(name = "name") String petName) {

        Optional<User> foundUser = userRepository.findById(userId.toString());
        if (foundUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User owner = foundUser.get();
        List<Pet> pets = owner.getPets().stream().toList();
        if (pets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (Pet pet : pets) {
            if (pet.getName().equalsIgnoreCase(petName)) {
                owner.deletePet(pet);
                userRepository.save(owner);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
