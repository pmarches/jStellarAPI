package jrippleapi.connection;

import org.json.simple.JSONObject;

public class GenericJSONSerializable implements JSONSerializable {
	public JSONObject jsonCommandResult;
	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		if("error".equals(jsonCommandResult.get("status"))){
			System.out.println("GenericJSONSerializable "+jsonCommandResult);
		}
		this.jsonCommandResult=jsonCommandResult;
	}
}
