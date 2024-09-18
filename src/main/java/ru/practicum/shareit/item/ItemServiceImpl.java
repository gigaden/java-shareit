package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.ValidationNullException;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.List;

@Service("itemServiceImpl")
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Qualifier("itemStorageImpl")
    private final ItemStorage itemStorage;

    @Qualifier("userServiceImpl")
    private final UserService userService;

    public ItemServiceImpl(ItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    @Override
    public Collection<ItemDto> getAll() {
        log.info("Попытка получить все вещи.");
        List<ItemDto> items = itemStorage.getAll().stream()
                .map(ItemMapper::mapToItemDto).toList();
        log.info("Коллекция вещей успешно передана.");
        return items;
    }

    @Override
    public Collection<ItemDto> getAllUserItems(long userId) {
        log.info("Получаем коллекцию всех вещей пользователя с id={}.", userId);
        List<ItemDto> items = itemStorage.getAllUserItems(userId).stream()
                .map(ItemMapper::mapToItemDto).toList();
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
        userService.get(userId);
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            log.warn("Не указано имя вещи.");
            throw new ValidationNullException("Имя не может быть пустым.");
        }
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
        checkUserIsOwnerOfItem(oldItem.getOwner(), userId);
        Item item = itemStorage.update(newItem);
        log.info("Вещь с id = {} успешно обновлена", item.getId());
        return item;
    }

    @Override
    public Item patch(long itemId, long userId, ItemDto itemDto) {
        log.info("Попытка обновить вещь через patch.");
        Item oldItem = itemStorage.get(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id=%d не найдена", itemId)));
        checkUserIsOwnerOfItem(oldItem.getOwner(), userId);
        Item item = itemStorage.patch(itemId, itemDto);
        log.info("Информация о вещи с id={} успешно обновлена.", itemId);
        return item;

    }

    @Override
    public Collection<ItemDto> search(String text) {
        log.info("Попытка найти вещи с текстом '{}'", text);
        if (text.isEmpty()) {
            log.warn("Не задан текст для поиска");
            return List.of();
        }
        List<ItemDto> itemsDto = getAll().stream()
                .filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        i.getName().toLowerCase().contains(text.toLowerCase()))
                .filter(ItemDto::getAvailable)
                .toList();
        if (itemsDto.isEmpty()) {
            log.info("Совпадений с текстом '{}' не найдено", text);
            return itemsDto;
        }
        log.info("Коллекция вещей с текстом '{}' успешно передана", text);
        return itemsDto;
    }

    @Override
    public void delete(long id) {
        get(id);
        itemStorage.delete(id);
    }

    // Проверяем, что пользователь - собственник вещи.
    private void checkUserIsOwnerOfItem(long ownerId, long userId) {
        if (ownerId != userId) {
            log.warn("не совпадают id пользователя и собственника вещи.");
            throw new ValidationException("Нельзя изменить чужую вещь.");
        }
    }
}
