package jrippleapi.beans;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;


public class CurrencyUnit {
	public String currencyCode;
	public int scale;
	
	private CurrencyUnit(String currencyCode, int scale) {
		this.currencyCode = currencyCode;
		this.scale = (byte) scale;
	}
	
	public static final CurrencyUnit USD = new CurrencyUnit("USD", 2);
	public static final CurrencyUnit CAD = new CurrencyUnit("CAD", 2);
	public static final CurrencyUnit EUR = new CurrencyUnit("EUR", 2);
	public static final CurrencyUnit BTC = new CurrencyUnit("BTC", 8); //1 BTC==100 000 000 satoshis
	public static final CurrencyUnit XRP = new CurrencyUnit("XRP", 6){ //1 XRP==  1 000 000 drops
		DecimalFormat TWO_DIGIT_PRECISION_FORMAT =new DecimalFormat("## #### ####.## XRP");
		public String toString(BigInteger amount) {
			BigDecimal humanReadableAmount=new BigDecimal(amount);
			return TWO_DIGIT_PRECISION_FORMAT.format(humanReadableAmount.scaleByPowerOfTen(-scale));
		}
	};

	public BigInteger fromString(String strDenomination) {
		BigDecimal fractionalForm = new BigDecimal(strDenomination);
		return fractionalForm.scaleByPowerOfTen(scale).toBigInteger();
	}

	public String toString(BigInteger amount) {
		return amount.toString()+" "+currencyCode;
	}

	public static CurrencyUnit parse(String currencyStr) {
		if("USD".equals(currencyStr)){
			return USD;
		}
		else if("XRP".equals(currencyStr)){
			return XRP;
		}
		else if("CAD".equals(currencyStr)){
			return CAD;
		}
		else if("EUR".equals(currencyStr)){
			return EUR;
		}
		else if("BTC".equals(currencyStr)){
			return BTC;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return currencyCode;
	}
}
