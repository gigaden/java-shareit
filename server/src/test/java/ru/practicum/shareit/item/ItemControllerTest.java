package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.User;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@DisabledInAotMode
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemServiceImpl itemService;

    @InjectMocks
    private ItemController itemController;

    private Item item;
    private ItemBookingsDto itemBookingsDto;
    private ItemDto itemDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        itemController = new ItemController(itemService);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();

        // Создание тестовых объектов
        item = new Item();
        item.setId(1L);
        item.setName("Item name");
        item.setRequestId(1L);

        itemBookingsDto = new ItemBookingsDto();
        itemBookingsDto.setId(1L);

        itemDto = new ItemDto();
        itemDto.setId(1L);
    }

    @Test
    void testGetAllItems() throws Exception {
        when(itemService.getAllUserItems(anyLong())).thenReturn(Collections.singletonList(itemBookingsDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(itemService, times(1)).getAllUserItems(anyLong());
    }

    @Test
    void testGetItemById() throws Exception {
        when(itemService.get(anyLong())).thenReturn(itemBookingsDto);

        mockMvc.perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(itemService, times(1)).get(anyLong());
    }

    @Test
    void testCreateItem() throws Exception {
        when(itemService.create(any(ItemDto.class), anyLong())).thenReturn(item);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Item\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(itemService, times(1)).create(any(ItemDto.class), anyLong());
    }

    @Test
    void testUpdateItem() throws Exception {
        when(itemService.update(any(Item.class), anyLong())).thenReturn(item);

        mockMvc.perform(put("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Updated Item\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(itemService, times(1)).update(any(Item.class), anyLong());
    }

    @Test
    void testPatchItem() throws Exception {
        when(itemService.patch(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(item);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Patched Item\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(itemService, times(1)).patch(anyLong(), anyLong(), any(ItemDto.class));
    }

    @Test
    void testSearchItems() throws Exception {
        when(itemService.search(anyString())).thenReturn(Collections.singletonList(item));

        mockMvc.perform(get("/items/search")
                        .param("text", "Item")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(itemService, times(1)).search(anyString());
    }

    @Test
    void testDeleteItem() throws Exception {
        doNothing().when(itemService).delete(anyLong());

        mockMvc.perform(delete("/items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService, times(1)).delete(anyLong());
    }

    @Test
    void testAddComment() throws Exception {
        Comment comment = new Comment();
        comment.setText("New Comment");
        comment.setAuthorName(User.builder().name("name").email("email@mail.ru").build());

        CommentDto commentDto = new CommentDto();
        commentDto.setText("New Comment");

        when(itemService.addComment(anyLong(), anyLong(), any(Comment.class))).thenReturn(comment);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"New Comment\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("New Comment"));

        verify(itemService, times(1)).addComment(anyLong(), anyLong(), any(Comment.class));
    }
}
