package com.example.virtualvet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    @NonNull
    private String email;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Pet> pets;

    public User(String firstName, String lastName, @NonNull String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pets = new ArrayList<>();
    }

    public Pet addPet(Pet pet) {
        if (pets.stream().map(Pet::getId).noneMatch(petId -> Objects.equals(petId, pet.getId()))) {
            pets.add(pet);
            return pet;
        } else {
            return null;
        }
    }
}
