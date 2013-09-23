package jrippleapi.beans;

import static org.junit.Assert.*;

import org.junit.Test;

public class DenominatedIssuedCurrencyTest {

	@Test
	public void testToJSON() {
		DenominatedIssuedCurrency xrpAmount = new DenominatedIssuedCurrency();
		xrpAmount.amountStr="1";
		xrpAmount.issuance = IssuedCurrency.CURRENCY_XRP;
		
		DenominatedIssuedCurrency xrpAmount2 = new DenominatedIssuedCurrency();
		xrpAmount2.copyFrom(xrpAmount.toJSON());
		assertEquals(xrpAmount, xrpAmount2);
	}

}
