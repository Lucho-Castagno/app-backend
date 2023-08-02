package com.entrenamiento.appbackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.MessageSourceUtils;
import com.entrenamiento.appbackend.exception.AppRequestException;
import com.entrenamiento.appbackend.model.Plate;
import com.entrenamiento.appbackend.model.Usser;
import com.entrenamiento.appbackend.repository.PlateRepository;
import com.entrenamiento.appbackend.repository.UserRepository;

@Service
public class PlateService {
	
	private final PlateRepository plateRepository;
	private final UserRepository userRepository;
	
	public PlateService(PlateRepository plateRepository, UserRepository userRepository) {
		this.plateRepository = plateRepository;
		this.userRepository = userRepository;
	}

	public ResponseEntity<List<Plate>> plates() {
		return ResponseEntity.ok(this.plateRepository.findAll());
	}

	public ResponseEntity<?> create(Long id, String cadena) {
		
		if (!plateNumberValidation(cadena)) {
			throw new AppRequestException(MessageSourceUtils.getMessage("createPlate.error.plateFormat"));
		}
		
		Optional<Usser> usser = userRepository.findById(id);
		
		if (usser.isEmpty()) {
			throw new AppRequestException(MessageSourceUtils.getMessage("createPlate.error.userNotFound"));
		}
		
		String stringFormat = cadena.toUpperCase();
		
		// este for es para verificar si la patente ya esta asociada al usuario.
		for (Plate plate : usser.get().getPlates()) {
			if (plate.getPlate().equals(stringFormat)) {
				throw new AppRequestException(MessageSourceUtils.getMessage("createPlate.error.plateAlreadyAssigned"));
			}
		}
		
		// verificamos si la patente ya existe, si existe no la creamos de vuelta sino que asociamos la patente con
		// el usuario, en cambio, si no existe la creamos y asociamos con el usuario.
		
		Optional<Plate> plate = plateRepository.findByPlate(stringFormat);
		
		if (plate.isEmpty()) {
			Plate newPlate = new Plate(stringFormat);
			usser.get().addPlate(newPlate);
			
			this.userRepository.save(usser.get());
			
			return ResponseEntity.ok(newPlate);
		} else {
			usser.get().addPlate(plate.get());
			
			this.userRepository.save(usser.get());
			
			return ResponseEntity.ok(plate.get());
		}
		
		
	}
	
	private boolean plateNumberValidation(String plate) {
		String pattern = "^(?i)[A-Za-z]{3}\\d{3}$|^(?i)[A-Za-z]{2}\\d{3}[A-Za-z]{2}$";
		return plate.matches(pattern);
	}
	
}
