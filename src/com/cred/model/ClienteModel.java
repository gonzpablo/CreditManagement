package com.cred.model;

import java.util.List;

public class ClienteModel {

	private int id;
	private String nombre;
	private String apellido;
	private String direccion;
	private String telefono;
	private String dni;
	private List<CreditoModel> creditos;

	public ClienteModel() {

	}
	
	public ClienteModel(String nombre, String apellido, String direccion, String telefono, String dni) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.direccion = direccion;
		this.telefono = telefono;
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public List<CreditoModel> getCreditos() {
		return creditos;
	}

	public void setCreditos(List<CreditoModel> creditos) {
		this.creditos = creditos;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;		
	}
}