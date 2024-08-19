package com.ticket.monolithticketmonster.user.presentation.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;

public class ApiResponse<T> {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private int code;
  private String message;
  private T data;
  private String requestId;

  public ApiResponse(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
    this.requestId = MDC.get("requestId");
  }

  public static <T> ApiResponse<T> success() {
    return new ApiResponse<>(20000, "Success", null);
  }

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(20000, "Success", data);
  }

  public static <T> ApiResponse<T> error(int code, String message) {
    return new ApiResponse<>(code, message, null);
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public T getData() {
    return data;
  }

  public String getRequestId() {
    return requestId;
  }

  public String toJson() {
    try {
      return objectMapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error serializing ApiResponse to JSON", e);
    }
  }
}

