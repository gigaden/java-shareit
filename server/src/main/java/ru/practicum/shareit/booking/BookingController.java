package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    @Qualifier("bookingServiceImpl")
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Получение данных о конкретном бронировании (включая его статус)
    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto get(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return BookingMapper.mapToBookingDto(bookingService.get(userId, bookingId));
    }

    // Получение списка всех бронирований текущего пользователя
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAll(userId, state).stream()
                .map(BookingMapper::mapToBookingDto).toList();
    }

    // Получение списка бронирований для всех вещей текущего пользователя
    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getOwnersBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getOwnersBooking(userId, state).stream()
                .map(BookingMapper::mapToBookingDto).toList();
    }

    // Добавление нового запроса на бронирование
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                             @RequestBody BookingCreateDto booking) {
        return BookingMapper.mapToBookingDto(bookingService.create(userId, booking));
    }

    // Подтверждение или отклонение запроса на бронирование
    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto changeBookingRequestStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam boolean approved) {
        return BookingMapper.mapToBookingDto(bookingService.changeBookingRequestStatus(userId, bookingId, approved));


    }


}
