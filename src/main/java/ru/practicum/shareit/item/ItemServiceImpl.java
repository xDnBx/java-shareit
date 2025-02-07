package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto dto) {
        userRepository.getUserById(userId);
        log.debug("Добавление новой вещи с именем: {} пользователю с id = {}", dto.getName(), userId);
        Item item = ItemMapper.toItem(dto);
        item.setOwnerId(userId);
        return ItemMapper.toItemDto(itemRepository.createItem(item));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto dto, Long userId) {
        checkId(itemId);
        Item item = itemRepository.getItemById(itemId);
        if (!item.getOwnerId().equals(userId)) {
            log.error("Пользователь с id = {} не владелец вещи с id = {}", userId, itemId);
            throw new NotFoundException("Редактировать может только владелец вещи");
        }

        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return ItemMapper.toItemDto(itemRepository.updateItem(item));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        checkId(itemId);
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public Collection<ItemDto> getAllItemsByOwner(Long id) {
        return itemRepository.getAllItemsByOwner(id).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    private void checkId(Long id) {
        if (id == null) {
            log.error("id вещи не указан");
            throw new ValidationException("id должен быть указан");
        }
    }
}