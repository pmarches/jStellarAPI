package jrippleapi.connection;

import org.json.simple.JSONObject;

public class GenericJSONSerializable implements JSONSerializable {
	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		System.out.println("GenericJSONSerializable "+jsonCommandResult);
	}
}
