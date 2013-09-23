package jrippleapi.beans;

import org.json.simple.JSONObject;

public class Transaction {
	public static JSONObject createPayment(Account payer, String payee, DenominatedIssuedCurrency amount){
		if(payer.secret==null){
			throw new NullPointerException("payer.secret must not be null");
		}
		Transaction tx = new Transaction();
		
		JSONObject jsonTx = new JSONObject();
		jsonTx.put("Account", payer.account);
		jsonTx.put("Amount", amount.toJSON());
		jsonTx.put("TransactionType", "Payment");
		jsonTx.put("Destination", payee);

		return jsonTx; //TODO return Transaction eventually..
	}
}
