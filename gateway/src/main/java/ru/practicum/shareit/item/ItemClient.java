package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getAll(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> get(long itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> create(long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> update(long userId, ItemDto itemDto) {
        return put("", userId, itemDto);
    }

    public ResponseEntity<Object> patch(long userId, long itemId, ItemDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }


    public ResponseEntity<Object> search(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", null, parameters);
    }

    public ResponseEntity<Object> addComment(long itemId, long userId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

    public ResponseEntity<Object> delete(long itemId) {
        return delete("/" + itemId);
    }
}
