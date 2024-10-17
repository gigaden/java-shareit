package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exception.BookingAccessException;
import ru.practicum.shareit.exception.BookingValidateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking booking;
    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("User");

        item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setOwner(user);
        item.setAvailable(true);

        booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.WAITING);
    }

    @Test
    void testGetBookingSuccess() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Booking foundBooking = bookingService.get(1L, 1L);

        assertNotNull(foundBooking);
        assertEquals(booking.getId(), foundBooking.getId());
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetBookingThrowsNotFoundException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.get(1L, 1L));
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    void testCreateBookingSuccess() {
        when(userService.get(anyLong())).thenReturn(user);
        when(itemService.getById(anyLong())).thenReturn(item);

        // Настраиваем поведение save() для установки ID после сохранения
        doAnswer(invocation -> {
            Booking savedBooking = invocation.getArgument(0);
            savedBooking.setId(1L);  // Устанавливаем ID
            return savedBooking;
        }).when(bookingRepository).save(any(Booking.class));

        BookingCreateDto bookingDto = new BookingCreateDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        Booking createdBooking = bookingService.create(1L, bookingDto);

        assertNotNull(createdBooking);
        assertEquals(1L, createdBooking.getId());  // Проверяем, что ID был установлен
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void testCreateBookingThrowsBookingValidationExceptionForUnavailableItem() {
        item.setAvailable(false);
        when(itemService.getById(anyLong())).thenReturn(item);

        BookingCreateDto bookingDto = new BookingCreateDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        assertThrows(BookingValidateException.class, () -> bookingService.create(1L, bookingDto));
    }

    @Test
    void testChangeBookingStatusSuccess() {
        when(userService.get(anyLong())).thenReturn(user);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Booking updatedBooking = bookingService.changeBookingRequestStatus(1L, 1L, true);

        assertNotNull(updatedBooking);
        assertEquals(BookingStatus.APPROVED, updatedBooking.getStatus());
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void testChangeBookingStatusThrowsBookingAccessException() {
        User otherUser = new User();
        otherUser.setId(2L);
        when(userService.get(anyLong())).thenReturn(otherUser);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(BookingAccessException.class, () -> bookingService.changeBookingRequestStatus(2L, 1L, true));
    }
}
