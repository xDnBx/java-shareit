package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {

    @Test
    void testUserIds() {
        User user1 = User.builder().id(1L).name("Yandex").email("yandex@practicum.ru").build();
        User user2 = User.builder().id(1L).name("Yandex").email("yandex@practicum.ru").build();
        User user3 = User.builder().id(2L).name("Yandex").email("yandex@practicum.ru").build();

        assertEquals(user1, user2, "id пользователей не равны, попробуйте еще раз");
        assertNotEquals(user1, user3, "id пользователей равны, попробуйте еще раз");
    }
}