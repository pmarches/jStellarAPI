package jrippleapi.beans;

import jrippleapi.connection.JSONSerializable;

import org.json.simple.JSONObject;

public class RandomString implements JSONSerializable {
	public String random;
	
	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		random = (String) jsonCommandResult.get("random");
	}
}
