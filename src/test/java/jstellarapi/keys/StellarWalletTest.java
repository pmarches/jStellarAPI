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

import org.abstractj.kalium.NaCl;
import org.junit.Test;

public class StellarWalletTest {
	@Test
	public void testKeys(){
		byte[] seedBytes=DatatypeConverter.parseHexBinary("C30BC4C509C78EB6D44369B598F4FFECC4006AB83B31D9DBB2EC9900ACF3240B");
		StellarSeedAddress seed = new StellarSeedAddress(seedBytes);
		StellarPrivateKey privateKey=seed.getPrivateKey();
		StellarPublicKey publicKey=privateKey.getPublicKey();
		String expectedPublicKeyHex="807E474895C2CD542B9699BF07C37BB5C995FB9CD563982AF8021838CFDAC79A";

		StellarAddress address=publicKey.getAddress();
		assertEquals("gnmMadkFCvPdq9ojhBmPh65HZDw9BDGo1n", address.toString());
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
