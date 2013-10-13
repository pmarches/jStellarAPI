package jrippleapi.serialization;

import java.util.HashMap;
import java.util.Map.Entry;

import jrippleapi.serialization.RippleBinarySchema.BinaryFormatField;
import jrippleapi.serialization.RippleBinarySchema.PrimitiveTypes;
import jrippleapi.serialization.RippleBinarySchema.TransactionTypes;

import org.json.simple.JSONObject;

public class RippleSerializedObject {
	HashMap<BinaryFormatField, Object> fields = new HashMap<BinaryFormatField, Object>();

	public Object getField(BinaryFormatField transactiontype) {
		Object obj = fields.get(transactiontype);
		if(obj==null){
			return null; //TODO refactor with Maybe object?
		}
		return obj;
	}

	public TransactionTypes getTransactionType() {
		Object txTypeObj = getField(BinaryFormatField.TransactionType);
		if(txTypeObj==null){
			throw new NullPointerException("No transaction type field found");
		}
		return TransactionTypes.fromType((short) txTypeObj);
	}

	public String toJSONString() {
		JSONObject root = new JSONObject();
		for(Entry<BinaryFormatField, Object> field:fields.entrySet()){
			PrimitiveTypes primitive = field.getKey().primitive;
			if(primitive==PrimitiveTypes.UINT8 || primitive==PrimitiveTypes.UINT16
					|| primitive==PrimitiveTypes.UINT32 || primitive==PrimitiveTypes.UINT64){
				root.put(field.getKey().toString(), field.getValue());
			}
			else{
				root.put(field.getKey().toString(), field.getValue().toString());
			}
		}
		return root.toJSONString();
	}

}
