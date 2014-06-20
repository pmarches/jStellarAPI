package jrippleapi.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

import jrippleapi.core.DenominatedIssuedCurrency;
import jrippleapi.core.RippleAddress;
import jrippleapi.core.RipplePaymentTransaction;
import jrippleapi.serialization.RippleBinarySchema.BinaryFormatField;
import jrippleapi.serialization.RippleBinarySchema.TransactionTypes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

public class RippleBinarySerializerTest {

	@Test
	public void testReadPayment() throws IOException {
		MappedByteBuffer payment1ByteBuffer = fileToBuffer("testdata/r32fLio1qkmYqFFYkwdnsaVN7cxBwkW4cT-to-rEQQNvhuLt1KTYmDWmw12mPvmJD4KCtxmS-amt-1000000XRP.bin");
		
		RippleBinarySerializer binSer = new RippleBinarySerializer();
		RippleBinaryObject serObj = binSer.readBinaryObject(payment1ByteBuffer);
		assertEquals(TransactionTypes.PAYMENT, serObj.getTransactionType());
		assertEquals("r32fLio1qkmYqFFYkwdnsaVN7cxBwkW4cT", serObj.getField(BinaryFormatField.Account).toString());
		assertEquals("rEQQNvhuLt1KTYmDWmw12mPvmJD4KCtxmS", serObj.getField(BinaryFormatField.Destination).toString());
		assertEquals("1 XRP", serObj.getField(BinaryFormatField.Amount).toString());
		ByteBuffer readBuffer = binSer.writeBinaryObject(serObj);
		payment1ByteBuffer.rewind();
		assertEquals(payment1ByteBuffer, readBuffer);
	}

