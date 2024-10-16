package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@DisabledInAotMode
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;


    @Test
    public void testGetBooking() throws Exception {
        // Мокаем успешный ответ от bookingClient
        when(bookingClient.get(anyLong(), anyLong())).thenReturn(ResponseEntity.ok().body("booking details"));

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("booking details"));
    }

    @Test
    public void testGetAllBookings() throws Exception {
        // Мокаем успешный ответ от bookingClient
        when(bookingClient.getAll(anyLong(), any(BookingState.class)))
                .thenReturn(ResponseEntity.ok().body("all bookings"));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(content().string("all bookings"));
    }

    @Test
    public void testGetOwnersBookings() throws Exception {
        // Мокаем успешный ответ от bookingClient
        when(bookingClient.getOwnersBooking(anyLong(), any(BookingState.class)))
                .thenReturn(ResponseEntity.ok().body("owner's bookings"));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(content().string("owner's bookings"));
    }

    @Test
    public void testCreateBooking() throws Exception {
        // Мокаем успешный ответ от bookingClient
        when(bookingClient.create(anyLong(), any(BookingCreateDto.class)))
                .thenReturn(ResponseEntity.ok().body("booking created"));

        String jsonRequest = "{"
                + "\"itemId\": 1,"
                + "\"start\": \"2024-10-01T10:00:00\","
                + "\"end\": \"2024-10-05T18:00:00\""
                + "}";

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("booking created"));
    }

    @Test
    public void testChangeBookingStatus() throws Exception {
        // Мокаем успешный ответ от bookingClient
        when(bookingClient.changeBookingRequestStatus(anyLong(), anyLong(), any(Boolean.class)))
                .thenReturn(ResponseEntity.ok().body("booking status changed"));

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string("booking status changed"));
    }
}
