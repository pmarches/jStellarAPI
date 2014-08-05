package jstellarapi.serialization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import jstellarapi.keys.StellarDeterministicKeyGenerator;
import jstellarapi.serialization.StellarBinarySchema.BinaryFormatField;
import jstellarapi.serialization.StellarBinarySchema.PrimitiveTypes;
import jstellarapi.serialization.StellarBinarySchema.TransactionTypes;

import org.json.simple.JSONObject;

public class StellarBinaryObject {
	HashMap<BinaryFormatField, Object> fields = new HashMap<BinaryFormatField, Object>();
	static StellarBinarySerializer binSer=new StellarBinarySerializer();

	public StellarBinaryObject(){
	}
		
	public StellarBinaryObject(StellarBinaryObject serObjToSign) {
		this.fields.putAll(serObjToSign.fields);
	}
	
	public StellarBinaryObject getUnsignedCopy(){
		StellarBinaryObject copy = new StellarBinaryObject(this);
		copy.removeField(BinaryFormatField.TxnSignature);
		return copy;
	}

	public byte[] generateHashFromBinaryObject() {
		byte[] bytesToSign = binSer.writeBinaryObject(this).array();

		//Prefix bytesToSign with the magic hashing prefix (32bit) 'STX\0'
		byte[] prefixedBytesToHash = new byte[bytesToSign.length+4];
		prefixedBytesToHash[0]=(byte) 'S';
		prefixedBytesToHash[1]=(byte) 'T';
		prefixedBytesToHash[2]=(byte) 'X';
		prefixedBytesToHash[3]=(byte) 0;
		System.arraycopy(bytesToSign, 0, prefixedBytesToHash, 4, bytesToSign.length);
		//Hash256
		byte[] hashOfBytes = StellarDeterministicKeyGenerator.halfSHA512(prefixedBytesToHash);
		return hashOfBytes;
	}

	public byte[] getTransactionHash(){
		//Convert to bytes again
		byte[] signedBytes = binSer.writeBinaryObject(this).array();
		//Prefix bytesToSign with the magic sigining prefix (32bit) 'TXN\0'
		byte[] prefixedSignedBytes = new byte[signedBytes.length+4];
		prefixedSignedBytes[0]=(byte) 'T';
		prefixedSignedBytes[1]=(byte) 'X';
		prefixedSignedBytes[2]=(byte) 'N';
		prefixedSignedBytes[3]=(byte) 0;
		System.arraycopy(signedBytes, 0, prefixedSignedBytes, 4, signedBytes.length);

		//Hash again, this wields the TransactionID
		byte[] hashOfTransaction = StellarDeterministicKeyGenerator.halfSHA512(prefixedSignedBytes);
		return hashOfTransaction;
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

	@Override
	public String toString() {
		return "StellarBinaryObject [fields=" + fields + "]";
	}

}
