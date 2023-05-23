package com.example.virtualvet.exception;

public class ExceptionMessage {
    public static String forUserNotFoundById(Long id) {
        return "User with id " + id + " not found";
    }

    public static String forPetNotFoundById(Long id) {
        return "Pet with id " + id + " not found";
    }

    public static String forPetNotFoundByName(String name) {
        return "Pet named " + name + " not found";
    }

    public static String forChatNotFoundById(Long id) {
        return "Chat with id " + id + " not found";
    }

    public static String forMessageIdAlreadyExists() {
        return "Message id already exists";
    }

    public static String forPetNameAlreadyExists() {
        return "Pet name already exists";
    }

    public static String forUserEmailAlreadyExists() {
        return "Email already exists";
    }

    public static String forUserFirstNameIsNull() {
        return "User's first name is null";
    }

    public static String forUserEmailIsNull() {
        return "User's email address is null";
    }

    public static String forPetNameIsNull() {
        return "Pet's name is null";
    }

    public static String forPetSpeciesIsNull() {
        return "User's species is null";
    }
}
