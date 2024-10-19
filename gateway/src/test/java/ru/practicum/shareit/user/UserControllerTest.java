package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    UserClient userClient;

    @Test
    void getAllWhenInvokedThenStatusCodIsOk() {

        Mockito.when(userClient.getAll()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Object> response = userController.getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createWhenUserIsValidThenCreated() {
        long userId = 1L;
        UserDto user = new UserDto(userId, "userName", "email@mail.ru");

        Mockito
                .when(userClient.create(user)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        ResponseEntity<Object> response = userController.create(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


}