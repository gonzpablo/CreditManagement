package com.cred.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PagoModel {

//	private FloatProperty montoPago;
	private int id;
	private int idCredito;
	private BigDecimal montoPago;
	private LocalDate fecha;
	private LocalTime hora;
	
	public PagoModel() {

	    LocalDateTime currentDateTime = LocalDateTime.now();	    
		LocalDate date1 = currentDateTime.toLocalDate();
		LocalTime time1 = currentDateTime.toLocalTime();
	    setFecha(date1);
	    setHora(time1);
	}

	public BigDecimal getMontoPago() {
		return montoPago.setScale(2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal getMontoPagoInterno() {
		return montoPago;
	}

	public void setMontoPago(String montoPago) {
//		this.montoPago.set(montoPago);
//		this.montoPago = new BigDecimal(montoPago);
		this.montoPago = NumeroUtil.crearBigDecimal(montoPago);
	}

	public void setMontoPagoNumeric(BigDecimal montoPago) {
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

	public int getIdCredito() {
		return idCredito;
	}

	public void setIdCredito(int idCredito) {
		this.idCredito = idCredito;
	}

	public void setMontoPago(BigDecimal montoPago) {
		this.montoPago = montoPago;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}