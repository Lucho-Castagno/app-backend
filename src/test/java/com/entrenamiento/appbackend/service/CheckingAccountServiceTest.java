package com.entrenamiento.appbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
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

import com.entrenamiento.appbackend.exception.AppRequestException;
import com.entrenamiento.appbackend.model.AccountMovements;
import com.entrenamiento.appbackend.model.CheckingAccount;
import com.entrenamiento.appbackend.model.Usser;
import com.entrenamiento.appbackend.repository.AccountMovementsRepository;
import com.entrenamiento.appbackend.repository.CheckingAccountRepository;

@ExtendWith(MockitoExtension.class)
public class CheckingAccountServiceTest {
	
	@Mock
	private CheckingAccountRepository checkingAccountRepository;
	
	@Mock
	private AccountMovementsRepository accountMovementsRepository;
	
	@InjectMocks
	private CheckingAccountService checkingAccountService;
	
	@Captor
	ArgumentCaptor<AccountMovements> movementCaptor; 
	
	@Captor
	ArgumentCaptor<CheckingAccount> accountCaptor;
	
	private CheckingAccount checkingAccount;
	private CheckingAccount checkingAccountWithMoves;
	private Usser user;
	
	@BeforeEach
	void setUp() {
		this.checkingAccount = new CheckingAccount();
		this.user = new Usser("1122916097", "password", "email@email.com");
		this.checkingAccount.setUser(user);
		
		this.checkingAccountWithMoves = new CheckingAccount();
		this.checkingAccountWithMoves.setUser(user);
		AccountMovements move = new AccountMovements(LocalDateTime.now(), 500.0, checkingAccountWithMoves, "CARGA");
		this.checkingAccountWithMoves.addMovements(move);
	}
	
	@Test
	void testLoadBalance_accountNotFound() {
		
		when(checkingAccountRepository.findById(1L)).thenReturn(Optional.empty());
		
		Exception e = assertThrows(AppRequestException.class, () -> checkingAccountService.loadBalance(1L, 100.0));
		assertThat(e.getMessage()).isEqualTo("No se encontro la cuenta.");
		
	}
	
	@Test
	void testLoadBalance_amountHasMinimun() {
		
		accountConfigurationOne();
		
		double minimunBalance = 99.99;
		
		Exception e = assertThrows(AppRequestException.class, () -> checkingAccountService.loadBalance(1L, minimunBalance));
		assertThat(e.getMessage()).isEqualTo("El monto minimo es de $100.0 pesos.");
		
	}
	
	@Test
	void testLoadBalance_correct() {
		
		accountConfigurationOne();
		
		double amountToCharge = 500.00;
		
		ResponseEntity<?> response = checkingAccountService.loadBalance(1L, amountToCharge);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(checkingAccount);
		
		verify(checkingAccountRepository).save(accountCaptor.capture());
		CheckingAccount accountCaptured = accountCaptor.getValue();
		
		verify(accountMovementsRepository).save(movementCaptor.capture());
		AccountMovements movementCaptured = movementCaptor.getValue();
		
		assertEquals(accountCaptured.getBalance(), 10000.00 + amountToCharge);
		assertThat(!accountCaptured.getAccountMovements().isEmpty());
		assertThat(accountCaptured.getAccountMovements().get(0)).isEqualTo(movementCaptured);
		
		assertEquals(movementCaptured.getType(), "CARGA");
		assertEquals(movementCaptured.getAccount(), accountCaptured);
		assertEquals(movementCaptured.getAmount(), amountToCharge);
		assertThat(movementCaptured.getDate()).isEqualToIgnoringHours(LocalDateTime.now());
		
	}
	
	@Test
	void testPerformTransaction() {
		
		accountConfigurationTwo();
		
		double amountToDoTransaction = 500.00;
		
		checkingAccountService.performTransaction(1L, amountToDoTransaction);
		
		verify(checkingAccountRepository).save(accountCaptor.capture());
		CheckingAccount accountCaptured = accountCaptor.getValue();
		
		verify(accountMovementsRepository).save(movementCaptor.capture());
		AccountMovements movementCaptured = movementCaptor.getValue();
		
		assertEquals(accountCaptured.getBalance(), 10000.00 - amountToDoTransaction);
		assertThat(!accountCaptured.getAccountMovements().isEmpty());
		assertThat(accountCaptured.getAccountMovements().get(0)).isEqualTo(movementCaptured);
		
		assertEquals(movementCaptured.getType(), "CONSUMO");
		assertEquals(movementCaptured.getAccount(), accountCaptured);
		assertEquals(movementCaptured.getAmount(), amountToDoTransaction);
		assertThat(movementCaptured.getDate()).isEqualToIgnoringHours(LocalDateTime.now());
		
	}
	
	@Test 
	void testFindUserAccount() {
		
		accountConfigurationTwo();
		
		CheckingAccount userAccount = checkingAccountService.findUserAccount(1L);
		
		assertNotNull(userAccount);
		assertEquals(userAccount, checkingAccount);
 		
	}
	
	@Test
	void testObtainMovements() {
		
		accountConfigurationOne();
		
		ResponseEntity<List<AccountMovements>> response = checkingAccountService.obtainMovements(1L);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().isEmpty());
		
		when(checkingAccountRepository.findById(1L)).thenReturn(Optional.of(checkingAccountWithMoves));
		
		response = checkingAccountService.obtainMovements(1L);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(!response.getBody().isEmpty());
		assertEquals(response.getBody().get(0), checkingAccountWithMoves.getAccountMovements().get(0));
		
	}
	
	
	// a partir de este punto se encuentran los metodos que engloban el codigo repetido, dado por "etapas de ejecucion" por asi decirlo
	
	void accountConfigurationOne() {
		when(checkingAccountRepository.findById(1L)).thenReturn(Optional.of(checkingAccount));
	}
	
	void accountConfigurationTwo() {
		when(checkingAccountService.findUserAccount(1L)).thenReturn(checkingAccount);
	}
	
}
