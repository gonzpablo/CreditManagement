package cred;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//http://www.opentaps.org/docs/index.php/How_to_Use_Java_BigDecimal:_A_Tutorial

public class CreditoModel {

	private String cliente;
	private int cantDias;
	private BigDecimal tasaInt;
	
	private BigDecimal montoCredito;
	private BigDecimal valorCuota; // Monto credito * interes / cant. dias
	private BigDecimal montoCuota = new BigDecimal("0"); // Monto pagado un día determinado
	private BigDecimal montoCuotaAcumulado;
	
	private BigDecimal gciaXDia = new BigDecimal("0");
	private int cuotasPagas;
	private String cobrador;
	private String ruta;
	private BigDecimal saldoCapital;
	private List<PagoModel> listaPagos = new ArrayList<PagoModel>();
	
	public CreditoModel(String cliente, int cantDias, String tasaInt, String montoCredito, String cobrador, String ruta) {
		
		this.cliente = cliente;
		this.cantDias = cantDias;
		this.tasaInt = NumeroUtil.crearBigDecimal(tasaInt);
		this.montoCredito = NumeroUtil.crearBigDecimal(montoCredito);		
		this.cobrador = cobrador;
		this.ruta = ruta;
		this.valorCuota = calcularValorCuota();
		calcularMontoAcumulado();
		calcularCuotasPagas();
		calcularSaldoCapital();
	}

	public static BigDecimal obtenerCuotaCapital(String montoCred, String cantCuotas2) {
		
		BigDecimal montoCredito = NumeroUtil.crearBigDecimal(montoCred);
		int cantCuotas = Integer.valueOf(cantCuotas2);
		
		return montoCredito.divide(BigDecimal.valueOf(cantCuotas), 2, RoundingMode.HALF_UP);
	}

