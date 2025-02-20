package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> getAllUsers() {
        log.debug("Получение списка всех пользователей");
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto createUser(UserDto dto) {
        checkEmail(dto);
        log.debug("Добавление нового пользователя с именем: {}", dto.getName());
        User user = UserMapper.toUser(dto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto dto) {
        checkEmail(dto);
        User user = UserMapper.toUser(getUserById(userId));
        log.debug("Обновление пользователя с id = {}", userId);
        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
        }
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserById(Long id) {
        checkId(id);
        log.debug("Получение пользователя с id = {}", id);
        return UserMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден")));
    }

    @Override
    public void deleteUser(Long id) {
        getUserById(id);
        log.debug("Удаление пользователя с id = {}", id);
        userRepository.deleteById(id);
    }

    private void checkId(Long id) {
        if (id == null) {
            log.error("id пользователя не указан");
            throw new ValidationException("id должен быть указан");
        }
    }

    private void checkEmail(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            log.error("E-mail уже присутствует у другого пользователя");
            throw new DuplicatedDataException("Этот e-mail уже используется");
        }
    }
}