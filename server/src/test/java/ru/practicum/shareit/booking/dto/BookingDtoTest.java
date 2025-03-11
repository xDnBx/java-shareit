package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        UserDto user = UserDto.builder().id(1L).name("Name").email("yandex@practicum.ru").build();
        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("Yandex")
                .description("YandexPracticum")
                .available(true)
                .ownerId(1L)
                .requestId(1L)
                .build();
        var dto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2025, 3, 9, 12,0))
                .end(LocalDateTime.of(2025, 3, 9, 14,0))
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();

        var result = json.write(dto);

        assertThat(result)
                .hasJsonPath("$.id")
                .hasJsonPath("$.start")
                .hasJsonPath("$.end")
                .hasJsonPath("$.item")
                .hasJsonPath("$.booker")
                .hasJsonPath("$.status");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2025-03-09T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2025-03-09T14:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(item.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.item.ownerId")
                .isEqualTo(item.getOwnerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.item.requestId")
                .isEqualTo(item.getRequestId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(user.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo(user.getEmail());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(dto.getStatus().name());
    }
}