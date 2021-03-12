package com.github.ghrocs.zeebe.demo.business.rest.exception;

import com.github.ghrocs.zeebe.demo.common.exception.DefaultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/** @author Rocs Zhang */
@Slf4j
@ControllerAdvice(basePackages = "com.github.ghrocs.zeebe.demo")
public class GlobalExceptionHandler {

  @ExceptionHandler(DefaultException.class)
  @ResponseBody
  public ResponseEntity defaultException(DefaultException defaultException) {
    log.error("Caught a DefaultException,", defaultException);
    Map<String, Object> result =
        new HashMap() {
          {
            put("timestamp", Timestamp.from(Instant.now()));
            put("status", -1);
            put("message", defaultException.getMessage());
          }
        };
    return ResponseEntity.ok(result);
  }
}
