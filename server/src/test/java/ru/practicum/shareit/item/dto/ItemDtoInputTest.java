package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoInputTest {

    @Autowired
    private JacksonTester<ItemDtoInput> json;

    @Test
    void testSerialize() throws Exception {
        var dto = ItemDtoInput.builder()
                .id(1L)
                .name("Yandex")
                .description("YandexPracticum")
                .available(true)
                .requestId(1L)
                .build();

        var result = json.write(dto);

        assertThat(result)
                .hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.description")
                .hasJsonPath("$.available")
                .hasJsonPath("$.requestId");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(dto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(dto.getRequestId().intValue());
    }
}