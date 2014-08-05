package jstellarapi.keys;

import static org.junit.Assert.*;

import java.io.File;
import java.math.BigInteger;

import jstellarapi.TestUtilities;
import jstellarapi.core.StellarAddress;
import jstellarapi.keys.StellarWallet;

import org.junit.Test;

public class StellarWalletTest {
	@Test
	public void testLoadWallet() throws Exception{
		StellarWallet wallet = new StellarWallet(new File("secrets/jStellarAPI-wallet.json"));
		assertEquals(StellarAddress.STELLAR_ADDRESS_JSTELLARAPI, wallet.seed.getPublicStellarAddress());
	}

	//	@Test
	public void testSendSTR() throws Exception {
		File testWalletFile = new File("testdata/jStellarAPI.wallet");
		StellarWallet wallet = StellarWallet.createWallet(TestUtilities.getTestSeed(), testWalletFile);
		wallet.sendSTR(BigInteger.ONE, StellarAddress.STELLAR_ADDRESS_PMARCHES);
		testWalletFile.delete();
	}
}
