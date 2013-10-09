package jrippleapi.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import jrippleapi.beans.Account;
import jrippleapi.beans.AccountInformation;
import jrippleapi.beans.AccountTest;
import jrippleapi.beans.CreditLines;
import jrippleapi.beans.CurrencyUnit;
import jrippleapi.beans.DenominatedIssuedCurrency;
import jrippleapi.beans.ExchangeOffers;
import jrippleapi.beans.OrderBook;
import jrippleapi.beans.Transaction;

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
		AccountInformation jRippleAccount = conn.getAccountInfo(Account.RIPPLE_ADDRESS_JRIPPLEAPI.toString());
		assertEquals(Account.RIPPLE_ADDRESS_JRIPPLEAPI.toString(), jRippleAccount.account);
		assertEquals(-1, new BigDecimal(200).compareTo(jRippleAccount.xrpBalance));
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
		OrderBook book = conn.getOrderBook(Account.RIPPLE_ADDRESS_BITSTAMP.toString(), CurrencyUnit.BTC, CurrencyUnit.XRP, NB_ENTRIES);
		assertEquals(NB_ENTRIES, book.size());
		
	}
	
	@Test
	public void testAccountOffers() throws Exception {
		ExchangeOffers offers = conn.getAccountOffers(Account.RIPPLE_ADDRESS_JRIPPLEAPI.toString());
		assertEquals(2, offers.size());
		DenominatedIssuedCurrency takerGets0 = offers.get(0).takerGets;
		DenominatedIssuedCurrency takerPays0 = offers.get(0).takerPays;
		assertEquals(CurrencyUnit.XRP, takerGets0.currency);
		assertEquals(CurrencyUnit.BTC, takerPays0.currency);

		DenominatedIssuedCurrency takerGets1 = offers.get(1).takerGets;
		DenominatedIssuedCurrency takerPays1 = offers.get(1).takerPays;
		assertEquals(CurrencyUnit.XRP, takerGets1.currency);
		assertEquals(CurrencyUnit.USD, takerPays1.currency);
	}

	@Test
//	@Ignore
	public void testPayment() throws Exception{
		DenominatedIssuedCurrency oneXRP = new DenominatedIssuedCurrency();
		oneXRP.issuerStr=Account.RIPPLE_ADDRESS_JRIPPLEAPI.toString();
		oneXRP.currency=CurrencyUnit.XRP;
		oneXRP.amount=oneXRP.currency.fromString("1000000");
		conn.sendPayment(AccountTest.getTestAccount(), Account.RIPPLE_ADDRESS_PMARCHES.toString(), oneXRP);

		DenominatedIssuedCurrency oneMiliBTC = new DenominatedIssuedCurrency();
		oneMiliBTC.issuerStr=Account.RIPPLE_ADDRESS_JRIPPLEAPI.toString();
		oneMiliBTC.currency=CurrencyUnit.BTC;
		oneMiliBTC.amount=oneMiliBTC.currency.fromString("0.001");
		conn.sendPayment(AccountTest.getTestAccount(), Account.RIPPLE_ADDRESS_PMARCHES.toString(), oneMiliBTC);
	}
	
	@Test
	public void testSetCreditLine() throws Exception{
		DenominatedIssuedCurrency creditAmount = new DenominatedIssuedCurrency();
		creditAmount.currency=CurrencyUnit.BTC;
		creditAmount.issuerStr=Account.RIPPLE_ADDRESS_PMARCHES.toString();
		creditAmount.amount=creditAmount.currency.fromString("1");
		conn.setCreditLine(AccountTest.getTestAccount(), Account.RIPPLE_ADDRESS_PMARCHES.toString(), creditAmount);
	}
	
	@Test
	public void testGetCreditLines() throws Exception {
		CreditLines creditLines = conn.getCreditLines(Account.RIPPLE_ADDRESS_JRIPPLEAPI.toString());
		assertEquals(2, creditLines.size());
	}
	
	@Test
	public void testSignTransaction() throws Exception {
		DenominatedIssuedCurrency oneXRP = new DenominatedIssuedCurrency();
		oneXRP.issuerStr=Account.RIPPLE_ADDRESS_JRIPPLEAPI.toString();
		oneXRP.currency=CurrencyUnit.XRP;
		oneXRP.amount=oneXRP.currency.fromString("1000000");
		Account testAccount=AccountTest.getTestAccount();
		Transaction tx = new Transaction(testAccount.address.toString(), Account.RIPPLE_ADDRESS_PMARCHES.toString(), oneXRP);
		Transaction signedTx = conn.signTransaction(testAccount.secret, tx);
		assertNotNull(signedTx.publicKeyUsedToSign);
		assertNotNull(signedTx.signature);
	}

}
