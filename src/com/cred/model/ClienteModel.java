package com.cred.model;

import java.sql.SQLException;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClienteModel {

	private final IntegerProperty id;	
	private final StringProperty nombre;
	private final StringProperty apellido;
	private final StringProperty direccion;
	private final StringProperty telefono;
	private final StringProperty dni; 
	
	private List<CreditoModel> creditos;
	
	
	public ClienteModel() {
		id = new SimpleIntegerProperty();
		nombre = new SimpleStringProperty();
		apellido = new SimpleStringProperty();
		direccion = new SimpleStringProperty();
		telefono = new SimpleStringProperty();
		dni = new SimpleStringProperty();		
	}
	
	public ClienteModel(String nombre, String apellido, String direccion, String telefono, String dni) {
		super();
		this.id = new SimpleIntegerProperty();
		this.nombre = new SimpleStringProperty(nombre);
		this.apellido = new SimpleStringProperty(apellido);
		this.direccion = new SimpleStringProperty(direccion);
		this.telefono = new SimpleStringProperty(telefono);
		this.dni = new SimpleStringProperty(dni);
	}

//	public String getNombre() {
//		return nombre;
//	}
//
//	public void setNombre(String nombre) {
//		this.nombre = nombre;
//	}
//
//	public String getApellido() {
//		return apellido;
//	}
//
//	public void setApellido(String apellido) {
//		this.apellido = apellido;
//	}
//
//	public String getDireccion() {
//		return direccion;
//	}
//
//	public void setDireccion(String direccion) {
//		this.direccion = direccion;
//	}
//
//	public String getTelefono() {
//		return telefono;
//	}
//
//	public void setTelefono(String telefono) {
//		this.telefono = telefono;
//	}
//
//	public String getDni() {
//		return dni;
//	}
//
//	public void setDni(String dni) {
//		this.dni = dni;
//	}

	public List<CreditoModel> getCreditos() {
		return creditos;
	}

	public void setId(int id) {
		this.id.set(id);	
	}
	
	public int getId() {
		return id.get();
	}
	
	public void setCreditos(List<CreditoModel> creditos) {
		this.creditos = creditos;
	}
	
//	public int getId() {
//		return id;
//	}

//	public void setId(int id) {
//		this.id = id;		
//	}

	public String getNombre() {
		return nombre.get();
	}

	public String getApellido() {
		return apellido.get();
	}

	public String getDireccion() {
		return direccion.get();
	}

	public String getTelefono() {
		return telefono.get();
	}

	public String getDni() {
		return dni.get();
	}
	
	public void setNombre(String nombre) {
	    this.nombre.set(nombre);
	}

	public void setApellido(String apellido) {
	    this.apellido.set(apellido);
	}
	
	public void setDireccion(String direccion) {
	    this.direccion.set(direccion);
	}
	
	public void setTelefono(String telefono) {
	    this.telefono.set(telefono);
	}
	
	public void setDni(String dni) {
	    this.dni.set(dni);
	}
	
	public StringProperty nombreProperty() {
		return nombre;
	}

	public StringProperty apellidoProperty() {
		return apellido;
	}
	
	public StringProperty direccionProperty() {
		return direccion;
	}

	public StringProperty telefonoProperty() {
		return telefono;
	}
	
	public StringProperty dniProperty() {
		return dni;
	}

	public void borrar() {
		try {
			ClienteDAO.borrarCliente(this);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}		
	}

	public boolean tieneCreditos() {
		boolean tieneCreditos = false;
		try {
			tieneCreditos = CreditoDAO.buscarCreditoCliente(this);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return tieneCreditos;
	}	
}