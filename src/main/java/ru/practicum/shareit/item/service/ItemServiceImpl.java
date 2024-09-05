package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.ValidationNullException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dao.ItemStorageImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("itemServiceImpl")
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Qualifier("itemStorageImpl")
    private final ItemStorage itemStorage;

    public ItemServiceImpl(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    @Override
    public Collection<ItemDto> getAll(long userId) {
        log.info("Получаем коллекцию всех вещей пользователя с id={}.", userId);
        Collection<ItemDto> items = itemStorage.getAll(userId).stream()
                .map(ItemMapper::mapToItemDto).collect(Collectors.toList());
        log.info("Коллекция вещей пользователя с id={} успешно передана.", userId);
        return items;
    }

    @Override
    public ItemDto get(long id) {
        log.info("Попытка получить вещь с id={}", id);
        Item item = itemStorage.get(id)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id=%d не найдена", id)));
        log.info("Вещь с id = {} успешно передана.", id);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public Item create(ItemDto itemDto, long userId) {
        log.info("Попытка добавить вещь {}", itemDto.getName());
        Item item = itemStorage.create(ItemMapper.mapToItem(itemDto, userId));
        log.info("Вещь с id={} успешно добавлена.", item.getId());
        return item;
    }

    @Override
    public Item update(Item newItem, long userId) {
        log.info("Попытка обновить вещь.");
        if (newItem.getId() == null) {
            log.warn("не указан Id вещи.");
            throw new ValidationNullException("Id вещи должен быть указан.");
        }
        Item oldItem = itemStorage.get(newItem.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id=%d не найдена", newItem.getId())));
        if (oldItem.getOwner() != userId) {
            log.warn("не совпадают id пользователя и собственника вещи.");
            throw new ValidationException("Нельзя изменить чужую вещь.");
        }
        Item item = itemStorage.update(newItem);
        log.info("Вещь с id = {} успешно обновлена", item.getId());
        return item;
    }

    @Override
    public Item patch(long itemId, long userId) {
        return null;
    }

    @Override
    public Collection<ItemDto> search(String text) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
