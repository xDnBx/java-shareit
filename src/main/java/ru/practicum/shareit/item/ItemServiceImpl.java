package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto createItem(Long id, ItemDto dto) {
        User itemOwner = getUserById(id);
        log.debug("Добавление новой вещи с именем: {} пользователю с id = {}", dto.getName(), id);
        Item item = ItemMapper.toItem(dto);
        item.setOwner(itemOwner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long itemId, ItemDto dto, Long userId) {
        checkId(userId);
        Item item = checkItem(itemId);
        if (!item.getOwner().getId().equals(userId)) {
            log.error("Пользователь с id = {} не владелец вещи с id = {}", userId, itemId);
            throw new NotFoundException("Редактировать может только владелец вещи");
        }
        log.debug("Обновление вещи с id = {} пользователя с id = {}", itemId, userId);
        if (dto.getName() != null && !dto.getName().isBlank()) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = checkItem(id);
        BookingDto lastBooking = bookingRepository
                .findTopByItemIdAndEndBeforeAndStatusOrderByEndDesc(id, LocalDateTime.now().minusSeconds(2),
                        BookingStatus.APPROVED)
                .map(BookingMapper::toBookingDto)
                .orElse(null);
        BookingDto nextBooking = bookingRepository
                .findTopByItemIdAndStartAfterOrderByStartAsc(id, LocalDateTime.now())
                .map(BookingMapper::toBookingDto)
                .orElse(null);
        Collection<Comment> comments = commentRepository.findAllByItemId(id).stream().toList();

        return ItemMapper.toItemDtoBooking(item, lastBooking, nextBooking, comments);
    }

    @Override
    public Collection<ItemDto> getAllItemsByOwner(Long id) {
        log.debug("Получение списка всех вещей пользователя с id = {}", id);
        return itemRepository.findAllByOwner(getUserById(id)).stream()
                .map(item -> getItemById(item.getId()))
                .toList();
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        log.debug("Получение списка доступных вещей с текстом '{}'", text);
        return itemRepository.searchItemsByText(text.toUpperCase()).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public CommentDto createComment(Long itemId, Long userId, CommentDto dto) {
        User user = getUserById(userId);
        Item item = checkItem(itemId);
        Comment comment = CommentMapper.toComment(dto, user, item);
        Collection<Booking> bookings = bookingRepository.findAllByItemIdAndBookerIdAndEndBefore(
                itemId, userId, LocalDateTime.now());

        if (bookings.isEmpty()) {
            throw new ValidationException("Отзыв может оставлять только пользователь, который арендовал ранее эту вещь");
        }

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private User getUserById(Long id) {
        checkId(id);
        log.debug("Получение пользователя с id = {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    private void checkId(Long id) {
        if (id == null) {
            log.error("id не указан");
            throw new ValidationException("id должен быть указан");
        }
    }

    private Item checkItem(Long id) {
        checkId(id);
        log.debug("Получение вещи с id = {}", id);
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена"));
    }
}