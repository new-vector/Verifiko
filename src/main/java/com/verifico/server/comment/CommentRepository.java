package com.verifico.server.comment;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  @EntityGraph(attributePaths = "author")
  List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long id);
}
