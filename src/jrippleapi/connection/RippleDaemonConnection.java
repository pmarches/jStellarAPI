package jrippleapi.connection;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import jrippleapi.beans.Account;
import jrippleapi.beans.AccountInformation;
import jrippleapi.beans.RippleAddress;
import jrippleapi.beans.TrustLines;
import jrippleapi.beans.CurrencyUnit;
import jrippleapi.beans.DenominatedIssuedCurrency;
import jrippleapi.beans.ExchangeOffers;
import jrippleapi.beans.OrderBook;
import jrippleapi.beans.RandomString;
import jrippleapi.beans.RippleSeedAddress;
import jrippleapi.beans.RippleTransaction;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebSocket
public class RippleDaemonConnection extends AbstractRippleMessageHandler {
	private int requestCounter=1;
	public final static URI RIPPLE_SERVER_URL=URI.create("wss://s1.ripple.com");
	public final static URI LOCALHOST_SERVER_URL=URI.create("ws://localhost:5006");
	JSONResponseHolder responseHolder = new JSONResponseHolder();
    
    public RippleDaemonConnection() throws Exception {
    	super(RIPPLE_SERVER_URL);
	}
    
	@OnWebSocketMessage
    public void onMessage(String msg) {
    	try {
			JSONObject jsonMessage = (JSONObject) new JSONParser().parse(msg);
			System.out.println("response:"+jsonMessage.toJSONString());
			if("response".equals(jsonMessage.get("type"))){
				responseHolder.setResponseContent(jsonMessage);
			}
			else if(jsonMessage.get("error")!=null){
				responseHolder.setResponseError(jsonMessage);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }

	public void sendString(String jsonString) throws IOException {
		System.out.println(jsonString);
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
		JSONObject accountInfoComand = new JSONObject();
		accountInfoComand.put("command", "ping");
		Future<GenericJSONSerializable> accountInfoResponse = sendCommand(accountInfoComand, new GenericJSONSerializable());
		try {
			return accountInfoResponse.get()!=null;
		} catch (InterruptedException | ExecutionException e) {
			return false;
		}
	}
	
	public Future<AccountInformation> getAccountInfoFuture(String account){
		JSONObject accountInfoComand = new JSONObject();
		accountInfoComand.put("command", "account_info");
		accountInfoComand.put("account", account);
		return sendCommand(accountInfoComand, new AccountInformation());
	}
	
	public AccountInformation getAccountInfo(String account){
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
	
	public Future<OrderBook> getOrderBookFuture(String takerGetsIssuerStr, CurrencyUnit takerGets, CurrencyUnit takerPays, int nbEntries){
		JSONObject jsonTakerGets = new JSONObject();
		if(takerGetsIssuerStr!=null){
			jsonTakerGets.put("issuer", takerGetsIssuerStr);
		}
		jsonTakerGets.put("currency", takerGets.currencyCode);

		JSONObject jsonTakerPays = new JSONObject();
//		if(takerPays.issuerStr!=null){
//			jsonTakerPays.put("issuer", takerPays.issuerStr);
//		}
		jsonTakerPays.put("currency", takerPays.currencyCode);

		JSONObject orderBookComand = new JSONObject();
		orderBookComand.put("command", "book_offers");
		orderBookComand.put("limit", nbEntries);
		orderBookComand.put("taker_gets", jsonTakerGets);
		orderBookComand.put("taker_pays", jsonTakerPays);
		orderBookComand.put("snapshot", true);

		return sendCommand(orderBookComand, new OrderBook());
	}

	public OrderBook getOrderBook(String issuerStr, CurrencyUnit takerGets, CurrencyUnit takerPays, int nbEntries) {
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
	
//	public void subscribeToLedgers(){
//		JSONObject command = new JSONObject();
//    	command.put("command", "subscribe");
//    	JSONArray streams = new JSONArray();
//    	streams.add("ledger");
//    	command.put("streams", streams);
//		sendCommand(command, new );
//	}
	
	public FutureJSONResponse<GenericJSONSerializable> sendPaymentFuture(Account payer, String payee, DenominatedIssuedCurrency amount){	
		JSONObject jsonTx = new RippleTransaction(payer.address.toString(), payee, amount).getTxJSON();
		JSONObject command = new JSONObject();
    	command.put("command", "submit");
    	command.put("tx_json", jsonTx);
    	command.put("secret", payer.secret.toString());
		return sendCommand(command, new GenericJSONSerializable());
	}
	
	public GenericJSONSerializable sendPayment(Account payer, String payee, DenominatedIssuedCurrency amount){
		try {
			return sendPaymentFuture(payer, payee, amount).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Future<GenericJSONSerializable> setCreditLineFuture(Account creditorAccount, RippleAddress debtorAccount, DenominatedIssuedCurrency creditAmount){
		JSONObject command = new JSONObject();
    	command.put("command", "submit");
    	JSONObject jsonTx = new JSONObject();
    	jsonTx.put("TransactionType", "TrustSet");
    	jsonTx.put("Account", creditorAccount.address.toString());
    	jsonTx.put("LimitAmount", creditAmount.toJSON());
    	
		command.put("tx_json", jsonTx);
    	command.put("secret", creditorAccount.secret.toString());
		return sendCommand(command, new GenericJSONSerializable());
	}

	public GenericJSONSerializable setTrustLine(Account creditorAccount, RippleAddress debtorAccount, DenominatedIssuedCurrency creditAmount){
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
	
	public Future<RippleTransaction> signTransactionFuture(RippleSeedAddress secret, RippleTransaction txToSign){
		JSONObject command = new JSONObject();
    	command.put("command", "sign");
    	command.put("secret", secret.toString());
		command.put("tx_json", txToSign.getTxJSON());
		return sendCommand(command, txToSign);
	}
	
	public RippleTransaction signTransaction(RippleSeedAddress secret, RippleTransaction txToSign) {
		try {
			return signTransactionFuture(secret, txToSign).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Future<RippleTransaction> submitTransactionFuture(RippleTransaction txToSign){
		if(txToSign.getSignedTxBlob()==null){
			throw new NullPointerException("Transaction must be signed before being submitted");
		}
		JSONObject command = new JSONObject();
    	command.put("command", "submit");
		command.put("tx_blob", txToSign.getSignedTxBlob());
		return sendCommand(command, txToSign);
	}
	
	public RippleTransaction submitTransaction(RippleTransaction txToSign) {
		try {
			return submitTransactionFuture(txToSign).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

}
