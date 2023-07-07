package com.entrenamiento.appbackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.TipoMovimiento;
import com.entrenamiento.appbackend.model.CtaCorriente;
import com.entrenamiento.appbackend.model.MovimientoCta;
import com.entrenamiento.appbackend.repository.CtaCorrienteRepository;
import com.entrenamiento.appbackend.repository.MovimientoCtaRepository;

@Service
public class CtaCorrienteService {
	
	private final CtaCorrienteRepository ctaCorrienteRepository;
	private final MovimientoCtaRepository movimientoCtaRepository;
	
	public CtaCorrienteService(CtaCorrienteRepository ctaCorrienteRepository, MovimientoCtaRepository movimientoCtaRepository) {
		this.ctaCorrienteRepository = ctaCorrienteRepository;
		this.movimientoCtaRepository = movimientoCtaRepository;
	}
	
	public ResponseEntity<?> cargarSaldo(Long id, double monto) {
		
		Optional<CtaCorriente> cuenta = this.ctaCorrienteRepository.findById(id);
		if(cuenta.isEmpty()) {
			return ResponseEntity.badRequest().body("No se encontro la cuenta.");
		}
		
		if (monto < 100.0) {
			return ResponseEntity.badRequest().body("El monto minimo es de $100.0 pesos.");
		}
		
		MovimientoCta movimiento = new MovimientoCta();
		movimiento.setFecha(LocalDateTime.now());
		movimiento.setTipo(TipoMovimiento.CARGA);
		movimiento.setMonto(monto);
		
		CtaCorriente ctaCorriente = cuenta.get();
		ctaCorriente.cargarCredito(monto);
		
		ctaCorriente.addMovimiento(movimiento);
		this.ctaCorrienteRepository.save(ctaCorriente);
		
		movimiento.setCuentaCorriente(ctaCorriente);
		this.movimientoCtaRepository.save(movimiento);
		
		return ResponseEntity.ok(ctaCorriente);
		
	}
	
	public void realizarConsumo(String celular, double importe) {
		CtaCorriente ctaCorriente = this.buscarCuentaUsuario(celular);
		ctaCorriente.consumo(importe);
		
		MovimientoCta movimiento = new MovimientoCta();
		movimiento.setFecha(LocalDateTime.now());
		movimiento.setTipo(TipoMovimiento.CONSUMO);
		movimiento.setMonto(importe);
		
		ctaCorriente.addMovimiento(movimiento);
		this.movimientoCtaRepository.save(movimiento);
		
		ctaCorrienteRepository.save(ctaCorriente);
	}
	
	public CtaCorriente buscarCuentaUsuario(String celular) {
		return this.ctaCorrienteRepository.findByUsuarioCelular(celular);
	}

	public ResponseEntity<List<MovimientoCta>> obtenerMovimientosCuenta(Long id) {
		Optional<CtaCorriente> cuenta = this.ctaCorrienteRepository.findById(id);
		if(cuenta.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		
		return ResponseEntity.ok(cuenta.get().getMovimientosCta());
		
	}
	
}
