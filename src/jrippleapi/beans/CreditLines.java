package jrippleapi.beans;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jrippleapi.connection.JSONSerializable;

public class CreditLines extends ArrayList<CreditLine> implements JSONSerializable {

	@Override
	public void copyFrom(JSONObject jsonCommandResult) {
		JSONArray jsonLinesOfCredit = (JSONArray) jsonCommandResult.get("lines");
		for(int i=0; i<jsonLinesOfCredit.size(); i++){
			CreditLine lineOfCredit = new CreditLine();
			lineOfCredit.copyFrom((JSONObject) jsonLinesOfCredit.get(i));
			add(lineOfCredit);
		}
	}

}
