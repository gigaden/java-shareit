package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BookingAccessException;
import ru.practicum.shareit.exception.BookingValidateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("bookingServiceImpl")
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingRepository bookingRepository;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "start");
    private static final int timeForBooking = 10; // минимальное время(минуты) для подтверждения бронирования

    public BookingServiceImpl(UserService userService, ItemService itemService,
                              BookingRepository bookingRepository) {
        this.userService = userService;
        this.itemService = itemService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking get(Long userId, Long bookingId) {
        log.info("Попытка получить бронирование с id = {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронь с id=%d не найдена", bookingId)));
        if (!Objects.equals(booking.getBooker().getId(), userId) &&
                !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new BookingAccessException("Информация доступна только автору бронирования," +
                    "или владельцу вещи.");
        }
        log.info("Бронирование с id = {} успешно передано.", bookingId);
        return booking;
    }

    @Override
    public List<Booking> getAll(Long userId, BookingState state) {
        userService.checkUserIsExist(userId);
        List<Booking> bookings = new ArrayList<>();
        log.info("Попытка получить бронирования со статусом {} пользователя с id = {}", state, userId);
        switch (state) {
            case ALL -> bookings = bookingRepository.findAllByBookerId(userId, sort);
            case PAST ->
                    bookings = bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStart(userId, LocalDateTime.now(), sort);
            case FUTURE ->
                    bookings = bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStart(userId, LocalDateTime.now(), sort);
            case CURRENT ->
                    bookings = bookingRepository.findAllByBookerIdAndEndIsAfterOrderByStart(userId, LocalDateTime.now(), sort);
            case WAITING ->
                    bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.WAITING, sort);
            case REJECTED ->
                    bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.REJECTED, sort);
        }
        log.info("Бронирования со статусом {} пользователя с id = {} успешно переданы", state, userId);
        return bookings;
    }

    @Override
    public List<Booking> getOwnersBooking(Long userId, BookingState state) {
        userService.checkUserIsExist(userId);
        List<Booking> bookings = new ArrayList<>();
        log.info("Попытка получить бронирования всех вещей со статусом {} пользователя с id = {}", state, userId);
        switch (state) {
            case ALL -> bookings = bookingRepository.findBookingByItemOwnerId(userId, sort);
            case PAST ->
                    bookings = bookingRepository.findBookingByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now(), sort);
            case FUTURE ->
                    bookings = bookingRepository.findBookingByItemOwnerIdAndStartIsAfter(userId, LocalDateTime.now(), sort);
            case CURRENT ->
                    bookings = bookingRepository.findBookingByItemOwnerIdAndEndIsAfter(userId, LocalDateTime.now(), sort);
            case WAITING ->
                    bookings = bookingRepository.findBookingByItemOwnerIdAndStatus(userId, BookingStatus.WAITING, sort);
            case REJECTED ->
                    bookings = bookingRepository.findBookingByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED, sort);
        }
        log.info("Бронирования всех вещей со статусом {} пользователя с id = {} успешно переданы", state, userId);
        return bookings;
    }

    @Override
    public Booking create(Long userId, BookingCreateDto bookingDto) {
        log.info("Попытка забронировать вещь с id = {}", bookingDto.getItemId());
        User booker = userService.get(userId);
        Item item = itemService.get(bookingDto.getItemId());
        checkItemIsAvailableForBooking(item);
        Booking booking = BookingMapper.mapDtoToBooking(bookingDto, booker, item);
        booking.setStatus(BookingStatus.WAITING);
        checkBookingsDates(booking);
        bookingRepository.save(booking);
        log.info("Вещь {} с id={} успешно забронирована.", item.getName(), item.getId());
        return booking;
    }

    @Override
    public Booking changeBookingRequestStatus(Long userId, Long bookingId, boolean approved) {
        log.info("Попытка юзером {} изменить статус брони {}", userId, bookingId);
        User user = userService.get(userId);
        Booking booking = get(userId, bookingId);
        Item item = booking.getItem();
        if (!Objects.equals(item.getOwner(), user)) {
            throw new BookingAccessException("Изменить статус бронировани может только владелец вещи.");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);
        log.info("Статус брони с id = {} изменён на {}", bookingId, booking.getStatus());
        return booking;

    }

    // Проверяем, корректность дат для бронирования.
    public void checkBookingsDates(Booking booking) {
        LocalDateTime date = LocalDateTime.now();
        if (booking.getStart().equals(booking.getEnd())) {
            log.warn("Совпадают даты начала и окончания бронирования.");
            throw new BookingValidateException("Дата начала и окончания бронирования не должны совпадать.");
        } else if (booking.getStart().isBefore(date)) {
            log.warn("Время начала бронирования раньше текущего момента.");
            throw new BookingValidateException("Время начала бронирования не должно быть в прошлом.");
        } else if (booking.getEnd().isBefore(date)) {
            log.warn("Время окончания бронирования раньше текущего момента.");
            throw new BookingValidateException("Время окончания бронирования не должно быть в прошлом.");
        } else if (booking.getEnd().isBefore(booking.getStart())) {
            log.warn("Начало бронирования позже окончания.");
            throw new BookingValidateException("Время начала бронирования не должно быть позже окончания.");
        } else if (booking.getStart().isBefore(date.plusMinutes(timeForBooking))) {
            log.warn("Начало бронирования равно текущему времени.");
            throw new BookingValidateException("Время начала бронирования не должно быть равно текущему моменту.");
        }
    }

    // Проверяем, что вещь доступна для бронирования.
    public void checkItemIsAvailableForBooking(Item item) {
        if (!item.getAvailable()) {
            log.warn("Вещь с id = {} недоступна для бронирования.", item.getId());
            throw new BookingValidateException("Собственник запретил бронирование вещи.");
        }
    }
}
