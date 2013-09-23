package jrippleapi.beans;

import jrippleapi.connection.JSONSerializable;

import org.json.simple.JSONObject;

public class CreditLine implements JSONSerializable {
	public String otherAccount;
	public String balance;
	public String currency;
	public String limit;
	public String limit_peer;
	public long quality_in;
	public long quality_out;
	
	@Override
	public void copyFrom(JSONObject jsonCreditLine) {
		otherAccount=(String) jsonCreditLine.get("account");
		balance=(String) jsonCreditLine.get("balance");
		currency=(String) jsonCreditLine.get("currency");
		limit=(String) jsonCreditLine.get("limit");
		limit_peer=(String) jsonCreditLine.get("limit_peer");
		quality_in=(Long) jsonCreditLine.get("quality_in");
		quality_out=(Long) jsonCreditLine.get("quality_out");
	}

}
