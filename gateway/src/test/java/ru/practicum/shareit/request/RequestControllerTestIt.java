package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
@DisabledInAotMode
public class RequestControllerTestIt {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestClient requestClient;

    @InjectMocks
    private RequestController requestController;

    private RequestDto requestDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        requestController = new RequestController(requestClient);
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();

        // Создаем тестовый объект RequestDto
        requestDto = new RequestDto();
        requestDto.setDescription("Test request description");
    }

    @Test
    void testCreateRequest() throws Exception {
        when(requestClient.create(anyLong(), any(RequestDto.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Test request description\"}"))
                .andExpect(status().isOk());

        verify(requestClient, times(1)).create(anyLong(), any(RequestDto.class));
    }

    @Test
    void testGetRequestByUserId() throws Exception {
        when(requestClient.get(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(requestClient, times(1)).get(anyLong());
    }

    @Test
    void testGetAllRequests() throws Exception {
        when(requestClient.getAll(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(requestClient, times(1)).getAll(anyLong());
    }

    @Test
    void testGetRequestById() throws Exception {
        when(requestClient.getOne(anyLong(), anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(requestClient, times(1)).getOne(anyLong(), anyLong());
    }
}
