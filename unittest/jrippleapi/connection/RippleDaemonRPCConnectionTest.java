package jrippleapi.connection;

import static org.junit.Assert.*;

import javax.xml.bind.DatatypeConverter;

import jrippleapi.core.RippleAddress;

import org.junit.Test;

public class RippleDaemonRPCConnectionTest {

	@Test
	public void testAccountPublicInfo() throws Exception{
		RippleDaemonRPCConnection rpcConnection = new RippleDaemonRPCConnection();
		RippleAddressPublicInformation publicInfo = rpcConnection.getPublicInformation(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI);
		assertTrue(publicInfo.nextTransactionSequence>159);
	}
	
	@Test
	public void testSubmitTransaction() throws Exception {
		RippleDaemonRPCConnection rpcConnection = new RippleDaemonRPCConnection();

		byte[] signedTXBytes =DatatypeConverter.parseHexBinary("1200002200000000240000005B61D3C38D7EA4C680000000000000000000000000004254430000000000530BE6CE7A1812CA1E21C0E11431784E246330ED68400000000000000A7321023FA9ED580CD3208BBB380DF3A0CAF142D3A240AF28A2F8E2F372FC635C24417774483046022100EBA01512524B32ABD03EA736110AC3326FFE2707C115B0904EBDECBB74088B1F022100B6BC3A277BAD105D5FFA151640D9161589649C4E76027BEA07375B65E1B7C2168114530BE6CE7A1812CA1E21C0E11431784E246330ED83149DFEBA50DE0C0BE1AA11A7509762FC2374080E2C");
		rpcConnection.submitTransaction(signedTXBytes);
	}

}
