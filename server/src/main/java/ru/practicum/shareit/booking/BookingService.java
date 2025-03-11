package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(Long userId, BookingDtoInput dto);

    BookingDto updateBooking(Long userId, Long bookingId, Boolean approved);

    BookingDto getBookingById(Long bookingId, Long userId);

    Collection<BookingDto> getBookingsByUser(Long userId, String state);

    Collection<BookingDto> getBookingsByOwner(Long userId, String state);
}