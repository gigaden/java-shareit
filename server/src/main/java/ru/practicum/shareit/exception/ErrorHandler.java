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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HashMap<String, String> handleNotFound(final NotFoundException e, WebRequest request) {
        log.error("Ошибка 404 NotFoundException: {} в запросе {}",
                e.getMessage(), request.getDescription(false));
        return buildErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HashMap<String, String> handleUserNotFound(final UserNotFoundException e, WebRequest request) {
        log.error("Ошибка 404 NotFoundException: {} в запросе {}",
                e.getMessage(), request.getDescription(false));
        return buildErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public HashMap<String, String> handleValidation(final ValidationException e, WebRequest request) {
        log.error("Ошибка 403 ValidationException: {} в запросе {}",
                e.getMessage(), request.getDescription(false));
        return buildErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap<String, String> handleValidationNull(final ValidationNullException e, WebRequest request) {
        log.error("Ошибка 400 ValidationNullException: {} в запросе {}",
                e.getMessage(), request.getDescription(false));
        return buildErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e,
                                                               WebRequest request) {
        log.error("Ошибка 400 MethodArgumentNotValidException: {} в запросе {}",
                e.getMessage(), request.getDescription(false));
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleBookingValidateException(final BookingValidateException e,
                                                        WebRequest request) {
        log.error("Ошибка 400 BookingValidateException: {} в запросе {}",
                e.getMessage(), request.getDescription(false));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public HashMap<String, String> handleEmailUniqueException(final EmailUniqueException e, WebRequest request) {
        log.error("Ошибка 409 EmailUniqueException: {} в запросе {}", e.getMessage(),
                request.getDescription(false));
        return buildErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public HashMap<String, String> handleBookingAccessException(final BookingAccessException e, WebRequest request) {
        log.error("Ошибка 401 BookingAccessException: {} в запросе {}", e.getMessage(),
                request.getDescription(false));
        return buildErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public HashMap<String, String> handleOtherExc(final Throwable e, WebRequest request) {
        log.error("Ошибка 500: {} в запросе {}", e.getMessage(),
                request.getDescription(false));
        return buildErrorResponse(e.getMessage());
    }

    public HashMap<String, String> buildErrorResponse(String message) {
        HashMap<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }


}