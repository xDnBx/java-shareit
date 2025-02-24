package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findByBookerIdOrderByStartDesc(Long userId);

    Collection<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    Collection<Booking> findByItemOwnerIdOrderByStartDesc(Long userId);

    Collection<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    Optional<Booking> findTopByItemIdAndEndBeforeAndStatusOrderByEndDesc(Long itemId, LocalDateTime now,
                                                                         BookingStatus status);

    Optional<Booking> findTopByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    Collection<Booking> findAllByItemIdAndBookerIdAndEndBefore(Long itemId, Long bookerId, LocalDateTime now);
}