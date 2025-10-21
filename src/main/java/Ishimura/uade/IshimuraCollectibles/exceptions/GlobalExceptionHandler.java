package Ishimura.uade.IshimuraCollectibles.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 1) Todas tus excepciones de dominio comparten una base (DomainException)
  @ExceptionHandler(DomainException.class)
  public ProblemDetail handleDomain(DomainException ex, HttpServletRequest req) {
    HttpStatus status = switch (ex.getCode()) {
      case CATEGORY_ALREADY_EXISTS, LINE_ALREADY_EXISTS, PROMOTION_CONFLICT -> HttpStatus.CONFLICT; // 409
      case CATEGORY_NOT_FOUND, BRAND_NOT_FOUND, LINE_NOT_FOUND -> HttpStatus.NOT_FOUND; // 404
      case INVALID_DOMAIN_STATE -> HttpStatus.UNPROCESSABLE_ENTITY; // 422
      default -> HttpStatus.UNPROCESSABLE_ENTITY;
    };
    return pd(status, "Domain error", ex.getMessage(), ex.getCode().name(),
        req.getRequestURI(), null);
  }

  // 2) Bean Validation (@Valid) de tus DTOs → 400 con listado de campos
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Validation error");
    pd.setProperty("code", "VALIDATION_ERROR");
    pd.setProperty("instance", req.getRequestURI());
    pd.setProperty("errors", ex.getBindingResult().getFieldErrors().stream()
        .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
        .toList());
    return pd;
  }

  // 3) Seguridad
  @ExceptionHandler(AuthenticationException.class)
  public ProblemDetail handleAuth(AuthenticationException ex, HttpServletRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
    pd.setTitle("Unauthorized");
    pd.setDetail("Authentication required");
    pd.setProperty("code", "AUTH_UNAUTHORIZED");
    pd.setProperty("instance", req.getRequestURI());
    return pd;
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail handleForbidden(AccessDeniedException ex, HttpServletRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
    pd.setTitle("Forbidden");
    pd.setDetail("You don't have permission to perform this action");
    pd.setProperty("code", "AUTH_FORBIDDEN");
    pd.setProperty("instance", req.getRequestURI());
    return pd;
  }

  // 4) Constraints de DB no previstas (fallback técnico)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    pd.setTitle("Data integrity violation");
    pd.setDetail("Operation violates a database constraint");
    pd.setProperty("code", "DATA_INTEGRITY_VIOLATION");
    pd.setProperty("instance", req.getRequestURI());
    return pd;
  }

  // 5) Fallback genérico
  @ExceptionHandler(Exception.class)
  public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    pd.setTitle("Unexpected error");
    pd.setProperty("code", "UNEXPECTED_ERROR");
    pd.setProperty("instance", req.getRequestURI());
    return pd;
  }

  // 6) Multipart: parte requerida faltante (p.ej., 'file') -> 400
  @ExceptionHandler(MissingServletRequestPartException.class)
  public ProblemDetail handleMissingPart(MissingServletRequestPartException ex, HttpServletRequest req) {
    String part = ex.getRequestPartName();
    return pd(HttpStatus.BAD_REQUEST,
        "Bad request",
        "Missing required part '" + part + "'",
        "BAD_REQUEST",
        req.getRequestURI(),
        null);
  }

  private ProblemDetail pd(HttpStatus status, String title, String detail,
      String code, String instance, Map<String, ?> extra) {
    var p = ProblemDetail.forStatus(status);
    p.setTitle(title);
    if (detail != null)
      p.setDetail(detail);
    p.setProperty("code", code);
    p.setProperty("instance", instance);
    if (extra != null && !extra.isEmpty())
      p.setProperty("errors", extra);
    return p;
  }

}
