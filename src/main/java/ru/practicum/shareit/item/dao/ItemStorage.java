package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {

    Collection<Item> getAll();

    Collection<Item> getAllUserItems(long userId);

    Optional<Item> get(long id);

    Item create(Item item);

    Item update(Item item);

    Item patch(long itemId, ItemDto item);

    Collection<Item> search(String text);

    void delete(long id);
}
