package jrippleapi.beans;

import jrippleapi.JSONSerializable;

import org.json.simple.JSONObject;

public class AccountInformation implements JSONSerializable {
	public String account;
	public String balance;
	public String urlgravatar;

	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		JSONObject jsonAccountData = (JSONObject) jsonCommandResult.get("account_data");
		balance=(String) jsonAccountData.get("Balance");
		account=(String) jsonAccountData.get("Account");
		urlgravatar=(String) jsonAccountData.get("urlgravatar");
	}
}
