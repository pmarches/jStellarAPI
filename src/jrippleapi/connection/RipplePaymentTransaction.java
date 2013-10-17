package jrippleapi.connection;

import jrippleapi.beans.DenominatedIssuedCurrency;
import jrippleapi.beans.RippleAddress;
import jrippleapi.serialization.RippleSerializedObject;

import org.json.simple.JSONObject;

public class RipplePaymentTransaction implements JSONSerializable {
	public RippleAddress payer;
	public RippleAddress payee;
	public DenominatedIssuedCurrency amount;
	public String signedTransactionBlob;
	public Long sequenceNumber;
	public String txHash;
	public String signature;
	public String publicKeyUsedToSign;
	
	public RipplePaymentTransaction(RippleAddress payer, RippleAddress payee, DenominatedIssuedCurrency amount){
		this.payer=payer;
		this.payee=payee;
		this.amount = amount;
	}
	
	public RipplePaymentTransaction(RippleSerializedObject serObj){
	}

	public JSONObject getTxJSON() {
		JSONObject jsonTx = new JSONObject();
		jsonTx.put("Account", payer.toString());
		jsonTx.put("Destination", payee.toString());
		jsonTx.put("Amount", amount.toJSON());
		jsonTx.put("TransactionType", "Payment");
		return jsonTx;
	}

	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		signedTransactionBlob=(String) jsonCommandResult.get("tx_blob");
		JSONObject tx_json = (JSONObject) jsonCommandResult.get("tx_json");
		if(tx_json!=null){
			payer=new RippleAddress((String) tx_json.get("Account"));
			payee=new RippleAddress((String) tx_json.get("Destination"));
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
