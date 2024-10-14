package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTestIT {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void addUsers() {
        userRepository.save(User.builder().name("Name").email("email@mail.ru").build());
    }

    @AfterEach
    public void deleteUsers() {
        userRepository.deleteAll();
    }

    @Test
    void GetAll_Users() {
        Collection<User> users = userRepository.findAll();
        assertEquals(1, users.size());
    }

}