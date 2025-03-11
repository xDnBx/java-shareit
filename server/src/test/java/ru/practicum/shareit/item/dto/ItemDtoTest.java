package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws Exception {
        UserDto user = UserDto.builder().id(1L).name("Name").email("yandex@practicum.ru").build();
        CommentDto comment = CommentDto.builder()
                .id(1L)
                .text("Text")
                .itemId(1L)
                .authorName("Name")
                .created(LocalDateTime.of(2025, 3, 9, 16,0))
                .build();
        BookingDto lastBooking = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2025, 3, 9, 12,0))
                .end(LocalDateTime.of(2025, 3, 9, 14,0))
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        BookingDto nextBooking = BookingDto.builder()
                .id(2L)
                .start(LocalDateTime.of(2025, 3, 10, 12,0))
                .end(LocalDateTime.of(2025, 3, 10, 14,0))
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        var dto = ItemDto.builder()
                .id(1L)
                .name("Yandex")
                .description("YandexPracticum")
                .available(true)
                .ownerId(1L)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(List.of(comment))
                .requestId(1L)
                .build();

        var result = json.write(dto);

        assertThat(result)
                .hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.description")
                .hasJsonPath("$.available")
                .hasJsonPath("$.ownerId")
                .hasJsonPath("$.lastBooking")
                .hasJsonPath("$.nextBooking")
                .hasJsonPath("$.comments")
                .hasJsonPath("$.requestId");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(dto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(dto.getOwnerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id")
                .isEqualTo(dto.getLastBooking().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.start")
                .isEqualTo("2025-03-09T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.end")
                .isEqualTo("2025-03-09T14:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id")
                .isEqualTo(dto.getNextBooking().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.start")
                .isEqualTo("2025-03-10T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.end")
                .isEqualTo("2025-03-10T14:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id")
                .isEqualTo(comment.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo(comment.getText());
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].itemId")
                .isEqualTo(comment.getItemId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName")
                .isEqualTo(comment.getAuthorName());
        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(dto.getRequestId().intValue());
    }
}