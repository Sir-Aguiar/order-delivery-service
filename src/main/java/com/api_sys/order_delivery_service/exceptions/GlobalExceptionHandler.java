package com.api_sys.order_delivery_service.exceptions;

import java.time.Instant;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.api_sys.order_delivery_service.dtos.ApiErrorResponse;
import com.api_sys.order_delivery_service.dtos.ApiErrorResponse.FieldErrorItem;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiErrorResponse> handleResponseStatus(
      ResponseStatusException ex,
      HttpServletRequest request) {
    HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
    if (status == null) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    String message = ex.getReason() != null && !ex.getReason().isBlank()
        ? ex.getReason()
        : status.getReasonPhrase();

    return build(status, message, request, List.of());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(
      MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    List<FieldErrorItem> details = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> new FieldErrorItem(error.getField(), resolveValidationMessage(error.getDefaultMessage())))
        .toList();

    return build(HttpStatus.BAD_REQUEST, "Erro de validação", request, details);
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<ApiErrorResponse> handleHandlerMethodValidation(
      HandlerMethodValidationException ex,
      HttpServletRequest request) {
    List<FieldErrorItem> details = ex.getParameterValidationResults().stream()
        .flatMap(result -> {
          String parameterName = result.getMethodParameter().getParameterName();
          return result.getResolvableErrors().stream()
              .map(error -> new FieldErrorItem(
                  parameterName != null ? parameterName : "parâmetro",
                  resolveValidationMessage(error.getDefaultMessage())));
        })
        .toList();

    return build(HttpStatus.BAD_REQUEST, "Erro de validação", request, details);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
      ConstraintViolationException ex,
      HttpServletRequest request) {
    List<FieldErrorItem> details = ex.getConstraintViolations().stream()
        .map(violation -> new FieldErrorItem(
            violation.getPropertyPath().toString(),
            resolveValidationMessage(violation.getMessage())))
        .toList();

    return build(HttpStatus.BAD_REQUEST, "Erro de validação", request, details);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleUnreadableMessage(
      HttpMessageNotReadableException ex,
      HttpServletRequest request) {
    log.debug("Corpo da requisição inválido: {}", ex.getMessage());
    return build(
        HttpStatus.BAD_REQUEST,
        "JSON inválido ou corpo da requisição ausente",
        request,
        List.of());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex,
      HttpServletRequest request) {
    String parameter = ex.getName();
    String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "valor válido";
    String message = "O parâmetro '%s' possui um valor inválido. Esperado: %s".formatted(parameter, requiredType);

    return build(HttpStatus.BAD_REQUEST, message, request, List.of());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpServletRequest request) {
    String supported = ex.getSupportedHttpMethods() == null || ex.getSupportedHttpMethods().isEmpty()
        ? "nenhum"
        : String.join(", ", ex.getSupportedHttpMethods().stream().map(method -> method.name()).toList());
    String message = "Método %s não suportado para esta rota. Métodos permitidos: %s"
        .formatted(ex.getMethod(), supported);

    return build(HttpStatus.METHOD_NOT_ALLOWED, message, request, List.of());
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNotFound(
      NoResourceFoundException ex,
      HttpServletRequest request) {
    return build(
        HttpStatus.NOT_FOUND,
        "Rota não encontrada: %s %s".formatted(ex.getHttpMethod(), request.getRequestURI()),
        request,
        List.of());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleDataIntegrity(
      DataIntegrityViolationException ex,
      HttpServletRequest request) {
    log.warn("Violação de integridade dos dados: {}", ex.getMostSpecificCause().getMessage());
    return build(
        HttpStatus.CONFLICT,
        "Os dados enviados violam uma restrição do banco (valor duplicado ou referência inválida)",
        request,
        List.of());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleUnexpected(
      Exception ex,
      HttpServletRequest request) {
    log.error("Erro inesperado ao processar {} {}", request.getMethod(), request.getRequestURI(), ex);
    return build(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Erro interno do servidor",
        request,
        List.of());
  }

  private ResponseEntity<ApiErrorResponse> build(
      HttpStatus status,
      String message,
      HttpServletRequest request,
      List<FieldErrorItem> details) {
    ApiErrorResponse body = ApiErrorResponse.builder()
        .timestamp(Instant.now())
        .status(status.value())
        .error(status.getReasonPhrase())
        .message(message)
        .path(request.getRequestURI())
        .details(details == null || details.isEmpty() ? null : details)
        .build();

    return ResponseEntity.status(status).body(body);
  }

  private String resolveValidationMessage(String message) {
    if (message == null || message.isBlank()) {
      return "Valor inválido";
    }
    return message;
  }
}
