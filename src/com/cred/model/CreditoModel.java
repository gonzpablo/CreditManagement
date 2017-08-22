package com.cred.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;

public class CreditoModel {

	private int id;		// ID del crédito
	private SimpleObjectProperty<LocalDate> fechaCreacion;
	private int cantCuotas;
	private BigDecimal tasaInt;
	private BigDecimal montoCredito;
	private BigDecimal valorCuota; // Monto credito * interes / cant. dias
	private BigDecimal montoCuota = new BigDecimal("0"); // Monto pagado un día determinado
	private BigDecimal montoCuotaAcumulado = new BigDecimal("0");	
	private BigDecimal gciaXDia = new BigDecimal("0");
	private BigDecimal cuotaCapital;
	private BigDecimal indiceCapital;
	private int cuotasPagas;
	private int cerrado;

	private String unidad;
	private BigDecimal saldoCapital = new BigDecimal("0");;
	
//	Id's Referencias	
	private int idCliente;
	private int idCobrador;
	private int idRuta;

//  Referencias objetos	
	private SimpleObjectProperty<ClienteModel> cliente;	
	private SimpleObjectProperty<CobradorModel> cobrador;
	private SimpleObjectProperty<RutaModel> ruta;
		
	private List<PagoModel> listaPagos = new ArrayList<PagoModel>();
	
	public CreditoModel(ClienteModel cliente, int cantCuotas, String unidad, String montoCuota,
						String montoCredito, CobradorModel cobrador, RutaModel ruta) {

		this.cliente = new SimpleObjectProperty<ClienteModel>();		
		this.cobrador = new SimpleObjectProperty<CobradorModel>();
		this.ruta = new SimpleObjectProperty<RutaModel>();
		
		this.cantCuotas = cantCuotas;
		this.montoCredito = NumeroUtil.crearBigDecimal(montoCredito);		
		this.unidad = unidad;
		this.valorCuota = obtenerMontoCuota(
							obtenerMontoTotalCredito(montoCuota, String.valueOf(cantCuotas)), cantCuotas);
		
		this.cliente.set(cliente);
		this.cobrador.set(cobrador);		
		this.ruta.set(ruta);
		
		this.cuotaCapital =	CreditoModel.obtenerCuotaCapital(
				this.getMontoCredito().toString(), String.valueOf(this.getCantCuotas()));

//		Se calcula qué porcentaje de la cuota corresponde a capital
		
//		indice = 1 - ( ( ValorCuota - CuotaCapital ) / ValorCuota )
		
//		donde [ ValorCuota - CuotaCapital = GciaXDia ]		

		this.indiceCapital = BigDecimal.valueOf(1).subtract(
				this.getValorCuota().subtract(
					this.getCuotaCapital()).divide(
							this.getValorCuota(), NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP));
		
	    LocalDateTime fechaHoraActual = LocalDateTime.now();	    
	    SimpleObjectProperty<LocalDate> fecha = new SimpleObjectProperty<LocalDate>( fechaHoraActual.toLocalDate() );
	    this.fechaCreacion = fecha;
	}	
	
//	CreditoModel (De BD):	
	public CreditoModel(int id, int idCliente, int cantCuotas, int idUnidad, int montoCuota, 
						int montoCredito, int idCobrador, int idRuta, int cerrado) {
		
		this.cliente = new SimpleObjectProperty<ClienteModel>();		
		this.cobrador = new SimpleObjectProperty<CobradorModel>();		
		this.ruta = new SimpleObjectProperty<RutaModel>();
		
		this.id = id;
		this.idCliente = idCliente;
		this.cantCuotas = cantCuotas;
		this.montoCredito = BigDecimal.valueOf(montoCredito).divide(BigDecimal.valueOf(100));
		this.idCliente = idCliente;
		this.idCobrador = idCobrador;
		this.idRuta = idRuta;
		this.unidad = obtenerUnidad(idUnidad);
		this.valorCuota = BigDecimal.valueOf(montoCuota).divide(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP));
		
		this.cuotaCapital =	CreditoModel.obtenerCuotaCapital(
				this.getMontoCredito().toString(), String.valueOf(this.getCantCuotas()));

		this.cerrado = cerrado;  
		
//		Se calcula qué porcentaje de la cuota corresponde a capital
		
//		indice = 1 - ( ( ValorCuota - CuotaCapital ) / ValorCuota )
		
//		donde [ ValorCuota - CuotaCapital = GciaXDia ]		
		
