package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationNullException;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Service("userServiceImpl")
@Slf4j
public class UserServiceImpl implements UserService {

    @Qualifier("userStorageImpl")
    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Collection<UserDto> getAll() {
        log.info("Получаем коллекцию всех пользователей.");
        Collection<UserDto> users = userStorage.getAll().stream()
                .map(UserMapper::mapToUserDto).collect(Collectors.toList());
        log.info("Пользователи успешно переданы");
        return users;
    }

    @Override
    public UserDto get(long id) {
        log.info("Попытка получить пользователя с id={}", id);
        User user = userStorage.get(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%d не найден", id)));
        log.info("Пользователь с id = {} успешно передан.", id);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public User create(User user) {
        log.info("Попытка создать пользователя");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User newUser = userStorage.create(user);
        log.info("Пользователь с id={} успешно создан.", user.getId());
        return newUser;
    }

    @Override
    public User update(User newUser) {
        log.info("Попытка обновить пользователя.");
        if (newUser.getId() == null) {
            log.warn("не указан Id пользователя.");
            throw new ValidationNullException("Id должен быть указан.");
        }
        get(newUser.getId());
        User user = userStorage.update(newUser);
        log.info("Пользователь с id = {} успешно обновлён", user.getId());
        return user;
    }

    @Override
    public void delete(long id) {
        log.info("Попытка удалить пользователя с id={}.", id);
        get(id);
        userStorage.delete(id);
        log.info("Пользоавтель удалён id={}.", id);
    }
}
