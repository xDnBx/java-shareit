package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItemRequest(@Valid @RequestBody ItemRequestDto dto,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на добавление нового запроса вещи: '{}' пользователя с id = {}", dto.getDescription(), userId);
        return itemRequestClient.createItemRequest(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsByRequestor(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение списка всех запросов пользователя с id = {}", userId);
        return itemRequestClient.getAllItemRequestsByRequestor(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests() {
        log.info("Запрос на получение списка всех запросов, созданных другими пользователями");
        return itemRequestClient.getAllItemRequests();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable Long requestId) {
        log.info("Запрос на получение запроса с id = {}", requestId);
        return itemRequestClient.getItemRequestById(requestId);
    }
}