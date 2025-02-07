package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class UserRepository {
    private long id = 1;
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User createUser(User user) {
        checkEmail(user);
        user.setId(generateNewId());
        users.put(user.getId(), user);
        log.info("Пользователь с id = {} успешно добавлен!", user.getId());
        return user;
    }

    public User updateUser(User newUser, UserDto dto) {
        if (dto.getEmail() != null) {
            checkEmail(newUser);
        }
        users.put(newUser.getId(), newUser);
        log.info("Обновление пользователя с id = {} прошло успешно!", newUser.getId());
        return newUser;
    }

    public User getUserById(Long id) {
        checkUser(id);
        return users.get(id);
    }

    public void deleteUser(Long id) {
        checkUser(id);
        users.remove(id);
        log.info("Удаление пользователя с id = {} прошло успешно!", id);
    }

    private long generateNewId() {
        return id++;
    }

    private void checkUser(Long id) {
        if (!users.containsKey(id)) {
            log.error("Пользователя с id = {} не существует", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    private void checkEmail(User newUser) {
        for (User user : users.values()) {
            if (user.getEmail().equals(newUser.getEmail())) {
                log.error("E-mail уже присутствует у другого пользователя");
                throw new DuplicatedDataException("Этот e-mail уже используется");
            }
        }
    }
}