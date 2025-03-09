package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInput;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemServiceImplTest {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    BookingService bookingService;

    @Autowired
    ItemRequestService itemRequestService;

    static UserDto user1;
    static UserDto user2;
    static ItemDtoInput item1;
    static ItemDtoInput item2;

    @BeforeAll
    static void beforeAll() {
        user1 = UserDto.builder().name("Yandex").email("yandex@practicum.ru").build();
        user2 = UserDto.builder().name("Yandex2").email("yandex2@practicum.ru").build();
        item1 = ItemDtoInput.builder().name("Yandex").description("YandexPracticum").available(true).build();
        item2 = ItemDtoInput.builder().name("Yandex2").description("YandexPracticum2").available(true).build();
    }

    @Test
    void shouldCreateAndGetItem() {
        UserDto user3 = userService.createUser(user1);
        ItemDtoRequest item = itemService.createItem(user3.getId(), item1);
        ItemDto getItem = itemService.getItemById(item.getId(), user3.getId());

        assertThat(item.getId()).isEqualTo(getItem.getId());
        assertThat(item.getName()).isEqualTo(getItem.getName());
        assertThat(item.getDescription()).isEqualTo(getItem.getDescription());
        assertThat(item.getAvailable()).isEqualTo(getItem.getAvailable());
        assertThat(item.getOwnerId()).isEqualTo(getItem.getOwnerId());
        assertThat(item.getRequestId()).isNull();
    }

    @Test
    void shouldCreateItemWithRequest() {
        UserDto user3 = userService.createUser(user1);
        UserDto user4 = userService.createUser(user2);
        ItemDtoInput item3 = ItemDtoInput.builder()
                .name("Yandex2")
                .description("YandexPracticum2")
                .available(true)
                .requestId(1L)
                .build();
        ItemRequestDtoInput itemRequest = ItemRequestDtoInput.builder().description("Yandex").build();
        itemRequestService.createItemRequest(user4.getId(), itemRequest);
        ItemDtoRequest item = itemService.createItem(user3.getId(), item3);
        ItemDto getItem = itemService.getItemById(item.getId(), user3.getId());

        assertThat(item.getId()).isEqualTo(getItem.getId());
        assertThat(item.getName()).isEqualTo(getItem.getName());
        assertThat(item.getDescription()).isEqualTo(getItem.getDescription());
        assertThat(item.getAvailable()).isEqualTo(getItem.getAvailable());
        assertThat(item.getOwnerId()).isEqualTo(getItem.getOwnerId());
        assertThat(item.getRequestId()).isEqualTo(getItem.getRequestId());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotFound() {
        assertThatThrownBy(() -> itemService.createItem(100L, item1)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThatThrownBy(() -> itemService.createItem(null, item1)).isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldUpdateItem() {
        UserDto user3 = userService.createUser(user1);
        ItemDtoRequest item = itemService.createItem(user3.getId(), item1);
        ItemDto updateItem = itemService.updateItem(item.getId(),item2, user3.getId());

        assertThat(updateItem.getId()).isEqualTo(item.getId());
        assertThat(updateItem.getName()).isEqualTo(item2.getName());
        assertThat(updateItem.getDescription()).isEqualTo(item2.getDescription());
        assertThat(updateItem.getAvailable()).isEqualTo(item2.getAvailable());
        assertThat(updateItem.getOwnerId()).isEqualTo(user3.getId());
    }

    @Test
    void shouldUpdateItemNameIsNull() {
        UserDto user3 = userService.createUser(user1);
        ItemDtoInput item3 = ItemDtoInput.builder().description("YandexPracticum2").available(false).build();
        ItemDtoRequest item = itemService.createItem(user3.getId(), item1);
        ItemDto updateItem = itemService.updateItem(item.getId(),item3, user3.getId());

        assertThat(updateItem.getId()).isEqualTo(item.getId());
        assertThat(updateItem.getName()).isEqualTo(item1.getName());
        assertThat(updateItem.getDescription()).isEqualTo(item3.getDescription());
        assertThat(updateItem.getAvailable()).isEqualTo(item3.getAvailable());
        assertThat(updateItem.getOwnerId()).isEqualTo(user3.getId());
    }

    @Test
    void shouldUpdateItemDescriptionIsNull() {
        UserDto user3 = userService.createUser(user1);
        ItemDtoInput item3 = ItemDtoInput.builder().name("Yandex2").available(false).build();
        ItemDtoRequest item = itemService.createItem(user3.getId(), item1);
        ItemDto updateItem = itemService.updateItem(item.getId(),item3, user3.getId());

        assertThat(updateItem.getId()).isEqualTo(item.getId());
        assertThat(updateItem.getName()).isEqualTo(item3.getName());
        assertThat(updateItem.getDescription()).isEqualTo(item1.getDescription());
        assertThat(updateItem.getAvailable()).isEqualTo(item3.getAvailable());
        assertThat(updateItem.getOwnerId()).isEqualTo(user3.getId());
    }

    @Test
    void shouldUpdateItemAvaliableIsNull() {
        UserDto user3 = userService.createUser(user1);
        ItemDtoInput item3 = ItemDtoInput.builder().name("Yandex2").description("YandexPracticum2").build();
        ItemDtoRequest item = itemService.createItem(user3.getId(), item1);
        ItemDto updateItem = itemService.updateItem(item.getId(),item3, user3.getId());

        assertThat(updateItem.getId()).isEqualTo(item.getId());
        assertThat(updateItem.getName()).isEqualTo(item3.getName());
        assertThat(updateItem.getDescription()).isEqualTo(item3.getDescription());
        assertThat(updateItem.getAvailable()).isEqualTo(item1.getAvailable());
        assertThat(updateItem.getOwnerId()).isEqualTo(user3.getId());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwnerWhenUpdateItem() {
        UserDto user3 = userService.createUser(user1);
        UserDto user4 = userService.createUser(user2);
        ItemDtoRequest item = itemService.createItem(user3.getId(), item1);

        assertThatThrownBy(() -> itemService.updateItem(item.getId(),item2, user4.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldGetItemsByOwner() {
        UserDto user3 = userService.createUser(user1);
        itemService.createItem(user3.getId(), item1);
        ItemDtoRequest item = itemService.createItem(user3.getId(), item2);

        List<ItemDto> items = itemService.getAllItemsByOwner(user3.getId()).stream().toList();

        assertThat(items).hasSize(2);
        assertThat(items.get(1).getId()).isEqualTo(item.getId());
        assertThat(items.get(1).getName()).isEqualTo(item.getName());
        assertThat(items.get(1).getDescription()).isEqualTo(item.getDescription());
        assertThat(items.get(1).getAvailable()).isEqualTo(item.getAvailable());
        assertThat(items.get(1).getOwnerId()).isEqualTo(user3.getId());
    }

    @Test
    void shouldSearchItems() {
        UserDto user3 = userService.createUser(user1);
        itemService.createItem(user3.getId(), item1);
        ItemDtoRequest item = itemService.createItem(user3.getId(), item2);

        List<ItemDto> items = itemService.searchItems("yandex").stream().toList();

        assertThat(items).hasSize(2);
        assertThat(items.get(1).getId()).isEqualTo(item.getId());
        assertThat(items.get(1).getName()).isEqualTo(item.getName());
        assertThat(items.get(1).getDescription()).isEqualTo(item.getDescription());
        assertThat(items.get(1).getAvailable()).isEqualTo(item.getAvailable());
        assertThat(items.get(1).getOwnerId()).isEqualTo(user3.getId());
    }

    @Test
    void shouldReturnEmptyListWhenSearchItemsTextIsBlank() {
        List<ItemDto> items = itemService.searchItems("").stream().toList();

        assertThat(items).isEmpty();
    }

    @Test
    void shouldCreateComment() {
        UserDto user3 = userService.createUser(user1);
        ItemDtoRequest item = itemService.createItem(user3.getId(), item1);
        BookingDtoInput booking = BookingDtoInput.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusNanos(1))
                .build();
        UserDto user4 = userService.createUser(user2);
        bookingService.createBooking(user4.getId(), booking);
        CommentDto comment = CommentDto.builder().text("Text").build();

        CommentDto newComment = itemService.createComment(item.getId(), user4.getId(), comment);

        assertThat(newComment.getText()).isEqualTo(comment.getText());
        assertThat(newComment.getItemId()).isEqualTo(item.getId());
        assertThat(newComment.getAuthorName()).isEqualTo(user4.getName());
    }

    @Test
    void shouldThrowExceptionWhenOwnerTryToComment() {
        UserDto user3 = userService.createUser(user1);
        ItemDtoRequest item = itemService.createItem(user3.getId(), item1);
        BookingDtoInput booking = BookingDtoInput.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusNanos(1))
                .build();
        UserDto user4 = userService.createUser(user2);
        bookingService.createBooking(user4.getId(), booking);
        CommentDto comment = CommentDto.builder().text("Text").build();

        assertThatThrownBy(() -> itemService.createComment(item.getId(), user3.getId(), comment))
                .isInstanceOf(ValidationException.class);
    }
}