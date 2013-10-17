package jrippleapi.beans;

import java.math.BigInteger;

import jrippleapi.connection.JSONSerializable;

import org.json.simple.JSONObject;

public class AccountInformation implements JSONSerializable {
	public String account;
	public BigInteger xrpBalance;
	public String urlgravatar;

	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		JSONObject jsonAccountData = (JSONObject) jsonCommandResult.get("account_data");
		xrpBalance=CurrencyUnit.XRP.fromString((String) jsonAccountData.get("Balance"));
		account=(String) jsonAccountData.get("Account");
		urlgravatar=(String) jsonAccountData.get("urlgravatar");
	}
}
