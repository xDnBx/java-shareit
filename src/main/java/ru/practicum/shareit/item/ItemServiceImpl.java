package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto dto) {
        User itemOwner = UserMapper.toUser(getUserById(userId));
        log.debug("Добавление новой вещи с именем: {} пользователю с id = {}", dto.getName(), userId);
        Item item = ItemMapper.toItem(dto);
        item.setOwner(itemOwner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto dto, Long userId) {
        checkId(itemId);
        Item item = ItemMapper.toItem(getItemById(itemId));
        if (!item.getOwner().getId().equals(userId)) {
            log.error("Пользователь с id = {} не владелец вещи с id = {}", userId, itemId);
            throw new NotFoundException("Редактировать может только владелец вещи");
        }
        log.debug("Обновление вещи с id = {} пользователя с id = {}", itemId, userId);
        if (dto.getName() != null && !dto.getName().isBlank()) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        checkId(itemId);
        log.debug("Получение вещи с id = {}", itemId);
        return ItemMapper.toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена")));
    }

    @Override
    public Collection<ItemDto> getAllItemsByOwner(Long id) {
        log.debug("Получение списка всех вещей пользователя с id = {}", id);
        return itemRepository.findAllByOwner(UserMapper.toUser(getUserById(id))).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        log.debug("Получение списка доступных вещей с текстом '{}'", text);
        return itemRepository.searchItemsByText(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    private void checkId(Long id) {
        if (id == null) {
            log.error("id не указан");
            throw new ValidationException("id должен быть указан");
        }
    }

    private UserDto getUserById(Long id) {
        checkId(id);
        log.debug("Получение пользователя с id = {}", id);
        return UserMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден")));
    }
}