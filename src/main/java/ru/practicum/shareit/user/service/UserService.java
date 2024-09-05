package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {

    public Collection<UserDto> getAll();

    public UserDto get(long id);

    public User create(User user);

    public User update(User user);

    public void delete(long id);
}
