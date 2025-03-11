package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findAllByBookerId(Long userId, Sort sort);

    Collection<Booking> findAllByBookerIdAndEndAfter(Long userId, LocalDateTime now, Sort sort);

    Collection<Booking> findAllByBookerIdAndEndBefore(Long userId, LocalDateTime now, Sort sort);

    Collection<Booking> findAllByBookerIdAndStartAfter(Long userId, LocalDateTime now, Sort sort);

    Collection<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    Collection<Booking> findAllByItemOwnerId(Long userId, Sort sort);

    Collection<Booking> findAllByItemOwnerIdAndEndAfter(Long userId, LocalDateTime now, Sort sort);

    Collection<Booking> findAllByItemOwnerIdAndEndBefore(Long userId, LocalDateTime now, Sort sort);

    Collection<Booking> findAllByItemOwnerIdAndStartAfter(Long userId, LocalDateTime now, Sort sort);

    Collection<Booking> findAllByItemOwnerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    Optional<Booking> findTopByItemIdAndItemOwnerIdAndEndBeforeAndStatusOrderByEndDesc(Long itemId, Long ownerId,
                                                                                       LocalDateTime now,
                                                                                       BookingStatus status);

    Optional<Booking> findTopByItemIdAndItemOwnerIdAndStartAfterOrderByStartAsc(Long itemId, Long ownerId,
                                                                                LocalDateTime now);

    Collection<Booking> findAllByItemIdAndBookerIdAndEndBefore(Long itemId, Long bookerId, LocalDateTime now);

    List<Booking> findAllByItemInAndStatusOrderByStartAsc(List<Item> items, BookingStatus status);
}