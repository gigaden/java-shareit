package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDto> getAll(long userId);

    ItemDto get(long id);

    Item create(ItemDto itemDto, long userId);

    Item update(Item item, long userId);

    Item patch(long itemId, long userId);

    Collection<ItemDto> search(String text);

    void delete(long id);
}
