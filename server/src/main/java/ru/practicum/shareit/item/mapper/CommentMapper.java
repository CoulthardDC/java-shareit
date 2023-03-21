package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.dto.CommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "itemId", source = "comment.item.id")
    @Mapping(target = "authorName", source = "comment.author.name")
    CommentDto toCommentDto(Comment comment);
}
