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
	public long sequenceNumber;
	public String txHash;
	public String signature;
	public String publicKeyUsedToSign;
	public DenominatedIssuedCurrency fee;
	public long flags;
	
	public RipplePaymentTransaction(RippleAddress payer, RippleAddress payee, DenominatedIssuedCurrency amount, int sequenceNumber){
		this.payer=payer;
		this.payee=payee;
		this.amount = amount;
		this.sequenceNumber = sequenceNumber;
		this.fee = new DenominatedIssuedCurrency(10);
	}
	
	public RipplePaymentTransaction(RippleBinaryObject serObj){
		if(serObj.getTransactionType()!=TransactionTypes.PAYMENT){
			throw new RuntimeException("The RippleBinaryObject is not a payment transaction, but a "+serObj.getTransactionType());
		}
		payer = (RippleAddress) serObj.getField(BinaryFormatField.Account);
		payee = (RippleAddress) serObj.getField(BinaryFormatField.Destination);
		amount = (DenominatedIssuedCurrency) serObj.getField(BinaryFormatField.Amount);
		sequenceNumber = (long) serObj.getField(BinaryFormatField.Sequence);
		fee= (DenominatedIssuedCurrency) serObj.getField(BinaryFormatField.Fee);
		flags= (long) serObj.getField(BinaryFormatField.Flags);
	}

	public RippleBinaryObject getBinaryObject() {
		RippleBinaryObject rbo = new RippleBinaryObject();
		rbo.putField(BinaryFormatField.TransactionType, (int) TransactionTypes.PAYMENT.byteValue);
		rbo.putField(BinaryFormatField.Account, this.payer);
		rbo.putField(BinaryFormatField.Destination, this.payee);
		rbo.putField(BinaryFormatField.Amount, this.amount);
		rbo.putField(BinaryFormatField.Sequence, this.sequenceNumber);
		rbo.putField(BinaryFormatField.Fee, this.fee);
		rbo.putField(BinaryFormatField.Flags, this.flags);

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
}
