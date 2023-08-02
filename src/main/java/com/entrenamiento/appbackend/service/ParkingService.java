package com.entrenamiento.appbackend.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.Holiday;
import com.entrenamiento.appbackend.MessageSourceUtils;
import com.entrenamiento.appbackend.SystemClock;
import com.entrenamiento.appbackend.data.GlobalData;
import com.entrenamiento.appbackend.exception.AppRequestException;
import com.entrenamiento.appbackend.model.CheckingAccount;
import com.entrenamiento.appbackend.model.Parking;
import com.entrenamiento.appbackend.model.Plate;
import com.entrenamiento.appbackend.repository.ParkingRepository;
import com.entrenamiento.appbackend.repository.PlateRepository;

@Service
public class ParkingService {
	
	private final ParkingRepository parkingRepository;
	private final CheckingAccountService checkingAccountService;
	private final PlateRepository plateRepository;
	private final GlobalData globalData;
	private final SystemClock sClock;
	
	public ParkingService(ParkingRepository parkingRepository, CheckingAccountService checkingAccountService, PlateRepository plateRepository, GlobalData globalData, SystemClock sClock) {
		this.parkingRepository = parkingRepository;
		this.checkingAccountService = checkingAccountService;
		this.plateRepository = plateRepository;
		this.globalData = globalData;
		this.sClock = sClock;
	}
	
	public ResponseEntity<List<Parking>> parkings() {
		return ResponseEntity.ok(this.parkingRepository.findAll());
	}
	
	public ResponseEntity<String> startParking(Long id, String plateNumber) {
		
		if (isHoliday() || isWeekend()) {
			throw new AppRequestException(MessageSourceUtils.getMessage("startParking.error.notWorkingToday"));
		}
		
		if (sClock.localTimeNow().isBefore(globalData.getOpeningHour())) {
			throw new AppRequestException(MessageSourceUtils.getMessage("startParking.error.openingHour"));
		}
		
		if (this.parkingRepository.existsByUserIdAndEndParkingIsNull(id)) {
			throw new AppRequestException(MessageSourceUtils.getMessage("startParking.error.alreadyStartedByUser"));
		}
		
		if (this.parkingRepository.existsByPlatePlateAndEndParkingIsNull(plateNumber)) {
			throw new AppRequestException(MessageSourceUtils.getMessage("startParking.error.alreadyStartedByPlate"));
		}
		
		CheckingAccount checkingAccount = this.checkingAccountService.findUserAccount(id);
		if (checkingAccount == null) {
			throw new AppRequestException(MessageSourceUtils.getMessage("startParking.error.accountNotFound"));
		}
		
		if (checkingAccount.getBalance() < globalData.getFractionCost()) {
			throw new AppRequestException(MessageSourceUtils.getMessage("startParking.error.notEnoughBalance"));
		}
		
		Parking parking = new Parking();
		parking.setUser(checkingAccount.getUser());
		
		Optional<Plate> plate = plateRepository.findByPlate(plateNumber);
		
		parking.setPlate(plate.get());
		
		parkingRepository.save(parking);
		return ResponseEntity.ok().body(MessageSourceUtils.getMessage("startParking.ok.started"));
		
	}
	
	public ResponseEntity<String> finishParking(Long id) {
		
		Optional<Parking> optionalParking = this.parkingRepository.findById(id);
		if (optionalParking.isEmpty()) { 
			throw new AppRequestException(MessageSourceUtils.getMessage("finishParking.error.parkingNotFound"));
		}
		
		/*
		 * Esta seccion de codigo se comento para poder finalizar un estacionamiento despues de la hora de cierre, feriado o fin de semana,
		 * para que no quede colgado el estacionamiento.
		 * Para el cobro, se van a tomar las horas en las cuales el sistema esta funcionando, es decir, entre las  8 AM y las 8 PM.
		 * 
		if (isHoliday() || isWeekend()) { 
			throw new AppRequestException("El sistema no funciona en feriados, sabados y domingos."); 
		}
		
		LocalDateTime end = LocalDateTime.now();
		if (end.toLocalTime().isAfter(globalData.getClosingHour())) {
			throw new AppRequestException("El horario de cierre del sistema de estacionamientos es a las 20:00 pm");
		}
		*/
		
		Parking parking = optionalParking.get();
		if (parking.getEnd() != null) { 
			throw new AppRequestException(MessageSourceUtils.getMessage("finishParking.error.parkingAlreadyFinished"));
		}
		
		LocalDateTime endTime = sClock.localDateTimeNow();
		long fractions;
		
		if (endTime.toLocalTime().isAfter(globalData.getClosingHour())) {
			// caso en el que se finaliza el estacionamiento fuera de la hora de funcionamiento del sistema,
			// por lo que se cuentan las horas de estacionamiento hasta las 20 hrs
			fractions = parkingHoursCalculation(parking.getStart(), sClock.localDateNow().atTime(globalData.getClosingHour()));
		} else if (endTime.toLocalTime().isBefore(globalData.getOpeningHour())) {
			// caso en el que se finaliza el estacionamiento fuera de la hora de funcionamiento del sistema,
			// pero antes de la hora de apertura
			fractions = parkingHoursCalculation(parking.getStart(), sClock.localDateNow().minusDays(1).atTime(globalData.getClosingHour()));
		} else {
			// caso en el que se finaliza el estacionamiento dentro de la hora de funcionamiento del sistema,
			// por lo que se usa endTime como valor a pasar a la funcion parkingHoursCalculation().
			fractions = parkingHoursCalculation(parking.getStart(), endTime);
		}
		
		double totalAmount = globalData.getFractionCost() * fractions;
		
		this.checkingAccountService.performTransaction(parking.getUser().getId(), totalAmount);
		
		parking.setEnd(endTime);
		parking.setAmount(totalAmount);
		parkingRepository.save(parking);
		
		return ResponseEntity.ok().body(MessageSourceUtils.getMessage("finishParking.ok.finished"));
		
	}
	
