package jrippleapi.beans;

import org.json.simple.JSONObject;

import jrippleapi.connection.JSONSerializable;

public class IssuedCurrency implements JSONSerializable {
	public String currencyStr;
	public String issuerStr;
	
	public static final IssuedCurrency CURRENCY_XRP = new IssuedCurrency();
	public static final IssuedCurrency CURRENCY_BITSTAMP_USD = new IssuedCurrency();
	public static final IssuedCurrency CURRENCY_BITSTAMP_BTC = new IssuedCurrency();
	static {
		CURRENCY_XRP.currencyStr="XRP";

		CURRENCY_BITSTAMP_USD.currencyStr="USD";
		CURRENCY_BITSTAMP_USD.issuerStr=Account.RIPPLE_ADDRESS_BITSTAMP;

		CURRENCY_BITSTAMP_BTC.currencyStr="BTC";
		CURRENCY_BITSTAMP_BTC.issuerStr=Account.RIPPLE_ADDRESS_BITSTAMP;
	}
	
	@Override
	public void copyFrom(JSONObject jsonDenomination) {
		currencyStr = ((String) jsonDenomination.get("currency")).intern();
		issuerStr = ((String) jsonDenomination.get("issuer")).intern();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currencyStr == null) ? 0 : currencyStr.hashCode());
		result = prime * result
				+ ((issuerStr == null) ? 0 : issuerStr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IssuedCurrency other = (IssuedCurrency) obj;
		if (currencyStr == null) {
			if (other.currencyStr != null)
				return false;
		} else if (!currencyStr.equals(other.currencyStr))
			return false;
		if (issuerStr == null) {
			if (other.issuerStr != null)
				return false;
		} else if (!issuerStr.equals(other.issuerStr))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if(this==CURRENCY_XRP){
			return currencyStr;
		}
		return currencyStr+"/"+issuerStr;
	}

	public void toJSON(JSONObject jsonThis) {
		jsonThis.put("currency", currencyStr);
		jsonThis.put("issuer", issuerStr);
	}
}
