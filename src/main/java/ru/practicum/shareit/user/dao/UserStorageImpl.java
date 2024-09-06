package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository("userStorageImpl")
@Slf4j
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();


    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        User oldUser = users.get(newUser.getId());
        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        return oldUser;
    }

    @Override
    public User patch(long userId, UserDto userDto) {
        User user = users.get(userId);
        user.setName(userDto.getName() != null ? userDto.getName() : user.getName());
        user.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());
        return user;
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }


    // Метод для генерации идентификатора
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
