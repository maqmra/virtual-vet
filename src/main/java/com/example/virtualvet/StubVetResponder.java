package com.example.virtualvet;

public class StubVetResponder implements VetResponder {
    @Override
    public String ask() {
        return "I don't know";
    }
}
