package com.cred.model;

public class CobradorModel {
	private int id;
	private String nombre;
	private String apellido;
	
	public CobradorModel() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int idCobrador) {
		this.id = idCobrador;
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
}
