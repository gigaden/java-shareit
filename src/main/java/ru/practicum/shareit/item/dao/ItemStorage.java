package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {

    Collection<Item> getAll(long userId);

    Optional<Item> get(long id);

    Item create(Item item);

    Item update(Item item);

    Item patch(Item item);

    Collection<Item> search(String text);

    void delete(long id);
}
