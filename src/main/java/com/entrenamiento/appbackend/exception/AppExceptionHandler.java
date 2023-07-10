package com.entrenamiento.appbackend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {
	
	@ExceptionHandler(AppRequestException.class)
	public ResponseEntity<String> handleAppRequestException(AppRequestException e) {
		return ResponseEntity.internalServerError().body(e.getMessage());
	}
	
}
