package cred;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//http://www.opentaps.org/docs/index.php/How_to_Use_Java_BigDecimal:_A_Tutorial

public class CreditoModel {

	private String cliente;
	private int cantDias;
	private float tasaInt;
	
	private float montoCredito;
	private float valorCuota;  // Monto credito * interes / cant. dias
	private float montoCuota;  // Monto pagado un día determinado
	private float montoCuotaAcumulado;
	
	private float gciaXDia;
	private int cuotasPagas;
	private String cobrador;
	private String ruta;
	private float saldoCapital;
	private List<PagoModel> listaPagos = new ArrayList<PagoModel>();
	
	public CreditoModel(String cliente, int cantDias, float tasaInt, float montoCredito, String cobrador, String ruta) {
		
		this.cliente = cliente;
		this.cantDias = cantDias;
		this.tasaInt = tasaInt;
		this.montoCredito = montoCredito;
		this.cobrador = cobrador;
		this.ruta = ruta;
//		this.valorCuota = this.montoCredito * (1 + ( this.tasaInt / 100 )) / this.cantDias;
//		this.valorCuota = this.getCostoTotalCredito() / this.cantDias;
		this.valorCuota = calcularValorCuota();
		calcularMontoAcumulado();
		calcularCuotasPagas();
		calcularSaldoCapital();
	}

	private float calcularValorCuota() {
		return getCostoTotalCredito() / cantDias;
	}

	public void calcularSaldoCapital() {
		
		float saldo; 
				
		saldo = this.montoCredito - this.montoCuotaAcumulado;
		
		if (saldo < 0)
			this.saldoCapital = 0;
		else
			this.saldoCapital = saldo;
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
		return cliente;
	}

	public void setCliente(String cliente) {
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
	
	public float getValorCuota() {
		return valorCuota;
	}

	public void setValorCuota(float valorCuota) {
		this.valorCuota = valorCuota;
	}

	public float getMontoCuotaAcumulado() {
		return montoCuotaAcumulado;
	}

	public void setMontoCuotaAcumulado(float montoCuotaAcumulado) {
		this.montoCuotaAcumulado = montoCuotaAcumulado;
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
	
	public void calcularMontoAcumulado() {

		float montoAcumulado = 0;
		
		for ( PagoModel pago: listaPagos ) 
			montoAcumulado+= pago.getMontoPago();

		this.setMontoCuotaAcumulado(montoAcumulado);
	}

	public void calcularCuotasPagas() {
		
		//this.cuotasPagas = (int) Math.floor((double)this.montoCuotaAcumulado / this.valorCuota);
		//System.out.println((double)this.montoCuotaAcumulado / this.valorCuota);
		this.cuotasPagas = (int) Math.round((double)this.montoCuotaAcumulado / this.valorCuota);
	}

	public int calcularCuotasAPagarSegunMonto() {
//		Segun el monto a pagar calcula la cantidad de cuotas que representa.
//		Si el monto de cuota a pagar es cero, devuelve 0		
		
		if ( montoCuota == 0)
			return 0;
		else
			return (int) Math.floor((double)this.montoCuota / this.valorCuota);
	}
	
	public float calcularMontoSegunCuota(int cantCuotas) {
		return (float) cantCuotas * valorCuota;
	}
	
	private float getCostoTotalCredito() {
		return montoCredito * (1 + ( tasaInt / 100 ));
	}
	
	public boolean validarMontoAPagar(float monto) {
		boolean result = true;
		
		if ( montoCuotaAcumulado + monto > this.getCostoTotalCredito() )
			result = false;

		return result;
	}
}