package jrippleapi;

import static org.junit.Assert.*;

import java.util.concurrent.Future;

import jrippleapi.beans.AccountInformation;
import jrippleapi.beans.DenominatedAmount;
import jrippleapi.beans.Denomination;
import jrippleapi.beans.ExchangeOffers;
import jrippleapi.beans.OrderBook;
import jrippleapi.beans.RandomString;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class RippleConnectionTest {

	private static final String ROOT_ACCOUNT = "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh";
	private static final String PMARCHES_ACCOUNT = "rEQQNvhuLt1KTYmDWmw12mPvmJD4KCtxmS";
	private static final String BITSTAMP_ACCOUNT = "rhkBt4tgr1Lt4qjghn1NXpWTcABUTQc6Cv";
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
		Future<AccountInformation> futureAccountInfo = conn.getAccountInfoFuture(ROOT_ACCOUNT);
		assertEquals(ROOT_ACCOUNT, conn.getAccountInfo(ROOT_ACCOUNT).account);
		assertEquals(ROOT_ACCOUNT, futureAccountInfo.get().account);
	}
	
	@Test
	public void testPing() throws Exception {
		assertTrue("ping", conn.ping());
	}
	
	@Test
	public void testRandom() throws Exception {
		Future<RandomString> randomFuture = conn.getRandomFuture();
		String random = conn.getRandom();
		assertFalse("", random.equals(randomFuture.get().random));
	}
	
	@Test
//	@Ignore
	public void testOrderBook() throws Exception {
		final int NB_ENTRIES=12;
		OrderBook book = conn.getOrderBook(Denomination.BTC_DENOMINATION, Denomination.XRP_DENOMINATION, NB_ENTRIES);
		assertNotNull(book);
		assertEquals(NB_ENTRIES, book.size());
	}
	
	@Test
	public void testAccountOffers() throws Exception {
//		conn.getAccountCreditLines("rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh");
		ExchangeOffers offers = conn.getAccountOffers(PMARCHES_ACCOUNT);
		assertEquals(2, offers.size());
		assertEquals(BITSTAMP_ACCOUNT, offers.get(0).takerGets.denomination.issuerStr);
		assertEquals(Denomination.XRP_DENOMINATION, offers.get(1).takerGets.denomination);
	}


}
