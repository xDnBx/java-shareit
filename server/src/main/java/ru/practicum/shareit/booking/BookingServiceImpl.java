package ru.practicum.shareit.booking;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto createBooking(Long userId, BookingDtoInput dto) {
        User user = getUserById(userId);
        Item item = getItemById(dto.getItemId());
        checkBooking(dto, userId, item);
        log.debug("Добавление нового бронирования вещи с id = {} пользователя с id = {}", item.getId(), userId);
        return BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toBooking(dto, item, user)));
    }

    @Override
    @Transactional
    public BookingDto updateBooking(Long userId, Long bookingId, Boolean approved) {
        getUserById(userId);
        Booking booking = getBooking(bookingId);

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException("Бронирование уже подтверждено или отклонено");
        }
        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new ValidationException("Подтверждение или отказ бронирования может делать только владелец");
        }
        log.debug("Обновление бронирования вещи с id = {} пользователя с id = {}", booking.getItem().getId(), userId);
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        getUserById(userId);
        Booking booking = getBooking(bookingId);

        if (booking.getItem().getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId)) {
            return BookingMapper.toBookingDto(booking);
        }

        throw new ValidationException("Просматривать бронирование может владелец вещи, либо автор бронирования");
    }

    @Override
    public Collection<BookingDto> getBookingsByUser(Long userId, String state) {
        getUserById(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings;

        switch (state.toUpperCase()) {
            case "ALL" -> bookings = bookingRepository.findAllByBookerId(userId, sort);
            case "CURRENT" -> bookings = bookingRepository.findAllByBookerIdAndEndAfter(userId, now, sort);
            case "PAST" -> bookings = bookingRepository.findAllByBookerIdAndEndBefore(userId, now, sort);
            case "FUTURE" -> bookings = bookingRepository.findAllByBookerIdAndStartAfter(userId, now, sort);
            case "WAITING" ->
                    bookings = bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.WAITING, sort);
            case "REJECTED" ->
                    bookings = bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.REJECTED, sort);
            default -> throw new IllegalArgumentException("Неверный параметр 'state'");
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getBookingsByOwner(Long userId, String state) {
        getUserById(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings;

        switch (state.toUpperCase()) {
            case "ALL" -> bookings = bookingRepository.findAllByItemOwnerId(userId, sort);
            case "CURRENT" -> bookings = bookingRepository.findAllByItemOwnerIdAndEndAfter(userId, now, sort);
            case "PAST" -> bookings = bookingRepository.findAllByItemOwnerIdAndEndBefore(userId, now, sort);
            case "FUTURE" -> bookings = bookingRepository.findAllByItemOwnerIdAndStartAfter(userId, now, sort);
            case "WAITING" ->
                    bookings = bookingRepository.findAllByItemOwnerIdAndStatus(userId, BookingStatus.WAITING, sort);
            case "REJECTED" ->
                    bookings = bookingRepository.findAllByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED, sort);
            default -> throw new IllegalArgumentException("Неверный параметр 'state'");
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    private User getUserById(Long id) {
        checkId(id);
        log.debug("Получение пользователя с id = {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id = " + id + " не найден"));
    }

    private Item getItemById(Long id) {
        checkId(id);
        log.debug("Получение вещи с id = {}", id);
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена"));
    }

    private void checkBooking(BookingDtoInput dto, Long userId, Item item) {
        if (!item.getAvailable()) {
            throw new ValidationException("Бронирование вещи с id = " + item.getId() + " недоступно");
        }
        if (dto.getStart().isEqual(dto.getEnd()) || dto.getStart().isAfter(dto.getEnd())) {
            throw new ValidationException("Время начала бронирования больше или равно концу времени бронирования");
        }
        if (userId.equals(item.getOwner().getId())) {
            throw new NotFoundException("Вещь с id = " + item.getId() + " не может быть забронирована владельцем");
        }
    }

    private Booking getBooking(Long id) {
        checkId(id);
        log.debug("Получение бронирования с id = {}", id);
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с id = " + id + " не найдено"));
    }

    private void checkId(Long id) {
        if (id == null) {
            log.error("id не указан");
            throw new ValidationException("id должен быть указан");
        }
    }
}