package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto dto);

    ItemDto updateItem(Long itemId, ItemDto dto, Long userId);

    ItemDto getItemById(Long itemId);

    Collection<ItemDto> getAllItemsByOwner(Long id);

//    Collection<ItemDto> searchItems(String text);
}