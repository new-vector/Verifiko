package com.verifico.server.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; 

@Getter
@Setter
@NoArgsConstructor
public class APIResponse<T> {

  private String message;
  private T data;

  public APIResponse(String message, T data) {
    this.message = message;
    this.data = data;
  }
}
