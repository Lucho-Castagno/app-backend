package com.entrenamiento.appbackend;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface Clock {
	
	LocalTime localTimeNow();
	
	LocalDate localDateNow();
	
	LocalDateTime localDateTimeNow();
	
}
