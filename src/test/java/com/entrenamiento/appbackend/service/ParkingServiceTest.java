package com.entrenamiento.appbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.entrenamiento.appbackend.SystemClock;
import com.entrenamiento.appbackend.data.GlobalData;
import com.entrenamiento.appbackend.exception.AppRequestException;
import com.entrenamiento.appbackend.model.CheckingAccount;
import com.entrenamiento.appbackend.model.Parking;
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
	
	@Captor
	ArgumentCaptor<Parking> parkingCaptor;
	
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
		this.accountWithBalance.setUser(user);
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
		
		parkingConfigurationOne();
		
		when(systemClock.localTimeNow()).thenReturn(LocalTime.of(7, 0));
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.startParking(1L, plate.getPlate()));
		assertThat(e.getMessage()).isEqualTo("El horario de apertura del sistema de estacionamientos es a las 8:00 am");
		
	}
	
	@Test
	void testStartParking_parkingAlreadyStartedForUser() {
		
		parkingConfigurationOne();
		parkingConfigurationTwo();
		
		when(parkingRepository.existsByUserIdAndEndParkingIsNull(1L)).thenReturn(true);
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.startParking(1L, plate.getPlate()));
		assertThat(e.getMessage()).isEqualTo("Ya existe un estacionamiento iniciado por este usuario.");
		
	}
	
	@Test
	void testStartParking_parkingAlreadyStartedForPlate() {
		
		parkingConfigurationOne();
		parkingConfigurationTwo();
		parkingConfigurationThree();
		
		when(parkingRepository.existsByPlatePlateAndEndParkingIsNull(plate.getPlate())).thenReturn(true);
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.startParking(1L, plate.getPlate()));
		assertThat(e.getMessage()).isEqualTo("Ya existe un estacionamiento iniciado para esta patente.");
		
	}
	
	@Test
	void testStartParking_accountNotFound() {
		
		parkingConfigurationOne();
		parkingConfigurationTwo();
		parkingConfigurationThree();
		parkingConfigurationFour();
		
		when(checkingAccountService.findUserAccount(1L)).thenReturn(null);
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.startParking(1L, plate.getPlate()));
		assertThat(e.getMessage()).isEqualTo("Cuenta Corriente no encontrada.");
		
	}
	
	@Test
	void testStartParking_accountWithoutBalance() {
		
		parkingConfigurationOne();
		parkingConfigurationTwo();
		parkingConfigurationThree();
		parkingConfigurationFour();
		parkingConfigurationFive();
		parkingConfigurationFiveBis();
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.startParking(1L, plate.getPlate()));
		assertThat(e.getMessage()).isEqualTo("Saldo insuficiente para iniciar un estacionamiento.");
		
	}
	
	@Test
	void testStartParking_correct() {
		
		parkingConfigurationOne();
		parkingConfigurationTwo();
		parkingConfigurationThree();
		parkingConfigurationFour();
		parkingConfigurationFive();
		parkingConfigurationSix();
		
		when(plateRepository.findByPlate(plate.getPlate())).thenReturn(Optional.of(plate));
		
		ResponseEntity<String> response = parkingService.startParking(1L, plate.getPlate());
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("Estacionamiento iniciado!");
		
		verify(parkingRepository).save(parkingCaptor.capture());
		Parking startedParking = parkingCaptor.getValue();
		
		assertEquals(startedParking.getUser(), user);
		assertEquals(startedParking.getStart().toLocalDate(), LocalDateTime.now().toLocalDate());
		assertEquals(startedParking.getStart().toLocalTime().toSecondOfDay(), LocalDateTime.now().toLocalTime().toSecondOfDay());
		assertNull(startedParking.getEnd());
		
	}
	
	@Test
	void testFinishParking_parkingNotFound() {
		
		when(parkingRepository.findById(1L)).thenReturn(Optional.empty());
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.finishParking(1L));
		assertThat(e.getMessage()).isEqualTo("No se encontro el estacionamiento indicado.");
		
	}
	
	@Test
	void testFinishParking_parkingAlreadyFinished() {
		
		Parking newParking = new Parking();
		newParking.setUser(user);
		newParking.setPlate(plate);
		newParking.setEnd(LocalDateTime.now());
		when(parkingRepository.findById(1L)).thenReturn(Optional.of(newParking));
		
		Exception e = assertThrows(AppRequestException.class, () -> parkingService.finishParking(1L));
		assertThat(e.getMessage()).isEqualTo("El estacionamiento indicado ya finalizó.");
		
	}
	
	@Test
	void testFinishParking_correct() {
		
		LocalDateTime localTime = LocalDateTime.now();
		
		Parking newParking = new Parking();
		newParking.setUser(user);
		newParking.setPlate(plate);
		parkingConfigurationFive();
		when(globalData.getFractionationScheme()).thenReturn(15.0);
		
		when(systemClock.localDateTimeNow()).thenReturn(localTime);
		when(parkingRepository.findById(1L)).thenReturn(Optional.of(newParking));
		
		ResponseEntity<String> response = parkingService.finishParking(1L);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("Estacionamiento finalizado!");
		
		verify(parkingRepository).save(parkingCaptor.capture());
		Parking finishParking = parkingCaptor.getValue();
		
		assertEquals(finishParking.getUser(), user);
		assertEquals(finishParking.getStart().toLocalDate(), newParking.getStart().toLocalDate());
		assertEquals(finishParking.getStart().toLocalTime().toSecondOfDay(), newParking.getStart().toLocalTime().toSecondOfDay());
		assertNotNull(finishParking.getEnd());
		assertEquals(finishParking.getEnd().toLocalDate(), localTime.toLocalDate());
		assertEquals(finishParking.getEnd().toLocalTime().toSecondOfDay(), localTime.toLocalTime().toSecondOfDay());
		assertThat(finishParking.getAmount()).isEqualTo(0);
		
	}
	
	
	
	// a partir de este punto se encuentran los metodos que engloban el codigo repetido, dado por "etapas de ejecucion" por asi decirlo
	
	void parkingConfigurationOne() {
		when(globalData.getOpeningHour()).thenReturn(LocalTime.of(8, 0));
		when(systemClock.localDateNow()).thenReturn(LocalDate.of(2023, 8, 16));
		when(systemClock.localDateTimeNow()).thenReturn(LocalDateTime.of(2023, 8, 16, 7, 0));
	}
	
	void parkingConfigurationTwo() {
		when(systemClock.localTimeNow()).thenReturn(LocalTime.of(9, 0));
	}
	
	void parkingConfigurationThree() {
		when(parkingRepository.existsByUserIdAndEndParkingIsNull(1L)).thenReturn(false);
	}
	
	void parkingConfigurationFour() {
		when(parkingRepository.existsByPlatePlateAndEndParkingIsNull(plate.getPlate())).thenReturn(false);
	}
	
	void parkingConfigurationFive() {
		when(globalData.getFractionCost()).thenReturn(2.50);
	}
	
	void parkingConfigurationFiveBis() {
		when(checkingAccountService.findUserAccount(1L)).thenReturn(accountWithoutBalance);
	}
	
	void parkingConfigurationSix() {
		when(checkingAccountService.findUserAccount(1L)).thenReturn(accountWithBalance);
	}
	
}
