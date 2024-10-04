package ru.practicum.shareit.request;

import java.util.Collection;

public interface RequestService {

    Request create(Long userId, Request request);

    Collection<Request> get(Long userId);

    Collection<Request> getAll(Long userId);

    Request getOne(Long userId, Long requestId);

}
