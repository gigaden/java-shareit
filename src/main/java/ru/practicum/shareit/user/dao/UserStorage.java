package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> getAll();

    Optional<User> get(long id);

    User create(User user);

    User update(User user);

    void delete(long id);

}
