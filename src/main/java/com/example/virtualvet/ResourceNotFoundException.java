package com.example.virtualvet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Chat ghjkl;not found")
public class ResourceNotFoundException extends RuntimeException {
}
