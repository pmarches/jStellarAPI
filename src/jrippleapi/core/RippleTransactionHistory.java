package jrippleapi.core;

import java.util.ArrayList;

import jrippleapi.connection.JSONSerializable;
import jrippleapi.serialization.RippleBinaryObject;
import jrippleapi.serialization.RippleBinarySerializer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class RippleTransactionHistory implements JSONSerializable {
	ArrayList<RippleTransaction> txHistory=new ArrayList<RippleTransaction>();
	
	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		RippleBinarySerializer ser = new RippleBinarySerializer();
		JSONArray transactions=(JSONArray) jsonCommandResult.get("transactions");
		for(int i=0; i<transactions.size(); i++){
			JSONObject txJSON=(JSONObject) transactions.get(i);
			String txHexBlob=(String) txJSON.get("tx_blob");
			if(txHexBlob==null){
				throw new RuntimeException("non-binary tx not implemented");
			}
			RippleBinaryObject rbo=ser.readBinaryObject(txHexBlob);
			RippleTransaction txObject= RippleTransaction.createFromRBO(rbo);
			long ledgerIndex=(long) txJSON.get("ledger_index");
			txObject.setLedgerIndex(ledgerIndex);
			txHistory.add(txObject);
		}
	}

	public void filterInPaymentsOnly() {
		ArrayList<RippleTransaction> txPaymentHistory=new ArrayList<RippleTransaction>(size());
		for(RippleTransaction tx :txHistory){
			if(tx instanceof RipplePaymentTransaction){
				txPaymentHistory.add(tx);
			}
		}
		txHistory=txPaymentHistory;
	}

	public int size() {
		return txHistory.size();
	}

	public RippleTransaction get(int i) {
		return txHistory.get(i);
	}

}
