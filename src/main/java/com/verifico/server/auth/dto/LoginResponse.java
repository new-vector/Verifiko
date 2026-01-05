package com.verifico.server.auth.dto;

public record LoginResponse(
  Long id,
  String username,
  String email
) {
}
