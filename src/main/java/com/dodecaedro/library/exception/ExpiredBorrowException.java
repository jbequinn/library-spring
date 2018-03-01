package com.dodecaedro.library.exception;

public class ExpiredBorrowException extends Exception {
  public ExpiredBorrowException(String message) {
    super(message);
  }
}
