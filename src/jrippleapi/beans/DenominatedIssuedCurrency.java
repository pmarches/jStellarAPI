package jrippleapi.beans;

import java.math.BigDecimal;

import org.json.simple.JSONObject;

import jrippleapi.connection.JSONSerializable;

public class DenominatedIssuedCurrency implements JSONSerializable {
	public BigDecimal amount;
	public String issuerStr;
	public CurrencyUnit currency;
		
	@Override
	public void copyFrom(JSONObject jsonDenomination) {
		issuerStr = ((String) jsonDenomination.get("issuer"));
		String currencyStr = ((String) jsonDenomination.get("currency"));
		currency = CurrencyUnit.parse(currencyStr);

		String amountStr = (String) jsonDenomination.get("value");
		amount=currency.fromString(amountStr);
	}

	public void copyFrom(Object jsonObject) {
		if(jsonObject instanceof String){
			currency = CurrencyUnit.XRP;
			amount=currency.fromString((String) jsonObject);
		}
		else{
			copyFrom((JSONObject) jsonObject);
		}
	}

	public Object toJSON(){
		if(currency==CurrencyUnit.XRP){
			return currency.toString(amount);
		}
		else{
			JSONObject jsonThis = new JSONObject();
			jsonThis.put("value", currency.toString(amount));
			jsonThis.put("issuer", issuerStr);
			jsonThis.put("currency", currency.currencyCode);
			return jsonThis;
		}
	}

}
