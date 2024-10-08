package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemBookingsDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private List<LocalDateTime> lastBooking;
    private List<LocalDateTime> nextBooking;
    private Collection<CommentDto> comments;
    private long request;

}
