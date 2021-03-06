package com.cred.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.*;

public class PagoModel {

	private int id;
	private int idCredito;
	private BigDecimal montoPago;
	private SimpleObjectProperty<LocalDate> fecha;
	
	public PagoModel() {

	    LocalDateTime fechaHoraActual = LocalDateTime.now();	    
	    SimpleObjectProperty<LocalDate> fecha = new SimpleObjectProperty<LocalDate>( fechaHoraActual.toLocalDate() );
	    setFecha(fecha);
	}

	public BigDecimal getMontoPago() {
		return montoPago.setScale(2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal getMontoPagoInterno() {
		return montoPago;
	}

	public void setMontoPago(String montoPago) {
		this.montoPago = NumeroUtil.crearBigDecimal(montoPago);
	}

	public void setMontoPagoNumeric(BigDecimal montoPago) {
		this.montoPago = montoPago;
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

	public SimpleObjectProperty<LocalDate> getFecha() {
		return fecha;
	}

	public void setFecha(SimpleObjectProperty<LocalDate> fecha) {
		this.fecha = fecha;
	}

	public LocalDate getFechaValue() {
		return this.fecha.getValue();
	}
	
	public void borrarPago() {
		try {
			PagoDAO.borrarPago(this);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}