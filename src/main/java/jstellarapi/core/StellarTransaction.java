package jstellarapi.core;

import jstellarapi.serialization.StellarBinaryObject;

public class StellarTransaction {

	long ledgerIndex;

	public StellarTransaction() {
	}
	
	public StellarTransaction(StellarBinaryObject rbo) {
	}

	public static StellarTransaction createFromRBO(StellarBinaryObject rbo) {
		switch(rbo.getTransactionType()){
			case PAYMENT: return new StellarPaymentTransaction(rbo);
			//TODO Add other TX types here
			default: return new StellarTransaction(rbo);
		}
	}

	public void setLedgerIndex(long ledgerIndex) {
		this.ledgerIndex=ledgerIndex;
	}
	public long getLedgerIndex() {
		return ledgerIndex;
	}

}
