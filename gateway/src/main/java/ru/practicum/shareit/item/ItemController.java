package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get all items");
        return itemClient.getAll(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@PathVariable long itemId) {
        log.info("Get item, itemId={}", itemId);
        return itemClient.get(itemId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Valid @RequestBody ItemDto itemDto) {
        log.info("Create item, item={}", itemDto);
        return itemClient.create(userId, itemDto);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Valid @RequestBody ItemDto itemDto) {
        log.info("Update item={}", itemDto);
        return itemClient.update(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patch(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PathVariable long itemId,
                                        @Valid @RequestBody ItemDto itemDto) {
        log.info("Patch item={}", itemDto);
        return itemClient.patch(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        log.info("Search text={}", text);
        return itemClient.search(text);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@PathVariable long itemId) {
        log.info("Delet item={}", itemId);
        return itemClient.delete(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody CommentDto commentDto) {
        log.info("Add comment={}", commentDto);
        return itemClient.addComment(itemId, userId, commentDto);
    }


}
