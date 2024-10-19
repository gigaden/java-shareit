package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.EmailUniqueException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void getAllWhenInvokedThenUserCollectionIsExist() {

        assertTrue(userController.getAll().isEmpty(), "Список юзеров не пуст");

        Collection<UserDto> expectedUsers = List.of(new UserDto(1L, "Bob", "bob@ya.ru"));
        Mockito.when(userService.getAll())
                .thenReturn(List.of(new User(1L, "Bob", "bob@ya.ru")));

        Collection<UserDto> response = userController.getAll();

        assertEquals(expectedUsers, response, "Вернулся неверный список юзеров.");

    }

    @Test
    void get_WhenUserIsExistThenUserReturned() {
        long userId = 1L;
        User user = new User(userId, "name", "mail@mail.ru");

        Mockito
                .when(userService.get(userId)).thenReturn(user);

        UserDto userDto = userController.get(userId);

        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void get_WhenUserIsNotExistThenThrownUserNotFoundException() {
        long userId = 1L;
        User user = new User(userId, "name", "mail@mail.ru");

        Mockito
                .when(userService.get(userId))
                .thenThrow(new UserNotFoundException(String.format("Пользователь с id=%d не найден", userId)));

        assertThrows(UserNotFoundException.class, () -> userController.get(userId));
    }

    @Test
    void create_UserWhenEmailIsUniqueThenCreated() {
        User user = User.builder().name("name").email("mail@mail.ru").build();

        Mockito
                .when(userService.create(user)).thenReturn(user);

        User userActual = userController.create(user);

        assertEquals(user.getName(), userActual.getName());
        assertEquals(user.getEmail(), userActual.getEmail());
    }

    @Test
    void create_UserWhenEmailIsNotUniqueThenThrownEmailUniqueException() {
        User user = User.builder().name("name").email("wrong_email").build();

        Mockito
                .when(userService.create(user)).thenThrow(new EmailUniqueException("Email is not unique."));

        assertThrows(EmailUniqueException.class, () -> userController.create(user));
    }

    @Test
    void delete_UserWhenUserIsExistThenDeleted() {
        long userId = 0L;
        User user = User.builder().id(userId).name("name").email("mail@mail.ru").build();

        Mockito.doNothing().when(userService).delete(userId);

        userController.delete(userId);

        Mockito.verify(userService, Mockito.times(1)).delete(userId);
    }


}