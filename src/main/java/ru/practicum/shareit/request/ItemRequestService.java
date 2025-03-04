package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(Long userId, ItemRequestDtoInput dto);
}