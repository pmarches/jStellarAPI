package jrippleapi.serialization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import jrippleapi.beans.RipplePaymentTransaction;
import jrippleapi.serialization.RippleBinarySchema.BinaryFormatField;
import jrippleapi.serialization.RippleBinarySchema.PrimitiveTypes;
import jrippleapi.serialization.RippleBinarySchema.TransactionTypes;

import org.json.simple.JSONObject;

public class RippleSerializedObject {
	HashMap<BinaryFormatField, Object> fields = new HashMap<BinaryFormatField, Object>();

	public RippleSerializedObject(){
	}
	
	public RippleSerializedObject(RipplePaymentTransaction payment){
		fields.put(BinaryFormatField.TransactionType, (int) TransactionTypes.PAYMENT.byteValue);
		fields.put(BinaryFormatField.Account, payment.payer);
		fields.put(BinaryFormatField.Destination, payment.payee);
		fields.put(BinaryFormatField.Amount, payment.amount);
	}
	
	public Object getField(BinaryFormatField transactiontype) {
		Object obj = fields.get(transactiontype);
		if(obj==null){
			return null; //TODO refactor with Maybe object?
		}
		return obj;
	}
	
	public void putField(BinaryFormatField field, Object value){
		fields.put(field, value);
	}

	public TransactionTypes getTransactionType() {
		Object txTypeObj = getField(BinaryFormatField.TransactionType);
		if(txTypeObj==null){
			throw new NullPointerException("No transaction type field found");
		}
		return TransactionTypes.fromType((int) txTypeObj);
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

	public List<BinaryFormatField> getSortedField() {
		ArrayList<BinaryFormatField> sortedFields = new ArrayList<BinaryFormatField>(fields.keySet());
		Collections.sort(sortedFields);
		return sortedFields;
	}

	public Object removeField(BinaryFormatField fieldToBeRemoved) {
		return fields.remove(fieldToBeRemoved);
	}

}
