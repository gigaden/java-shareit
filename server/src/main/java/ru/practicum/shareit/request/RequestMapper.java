package ru.practicum.shareit.request;

import ru.practicum.shareit.item.Item;

import java.util.Collection;

public class RequestMapper {

    static RequestDto mapToRequestDto(Request request, Collection<Item> itemAnswers) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .requestor(request.getRequestor().getId())
                .items(itemAnswers)
                .build();
    }
}
