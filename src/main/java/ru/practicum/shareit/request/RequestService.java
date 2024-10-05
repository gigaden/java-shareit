package ru.practicum.shareit.request;

import java.util.Collection;

public interface RequestService {

    Request create(Long userId, Request request);

    Collection<RequestDto> get(Long userId);

    Collection<RequestDto> getAll(Long userId);

    RequestDto getOne(Long userId, Long requestId);

    void checkRequestIsExist(Long requestId);

}
