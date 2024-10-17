package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @InjectMocks
    RequestServiceImpl requestService;

    @Mock
    RequestRepository requestRepository;

    @Mock
    UserService userService;

    @Mock
    ItemRepository itemRepository;


    @Test
    void create_RequestWhenUserIsValidThenRequestCreated() {
        long userId = 1L;
        User user = User.builder().id(userId).name("userName").email("mail@mail.ru").build();
        Request request = Request.builder().description("request").build();

        Mockito
                .doNothing().when(userService).checkUserIsExist(userId);
        Mockito
                .when(requestRepository.save(request)).thenReturn(request);
        Mockito
                .when(userService.get(userId)).thenReturn(user);

        requestService.create(userId, request);

        Mockito.verify(userService, Mockito.times(1)).checkUserIsExist(userId);
        Mockito.verify(userService, Mockito.times(1)).get(userId);
        Mockito.verify(requestRepository, Mockito.times(1)).save(request);

    }

    @Test
    void get_RequestWhenUserIsExistThenCollectionOfRequestsIsReturned() {
        long userId = 1L;
        long requestId = 1L;
        User user = User.builder().id(userId).name("userName").email("mail@mail.ru").build();
        Collection<Request> requests = List.of(Request
                .builder().id(requestId).description("request").requestor(user).build());


        Mockito.doNothing().when(userService).checkUserIsExist(userId);
        Mockito
                .when(requestRepository.findAllByRequestorId(userId, Sort.by(Sort.Direction.DESC, "created")))
                .thenReturn(requests);
        Mockito
                .when(itemRepository.findAllByRequestId(requestId))
                .thenReturn(List.of(Item.builder()
                        .name("Item")
                        .description("Description")
                        .available(true)
                        .requestId(requestId).build()));

        Collection<RequestDto> actualRequests = requestService.get(userId);

        assertEquals(actualRequests.size(), 1);


    }

    @Test
    void getAll_WhenCollectionRequestsIsExistThenReturned() {
        long userId = 1L;
        long requestId = 1L;
        User user = User.builder().id(userId).name("userName").email("mail@mail.ru").build();
        List<Request> requests = List.of(Request
                .builder().id(requestId).description("request").requestor(user).build());

        Mockito
                .when(requestRepository.findAll(Sort.by(Sort.Direction.DESC, "created")))
                .thenReturn(requests);
        Mockito
                .when(itemRepository.findAllByRequestId(requestId))
                .thenReturn(List.of(Item.builder()
                        .name("Item")
                        .description("Description")
                        .available(true)
                        .requestId(requestId).build()));


        Collection<RequestDto> actualRequests = requestService.getAll(userId);

        assertEquals(actualRequests.size(), 1);
    }

    @Test
    void getOne_WhenRequestIsExistThenReturned() {
        long userId = 1L;
        long requestId = 1L;
        User user = User.builder().id(userId).name("userName").email("mail@mail.ru").build();
        Request request = Request.builder().id(requestId).description("request").requestor(user).build();

        Mockito
                .when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        Mockito
                .when(itemRepository.findAllByRequestId(requestId))
                .thenReturn(List.of(Item.builder()
                        .name("Item")
                        .description("Description")
                        .available(true)
                        .requestId(requestId).build()));

        RequestDto actualRequest = requestService.getOne(userId, requestId);

        Mockito.verify(requestRepository, Mockito.times(1)).findById(requestId);

        assertEquals(request.getDescription(), actualRequest.getDescription());
        assertEquals(userId, actualRequest.getRequestor());

    }

    @Test
    void getOne_WhenRequestIsNotExistThenNotFoundException() {
        long userId = 1L;
        long requestId = 1L;
        User user = User.builder().id(userId).name("userName").email("mail@mail.ru").build();
        Request request = Request.builder().id(requestId).description("request").requestor(user).build();

        Mockito
                .when(requestRepository.findById(requestId)).thenThrow(new NotFoundException("Запрос не найден."));

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> requestService.getOne(userId, requestId));


        Mockito.verify(requestRepository, Mockito.times(1)).findById(requestId);
        Mockito.verify(itemRepository, Mockito.never()).findAllByRequestId(requestId);
        assertEquals("Запрос не найден.", exception.getMessage());


    }
}