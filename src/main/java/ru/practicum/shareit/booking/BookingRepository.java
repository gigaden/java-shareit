package ru.practicum.shareit.booking;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long id, Sort sort);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStart(Long id, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStart(Long id, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndEndIsAfterOrderByStart(Long id, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndStatusOrderByStart(Long id, BookingStatus status, Sort sort);

    @Query(value = """
            select b from Booking b
            where (cast(?1 as timestamp) between b.start and b.end
            or cast(?2 as timestamp) between b.start and b.end)
            and b.item.id = ?3
            """)
    List<Booking> findAllCrossingsDates(LocalDateTime start, LocalDateTime end, Long id);

    List<Booking> findBookingByItemOwnerId(Long id, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndStatus(Long id, BookingStatus status, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndEndIsBefore(Long id, LocalDateTime localDateTime, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndStartIsAfter(Long id, LocalDateTime localDateTime, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndEndIsAfter(Long id, LocalDateTime localDateTime, Sort sort);

    List<Booking> findAllByItemIdOrderByStart(Long itemId, Limit limit);

    Optional<Booking> findBookingByItemIdAndBookerId(Long itemId, Long userId);
}
