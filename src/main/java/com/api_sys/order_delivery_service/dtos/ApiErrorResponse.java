package com.api_sys.order_delivery_service.dtos;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiErrorResponse {

  private Instant timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
  private List<FieldErrorItem> details;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FieldErrorItem {
    private String field;
    private String message;
  }
}
