package com.entrenamiento.appbackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.Parking;
import com.entrenamiento.appbackend.service.ParkingService;

@RestController
@RequestMapping("/parkings")
public class ParkingController {
	
	private final ParkingService parkingService;
	
	public ParkingController(ParkingService parkingService) {
		this.parkingService = parkingService;
	}
	
	@GetMapping("")
	public ResponseEntity<List<Parking>> obtainParkings() {
		return this.parkingService.parkings();
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<List<Parking>> userParkings(@PathVariable("userId") Long id) {
		return this.parkingService.userParkings(id);
	}
	
	@GetMapping("/{userId}/pending")
	public ResponseEntity<Optional<Parking>> pendingParking(@PathVariable("userId") Long id) {
		return this.parkingService.pendingParking(id);
	}
	
	@PostMapping("/start/{userId}")
	public ResponseEntity<String> startParking(@PathVariable("userId") Long id, @RequestParam("plate") String plate) {
		return this.parkingService.startParking(id, plate);
	}
	
	@PostMapping("/{id}/finish")
	public ResponseEntity<String> finishParking(@PathVariable Long id) {
		return this.parkingService.finishParking(id);
	}
	
}
