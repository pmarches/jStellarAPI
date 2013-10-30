package jrippleapi.connection;

import jrippleapi.core.DenominatedIssuedCurrency;

import org.json.simple.JSONObject;

public class OrderBookEntry implements JSONSerializable {
	String accountStr;
	DenominatedIssuedCurrency takerGetsAmount;
	DenominatedIssuedCurrency takerPaysAmount;

	@Override
	public void copyFrom(JSONObject jsonOrderBookEntry) {
		jsonOrderBookEntry.get("index");

		takerPaysAmount = new DenominatedIssuedCurrency();
		takerPaysAmount.copyFrom(jsonOrderBookEntry.get("TakerPays"));
		
		takerGetsAmount = new DenominatedIssuedCurrency();
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
