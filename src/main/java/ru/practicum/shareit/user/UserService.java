package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.item.CommentDto;

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
