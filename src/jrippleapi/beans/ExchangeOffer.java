package jrippleapi.beans;

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
		DenominatedIssuedCurrency amount = new DenominatedIssuedCurrency();
		if(jsonDenominatedAmount instanceof JSONObject){
			amount.copyFrom((JSONObject) jsonDenominatedAmount);
		}
		else{
			amount.denomination = IssuedCurrency.XRP_DENOMINATION;
			//FIXME XRPs are specified with 6 zeroes
			//1 XRP == 1 000 000
			amount.amountStr = (String) jsonDenominatedAmount;
		}
		return amount;
	}

}
