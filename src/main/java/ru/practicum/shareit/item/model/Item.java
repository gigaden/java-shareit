package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    private Long id;
    @NonNull
    private String name;
    private String description;
    private boolean available;
    private Long owner;
    private ItemRequest request;

}
