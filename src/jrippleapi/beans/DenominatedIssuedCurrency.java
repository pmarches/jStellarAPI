package jrippleapi.beans;

import java.math.BigDecimal;
import java.math.BigInteger;

import jrippleapi.connection.JSONSerializable;

import org.json.simple.JSONObject;

public class DenominatedIssuedCurrency implements JSONSerializable {
	public BigDecimal amount;
	public RippleAddress issuer;
	public CurrencyUnit currency;
	
	public DenominatedIssuedCurrency(){
	}
	
	public DenominatedIssuedCurrency(BigDecimal amount, RippleAddress issuer, CurrencyUnit currency){
		this.amount = amount;
		this.issuer = issuer;
		this.currency = currency;
	}
	
	public DenominatedIssuedCurrency(BigDecimal amount) {
		this.amount=amount;
		this.currency = CurrencyUnit.XRP;
	}
	
	@Override
	public String toString() {
		if(issuer==null || currency==null || currency==CurrencyUnit.XRP){
			return CurrencyUnit.XRP.toString(amount);
		}
		return currency.toString(amount)+"/"+issuer;
	}

	@Override
	public void copyFrom(JSONObject jsonDenomination) {
		issuer = new RippleAddress(((String) jsonDenomination.get("issuer")));
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
			jsonThis.put("issuer", issuer.toString());
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
				+ ((issuer == null) ? 0 : issuer.hashCode());
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
		if (issuer == null) {
			if (other.issuer != null)
				return false;
		} else if (!issuer.equals(other.issuer))
			return false;
		return true;
	}

	
}
