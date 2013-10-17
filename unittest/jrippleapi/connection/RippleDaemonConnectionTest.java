package jrippleapi.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import jrippleapi.beans.Account;
import jrippleapi.beans.AccountInformation;
import jrippleapi.beans.AccountTest;
import jrippleapi.beans.TrustLines;
import jrippleapi.beans.CurrencyUnit;
import jrippleapi.beans.DenominatedIssuedCurrency;
import jrippleapi.beans.ExchangeOffers;
import jrippleapi.beans.OrderBook;
import jrippleapi.beans.RippleAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RippleDaemonConnectionTest {
	static RippleDaemonConnection conn;
	
	@BeforeClass
	public static void setupConnection() throws Exception{
		conn = new RippleDaemonConnection();
	}
	
	@AfterClass
	public static void closeConnection() throws Exception{
		conn.close();
	}
	
	@Test
	public void testAccountInfo() throws Exception {
		AccountInformation jRippleAccount = conn.getAccountInfo(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI.toString());
		assertEquals(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI.toString(), jRippleAccount.account);
		assertEquals(1, jRippleAccount.xrpBalance.compareTo(BigInteger.valueOf(200000000)));
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
		OrderBook book = conn.getOrderBook(RippleAddress.RIPPLE_ADDRESS_BITSTAMP.toString(), CurrencyUnit.BTC, CurrencyUnit.XRP, NB_ENTRIES);
		assertEquals(NB_ENTRIES, book.size());
		
	}
	
	@Test
	public void testAccountOffers() throws Exception {
		ExchangeOffers offers = conn.getAccountOffers(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI.toString());
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
		oneXRP.issuer=RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI;
		oneXRP.currency=CurrencyUnit.XRP;
		oneXRP.amount=oneXRP.currency.fromString("1000000");
		conn.sendPayment(AccountTest.getTestAccount(), RippleAddress.RIPPLE_ADDRESS_PMARCHES, oneXRP);

		DenominatedIssuedCurrency oneMiliBTC = new DenominatedIssuedCurrency();
		oneMiliBTC.issuer=RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI;
		oneMiliBTC.currency=CurrencyUnit.BTC;
		oneMiliBTC.amount=oneMiliBTC.currency.fromString("0.001");
		conn.sendPayment(AccountTest.getTestAccount(), RippleAddress.RIPPLE_ADDRESS_PMARCHES, oneMiliBTC);
	}
	
	@Test
	public void testSetTrustLine() throws Exception{
		DenominatedIssuedCurrency trustAmount = new DenominatedIssuedCurrency();
		trustAmount.currency=CurrencyUnit.BTC;
		trustAmount.issuer=RippleAddress.RIPPLE_ADDRESS_PMARCHES;
		trustAmount.amount=trustAmount.currency.fromString("1");
		conn.setTrustLine(AccountTest.getTestAccount(), RippleAddress.RIPPLE_ADDRESS_PMARCHES, trustAmount);
	}
	
	@Test
	public void testGetCreditLines() throws Exception {
		TrustLines creditLines = conn.getCreditLines(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI.toString());
		assertEquals(2, creditLines.size());
	}
	
	@Test
	public void testSignTransaction() throws Exception {
		DenominatedIssuedCurrency oneXRP = new DenominatedIssuedCurrency();
		oneXRP.issuer=RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI;
		oneXRP.currency=CurrencyUnit.XRP;
		oneXRP.amount=oneXRP.currency.fromString("1000000");
		Account testAccount=AccountTest.getTestAccount();
		RipplePaymentTransaction tx = new RipplePaymentTransaction(testAccount.address, RippleAddress.RIPPLE_ADDRESS_PMARCHES, oneXRP);
		RipplePaymentTransaction signedTx = conn.signTransaction(testAccount.secret, tx);
		assertNotNull(signedTx.publicKeyUsedToSign);
		assertNotNull(signedTx.signature);
	}

}
