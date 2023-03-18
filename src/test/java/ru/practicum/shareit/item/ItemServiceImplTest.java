package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @Mock
    private ItemRepository mockItemRepository;

    private ItemMapper itemMapper;
    private CommentRepository commentRepository;

    @Test
    void shouldExceptionWhenGetItemWithWrongId() {
        ItemService itemService = new ItemServiceImpl(mockItemRepository, null, null,
                null, null, null, null, null);
        when(mockItemRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.getItemById(-1L, 1L));
    }
}