	private long parkingHoursCalculation(LocalDateTime start, LocalDateTime end) {
		// para un sistema que cuenta horas completas se trabajaria dividiendo la cantidad de minutos entre duraciones por 60
		// en cambio, si se aplican cobros con un esquema de fraccionamiento se deberia dividir por la cantidad de minutos que
		// se usa para el fraccionamiento, en el caso de una fraccion de 15 minutos a $2.50 seria duracion.toMinutes()/15
		// luego se podria tomar el valor de duracion.toMinutes() % 15 para que me de los minutos que sobraron y tomarlos con el valor de 15 minutos
		
		double fractions = 0;
		
		Duration duration = Duration.between(start, end);
		// para el caso en el que el estacionamiento inicie y termine en el mismo dia
		if (start.toLocalDate().equals(end.toLocalDate())) {
			fractions = duration.toMinutes() / globalData.getFractionationScheme();
			
		} else {
			// caso en el que inicie en un dia y termine en el siguiente
			long days = duration.toDays();
			
			Duration firstDayDuration = Duration.between(start.toLocalTime(), globalData.getClosingHour());
			Duration lastDayDuration = Duration.between(globalData.getOpeningHour(), end.toLocalTime());
			
			if ( days <= 1) {
				
				fractions = (firstDayDuration.toMinutes() + lastDayDuration.toMinutes()) / globalData.getFractionationScheme();
			
			} else {
				// caso en el que tome mas de un dia (diferentes dias) ej. empieza un lunes y termina un jueves
				long fullDays = 0;
				
				// este if es mas que nada para ver si el estacionamiento tomo tanto dias de un mes como del siguiente
				if (end.getMonth().equals(start.getMonth())) {
					fullDays = (end.getDayOfMonth() - start.getDayOfMonth()) + 1; 
				} else {
					long daysInStartMonth = start.getMonth().length(false) - start.getDayOfMonth() + 1;
					long daysInEndMonth = end.getDayOfMonth();
				    fullDays = daysInStartMonth + daysInEndMonth;
				}
 				
				long inBetweenDays = fullDays - 2;
				double fullHoursToMinutes = ((inBetweenDays * (globalData.getClosingHour().getHour() - globalData.getOpeningHour().getHour())) * 60) / globalData.getFractionationScheme();
				
				fractions = ((firstDayDuration.toMinutes() + lastDayDuration.toMinutes()) / globalData.getFractionationScheme()) + fullHoursToMinutes;
			}

		}
		
		if ((fractions % globalData.getFractionationScheme()) > 0) fractions ++;
		return (long) fractions;
		
	}

	public ResponseEntity<List<Parking>> userParkings(Long id) {
		return ResponseEntity.ok(this.parkingRepository.findAllByUserId(id));
	}
	
	public ResponseEntity<Optional<Parking>> pendingParking(Long id) {
		return ResponseEntity.ok(this.parkingRepository.findByUserIdAndEndParkingIsNull(id));
	}
	
	private boolean isHoliday() {
		LocalDate actualDate = sClock.localDateNow();
		int actualYear = actualDate.getYear();
		return Arrays.stream(Holiday.values()).anyMatch(holiday -> holiday.getDate(actualYear).equals(actualDate));
	}
	
	private boolean isWeekend() {
		LocalDateTime actualDate = sClock.localDateTimeNow();
		if (actualDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || actualDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) return true;
		return false;
	}
	
}
