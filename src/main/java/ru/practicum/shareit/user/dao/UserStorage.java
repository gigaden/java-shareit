package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    public Collection<User> getAll();

    public Optional<User> get(long id);

    public User create(User user);

    public User update(User user);

    public void delete(long id);

}
