package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInput;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@Valid @RequestBody ItemDtoInput dto,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на добавление новой вещи: {} пользователю с id = {}", dto.getName(), userId);
        return itemService.createItem(userId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody ItemDtoInput dto,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на обновление вещи с id = {} у пользователя с id = {}", itemId, userId);
        return itemService.updateItem(itemId, dto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение вещи с id = {} у пользователя с id = {}", itemId, userId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDto> getAllItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение списка всех вещей пользователя с id = {}", userId);
        return itemService.getAllItemsByOwner(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на поиск вещей пользователя с id = {} с текстом '{}'", userId, text);
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@Valid @RequestBody CommentDto dto,
                                    @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long itemId) {
        log.info("Запрос на добавление комментария к вещи с id = {} пользователем с id = {}", itemId, userId);
        return itemService.createComment(itemId, userId, dto);
    }

}