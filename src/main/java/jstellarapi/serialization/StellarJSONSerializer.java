package jstellarapi.serialization;

import jstellarapi.core.StellarPaymentTransaction;
import jstellarapi.core.StellarTransaction;

import org.json.simple.JSONObject;

public class StellarJSONSerializer {
	//TODO Fix all this
	public static Object createFromJSON(JSONObject jsonObj) {
		switch((String) jsonObj.get("type")){

		case "transaction":
			return createTransactionFromJSON(jsonObj);
			
		default:
			return null;
		}
	}

	public static StellarTransaction createTransactionFromJSON(JSONObject jsonObj) {
		StellarTransaction tx=null;
		switch((String) jsonObj.get("TransactionType")){
		case "Payment": 
			tx=new StellarPaymentTransaction(jsonObj);
			break;
		case "OfferCreate":
		case "OfferCancel":
		case "TrustSet":
		case "AccountSet":
			tx=new StellarTransaction();
		}
		return tx;
	}

}
