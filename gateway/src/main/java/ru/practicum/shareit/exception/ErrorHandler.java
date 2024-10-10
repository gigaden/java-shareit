package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap<String, String> handleValidationNull(final ValidationNullException e, WebRequest request) {
        log.error("Ошибка 400 ValidationNullException: {} в запросе {}",
                e.getMessage(), request.getDescription(false));
        return buildErrorResponse(e.getMessage());
    }

    public HashMap<String, String> buildErrorResponse(String message) {
        HashMap<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }


}