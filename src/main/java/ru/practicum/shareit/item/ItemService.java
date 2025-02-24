package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(Long id, ItemDto dto);

    ItemDto updateItem(Long itemId, ItemDto dto, Long userId);

    ItemDto getItemById(Long id);

    Collection<ItemDto> getAllItemsByOwner(Long id);

    Collection<ItemDto> searchItems(String text);

    CommentDto createComment(Long itemId, Long userId, CommentDto dto);
}