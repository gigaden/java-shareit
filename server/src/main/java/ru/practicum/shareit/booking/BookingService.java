package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    Booking get(Long userId, Long bookingId);

    List<Booking> getAll(Long userId, BookingState state);

    List<Booking> getOwnersBooking(Long userId, BookingState state);

    Booking create(Long userId, BookingCreateDto booking);

    Booking changeBookingRequestStatus(Long userId, Long bookingId, boolean approved);


}
