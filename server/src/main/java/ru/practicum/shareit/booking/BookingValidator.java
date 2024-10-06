package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.BookingValidateException;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;

@Slf4j
public class BookingValidator {

    // Проверяем, корректность дат для бронирования.
    public static void checkBookingsDates(Booking booking) {
        LocalDateTime date = LocalDateTime.now();
        if (booking.getStart().equals(booking.getEnd())) {
            log.warn("Совпадают даты начала и окончания бронирования.");
            throw new BookingValidateException("Дата начала и окончания бронирования не должны совпадать.");
        } else if (booking.getStart().isEqual(date)) {
            log.warn("Время начала бронирования равно текущему моменту.");
            throw new BookingValidateException("Время начала бронирования не должно быть в прошлом.");
        } else if (booking.getEnd().isBefore(date.minusSeconds(30))) {
            log.warn("Время окончания бронирования раньше текущего момента.");
            throw new BookingValidateException("Время окончания бронирования не должно быть в прошлом.");
        } else if (booking.getEnd().isBefore(booking.getStart())) {
            log.warn("Начало бронирования позже окончания.");
            throw new BookingValidateException("Время начала бронирования не должно быть позже окончания.");
        } //else if (booking.getStart().isBefore(date.plusMinutes(timeForBooking))) {
//            log.warn("Начало бронирования равно текущему времени.");
//            throw new BookingValidateException("Время начала бронирования не должно быть равно текущему моменту.");
//        }
    }

    // Проверяем, что вещь доступна для бронирования.
    public static void checkItemIsAvailableForBooking(Item item) {
        if (!item.getAvailable()) {
            log.warn("Вещь с id = {} недоступна для бронирования.", item.getId());
            throw new BookingValidateException("Собственник запретил бронирование вещи.");
        }
    }
}
