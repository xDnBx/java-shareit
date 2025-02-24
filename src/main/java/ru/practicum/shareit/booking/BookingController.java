package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@Valid @RequestBody BookingDtoInput dto,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на добавление нового бронирования вещи c id = {} у пользователя с id = {}",
                dto.getItemId(), userId);
        return bookingService.createBooking(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestParam Boolean approved,
                                    @PathVariable Long bookingId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на подтверждение или отклонение бронирования с id = {}", bookingId);
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение бронирования с id = {} от пользователя с id = {}", bookingId, userId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> getBookingsByUser(@RequestParam(defaultValue = "ALL") String state,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение списка бронирований пользователя с id = {} с параметром '{}'", userId, state);
        return bookingService.getBookingsByUser(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getBookingsByOwner(@RequestParam(defaultValue = "ALL") String state,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение списка бронирований владельца с id = {} с параметром '{}'", userId, state);
        return bookingService.getBookingsByOwner(userId, state);
    }
}