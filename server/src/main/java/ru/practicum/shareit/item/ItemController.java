package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/items")
public class ItemController {

    @Qualifier("itemServiceImpl")
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemBookingsDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemBookingsDto get(@PathVariable long itemId) {
        return itemService.get(itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item create(@RequestHeader("X-Sharer-User-Id") long userId,
                       @RequestBody ItemDto itemDto) {
        return itemService.create(itemDto, userId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Item update(@RequestHeader("X-Sharer-User-Id") long userId,
                       @RequestBody Item item) {
        return itemService.update(item, userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item patch(@RequestHeader("X-Sharer-User-Id") long userId,
                      @PathVariable long itemId,
                      @RequestBody ItemDto itemDto) {
        return itemService.patch(itemId, userId, itemDto);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> search(@RequestParam String text) {
        return itemService.search(text).stream()
                .map(ItemMapper::mapToItemDto).toList();
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long itemId) {
        itemService.delete(itemId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody Comment comment) {
        return CommentMapper.mapToItemDto(itemService.addComment(userId, itemId, comment));
    }


}
