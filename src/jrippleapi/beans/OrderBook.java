package jrippleapi.beans;

import java.util.ArrayList;

import jrippleapi.connection.JSONSerializable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class OrderBook extends ArrayList<OrderBookEntry> implements JSONSerializable {
	private static final long serialVersionUID = 7170053946114996963L;

	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		JSONArray jsonOffers = (JSONArray) jsonCommandResult.get("offers");
		for(int i=0; i<jsonOffers.size(); i++){
			OrderBookEntry bookEntry = new OrderBookEntry();
			bookEntry.copyFrom((JSONObject) jsonOffers.get(i));
			add(bookEntry);
		}
	}

}
