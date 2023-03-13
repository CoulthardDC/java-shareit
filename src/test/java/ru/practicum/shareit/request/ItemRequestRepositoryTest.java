package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"/schema.sql"})
public class ItemRequestRepositoryTest {
    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Autowired
    UserRepository userRepository;

    User user = User
            .builder()
            .name("name")
            .email("email@example.com")
            .build();

    ItemRequest itemRequest = ItemRequest
            .builder()
            .description("request description")
            .requestor(user)
            .created(LocalDateTime.of(2023, 4, 12, 12, 0))
            .build();

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();

        user.setId(null);
        user = userRepository.save(user);

        itemRequest.setId(null);
        itemRequest.setRequestor(user);
    }

    @Test
    public void testSaveRequest() {
        ItemRequest savedRequest = itemRequestRepository.save(itemRequest);

        Assertions.assertEquals(1, savedRequest.getId());
        Assertions.assertEquals(user.getId(), savedRequest.getRequestor().getId());
    }

    @Test
    public void testFindByRequestorId() {
        itemRequestRepository.save(itemRequest);

        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorId(
                user.getId(),
                Sort.by(Sort.Direction.DESC, "created")
        );

        Assertions.assertEquals(1, itemRequests.get(0).getId());
        Assertions.assertEquals(user.getId(), itemRequests.get(0).getRequestor().getId());
    }

    @Test
    public void testFindAllByRequestorIdNot() {
        itemRequestRepository.save(itemRequest);

        Page<ItemRequest> itemRequestsPage = itemRequestRepository.findAllByRequestorIdNot(
                2L,
                PageRequest.of(0, 10)
        );

        List<ItemRequest> itemRequests = itemRequestsPage.getContent();

        Assertions.assertEquals(1, itemRequests.get(0).getId());
        Assertions.assertEquals(user.getId(), itemRequests.get(0).getRequestor().getId());
    }
}
