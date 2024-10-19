package ru.practicum.shareit.booking;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateDto {

    @NonNull
    //@FutureOrPresent
    private LocalDateTime start;
    @NonNull
    //@Future
    private LocalDateTime end;
    @NonNull
    private Long itemId;
}
