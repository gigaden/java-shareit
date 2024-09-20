package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EmailUniqueException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationNullException;

import java.util.Collection;
import java.util.stream.Collectors;

@Service("userServiceImpl")
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<User> getAll() {
        log.info("Получаем коллекцию всех пользователей.");
        Collection<User> users = userRepository.findAll();
        log.info("Пользователи успешно переданы");
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public User get(long id) {
        log.info("Попытка получить пользователя с id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%d не найден", id)));
        log.info("Пользователь с id = {} успешно передан.", id);
        return user;
    }

    @Override
    public User create(User user) {
        log.info("Попытка создать пользователя");
        emailIsUnique(user.getEmail());
        User newUser = userRepository.save(user);
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
        emailIsUnique(newUser.getEmail());
        User oldUser = get(newUser.getId());
        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        User user = userRepository.save(oldUser);
        log.info("Пользователь с id = {} успешно обновлён", user.getId());
        return user;
    }

    @Override
    public User patch(long userId, UserDto userDto) {
        log.info("Попытка обновить пользователя через patch.");
        User user = get(userId);
        if (userDto.getEmail() != null) {
            emailIsUnique(userDto.getEmail());
        }
        user.setName(userDto.getName() != null ? userDto.getName() : user.getName());
        user.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());
        userRepository.save(user);
        log.info("Пользователь с id = {} успешно обновлён", userId);
        return user;
    }

    @Override
    public void delete(long id) {
        log.info("Попытка удалить пользователя с id={}.", id);
        get(id);
        userRepository.deleteById(id);
        log.info("Пользователь с id={} удалён.", id);
    }

    // Проверяем уникальность email
    private void emailIsUnique(String email) {
        if (!getAll().stream().filter(i -> i.getEmail().equals(email)).toList().isEmpty()) {
            log.warn("Email {} уже существует", email);
            throw new EmailUniqueException(String.format("Email %s уже существует", email));
        }
    }
}
