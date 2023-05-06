package com.example.virtualvet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PetController {
    @Autowired
    private PetService petService;

    @PostMapping("/users/{ownerId}/pets")
    public ResponseEntity<Pet> createPet(@PathVariable(name = "ownerId") Long ownerId, @RequestBody Pet pet) {
        Pet createdPet = petService.createPet(ownerId, pet);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getAll() {
        List<Pet> pets = petService.getAll();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<Pet> getById(@PathVariable(name = "id") Long id) {
        Pet pet = petService.getById(id);
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @GetMapping("/users/{id}/pets")
    public ResponseEntity<List<Pet>> getAllByOwnerId(@PathVariable(name = "id") Long id) {
        List<Pet> pets = petService.getAllByOwnerId(id);
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}/pets", params = "name")
    public ResponseEntity<Pet> getByOwnerIdAndPetName(@PathVariable(name = "id") Long id, @RequestParam(name = "name") String name) {
        Pet pet = petService.getByOwnerIdAndPetName(id, name);
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @PutMapping(value = "/pets/{id}")
    public ResponseEntity<Pet> updateById(@PathVariable(name = "id") Long id, @RequestBody Pet pet) {
        Pet updatedPet = petService.updateById(id, pet);
        return new ResponseEntity<>(updatedPet, HttpStatus.OK);
    }

    @PutMapping(value = "/users/{userId}/pets", params = "name")
    public ResponseEntity<Pet> updateByOwnerIdAndPetName(@PathVariable(name = "userId") Long id, @RequestParam(name = "name") String name, @RequestBody Pet pet) {
        Pet updatedPet = petService.updateByOwnerIdAndPetName(id, name, pet);
        return new ResponseEntity<>(updatedPet, HttpStatus.OK);
    }

    @DeleteMapping(value = "/pets/{petId}")
    public ResponseEntity<Long> deleteById(@PathVariable(name = "petId") Long id) {
        petService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/users/{id}/pets", params = "name")
    public ResponseEntity<Long> deleteByUserIdAndPetName(@PathVariable(name = "id") Long id, @RequestParam(name = "name") String name) {
        Long deletedPetId = petService.deleteByUserIdAndPetName(id, name);
        return new ResponseEntity<>(deletedPetId, HttpStatus.NO_CONTENT);
    }
}
