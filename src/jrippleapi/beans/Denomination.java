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
		USD_DENOMINATION.issuerStr="rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh";
		BTC_DENOMINATION.currencyStr="BTC";
		BTC_DENOMINATION.issuerStr="rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B";
	}
	
	@Override
	public void copyFrom(JSONObject jsonDenomination) {
		currencyStr = ((String) jsonDenomination.get("currency")).intern();
		issuerStr = ((String) jsonDenomination.get("issuer")).intern();
	}

	@Override
	public String toString() {
		if(this==XRP_DENOMINATION){
			return currencyStr;
		}
		return currencyStr+"/"+issuerStr;
	}
}