		this.indiceCapital = BigDecimal.valueOf(1).subtract(
				this.getValorCuota().subtract(
					this.getCuotaCapital()).divide(
							this.getValorCuota(), NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP));		
	}

	public static String obtenerUnidad(int idUnidad) {
		
		switch (idUnidad) {
			case 1: return "Días";
			case 2: return "Semanas";		
		}
		
		return null;
	}

	public static int obtenerIdUnidad(String unidad) {
		switch (unidad) {
			case "Días": return 1;
			case "Semanas": return 2;
		}
		return 0;
	}	
	
	public static BigDecimal obtenerCuotaCapital(String montoCred, String cantCuotas2) {
//		montoCredito: es el monto prestado (sin intereses)
		BigDecimal montoCredito = NumeroUtil.crearBigDecimal(montoCred);
		int cantCuotas = Integer.valueOf(cantCuotas2);
		
		return montoCredito.divide(BigDecimal.valueOf(cantCuotas), NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP);
	}

	public static BigDecimal obtenerMontoTotalCredito(String montoCuotaIn, String cantCuotasIn) {
//		montoCredito: es el monto prestado (sin intereses)
		BigDecimal montoCuota = NumeroUtil.crearBigDecimal(montoCuotaIn);
		int cantCuotas = Integer.valueOf(cantCuotasIn);
		
//		Fórmula: montoCredito + (montoCredito * (tasaInteres/100/30) * cantCuotas)
//		Como es tasa de interés mensual, se divide la tasa en 30 días y se aplica a la cantidad de cuotas
		return montoCuota.multiply(BigDecimal.valueOf(cantCuotas)).setScale(2, RoundingMode.HALF_UP);
	}

	public static BigDecimal obtenerMontoCuota(BigDecimal montoTotalCredito, int cantCuotas) {
//		montoTotalCredito: Es el monto total a pagar, incluyendo los intereses
		return montoTotalCredito.divide(BigDecimal.valueOf(Integer.valueOf(cantCuotas)), 2, RoundingMode.HALF_UP);
	}

	public void calcularSaldoCapital() {

		BigDecimal capitalAcumulado = montoCuotaAcumulado.multiply(this.getIndiceCapital());
		
		this.saldoCapital = this.getMontoCredito().subtract(capitalAcumulado);
	}

	public int getCantCuotas() {
		return cantCuotas;
	}

	public void setCantCuotas(int cantCuotas) {
		this.cantCuotas = cantCuotas;
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
			if ( pago.getFecha().getValue().equals(fechaFiltro) )
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
	
		BigDecimal montoPagado = NumeroUtil.crearBigDecimal("0");
		BigDecimal gciaXDia = NumeroUtil.crearBigDecimal("0");
		
		for ( PagoModel pago: listaPagos )
			if ( pago.getFecha().getValue().equals(fechaFiltro) )
				montoPagado = montoPagado.add(pago.getMontoPagoInterno());

		if (montoPagado.compareTo(BigDecimal.valueOf(0)) == 1)	// mayor que 0			
			gciaXDia = montoPagado.multiply((BigDecimal.valueOf(1).subtract(this.getIndiceCapital())));

		this.setGciaXDia(gciaXDia);

		return gciaXDia;
	}

	public void setGciaXDia(BigDecimal gciaXDia) {
		this.gciaXDia = gciaXDia;
	}

	public String getCliente() {
		return ( this.cliente == null ) ? null : 
			this.cliente.get().getNombre() + " " + this.cliente.get().getApellido();		
	}

	public int getCuotasPagas() {
		return cuotasPagas;
	}

	public void setCuotasPagas(int cuotasPagas) {
		this.cuotasPagas = cuotasPagas;
	}

	public int getIdCobrador() {
		return idCobrador;
	}

	public void setCobrador(int idCobrador) {
		this.idCobrador = idCobrador;
	}

	public BigDecimal getSaldoCapital() {
		return saldoCapital.setScale(2, RoundingMode.HALF_UP);
	}

	public void setSaldoCapital(BigDecimal saldoCapital) {
		this.saldoCapital = saldoCapital;
	}

	public int getIdRuta() {
		return idRuta;
	}

	public BigDecimal getValorCuota() {
		return valorCuota.setScale(2, RoundingMode.HALF_UP);
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

	public void setRuta(int idRuta) {
		this.idRuta = idRuta;
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
		this.cuotasPagas = this.montoCuotaAcumulado.divide(
				this.valorCuota, NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP).intValue();
	}

	public int calcularCuotasAPagarSegunMonto() {
//		Segun el monto a pagar calcula la cantidad de cuotas que representa.
//		Si el monto de cuota a pagar es cero, devuelve 0		
		
		if ( montoCuota.compareTo(BigDecimal.valueOf(0)) == 0)		
			return 0;
		else
			return this.montoCuota.divide(
					this.valorCuota, NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP).intValue();
	}
	
	public BigDecimal calcularMontoSegunCuota(int cantCuotas) {
		return valorCuota.multiply(
				BigDecimal.valueOf(cantCuotas)).setScale(2, RoundingMode.HALF_UP);
	}
	
	public boolean validarMontoAPagar(BigDecimal monto) {
		boolean result = true;
		BigDecimal montoTotal;

		montoTotal = montoCuotaAcumulado.add(monto).setScale(2, RoundingMode.HALF_UP);

		if ( montoTotal.compareTo(
				CreditoModel.obtenerMontoTotalCredito(this.valorCuota.toString(), String.valueOf(cantCuotas))) == 1 )			
			result = false;

		return result;
	}
	
	public boolean alcanzoMontoFinal() {
			
		if ( montoCuotaAcumulado.compareTo(
				CreditoModel.obtenerMontoTotalCredito(this.valorCuota.toString(), String.valueOf(cantCuotas))) == 0 )
			return true;
		else
			return false;
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

	public static BigDecimal obtenerTasaInteres(
				String montoCreditoIn, BigDecimal montoTotalCredito, String cantCuotasIn, String unidad) {	

		int cantCuotas = Integer.valueOf(cantCuotasIn);
		BigDecimal montoCredito = NumeroUtil.crearBigDecimal(montoCreditoIn);
		
//		A = montoTotalCredito / montoCredito => Importe debido a intereses		
//		B = A / cantCuotas => Interés diario
//		C = Interés Diario * 30 => Tasa interés mensual		
		
		if ( unidad.equals("Días") )
		
		return montoTotalCredito.divide(montoCredito, 15, RoundingMode.HALF_UP)
				.subtract(BigDecimal.valueOf(1))
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(cantCuotas), 15, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(30)).setScale(2, RoundingMode.HALF_UP);
		else // Semanas
			return montoTotalCredito.divide(montoCredito, 15, RoundingMode.HALF_UP)
					.subtract(BigDecimal.valueOf(1))
					.multiply(BigDecimal.valueOf(100))
					.divide(BigDecimal.valueOf(cantCuotas).multiply(BigDecimal.valueOf(7)), 15, RoundingMode.HALF_UP)
					.multiply(BigDecimal.valueOf(30)).setScale(2, RoundingMode.HALF_UP);
	}

	public boolean isCerrado() {
		if (this.cerrado != 0)
			return true;
		else
			return false;		
	}

	public void setCerrado(boolean cerrado) {
		if (cerrado)
			this.cerrado = 1;
		else
			this.cerrado = 0;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public ClienteModel getClienteRef() {
		return cliente.get();
	}

	public void setCliente(ClienteModel cliente) {
		this.cliente.set(cliente);
	}

	public void setCobrador(CobradorModel cobrador) {
		this.cobrador.set(cobrador);
	}

	public void setRuta(RutaModel ruta) {
		this.ruta.set(ruta);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCobrador() {		
		return ( this.cobrador.get() == null ) ? null : this.cobrador.get().getNombre() +
				  " " + this.cobrador.get().getApellido();		
	}

	public CobradorModel getCobradorRef() {
		return this.cobrador.get();		
	}
	
	public RutaModel getRutaRef() {
		return this.ruta.get();
	}

	public String getRuta() {
		return ( this.ruta.get() == null ) ? null : this.ruta.get().getNombre(); 
	}
	
	public void setTasaInt(BigDecimal tasaInt) {
		this.tasaInt = tasaInt;
	}

	public void setMontoCredito(BigDecimal montoCredito) {
		this.montoCredito = montoCredito;
	}

	public void setIdCobrador(int idCobrador) {
		this.idCobrador = idCobrador;
	}

	public void setIdRuta(int idRuta) {
		this.idRuta = idRuta;
	}

	public void setListaPagos(List<PagoModel> listaPagos) {
		this.listaPagos = listaPagos;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public void borrarCredito() {
		try {
			CreditoDAO.borrarCredito(this);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public void cerrar(boolean cerrar) {
		try {
			CreditoDAO.cerrarCredito(this, cerrar);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}		
	}

	public BigDecimal getCuotaCapital() {
		return cuotaCapital;
	}

	public BigDecimal getIndiceCapital() {
		return indiceCapital;
	}
	
	public SimpleObjectProperty<LocalDate> getFecha() {
		return fechaCreacion;
	}

	public void setFecha(SimpleObjectProperty<LocalDate> fecha) {
		this.fechaCreacion = fecha;
	}

	public LocalDate getFechaValue() {
		return this.fechaCreacion.getValue();
	}
}