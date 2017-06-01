package cred;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditoModel {

	private String cliente;
	private int cantDias;
	private float tasaInt;
	private float montoCredito;
	private float montoCuota;
	private float gciaXDia;
	private int cuotasPagas;
	private String cobrador;
	private String ruta;
	private float saldoCapital;
	private List<PagoModel> listaPagos = new ArrayList<PagoModel>();
	
	
//	Hacer un constructor que pase solo Cliente, cantDias, Monto y tasa y que calcule el resto?	
	public CreditoModel(String cliente, int cantDias, float tasaInt,
						float montoCredito, float montoCuota, float saldoCapital, String cobrador, String ruta) {

//		this.cliente = new SimpleStringProperty(cliente);			
		this.cliente = cliente;
		this.cantDias = cantDias;
		this.tasaInt = tasaInt;
		this.montoCredito = montoCredito;
		this.montoCuota = montoCuota;
		this.cobrador = cobrador;
		this.saldoCapital = saldoCapital;
		this.ruta = ruta;
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
	
	public float getMontoCuota(LocalDate fechaFiltro) {
		
		float montoCuotaPura = 0;
		
		for ( PagoModel pago: listaPagos ) 

			if ( pago.getFecha().equals(fechaFiltro) )
				montoCuotaPura+= pago.getMontoPago();

		this.setMontoCuota(montoCuotaPura);
		
		return montoCuotaPura;		
	}

	public void setMontoCuota(float montoCuota) {
		this.montoCuota = montoCuota;
	}
	
	public float getGciaXDia() {
		return gciaXDia;		
	}
	
	public float getGciaXDia(LocalDate fechaFiltro) {
		
		float cuotaCapital = this.getMontoCredito() / this.getCantDias();		
//		montoCuota = cuotaCapital + ( cuotaCapital * ( Float.valueOf(fldTasaInt.getText()) / 100 )); //Float.valueOf("100") ));		
		float montoPagado = 0;		
		float gciaXDia = 0;
		
		
		for ( PagoModel pago: listaPagos ) 
			if ( pago.getFecha().equals(fechaFiltro) )
				montoPagado+= pago.getMontoPago();
	
		if (montoPagado > 0)
			gciaXDia = montoPagado - cuotaCapital;		
		
		this.setGciaXDia(gciaXDia);
		
		return gciaXDia;
	}

	public void setGciaXDia(float gciaXDia) {
		this.gciaXDia = gciaXDia;
	}

	public String getCliente() {
//		return cliente.get();
		return cliente;
	}

	public void setCliente(String cliente) {
//		this.clienteColumn = cliente;
//		this.cliente.set(cliente);
		this.cliente = cliente;
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

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public List<PagoModel> getListaPagos() {
		return listaPagos;
	}	
	
	public void agregarPago(PagoModel pago) {
		listaPagos.add(pago);
	}
}