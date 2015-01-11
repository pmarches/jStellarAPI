package jstellarapi.core;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import jstellarapi.core.DenominatedIssuedCurrency;

import org.junit.Test;

public class DenominatedIssuedCurrencyTest {

	@Test
	public void testToJSON() {
		DenominatedIssuedCurrency STRAmount = DenominatedIssuedCurrency.ONE_STR;
		
		DenominatedIssuedCurrency STRAmount2 = new DenominatedIssuedCurrency();
		STRAmount2.copyFrom(STRAmount.toJSON());
		assertEquals(STRAmount, STRAmount2);
	}

}
