package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInput;
import ru.practicum.shareit.item.dto.ItemDtoRequest;

import java.util.Collection;

public interface ItemService {
    ItemDtoRequest createItem(Long id, ItemDtoInput dto);

    ItemDto updateItem(Long itemId, ItemDtoInput dto, Long userId);

    ItemDto getItemById(Long itemId, Long userId);

    Collection<ItemDto> getAllItemsByOwner(Long id);

    Collection<ItemDto> searchItems(String text);

    CommentDto createComment(Long itemId, Long userId, CommentDto dto);
}