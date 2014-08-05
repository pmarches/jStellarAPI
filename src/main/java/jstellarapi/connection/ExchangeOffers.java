package jstellarapi.connection;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ExchangeOffers extends ArrayList<ExchangeOffer> implements JSONSerializable {
	private static final long serialVersionUID = 5605897503055346397L;

	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		JSONArray jsonOffers = (JSONArray) jsonCommandResult.get("offers");
		for(int i=0; i<jsonOffers.size(); i++){
			JSONObject jsonOffer = (JSONObject) jsonOffers.get(i);
			ExchangeOffer offer = new ExchangeOffer();
			offer.copyFrom(jsonOffer);
			add(offer);
		}
	}

}
