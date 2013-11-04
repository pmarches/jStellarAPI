package jrippleapi.keys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import jrippleapi.connection.RippleAddressPublicInformation;
import jrippleapi.connection.RippleDaemonRPCConnection;
import jrippleapi.core.DenominatedIssuedCurrency;
import jrippleapi.core.RippleAddress;
import jrippleapi.core.RipplePaymentTransaction;
import jrippleapi.core.RippleSeedAddress;
import jrippleapi.serialization.RippleBinaryObject;
import jrippleapi.serialization.RippleBinarySerializer;

public class RippleWallet implements Serializable {
	private static final long serialVersionUID = -4849034810727882329L;

	transient File walletFile;
	RippleSeedAddress seed;
	int lastTransactionSequenceNumber;
	byte[] pendingTransaction;

	public RippleWallet(RippleSeedAddress seed, File walletFile) throws IOException{
		this(seed, -1, walletFile);
	}

	public RippleWallet(RippleSeedAddress seed, int lastTransactionSequenceNumber, File walletFile) throws IOException{
		this.seed = seed;
		this.lastTransactionSequenceNumber = lastTransactionSequenceNumber;
		this.walletFile=walletFile;

		if(lastTransactionSequenceNumber<0){
			try {
				RippleDaemonRPCConnection conn = new RippleDaemonRPCConnection();
				RippleAddressPublicInformation publicInfo = conn.getPublicInformation(seed.getPublicRippleAddress());
				this.lastTransactionSequenceNumber=(int) publicInfo.lastTransactionSequence;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		//TODO recover any pending transactions
		saveWallet(walletFile);
	}
	
	public RippleWallet(File walletFile) throws Exception {
		this.walletFile=walletFile;
		if(walletFile.canWrite()==false){
			throw new RuntimeException("We will need to write to the wallet file");
		}
		if(walletFile.canRead()){
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(walletFile));
			seed = (RippleSeedAddress) ois.readObject();
			lastTransactionSequenceNumber = ois.readInt();
			pendingTransaction=(byte[]) ois.readObject();
			ois.close();
		}
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
		lastTransactionSequenceNumber++;
		RipplePaymentTransaction tx = new RipplePaymentTransaction(seed.getPublicRippleAddress(), payee, amount, this.lastTransactionSequenceNumber);
		RippleBinaryObject rbo = tx.getBinaryObject();
		rbo = new RippleSigner(seed.getPrivateKey(0)).sign(rbo);

		RippleDaemonRPCConnection conn = new RippleDaemonRPCConnection();
		byte[] signedTXBytes = new RippleBinarySerializer().writeBinaryObject(rbo).array();
		pendingTransaction = signedTXBytes;
		saveWallet(walletFile);
		conn.submitTransaction(signedTXBytes);
		pendingTransaction = null;
		saveWallet(walletFile);
	}

	public void saveWallet(File saveToFile) throws IOException {
		File tempWalletFile = new File(saveToFile.getAbsolutePath()+".tmp");
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempWalletFile));
		oos.writeObject(seed);
		oos.writeInt(lastTransactionSequenceNumber);
		oos.writeObject(pendingTransaction);
		oos.close();

		saveToFile.delete();
		tempWalletFile.renameTo(saveToFile);
	}
}
