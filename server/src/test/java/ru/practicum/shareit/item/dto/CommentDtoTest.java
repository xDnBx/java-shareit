package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = CommentDto.builder()
                .id(1L)
                .text("Text")
                .itemId(1L)
                .authorName("Name")
                .created(LocalDateTime.of(2025, 3, 9, 16,0))
                .build();

        var result = json.write(dto);

        assertThat(result)
                .hasJsonPath("$.id")
                .hasJsonPath("$.text")
                .hasJsonPath("$.itemId")
                .hasJsonPath("$.authorName")
                .hasJsonPath("$.created");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(dto.getText());
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(dto.getItemId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(dto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2025-03-09T16:00:00");
    }
}