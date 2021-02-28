package com.github.ghrocs.zeebe.demo.base.exception;

/** @author Rocs Zhang */
public class DefaultException extends RuntimeException {

  public DefaultException(String message) {
    super(message);
  }

  public DefaultException(String message, Throwable cause) {
    super(message, cause);
  }
}
