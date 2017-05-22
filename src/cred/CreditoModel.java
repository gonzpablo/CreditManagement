package cred;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreditoModel {

	private final StringProperty cliente;
	private int cantDias;
	private float tasaInt;
	private float montoCredito;
	private float montoCuota;
	private float gciaXDia;
	private int cuotasPagas;
	private String cobrador;
	private float saldoCapital;
	
//	Hacer un constructor que pase solo Cliente, cantDias, Monto y tasa y que calcule el resto?	
	public CreditoModel(String cliente, int cantDias, float tasaInt,
						float montoCredito, float montoCuota,
						float gciaXDia, float saldoCapital, String cobrador) {

		this.cliente = new SimpleStringProperty(cliente);			
		
		this.cantDias = cantDias;
		this.tasaInt = tasaInt;
		this.montoCredito = montoCredito;
		this.montoCuota = montoCuota;
		this.gciaXDia = gciaXDia;
		this.cobrador = cobrador;
		this.saldoCapital = saldoCapital;
	}

	public int getCantDias() {
		return cantDias;
	}

	public void setCantDias(int cantDias) {
		this.cantDias = cantDias;
	}

	public float getTasaInt() {
		return tasaInt;
	}

	public void setTasaInt(float tasaInt) {
		this.tasaInt = tasaInt;
	}

	public float getMontoCredito() {
		return montoCredito;
	}

	public void setMontoCredito(float montoCredito) {
		this.montoCredito = montoCredito;
	}

	public float getMontoCuota() {
		return montoCuota;
	}

	public void setMontoCuota(float montoCuota) {
		this.montoCuota = montoCuota;
	}

	public float getGciaXDia() {
		return gciaXDia;
	}

	public void setGciaXDia(float gciaXDia) {
		this.gciaXDia = gciaXDia;
	}

	public String getCliente() {
		return cliente.get();
	}

	public void setCliente(String cliente) {
//		this.clienteColumn = cliente;
		this.cliente.set(cliente);
	}

	public int getCuotasPagas() {
		return cuotasPagas;
	}

	public void setCuotasPagas(int cuotasPagas) {
		this.cuotasPagas = cuotasPagas;
	}

	public String getCobrador() {
		return cobrador;
	}

	public void setCobrador(String cobrador) {
		this.cobrador = cobrador;
	}

	public float getSaldoCapital() {
		return saldoCapital;
	}

	public void setSaldoCapital(float saldoCapital) {
		this.saldoCapital = saldoCapital;
	}
	
	public StringProperty clienteProperty() {
		return cliente;
	}	
	
}
