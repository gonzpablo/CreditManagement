package com.cred.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumeroUtil {

	public static final int EXCEL_MAX_DIGITS = 15;	
	
	public static BigDecimal crearBigDecimal(String value) {
		return new BigDecimal(value).setScale(EXCEL_MAX_DIGITS, RoundingMode.HALF_UP);		
	}	

	public static BigDecimal dividir(String value) {
		return new BigDecimal(value).setScale(EXCEL_MAX_DIGITS, RoundingMode.HALF_UP);
	}
	
	public static BigDecimal multiplicar(String value) {
		return new BigDecimal(value).setScale(EXCEL_MAX_DIGITS, RoundingMode.HALF_UP);
	}
}