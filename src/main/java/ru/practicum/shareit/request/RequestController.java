package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping(path = "/requests")
public class RequestController {

    @Qualifier("requestServiceImpl")
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Request create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Valid @RequestBody Request request) {
        return requestService.create(userId, request);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Request> get(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.get(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Request> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getAll(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public Request getOne(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable Long requestId ) {
        return requestService.getOne(userId, requestId);
    }


}
