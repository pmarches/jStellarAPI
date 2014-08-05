package jstellarapi.connection;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.xml.bind.DatatypeConverter;

import jstellarapi.core.DenominatedIssuedCurrency;
import jstellarapi.core.StellarAddress;
import jstellarapi.core.StellarPaymentTransaction;
import jstellarapi.core.StellarSeedAddress;
import jstellarapi.core.StellarTransactionHistory;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebSocket
public class StellarDaemonWebsocketConnection extends StellarDaemonConnection {
	WebSocketConnection connection;
	public Session session;
		
	private int requestCounter=1;
	public final static URI TEST_SERVER_URL=URI.create("ws://test.stellar.org:9001");
	public final static URI MAIN_SERVER_URL=URI.create("ws://live.stellar.org:9001");
	public final static URI LOCALHOST_SERVER_URL=URI.create("ws://localhost:5006");
	JSONResponseHolder responseHolder = new JSONResponseHolder();
	JSONSubscribtionFeed ledgerFeed=new JSONSubscribtionFeed();
	JSONSubscribtionFeed transactionFeed=new JSONSubscribtionFeed();
    
    public StellarDaemonWebsocketConnection(URI StellardURI) throws Exception {
		this.connection = new WebSocketConnection(StellardURI, this);
	}
    
