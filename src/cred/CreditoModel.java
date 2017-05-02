package cred;

public class CreditoModel {

	private String cliente;
	private int cantDias;
	private float tasaInt;
	private float montoCredito;
	private float montoCuota;
	private float gciaXDia;
	
//	Hacer un constructor que pase solo Cliente, cantDias, Monto y tasa y que calcule el resto?	
	public CreditoModel(String cliente, int cantDias, float tasaInt, float montoCredito, float montoCuota, float gciaXDia) {

		this.cliente = cliente;
		this.cantDias = cantDias;
		this.tasaInt = tasaInt;
		this.montoCredito = montoCredito;
		this.montoCuota = montoCuota;
		this.gciaXDia = gciaXDia;
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
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	
	
}
