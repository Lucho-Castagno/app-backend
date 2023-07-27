package com.entrenamiento.appbackend.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

public class AppExceptionHandlerTest {

	@Test
	void testHandleAppRequestException() {
		AppExceptionHandler exceptionHandler = new AppExceptionHandler();
		String mensaje = "Mensaje de testeo.";
		AppRequestException appRequestException = new AppRequestException(mensaje);
		
		ResponseEntity<String> response = exceptionHandler.handleAppRequestException(appRequestException);
		
		assertEquals(500, response.getStatusCode().value());
		assertEquals(mensaje, response.getBody());
	}
	
}
