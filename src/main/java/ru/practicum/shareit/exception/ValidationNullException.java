package ru.practicum.shareit.exception;

public class ValidationNullException extends RuntimeException {
    public ValidationNullException(String message) {
        super(message);
    }
}