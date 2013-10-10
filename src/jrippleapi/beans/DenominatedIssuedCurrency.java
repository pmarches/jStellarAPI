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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result
				+ ((currency == null) ? 0 : currency.hashCode());
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
		DenominatedIssuedCurrency other = (DenominatedIssuedCurrency) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (issuerStr == null) {
			if (other.issuerStr != null)
				return false;
		} else if (!issuerStr.equals(other.issuerStr))
			return false;
		return true;
	}

	
}
