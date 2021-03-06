package jstellarapi.keys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.Security;

import javax.xml.bind.DatatypeConverter;

import jstellarapi.keys.StellarDeterministicKeyGenerator;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

public class StellarDeterministicKeyGeneratorTest {

	{
		Security.addProvider(new BouncyCastleProvider());
	}

	//As described in https://Stellar.com/wiki/Account_Family
	@Test
	public void testVector() throws Exception {
		final byte[] seedBytes = DatatypeConverter.parseHexBinary("71ED064155FFADFA38782C5E0158CB26");
		StellarDeterministicKeyGenerator generator = new StellarDeterministicKeyGenerator(seedBytes);
		final byte[] FIRST_HALF_ZERO_HASH = DatatypeConverter.parseHexBinary("B8244D028981D693AF7B456AF8EFA4CAD63D282E19FF14942C246E50D9351D22");
		byte[] first256BitsOfHash = generator.halfSHA512(new byte[]{0});
		assertTrue(MessageDigest.isEqual(first256BitsOfHash , FIRST_HALF_ZERO_HASH));

		byte[] hunderedKBytes = ByteBuffer.allocate(4).putInt(100000).array();
		byte[] first256BitsOfHunderedKHash = generator.halfSHA512(hunderedKBytes);
		final byte[] FIRST_HALF_HUNDEREDK_HASH = DatatypeConverter.parseHexBinary("8EEE2EA9E7F93AB0D9E66EE4CE696D6824922167784EC7F340B3567377B1CE64");
		assertTrue(MessageDigest.isEqual(first256BitsOfHunderedKHash , FIRST_HALF_HUNDEREDK_HASH));
		
		assertEquals("7CFBA64F771E93E817E15039215430B53F7401C34931D111EAB3510B22DBB0D8", DatatypeConverter.printHexBinary(generator.getPrivateRootKeyBytes()));
		assertEquals("fht5yrLWh3P8DrJgQuVNDPQVXGTMyPpgRHFKGQzFQ66o3ssesk3o", generator.getPublicGeneratorFamily().toString());
		assertEquals("aBRoQibi2jpDofohooFuzZi9nEzKw9Zdfc4ExVNmuXHaJpSPh8uJ", generator.getAccountPublicKey(0).toString());
		assertEquals("rhcfR9Cg98qCxHpCcPBmMonbDBXo84wyTn", generator.getAccountPublicKey(0).getAddress().toString());
	}
}
