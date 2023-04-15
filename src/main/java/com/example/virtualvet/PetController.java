package com.example.virtualvet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
        if (foundOwner.isPresent()) {
            User owner = foundOwner.get();
            owner.addPet(pet);
            userRepository.save(owner);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
        if (foundOwner.isPresent()) {
            User owner = foundOwner.get();
            List<Pet> pets = owner.getPets().stream().toList();
            return new ResponseEntity<>(pets, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
