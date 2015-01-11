package jstellarapi.core;

import java.math.BigInteger;

import jstellarapi.connection.JSONSerializable;
import jstellarapi.serialization.StellarBinaryObject;
import jstellarapi.serialization.StellarBinarySchema.BinaryFormatField;
import jstellarapi.serialization.StellarBinarySchema.TransactionTypes;

import org.json.simple.JSONObject;

public class StellarPaymentTransaction extends StellarTransaction implements JSONSerializable {
	public StellarAddress payer;
	public StellarAddress payee;
	public DenominatedIssuedCurrency amount;
	public String signedTransactionBlob;
	public long sequenceNumber;
	public String txHash;
	public String signature;
	public String publicKeyUsedToSign;
	public DenominatedIssuedCurrency fee;
	public long flags;
	
	public StellarPaymentTransaction(StellarAddress payer, StellarAddress payee, DenominatedIssuedCurrency amount, int sequenceNumber){
		this.payer=payer;
		this.payee=payee;
		this.amount = amount;
		this.sequenceNumber = sequenceNumber;
		this.fee = DenominatedIssuedCurrency.FEE;
	}
	
	public StellarPaymentTransaction(StellarBinaryObject serObj){
		super(serObj);
		if(serObj.getTransactionType()!=TransactionTypes.PAYMENT){
			throw new RuntimeException("The StellarBinaryObject is not a payment transaction, but a "+serObj.getTransactionType());
		}
		payer = (StellarAddress) serObj.getField(BinaryFormatField.Account);
		payee = (StellarAddress) serObj.getField(BinaryFormatField.Destination);
		amount = (DenominatedIssuedCurrency) serObj.getField(BinaryFormatField.Amount);
		sequenceNumber = (long) serObj.getField(BinaryFormatField.Sequence);
		fee= (DenominatedIssuedCurrency) serObj.getField(BinaryFormatField.Fee);
		flags= (long) serObj.getField(BinaryFormatField.Flags);
	}

	public StellarPaymentTransaction(JSONObject jsonTx) {
		fromTxJSON(jsonTx);
	}

	public StellarBinaryObject getBinaryObject() {
		StellarBinaryObject rbo = new StellarBinaryObject();
		rbo.putField(BinaryFormatField.TransactionType, (int) TransactionTypes.PAYMENT.byteValue);
		rbo.putField(BinaryFormatField.Account, this.payer);
		rbo.putField(BinaryFormatField.Destination, this.payee);
		rbo.putField(BinaryFormatField.Amount, this.amount);
		rbo.putField(BinaryFormatField.Sequence, this.sequenceNumber);
		rbo.putField(BinaryFormatField.Fee, this.fee);
		rbo.putField(BinaryFormatField.Flags, this.flags);

		return rbo;
	}

	public JSONObject toTxJSON() {
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
			fromTxJSON(tx_json);
		}
	}

	protected void fromTxJSON(JSONObject tx_json) {
		payer=new StellarAddress((String) tx_json.get("Account"));
		payee=new StellarAddress((String) tx_json.get("Destination"));
		sequenceNumber=(Long) tx_json.get("Sequence");
		txHash = (String) tx_json.get("hash");
		signature = (String) tx_json.get("TxnSignature");
		publicKeyUsedToSign = (String) tx_json.get("SigningPubKey");
		flags = (Long) tx_json.get("Flags");
		Object amount = tx_json.get("Amount");
		if (amount instanceof String) {
			String amountInMicroSTR = (String) amount;
			this.amount = new DenominatedIssuedCurrency(amountInMicroSTR);
		}else if(amount instanceof JSONObject){
			JSONObject jamt = (JSONObject) amount;
			this.amount = new DenominatedIssuedCurrency(jamt.get("value").toString(), new StellarAddress(jamt.get("issuer").toString()), jamt.get("currency").toString());
		}else{
			this.amount = new DenominatedIssuedCurrency();
		}
	}

	public String getSignedTxBlob() {
		return signedTransactionBlob;
	}	
}
