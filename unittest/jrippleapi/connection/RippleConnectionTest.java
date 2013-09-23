package jrippleapi.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import jrippleapi.beans.Account;
import jrippleapi.beans.AccountInformation;
import jrippleapi.beans.AccountTest;
import jrippleapi.beans.DenominatedIssuedCurrency;
import jrippleapi.beans.IssuedCurrency;
import jrippleapi.beans.ExchangeOffers;
import jrippleapi.beans.OrderBook;
import jrippleapi.connection.RippleConnection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RippleConnectionTest {
	static RippleConnection conn;
	
	@BeforeClass
	public static void setupConnection() throws Exception{
		conn = new RippleConnection();
	}
	
	@AfterClass
	public static void closeConnection() throws Exception{
		conn.close();
	}
	
	@Test
	public void testAccountInfo() throws Exception {
		AccountInformation jRippleAccount = conn.getAccountInfo(Account.RIPPLE_ADDRESS_JRIPPLEAPI);
		assertEquals(Account.RIPPLE_ADDRESS_JRIPPLEAPI, jRippleAccount.account);
		assertEquals("199999910", jRippleAccount.balance);
	}
	
	@Test
	public void testPing() throws Exception {
		assertTrue("ping", conn.ping());
	}
	
	@Test
	public void testRandom() throws Exception {
		assertEquals("wrong random", 64, conn.getRandom().length());
	}
	
	@Test
	public void testOrderBook() throws Exception {
		final int NB_ENTRIES=12;
		OrderBook book = conn.getOrderBook(IssuedCurrency.CURRENCY_BITSTAMP_BTC, IssuedCurrency.CURRENCY_XRP, NB_ENTRIES);
		assertEquals(NB_ENTRIES, book.size());
		
	}
	
	@Test
	public void testAccountOffers() throws Exception {
		ExchangeOffers offers = conn.getAccountOffers(Account.RIPPLE_ADDRESS_JRIPPLEAPI);
		assertEquals(2, offers.size());
		DenominatedIssuedCurrency takerGets0 = offers.get(0).takerGets;
		DenominatedIssuedCurrency takerPays0 = offers.get(0).takerPays;
		assertEquals(IssuedCurrency.CURRENCY_XRP, takerGets0.issuance);
		assertEquals(IssuedCurrency.CURRENCY_BITSTAMP_BTC, takerPays0.issuance);

		DenominatedIssuedCurrency takerGets1 = offers.get(1).takerGets;
		DenominatedIssuedCurrency takerPays1 = offers.get(1).takerPays;
		assertEquals(IssuedCurrency.CURRENCY_XRP, takerGets1.issuance);
		assertEquals(IssuedCurrency.CURRENCY_BITSTAMP_USD, takerPays1.issuance);
	}

	@Test
	public void testPayment() throws Exception{
//		DenominatedIssuedCurrency oneXRP = new DenominatedIssuedCurrency();
//		oneXRP.amountStr="1000000";
//		oneXRP.issuance=IssuedCurrency.XRP_CURRENCY;
//		GenericJSONSerializable tx = conn.sendPayment(AccountTest.getTestAccount(), Account.RIPPLE_ADDRESS_PMARCHES, oneXRP);

		DenominatedIssuedCurrency oneSatoshi = new DenominatedIssuedCurrency();
		oneSatoshi.amountStr="0.001";
		oneSatoshi.issuance=new IssuedCurrency();
		oneSatoshi.issuance.currencyStr="BTC";
		oneSatoshi.issuance.issuerStr=Account.RIPPLE_ADDRESS_JRIPPLEAPI;
		conn.sendPayment(AccountTest.getTestAccount(), Account.RIPPLE_ADDRESS_PMARCHES, oneSatoshi);
	}
}
