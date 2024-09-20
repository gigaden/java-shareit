package ru.practicum.shareit.item;

import java.util.Collection;

public interface ItemService {

    Collection<Item> getAll();

    Collection<Item> getAllUserItems(long userId);

    Item get(long id);

    Item create(ItemDto itemDto, long userId);

    Item update(Item item, long userId);

    Item patch(long itemId, long userId, ItemDto itemDto);

    Collection<Item> search(String text);

    void delete(long id);
}
