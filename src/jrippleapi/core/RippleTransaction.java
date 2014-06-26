package jrippleapi.core;

import jrippleapi.serialization.RippleBinaryObject;

public class RippleTransaction {

	long ledgerIndex;

	public RippleTransaction() {
	}
	
	public RippleTransaction(RippleBinaryObject rbo) {
	}

	public static RippleTransaction createFromRBO(RippleBinaryObject rbo) {
		switch(rbo.getTransactionType()){
			case PAYMENT: return new RipplePaymentTransaction(rbo);
			//TODO Add other TX types here
			default: return new RippleTransaction(rbo);
		}
	}

	public void setLedgerIndex(long ledgerIndex) {
		this.ledgerIndex=ledgerIndex;
	}
	public long getLedgerIndex() {
		return ledgerIndex;
	}

}
