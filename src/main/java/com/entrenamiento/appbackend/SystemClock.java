package com.entrenamiento.appbackend;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

@Component
public class SystemClock implements Clock {

	@Override
	public LocalTime localTimeNow() {
		return LocalTime.now();
	}

	@Override
	public LocalDate localDateNow() {
		return LocalDate.now();
	}

	@Override
	public LocalDateTime localDateTimeNow() {
		return LocalDateTime.now();
	}

}
