package ru.practicum.shareit.item;

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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@DisabledInAotMode
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemClient itemClient;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        itemController = new ItemController(itemClient);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void testGetAll() throws Exception {
        // Мокаем успешный ответ от itemClient
        when(itemClient.getAll(anyLong())).thenReturn(ResponseEntity.ok().body("items"));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("items"));
    }

    @Test
    public void testGetItem() throws Exception {
        // Мокаем успешный ответ от itemClient
        when(itemClient.get(anyLong())).thenReturn(ResponseEntity.ok().body("item"));

        mockMvc.perform(get("/items/{itemId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("item"));
    }

    @Test
    public void testCreateItem() throws Exception {
        // Мокаем успешный ответ от itemClient
        when(itemClient.create(anyLong(), any(ItemDto.class)))
                .thenReturn(ResponseEntity.ok().body("item created"));

        String jsonRequest = "{"
                + "\"name\": \"Item\","
                + "\"description\": \"Item description\""
                + "}";

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("item created"));
    }

    @Test
    public void testUpdateItem() throws Exception {
        // Мокаем успешный ответ от itemClient
        when(itemClient.update(anyLong(), any(ItemDto.class)))
                .thenReturn(ResponseEntity.ok().body("item updated"));

        String jsonRequest = "{"
                + "\"id\": 1,"
                + "\"name\": \"Updated Item\","
                + "\"description\": \"Updated description\""
                + "}";

        mockMvc.perform(put("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("item updated"));
    }

    @Test
    public void testPatchItem() throws Exception {
        // Мокаем успешный ответ от itemClient
        when(itemClient.patch(anyLong(), anyLong(), any(ItemDto.class)))
                .thenReturn(ResponseEntity.ok().body("item patched"));

        String jsonRequest = "{"
                + "\"id\": 1,"
                + "\"name\": \"Patched Item\","
                + "\"description\": \"Patched description\""
                + "}";

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("item patched"));
    }

    @Test
    public void testDeleteItem() throws Exception {
        // Мокаем успешный ответ от itemClient
        when(itemClient.delete(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/items/{itemId}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchItems() throws Exception {
        // Мокаем успешный ответ от itemClient
        when(itemClient.search(any())).thenReturn(ResponseEntity.ok().body("found items"));

        mockMvc.perform(get("/items/search")
                        .param("text", "search text"))
                .andExpect(status().isOk())
                .andExpect(content().string("found items"));
    }

    @Test
    public void testAddComment() throws Exception {
        // Мокаем успешный ответ от itemClient
        when(itemClient.addComment(anyLong(), anyLong(), any()))
                .thenReturn(ResponseEntity.ok().body("comment added"));

        String jsonRequest = "{"
                + "\"text\": \"Nice item!\""
                + "}";

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("comment added"));
    }
}
