package jstellarapi.connection;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TrustLines extends ArrayList<TrustLine> implements JSONSerializable {

	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		JSONArray jsonLinesOfTrust = (JSONArray) jsonCommandResult.get("lines");
		for(int i=0; i<jsonLinesOfTrust.size(); i++){
			TrustLine lineOfCredit = new TrustLine();
			lineOfCredit.copyFrom((JSONObject) jsonLinesOfTrust.get(i));
			add(lineOfCredit);
		}
	}

}
