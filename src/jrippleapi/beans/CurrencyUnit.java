package jrippleapi.beans;
import java.math.BigDecimal;
import java.math.BigInteger;


public class CurrencyUnit {
	public String currencyCode;
	
	private CurrencyUnit(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	public static final CurrencyUnit USD = new CurrencyUnit("USD");
	public static final CurrencyUnit CAD = new CurrencyUnit("CAD");
	public static final CurrencyUnit EUR = new CurrencyUnit("EUR");
	public static final CurrencyUnit BTC = new CurrencyUnit("BTC");
	public static final CurrencyUnit XRP = new CurrencyUnit("XRP"){
		@Override
		public BigDecimal fromString(String strDenomination) {
			BigDecimal microRipples = new BigDecimal(strDenomination);
			return microRipples.movePointLeft(6);
		}
		public String toString(BigDecimal amount) {
			return amount.movePointRight(6).toPlainString();
		}
	};

	public BigDecimal fromString(String strDenomination) {
		return new BigDecimal(strDenomination);
	}

	public String toString(BigDecimal amount) {
		return amount.toPlainString();
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
