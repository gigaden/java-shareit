package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long id, Sort sort);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStart(Long id, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStart(Long id, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndEndIsAfterOrderByStart(Long id, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndStatusOrderByStart(Long id, BookingStatus status, Sort sort);


    List<Booking> findBookingByItemOwnerId(Long id, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndStatus(Long id, BookingStatus status, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndEndIsBefore(Long id, LocalDateTime localDateTime, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndStartIsAfter(Long id, LocalDateTime localDateTime, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndEndIsAfter(Long id, LocalDateTime localDateTime, Sort sort);
}
