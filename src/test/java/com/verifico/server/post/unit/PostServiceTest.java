package com.verifico.server.post.unit;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.verifico.server.post.Category;
import com.verifico.server.post.Post;
import com.verifico.server.post.PostRepository;
import com.verifico.server.post.PostService;
import com.verifico.server.post.Stage;
import com.verifico.server.post.dao.PostSearchDao;
import com.verifico.server.post.dto.PostRequest;
import com.verifico.server.user.User;
import com.verifico.server.user.UserRepository;

// since we don't have any service layer validation for post api
// the unit tests do not feature the missing input/validation checks
// instead we will do those DTO/annotation validation (@NotNull, @Size, etc.) → Test in integration/controller tests.
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
  @Mock
  PostRepository postRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  PostSearchDao postSearchDao;

  @Mock
  SecurityContext securityContext;

  @Mock
  Authentication authentication;

  @InjectMocks
  PostService postService;

  // returning our mock securityContext object:
  @BeforeEach
  void setUp() {
    SecurityContextHolder.setContext(securityContext);
  }

  private PostRequest validPostRequest() {
    PostRequest postRequest = new PostRequest();
    List<String> screenshots = new ArrayList<>(Arrays.asList("FIRST PIC", "SECOND PIC"));
    postRequest.setTitle("TEST POST");
    postRequest.setTagline("TEST TAGLINE");
    postRequest.setCategory(Category.AI);
    postRequest.setStage(Stage.DEVELOPMENT);
    postRequest.setProblemDescription("FACING PROBLEM");
    postRequest.setSolutionDescription("VERY GOOD SOLUTION");
    postRequest.setScreenshotUrls(screenshots);
    postRequest.setLiveDemoUrl("FIND HERE ON WWW.GOOGLE.COM");

    return postRequest;
  };

  private User mockUser() {
    User user = new User();
    user.setId(1L);
    user.setUsername("JohnDoe123");
    user.setEmail("johndoe2@gmail.com");
    user.setPassword("hashedPass");
    return user;
  }

  private Post mockPost() {
    Post post = new Post();
    post.setId(1L);
    post.setAuthor(mockUser());
    post.setTitle("Test Post");
    post.setTagline("Test Tagline");
    post.setCategory(Category.AI);
    post.setStage(Stage.DEVELOPMENT);
    post.setProblemDescription("Test problem");
    post.setSolutionDescription("Test solution");
    post.setScreenshotUrls(new ArrayList<>());
    post.setLiveDemoUrl("http://demo.com");
    post.setCreatedAt(Instant.now());
    post.setUpdatedAt(Instant.now());
    return post;
  }

  // create post test endpoints:
  // user not found/ not authenticated and tries to make post
  // successfull post creation with only required fields, optionals left blank
  // successfull post creation with all fields including optionals

  // get post by id test endpoints:
  // post not found endpoint
  // successfull post fetch

  // get all posts test endpoints:
  // get posts without any filters
  // get all posts filtered by category
  // get all posts with search query

  // update post test endpoints:
  // partial update (only some fields provided)
  // unauthorised user trying to update
  // post not found exception
  // successfull full update

  // delete post test endpoints:
  // unauthorised user trying to delete post
  // post not found exception
  // successfull post deletion

}
