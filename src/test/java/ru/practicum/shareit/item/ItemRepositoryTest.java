package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"/schema.sql"})
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    User user = User
            .builder()
            .name("name")
            .email("email@example.com")
            .build();

    Item item = Item
            .builder()
            .name("item name")
            .description("item description")
            .owner(user)
            .available(true)
            .build();

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();

        user.setId(null);
        user = userRepository.save(user);

        item.setId(null);
        item.setOwner(user);
        item.setRequestId(1L);
    }

    @Test
    public void testSaveItem() {
        Item savedItem = itemRepository.save(item);

        Assertions.assertEquals(1, savedItem.getId());
        Assertions.assertEquals(item.getOwner().getId(), savedItem.getOwner().getId());
    }

    @Test
    public void testFindByOwnerId() {
        itemRepository.save(item);

        List<Item> items = itemRepository.findByOwnerId(
                user.getId(),
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, items.get(0).getId());
        Assertions.assertEquals(item.getOwner().getId(), items.get(0).getOwner().getId());
    }

    @Test
    public void testFindByRequestId() {
        itemRepository.save(item);

        List<Item> items = itemRepository.findByRequestId(1L);

        Assertions.assertEquals(1, items.get(0).getId());
        Assertions.assertEquals(item.getOwner().getId(), items.get(0).getOwner().getId());
    }

    @Test
    public void testGetItemsBySearchQuery() {
        itemRepository.save(item);

        List<Item> items = itemRepository.getItemsBySearchQuery(
                "name",
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, items.get(0).getId());
        Assertions.assertEquals(item.getOwner().getId(), items.get(0).getOwner().getId());
    }
}
