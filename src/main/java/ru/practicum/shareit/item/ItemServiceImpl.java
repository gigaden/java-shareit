package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.ValidationNullException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.List;

@Service("itemServiceImpl")
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    @Qualifier("userServiceImpl")
    private final UserService userService;
    private final ItemRepository itemRepository;

    public ItemServiceImpl(UserService userService, ItemRepository itemRepository) {
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    @Override
    public Collection<Item> getAll() {
        log.info("Попытка получить все вещи.");
        List<Item> items = itemRepository.findAll();
        log.info("Коллекция вещей успешно передана.");
        return items;
    }

    @Override
    public Collection<Item> getAllUserItems(long userId) {
        log.info("Получаем коллекцию всех вещей пользователя с id={}.", userId);
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        log.info("Коллекция вещей пользователя с id={} успешно передана.", userId);
        return items;
    }

    @Override
    public Item get(long id) {
        log.info("Попытка получить вещь с id={}", id);
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id=%d не найдена", id)));
        log.info("Вещь с id = {} успешно передана.", id);
        return item;
    }

    @Override
    @Transactional
    public Item create(ItemDto itemDto, long userId) {
        log.info("Попытка добавить вещь {}", itemDto.getName());
        User user = userService.get(userId);
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            log.warn("Не указано имя вещи.");
            throw new ValidationNullException("Имя не может быть пустым.");
        }
        Item item = itemRepository.save(ItemMapper.mapToItem(itemDto, user));
        log.info("Вещь с id={} успешно добавлена.", item.getId());
        return item;
    }

    @Override
    @Transactional
    public Item update(Item newItem, long userId) {
        log.info("Попытка обновить вещь.");
        if (newItem.getId() == null) {
            log.warn("не указан Id вещи.");
            throw new ValidationNullException("Id вещи должен быть указан.");
        }
        Item oldItem = itemRepository.findById(newItem.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id=%d не найдена", newItem.getId())));
        checkUserIsOwnerOfItem(oldItem.getOwner().getId(), userId);
        oldItem.setName(newItem.getName());
        oldItem.setDescription(newItem.getDescription());
        oldItem.setOwner(newItem.getOwner());
        oldItem.setAvailable(newItem.getAvailable());
        itemRepository.save(oldItem);
        log.info("Вещь с id = {} успешно обновлена", oldItem.getId());
        return oldItem;
    }

    @Override
    @Transactional
    public Item patch(long itemId, long userId, ItemDto itemDto) {
        log.info("Попытка обновить вещь через patch.");
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id=%d не найдена", itemId)));
        checkUserIsOwnerOfItem(oldItem.getOwner().getId(), userId);
        oldItem.setName(itemDto.getName() != null ? itemDto.getName() : oldItem.getName());
        oldItem.setDescription(itemDto.getDescription() != null ? itemDto.getDescription() : oldItem.getDescription());
        oldItem.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : oldItem.getAvailable());
        itemRepository.save(oldItem);
        log.info("Информация о вещи с id={} успешно обновлена.", itemId);
        return oldItem;

    }

    @Override
    public Collection<Item> search(String text) {
        log.info("Попытка найти вещи с текстом '{}'", text);
        if (text.isEmpty()) {
            log.warn("Не задан текст для поиска");
            return List.of();
        }
        List<Item> items = itemRepository.searchTextInNameOrDescription(text);
        if (items.isEmpty()) {
            log.info("Совпадений с текстом '{}' не найдено", text);
            return items;
        }
        log.info("Коллекция вещей с текстом '{}' успешно передана", text);
        return items;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Item item = get(id);
        itemRepository.delete(item);
    }

    // Проверяем, что пользователь - собственник вещи.
    private void checkUserIsOwnerOfItem(long ownerId, long userId) {
        if (ownerId != userId) {
            log.warn("не совпадают id пользователя и собственника вещи.");
            throw new ValidationException("Нельзя изменить чужую вещь.");
        }
    }
}
