package jrippleapi.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.DatatypeConverter;

import jrippleapi.TestUtilities;
import jrippleapi.core.DenominatedIssuedCurrency;
import jrippleapi.core.RippleAddress;
import jrippleapi.core.RipplePaymentTransaction;
import jrippleapi.core.RippleSeedAddress;
import jrippleapi.core.RippleTransactionHistory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RippleDaemonWebsocketConnectionTest {
	static RippleDaemonWebsocketConnection conn;
	
	@BeforeClass
	public static void setupConnection() throws Exception{
		conn = new RippleDaemonWebsocketConnection(RippleDaemonWebsocketConnection.RIPPLE_SERVER_URL);
	}
	
	@AfterClass
	public static void closeConnection() throws Exception{
		if(conn!=null){
			conn.close();
		}
	}
	
	@Test
	public void testAccountInfo() throws Exception {
		RippleAddressPublicInformation jRippleAccount = conn.getAccountInfo(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI.toString());
		assertEquals(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI.toString(), jRippleAccount.account);
		assertEquals(1, jRippleAccount.xrpBalance.compareTo(BigDecimal.valueOf(200)));
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
		final int NB_ENTRIES=9;
		OrderBook book = conn.getOrderBook(RippleAddress.RIPPLE_ADDRESS_BITSTAMP.toString(), "BTC", "XRP", NB_ENTRIES);
		assertEquals(NB_ENTRIES, book.size());
		
	}
	
	@Test
	public void testAccountOffers() throws Exception {
		ExchangeOffers offers = conn.getAccountOffers(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI.toString());
		assertEquals(1, offers.size());
		DenominatedIssuedCurrency takerGets0 = offers.get(0).takerGets;
		DenominatedIssuedCurrency takerPays0 = offers.get(0).takerPays;
		assertNull(takerGets0.currency);
		assertEquals("BTC", takerPays0.currency);

//		DenominatedIssuedCurrency takerGets1 = offers.get(1).takerGets;
//		DenominatedIssuedCurrency takerPays1 = offers.get(1).takerPays;
//		assertNull(takerGets1.currency);
//		assertEquals("USD", takerPays1.currency);
	}

	@Test
//	@Ignore
	public void testPayment() throws Exception{
		RippleSeedAddress secret = TestUtilities.getTestSeed();
		DenominatedIssuedCurrency oneXRP = new DenominatedIssuedCurrency();
		oneXRP.issuer=RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI;
		oneXRP.amount=new BigDecimal("1000000");
		conn.sendPayment(secret, RippleAddress.RIPPLE_ADDRESS_PMARCHES, oneXRP);

		DenominatedIssuedCurrency oneMiliBTC = new DenominatedIssuedCurrency();
		oneMiliBTC.issuer=RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI;
		oneMiliBTC.currency="BTC";
		oneMiliBTC.amount=new BigDecimal("0.001");
		conn.sendPayment(TestUtilities.getTestSeed(), RippleAddress.RIPPLE_ADDRESS_PMARCHES, oneMiliBTC);
	}
	
	@Test
	public void testSetTrustLine() throws Exception{
		DenominatedIssuedCurrency trustAmount = new DenominatedIssuedCurrency();
		trustAmount.currency="BTC";
		trustAmount.issuer=RippleAddress.RIPPLE_ADDRESS_PMARCHES;
		trustAmount.amount=new BigDecimal("1");
		conn.setTrustLine(TestUtilities.getTestSeed(), RippleAddress.RIPPLE_ADDRESS_PMARCHES, trustAmount);
	}
	
	@Test
	public void testGetCreditLines() throws Exception {
		TrustLines creditLines = conn.getCreditLines(RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI.toString());
		assertEquals(3, creditLines.size());
	}
	
	@Test
	public void testSignTransaction() throws Exception {
		DenominatedIssuedCurrency oneXRP = new DenominatedIssuedCurrency();
		oneXRP.issuer=RippleAddress.RIPPLE_ADDRESS_JRIPPLEAPI;
		oneXRP.amount=new BigDecimal("1000000");
		RippleSeedAddress testAccount=TestUtilities.getTestSeed();
		RipplePaymentTransaction tx = new RipplePaymentTransaction(testAccount.getPublicRippleAddress(), RippleAddress.RIPPLE_ADDRESS_PMARCHES, oneXRP, 1);
		RipplePaymentTransaction signedTx = conn.signTransaction(testAccount, tx);
		assertNotNull(signedTx.publicKeyUsedToSign);
		assertNotNull(signedTx.signature);
	}
	
	@Test
	public void testSubmitTransaction() throws Exception {
		byte[] signedTXBytes =DatatypeConverter.parseHexBinary("1200002200000000240000005B61D3C38D7EA4C680000000000000000000000000004254430000000000530BE6CE7A1812CA1E21C0E11431784E246330ED68400000000000000A7321023FA9ED580CD3208BBB380DF3A0CAF142D3A240AF28A2F8E2F372FC635C24417774483046022100EBA01512524B32ABD03EA736110AC3326FFE2707C115B0904EBDECBB74088B1F022100B6BC3A277BAD105D5FFA151640D9161589649C4E76027BEA07375B65E1B7C2168114530BE6CE7A1812CA1E21C0E11431784E246330ED83149DFEBA50DE0C0BE1AA11A7509762FC2374080E2C");
		GenericJSONSerializable result = conn.submitTransaction(signedTXBytes);
	}
	
	@Test
	public void testSubscribeToLedgers() throws InterruptedException, ExecutionException{
		conn.subscribeToLedgers(true);
		JSONSubscribtionFeed ledgerFeed=conn.getLedgerFeed();
		Thread.sleep(15000);
		assertEquals(3, ledgerFeed.size());
		conn.subscribeToLedgers(false);
		Thread.sleep(15000);
		assertEquals(3, ledgerFeed.size());
	}
	
	@Test
	public void testSubscribeToTransaction() throws Exception{
		conn.subscribeToTransactionOfAddress(RippleAddress.RIPPLE_ADDRESS_PMARCHES.toString());
		JSONSubscribtionFeed txFeed=conn.getTransactionFeed();

		RippleSeedAddress secret = TestUtilities.getTestSeed();
		conn.sendPayment(secret, RippleAddress.RIPPLE_ADDRESS_PMARCHES, DenominatedIssuedCurrency.ONE_XRP);
		RipplePaymentTransaction tx=new RipplePaymentTransaction(txFeed.take());
		assertEquals(RippleAddress.RIPPLE_ADDRESS_PMARCHES, tx.payee);
		assertEquals(secret.getPublicRippleAddress(), tx.payer);
		assertTrue(txFeed.isEmpty());
		conn.unsubscribeToTransactionOfAddress(RippleAddress.RIPPLE_ADDRESS_PMARCHES.toString());
	}

	@Test
	public void testGetTransactionOfAccount(){
		RippleTransactionHistory txHistory=conn.getTransactionsForAccount(RippleAddress.RIPPLE_ADDRESS_PMARCHES.toString(), RippleDaemonConnection.GENESIS_LEDGER_NUMBER);
		assertEquals(322, txHistory.size());
	}
}
