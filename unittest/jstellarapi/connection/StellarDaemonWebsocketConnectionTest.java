package jstellarapi.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DatatypeConverter;

import jstellarapi.TestUtilities;
import jstellarapi.connection.ExchangeOffers;
import jstellarapi.connection.GenericJSONSerializable;
import jstellarapi.connection.JSONSubscribtionFeed;
import jstellarapi.connection.OrderBook;
import jstellarapi.connection.StellarAddressPublicInformation;
import jstellarapi.connection.StellarDaemonConnection;
import jstellarapi.connection.StellarDaemonWebsocketConnection;
import jstellarapi.connection.TrustLines;
import jstellarapi.core.DenominatedIssuedCurrency;
import jstellarapi.core.StellarAddress;
import jstellarapi.core.StellarPaymentTransaction;
import jstellarapi.core.StellarSeedAddress;
import jstellarapi.core.StellarTransactionHistory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class StellarDaemonWebsocketConnectionTest {
	static StellarDaemonWebsocketConnection conn;
	
	@BeforeClass
	public static void setupConnection() throws Exception{
		conn = new StellarDaemonWebsocketConnection(StellarDaemonWebsocketConnection.TEST_SERVER_URL);
	}
	
	@AfterClass
	public static void closeConnection() throws Exception{
		if(conn!=null){
			conn.close();
		}
	}
	
	@Test
	public void testAccountInfo() throws Exception {
		StellarAddressPublicInformation jStellarAccount = conn.getAccountInfo(StellarAddress.STELLAR_ADDRESS_JSTELLARAPI.toString());
		assertEquals(StellarAddress.STELLAR_ADDRESS_JSTELLARAPI.toString(), jStellarAccount.account);
		assertEquals(1, jStellarAccount.STRBalance.compareTo(BigDecimal.valueOf(200)));
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
		OrderBook book = conn.getOrderBook(StellarAddress.STELLAR_ADDRESS_JSTELLARAPI.toString(), "BTC", "STR", NB_ENTRIES);
		assertEquals(NB_ENTRIES, book.size());
		
	}
	
	@Test
	public void testAccountOffers() throws Exception {
		ExchangeOffers offers = conn.getAccountOffers(StellarAddress.STELLAR_ADDRESS_JSTELLARAPI.toString());
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
		StellarSeedAddress secret = TestUtilities.getTestSeed();
		DenominatedIssuedCurrency oneSTR = new DenominatedIssuedCurrency();
		oneSTR.issuer=StellarAddress.STELLAR_ADDRESS_JSTELLARAPI;
		oneSTR.amount=new BigDecimal("1000000");
		conn.sendPayment(secret, StellarAddress.STELLAR_ADDRESS_PMARCHES, oneSTR);

		DenominatedIssuedCurrency oneMiliBTC = new DenominatedIssuedCurrency();
		oneMiliBTC.issuer=StellarAddress.STELLAR_ADDRESS_JSTELLARAPI;
		oneMiliBTC.currency="BTC";
		oneMiliBTC.amount=new BigDecimal("0.001");
		conn.sendPayment(TestUtilities.getTestSeed(), StellarAddress.STELLAR_ADDRESS_PMARCHES, oneMiliBTC);
	}
	
	@Test
	public void testSetTrustLine() throws Exception{
		DenominatedIssuedCurrency trustAmount = new DenominatedIssuedCurrency();
		trustAmount.currency="BTC";
		trustAmount.issuer=StellarAddress.STELLAR_ADDRESS_PMARCHES;
		trustAmount.amount=new BigDecimal("1");
		conn.setTrustLine(TestUtilities.getTestSeed(), StellarAddress.STELLAR_ADDRESS_PMARCHES, trustAmount);
	}
	
	@Test
	public void testGetCreditLines() throws Exception {
		TrustLines creditLines = conn.getCreditLines(StellarAddress.STELLAR_ADDRESS_JSTELLARAPI.toString());
		assertEquals(3, creditLines.size());
	}
	
	@Test
	public void testSignTransaction() throws Exception {
		DenominatedIssuedCurrency oneSTR = new DenominatedIssuedCurrency();
		oneSTR.issuer=StellarAddress.STELLAR_ADDRESS_JSTELLARAPI;
		oneSTR.amount=new BigDecimal("1000000");
		StellarSeedAddress testAccount=TestUtilities.getTestSeed();
		StellarPaymentTransaction tx = new StellarPaymentTransaction(testAccount.getPublicStellarAddress(), StellarAddress.STELLAR_ADDRESS_PMARCHES, oneSTR, 1);
		StellarPaymentTransaction signedTx = conn.signTransaction(testAccount, tx);
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
		assertNotNull(ledgerFeed.poll(21, TimeUnit.SECONDS));
		assertNotNull(ledgerFeed.poll(21, TimeUnit.SECONDS));
		conn.subscribeToLedgers(false);
		Thread.sleep(20000);
		assertNull(ledgerFeed.poll());
	}
	
	@Test
	public void testSubscribeToTransaction() throws Exception{
		conn.subscribeToTransactionOfAddress(StellarAddress.STELLAR_ADDRESS_PMARCHES.toString());
		JSONSubscribtionFeed txFeed=conn.getTransactionFeed();

		StellarSeedAddress secret = TestUtilities.getTestSeed();
		conn.sendPayment(secret, StellarAddress.STELLAR_ADDRESS_PMARCHES, DenominatedIssuedCurrency.ONE_STR);
		StellarPaymentTransaction tx=new StellarPaymentTransaction(txFeed.take());
		assertEquals(StellarAddress.STELLAR_ADDRESS_PMARCHES, tx.payee);
		assertEquals(secret.getPublicStellarAddress(), tx.payer);
		assertTrue(txFeed.isEmpty());
		conn.unsubscribeToTransactionOfAddress(StellarAddress.STELLAR_ADDRESS_PMARCHES.toString());
	}

	@Test
	public void testGetTransactionOfAccount(){
		StellarTransactionHistory txHistory=conn.getTransactionsForAccount(StellarAddress.STELLAR_ADDRESS_PMARCHES.toString(), StellarDaemonConnection.GENESIS_LEDGER_NUMBER);
		assertEquals(322, txHistory.size());
	}
}
