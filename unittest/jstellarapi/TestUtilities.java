package jstellarapi;


import java.io.FileReader;

import jstellarapi.core.StellarSeedAddress;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TestUtilities {
	static StellarSeedAddress stellarAccount;
	
	public static StellarSeedAddress getTestSeed() throws Exception {
		if(stellarAccount==null){
			JSONObject jsonWallet = (JSONObject) new JSONParser().parse(new FileReader("jStellarapi-wallet.json"));
			String seedStr = (String)jsonWallet.get("master_seed");
			stellarAccount = new StellarSeedAddress(seedStr);
		}
		return stellarAccount;
	}

}
