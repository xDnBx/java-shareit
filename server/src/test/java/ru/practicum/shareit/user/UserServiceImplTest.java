package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserServiceImplTest {

    @Autowired
    UserService userService;

    static UserDto user1;
    static UserDto user2;

    @BeforeAll
    static void beforeAll() {
        user1 = UserDto.builder().name("Yandex").email("yandex@practicum.ru").build();
        user2 = UserDto.builder().name("Yandex2").email("yandex2@practicum.ru").build();
    }

    @Test
    void shouldGetAllUsers() {
        userService.createUser(user1);
        UserDto newUser = userService.createUser(user2);
        List<UserDto> users = userService.getAllUsers().stream().toList();

        assertThat(users.get(1).getId()).isEqualTo(newUser.getId());
        assertThat(users.get(1).getName()).isEqualTo(newUser.getName());
        assertThat(users.get(1).getEmail()).isEqualTo(newUser.getEmail());
    }

    @Test
    void shouldCreateAndGetUser() {
        UserDto user = userService.createUser(user1);
        UserDto getUser = userService.getUserById(user.getId());

        assertThat(user.getId()).isEqualTo(getUser.getId());
        assertThat(user.getName()).isEqualTo(getUser.getName());
        assertThat(user.getEmail()).isEqualTo(getUser.getEmail());
    }

    @Test
     void shouldThrowExceptionWhenEmailIsDuplicateWhenCreateUser() {
        userService.createUser(user1);
        UserDto newUser = UserDto.builder().name("Yandex2").email("yandex@practicum.ru").build();

        assertThatThrownBy(() -> userService.createUser(newUser)).isInstanceOf(DuplicatedDataException.class);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThatThrownBy(() -> userService.getUserById(user1.getId())).isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldUpdateUser() {
        UserDto user = userService.createUser(user1);
        UserDto updateUser = userService.updateUser(user.getId(), user2);

        assertThat(updateUser.getId()).isEqualTo(user.getId());
        assertThat(updateUser.getName()).isEqualTo(user2.getName());
        assertThat(updateUser.getEmail()).isEqualTo(user2.getEmail());
    }

    @Test
    void shouldUpdateUserNameIsNull() {
        UserDto user = userService.createUser(user1);
        UserDto user3 = UserDto.builder().email("yandex2@practicum.ru").build();
        UserDto updateUser = userService.updateUser(user.getId(), user3);

        assertThat(updateUser.getId()).isEqualTo(user.getId());
        assertThat(updateUser.getName()).isEqualTo(user.getName());
        assertThat(updateUser.getEmail()).isEqualTo(user3.getEmail());
    }

    @Test
    void shouldUpdateUserEmailIsNull() {
        UserDto user = userService.createUser(user1);
        UserDto user3 = UserDto.builder().name("Yandex2").build();
        UserDto updateUser = userService.updateUser(user.getId(), user3);

        assertThat(updateUser.getId()).isEqualTo(user.getId());
        assertThat(updateUser.getName()).isEqualTo(user3.getName());
        assertThat(updateUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsDuplicateWhenUpdateUser() {
        UserDto user = userService.createUser(user1);
        UserDto updateUser = UserDto.builder().name("Yandex2").email("yandex@practicum.ru").build();

        assertThatThrownBy(() -> userService.updateUser(user.getId(), updateUser))
                .isInstanceOf(DuplicatedDataException.class);
    }

    @Test
    void shouldDeleteUser() {
        UserDto user = userService.createUser(user1);
        userService.deleteUser(user.getId());

        assertThatThrownBy(() -> userService.getUserById(user.getId()))
                .isInstanceOf(NotFoundException.class);
    }
}