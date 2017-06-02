package cred;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PagoModel {

//	private float montoPago;
	private FloatProperty montoPago;
	private IntegerProperty cuotasPagas;
	private LocalDate fecha;
	private LocalTime hora;
	
	public PagoModel() {
		
	    LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDate date1 = currentDateTime.toLocalDate();
		LocalTime time1 = currentDateTime.toLocalTime();
	    this.setFecha(date1);
	    this.setHora(time1);
	    this.montoPago = new SimpleFloatProperty();
	    this.cuotasPagas = new SimpleIntegerProperty();
//		this.fechaPagoField.setValue(date1);
	    
//	    montoPago.bind(cuotasPagas);
	}

	public float getMontoPago() {
		return montoPago.get();
	}

	public void setMontoPago(float montoPago) {
		this.montoPago.set(montoPago);
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
