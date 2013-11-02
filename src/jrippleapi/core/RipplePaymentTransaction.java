package jrippleapi.core;

import jrippleapi.connection.JSONSerializable;
import jrippleapi.serialization.RippleBinarySchema.BinaryFormatField;
import jrippleapi.serialization.RippleBinarySchema.TransactionTypes;
import jrippleapi.serialization.RippleBinaryObject;

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
	
	public RipplePaymentTransaction(RippleBinaryObject serObj){
		if(serObj.getTransactionType()!=TransactionTypes.PAYMENT){
			throw new RuntimeException("The RippleBinaryObject is not a payment transaction, but a "+serObj.getTransactionType());
		}
		payer = (RippleAddress) serObj.getField(BinaryFormatField.Account);
		payee = (RippleAddress) serObj.getField(BinaryFormatField.Destination);
		amount = (DenominatedIssuedCurrency) serObj.getField(BinaryFormatField.Amount);
	}

	public RippleBinaryObject getBinaryObject() {
		RippleBinaryObject rbo = new RippleBinaryObject();
		rbo.putField(BinaryFormatField.TransactionType, (int) TransactionTypes.PAYMENT.byteValue);
		rbo.putField(BinaryFormatField.Account, this.payer);
		rbo.putField(BinaryFormatField.Destination, this.payee);
		rbo.putField(BinaryFormatField.Amount, this.amount);

		return rbo;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((payee == null) ? 0 : payee.hashCode());
		result = prime * result + ((payer == null) ? 0 : payer.hashCode());
		result = prime
				* result
				+ ((publicKeyUsedToSign == null) ? 0 : publicKeyUsedToSign
						.hashCode());
		result = prime * result
				+ ((sequenceNumber == null) ? 0 : sequenceNumber.hashCode());
		result = prime * result
				+ ((signature == null) ? 0 : signature.hashCode());
		result = prime
				* result
				+ ((signedTransactionBlob == null) ? 0 : signedTransactionBlob
						.hashCode());
		result = prime * result + ((txHash == null) ? 0 : txHash.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RipplePaymentTransaction other = (RipplePaymentTransaction) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (payee == null) {
			if (other.payee != null)
				return false;
		} else if (!payee.equals(other.payee))
			return false;
		if (payer == null) {
			if (other.payer != null)
				return false;
		} else if (!payer.equals(other.payer))
			return false;
		if (publicKeyUsedToSign == null) {
			if (other.publicKeyUsedToSign != null)
				return false;
		} else if (!publicKeyUsedToSign.equals(other.publicKeyUsedToSign))
			return false;
		if (sequenceNumber == null) {
			if (other.sequenceNumber != null)
				return false;
		} else if (!sequenceNumber.equals(other.sequenceNumber))
			return false;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		if (signedTransactionBlob == null) {
			if (other.signedTransactionBlob != null)
				return false;
		} else if (!signedTransactionBlob.equals(other.signedTransactionBlob))
			return false;
		if (txHash == null) {
			if (other.txHash != null)
				return false;
		} else if (!txHash.equals(other.txHash))
			return false;
		return true;
	}	
	
}
