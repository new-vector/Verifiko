package com.verifico.server.user.dto;

import java.time.LocalDate;

public record UserResponse(
  Long id,
  String username,
  String email,
  String firstName,
  String LastName,
  String bio,
  String avatarUrl,
  LocalDate joinedDate
) {
};
