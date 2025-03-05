package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Long userId, ItemDto dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> updateItem(Long itemId, ItemDto dto, Long userId) {
        return patch("/" + itemId, userId, dto);
    }

    public ResponseEntity<Object> getItemById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllItemsByOwner(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItems(String text, Long userId) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> createComment(Long itemId, Long userId, CommentDto dto) {
        return post("/" + itemId + "/comment", userId, dto);
    }
}