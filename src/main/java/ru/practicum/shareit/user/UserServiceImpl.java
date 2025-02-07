package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
        return userRepository.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto createUser(UserDto dto) {
        log.debug("Добавление нового пользователя с именем: {}", dto.getName());
        User user = UserMapper.toUser(dto);
        return UserMapper.toUserDto(userRepository.createUser(user));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto dto) {
        checkId(userId);
        User user = userRepository.getUserById(userId).toBuilder().build();
        log.debug("Обновление пользователя с id = {}", userId);
        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
        }
        return UserMapper.toUserDto(userRepository.updateUser(user, dto));
    }

    @Override
    public UserDto getUserById(Long id) {
        checkId(id);
        log.debug("Получение пользователя с id = {}", id);
        return UserMapper.toUserDto(userRepository.getUserById(id));
    }

    @Override
    public void deleteUser(Long id) {
        checkId(id);
        log.debug("Удаление пользователя с id = {}", id);
        userRepository.deleteUser(id);
    }

    private void checkId(Long id) {
        if (id == null) {
            log.error("id пользователя не указан");
            throw new ValidationException("id должен быть указан");
        }
    }
}