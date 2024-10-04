package ru.practicum.shareit.request;

import java.util.Collection;

public interface RequestService {

    Request create(Long userId, Request request);

    Collection<Request> get(Long userId);

    Collection<Request> getAll();

    Request getOne(Long requestId);

}
