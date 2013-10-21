package jrippleapi.serialization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.List;

import jrippleapi.beans.CurrencyUnit;
import jrippleapi.beans.DenominatedIssuedCurrency;
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
				firstByte=(byte)field;
			}

			BinaryFormatField serializedField = BinaryFormatField.lookup(type, field);
			Object value = readPrimitive(serializedField.primitive);
			serializedObject.fields.put(serializedField, value );
		}
		return serializedObject;
	}

	private Object readPrimitive(PrimitiveTypes primitive) {
		if(primitive==PrimitiveTypes.UINT16){
			return input.getShort();//FIXME SIGNED Always return BigInt? Long? Different types? Custom Unsigned Int of various length?
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
			return sixteenBytes;
		}
		else if(primitive==PrimitiveTypes.HASH256){
			byte[] thirtyTwoBytes = new byte[32];
			input.get(thirtyTwoBytes);
			return thirtyTwoBytes;
		}
		else if(primitive==PrimitiveTypes.AMOUNT){
			return readAmount();
		}
		else if(primitive==PrimitiveTypes.VARIABLE_LENGTH){
			return readVariableLength();
		}
		else if(primitive==PrimitiveTypes.ACCOUNT){
			return readAccount();
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
			return readIssuer();
		}
		else if(primitive==PrimitiveTypes.PATHSET){
			return readPathSet();
		}
		else if(primitive==PrimitiveTypes.VECTOR256){
			throw new RuntimeException("Vector");
		}
		throw new RuntimeException("Unsupported primitive "+primitive);
	}

	private RippleAddress readAccount() {
		byte[] accountBytes = readVariableLength();
		return new RippleAddress(accountBytes);
	}
	//See https://ripple.com/wiki/Currency_Format
	private DenominatedIssuedCurrency readAmount() {
		long offsetNativeSignMagnitudeBytes = input.getLong();
		//1 bit for Native
		boolean isXRPAmount =(0x8000000000000000l & offsetNativeSignMagnitudeBytes)==0; 
		//1 bit for sign
		int sign = (0x4000000000000000l & offsetNativeSignMagnitudeBytes)==0?-1:1;
		//8 bits of offset
		int offset = (int) ((offsetNativeSignMagnitudeBytes & 0x3FC0000000000000l)>>>54);
		//The remaining 54 bits are magnitude
		long longMagnitude = offsetNativeSignMagnitudeBytes&0x3FFFFFFFFFFFFFl;
		if(isXRPAmount){
			BigDecimal magnitude = BigDecimal.valueOf(sign*longMagnitude);
			return new DenominatedIssuedCurrency(magnitude);
		}
		else{
			BigInteger biMagnitude = BigInteger.valueOf(longMagnitude);

			String currencyStr = readCurrency();
			CurrencyUnit currency = CurrencyUnit.parse(currencyStr);
			RippleAddress issuer = readIssuer();
			int decimalPosition = 97-offset; //FIXME This will change when we change to a BigInteger to store moneys
			
			BigDecimal fractionalValue=new BigDecimal(biMagnitude, decimalPosition).stripTrailingZeros();
			return new DenominatedIssuedCurrency(fractionalValue, issuer, currency);
		}
	}

	private RippleAddress readIssuer() {
		byte[] issuerBytes = new byte[20];
		input.get(issuerBytes);
		//TODO If issuer is all 0, this means any issuer
		return new RippleAddress(issuerBytes);
	}

	private String readCurrency() {
		byte[] unknown = new byte[12];
		input.get(unknown);
		byte[] currency = new byte[8];
		input.get(currency);
		return new String(currency, 0, 3);
		//TODO See https://ripple.com/wiki/Currency_Format for format
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
			secondByte = input.get();
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

	private String readPathSet() {
		while(true){
			byte pathElementType = input.get();
			if(pathElementType==(byte)0x00){ //End of Path set
				break;
			}
			else if(pathElementType==(byte)0xFF){ //End of Path
			}
			else{
				if((pathElementType&0x01)!=0){ //Account bit is set
					readIssuer();
				}
				if((pathElementType&0x10)!=0){ //Currency bit is set
					readCurrency();
				}
				if((pathElementType&0x20)!=0){ //Issuer bit is set
					readIssuer();
				}
			}
		}
		
		return "PathSet";
	}

	public ByteBuffer writeSerializedObject(RippleSerializedObject serializedObj) {
		ByteBuffer output = ByteBuffer.allocate(2000); //FIXME Hum..
		List<BinaryFormatField> sortedFields = serializedObj.getSortedField();
		for(BinaryFormatField field: sortedFields){
			byte typeHalfByte=0;
			if(field.primitive.typeCode<=15){
				typeHalfByte = (byte) (field.primitive.typeCode<<4);
			}
			byte fieldHalfByte = 0;
			if(field.fieldId<=15){
				fieldHalfByte = (byte) (field.fieldId&0x0F);
			}
			output.put((byte) (typeHalfByte|fieldHalfByte));
			if(typeHalfByte==0){
				output.put((byte) field.primitive.typeCode);
			}
			if(fieldHalfByte==0){
				output.put((byte) field.fieldId);
			}
			
			writePrimitive(output, field.primitive, serializedObj.getField(field));
		}
		output.flip();
		ByteBuffer compactBuffer = ByteBuffer.allocate(output.limit());
		compactBuffer.put(output);
		return compactBuffer;
	}

	private void writePrimitive(ByteBuffer output, PrimitiveTypes primitive, Object value) {
		if(primitive==PrimitiveTypes.UINT16){
			output.putShort((short) value);//FIXME SIGNED Always return BigInt? Long? Different types? Custom Unsigned Int of various length?
		}
		else if(primitive==PrimitiveTypes.UINT32){
			output.putInt((int) value);//FIXME SIGNED
		}
		else if(primitive==PrimitiveTypes.UINT64){
			output.putLong((long) value); //FIXME SIGNED
		}
		else if(primitive==PrimitiveTypes.HASH128){
			byte[] sixteenBytes = (byte[]) value;
			if(sixteenBytes.length!=16){
				throw new RuntimeException("value "+value+" is not a HASH128");
			}
			output.put(sixteenBytes);
		}
		else if(primitive==PrimitiveTypes.HASH256){
			byte[] thirtyTwoBytes = (byte[]) value;
			if(thirtyTwoBytes.length!=32){
				throw new RuntimeException("value "+value+" is not a HASH256");
			}
			output.put(thirtyTwoBytes);
		}
		else if(primitive==PrimitiveTypes.AMOUNT){
			writeAmount(output, (DenominatedIssuedCurrency) value);
		}
		else if(primitive==PrimitiveTypes.VARIABLE_LENGTH){
			writeVariableLength(output, (byte[]) value);
		}
		else if(primitive==PrimitiveTypes.ACCOUNT){
			writeAccount(output, (RippleAddress) value);
		}
		else if(primitive==PrimitiveTypes.OBJECT){
			throw new RuntimeException("Object type, not yet supported");
		}
		else if(primitive==PrimitiveTypes.ARRAY){
			throw new RuntimeException("Array type, not yet supported");
		}
		else if(primitive==PrimitiveTypes.UINT8){
			output.put((byte) value); //FIXME Signed
		}
		else if(primitive==PrimitiveTypes.HASH160){
			writeIssuer(output, (RippleAddress) value);
		}
		else if(primitive==PrimitiveTypes.PATHSET){
			writePathSet(output, value);
		}
		else if(primitive==PrimitiveTypes.VECTOR256){
			throw new RuntimeException("Vector");
		}
		else{
			throw new RuntimeException("Unsupported primitive "+primitive);
		}
	}

	private void writePathSet(ByteBuffer output, Object value) {
		throw new RuntimeException("Not implemented, implement PathSet model first");
	}

	private void writeIssuer(ByteBuffer output, RippleAddress value) {
		byte[] issuerBytes = value.getBytes();
		output.put(issuerBytes);
	}

	private void writeAccount(ByteBuffer output, RippleAddress address) {
		writeVariableLength(output, address.getBytes());
	}

	//TODO Unit test this function
	private void writeVariableLength(ByteBuffer output, byte[] value) {
		if(value.length<192){
			output.put((byte) value.length);
		}
		else if(value.length<12480){ //193 + (b1-193)*256 + b2
			//FIXME This is not right...
			int firstByte=(value.length/256)+193;
			output.put((byte) firstByte);
			//FIXME What about arrays of length 193?
			int secondByte=value.length-firstByte-193;
			output.put((byte) secondByte);
		}
		else if(value.length<918744){ //12481 + (b1-241)*65536 + b2*256 + b3
			int firstByte=(value.length/65536)+241;
			output.put((byte) firstByte);
			int secondByte=(value.length-firstByte)/256;
			output.put((byte) secondByte);
			int thirdByte=value.length-firstByte-secondByte-12481;
			output.put((byte) thirdByte);
		}
		output.put(value);
	}

	private void writeAmount(ByteBuffer output, DenominatedIssuedCurrency denominatedCurrency) {
		long offsetNativeSignMagnitudeBytes=0;
		if(denominatedCurrency.amount.signum()>=0){
			offsetNativeSignMagnitudeBytes|= 0x4000000000000000l;
		}
		if(denominatedCurrency.currency.equals(CurrencyUnit.XRP)){
			long drops = denominatedCurrency.amount.longValue(); //XRP does not have fractional portion
			offsetNativeSignMagnitudeBytes|=drops;
			output.putLong(offsetNativeSignMagnitudeBytes);
		}
		else{
			offsetNativeSignMagnitudeBytes|= 0x8000000000000000l;
			BigDecimal rescaledBD = denominatedCurrency.amount.setScale(14);
			int scale = rescaledBD.scale();
			long offset = 97-scale;
			offsetNativeSignMagnitudeBytes|=(offset<<54);
			BigInteger unscaledValue = rescaledBD.unscaledValue();
			offsetNativeSignMagnitudeBytes|=unscaledValue.longValue();
			output.putLong(offsetNativeSignMagnitudeBytes);
			writeCurrency(output, denominatedCurrency.currency);
			writeIssuer(output, denominatedCurrency.issuer);
		}
	}
//	long offsetNativeSignMagnitudeBytes = input.getLong();
//	//1 bit for Native
//	boolean isXRPAmount =(0x8000000000000000l & offsetNativeSignMagnitudeBytes)==0; 
//	//1 bit for sign
//	int sign = (0x4000000000000000l & offsetNativeSignMagnitudeBytes)==0?-1:1;
//	//8 bits of offset
//	int offset = (int) ((offsetNativeSignMagnitudeBytes & 0x3FC0000000000000l)>>>54);
//	//The remaining 54 bits are magnitude
//	long longMagnitude = offsetNativeSignMagnitudeBytes&0x3FFFFFFFFFFFFFl;
//	if(isXRPAmount){
//		BigDecimal magnitude = BigDecimal.valueOf(sign*longMagnitude);
//		return new DenominatedIssuedCurrency(magnitude);
//	}
//	else{
//		String currencyStr = readCurrency();
//		CurrencyUnit currency = CurrencyUnit.parse(currencyStr);
//		RippleAddress issuer = readIssuer();
//		int decimalPosition = 97-offset; //FIXME This will change when we change to a BigInteger to store moneys
//		double fractionalValue= longMagnitude*Math.pow(10, -decimalPosition); //FIXME! Should not need to go thru a double
//		return new DenominatedIssuedCurrency(BigDecimal.valueOf(fractionalValue), issuer, currency);
//	}

	private void writeCurrency(ByteBuffer output, CurrencyUnit currency) {
		byte[] currencyBytes = new byte[20];
		System.arraycopy(currency.currencyCode.getBytes(), 0, currencyBytes, 12, 3);
		output.put(currencyBytes);
		//TODO See https://ripple.com/wiki/Currency_Format for format
	}

}
