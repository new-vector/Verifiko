package com.verifico.server.auth;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.verifico.server.user.User;
import com.verifico.server.user.UserRepository;
import com.verifico.server.user.dto.UserResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public AuthController(UserRepository userRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  @PostMapping("/register")
  public UserResponse register(@RequestBody User user) {

    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use");
    }

    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setJoinedDate(LocalDate.now());

    User savedUser = userRepository.save(user);

    return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getFirstName(),
        savedUser.getLastName(), savedUser.getBio(), savedUser.getAvatarUrl(), savedUser.getJoinedDate());
  };
}
