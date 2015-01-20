package jstellarapi.connection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import jstellarapi.core.StellarAddress;
import jstellarapi.core.StellarEngineException;
import jstellarapi.core.StellarPaymentTransaction;
import jstellarapi.core.StellarSeedAddress;
import jstellarapi.ds.account.tx.AccountTx;
import jstellarapi.ds.account.tx.Balance;
import jstellarapi.ds.account.tx.BalanceAdapter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.GsonBuilder;

/**
 * This class should use only http POST/GET, not the websocket. For small
 * operations, (submit a TX), this could be faster than setting up a websocket
 * connection.
 *
 * @author pmarches
 */
public class StellarDaemonRPCConnection extends StellarDaemonConnection {
	protected URI stellarDaemonURI;
	public static URI STELLAR_RPC_URI = URI.create("http://test.stellar.org:9002");

	public StellarDaemonRPCConnection(URI StellarDaemonURI) throws Exception {
		this.stellarDaemonURI = StellarDaemonURI;
	}

	public StellarDaemonRPCConnection() throws Exception {
		this(StellarDaemonRPCConnection.STELLAR_RPC_URI);
	}

	public StellarPaymentTransaction submitTransaction(byte[] signedTransactionBytes) throws Exception {
		return submitTransaction(DatatypeConverter.printHexBinary(signedTransactionBytes));
	}

	public StellarPaymentTransaction submitTransaction(String tx_blob) throws Exception {
		JSONObject txBlob = new JSONObject();
		txBlob.put("tx_blob", tx_blob);
		JSONObject command = createJSONCommand("submit", txBlob);
		JSONObject response = executeJSONCommand(command);

		JSONObject result = (JSONObject) response.get("result");
		if ((Long) result.get("engine_result_code") != 0) {
			throw new StellarEngineException((String) result.get("engine_result"), (Long) result.get("engine_result_code"), (String) result.get("engine_result_message"));
		}
		StellarPaymentTransaction txToSign = new StellarPaymentTransaction(null, null, null, 0);
		txToSign.copyFrom(result);
		return txToSign;
	}

	protected JSONObject createJSONCommand(String commandName, JSONObject... parameterObjects) {
		JSONObject command = new JSONObject();
		command.put("method", commandName);
		JSONArray parameterArray = new JSONArray();
		command.put("params", parameterArray);
		parameterArray.addAll(Arrays.asList(parameterObjects));
		return command;
	}

	protected JSONObject executeJSONCommand(JSONObject command) throws Exception {
		String jsonString = command.toJSONString();

		HttpURLConnection connection = (HttpURLConnection) stellarDaemonURI.toURL().openConnection();
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Length", "" + jsonString.length());
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		OutputStream os = connection.getOutputStream();
		os.write(jsonString.getBytes());
		os.close();

		try {
			// TODO The response handling is probably common between the
			// websocket and the RPC
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			JSONObject response = (JSONObject) new JSONParser().parse(rd);
			rd.close();
			return response;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public StellarAddressPublicInformation getPublicInformation(StellarAddress StellarAddress) throws Exception {
		JSONObject account = new JSONObject();
		account.put("account", StellarAddress.toString());
		JSONObject command = createJSONCommand("account_info", account);
		JSONObject response = executeJSONCommand(command);
		StellarAddressPublicInformation info = new StellarAddressPublicInformation();
		info.copyFrom((JSONObject) response.get("result"));
		return info;
	}

	public TrustLines getTrustLines(StellarAddress StellarAddress) throws Exception {
		JSONObject account = new JSONObject();
		account.put("account", StellarAddress.toString());
		JSONObject command = createJSONCommand("account_lines", account);
		JSONObject response = executeJSONCommand(command);
		TrustLines info = new TrustLines();
		info.copyFrom((JSONObject) response.get("result"));
		return info;
	}

	public AccountTx getAccountTx(StellarAddress StellarAddress) throws Exception {
		return getAccountTx(StellarAddress, null, null, null, false);
	}

	public AccountTx getAccountTx(StellarAddress StellarAddress, Integer ledger_index_min, Integer ledger_index_max, Integer limit, boolean forward) throws Exception {
		JSONObject account = new JSONObject();
		account.put("account", StellarAddress.toString());
		if (ledger_index_min != null) {
			account.put("ledger_index_min", ledger_index_min);
		}
		if (ledger_index_max != null) {
			account.put("ledger_index_max", ledger_index_max);
		}
		if (limit != null) {
			account.put("limit", limit);
		}
		if (forward) {
			account.put("forward", true);
		}
		JSONObject command = createJSONCommand("account_tx", account);
		String jsonString = command.toJSONString();

		HttpURLConnection connection = (HttpURLConnection) stellarDaemonURI.toURL().openConnection();
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Length", "" + jsonString.length());
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		OutputStream os = connection.getOutputStream();
		os.write(jsonString.getBytes());
		os.close();

		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		while(rd.ready()){
			String line = rd.readLine();
			System.out.println(line);
		}
		AccountTx atx = new GsonBuilder().registerTypeAdapter(Balance.class, new BalanceAdapter()).create().fromJson(rd, AccountTx.class);
		rd.close();
		is.close();
		return atx;
	}
	
	public StellarPaymentTransaction signTransaction(StellarSeedAddress secret, StellarPaymentTransaction txToSign) throws Exception {
		JSONObject param = new JSONObject();
    	param.put("secret", secret.toString());
		param.put("tx_json", txToSign.toTxJSON());
		JSONObject command = createJSONCommand("sign", param);
		String jsonString = command.toJSONString();

		HttpURLConnection connection = (HttpURLConnection) stellarDaemonURI.toURL().openConnection();
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Length", "" + jsonString.length());
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		OutputStream os = connection.getOutputStream();
		os.write(jsonString.getBytes());
		os.close();

		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		JSONObject result = (JSONObject) new JSONParser().parse(rd);
		result = (JSONObject) result.get("result");
		String status = (String) result.get("status");
		if(!status.equals("success")){
			throw new IllegalStateException(status);
		}
		txToSign.copyFrom(result);
		return txToSign;
	}


}
