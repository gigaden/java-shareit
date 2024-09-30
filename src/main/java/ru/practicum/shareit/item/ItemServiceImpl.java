package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BookingValidateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.ValidationNullException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service("itemServiceImpl")
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    @Qualifier("userServiceImpl")
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(UserService userService, ItemRepository itemRepository,
                           BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.userService = userService;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Collection<Item> getAll() {
        log.info("Попытка получить все вещи.");
        List<Item> items = itemRepository.findAll();
        log.info("Коллекция вещей успешно передана.");
        return items;
    }

    @Override
    public Collection<ItemBookingsDto> getAllUserItems(Long userId) {
        userService.checkUserIsExist(userId);
        log.info("Получаем коллекцию всех вещей пользователя с id={}.", userId);
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        List<ItemBookingsDto> itemBookingsDtos = new ArrayList<>();
        for (Item item : items) {
            List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStart(item.getId(), Limit.of(2));
            Collection<Comment> comments = getComments(item.getId());
            ItemBookingsDto itemBookingsDto = ItemMapper.mapToItemBookingDto(item, bookings, comments);
            itemBookingsDtos.add(itemBookingsDto);
        }
        log.info("Коллекция вещей пользователя с id={} успешно передана.", userId);
        return itemBookingsDtos;
    }

    public Collection<Comment> getComments(Long itemId) {
        log.info("Попытка получить все комментарии по вещи id = {}.", itemId);
        checkItemIsExist(itemId);
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        if (comments.isEmpty()) {
            log.info("Комментарии по вещи id = {} отсутствуют", itemId);
            return comments;
        }
        log.info("Комментарии по вещи id = {} успешно переданы.", itemId);
        return comments;
    }

    @Override
    public ItemBookingsDto get(Long id) {
        log.info("Попытка получить вещь с id={}", id);
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id=%d не найдена", id)));
        List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStart(item.getId(), Limit.of(2));
        Collection<Comment> comments = getComments(id);
        ItemBookingsDto itemBookingsDto = ItemMapper.mapToItemBookingDto(item, bookings, comments);
        log.info("Вещь с id = {} успешно передана.", id);
        return itemBookingsDto;
    }

    @Override
    public Item getById(Long id) {
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
        checkItemIsExist(id);
        itemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Comment addComment(Long userId, Long itemId, Comment comment) {
        log.info("Попытка пользователя id = {} добавить отзыв вещи id = {}.", userId, itemId);
        User user = userService.get(userId);
        Item item = getById(itemId);
        Optional<Booking> booking = bookingRepository.findBookingByItemIdAndBookerId(itemId, userId);
        if (booking.isEmpty()) {
            log.warn("Пользователь id = {} не брал в аренду вещь id = {}", userId, itemId);
            throw new ValidationException("Оставить отзыв можно только по вещи, которую вы брали в аренду.");
        }
        if (booking.get().getEnd().isAfter(LocalDateTime.now().minusSeconds(1))) {
            log.warn("Пользователь id = {} ещё не закончил аренду вещи id = {}", userId, itemId);
            throw new BookingValidateException("Оставить отзыв можно только после окончания аренды.");
        }
        comment.setItem(item);
        comment.setAuthorName(user);
        commentRepository.save(comment);
        log.info("Отзыв пользователя id = {} для вещи id = {} успешно добавлен", userId, itemId);
        return comment;
    }

    // Проверяем, что пользователь - собственник вещи.
    private void checkUserIsOwnerOfItem(long ownerId, long userId) {
        if (ownerId != userId) {
            log.warn("не совпадают id пользователя и собственника вещи.");
            throw new ValidationException("Нельзя изменить чужую вещь.");
        }
    }

    // Проверяем, что вещь существует
    public void checkItemIsExist(Long id) {
        log.info("Проверяем существует ли вещь с id={}", id);
        if (itemRepository.findById(id).isEmpty()) {
            throw new NotFoundException(String.format("Вещь с id=%d не найдена", id));
        }
        log.info("Вещь с id = {} существует.", id);
    }
}
