package com.verifico.server.post;

import org.springframework.stereotype.Service;

import com.verifico.server.post.dto.PostResponse;
import com.verifico.server.post.dto.PostRequest;

import jakarta.transaction.Transactional;

@Service
public class PostService {
  
  @Transactional
  public PostResponse createPost(PostRequest request){
    
  }
}
