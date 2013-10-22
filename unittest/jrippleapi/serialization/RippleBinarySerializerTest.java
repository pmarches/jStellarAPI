package jrippleapi.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.xml.bind.DatatypeConverter;

import jrippleapi.beans.DenominatedIssuedCurrency;
import jrippleapi.beans.RippleAddress;
import jrippleapi.connection.RipplePaymentTransaction;
import jrippleapi.serialization.RippleBinarySchema.BinaryFormatField;
import jrippleapi.serialization.RippleBinarySchema.TransactionTypes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Ignore;
import org.junit.Test;

public class RippleBinarySerializerTest {

	@Test
	public void testReadPayment() throws IOException {
		MappedByteBuffer payment1ByteBuffer = fileToBuffer("testdata/r32fLio1qkmYqFFYkwdnsaVN7cxBwkW4cT-to-rEQQNvhuLt1KTYmDWmw12mPvmJD4KCtxmS-amt-1000000XRP.bin");
		
		RippleBinarySerializer binSer = new RippleBinarySerializer();
		RippleSerializedObject serObj = binSer.readSerializedObject(payment1ByteBuffer);
		assertEquals(TransactionTypes.PAYMENT, serObj.getTransactionType());
		assertEquals("r32fLio1qkmYqFFYkwdnsaVN7cxBwkW4cT", serObj.getField(BinaryFormatField.Account).toString());
		assertEquals("rEQQNvhuLt1KTYmDWmw12mPvmJD4KCtxmS", serObj.getField(BinaryFormatField.Destination).toString());
		assertEquals("1 XRP", serObj.getField(BinaryFormatField.Amount).toString());
		ByteBuffer readBuffer = binSer.writeSerializedObject(serObj);
		assertEquals(payment1ByteBuffer, readBuffer);
	}

	@Test
	public void testTrustSet() throws IOException {
		MappedByteBuffer trustet1ByteBuffer = fileToBuffer("testdata/trustset1.bin");
		
		RippleBinarySerializer binSer = new RippleBinarySerializer();
		RippleSerializedObject serObj = binSer.readSerializedObject(trustet1ByteBuffer);
		assertEquals(TransactionTypes.TRUST_SET, serObj.getTransactionType());
		assertEquals(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI, serObj.getField(BinaryFormatField.Account));

		final DenominatedIssuedCurrency EXPECTED_FEE = new DenominatedIssuedCurrency(BigDecimal.TEN);
		assertEquals(EXPECTED_FEE, serObj.getField(BinaryFormatField.Fee));

		final DenominatedIssuedCurrency EXPECTED_TRUST_AMOUNT = new DenominatedIssuedCurrency(BigDecimal.valueOf(1), RippleAddress.RIPPLE_ADDRESS_PMARCHES, "BTC");
		assertEquals(EXPECTED_TRUST_AMOUNT, serObj.getField(BinaryFormatField.LimitAmount));

		assertNotNull(serObj.getField(BinaryFormatField.SigningPubKey));
		assertNotNull(serObj.getField(BinaryFormatField.TxnSignature));
	}

	private MappedByteBuffer fileToBuffer(String filename) throws IOException {
		FileSystem fs = FileSystems.getDefault();
		Path trustSet1Path = fs.getPath(filename);
		FileChannel trust1FC = FileChannel.open(trustSet1Path, StandardOpenOption.READ);
		MappedByteBuffer trust1ByteBuffer = trust1FC.map(MapMode.READ_ONLY, 0, trust1FC.size());
		trust1FC.close();
		return trust1ByteBuffer;
	}

	@Test
	public void testManyFiles() throws IOException{
		RippleBinarySerializer binSer = new RippleBinarySerializer();
		LineNumberReader reader = new LineNumberReader(new FileReader("testdata/12kRawTxn.hex"));
		while(true)
		{
			String line = reader.readLine();
			if(line==null){
				break;
			}
			byte[] txBytes = DatatypeConverter.parseHexBinary(line);
			ByteBuffer buffer = ByteBuffer.wrap(txBytes);
			RippleSerializedObject serObj = binSer.readSerializedObject(buffer);
			ByteBuffer readBuffer = binSer.writeSerializedObject(serObj);
			assertEquals(readBuffer, buffer);
		}
		reader.close();
	}
	
	@Test
	public void testSerializedTx() throws Exception {
		RippleBinarySerializer binSer = new RippleBinarySerializer();
		JSONArray allTx = (JSONArray) new JSONParser().parse(new FileReader("testdata/unittest-tx.json"));
		for(Object obj : allTx){
			JSONObject tx = (JSONObject) obj;
			String hexTx = (String) tx.get("tx");
			byte[] txBytes = DatatypeConverter.parseHexBinary(hexTx);
			ByteBuffer buffer = ByteBuffer.wrap(txBytes);
			RippleSerializedObject txRead = binSer.readSerializedObject(buffer);
			assertEquals(tx.get("payee"), txRead.getField(BinaryFormatField.Destination).toString());
			assertEquals(tx.get("payer"), txRead.getField(BinaryFormatField.Account).toString());
			assertEquals(tx.get("amount"), txRead.getField(BinaryFormatField.Amount).toString());
//			assertEquals(tx.get("fee"), txRead.getField(BinaryFormatField.Fee).toString());
			ByteBuffer writtenBytes = binSer.writeSerializedObject(txRead);
			assertEquals(buffer, writtenBytes);
		}
	}
	
	@Test
	@Ignore
	public void testWriteAndReadPaymentTransaction(){
		RippleBinarySerializer binSer = new RippleBinarySerializer();
		DenominatedIssuedCurrency amount = new DenominatedIssuedCurrency(BigDecimal.valueOf(.1));
		RipplePaymentTransaction payment = new RipplePaymentTransaction(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI, RippleAddress.RIPPLE_ADDRESS_PMARCHES, amount);
		ByteBuffer byteBuffer = binSer.writeSerializedObject(new RippleSerializedObject(payment));
		RippleSerializedObject serObjRead = binSer.readSerializedObject(byteBuffer);
		assertEquals(payment, new RipplePaymentTransaction(serObjRead));
		ByteBuffer writtenObj = binSer.writeSerializedObject(serObjRead);
		assertEquals(byteBuffer, writtenObj);
	}
}
