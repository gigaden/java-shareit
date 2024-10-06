package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;

@Service("requestServiceImpl")
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "created");

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository,
                              UserService userService,
                              ItemRepository itemRepository) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public Request create(Long userId, Request request) {
        log.info("Попытка создать запрос на вещь пользователем id = {}", userId);
        userService.checkUserIsExist(userId);
        request.setRequestor(userService.get(userId));
        requestRepository.save(request);
        log.info("Запрос на вещь создан.");
        return request;
    }

    @Override
    public Collection<RequestDto> get(Long userId) {
        log.info("Попытка получить все запросы пользователя с id = {}", userId);
        userService.checkUserIsExist(userId);
        Collection<RequestDto> requests = requestRepository.findAllByRequestorId(userId, sort)
                .stream()
                .map(el -> RequestMapper.mapToRequestDto(el, itemRepository.findAllByRequestId(el.getId())))
                .toList();
        log.info("Коллекция запросов пользователя id = {} успешно передана.", userId);
        return requests;
    }

    @Override
    public Collection<RequestDto> getAll(Long userId) {
        log.info("Попытка пользователя id = {} получить все запросы пользователей", userId);
        Collection<RequestDto> requests = requestRepository.findAll(sort)
                .stream()
                .map(el -> RequestMapper.mapToRequestDto(el, itemRepository.findAllByRequestId(el.getId())))
                .toList();
        log.info("Коллекция запросов пользователей успешно передана пользователю с id = {}.", userId);
        return requests;
    }

    @Override
    public RequestDto getOne(Long userId, Long requestId) {
        log.info("Попытка пользователся id = {} получить запрос с id = {}", userId, requestId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id=%d не найден", requestId)));

        log.info("Запрос с id = {} успешно передан пользователю с id = {}.", requestId, userId);
        return RequestMapper.mapToRequestDto(request, itemRepository.findAllByRequestId(requestId));
    }

    @Override
    public void checkRequestIsExist(Long requestId) {
        log.info("Проверяем существует ли запрос с id={}", requestId);
        if (requestRepository.findById(requestId).isEmpty()) {
            throw new NotFoundException(String.format("Запрос с id=%d не найден", requestId));
        }
        log.info("Запрос с id = {} существует.", requestId);
    }
}
