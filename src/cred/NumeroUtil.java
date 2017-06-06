package cred;

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
	
	/**
	 * Fix floating-point rounding errors.
	 *
	 * https://en.wikipedia.org/wiki/Numeric_precision_in_Microsoft_Excel
	 * https://support.microsoft.com/en-us/kb/214118
	 * https://support.microsoft.com/en-us/kb/269370
	 */	
	
	private static double fixFloatingPointPrecision(double value) {
	    BigDecimal original = new BigDecimal(value);
	    BigDecimal fixed = new BigDecimal(original.unscaledValue(), original.precision())
	            .setScale(EXCEL_MAX_DIGITS, RoundingMode.HALF_UP);
	    int newScale = original.scale() - original.precision() + EXCEL_MAX_DIGITS;
	    return new BigDecimal(fixed.unscaledValue(), newScale).doubleValue();
	}	
}