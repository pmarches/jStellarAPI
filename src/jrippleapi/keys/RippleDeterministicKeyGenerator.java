package jrippleapi.keys;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

import jrippleapi.beans.RippleAddress;
import jrippleapi.beans.RipplePublicGeneratorAddress;
import jrippleapi.beans.RipplePublickKeyAddress;
import jrippleapi.beans.RippleSeedAddress;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.Arrays;

public class RippleDeterministicKeyGenerator {
	protected static ECDomainParameters ecParams;
	protected byte[] seedBytes;

	static {
        X9ECParameters params = SECNamedCurves.getByName("secp256k1");
        ecParams = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
	}

	public RippleDeterministicKeyGenerator(RippleSeedAddress secret) {
		seedBytes = secret.getBytes();
	}

	public RippleDeterministicKeyGenerator(byte[] bytesSeed) {
		if(bytesSeed.length!=16){
			throw new RuntimeException("The seed size should be 128 bit, was "+bytesSeed.length*8);
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

	public byte[] getPrivateRootKeyBytes() throws Exception{
		for(int seq=0;; seq++){
			byte[] seqBytes = ByteBuffer.allocate(4).putInt(seq).array();
			byte[] seedAndSeqBytes = Arrays.concatenate(seedBytes, seqBytes);
			byte[] privateGeneratorBytes = halfSHA512(seedAndSeqBytes);
			BigInteger privateGeneratorBI = new BigInteger(1, privateGeneratorBytes);
			
	        if(privateGeneratorBI.compareTo(ecParams.getN()) ==-1){
	        	return privateGeneratorBytes; //We return the byte[] instead of the BigInteger because the toArray of BigInt allocates only the minimal number of bytes to represent the value.
	        }
		}
	}

	public ECPoint getPublicGeneratorPoint() throws Exception {
		byte[] privateGeneratorBytes = getPrivateRootKeyBytes();
        ECPoint publicGenerator = privateBigIntegerToPublicPoint(new BigInteger(1, privateGeneratorBytes));
		return publicGenerator;
	}

	public ECPoint getAccountPublicPoint(int accountNumber) throws Exception {
		ECPoint publicGeneratorPoint = getPublicGeneratorPoint();
		byte[] publicGeneratorBytes = publicGeneratorPoint.getEncoded();
		byte[] accountNumberBytes = ByteBuffer.allocate(4).putInt(accountNumber).array();
		BigInteger pubGenSeqSubSeqHashBI;
		for(int subSequence=0;; subSequence++){
			byte[] subSequenceBytes = ByteBuffer.allocate(4).putInt(subSequence).array();
			byte[] pubGenAccountSubSeqBytes = Arrays.concatenate(publicGeneratorBytes, accountNumberBytes, subSequenceBytes);
			byte[] publicGeneratorAccountSeqHashBytes = halfSHA512(pubGenAccountSubSeqBytes);

			pubGenSeqSubSeqHashBI = new BigInteger(1, publicGeneratorAccountSeqHashBytes);
	        if(pubGenSeqSubSeqHashBI.compareTo(ecParams.getN()) ==-1){
	        	break;
	        }
		}
		
        ECPoint accountPublicKeyPoint = publicGeneratorPoint.add(privateBigIntegerToPublicPoint(pubGenSeqSubSeqHashBI));
        return accountPublicKeyPoint;
	}

	public BigInteger getAccountPrivateKeyBI(int accountNumber) throws Exception {
		BigInteger privateRootKeyBI = new BigInteger(1, getPrivateRootKeyBytes());
		//TODO factor out the common part with the public key
		ECPoint publicGeneratorPoint = getPublicGeneratorPoint();
		byte[] publicGeneratorBytes = publicGeneratorPoint.getEncoded();
		byte[] accountNumberBytes = ByteBuffer.allocate(4).putInt(accountNumber).array();
		BigInteger pubGenSeqSubSeqHashBI;
		for(int subSequence=0;; subSequence++){
			byte[] subSequenceBytes = ByteBuffer.allocate(4).putInt(subSequence).array();
			byte[] pubGenAccountSubSeqBytes = Arrays.concatenate(publicGeneratorBytes, accountNumberBytes, subSequenceBytes);
			byte[] publicGeneratorAccountSeqHashBytes = halfSHA512(pubGenAccountSubSeqBytes);

			pubGenSeqSubSeqHashBI = new BigInteger(1, publicGeneratorAccountSeqHashBytes);
	        if(pubGenSeqSubSeqHashBI.compareTo(privateRootKeyBI) ==-1 && !pubGenSeqSubSeqHashBI.equals(BigInteger.ZERO)){
	        	break;
	        }
		}
		BigInteger privateKeyForAccount = privateRootKeyBI.add(pubGenSeqSubSeqHashBI).mod(ecParams.getN());
        return privateKeyForAccount;
	}

	private ECPoint privateBigIntegerToPublicPoint(BigInteger privateBI) {
		ECPoint uncompressed= ecParams.getG().multiply(privateBI);
        return new ECPoint.Fp(ecParams.getCurve(), uncompressed.getX(), uncompressed.getY(), true);
	}

	public RipplePublickKeyAddress getAccountPublicKey(int accountNumber) throws Exception {
		return new RipplePublickKeyAddress(getAccountPublicBytes(accountNumber));
	}

	private byte[] getAccountPublicBytes(int accountNumber) throws Exception {
		ECPoint publicKey = getAccountPublicPoint(accountNumber);
		return publicKey.getEncoded();
	}

	public RippleAddress getAccountId(int accountNumber) throws Exception {
		byte[] publicKeyBytes = getAccountPublicBytes(accountNumber);
		//Hashing of the publicKey is performed with a single SHA256 instead of the typical ripple HalfSHA512
        MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
        byte[] sha256PubKeyBytes = sha256Digest.digest(publicKeyBytes);
        
		RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(sha256PubKeyBytes, 0, sha256PubKeyBytes.length);
        byte[] accountIdBytes = new byte[20];
        digest.doFinal(accountIdBytes, 0);

        return new RippleAddress(accountIdBytes);
	}

	public RipplePublicGeneratorAddress getPublicGeneratorFamily() throws Exception {
		byte[] publicGeneratorBytes = getPublicGeneratorPoint().getEncoded();
		return new RipplePublicGeneratorAddress(publicGeneratorBytes);
	}
	
}
