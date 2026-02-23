package com.verifico.server.comment;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.verifico.server.comment.dto.CommentRequest;
import com.verifico.server.comment.dto.CommentResponse;
import com.verifico.server.common.dto.APIResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Comment API Endpoints", description = "Endpoints for posting, fetching, deleting and interacting with comments on posts")
public class CommentController {

  private final CommentService commentService;

  @Operation(summary = "Post a comment on a post")
  @PostMapping("/posts/{id}/comments")
  public ResponseEntity<APIResponse<CommentResponse>> createComment(@Valid @RequestBody CommentRequest request,
      @PathVariable("id") Long id) {
    CommentResponse response = commentService.postComment(request, id);

    return ResponseEntity.status(HttpStatus.CREATED.value())
        .body(new APIResponse<>("Comment Successfully Posted!", response));
  }

  @Operation(summary = "Get all comments for a post with pagination")
  @GetMapping("/posts/{id}/comments")
  public ResponseEntity<APIResponse<Page<CommentResponse>>> fetchAllCommentsForPost(@PathVariable("id") Long id,
      @RequestParam(value = "page", defaultValue = "0") int page) {

    if (page < 0 || page > 20) {
      page = 0;
    }

    int size = 15;

    Page<CommentResponse> response = commentService.getAllCommentsForPost(id, page, size);

    return ResponseEntity.ok()
        .body(new APIResponse<>("All comments fecthed!", response));

  }

  @Operation(summary = "Delete a comment by ID")
  @DeleteMapping("/comments/{id}")
  public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id) {
    commentService.deleteMyComment(id);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Mark a comment as helpful")
  @PostMapping("/comments/{id}/mark-helpful")
  public ResponseEntity<APIResponse<String>> markCommentHelpful(@PathVariable("id") Long id) {

    commentService.markCommentHelpful(id);

    return ResponseEntity.status(HttpStatus.CREATED.value())
        .body(new APIResponse<>("Successfully marked comment helpful!", null));
  }
}
