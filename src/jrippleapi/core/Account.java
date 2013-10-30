package jrippleapi.core;

import org.json.simple.JSONObject;

public class Account {
	public RippleAddress address;
	public RippleSeedAddress secret;
	
	public Account(JSONObject jsonObj){
		address=new RippleAddress((String)jsonObj.get("account_id"));
		secret=new RippleSeedAddress((String)jsonObj.get("master_seed"));
	}
}
