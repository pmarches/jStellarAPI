package jstellarapi.connection;

import org.json.simple.JSONObject;

public class TrustLine implements JSONSerializable {
	public String otherAccount;
	public String balance;
	public String currency;
	public String limit;
	public String limit_peer;
	
	@Override
	public void copyFrom(JSONObject jsonTrustLine) {
		otherAccount=(String) jsonTrustLine.get("account");
		balance=(String) jsonTrustLine.get("balance");
		currency=(String) jsonTrustLine.get("currency");
		limit=(String) jsonTrustLine.get("limit");
		limit_peer=(String) jsonTrustLine.get("limit_peer");
	}

	@Override
	public String toString() {
		return "TrustLine [otherAccount=" + otherAccount + ", balance=" + balance + ", currency=" + currency + ", limit=" + limit + ", limit_peer=" + limit_peer + "]";
	}

}
