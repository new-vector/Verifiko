package com.verifico.server.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.verifico.server.auth.dto.RegisterRequest;
import com.verifico.server.user.User;
import com.verifico.server.user.UserRepository;
import com.verifico.server.user.dto.UserResponse;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public UserResponse register(RegisterRequest request) {

    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already in use");
    }

    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setBio(request.getBio() != null ? request.getBio() : "");
    user.setAvatarUrl(request.getAvatarUrl());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    User savedUser = userRepository.save(user);

    return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getFirstName(),
        savedUser.getLastName(), savedUser.getBio(), savedUser.getAvatarUrl(), savedUser.getJoinedDate());

  }
}
