package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserDtoTest {

    @Test
    void testUserDtoIds() {
        UserDto userDto1 = UserDto.builder().id(1L).name("Yandex").email("yandex@practicum.ru").build();
        UserDto userDto2 = UserDto.builder().id(1L).name("Yandex").email("yandex@practicum.ru").build();
        UserDto userDto3 = UserDto.builder().id(2L).name("Yandex").email("yandex@practicum.ru").build();

        assertEquals(userDto1, userDto2, "id пользователей не равны, попробуйте еще раз");
        assertNotEquals(userDto1, userDto3, "id пользователей равны, попробуйте еще раз");
    }
}