package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.List;

@Service("requestServiceImpl")
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository,
                              UserService userService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
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
    public Collection<Request> get(Long userId) {
        log.info("Попытка получить все запросы пользователя с id = {}", userId);
        userService.checkUserIsExist(userId);
        Collection<Request> requests = requestRepository.findAllByRequestorId(userId);
        log.info("Коллекция запросов пользователя id = {} успешно передана.", userId);
        return requests;
    }

    @Override
    public Collection<Request> getAll(Long userId) {
        log.info("Попытка пользователя id = {} получить все запросы пользователей", userId);
        Collection<Request> requests = requestRepository.findAll();
        log.info("Коллекция запросов пользователей успешно передана пользователю с id = {}.", userId);
        return requests;
    }

    @Override
    public Request getOne(Long userId, Long requestId) {
        log.info("Попытка пользователся id = {} получить запрос с id = {}", userId, requestId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id=%d не найден", requestId)));
        log.info("Запрос с id = {} успешно передан пользователю с id = {}.", requestId, userId);
        return request;
    }
}
