package jstellarapi.core;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import jstellarapi.core.DenominatedIssuedCurrency;

import org.junit.Test;

public class DenominatedIssuedCurrencyTest {

	@Test
	public void testToJSON() {
		DenominatedIssuedCurrency xrpAmount = new DenominatedIssuedCurrency(new BigDecimal(1));
		
		DenominatedIssuedCurrency xrpAmount2 = new DenominatedIssuedCurrency();
		xrpAmount2.copyFrom(xrpAmount.toJSON());
		assertEquals(xrpAmount, xrpAmount2);
	}

}
