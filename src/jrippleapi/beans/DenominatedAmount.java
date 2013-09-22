package jrippleapi.beans;

import jrippleapi.JSONSerializable;

import org.json.simple.JSONObject;

public class DenominatedAmount implements JSONSerializable {
	public String amountStr;
	public Denomination denomination;
	
	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		amountStr = (String) jsonCommandResult.get("value");
		denomination = new Denomination();
		denomination.copyFrom(jsonCommandResult);
	}

	public void copyFrom(Object jsonObject) {
		if(jsonObject instanceof String){
			denomination=Denomination.XRP_DENOMINATION;
			amountStr=(String) jsonObject;
		}
		else{
			copyFrom((JSONObject) jsonObject);
		}
	}

}
