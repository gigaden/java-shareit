package ru.practicum.shareit.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidRequestDto() {
        // Создаем корректный RequestDto
        RequestDto requestDto = RequestDto.builder()
                .description("Valid description")
                .requestor(1L)
                .build();

        // Выполняем валидацию
        Set<ConstraintViolation<RequestDto>> violations = validator.validate(requestDto);

        // Проверяем, что нарушений нет
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testBlankDescription() {
        // Создаем RequestDto с пустым значением для description
        RequestDto requestDto = RequestDto.builder()
                .description("")
                .requestor(1L)
                .build();

        // Выполняем валидацию
        Set<ConstraintViolation<RequestDto>> violations = validator.validate(requestDto);

        // Ожидаем, что валидация выдаст ошибку по полю description
        assertEquals(1, violations.size());

        ConstraintViolation<RequestDto> violation = violations.iterator().next();
        assertEquals("must not be blank", violation.getMessage());
        assertEquals("description", violation.getPropertyPath().toString());
    }

    @Test
    public void testWhitespaceDescription() {
        // Создаем RequestDto с пробельным значением для description
        RequestDto requestDto = RequestDto.builder()
                .description("   ")
                .requestor(1L)
                .build();

        // Выполняем валидацию
        Set<ConstraintViolation<RequestDto>> violations = validator.validate(requestDto);

        // Ожидаем, что валидация выдаст ошибку по полю description
        assertEquals(1, violations.size());

        ConstraintViolation<RequestDto> violation = violations.iterator().next();
        assertEquals("must not be blank", violation.getMessage());
        assertEquals("description", violation.getPropertyPath().toString());
    }

    @Test
    public void testValidRequestor() {
        // Создаем RequestDto с корректным requestor
        RequestDto requestDto = RequestDto.builder()
                .description("Valid description")
                .requestor(1L)
                .build();

        // Выполняем валидацию
        Set<ConstraintViolation<RequestDto>> violations = validator.validate(requestDto);

        // Проверяем, что нарушений нет
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNullRequestor() {
        // Создаем RequestDto с null-значением для requestor
        RequestDto requestDto = RequestDto.builder()
                .description("Valid description")
                .requestor(null)
                .build();

        // Выполняем валидацию
        Set<ConstraintViolation<RequestDto>> violations = validator.validate(requestDto);

        // Валидация должна пройти, так как поле requestor не помечено обязательным.
        assertTrue(violations.isEmpty());
    }
}
