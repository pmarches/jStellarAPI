package jstellarapi.keys;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.Security;

import jstellarapi.core.StellarPrivateKey;
import jstellarapi.core.StellarPublicGeneratorAddress;
import jstellarapi.core.StellarPublicKey;
import jstellarapi.core.StellarSeedAddress;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.Arrays;

public class StellarDeterministicKeyGenerator {
	public static ECDomainParameters SECP256K1_PARAMS;
	protected byte[] seedBytes;
	
	static {
//		ECGenParameterSpec ecSpec = new ECGenParameterSpec("SECp256k1");
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        X9ECParameters params = SECNamedCurves.getByName("secp256k1");
        SECP256K1_PARAMS = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
	}

	public StellarDeterministicKeyGenerator(StellarSeedAddress secret) {
		this(secret.getBytes());
	}

	public StellarDeterministicKeyGenerator(byte[] bytesSeed) {
		if(bytesSeed.length!=32){
			throw new RuntimeException("The seed size should be 256 bit, was "+bytesSeed.length*8);
		}
		this.seedBytes = bytesSeed;
	}

	public static byte[] halfSHA512(byte[] bytesToHash) {
		try {
			MessageDigest sha512Digest = MessageDigest.getInstance("SHA-512", "BC");
			byte [] bytesHash = sha512Digest.digest(bytesToHash);
			byte[] first256BitsOfHash = Arrays.copyOf(bytesHash, 32);
			return first256BitsOfHash;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected byte[] getPrivateRootKeyBytes() {
		for(int seq=0;; seq++){
			byte[] seqBytes = ByteBuffer.allocate(4).putInt(seq).array();
			byte[] seedAndSeqBytes = Arrays.concatenate(seedBytes, seqBytes);
			byte[] privateGeneratorBytes = halfSHA512(seedAndSeqBytes);
			BigInteger privateGeneratorBI = new BigInteger(1, privateGeneratorBytes);
			
	        if(privateGeneratorBI.compareTo(SECP256K1_PARAMS.getN()) ==-1){
	        	return privateGeneratorBytes; //We return the byte[] instead of the BigInteger because the toArray of BigInt allocates only the minimal number of bytes to represent the value.
	        }
		}
	}

	//PublicGenerator is also known as PublicRootKey
	protected ECPoint getPublicGeneratorPoint() {
		byte[] privateGeneratorBytes = getPrivateRootKeyBytes();
        ECPoint publicGenerator = new StellarPrivateKey(privateGeneratorBytes).getPublicKey().getPublicPoint();
		return publicGenerator;
	}

	public StellarPrivateKey getAccountPrivateKey(int accountNumber) {
		BigInteger privateRootKeyBI = new BigInteger(1, getPrivateRootKeyBytes());
		//TODO factor out the common part with the public key
		ECPoint publicGeneratorPoint = getPublicGeneratorPoint();
		byte[] publicGeneratorBytes = publicGeneratorPoint.getEncoded();
		byte[] accountNumberBytes = ByteBuffer.allocate(4).putInt(accountNumber).array();
		BigInteger pubGenSeqSubSeqHashBI;
		for(int subSequence=0;; subSequence++){
			byte[] subSequenceBytes = ByteBuffer.allocate(4).putInt(subSequence).array(); //FIXME Avoid pointless concatenation or arrays everywhere
			byte[] pubGenAccountSubSeqBytes = Arrays.concatenate(publicGeneratorBytes, accountNumberBytes, subSequenceBytes);
			byte[] publicGeneratorAccountSeqHashBytes = halfSHA512(pubGenAccountSubSeqBytes);

			pubGenSeqSubSeqHashBI = new BigInteger(1, publicGeneratorAccountSeqHashBytes);
	        if(pubGenSeqSubSeqHashBI.compareTo(SECP256K1_PARAMS.getN()) ==-1 && !pubGenSeqSubSeqHashBI.equals(BigInteger.ZERO)){
	        	break;
	        }
		}
		BigInteger privateKeyForAccount = privateRootKeyBI.add(pubGenSeqSubSeqHashBI).mod(SECP256K1_PARAMS.getN());
        return new StellarPrivateKey(privateKeyForAccount);
	}

	public StellarPublicKey getAccountPublicKey(int accountNumber) {
		//FIXME This method should be able to generate public addresses from the publicGenerator only (Deterministic watch only addresses)
		ECPoint publicGeneratorPoint = getPublicGeneratorPoint();
		byte[] publicGeneratorBytes = publicGeneratorPoint.getEncoded();
		byte[] accountNumberBytes = ByteBuffer.allocate(4).putInt(accountNumber).array();
		byte[] publicGeneratorAccountSeqHashBytes;
		for(int subSequence=0;; subSequence++){
			byte[] subSequenceBytes = ByteBuffer.allocate(4).putInt(subSequence).array();
			byte[] pubGenAccountSubSeqBytes = Arrays.concatenate(publicGeneratorBytes, accountNumberBytes, subSequenceBytes);
			publicGeneratorAccountSeqHashBytes = halfSHA512(pubGenAccountSubSeqBytes);

			BigInteger pubGenSeqSubSeqHashBI = new BigInteger(1, publicGeneratorAccountSeqHashBytes);
	        if(pubGenSeqSubSeqHashBI.compareTo(SECP256K1_PARAMS.getN()) ==-1){
	        	break;
	        }
		}
		ECPoint temporaryPublicPoint = new StellarPrivateKey(publicGeneratorAccountSeqHashBytes).getPublicKey().getPublicPoint();
        ECPoint accountPublicKeyPoint = publicGeneratorPoint.add(temporaryPublicPoint);
		byte[] publicKeyBytes = accountPublicKeyPoint.getEncoded();
		return new StellarPublicKey(publicKeyBytes);
	}

	public StellarPublicGeneratorAddress getPublicGeneratorFamily() throws Exception {
		byte[] publicGeneratorBytes = getPublicGeneratorPoint().getEncoded();
		return new StellarPublicGeneratorAddress(publicGeneratorBytes);
	}
	
}
