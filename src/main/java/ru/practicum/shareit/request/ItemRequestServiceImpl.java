package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.util.Pagination;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService{

    private final ItemRequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemRequestServiceImpl(ItemRequestRepository requestRepository,
                                  UserRepository userRepository,
                                  RequestMapper requestMapper,
                                  ItemRepository itemRepository,
                                  ItemMapper itemMapper) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }
    @Override
    public ItemRequestDto addRequest(ItemRequestDto requestDto, Long userId, LocalDateTime created) {
        userExistOrElseThrow(
                userRepository.findById(userId),
                userId);

        ItemRequest itemRequest = requestMapper.toItemRequest(requestDto);
        itemRequest.setCreated(created);
        itemRequest.setId(userId);
        itemRequest.setRequestor(userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)));
        return requestMapper.toItemRequestDto(
                requestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getAllRequestsByUser(Long userId) {
        userExistOrElseThrow(
                userRepository.findById(userId),
                userId);

        List<ItemRequestDto> itemRequestDtoList = requestRepository.findByRequestorId(
                userId,
                Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .map(requestMapper::toItemRequestDto)
                .collect(Collectors.toList());

        setItemsForRequestDto(itemRequestDtoList);
        return itemRequestDtoList;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId, Integer from, Integer size) {
        userExistOrElseThrow(
                userRepository.findById(userId),
                userId
        );
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        Pageable pageable;
        Page<ItemRequest> page;
        Pagination pager = new Pagination(from, size);

        Sort sort = Sort.by(Sort.Direction.DESC, "created");

        if (size == null) {
            List<ItemRequest> listItemRequest = requestRepository.findAllByRequestorIdNotOrderByCreatedDesc(userId);
            itemRequestDtoList.addAll(
                    listItemRequest
                            .stream()
                            .skip(from)
                            .map(requestMapper::toItemRequestDto)
                            .collect(Collectors.toList()));
        } else {
            for (int i = pager.getIndex(); i < pager.getTotalPages(); i++) {
                pageable =
                        PageRequest.of(i, pager.getPageSize(), sort);
                page = requestRepository.findAllByRequestorIdNot(userId, pageable);
                itemRequestDtoList.addAll(page
                        .stream()
                        .map(requestMapper::toItemRequestDto)
                        .collect(Collectors.toList()));
                if (!page.hasNext()) {
                    break;
                }
            }
            itemRequestDtoList = itemRequestDtoList.stream().limit(size).collect(Collectors.toList());
        }
        setItemsForRequestDto(itemRequestDtoList);
        return itemRequestDtoList;
    }

    @Override
    public ItemRequestDto getRequestById (Long requestId, Long userId) {
        userExistOrElseThrow(userRepository.findById(userId), userId);
        ItemRequest itemRequest =  getItemRequestOrElseThrow(
                requestRepository.findById(requestId),
                requestId);
        ItemRequestDto itemRequestDto = requestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(
                itemRepository.findByRequestId(requestId)
                        .stream()
                        .map(itemMapper::toItemDto)
                        .collect(Collectors.toList())
        );
        return itemRequestDto;
    }

    private ItemRequest getItemRequestOrElseThrow(Optional<ItemRequest> optionalItemRequest,
                                                  Long requestId) {
        return optionalItemRequest.orElseThrow(
                () -> new ItemRequestNotFoundException(
                        String.format("Реквест с id = %d не найден", requestId)));
    }

    private void userExistOrElseThrow(Optional<User> optionalUser, Long userId) {
        optionalUser.orElseThrow(
                () -> new UserNotFoundException(userId));
    }

    private void setItemsForRequestDto(List<ItemRequestDto> itemRequestDto) {
        itemRequestDto.forEach(
                (item) -> item.setItems(
                        itemRepository.findByRequestId(item.getId())
                                .stream()
                                .map(itemMapper::toItemDto)
                                .collect(Collectors.toList())
                )
        );
    }
}
