package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:8432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceImplTestIT {

    private final EntityManager em;
    private final UserService service;

    @Test
    void create_UserWhenUserIsValidThenCreated() {
        User user = User.builder().name("Name").email("mail@mail.ru").build();

        service.create(user);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User actualUser = query.setParameter("email", user.getEmail())
                .getSingleResult();

        assertThat(actualUser.getId(), notNullValue());
        assertThat(actualUser.getName(), equalTo(actualUser.getName()));
        assertThat(actualUser.getEmail(), equalTo(actualUser.getEmail()));
    }

    @Test
    void delete_UserWhenUserIsValidThenCreated() {

        User user = User.builder().name("Name").email("mail@mail.ru").build();

        int usersSize = service.getAll().size();
        service.create(user);

        assertEquals(usersSize + 1, service.getAll().size(),
                "Количество пользователей отличается.");

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User actualUser = query.setParameter("email", user.getEmail())
                .getSingleResult();

        service.delete(actualUser.getId());

        assertEquals(usersSize, service.getAll().size(),
                "Количество пользователей отличается.");
    }
}
