package ru.practicum.shareit.item;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDto> getAll();

    Collection<ItemDto> getAllUserItems(long userId);

    ItemDto get(long id);

    Item create(ItemDto itemDto, long userId);

    Item update(Item item, long userId);

    Item patch(long itemId, long userId, ItemDto itemDto);

    Collection<ItemDto> search(String text);

    void delete(long id);
}
