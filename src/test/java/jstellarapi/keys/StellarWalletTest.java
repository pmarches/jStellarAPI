package jstellarapi.keys;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigInteger;

import javax.xml.bind.DatatypeConverter;

import jstellarapi.TestUtilities;
import jstellarapi.core.StellarAddress;
import jstellarapi.core.StellarPrivateKey;
import jstellarapi.core.StellarPublicKey;
import jstellarapi.core.StellarSeedAddress;

import org.junit.Test;

public class StellarWalletTest {
	@Test
	public void testKeys(){
		String seedBytesStr="B0739E1908429A4E259960E99E6B50883BFF4CCC58B546F798F8CB829674730A";
		byte[] seedBytes=DatatypeConverter.parseHexBinary(seedBytesStr);
		assertEquals(32, seedBytes.length);
		StellarSeedAddress seed = new StellarSeedAddress(seedBytes);
		StellarPrivateKey privateKey=seed.getPrivateKey();

		StellarPublicKey publicKey=privateKey.getPublicKey();
		String expectedPublicKeyHex="58ADFD3A60E59396E625D37C217B2BE80D1DB45D3C6B0E7A942CAE7223D3EF3C";
		assertEquals(expectedPublicKeyHex, DatatypeConverter.printHexBinary(publicKey.getBytes()));
		StellarAddress address=publicKey.getAddress();
		assertEquals("ga2S7sAuSqU4zseNDs2cRxMQXxmVEPm4MW", address.toString());
	}
	
	@Test
	public void testLoadWallet() throws Exception{
		StellarWallet wallet = new StellarWallet(new File("secrets/jStellarAPI-wallet.json"));
		assertEquals(StellarAddress.STELLAR_ADDRESS_JSTELLARAPI, wallet.getSeed().getPublicStellarAddress());
	}

	//	@Test
	public void testSendSTR() throws Exception {
		File testWalletFile = new File("testdata/jStellarAPI.wallet");
		StellarWallet wallet = StellarWallet.createWallet(TestUtilities.getTestSeed(), testWalletFile);
		wallet.sendSTR(BigInteger.ONE, StellarAddress.STELLAR_ADDRESS_PMARCHES);
		testWalletFile.delete();
	}
}
