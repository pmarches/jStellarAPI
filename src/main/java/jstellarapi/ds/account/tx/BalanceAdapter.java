package jstellarapi.ds.account.tx;

import java.io.IOException;
import java.math.BigDecimal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class BalanceAdapter extends TypeAdapter<Balance> {

	@Override
	public void write(JsonWriter out, Balance balance) throws IOException {
		if(balance==null||balance.getCurrency()==null){
			out.nullValue();
		}else if("STR".equals(balance.getCurrency())||balance.getCurrency()==null||balance.getIssuer()==null){
			out.value(balance.getValue().toPlainString());
		}else{
			out.beginObject();
			out.name("currency");
			out.value(balance.getCurrency());
			out.name("issuer");
			out.value(balance.getIssuer());
			out.name("value");
			out.value(balance.getValue().toPlainString());
			out.endObject();
		}
	}

	@Override
	public Balance read(JsonReader reader) throws IOException {
		if (reader.peek() == JsonToken.NULL) {
			reader.nextNull();
			return null;
		} else if (reader.peek() == JsonToken.STRING) {
			String amt = reader.nextString();
			Balance balance = new Balance().withCurrency("STR").withValue(new BigDecimal(amt));
			return balance;
		}else if (reader.peek() == JsonToken.BEGIN_OBJECT) {
			reader.beginObject();
			Balance balance = new Balance();
			while(reader.hasNext()){
				String name = reader.nextName();
				String val = reader.nextString();
				switch(name){
				case "currency":
					balance.setCurrency(val);
					break;
				case "issuer":
					balance.setIssuer(val);
					break;
				case "value":
					balance.setValue(new BigDecimal(val));
					break;
				}
			}
			reader.endObject();
			return balance;
		}
		throw new IOException();
	}

}
