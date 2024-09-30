package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static ItemBookingsDto mapToItemBookingDto(Item item, List<Booking> bookings, Collection<Comment> comments) {
        LocalDateTime date = LocalDateTime.now();
        ItemBookingsDto itemBookingsDto = ItemBookingsDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments)
                .build();
        if (bookings.size() == 2) {
            List<LocalDateTime> first = List.of(bookings.getFirst().getStart(), bookings.getFirst().getEnd());
            itemBookingsDto.setLastBooking(first.getLast().isAfter(date) ? first : null);
            List<LocalDateTime> next = List.of(bookings.getLast().getStart(), bookings.getLast().getEnd());
            itemBookingsDto.setLastBooking(next.getLast().isAfter(date) ? next : null);
        } else if (bookings.size() == 1) {
            List<LocalDateTime> first = List.of(bookings.getFirst().getStart(), bookings.getFirst().getEnd());
            itemBookingsDto.setLastBooking(first.getLast().isAfter(date) ? first : null);
        }
        return itemBookingsDto;
    }

    public static Item mapToItem(ItemDto itemDto, User user) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                //.request(null)
                .build();
    }
}
