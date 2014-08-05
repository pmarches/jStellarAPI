package jstellarapi.keys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

import jstellarapi.connection.StellarAddressPublicInformation;
import jstellarapi.connection.StellarDaemonRPCConnection;
import jstellarapi.core.DenominatedIssuedCurrency;
import jstellarapi.core.StellarAddress;
import jstellarapi.core.StellarPaymentTransaction;
import jstellarapi.core.StellarSeedAddress;
import jstellarapi.serialization.StellarBinaryObject;
import jstellarapi.serialization.StellarBinarySerializer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class StellarWallet implements Serializable {
	private static final long serialVersionUID = -4849034810727882329L;

	transient File walletFile;
	StellarSeedAddress seed;
	int nextTransactionSequenceNumber;
	byte[] pendingTransaction;

	protected StellarWallet(StellarSeedAddress seed, int nextTransactionSequenceNumber, File walletFile) throws IOException{
		this.seed = seed;
		this.nextTransactionSequenceNumber = nextTransactionSequenceNumber;
		this.walletFile=walletFile;
	}

	static public StellarWallet createWallet(StellarSeedAddress seed, File walletFile) throws Exception {
		StellarDaemonRPCConnection conn = new StellarDaemonRPCConnection();
		StellarAddressPublicInformation publicInfo = conn.getPublicInformation(seed.getPublicStellarAddress());

		StellarWallet wallet = new StellarWallet(seed, (int) publicInfo.nextTransactionSequence, walletFile);
		wallet.saveWallet(walletFile);
		return wallet;
	}

	public StellarSeedAddress getSeed(){
		return seed;
	}
	
	public StellarWallet(File walletFile) throws Exception {
		this.walletFile=walletFile;
		if(walletFile.canWrite()==false){
			throw new RuntimeException("We will need to write to the wallet file");
		}

		StellarDaemonRPCConnection conn = new StellarDaemonRPCConnection();
		if(walletFile.canRead()){
			JSONObject root=(JSONObject) new JSONParser().parse(new FileReader(walletFile));
			seed=new StellarSeedAddress((String) root.get("master_seed"));

			if(false){
				long seqNumber=(Long)root.get("nextTransactionSequenceNumber");
				nextTransactionSequenceNumber=(int) seqNumber;
			}
			else{
				StellarAddressPublicInformation publicInfo = conn.getPublicInformation(seed.getPublicStellarAddress());
				nextTransactionSequenceNumber=(int) publicInfo.nextTransactionSequence;
			}
			
			if(root.containsKey("pendingTransaction")){
				String hexTX=(String) root.get("pendingTransaction");
				pendingTransaction=DatatypeConverter.parseHexBinary(hexTX);
				if(false){
					conn.submitTransaction(pendingTransaction);
				}
			}
		}
	}
	
	public StellarWallet() {
		seed=new StellarSeedAddress(new SecureRandom().generateSeed(16));
		nextTransactionSequenceNumber=0;
		pendingTransaction=null;
	}

	/**
	 * This is the all-in-one API, it constructs the TX, signs it, stores it, and submits it to the network 
	 * 
	 * @param STRAmount
	 * @param payee
	 * @throws Exception
	 */
	public void sendSTR(BigInteger STRAmount, StellarAddress payee) throws Exception{
		DenominatedIssuedCurrency amount = new DenominatedIssuedCurrency(new BigDecimal(STRAmount));
		StellarPaymentTransaction tx = new StellarPaymentTransaction(seed.getPublicStellarAddress(), payee, amount, this.nextTransactionSequenceNumber);
		//TODO Compute the required fee from the server_info
		tx.fee=new DenominatedIssuedCurrency(new BigDecimal("10"));
		StellarBinaryObject rbo = tx.getBinaryObject();
		rbo = new StellarSigner(seed.getPrivateKey(0)).sign(rbo);

		StellarDaemonRPCConnection conn = new StellarDaemonRPCConnection();
		byte[] signedTXBytes = new StellarBinarySerializer().writeBinaryObject(rbo).array();
		pendingTransaction = signedTXBytes;
		nextTransactionSequenceNumber++;
		saveWallet(walletFile);
		conn.submitTransaction(signedTXBytes);
		pendingTransaction = null;
		saveWallet(walletFile);
	}

	public void saveWallet(File saveToFile) throws IOException {
		JSONObject root=new JSONObject();
		root.put("address", seed.getPublicStellarAddress().toString());
		root.put("seed", seed.toString());
		root.put("nextTransactionSequenceNumber", nextTransactionSequenceNumber);
		if(pendingTransaction!=null){
			root.put("pendingTransaction", DatatypeConverter.printHexBinary(pendingTransaction));
		}
		File tempWalletFile = new File(saveToFile.getAbsolutePath()+".tmp");
		Writer writer=new FileWriter(tempWalletFile);
		root.writeJSONString(writer);
		writer.close();
		
		saveToFile.delete();
		tempWalletFile.renameTo(saveToFile);
	}
}