	@Test
	public void testTrustSet() throws IOException {
		MappedByteBuffer trustet1ByteBuffer = fileToBuffer("testdata/trustset1.bin");
		
		RippleBinarySerializer binSer = new RippleBinarySerializer();
		RippleBinaryObject serObj = binSer.readBinaryObject(trustet1ByteBuffer);
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
			RippleBinaryObject serObj = binSer.readBinaryObject(buffer);
			ByteBuffer readBuffer = binSer.writeBinaryObject(serObj);
			assertEquals(line, DatatypeConverter.printHexBinary(readBuffer.array()));
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
			RippleBinaryObject txRead = binSer.readBinaryObject(buffer);
			RipplePaymentTransaction payment = new RipplePaymentTransaction(txRead);
			assertEquals(tx.get("payee"), txRead.getField(BinaryFormatField.Destination).toString());
			assertEquals(tx.get("payer"), txRead.getField(BinaryFormatField.Account).toString());
			assertEquals(tx.get("amount"), txRead.getField(BinaryFormatField.Amount).toString());
			assertEquals(tx.get("inLedger").toString(), tx.get("fee"), txRead.getField(BinaryFormatField.Fee).toString());
			ByteBuffer writtenBytes = binSer.writeBinaryObject(txRead);
			assertEquals(hexTx, DatatypeConverter.printHexBinary(writtenBytes.array()));
		}
	}
	
	@Test
	public void testWriteAndReadPaymentTransaction(){
		RippleBinarySerializer binSer = new RippleBinarySerializer();
		DenominatedIssuedCurrency amount = new DenominatedIssuedCurrency(BigDecimal.valueOf(1));
		RipplePaymentTransaction payment = new RipplePaymentTransaction(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI, RippleAddress.RIPPLE_ADDRESS_PMARCHES, amount, 1);
		ByteBuffer byteBuffer = binSer.writeBinaryObject(payment.getBinaryObject());
		RippleBinaryObject serObjRead = binSer.readBinaryObject(byteBuffer);
//		assertEquals(payment, new RipplePaymentTransaction(serObjRead));
		ByteBuffer writtenObj = binSer.writeBinaryObject(serObjRead);
		byteBuffer.rewind();
		assertEquals(byteBuffer, writtenObj);
	}
	
	@Test
	public void testNegativeAmounts(){
        DenominatedIssuedCurrency amount = new DenominatedIssuedCurrency("-99.2643419677474", RippleAddress.RIPPLE_ADDRESS_NEUTRAL, "USD");

        assertEquals(13, amount.amount.scale());
        assertTrue(amount.isNegative());
        assertFalse(amount.isNative());
        assertEquals("-99.2643419677474/USD/rrrrrrrrrrrrrrrrrrrrBZbvji", amount.toString());
        
		ByteBuffer output=ByteBuffer.allocate(48);
		new RippleBinarySerializer().writeAmount(output, amount);
		String hex = DatatypeConverter.printHexBinary(output.array());
        String scale14expectedHex = "94E3440A102F5F5400000000000000000000000055534400000000000000000000000000000000000000000000000001";
        String scale13ExpectedHex="950386CDCE6B232200000000000000000000000055534400000000000000000000000000000000000000000000000001";
        assertEquals(scale13ExpectedHex, hex);
	}
	
	@Test
	public void testMemoTypes(){
		String memoHex="12000322000000002400000E48201B0054625068400000000000000C732102EEAF2C95B668D411FC490746C52071514F6D3A7B742D91D82CB591B5443D1C5974473045022100F3B0747B1D0CD2C1DC25D172E5BD8359FBD9C50C34A405EAD3834AD83311A1E8022056CD3DCA12EA95B5AC9F21B705B32FA7A7E86AB7ED6DE95F7901EDCB7E6BDF3B811466B05AAE728123957EF8411C44B787650C27231DF9EA7C0964616E6E792E6A70677DC39F26F91CA2138F5D4E749C52AED1D15D0709FCC22298B6386FD4AC52C2D323DDD0EE7C2506DE07482B4715333954B9C8BF304EFB2BB721C9E378FC611E43D63980F41B07DEB64EC09717A319BA469867705797DB43FBF3FFB978000D1E600321C51C76C71D9F4F071EB48EA68A13BBCEEA55D059DEAF52355E80BB5ED21D8A2B70AD49E2FF9F66BCF21BD0C651EBC03ABED3E99418B12E2ADD5C42D00E3859D03656DE662ACD0D9456CB4CAFBA208EEBFB2F76D96C138E59C76CF96395C1C0D5A04D68397F81BA9303042F121F7B765F65E1C721CC4F23FC8DC3A2E66A25B6D7CB2649EF556B80FEBCE586A75138A90472698BFE5892A5C7940DCF59B3392E291823CAAF82C451507EF3040739874B4B0DD3B55CD1C12C9FD869E3DD08B835430BF0766FD583FD083372461FD8015776DEEF9879EEF633C03C644F08F2EED7514C917905468ED122D40978BD9FB76156DA52B6A3ECFD47902D572A4E15C7FB2F51708693EC9EBBFDF78BDA1B894884C70691580D2B32E9E17F5E912AA704D95AD046941CF73D96370B9CCA338D55565EA9AF417A1B248FA6C7C531051FD96C38FDBE212EFAEC74F993EFFB73648A4EBB4C16B2389B94C5C8684388243162C96E87909BC2D56654A93D7AFAA91ECCD876BE5DBEC987D01C237E861AFCBAB6B821A7AD3AF22CA2BC60E97164AC0FB5A16E56447C4541BEEED84FFC77F0AF224A1756C07E9ABC6E59C93AC7E450316F13DD525034D2B5C1598247BBD21629A28A157F773ADF0199C2D2AF358225E18C5E3D47E171548A2B0DEE0BCA2BDBE03B78CC506E6FF87EEE1FEFE4A67A614B7E1FF2ECFA9697511EA7F196BAA122A0CC27BC4415FA6959EC915191AC7200C690D1BCB74F27559804278CF449B2D3D1E0C5001C6A46F2873840D37FD1E4E93CC55D507748B5D2EEBB2FAED04BD8826D065C6045C59696F14E6583128F1B258F6599539B72C64C7CFAFF7E173071499A8A3337872BF2B68CF373795435BD98BD45383F71BF778ED193566B90BC6F05C6C3E2B2D2F1308EB121E4670D7E0872706CC76F54666B92FE18F4F1C44AFC339A04F7E99710802DE87948F2EB06F38E6DE93073A354D78D8370E5C8E29106C9B67F1B03591A459A9F278E5950DFAC933E9E2EA78F51D7A48455293C5DC722BE2CA0EB5B34FDA8970CD7161B63997A13F3AAD59B1EED0BA14B7F56DDBB178D06954488BBE8E1F1";
		RippleBinarySerializer binSer = new RippleBinarySerializer();
		RippleBinaryObject memoObj = binSer.readBinaryObject(memoHex);
		assertNotNull(memoObj);
		RippleBinaryObject[] memos = (RippleBinaryObject[]) memoObj.getField(BinaryFormatField.Memos);
		assertEquals(1, memos.length);
		assertEquals("danny.jpg", new String((byte[]) memos[0].getField(BinaryFormatField.MemoType)));
	}
	
	@Test
	public void testTransactionMetaData(){
		String largeMeta="201C00000004F8E51100612500355EFD5570584A9E234F816D9A11908F7AF948DEE68BF2368219AD54E50DA443813F1096561B4C8C97F507598D2670A0AD1D067C0271B744A3BB5CDC1B9BD9280BB92E7F72E6240000006E6240000046028B2DBAE1E72200000000240000006F2D000000036240000046028B2DAE8114101E753720DAD840631AB0007DBACD1607644708E1E1E51100722500351A1B55888575BF83630284C80624DD0B864E4CD7725C5B6458AEEB76AA63A7C3D3A05C56368EF94B49105A424D1995B4855ECE6637ECB559374412AE130D97D96268B453E662800000000000000000000000000000000000000055534400000000000000000000000000000000000000000000000001E1E72200020000370000000000000000380000000000000000629498DE76816D8000000000000000000000000000555344000000000000000000000000000000000000000000000000016680000000000000000000000000000000000000005553440000000000101E753720DAD840631AB0007DBACD160764470867D5438D7EA4C6800000000000000000000000000055534400000000006E5172BBC9FD4741747D82AA414650A412CB1801E1E1E411006F563A5978D04B01A15B3EF3327EFE3BB21AA9335517F6CC3206246F3599DEF00F2AE7220000000024000005832500355E9933000000000000000034000000000000000255E28FE51B2D90589AD6C86662170024DF217FF641DE43E3AA5C7B7466B531AF785010CF8D13399C6ED20BA82740CFA78E928DC8D498255249BA634D04644ACD92309E64D4838D7EA4C680000000000000000000000000005553440000000000DD39C650A96EDA48334E70CC4A85B8B2E8502CD3654000000004D25B9581148F886D2B1C5BA7AD18C60010666517AA9AF8D8ECE1E1E51100612500355F1E55A4D7D0732075AC758DDA215A5BD3C4325C979045BB1D278EB5AB49F707966E635660E37C2F3D3EC22CE00E8BCA89A6360B88F90BBFB8A83F00C6D6DB23ED270C3DE62D0000000CE1E72200000000240000058A2D0000000B62400000023C9645D281148F886D2B1C5BA7AD18C60010666517AA9AF8D8ECE1E1E5110064566C9B9ACDEF170217828B7899E806F9A7DE6E7F27E45DF0C3B9488DFFFDD9BEE1E7220000000032000000000000000058F6EFD3F2E5BBF234DC0906ADEC70F8A9832167B8745B8EE8E19362EE2FD8062E82148F886D2B1C5BA7AD18C60010666517AA9AF8D8ECE1E1E411006456CF8D13399C6ED20BA82740CFA78E928DC8D498255249BA634D04644ACD92309EE72200000000364D04644ACD92309E58CF8D13399C6ED20BA82740CFA78E928DC8D498255249BA634D04644ACD92309E011100000000000000000000000055534400000000000211DD39C650A96EDA48334E70CC4A85B8B2E8502CD30311000000000000000000000000000000000000000004110000000000000000000000000000000000000000E1E1F1031000";
		RippleBinarySerializer binSer = new RippleBinarySerializer();
		RippleBinaryObject largeTxObj = binSer.readBinaryObject(largeMeta);
		assertNotNull(largeTxObj);
	}
}
