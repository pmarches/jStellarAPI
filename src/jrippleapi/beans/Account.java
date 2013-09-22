package jrippleapi.beans;

import org.json.simple.JSONObject;

public class Account {
	public static final String RIPPLE_ROOT_ACCOUNT="rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh";
	public static final String RIPPLE_ADDRESS_ZERO="rrrrrrrrrrrrrrrrrrrrrhoLvTp";
	public static final String RIPPLE_ADDRESS_ONE="rrrrrrrrrrrrrrrrrrrrBZbvji";
	public static final String RIPPLE_ADDRESS_NEUTRAL=RIPPLE_ADDRESS_ONE;
	public static final String RIPPLE_ADDRESS_NAN="rrrrrrrrrrrrrrrrrrrn5RM1rHd";
	
	public static final String RIPPLE_ADDRESS_BITSTAMP = "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B";
	public static final String RIPPLE_ADDRESS_JRIPPLEAPI="r32fLio1qkmYqFFYkwdnsaVN7cxBwkW4cT";
	public static final String RIPPLE_ADDRESS_PMARCHES="rEQQNvhuLt1KTYmDWmw12mPvmJD4KCtxmS";
	
	public String account;
	public String secret;
	public String username;
	public String password;
	
	public Account(JSONObject jsonObj){
		account=(String)jsonObj.get("Account");
		secret=(String)jsonObj.get("Secret");
		username=(String)jsonObj.get("Username");
		password=(String)jsonObj.get("Password");
	}
}
