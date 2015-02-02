package jstellarapi.ds.tx;

import java.io.IOException;
import java.math.BigDecimal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class AmountAdapter extends TypeAdapter<Amount> {

	@Override
	public void write(JsonWriter out, Amount amount) throws IOException {
		if(amount==null||amount.getCurrency()==null){
			out.nullValue();
		}else if("STR".equals(amount.getCurrency())||amount.getCurrency()==null||amount.getIssuer()==null){
			out.value(amount.getValue().toPlainString());
		}else{
			out.beginObject();
			out.name("currency");
			out.value(amount.getCurrency());
			out.name("issuer");
			out.value(amount.getIssuer());
			out.name("value");
			out.value(amount.getValue().toPlainString());
			out.endObject();
		}
	}

	@Override
	public Amount read(JsonReader reader) throws IOException {
		if (reader.peek() == JsonToken.NULL) {
			reader.nextNull();
			return null;
		} else if (reader.peek() == JsonToken.STRING) {
			String amt = reader.nextString();
			Amount amount = new Amount().withCurrency("STR").withValue(new BigDecimal(amt));
			return amount;
		}else if (reader.peek() == JsonToken.BEGIN_OBJECT) {
			reader.beginObject();
			Amount amount = new Amount();
			while(reader.hasNext()){
				String name = reader.nextName();
				String val = reader.nextString();
				switch(name){
				case "currency":
					amount.setCurrency(val);
					break;
				case "issuer":
					amount.setIssuer(val);
					break;
				case "value":
					amount.setValue(new BigDecimal(val));
					break;
				}
			}
			reader.endObject();
			return amount;
		}
		throw new IOException();
	}

}
