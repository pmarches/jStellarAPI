package jrippleapi.keys;

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

import jrippleapi.connection.RippleAddressPublicInformation;
import jrippleapi.connection.RippleDaemonRPCConnection;
import jrippleapi.core.DenominatedIssuedCurrency;
import jrippleapi.core.RippleAddress;
import jrippleapi.core.RipplePaymentTransaction;
import jrippleapi.core.RippleSeedAddress;
import jrippleapi.serialization.RippleBinaryObject;
import jrippleapi.serialization.RippleBinarySerializer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RippleWallet implements Serializable {
	private static final long serialVersionUID = -4849034810727882329L;

	transient File walletFile;
	RippleSeedAddress seed;
	int nextTransactionSequenceNumber;
	byte[] pendingTransaction;

	protected RippleWallet(RippleSeedAddress seed, int nextTransactionSequenceNumber, File walletFile) throws IOException{
		this.seed = seed;
		this.nextTransactionSequenceNumber = nextTransactionSequenceNumber;
		this.walletFile=walletFile;
	}

	static public RippleWallet createWallet(RippleSeedAddress seed, File walletFile) throws Exception {
		RippleDaemonRPCConnection conn = new RippleDaemonRPCConnection();
		RippleAddressPublicInformation publicInfo = conn.getPublicInformation(seed.getPublicRippleAddress());

		RippleWallet wallet = new RippleWallet(seed, (int) publicInfo.nextTransactionSequence, walletFile);
		wallet.saveWallet(walletFile);
		return wallet;
	}

	public RippleSeedAddress getSeed(){
		return seed;
	}
	
	public RippleWallet(File walletFile) throws Exception {
		this.walletFile=walletFile;
		if(walletFile.canWrite()==false){
			throw new RuntimeException("We will need to write to the wallet file");
		}

		RippleDaemonRPCConnection conn = new RippleDaemonRPCConnection();
		if(walletFile.canRead()){
			JSONObject root=(JSONObject) new JSONParser().parse(new FileReader(walletFile));
			seed=new RippleSeedAddress((String) root.get("seed"));

			if(false){
				long seqNumber=(Long)root.get("nextTransactionSequenceNumber");
				nextTransactionSequenceNumber=(int) seqNumber;
			}
			else{
				RippleAddressPublicInformation publicInfo = conn.getPublicInformation(seed.getPublicRippleAddress());
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
	
	public RippleWallet() {
		seed=new RippleSeedAddress(new SecureRandom().generateSeed(16));
		nextTransactionSequenceNumber=0;
		pendingTransaction=null;
	}

	/**
	 * This is the all-in-one API, it constructs the TX, signs it, stores it, and submits it to the network 
	 * 
	 * @param xrpAmount
	 * @param payee
	 * @throws Exception
	 */
	public void sendXRP(BigInteger xrpAmount, RippleAddress payee) throws Exception{
		DenominatedIssuedCurrency amount = new DenominatedIssuedCurrency(new BigDecimal(xrpAmount));
		RipplePaymentTransaction tx = new RipplePaymentTransaction(seed.getPublicRippleAddress(), payee, amount, this.nextTransactionSequenceNumber);
		//TODO Compute the required fee from the server_info
		tx.fee=new DenominatedIssuedCurrency(new BigDecimal("10"));
		RippleBinaryObject rbo = tx.getBinaryObject();
		rbo = new RippleSigner(seed.getPrivateKey(0)).sign(rbo);

		RippleDaemonRPCConnection conn = new RippleDaemonRPCConnection();
		byte[] signedTXBytes = new RippleBinarySerializer().writeBinaryObject(rbo).array();
		pendingTransaction = signedTXBytes;
		nextTransactionSequenceNumber++;
		saveWallet(walletFile);
		conn.submitTransaction(signedTXBytes);
		pendingTransaction = null;
		saveWallet(walletFile);
	}

	public void saveWallet(File saveToFile) throws IOException {
		JSONObject root=new JSONObject();
		root.put("address", seed.getPublicRippleAddress().toString());
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
