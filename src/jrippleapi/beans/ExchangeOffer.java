package jrippleapi.beans;

import jrippleapi.JSONSerializable;

import org.json.simple.JSONObject;

public class ExchangeOffer implements JSONSerializable {
	public long sequenceNumber;
	public DenominatedAmount takerGets;
	public DenominatedAmount takerPays;

	@Override
	public void copyFrom(JSONObject jsonOffer) {
		sequenceNumber = (long) jsonOffer.get("seq");
		takerGets = jsonToDenominatedAmount(jsonOffer.get("taker_gets"));
		takerPays = jsonToDenominatedAmount(jsonOffer.get("taker_pays"));
	}

	private DenominatedAmount jsonToDenominatedAmount(Object jsonDenominatedAmount) {
		DenominatedAmount amount = new DenominatedAmount();
		if(jsonDenominatedAmount instanceof JSONObject){
			amount.copyFrom((JSONObject) jsonDenominatedAmount);
		}
		else{
			amount.denomination = Denomination.XRP_DENOMINATION;
			//FIXME XRPs are specified with 6 zeroes
			//1 XRP == 1 000 000
			amount.amountStr = (String) jsonDenominatedAmount;
		}
		return amount;
	}

}
