package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class CommentMapper {

    public static CommentDto mapToItemDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(comment.getItem())
                .authorName(comment.getAuthorName().getName())
                .created(comment.getCreated())
                .build();
    }

}
