package jrippleapi.serialization;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import jrippleapi.beans.RippleAddress;
import jrippleapi.serialization.RippleBinarySchema.BinaryFormatField;
import jrippleapi.serialization.RippleBinarySchema.TransactionTypes;

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
		assertEquals("1000000", serObj.getField(BinaryFormatField.Amount).toString());
	}

	@Test
	public void testTrustSet() throws IOException {
		MappedByteBuffer trustet1ByteBuffer = fileToBuffer("testdata/trustset1.bin");
		
		RippleBinarySerializer binSer = new RippleBinarySerializer();
		RippleSerializedObject serObj = binSer.readSerializedObject(trustet1ByteBuffer);
		assertEquals(TransactionTypes.TRUST_SET, serObj.getTransactionType());
		assertEquals(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI, serObj.getField(BinaryFormatField.Account));
		assertEquals("1000000000000000", serObj.getField(BinaryFormatField.LimitAmount).toString());
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

		File binDir = new File("testdata/10k");
		for(File binFile : binDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".bin");
			}
		})){
			MappedByteBuffer buffer = fileToBuffer(binFile.getAbsolutePath());
			RippleSerializedObject serObj = binSer.readSerializedObject(buffer);
		}
	}
}
