package jrippleapi.beans;

import java.math.BigDecimal;

import jrippleapi.connection.JSONSerializable;

import org.json.simple.JSONObject;

public class ExchangeOffer implements JSONSerializable {
	public long sequenceNumber;
	public DenominatedIssuedCurrency takerGets;
	public DenominatedIssuedCurrency takerPays;

	@Override
	public void copyFrom(JSONObject jsonOffer) {
		sequenceNumber = (long) jsonOffer.get("seq");
		takerGets = jsonToDenominatedAmount(jsonOffer.get("taker_gets"));
		takerPays = jsonToDenominatedAmount(jsonOffer.get("taker_pays"));
	}

	private DenominatedIssuedCurrency jsonToDenominatedAmount(Object jsonDenominatedAmount) {
		if(jsonDenominatedAmount instanceof JSONObject){
			DenominatedIssuedCurrency amount = new DenominatedIssuedCurrency();
			amount.copyFrom((JSONObject) jsonDenominatedAmount);
			return amount;
		}
		else{
			return new DenominatedIssuedCurrency(new BigDecimal((String) jsonDenominatedAmount));
		}
	}

}
