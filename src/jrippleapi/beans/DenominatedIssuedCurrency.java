package jrippleapi.beans;

import jrippleapi.connection.JSONSerializable;

import org.json.simple.JSONObject;

public class DenominatedIssuedCurrency implements JSONSerializable {
	public String amountStr;
	public IssuedCurrency issuance;
	
	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		amountStr = (String) jsonCommandResult.get("value");
		issuance = new IssuedCurrency();
		issuance.copyFrom(jsonCommandResult);
	}

	public void copyFrom(Object jsonObject) {
		if(jsonObject instanceof String){
			issuance=IssuedCurrency.CURRENCY_XRP;
			amountStr=(String) jsonObject;
		}
		else{
			copyFrom((JSONObject) jsonObject);
		}
	}

	public Object toJSON(){
		if(issuance==IssuedCurrency.CURRENCY_XRP){
			return amountStr;
		}
		else{
			JSONObject jsonThis = new JSONObject();
			jsonThis.put("value", amountStr);
			issuance.toJSON(jsonThis);
			return jsonThis;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((amountStr == null) ? 0 : amountStr.hashCode());
		result = prime * result
				+ ((issuance == null) ? 0 : issuance.hashCode());
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
		if (amountStr == null) {
			if (other.amountStr != null)
				return false;
		} else if (!amountStr.equals(other.amountStr))
			return false;
		if (issuance == null) {
			if (other.issuance != null)
				return false;
		} else if (!issuance.equals(other.issuance))
			return false;
		return true;
	}


}
