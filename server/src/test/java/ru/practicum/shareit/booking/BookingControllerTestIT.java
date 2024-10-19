package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@DisabledInAotMode
public class BookingControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private Booking booking;
    private BookingDto bookingDto;
    private User user;
    private Item item;

    @BeforeEach
    public void setUp() {

        user = new User();
        user.setId(1L);

        item = new Item();
        item.setId(1L);
        item.setName("Item name");
        item.setDescription("description");

        booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        bookingDto = BookingMapper.mapToBookingDto(booking);
    }

    @Test
    void testGetBooking() throws Exception {
        when(bookingService.get(anyLong(), anyLong())).thenReturn(booking);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(bookingService, times(1)).get(anyLong(), anyLong());
    }

    @Test
    void testGetAllBookings() throws Exception {
        when(bookingService.getAll(anyLong(), any())).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(bookingService, times(1)).getAll(anyLong(), any());
    }

    @Test
    void testGetOwnersBooking() throws Exception {
        when(bookingService.getOwnersBooking(anyLong(), any())).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(bookingService, times(1)).getOwnersBooking(anyLong(), any());
    }

    @Test
    void testCreateBooking() throws Exception {
        when(bookingService.create(anyLong(), any())).thenReturn(booking);

        String bookingRequest = "{\"itemId\":1,\"start\":\"" + LocalDateTime.now().plusDays(1) + "\",\"end\":\"" + LocalDateTime.now().plusDays(2) + "\"}";

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(bookingService, times(1)).create(anyLong(), any());
    }

    @Test
    void testChangeBookingRequestStatus() throws Exception {
        when(bookingService.changeBookingRequestStatus(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(bookingService, times(1)).changeBookingRequestStatus(anyLong(), anyLong(), anyBoolean());
    }
}
