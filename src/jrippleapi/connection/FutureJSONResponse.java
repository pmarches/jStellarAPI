package jrippleapi.connection;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONObject;

public class FutureJSONResponse<T extends JSONSerializable> implements Future<T> {
	public final int responseCounter;

	JSONResponseHolder responseHolder;
	JSONObject response;
	T unserializedObj;
	
	public FutureJSONResponse(int requestCounter, JSONResponseHolder responseHolder, T unserializedObj) {
		this.responseCounter = requestCounter;
		this.responseHolder =responseHolder;
		this.unserializedObj = unserializedObj;
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return responseHolder.cancel(this);
	}

	synchronized public void set(JSONObject response){
		this.response=response;
		notifyAll();
	}

	public JSONObject getResponse() throws InterruptedException {
		synchronized (this) {
			if(response==null){
				wait();
			}
			return response;
		}
	}
	
	@Override
	public T get() throws InterruptedException, ExecutionException {
		JSONObject response = getResponse();
		return deSerialize(response);
	}

	protected T deSerialize(JSONObject response) {
		JSONObject result = (JSONObject) response.get("result");
		if(result==null || "success".equals(response.get("status"))==false){
			unserializedObj=null;
		}
		else{
			unserializedObj.copyFrom(result);
		}
		return unserializedObj;
	}

	public JSONObject getResponse(long timeout, TimeUnit unit) throws InterruptedException {
		synchronized (this) {
			if(response==null){
				unit.timedWait(this, timeout);
			}
			return response;
		}
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		JSONObject response = getResponse(timeout, unit);
		return deSerialize(response);
	}

	@Override
	public boolean isCancelled() {
		return responseHolder.isCancelled(this);
	}

	@Override
	public boolean isDone() {
		return response!=null;
	}

}
