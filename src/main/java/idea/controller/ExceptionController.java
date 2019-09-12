package idea.controller;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
  Logger logger = LoggerFactory.getLogger(ExceptionController.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleError(HttpServletRequest request, Exception e)   {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  @ExceptionHandler(value = {IllegalArgumentException.class, MethodArgumentNotValidException.class})
  public ResponseEntity<?> handleValidation(HttpServletRequest request, Exception e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler(WebApplicationException.class)
  public ResponseEntity<?> handleWebapplication(HttpServletRequest request, WebApplicationException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(e.getResponse().getStatus()).body(e.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDenied(HttpServletRequest request, AccessDeniedException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }
}
