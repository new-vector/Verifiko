package com.verifico.server.comment.dto;

import java.time.Instant;

import com.verifico.server.user.dto.AuthorResponse;

public record CommentResponse(
    Long id,
    String content,
    AuthorResponse author,
    Long postId,
    Instant createdAt) {
}