	public static BigDecimal obtenerMontoTotalCredito(String montoCred, String tasaInt) {
		
		BigDecimal montoCredito = NumeroUtil.crearBigDecimal(montoCred);		
		BigDecimal tasaInteres = NumeroUtil.crearBigDecimal(tasaInt);
		
		return montoCredito.add(montoCredito.multiply(tasaInteres).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));		
	}
	
	public static BigDecimal obtenerMontoCuota(BigDecimal montoTotalCredito, String cantCuotas) {
	
		return montoTotalCredito.divide(BigDecimal.valueOf(Integer.valueOf(cantCuotas)), 2, RoundingMode.HALF_UP);
	}
	
	private BigDecimal calcularValorCuota() {
		return getCostoTotalCredito().divide(BigDecimal.valueOf(cantDias), NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP);
	}

	public void calcularSaldoCapital() {
		
		BigDecimal saldo;
				
		saldo = montoCredito.subtract(montoCuotaAcumulado).setScale(NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP);

		if (saldo.compareTo(new BigDecimal("0")) == -1)
			this.saldoCapital = NumeroUtil.crearBigDecimal("0");
		else
			this.saldoCapital = NumeroUtil.crearBigDecimal(saldo.toString());
	}

	public int getCantDias() {
		return cantDias;
	}

	public void setCantDias(int cantDias) {
		this.cantDias = cantDias;
	}

	public BigDecimal getTasaInt() {
		return tasaInt;
	}

	public void setTasaInt(String tasaInt) {
		this.tasaInt = NumeroUtil.crearBigDecimal(tasaInt);
	}

	public BigDecimal getMontoCredito() {
		return montoCredito.setScale(2, RoundingMode.HALF_UP);
	}

	public void setMontoCredito(String montoCredito) {
		this.montoCredito = NumeroUtil.crearBigDecimal(montoCredito);
	}

	public BigDecimal getMontoCuota() {
		return montoCuota.setScale(2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal getMontoCuota(LocalDate fechaFiltro) {

		BigDecimal montoCuotaPura = NumeroUtil.crearBigDecimal("0");
		
		for ( PagoModel pago: listaPagos )
			if ( pago.getFecha().equals(fechaFiltro) )
				montoCuotaPura = montoCuotaPura.add(pago.getMontoPagoInterno());				
			
		this.setMontoCuota(montoCuotaPura);
		
		return montoCuotaPura;		
	}

	public void setMontoCuota(BigDecimal montoCuota) {
		this.montoCuota = montoCuota;
	}
	
	public BigDecimal getGciaXDia() {
		return gciaXDia.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getGciaXDia(LocalDate fechaFiltro) {
		int cant = 0;
		BigDecimal cuotaCapital;	
		BigDecimal montoPagado = NumeroUtil.crearBigDecimal("0");
		BigDecimal gciaXDia = NumeroUtil.crearBigDecimal("0");

		
		cuotaCapital = this.getMontoCredito().divide(BigDecimal.valueOf(this.getCantDias()), NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP);		
		
		for ( PagoModel pago: listaPagos )
			if ( pago.getFecha().equals(fechaFiltro) ) {
				montoPagado = montoPagado.add(pago.getMontoPagoInterno());
				cant+=1;
			}

		if (montoPagado.compareTo(BigDecimal.valueOf(0)) == 1)		
			gciaXDia = montoPagado.subtract(cuotaCapital.multiply(BigDecimal.valueOf(cant).setScale(NumeroUtil.EXCEL_MAX_DIGITS)));

		this.setGciaXDia(gciaXDia);

		return gciaXDia;
	}

	public void setGciaXDia(BigDecimal gciaXDia) {
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

	public BigDecimal getSaldoCapital() {
		return saldoCapital.setScale(2, RoundingMode.HALF_UP);
	}

	public void setSaldoCapital(BigDecimal saldoCapital) {
		this.saldoCapital = saldoCapital;
	}

	public String getRuta() {
		return ruta;
	}
	
	public BigDecimal getValorCuota() {
		return valorCuota.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getValorCuotaInterno() {
		return valorCuota;
	}
	
	public void setValorCuota(BigDecimal valorCuota) {
		this.valorCuota = valorCuota;
	}

	public BigDecimal getMontoCuotaAcumulado() {
		return montoCuotaAcumulado.setScale(2, RoundingMode.HALF_UP);
	}

	public void setMontoCuotaAcumulado(BigDecimal montoCuotaAcumulado) {
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

		BigDecimal montoAcumulado = NumeroUtil.crearBigDecimal("0");
		
		for ( PagoModel pago: listaPagos ) 
			montoAcumulado = montoAcumulado.add(pago.getMontoPagoInterno());

		this.setMontoCuotaAcumulado(montoAcumulado);
	}

	public void calcularCuotasPagas() {
		this.cuotasPagas = this.montoCuotaAcumulado.divide(this.valorCuota, NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP).intValue();
	}

	public int calcularCuotasAPagarSegunMonto() {
//		Segun el monto a pagar calcula la cantidad de cuotas que representa.
//		Si el monto de cuota a pagar es cero, devuelve 0		
		
		if ( montoCuota.compareTo(BigDecimal.valueOf(0)) == 0)		
			return 0;
		else
			return this.montoCuota.divide(this.valorCuota, NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP).intValue();
	}
	
	public BigDecimal calcularMontoSegunCuota(int cantCuotas) {
		return valorCuota.multiply(BigDecimal.valueOf(cantCuotas)).setScale(NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP);
	}
	
	private BigDecimal getCostoTotalCredito() {
		
		BigDecimal interes;
		
		interes = (tasaInt.divide(BigDecimal.valueOf(100), NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP)).add(BigDecimal.valueOf(1));				
		
		return montoCredito.multiply(interes).setScale(NumeroUtil.EXCEL_MAX_DIGITS);
	}
	
	public boolean validarMontoAPagar(BigDecimal monto) {
		boolean result = true;
		BigDecimal montoTotal;
		
		montoTotal = montoCuotaAcumulado.add(monto);

		if ( montoTotal.compareTo(this.getCostoTotalCredito()) == 1 )			
			result = false;

		return result;
	}
	
	public void borrarPago(PagoModel pago) {
		listaPagos.remove(pago);
	}
	
	public void calcular() {
		calcularMontoAcumulado();		
		calcularCuotasAPagarSegunMonto();
		calcularSaldoCapital();
		calcularCuotasPagas();		
	}	
}