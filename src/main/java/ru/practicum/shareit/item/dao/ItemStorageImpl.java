package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("itemStorageImpl")
@Slf4j
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Collection<Item> getAll() {
        return items.values();
    }


    @Override
    public Collection<Item> getAllUserItems(long userId) {
        return items.values().stream().filter(i -> i.getOwner() == userId).collect(Collectors.toList());
    }

    @Override
    public Optional<Item> get(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item create(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item newItem) {
        if (items.containsKey(newItem.getId())) {
            Item oldItem = items.get(newItem.getId());
            oldItem.setName(newItem.getName());
            oldItem.setDescription(newItem.getDescription());
            oldItem.setOwner(newItem.getOwner());
            oldItem.setAvailable(newItem.getAvailable());
            oldItem.setRequest(newItem.getRequest());
            return oldItem;
        }
        log.warn("Вещь с id={} не найдена.", newItem.getId());
        throw new NotFoundException("Вещь с id = " + newItem.getId() + " не найдена.");
    }

    @Override
    public Item patch(long itemId, ItemDto itemDto) {
        Item oldItem = items.get(itemId);
        oldItem.setName(itemDto.getName() != null ? itemDto.getName() : oldItem.getName());
        oldItem.setDescription(itemDto.getDescription() != null ? itemDto.getDescription() : oldItem.getDescription());
        oldItem.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : oldItem.getAvailable());
        return oldItem;
    }

    @Override
    public Collection<Item> search(String text) {
        return List.of();
    }

    @Override
    public void delete(long id) {
        items.remove(id);
    }


    // Метод для генерации идентификатора
    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
