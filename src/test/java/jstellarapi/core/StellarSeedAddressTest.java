package jstellarapi.core;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import javax.xml.bind.DatatypeConverter;

import jstellarapi.core.StellarPrivateKey;
import jstellarapi.core.StellarSeedAddress;

import org.junit.Test;

public class StellarSeedAddressTest {
	@Test
	public void testBigIntegerToBytes() {
		byte[] b1=StellarPrivateKey.bigIntegerToBytes(BigInteger.valueOf(0b10000000), 1);
		assertEquals(1, b1.length);
		assertEquals(128, 0xFF&b1[0]);
	}

	@Test
	public void testStellarSeedAddressStringFirstReport() {
        StellarSeedAddress seedFirstReported = new StellarSeedAddress("ss1i94tYmAPsGZNHtHiBxTB2okf8Q");
		assertEquals("rLMy8VetV8nGrtAKfRyU5oXUKmEUxzkwSJ", seedFirstReported.getPublicStellarAddress().toString());
	}
	
	@Test
	public void testStellarSeedAddressString() {
		StellarSeedAddress seed = new StellarSeedAddress("ss4vxREF8bnM7uEYiZs31x1QmAEWj");
		assertEquals("ss4vxREF8bnM7uEYiZs31x1QmAEWj", seed.toString());
        assertEquals("r3jjRU5BGWQgnTdY5xXqTYepmFksnwD7VG", seed.getPublicStellarAddress().toString());
	}
}
