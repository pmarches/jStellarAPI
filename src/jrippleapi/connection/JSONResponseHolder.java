package jrippleapi.connection;

import java.util.Hashtable;

import org.json.simple.JSONObject;

public class JSONResponseHolder {
	Hashtable<Integer, FutureJSONResponse> waitingResponses = new Hashtable<Integer, FutureJSONResponse>();

	public boolean remove(FutureJSONResponse futureJSONResponse) {
		if(waitingResponses.remove(futureJSONResponse.responseCounter)==null){
			return false; //Future has already been processed
		}
		return true;
	}

	public boolean isCancelled(FutureJSONResponse futureJSONResponse) {
		return waitingResponses.containsKey(futureJSONResponse.responseCounter);
	}
	
	public void addPendingResponse(FutureJSONResponse pendingResponse){
		waitingResponses.put(pendingResponse.responseCounter, pendingResponse);
	}

	public void setResponseContent(JSONObject jsonMessage) {
		int id  = ((Long) jsonMessage.get("id")).intValue();
		FutureJSONResponse response=waitingResponses.remove(id);
		if(response==null){
			System.err.println("did not find response "+jsonMessage.get("id"));
			response.cancel(true);
		}
		else{
			response.set(jsonMessage);
		}
	}

	public void setResponseError(JSONObject jsonMessage) {
		Object messageId = jsonMessage.get("id");
		if(messageId==null){
			return;
		}
		int id  = ((Long) messageId).intValue();
		FutureJSONResponse response=waitingResponses.get(id);
		if(response==null){
			System.err.println("did not find response "+jsonMessage.get("id"));
		}
		else{
			response.cancel(true);
			response.set(jsonMessage);
		}
	}
}
