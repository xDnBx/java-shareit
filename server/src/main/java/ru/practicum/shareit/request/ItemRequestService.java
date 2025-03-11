package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(Long userId, ItemRequestDtoInput dto);

    Collection<ItemRequestDto> getAllItemRequestsByRequestor(Long userId);

    Collection<ItemRequestDto> getAllItemRequests();

    ItemRequestDto getItemRequestById(Long requestId);
}