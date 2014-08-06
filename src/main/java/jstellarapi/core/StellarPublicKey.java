package jstellarapi.core;

import jstellarapi.keys.StellarDeterministicKeyGenerator;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.math.ec.ECPoint;

public class StellarPublicKey extends StellarIdentifier {
	//int accountId; //-1 means this public key is not deterministic?
	public StellarPublicKey(byte[] publicKeyBytes) {
		super(publicKeyBytes, 67);
		if(publicKeyBytes.length!=32){
			throw new RuntimeException("The public key must be of length 32 bytes was of length "+publicKeyBytes.length);
		}
	}
	
	public StellarAddress getAddress(){
		// Hashing of the publicKey is performed with a single SHA256 instead of
		// the typical Stellar HalfSHA512
		SHA256Digest sha256Digest = new SHA256Digest();
		sha256Digest.update(payloadBytes, 0, payloadBytes.length);
		byte[] sha256PubKeyBytes = new byte[32];
		sha256Digest.doFinal(sha256PubKeyBytes, 0);

		RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(sha256PubKeyBytes, 0, sha256PubKeyBytes.length);
        byte[] accountIdBytes = new byte[20];
        digest.doFinal(accountIdBytes, 0);

        return new StellarAddress(accountIdBytes);
	}
	
	public ECPoint getPublicPoint(){
		return StellarDeterministicKeyGenerator.SECP256K1_PARAMS.getCurve().decodePoint(payloadBytes);
	}
}
