package jrippleapi.beans;

import org.json.simple.JSONObject;

import jrippleapi.JSONSerializable;

public class OrderBookEntry implements JSONSerializable {
	String accountStr;
	DenominatedAmount takerGetsAmount;
	DenominatedAmount takerPaysAmount;

	@Override
	public void copyFrom(JSONObject jsonOrderBookEntry) {
		jsonOrderBookEntry.get("index");

		takerPaysAmount = new DenominatedAmount();
		takerPaysAmount.copyFrom(jsonOrderBookEntry.get("TakerPays"));
		
		takerGetsAmount = new DenominatedAmount();
		takerGetsAmount.copyFrom(jsonOrderBookEntry.get("TakerGets"));
		
		accountStr=(String) jsonOrderBookEntry.get("Account");
		jsonOrderBookEntry.get("BookDirectory");
		jsonOrderBookEntry.get("LedgerEntryType");
		jsonOrderBookEntry.get("Sequence");
		jsonOrderBookEntry.get("BookNode");
		jsonOrderBookEntry.get("Flags");
		jsonOrderBookEntry.get("OwnerNode");
		jsonOrderBookEntry.get("PreviousTxnID");
		jsonOrderBookEntry.get("PreviousTxnLgrSeq");
		jsonOrderBookEntry.get("quality");
		jsonOrderBookEntry.get("");
		jsonOrderBookEntry.get("");
		jsonOrderBookEntry.get("");
	}

}
