package jrippleapi.beans;

import static org.junit.Assert.*;

import org.junit.Test;

public class DenominatedIssuedCurrencyTest {

	@Test
	public void testToJSON() {
		DenominatedIssuedCurrency xrpAmount = new DenominatedIssuedCurrency();
		xrpAmount.currency = CurrencyUnit.XRP;
		xrpAmount.amount = xrpAmount.currency.fromString("1");
		
		DenominatedIssuedCurrency xrpAmount2 = new DenominatedIssuedCurrency();
		xrpAmount2.copyFrom(xrpAmount.toJSON());
		assertEquals(xrpAmount, xrpAmount2);
	}

}
