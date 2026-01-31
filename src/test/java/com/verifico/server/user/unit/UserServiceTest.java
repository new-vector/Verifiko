package com.verifico.server.user.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import com.verifico.server.user.User;
import com.verifico.server.user.UserRepository;
import com.verifico.server.user.UserService;
import com.verifico.server.user.dto.ProfileRequest;
import com.verifico.server.user.dto.PublicUserResponse;
import com.verifico.server.user.dto.UserResponse;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock
  UserRepository userRepository;

  @Mock
  SecurityContext securityContext;

  @Mock
  Authentication authentication;

  @InjectMocks
  UserService userService;

  private User mockUser() {
    User user = new User();
    user.setId(1L);
    user.setUsername("JohnDoe123");
    user.setEmail("johndoe2@gmail.com");
    user.setPassword("hashedPass");
    user.setFirstName("John");
    user.setLastName("Doe");
    return user;
  }

  @BeforeEach
  void setup() {
    SecurityContextHolder.setContext(securityContext);
  }

  // me endpoint (check for unauthenticated user trying to get /me where auth =
  // null case, user not found in db case, success path)
  @Test
  void checkForUnauthenticatedUserAccessingProfileEndpoint() {
    when(securityContext.getAuthentication()).thenReturn(null);

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.meEndpoint());

    assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    assertEquals("Unable to find authenticated user", ex.getReason());
  }

  @Test
  void checkForUserNotFoundInDBWhenFetchingProfile() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("JohnDoe123");

    when(userRepository.findByUsername("JohnDoe123")).thenReturn(Optional.empty());

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.meEndpoint());

    assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    assertEquals("Unable to find user associated with that username.", ex.getReason());

  }

  @Test
  void successfullyFetchMeProfile() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("JohnDoe123");

    User user = mockUser();

    when(userRepository.findByUsername("JohnDoe123")).thenReturn(Optional.of(user));

    UserResponse response = userService.meEndpoint();

    assertEquals(user.getId(), response.id());
    assertEquals(user.getUsername(), response.username());
    assertEquals(user.getEmail(), response.email());

  }

  // vieweing other peoples profile endpoint tests:
  // id not found
  // successfully fecthed
  @Test
  void idNotFoundWhenSearchingForSomeoneProfile() {
    when(userRepository.findById(4L)).thenReturn(Optional.empty());

    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> userService.viewSomebodiesProfile(4L));

    assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    assertEquals("A user with that associated id couldn't be found", ex.getReason());
  }

  @Test
  void successfullyFetchingSomebodyProfile() {
    User user = mockUser();
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    PublicUserResponse response = userService.viewSomebodiesProfile(1L);

    assertEquals(user.getUsername(), response.username());
    assertEquals(user.getFirstName(), response.firstName());
    assertEquals(user.getLastName(), response.LastName());
  }

  // update profile endpoint tests:
  // 1. unauthenticated user
  @Test
  void unauthenticatedUserTryingToUpdateProfile() {
    when(securityContext.getAuthentication()).thenReturn(null);

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.updateMyProfile(null));

    assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    assertEquals("Authenticated user not found!", ex.getReason());

  }

  // 2. user not found in db
  @Test
  void userNotFoundInDBWhenUpdatingProfile() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("JohnDoe123");
    when(userRepository.findByUsername("JohnDoe123")).thenReturn(Optional.empty());

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.updateMyProfile(null));

    assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    assertEquals("User couldn't be found", ex.getReason());
  }

  // 3. email already in use
  @Test
  void emailAlreadyInUseWhenUpdatingProfile() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("JohnDoe123");
    User user = mockUser();

    when(userRepository.findByUsername("JohnDoe123")).thenReturn(Optional.of(user));
    when(userRepository.existsByEmail("newemail@gmail.com")).thenReturn(true);

    ProfileRequest request = new ProfileRequest();
    request.setEmail("newemail@gmail.com");

    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> userService.updateMyProfile(request));

    assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    assertEquals("Email already in use", ex.getReason());

    verify(userRepository, never()).save(any());
  }

  // 4. username already in use
  @Test
  void usernameAlreadyInUseWhenUpdatingProfile() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("JohnDoe123");
    User user = mockUser();

    when(userRepository.findByUsername("JohnDoe123")).thenReturn(Optional.of(user));
    when(userRepository.existsByUsername("NewUsername")).thenReturn(true);

    ProfileRequest request = new ProfileRequest();
    request.setUsername("NewUsername");

    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> userService.updateMyProfile(request));

    assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    assertEquals("Username already in use", ex.getReason());

    verify(userRepository, never()).save(any());
  }

  // 5. avatar url not starting with https://
  @Test
  void avatarUrlNotStartWithHttpsWhenUpdatingProfile() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("JohnDoe123");

    User user = mockUser();

    when(userRepository.findByUsername("JohnDoe123")).thenReturn(Optional.of(user));

    ProfileRequest request = new ProfileRequest();
    request.setAvatarUrl("GIGANIGGA");

    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> userService.updateMyProfile(request));

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    assertEquals("Avatar URL must be HTTPS", ex.getReason());

    verify(userRepository, never()).save(any());
  }

  // 6. long avatar url
  @Test
  void avatarUrlTooLongWhenUpdatingProfile() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("JohnDoe123");

    User user = mockUser();

    when(userRepository.findByUsername("JohnDoe123")).thenReturn(Optional.of(user));

    ProfileRequest request = new ProfileRequest();
    String longurl = "https://" + "a".repeat(2050);
    request.setAvatarUrl(longurl);

    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> userService.updateMyProfile(request));

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    assertEquals("Avatar URL too long", ex.getReason());
    verify(userRepository, never()).save(any());
  }

  // 7. successfull partial update
  @Test
  void successfullPartialUpdate() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("JohnDoe123");

    User user = mockUser();

    when(userRepository.findByUsername("JohnDoe123")).thenReturn(Optional.of(user));
    when(userRepository.save(any())).thenReturn(user);

    ProfileRequest request = new ProfileRequest();
    request.setFirstName("Jonnhy");
    request.setEmail("jonnhydoe2@gmail.com");

    UserResponse response = userService.updateMyProfile(request);

    assertNotNull(response);
    assertEquals("jonnhydoe2@gmail.com", response.email());
    assertEquals("Jonnhy", response.firstName());

    verify(userRepository, times(1)).save(any());
  }

  // 8. given email is same as current email (make sure this don't hit db)
  @Test
  void updatedEmailSameAsCurrentEmail() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("JohnDoe123");

    User user = mockUser();

    when(userRepository.findByUsername("JohnDoe123")).thenReturn(Optional.of(user));
    when(userRepository.save(any())).thenReturn(user);

    ProfileRequest request = new ProfileRequest();
    request.setEmail("johndoe2@gmail.com");

    userService.updateMyProfile(request);

    verify(userRepository, never()).existsByEmail(any());
    verify(userRepository, times(1)).save(any());
  }

  // 9. given username is the same as current username (make sure this don't hit
  // db)
  @Test
  void updatedUsernameSameAsCurrentUsername() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("JohnDoe123");

    User user = mockUser();

    when(userRepository.findByUsername("JohnDoe123")).thenReturn(Optional.of(user));
    when(userRepository.save(any())).thenReturn(user);

    ProfileRequest request = new ProfileRequest();
    request.setUsername("JohnDoe123");

    userService.updateMyProfile(request);

    verify(userRepository, never()).existsByUsername(any());
    verify(userRepository, times(1)).save(any());
  }

  // 10. bio blank space/white space -> set to null
  @Test
  void bioBlankSpace() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("JohnDoe123");

    User user = mockUser();

    when(userRepository.findByUsername("JohnDoe123")).thenReturn(Optional.of(user));
    when(userRepository.save(any())).thenReturn(user);

    ProfileRequest request = new ProfileRequest();
    request.setBio("      ");

    UserResponse response = userService.updateMyProfile(request);

    assertEquals(null, response.bio());

    verify(userRepository, times(1)).save(any());
  }

  // 11. Email trimmed and lowercased, username trimmed - Data sanitisation
  @Test
  void userNameAndEmailTrimmedAndEmailLowercased() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("JohnDoe123");

    User user = mockUser();
    when(userRepository.findByUsername("JohnDoe123")).thenReturn(Optional.of(user));
    when(userRepository.save(any())).thenReturn(user);

    ProfileRequest request = new ProfileRequest();
    request.setEmail("  JOHHNNDOEEE22@GMAIL.COM  ");
    request.setUsername("  jooohnDoe23  ");

    UserResponse response = userService.updateMyProfile(request);

    assertNotNull(response);
    assertEquals("johhnndoeee22@gmail.com", response.email());
    assertEquals("jooohnDoe23", response.username());

    verify(userRepository, times(1)).save(any());
  }

}
