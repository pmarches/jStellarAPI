package jrippleapi.keys;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

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

	public RippleDeterministicKeyGenerator(String secretSeed) {
		byte[] secretBytes = Base58.decode(secretSeed);
		//TODO check the checksum
		seedBytes = Arrays.copyOfRange(secretBytes, 1, 17);
	}

	public RippleDeterministicKeyGenerator(byte[] bytesSeed) {
		if(bytesSeed.length!=16){
			throw new RuntimeException("The seed size should be 128 bit, was "+bytesSeed.length*8);
		}
		this.seedBytes = bytesSeed;
	}

	public static byte[] halfSHA512(byte[] bytesToHash) throws Exception {
		MessageDigest sha512Digest = MessageDigest.getInstance("SHA-512", "BC");
		byte [] bytesHash = sha512Digest.digest(bytesToHash);
		byte[] first256BitsOfHash = Arrays.copyOf(bytesHash, 32);
		return first256BitsOfHash;
	}

	public String getHumandReadableSeed() throws Exception {
		return toRippleHumanReadable(seedBytes, (byte) 33);
	}

	//See https://ripple.com/wiki/Encodings
	protected String toRippleHumanReadable(byte[] payloadBytes, byte version) throws Exception {
		byte[] versionPayloadChecksumBytes=new byte[1+payloadBytes.length+4];
		versionPayloadChecksumBytes[0]=version;
		System.arraycopy(payloadBytes, 0, versionPayloadChecksumBytes, 1, payloadBytes.length);

		MessageDigest mda = MessageDigest.getInstance("SHA-256", "BC");
		mda.update(versionPayloadChecksumBytes, 0, 1+payloadBytes.length);
		byte[] firstHash = mda.digest();
		mda.reset();
		System.arraycopy(mda.digest(firstHash ), 0, versionPayloadChecksumBytes, 1+payloadBytes.length, 4);
		
		return Base58.encode(versionPayloadChecksumBytes);
	}

	public byte[] getPrivateGeneratorBytes() throws Exception{
		for(int seq=0;; seq++){
			byte[] seqBytes = ByteBuffer.allocate(4).putInt(seq).array();
			byte[] seedAndSeqBytes = Arrays.concatenate(seedBytes, seqBytes);
			byte[] privateGenerator = halfSHA512(seedAndSeqBytes);
			BigInteger hashOfSeedAndSEQ = new BigInteger(1, privateGenerator);
			
	        if(hashOfSeedAndSEQ.compareTo(ecParams.getN()) ==-1){
	        	return privateGenerator; //We return the byte[] instead of the BigInteger because the toArray of BigInt allocates only the minimal number of bytes to represent the value.
	        }
		}
	}

	public ECPoint getPublicGeneratorPoint() throws Exception {
		byte[] privateGeneratorBytes = getPrivateGeneratorBytes();
        ECPoint publicGenerator = privateBigIntegerToPoint(new BigInteger(1, privateGeneratorBytes));
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
		
        ECPoint accountPublicKeyPoint = publicGeneratorPoint.add(privateBigIntegerToPoint(pubGenSeqSubSeqHashBI));
        return accountPublicKeyPoint;
	}

	private ECPoint privateBigIntegerToPoint(BigInteger pubGenSeqSubSeqHashBI) {
		ECPoint uncompressed= ecParams.getG().multiply(pubGenSeqSubSeqHashBI);
        return new ECPoint.Fp(ecParams.getCurve(), uncompressed.getX(), uncompressed.getY(), true);
	}

	public String getAccountPublicKey(int accountNumber) throws Exception {
		return toRippleHumanReadable(getAccountPublicBytes(accountNumber), (byte) 35);
	}

	private byte[] getAccountPublicBytes(int accountNumber) throws Exception {
		ECPoint publicKey = getAccountPublicPoint(accountNumber);
		return publicKey.getEncoded();
	}

	public String getAccountId(int accountNumber) throws Exception {
		byte[] publicKeyBytes = getAccountPublicBytes(accountNumber);
		//Hashing of the publicKey is performed with a single SHA256 instead of the typical ripple HalfSHA512
        MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
        byte[] sha256PubKeyBytes = sha256Digest.digest(publicKeyBytes);
        
		RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(sha256PubKeyBytes, 0, sha256PubKeyBytes.length);
        byte[] accountIdBytes = new byte[20];
        digest.doFinal(accountIdBytes, 0);

        return toRippleHumanReadable(accountIdBytes, (byte) 0);
	}

	public String getPublicGeneratorFamily() throws Exception {
		byte[] publicGeneratorBytes = getPublicGeneratorPoint().getEncoded();
		return toRippleHumanReadable(publicGeneratorBytes, (byte) 41);
	}
	
}
