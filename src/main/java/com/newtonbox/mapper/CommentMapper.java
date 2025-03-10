package com.newtonbox.mapper;

import com.newtonbox.Models.Comment;
import com.newtonbox.dto.CommentDTO;

import java.time.LocalDateTime;

public class CommentMapper {

    public static CommentDTO toDTO (Comment comment){
        if(comment == null) return null;

        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(UserMapper.toDTO(comment.getUser()))
                .timestamp(comment.getTimestamp())
                .build();
    }

    public static Comment toEntity(CommentDTO commentDTO){
        if(commentDTO == null) return null;
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setContent(commentDTO.getContent());
        comment.setUser(UserMapper.toEntity(commentDTO.getUser()));
        comment.setTimestamp(commentDTO.getTimestamp() != null ? commentDTO.getTimestamp() : LocalDateTime.now());

        return comment;
    }
}
