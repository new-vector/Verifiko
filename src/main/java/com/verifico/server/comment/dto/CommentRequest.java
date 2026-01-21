package com.verifico.server.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {

  @NotBlank(message = "Comment cannot be blank")
  @Size(max = 2500, message = "Comment cannot be greater than 2500 characters")
  private String content;
}
