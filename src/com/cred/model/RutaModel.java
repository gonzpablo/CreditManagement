package com.cred.model;

import java.sql.SQLException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RutaModel {

	private final IntegerProperty id;
	private final StringProperty nombre;
	private final StringProperty descripcion;
	
	public RutaModel() {
		id = new SimpleIntegerProperty();
		nombre = new SimpleStringProperty();
		descripcion = new SimpleStringProperty();			
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

	public String getDescripcion() {
		return descripcion.get();
	}

	public void setNombre(String nombre) {
	    this.nombre.set(nombre);
	}

	public void setDescripcion(String descripcion) {
	    this.descripcion.set(descripcion);
	}
	
	public StringProperty nombreProperty() {
		return nombre;
	}

	public StringProperty descripcionProperty() {
		return descripcion;
	}

	public void borrar() {
		try {
			RutaDAO.borrarRuta(this);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}		
	}
}
