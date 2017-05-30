package cred;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PagoModel {

	private float montoPago;
	private LocalDate fecha;
	private LocalTime hora;
	
	public PagoModel() {
		
	    LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDate date1 = currentDateTime.toLocalDate();
		LocalTime time1 = currentDateTime.toLocalTime();
	    this.setFecha(date1);
	    this.setHora(time1);
//		this.fechaPagoField.setValue(date1);
	}

	public float getMontoPago() {
		return montoPago;
	}

	public void setMontoPago(float montoPago) {
		this.montoPago = montoPago;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}
}
