package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.practicum.shareit.exception.EmailUniqueException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void get_WhenUserIsExistThenReturnedUser() {
        long userId = 0L;
        User expectedUser = new User(0L, "JackBlack", "pentagon@gmail.com");
        Mockito
                .when(userRepository.findById(userId))
                .thenReturn(Optional.of(expectedUser));

        User actualUser = userService.get(userId);

        assertEquals(expectedUser, actualUser, "Вернулся не тот юзер.");
    }

    @Test
    void get_WhenUserNotExistThenUserNotFoundExceptionThrown() {
        long userId = 0L;
        Mockito
                .when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.get(userId),
                "Не выброшено исключение для несуществующего юзера.");
    }

    @Test
    void create_WhenUserEmailIsUniqueThenSavedUser() {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(List.of(new User(0L, "JackBlack", "jack@ya.ru")));

        User userToSave = new User();
        Mockito.when(userRepository.save(userToSave)).thenReturn(userToSave);

        User actualUser = userService.create(userToSave);

        assertEquals(userToSave, actualUser, "Юзеры не совпадают");
        Mockito.verify(userRepository).save(userToSave);
    }

    @Test
    void create_WhenUserEmailIsNotUniqueThenEmailUniqueException() {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(List.of(new User(0L, "JackBlack", "jack@ya.ru")));

        User userToSave = new User(1L, "BlackJack", "jack@ya.ru");

        assertThrows(EmailUniqueException.class, () -> userService.create(userToSave),
                "Не выброшено исключение не уникального email");
        Mockito.verify(userRepository, Mockito.never()).save(userToSave);

    }

    @Test
    void update_UserWhenUserIsExistThenUpdatedOnlyAvailableFIelds() {
        Long userId = 0L;
        User oldUser = new User(userId, "name", "email");
        User newUser = new User(userId, "newName", "newEmail");

        Mockito
                .when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        User actualUser = userService.update(newUser);

        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("newName", savedUser.getName());
        assertEquals("newEmail", savedUser.getEmail());


    }


    @Test
    void update_UserWhenUserIsNotExistThenThrowUserNotFoundException() {
        Long userId = 0L;
        User newUser = new User(userId, "newName", "newEmail");

        Mockito
                .when(userRepository.findById(userId))
                .thenThrow(new UserNotFoundException("Юзер не найден."));

        assertThrows(UserNotFoundException.class, () -> userService.update(newUser),
                "Обновлён несуществующий пользователь.");



    }


    @Test
    void patch_UserWhenUserIsExistThenUpdatedOnlyAvailableFIelds() {
        Long userId = 0L;
        User oldUser = new User(userId, "name", "email");
        UserDto newUser = UserDto.builder().name("newName").build();

        Mockito
                .when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        userService.patch(userId, newUser);

        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("newName", savedUser.getName());
        assertEquals("email", savedUser.getEmail());

    }

    @Test
    void delete_UserWhenUserIsExistThenDeletted() {
        Long userId = 0L;

        User user = new User(userId, "name", "email@mail.ru");
        Mockito
                .when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.delete(userId);

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
    }

    @Test
    void delete_UserWhenUserIsNotExistThenThrow() {
        Long userId = 0L;

        Mockito
                .when(userRepository.findById(userId))
                .thenThrow(new UserNotFoundException("Юзер не найден."));

        final UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.delete(userId));

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepository, Mockito.never()).deleteById(userId);
        Assertions.assertEquals("Юзер не найден.", exception.getMessage());

    }
}