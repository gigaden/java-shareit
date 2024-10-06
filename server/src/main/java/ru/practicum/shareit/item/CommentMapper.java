package ru.practicum.shareit.item;

public class CommentMapper {

    public static CommentDto mapToItemDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(comment.getItem())
                .authorName(comment.getAuthorName().getName())
                .created(comment.getCreated())
                .build();
    }

}
