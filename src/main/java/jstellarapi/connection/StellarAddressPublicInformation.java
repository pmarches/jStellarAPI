package jstellarapi.connection;

import java.math.BigDecimal;

import org.json.simple.JSONObject;

public class StellarAddressPublicInformation implements JSONSerializable {
	public String account;
	public BigDecimal STRBalance;
	public String urlgravatar;
	public long nextTransactionSequence;
	public String inflationDestination;
	
	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		JSONObject jsonAccountData = (JSONObject) jsonCommandResult.get("account_data");
		if(jsonAccountData!=null){
			STRBalance=new BigDecimal((String) jsonAccountData.get("Balance"));
			account=(String) jsonAccountData.get("Account");
			urlgravatar=(String) jsonAccountData.get("urlgravatar");
			nextTransactionSequence = (long) jsonAccountData.get("Sequence");
			inflationDestination=(String) jsonAccountData.get("InflationDest");
		}
	}

	@Override
	public String toString() {
		return "StellarAddressPublicInformation [account=" + account + ", STRBalance=" + STRBalance + ", urlgravatar=" + urlgravatar + ", nextTransactionSequence=" + nextTransactionSequence
				+ ", inflationDestination=" + inflationDestination + "]";
	}
}
