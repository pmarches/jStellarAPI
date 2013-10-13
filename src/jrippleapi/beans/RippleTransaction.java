package jrippleapi.beans;

import jrippleapi.connection.JSONSerializable;

import org.json.simple.JSONObject;

public class RippleTransaction implements JSONSerializable {
	String payer;
	String payee;
	DenominatedIssuedCurrency amount;
	String signedTransactionBlob;
	Long sequenceNumber;
	String txHash;
	public String signature;
	public String publicKeyUsedToSign;
	
	public RippleTransaction(String payer, String payee, DenominatedIssuedCurrency amount){
		this.payer=payer;
		this.payee=payee;
		this.amount = amount;
	}

	public JSONObject getTxJSON() {
		JSONObject jsonTx = new JSONObject();
		jsonTx.put("Account", payer);
		jsonTx.put("Destination", payee);
		jsonTx.put("Amount", amount.toJSON());
		jsonTx.put("TransactionType", "Payment");
		return jsonTx;
	}

	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		signedTransactionBlob=(String) jsonCommandResult.get("tx_blob");
		JSONObject tx_json = (JSONObject) jsonCommandResult.get("tx_json");
		if(tx_json!=null){
			payer=(String) tx_json.get("Account");
			payee=(String) tx_json.get("Destination");
//			amount = (String) tx_json.get("Amount");
			sequenceNumber=(Long) tx_json.get("Sequence");
			txHash = (String) tx_json.get("hash");
			signature = (String) tx_json.get("TxnSignature");
			publicKeyUsedToSign = (String) tx_json.get("SigningPubKey");
//			flags = (Long) tx_json.get("Flags");
		}
	}

	public String getSignedTxBlob() {
		return signedTransactionBlob;
	}
}
