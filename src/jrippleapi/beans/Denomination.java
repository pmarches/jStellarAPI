package jrippleapi.beans;

import org.json.simple.JSONObject;

import jrippleapi.JSONSerializable;

public class Denomination implements JSONSerializable {
	public String currencyStr;
	public String issuerStr;
	
	public static final Denomination XRP_DENOMINATION = new Denomination();
	public static final Denomination USD_DENOMINATION = new Denomination();
	public static final Denomination BTC_DENOMINATION = new Denomination();
	static {
		XRP_DENOMINATION.currencyStr="XRP";

		USD_DENOMINATION.currencyStr="USD";
		USD_DENOMINATION.issuerStr=Account.RIPPLE_ADDRESS_BITSTAMP;

		BTC_DENOMINATION.currencyStr="BTC";
		BTC_DENOMINATION.issuerStr=Account.RIPPLE_ADDRESS_BITSTAMP;
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
		Denomination other = (Denomination) obj;
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
		if(this==XRP_DENOMINATION){
			return currencyStr;
		}
		return currencyStr+"/"+issuerStr;
	}
}