	@OnWebSocketMessage
    public void onMessage(String msg) {
    	try {
			JSONObject jsonMessage = (JSONObject) new JSONParser().parse(msg);
			Object messageType=jsonMessage.get("type");
			if("response".equals(messageType)){
				responseHolder.setResponseContent(jsonMessage);
			}
			else if("error".equals(messageType)){ //FIXME Can we remove this? Errors are responses also?
				responseHolder.setResponseError(jsonMessage);
			}
			else if("ledgerClosed".equals(messageType)){
				ledgerFeed.add(jsonMessage);
			}
			else if("transaction".equals(messageType)){
				transactionFeed.add((JSONObject) jsonMessage.get("transaction"));
			}
			else{
				//TODO Notify the subscribtions
				System.out.println("subscription of type "+messageType+" "+jsonMessage);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }

	public void close() throws Exception {
		connection.close();
	}

	public void sendString(String jsonString) throws IOException {
		session.getRemote().sendString(jsonString);
	}

	public <T extends JSONSerializable> FutureJSONResponse<T> sendCommand(JSONObject command, T unserializedResponse){
        try {
        	command.put("id", requestCounter);
			FutureJSONResponse<T> pendingResponse=new FutureJSONResponse<T>(requestCounter, responseHolder, unserializedResponse);
			responseHolder.addPendingResponse(pendingResponse);
			sendString(command.toJSONString());
        	requestCounter++;
			return pendingResponse;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean ping(){
		JSONObject pingComand = new JSONObject();
		pingComand.put("command", "ping");
		Future<GenericJSONSerializable> pingResponse = sendCommand(pingComand, new GenericJSONSerializable());
		try {
			return pingResponse.get()!=null;
		} catch (InterruptedException | ExecutionException e) {
			return false;
		}
	}
	
	public Future<StellarAddressPublicInformation> getAccountInfoFuture(String account){
		JSONObject accountInfoComand = new JSONObject();
		accountInfoComand.put("command", "account_info");
		accountInfoComand.put("account", account);
		return sendCommand(accountInfoComand, new StellarAddressPublicInformation());
	}
	
	public StellarAddressPublicInformation getAccountInfo(String account){
		try {
			return getAccountInfoFuture(account).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
		
	public Future<ExchangeOffers> getAccountOffersFuture(String account){
		JSONObject accountOffersComand = new JSONObject();
		accountOffersComand.put("command", "account_offers");
		accountOffersComand.put("account", account);
		return sendCommand(accountOffersComand, new ExchangeOffers());
	}

	public ExchangeOffers getAccountOffers(String account){
		Future<ExchangeOffers> futureOffersResponse = getAccountOffersFuture(account);
		try {
			return futureOffersResponse.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Future<OrderBook> getOrderBookFuture(String takerGetsIssuerStr, String takerGetsCurrency, String takerPaysCurrency, int nbEntries){
		JSONObject jsonTakerGets = new JSONObject();
		if(takerGetsIssuerStr!=null){
			jsonTakerGets.put("issuer", takerGetsIssuerStr);
		}
		jsonTakerGets.put("currency", takerGetsCurrency);

		JSONObject jsonTakerPays = new JSONObject();
//		if(takerPays.issuerStr!=null){
//			jsonTakerPays.put("issuer", takerPays.issuerStr);
//		}
		jsonTakerPays.put("currency", takerPaysCurrency);

		JSONObject orderBookComand = new JSONObject();
		orderBookComand.put("command", "book_offers");
		orderBookComand.put("limit", nbEntries);
		orderBookComand.put("taker_gets", jsonTakerGets);
		orderBookComand.put("taker_pays", jsonTakerPays);
		orderBookComand.put("snapshot", true);

		return sendCommand(orderBookComand, new OrderBook());
	}

	public OrderBook getOrderBook(String issuerStr, String takerGets, String takerPays, int nbEntries) {
		try {
			return getOrderBookFuture(issuerStr, takerGets, takerPays, nbEntries).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Future<RandomString> getRandomFuture() {
		JSONObject randomComand = new JSONObject();
		randomComand.put("command", "random");
		FutureJSONResponse randomResponse = sendCommand(randomComand, new RandomString());
		return randomResponse;
	}

	public String getRandom(){
		try {
			RandomString randomString = getRandomFuture().get();
			return randomString.random;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public GenericJSONSerializable subscribeToLedgers(boolean doSubscribe){
		try {
			return subscribeToLedgersFuture(doSubscribe).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public FutureJSONResponse<GenericJSONSerializable> subscribeToLedgersFuture(boolean doSubscribe){
		JSONObject command = new JSONObject();
		if(doSubscribe){
	    	command.put("command", "subscribe");
		}
		else{
	    	command.put("command", "unsubscribe");
		}
    	JSONArray streams = new JSONArray();
    	streams.add("ledger");
    	command.put("streams", streams);
		return sendCommand(command, new GenericJSONSerializable());
	}

	public GenericJSONSerializable subscribeToTransactionOfAddress(String StellarAddressToMonitor) {
		try {
			return subscribeToTransactionOfAddressFuture(StellarAddressToMonitor).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
	public FutureJSONResponse<GenericJSONSerializable> subscribeToTransactionOfAddressFuture(String StellarAddressToMonitor) {
		JSONObject command = new JSONObject();
    	command.put("command", "subscribe");

//Only if you want to see all transactions
//    	JSONArray streams = new JSONArray();
//    	streams.add("transactions");
//    	command.put("streams", streams);

    	JSONArray accounts = new JSONArray();
    	accounts.add(StellarAddressToMonitor);
    	command.put("accounts", accounts);
		return sendCommand(command, new GenericJSONSerializable());
	}

	public GenericJSONSerializable unsubscribeToTransactionOfAddress(String StellarAddressToMonitor) {
		JSONObject command = new JSONObject();
    	command.put("command", "unsubscribe");
    	JSONArray accounts = new JSONArray();
    	accounts.add(StellarAddressToMonitor);
    	command.put("accounts", accounts);
		try {
			return sendCommand(command, new GenericJSONSerializable()).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public FutureJSONResponse<GenericJSONSerializable> sendPaymentFuture(StellarSeedAddress payer, StellarAddress payee, DenominatedIssuedCurrency amount){	
		JSONObject jsonTx = new StellarPaymentTransaction(payer.getPublicStellarAddress(), payee, amount, 1).toTxJSON();
		JSONObject command = new JSONObject();
    	command.put("command", "submit");
    	command.put("tx_json", jsonTx);
    	command.put("secret", payer.toString());
		return sendCommand(command, new GenericJSONSerializable());
	}
	
	public GenericJSONSerializable sendPayment(StellarSeedAddress payer, StellarAddress payee, DenominatedIssuedCurrency amount){
		try {
			return sendPaymentFuture(payer, payee, amount).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Future<GenericJSONSerializable> setCreditLineFuture(StellarSeedAddress creditorAccount, StellarAddress debtorAccount, DenominatedIssuedCurrency creditAmount){
		JSONObject command = new JSONObject();
    	command.put("command", "submit");
    	JSONObject jsonTx = new JSONObject();
    	jsonTx.put("TransactionType", "TrustSet");
    	jsonTx.put("Account", creditorAccount.getPublicStellarAddress().toString());
    	jsonTx.put("LimitAmount", creditAmount.toJSON());
    	
		command.put("tx_json", jsonTx);
    	command.put("secret", creditorAccount.toString());
		return sendCommand(command, new GenericJSONSerializable());
	}

	public GenericJSONSerializable setTrustLine(StellarSeedAddress creditorAccount, StellarAddress debtorAccount, DenominatedIssuedCurrency creditAmount){
		try {
			return setCreditLineFuture(creditorAccount, debtorAccount, creditAmount).get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Future<TrustLines> getCreditLinesFuture(String ourAccount) {
		JSONObject command = new JSONObject();
    	command.put("command", "account_lines");
    	command.put("account", ourAccount);
		return sendCommand(command, new TrustLines());
	}

	public TrustLines getCreditLines(String ourAccount) {
		try {
			return getCreditLinesFuture(ourAccount).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Future<StellarPaymentTransaction> signTransactionFuture(StellarSeedAddress secret, StellarPaymentTransaction txToSign){
		JSONObject command = new JSONObject();
    	command.put("command", "sign");
    	command.put("secret", secret.toString());
		command.put("tx_json", txToSign.toTxJSON());
		return sendCommand(command, txToSign);
	}
	
	public StellarPaymentTransaction signTransaction(StellarSeedAddress secret, StellarPaymentTransaction txToSign) {
		try {
			return signTransactionFuture(secret, txToSign).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Future<GenericJSONSerializable> submitTransactionFuture(byte[] signedTransactionBytes){
		JSONObject command = new JSONObject();
		command.put("command", "submit");
		command.put("tx_blob", DatatypeConverter.printHexBinary(signedTransactionBytes));
		return sendCommand(command, new GenericJSONSerializable());
	}
	
	public GenericJSONSerializable submitTransaction(byte[] signedTransactionBytes) {
		try {
			return submitTransactionFuture(signedTransactionBytes).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public JSONSubscribtionFeed getLedgerFeed() {
		return ledgerFeed;
	}

	public JSONSubscribtionFeed getTransactionFeed() {
		return transactionFeed;
	}

	public StellarTransactionHistory getTransactionsForAccount(String StellarAddress, long startFromLedgerNumber) {
		StellarTransactionHistory txHistory=new StellarTransactionHistory();
		while(true){
			int oldSize=txHistory.size();
			getTransactionsForAccount(StellarAddress, txHistory, oldSize, startFromLedgerNumber);
			if(txHistory.size()==oldSize){
				break;
			}
		}
		return txHistory;
	}
	
	public StellarTransactionHistory getTransactionsForAccount(String StellarAddress, StellarTransactionHistory txHistory, int offset, long startFromLedgerNumber) {
		try {
			JSONObject command = new JSONObject();
			command.put("command", "account_tx");
			command.put("account", StellarAddress);
			if(startFromLedgerNumber<StellarDaemonConnection.GENESIS_LEDGER_NUMBER){
				startFromLedgerNumber=-1;
			}
			command.put("ledger_index_min", startFromLedgerNumber);
			command.put("ledger_index_max", -1);
			command.put("binary", true); //Default to false

			command.put("count", false);
			command.put("descending", false);
			command.put("offset", offset);
//			command.put("limit", 30); //Let the server choose it's own limits
			command.put("forward", false);
			return sendCommand(command, txHistory).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
}
