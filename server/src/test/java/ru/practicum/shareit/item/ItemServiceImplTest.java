package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void getAll_ShouldReturnListOfItems() {
        // Создаем тестовые данные
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");

        List<Item> itemList = List.of(item1, item2);

        // Мокируем поведение репозитория
        when(itemRepository.findAll()).thenReturn(itemList);

        // Вызываем метод сервиса
        List<Item> result = (List<Item>) itemService.getAll();

        // Проверяем результат и вызовы
        verify(itemRepository, times(1)).findAll();
        assertEquals(2, result.size());
        assertEquals(item1, result.get(0));
        assertEquals(item2, result.get(1));
    }

    @Test
    void create_ShouldReturnCreatedItem() {
        // Данные для теста
        long userId = 1L;
        User user = new User();
        user.setId(userId);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("New Item");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);

        Item newItem = new Item();
        newItem.setId(1L);
        newItem.setName("New Item");
        newItem.setDescription("Description");

        // Мокируем поведение
        when(userService.get(userId)).thenReturn(user);
        when(itemRepository.save(any(Item.class))).thenReturn(newItem);

        // Вызываем метод сервиса
        Item result = itemService.create(itemDto, userId);

        // Проверяем результат и вызовы
        verify(userService, times(1)).get(userId);
        verify(itemRepository, times(1)).save(any(Item.class));
        assertEquals("New Item", result.getName());
    }

    @Test
    void get_ShouldReturnItem_WhenItemExists() {
        // Данные для теста
        long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setName("Test Item");

        // Мокируем поведение репозитория
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // Вызываем метод сервиса
        Item result = itemService.getById(itemId);

        // Проверяем результат и вызовы
        verify(itemRepository, times(1)).findById(itemId);
        assertEquals(item, result);
    }

    @Test
    void get_ShouldThrowNotFoundException_WhenItemDoesNotExist() {
        // Данные для теста
        long itemId = 1L;

        // Мокируем поведение репозитория
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Вызываем метод сервиса и ожидаем исключение
        assertThrows(NotFoundException.class, () -> itemService.getById(itemId));

        // Проверяем вызовы
        verify(itemRepository, times(1)).findById(itemId);
    }
}
