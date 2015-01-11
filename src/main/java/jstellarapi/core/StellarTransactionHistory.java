package jstellarapi.core;

import java.util.ArrayList;

import jstellarapi.connection.JSONSerializable;
import jstellarapi.serialization.StellarBinaryObject;
import jstellarapi.serialization.StellarBinarySerializer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StellarTransactionHistory implements JSONSerializable {
	ArrayList<StellarTransaction> txHistory=new ArrayList<StellarTransaction>();
	
	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		StellarBinarySerializer ser = new StellarBinarySerializer();
		JSONArray transactions=(JSONArray) jsonCommandResult.get("transactions");
		for(int i=0; i<transactions.size(); i++){
			JSONObject txJSON=(JSONObject) transactions.get(i);
			String txHexBlob=(String) txJSON.get("tx_blob");
			if(txHexBlob==null){
				throw new RuntimeException("non-binary tx not implemented");
			}
			StellarBinaryObject rbo=ser.readBinaryObject(txHexBlob);
			StellarTransaction txObject= StellarTransaction.createFromRBO(rbo);
			long ledgerIndex=(long) txJSON.get("ledger_index");
			txObject.setLedgerIndex(ledgerIndex);
			txHistory.add(txObject);
		}
	}

	public void filterInPaymentsOnly() {
		ArrayList<StellarTransaction> txPaymentHistory=new ArrayList<StellarTransaction>(size());
		for(StellarTransaction tx :txHistory){
			if(tx instanceof StellarPaymentTransaction){
				txPaymentHistory.add(tx);
			}
		}
		txHistory=txPaymentHistory;
	}

	public int size() {
		return txHistory.size();
	}

	public StellarTransaction get(int i) {
		return txHistory.get(i);
	}

}
