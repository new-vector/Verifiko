package com.verifico.server.comment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.verifico.server.comment.dto.CommentRequest;
import com.verifico.server.common.dto.APIResponse;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

  @PostMapping("/create/{id}")
  public void createComment(CommentRequest request) {

  }
}
