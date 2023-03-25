package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

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
                () -> new NotFoundException(userId)));
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
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);

        List<ItemRequestDto> itemRequestDtoList = requestRepository.findAllByRequestorIdNot(userId, pageRequest)
                        .stream()
                        .map(requestMapper::toItemRequestDto)
                        .collect(Collectors.toList());
        setItemsForRequestDto(itemRequestDtoList);
        return itemRequestDtoList;
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId, Long userId) {
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
                () -> new NotFoundException(requestId));
    }

    private void userExistOrElseThrow(Optional<User> optionalUser, Long userId) {
        optionalUser.orElseThrow(
                () -> new NotFoundException(userId));
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
