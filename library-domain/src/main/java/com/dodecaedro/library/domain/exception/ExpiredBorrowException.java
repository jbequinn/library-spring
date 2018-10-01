package com.dodecaedro.library.domain.exception;

public class ExpiredBorrowException extends Exception {
	public ExpiredBorrowException(String message) {
		super(message);
	}
}
