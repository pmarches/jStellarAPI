package jstellarapi.connection;

import org.json.simple.JSONObject;

public interface JSONSerializable {
	public void copyFrom(JSONObject jsonCommandResult);
}
