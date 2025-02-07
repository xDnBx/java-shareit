package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class ItemRepository {
    private long id = 1;
    private final Map<Long, Item> items = new HashMap<>();

    public Item createItem(Item item) {
        item.setId(generateNewId());
        items.put(item.getId(), item);
        log.info("Вещь с id = {} успешно добавлена!", item.getId());
        return item;
    }

    public Item updateItem(Item newItem) {
        items.put(newItem.getId(), newItem);
        log.info("Обновление вещи с id = {} прошло успешно!", newItem.getId());
        return newItem;
    }

    public Item getItemById(Long id) {
        checkItem(id);
        return items.get(id);
    }

    public Collection<Item> getAllItemsByOwner(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .toList();
    }

    private long generateNewId() {
        return id++;
    }

    private void checkItem(Long id) {
        if (!items.containsKey(id)) {
            log.error("Вещи с id = {} не существует", id);
            throw new NotFoundException("Вещь с id = " + id + " не найдена");
        }
    }
}