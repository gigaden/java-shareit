package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DisabledInAotMode
class RequestControllerTest {

    @InjectMocks
    private RequestController requestController;

    @Mock
    private RequestServiceImpl requestService;

    @Test
    void getAll() {
        long userId = 1L;

        assertTrue(requestController.getAll(userId).isEmpty(), "Список юзеров не пуст");

        Collection<RequestDto> expectedRequests = List.of(RequestDto.builder().description("description").build());
        Mockito.when(requestService.getAll(userId)).thenReturn(expectedRequests);

        Collection<RequestDto> response = requestController.getAll(userId);

        assertEquals(expectedRequests, response, "Вернулся неверный список юзеров.");
    }
}