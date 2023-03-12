package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"/schema.sql"})
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    User user = User
            .builder()
            .name("name")
            .email("email@example.com")
            .build();

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();

        user.setId(null);
    }

    @Test
    public void testSaveUser() {
        User savedUser = userRepository.save(user);

        Assertions.assertEquals(1, savedUser.getId());
        Assertions.assertEquals("name", savedUser.getName());
        Assertions.assertEquals("email@example.com", savedUser.getEmail());
    }

    @Test
    public void testFindByEmail() {
        userRepository.save(user);

        User savedUser = userRepository.findByEmail("email@example.com").get();

        Assertions.assertEquals(1, savedUser.getId());
        Assertions.assertEquals("name", savedUser.getName());
        Assertions.assertEquals("email@example.com", savedUser.getEmail());
    }
}
