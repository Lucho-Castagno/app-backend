package com.entrenamiento.appbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.entrenamiento.appbackend.Holiday;
import com.entrenamiento.appbackend.SystemClock;
import com.entrenamiento.appbackend.data.GlobalData;
import com.entrenamiento.appbackend.exception.AppRequestException;
import com.entrenamiento.appbackend.model.CheckingAccount;
import com.entrenamiento.appbackend.model.Plate;
import com.entrenamiento.appbackend.model.Usser;
import com.entrenamiento.appbackend.repository.ParkingRepository;
import com.entrenamiento.appbackend.repository.PlateRepository;


@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {
	
	@Mock
	private ParkingRepository parkingRepository;
	
	@Mock
	private CheckingAccountService checkingAccountService;	
	
	@Mock
	private PlateRepository plateRepository;
	
	@Mock
	private SystemClock systemClock;
	
	@Mock
	private GlobalData globalData;
	
	@InjectMocks
	private ParkingService parkingService;
	
	private CheckingAccount accountWithBalance;
	private CheckingAccount accountWithoutBalance;
	private Usser user;
	private Plate plate;
	
	@BeforeEach
	void setUp() {
		this.accountWithBalance = new CheckingAccount();
		this.accountWithoutBalance = new CheckingAccount();
		this.accountWithoutBalance.setBalance(0.0);
		this.user = new Usser("1122916097", "password", "email@email.com");
		this.plate = new Plate("ABC123");
	}
	
	@Test
	void testStartParking_isHoliday() {
		
		when(systemClock.localDateNow()).thenReturn(LocalDate.of(2023, 8, 17));
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.startParking(1L, plate.getPlate()));
		assertThat(e.getMessage()).isEqualTo("El sistema no funciona en feriados, sabados y domingos.");
		
	}
	
	@Test
	void testStartParking_isWeekend() {
		
		when(systemClock.localDateNow()).thenReturn(LocalDate.of(2023, 7, 29));
		when(systemClock.localDateTimeNow()).thenReturn(LocalDateTime.of(2023, 7, 29, 9, 0));
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.startParking(1L, plate.getPlate()));
		assertThat(e.getMessage()).isEqualTo("El sistema no funciona en feriados, sabados y domingos.");
		
	}
	
	@Test
	void testStartParking_beforeOpeningHour() {
		
		firstStage();
		
		when(systemClock.localTimeNow()).thenReturn(LocalTime.of(7, 0));
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.startParking(1L, plate.getPlate()));
		assertThat(e.getMessage()).isEqualTo("El horario de apertura del sistema de estacionamientos es a las 8:00 am");
		
	}
	
	@Test
	void testStartParking_parkingAlreadyStartedForUser() {
		
		firstStage();
		secondStage();
		
		when(parkingRepository.existsByUserIdAndEndParkingIsNull(1L)).thenReturn(true);
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.startParking(1L, plate.getPlate()));
		assertThat(e.getMessage()).isEqualTo("Ya existe un estacionamiento iniciado por este usuario.");
		
	}
	
	@Test
	void testStartParking_parkingAlreadyStartedForPlate() {
		
		firstStage();
		secondStage();
		thirdStage();
		
		when(parkingRepository.existsByPlatePlateAndEndParkingIsNull(plate.getPlate())).thenReturn(true);
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.startParking(1L, plate.getPlate()));
		assertThat(e.getMessage()).isEqualTo("Ya existe un estacionamiento iniciado para esta patente.");
		
	}
	
	@Test
	void testStartParking_accountNotFound() {
		
		firstStage();
		secondStage();
		thirdStage();
		fourthStage();
		
		when(checkingAccountService.findUserAccount(1L)).thenReturn(null);
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.startParking(1L, plate.getPlate()));
		assertThat(e.getMessage()).isEqualTo("Cuenta Corriente no encontrada.");
		
	}
	
	@Test
	void testStartParking_accountWithoutBalance() {
		
		firstStage();
		secondStage();
		thirdStage();
		fourthStage();
		fifthStage();
		fifthStage_bis();
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.startParking(1L, plate.getPlate()));
		assertThat(e.getMessage()).isEqualTo("Saldo insuficiente para iniciar un estacionamiento.");
		
	}
	
	@Test
	void testStartParking_correct() {
		
		firstStage();
		secondStage();
		thirdStage();
		fourthStage();
		fifthStage();
		sixthStage();
		
		when(plateRepository.findByPlate(plate.getPlate())).thenReturn(Optional.of(plate));
		
		ResponseEntity<String> response = parkingService.startParking(1L, plate.getPlate());
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("Estacionamiento iniciado!");
		
	}
	
	// a partir de este punto se encuentran los metodos que engloban el codigo repetido, dado por "etapas de ejecucion" por asi decirlo
	
	void firstStage() {
		when(globalData.getOpeningHour()).thenReturn(LocalTime.of(8, 0));
		when(systemClock.localDateNow()).thenReturn(LocalDate.of(2023, 8, 16));
		when(systemClock.localDateTimeNow()).thenReturn(LocalDateTime.of(2023, 8, 16, 7, 0));
	}
	
	void secondStage() {
		when(systemClock.localTimeNow()).thenReturn(LocalTime.of(9, 0));
	}
	
	void thirdStage() {
		when(parkingRepository.existsByUserIdAndEndParkingIsNull(1L)).thenReturn(false);
	}
	
	void fourthStage() {
		when(parkingRepository.existsByPlatePlateAndEndParkingIsNull(plate.getPlate())).thenReturn(false);
	}
	
	void fifthStage() {
		when(globalData.getFractionCost()).thenReturn(2.50);
	}
	
	void fifthStage_bis() {
		when(checkingAccountService.findUserAccount(1L)).thenReturn(accountWithoutBalance);
	}
	
	void sixthStage() {
		when(checkingAccountService.findUserAccount(1L)).thenReturn(accountWithBalance);
	}
	
}
