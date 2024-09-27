package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserService {

    Collection<User> getAll();

    User get(long id);

    User create(User user);

    User update(User user);

    User patch(long userId, UserDto userDto);

    void delete(long id);

    void checkUserIsExist(Long id);
}
