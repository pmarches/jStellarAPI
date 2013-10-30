package jrippleapi.connection;

import org.json.simple.JSONObject;

public class TrustLine implements JSONSerializable {
	public String otherAccount;
	public String balance;
	public String currency;
	public String limit;
	public String limit_peer;
	public long quality_in;
	public long quality_out;
	
	@Override
	public void copyFrom(JSONObject jsonTrustLine) {
		otherAccount=(String) jsonTrustLine.get("account");
		balance=(String) jsonTrustLine.get("balance");
		currency=(String) jsonTrustLine.get("currency");
		limit=(String) jsonTrustLine.get("limit");
		limit_peer=(String) jsonTrustLine.get("limit_peer");
		quality_in=(Long) jsonTrustLine.get("quality_in");
		quality_out=(Long) jsonTrustLine.get("quality_out");
	}

}
