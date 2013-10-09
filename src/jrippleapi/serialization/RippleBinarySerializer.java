package jrippleapi.serialization;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import javax.xml.bind.DatatypeConverter;

import jrippleapi.beans.RippleAddress;
import jrippleapi.serialization.RippleBinarySchema.BinaryFormatField;
import jrippleapi.serialization.RippleBinarySchema.PrimitiveTypes;

public class RippleBinarySerializer {
	ByteBuffer input;

	//THIS IS NOT THREAD SAFE!
	public RippleSerializedObject readSerializedObject(ByteBuffer inputBytes) {
		this.input = inputBytes;
		RippleSerializedObject serializedObject = new RippleSerializedObject();
		while(input.hasRemaining()){
			byte firstByte = input.get();
			int type=(0xF0 & firstByte)>>4;
			if(type==0){
				type = input.get();
			}
			int field=0x0F & firstByte;
			if(field==0){
				field = input.get();
			}

			BinaryFormatField serializedField = BinaryFormatField.lookup(type, field);
			Object value = readPrimitive(serializedField.primitive);
			serializedObject.fields.put(serializedField, value );
		}
		return serializedObject;
	}

	private Object readPrimitive(PrimitiveTypes primitive) {
		if(primitive==PrimitiveTypes.UINT16){
			return input.getShort();//FIXME SIGNED
		}
		else if(primitive==PrimitiveTypes.UINT32){
			return input.getInt();//FIXME SIGNED
		}
		else if(primitive==PrimitiveTypes.UINT64){
			return input.getLong(); //FIXME SIGNED
		}
		else if(primitive==PrimitiveTypes.HASH128){
			byte[] sixteenBytes = new byte[16];
			input.get(sixteenBytes);
			return DatatypeConverter.printHexBinary(sixteenBytes);
		}
		else if(primitive==PrimitiveTypes.HASH256){
			byte[] thirtyTwoBytes = new byte[32];
			input.get(thirtyTwoBytes);
			return DatatypeConverter.printHexBinary(thirtyTwoBytes);
		}
		else if(primitive==PrimitiveTypes.AMOUNT){
			return readAmount();
		}
		else if(primitive==PrimitiveTypes.VARIABLE_LENGTH){
			byte[] dataBytes = readVariableLength();
			return DatatypeConverter.printHexBinary(dataBytes);
		}
		else if(primitive==PrimitiveTypes.ACCOUNT){
			byte[] accountBytes = readVariableLength();
			return new RippleAddress(accountBytes);
		}
		else if(primitive==PrimitiveTypes.OBJECT){
			throw new RuntimeException("Object type, not yet supported");
		}
		else if(primitive==PrimitiveTypes.ARRAY){
			throw new RuntimeException("Array type, not yet supported");
		}
		else if(primitive==PrimitiveTypes.UINT8){
			return input.get();
		}
		else if(primitive==PrimitiveTypes.HASH160){
			byte[] twentyBytes = new byte[20];
			input.get(twentyBytes);
			return DatatypeConverter.printHexBinary(twentyBytes);
		}
		else if(primitive==PrimitiveTypes.PATHSET){
			throw new RuntimeException("Path set");
		}
		else if(primitive==PrimitiveTypes.VECTOR256){
			throw new RuntimeException("Vector");
		}
		throw new RuntimeException("Unsupported primitive "+primitive);
	}

	//FIXME return amount object
	private Object readAmount() {
		long offsetNativeSignMagnitudeBytes = input.getLong();
		boolean isXRPAmount =(0x8000000000000000l & offsetNativeSignMagnitudeBytes)==0; 
		int sign =(0x4000000000000000l & offsetNativeSignMagnitudeBytes)==0?-1:1;
		//8 bits of offset
		//The remaining 54 bits are magnitude
		long MAGNITUDE_MASK = 0x3FFFFFFFFFFFFFl;
		long longMagnitude = (offsetNativeSignMagnitudeBytes & MAGNITUDE_MASK);
		BigInteger magnitude = BigInteger.valueOf(sign*longMagnitude);
		if(isXRPAmount){
			return magnitude;
		}
		else{
			byte[] unknown = new byte[12];
			input.get(unknown);
			byte[] currency = new byte[8];
			input.get(currency);
			//TODO See https://ripple.com/wiki/Currency_Format for format
			
			byte[] issuer = new byte[20];
			input.get(issuer);
			//TODO If issuer is all 0, this means any issuer
			return magnitude;
		}
	}

	private byte[] readVariableLength() {
		int byteLen=0;
		int firstByte = input.get();
		int secondByte=0;
		if(firstByte<192){
			byteLen=firstByte;
		}
		else if(firstByte<240){
			secondByte = input.get();
			byteLen=193+(firstByte-193)*256 + secondByte;
		}
		else if(firstByte<254){
			int thirdByte = input.get();
			byteLen=12481 + (firstByte-241)*65536 + secondByte*256 + thirdByte;
		}
		else {
			throw new RuntimeException("firstByte="+firstByte+", value reserved");
		}

		byte[] variableBytes = new byte[byteLen];
		input.get(variableBytes);
		return variableBytes;
	}

}
