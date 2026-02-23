package com.verifico.server.health;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Health API Endpoints", description = "Basic health check and root endpoints")
public class HealthController {

  @Operation(summary = "Root endpoint")
  @GetMapping("/")
  public Map<String, String> homeString() {
    return Map.of("message", "Verifico");
  }

  @Operation(summary = "Health check")
  @GetMapping("/health")
  public Map<String, String> healthString() {
    return Map.of("status", "ok");
  }
}
