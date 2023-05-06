package com.example.virtualvet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Chat not found")
public class ResourceNotFoundException extends RuntimeException {
}
