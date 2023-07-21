package com.entrenamiento.appbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entrenamiento.appbackend.model.Parking;

public interface ParkingRepository extends JpaRepository<Parking, Long>{

	boolean existsByUserIdAndEndParkingIsNull(Long id);
	
	Optional<Parking> findByUserIdAndEndParkingIsNull(Long id);
	
	List<Parking> findAllByUserId(Long id);

	boolean existsByPlatePlateAndEndParkingIsNull(String plate);

}
