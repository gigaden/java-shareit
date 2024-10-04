package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return List.of();
    }

    @Override
    public Collection<Request> getAll() {
        return List.of();
    }

    @Override
    public Request getOne(Long requestId) {
        return null;
    }
}
