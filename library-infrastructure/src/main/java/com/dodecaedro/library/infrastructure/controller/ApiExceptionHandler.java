package com.dodecaedro.library.infrastructure.controller;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiErrorResponse handleNotFoundException(EntityNotFoundException ex) {
		return ApiErrorResponse.builder()
			.status(HttpStatus.NOT_FOUND)
			.error_code("NOT_FOUND")
			.message(ex.getLocalizedMessage())
			.build();
	}

	@Data
	@Builder
	static class ApiErrorResponse {
		private HttpStatus status;
		private String error_code;
		private String message;
		private String detail;
	}
}
