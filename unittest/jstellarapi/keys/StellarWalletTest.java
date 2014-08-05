package jstellarapi.keys;

import java.io.File;
import java.math.BigInteger;

import jstellarapi.TestUtilities;
import jstellarapi.core.StellarAddress;
import jstellarapi.keys.StellarWallet;

import org.junit.Test;

public class StellarWalletTest {
	@Test
	public void testSendXRP() throws Exception {
		File testWalletFile = new File("testdata/jStellarAPI.wallet");
		StellarWallet wallet = StellarWallet.createWallet(TestUtilities.getTestSeed(), testWalletFile);
		wallet.sendXRP(BigInteger.ONE, StellarAddress.STELLAR_ADDRESS_PMARCHES);
		testWalletFile.delete();
	}
}
