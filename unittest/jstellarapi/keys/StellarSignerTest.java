package jstellarapi.keys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.nio.ByteBuffer;

import javax.xml.bind.DatatypeConverter;

import jstellarapi.TestUtilities;
import jstellarapi.connection.GenericJSONSerializable;
import jstellarapi.connection.StellarDaemonWebsocketConnection;
import jstellarapi.core.StellarPrivateKey;
import jstellarapi.keys.StellarSigner;
import jstellarapi.serialization.StellarBinaryObject;
import jstellarapi.serialization.StellarBinarySerializer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

public class StellarSignerTest {
	@Test
	public void testSubmitSignedTransaction() throws Exception{
		StellarBinarySerializer binSer=new StellarBinarySerializer();
		StellarPrivateKey privateKey = TestUtilities.getTestSeed().getPrivateKey(0);
		StellarSigner signer = new StellarSigner(privateKey);
		JSONArray allTx = (JSONArray) new JSONParser().parse(new FileReader("testdata/unittest-tx.json"));
		for(Object obj : allTx){
			JSONObject jsonTx = (JSONObject) obj;
			String hexTx = (String) jsonTx.get("tx");
			
			ByteBuffer inputBytes=ByteBuffer.wrap(DatatypeConverter.parseHexBinary(hexTx));
			StellarBinaryObject originalSignedRBO = binSer.readBinaryObject(inputBytes);
			assertTrue("Verification failed for "+hexTx, signer.isSignatureVerified(originalSignedRBO));
			
			StellarBinaryObject reSignedRBO = signer.sign(originalSignedRBO.getUnsignedCopy());
			byte[] signedBytes = binSer.writeBinaryObject(reSignedRBO).array();
			GenericJSONSerializable submitResult = new StellarDaemonWebsocketConnection(StellarDaemonWebsocketConnection.TEST_SERVER_URL).submitTransaction(signedBytes);
//			assertNull(submitResult.jsonCommandResult.get("error_exception"));
			assertEquals("This sequence number has already past.", submitResult.jsonCommandResult.get("engine_result_message"));
			assertTrue(signer.isSignatureVerified(reSignedRBO));
		}
	}
}
