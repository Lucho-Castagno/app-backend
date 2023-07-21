package com.entrenamiento.appbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.Plate;
import com.entrenamiento.appbackend.service.PlateService;

@RestController
@RequestMapping("/plates")
public class PlateController {
	
	private final PlateService plateService;
	
	public PlateController(PlateService plateService) {
		this.plateService = plateService;
	}
	
	@GetMapping("")
	public ResponseEntity<List<Plate>> obtainPlates() {
		return plateService.plates();
	}
	
	@PostMapping("/{userId}/create")
	public ResponseEntity<?> createPlate(@PathVariable("userId") Long id, @RequestParam("plate") String plate) {
		return this.plateService.create(id, plate);
	}
	
}
