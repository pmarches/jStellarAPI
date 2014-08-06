package jstellarapi.core;

import java.util.Arrays;

import org.abstractj.kalium.NaCl;

public class StellarSeedAddress extends StellarIdentifier {
	private static final long serialVersionUID = 1845189349528742766L;
	protected StellarPrivateKey privateKey;
	
	public StellarSeedAddress(byte[] payloadBytes) {
		super(payloadBytes, 33);
		if(payloadBytes.length!=32){
			throw new RuntimeException("The seed bytes should be 32 bytes in length. They can be random bytes, or the first 256bit of the SHA512 hash of the passphrase");
		}
	}
	
	public StellarSeedAddress(String stringID) {
		super(stringID);
	}

	public StellarPrivateKey getPrivateKey() {
		if(privateKey==null){
			byte[] publicBytes=new byte[NaCl.Sodium.PUBLICKEY_BYTES];
			byte[] privateBytesWithOverflowCompensation=new byte[NaCl.Sodium.SECRETKEY_BYTES*2]; //Wow, buffer overflow
			if(0!=NaCl.sodium().crypto_sign_ed25519_seed_keypair(publicBytes, privateBytesWithOverflowCompensation, payloadBytes)){
				throw new Error("crypto_sign_ed25519_seed_keypair failed");
			}
			byte[] privateBytes=Arrays.copyOf(privateBytesWithOverflowCompensation, NaCl.Sodium.SECRETKEY_BYTES);
			privateKey=new StellarPrivateKey(privateBytes, publicBytes);
		}
		return privateKey;
	}

	public StellarAddress getPublicStellarAddress() {
		return getPrivateKey().getPublicKey().getAddress();
	}
}
