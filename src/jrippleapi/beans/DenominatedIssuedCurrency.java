package jrippleapi.beans;

import jrippleapi.connection.JSONSerializable;

import org.json.simple.JSONObject;

public class DenominatedIssuedCurrency implements JSONSerializable {
	public String amountStr;
	public IssuedCurrency denomination;
	
	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		amountStr = (String) jsonCommandResult.get("value");
		denomination = new IssuedCurrency();
		denomination.copyFrom(jsonCommandResult);
	}

	public void copyFrom(Object jsonObject) {
		if(jsonObject instanceof String){
			denomination=IssuedCurrency.XRP_DENOMINATION;
			amountStr=(String) jsonObject;
		}
		else{
			copyFrom((JSONObject) jsonObject);
		}
	}

}
