package jrippleapi.connection;

import org.json.simple.JSONObject;

public class GenericJSONSerializable implements JSONSerializable {
	public JSONObject jsonCommandResult;
	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		System.out.println("GenericJSONSerializable "+jsonCommandResult);
		if("error".equals(jsonCommandResult.get("status"))){
			
		}
		this.jsonCommandResult=jsonCommandResult;
	}
}
