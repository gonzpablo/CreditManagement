package com.cred.model;

import java.sql.SQLException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CobradorModel {

	private final IntegerProperty id;	
	private final StringProperty nombre;
	private final StringProperty apellido;	
	
	public CobradorModel() {
		id = new SimpleIntegerProperty();
		nombre = new SimpleStringProperty();
		apellido = new SimpleStringProperty();		
	}

	public void setId(int id) {
		this.id.set(id);	
	}
	
	public int getId() {
		return id.get();
	}
	
	public String getNombre() {
		return nombre.get();
	}

	public String getApellido() {
		return apellido.get();
	}

	public void setNombre(String nombre) {
	    this.nombre.set(nombre);
	}

	public void setApellido(String apellido) {
	    this.apellido.set(apellido);
	}
	
	public StringProperty nombreProperty() {
		return nombre;
	}

	public StringProperty apellidoProperty() {
		return apellido;
	}

	public void borrar() {
		try {
			CobradorDAO.borrarCobrador(this);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}		
	}	
}