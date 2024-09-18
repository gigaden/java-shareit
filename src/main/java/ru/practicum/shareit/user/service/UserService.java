package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> getAll();

    UserDto get(long id);

    User create(User user);

    User update(User user);

    User patch(long userId, UserDto userDto);

    void delete(long id);
}
