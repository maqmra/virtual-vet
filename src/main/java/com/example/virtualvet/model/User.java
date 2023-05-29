package com.example.virtualvet.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false)
    private List<Pet> pets;

    public User(@NonNull String firstName, @NonNull String email) {
        this.firstName = firstName;
        this.email = email;
        this.pets = new ArrayList<>();
    }

    public boolean addPet(Pet pet) {
        if (pets.stream().map(Pet::getName).noneMatch(petName -> Objects.equals(petName, pet.getName()))) {
            pets.add(pet);
            return true;
        }
        return false;
    }

    public void deletePet(Pet pet) {
        if (pets.stream().map(Pet::getId).anyMatch(petId -> Objects.equals(petId, pet.getId()))) {
            pets.remove(pet);
        }
    }

    public Pet findPetById(Long id) {
        if (pets.isEmpty()) {
            return null;
        }
        for (Pet pet : pets) {
            if (Objects.equals(pet.getId(), id)) {
                return pet;
            }
        }
        return null;
    }





}
