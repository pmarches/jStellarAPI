package jrippleapi;


import java.io.FileReader;

import jrippleapi.core.RippleSeedAddress;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TestUtilities {
	static RippleSeedAddress rippleAccount;
	
	public static RippleSeedAddress getTestSeed() throws Exception {
		if(rippleAccount==null){
			JSONObject jsonWallet = (JSONObject) new JSONParser().parse(new FileReader("jrippleapi-wallet.json"));
			String seedStr = (String)jsonWallet.get("master_seed");
			rippleAccount = new RippleSeedAddress(seedStr);
		}
		return rippleAccount;
	}

}
