package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.request.ItemRequest;


@Data
@Builder
public class Item {
    private Long id;
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    @NotBlank
    private String description;
    @NonNull
    @NotBlank
    private Boolean available;
    private Long owner;
    // private ItemRequest request;

}
