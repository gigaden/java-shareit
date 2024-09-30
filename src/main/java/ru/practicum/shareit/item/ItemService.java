package ru.practicum.shareit.item;

import java.util.Collection;

public interface ItemService {

    Collection<Item> getAll();

    Collection<ItemBookingsDto> getAllUserItems(Long userId);

    ItemBookingsDto get(Long id);

    Item getById(Long id);

    Item create(ItemDto itemDto, long userId);

    Item update(Item item, long userId);

    Item patch(long itemId, long userId, ItemDto itemDto);

    Collection<Item> search(String text);

    void delete(long id);

    Comment addComment(Long userId, Long itemId, Comment comment);
}
