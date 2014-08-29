package jstellarapi.core;

import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;

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
			EdDSAPrivateKeySpec keySpec=new EdDSAPrivateKeySpec(payloadBytes, StellarPublicKey.ed25519);
			byte[] publicKeyBytes=keySpec.getA().toByteArray();
			byte[] privateBytes=keySpec.geta();
			privateKey=new StellarPrivateKey(privateBytes, publicKeyBytes);
		}
		return privateKey;
	}

	public StellarAddress getPublicStellarAddress() {
		return getPrivateKey().getPublicKey().getAddress();
	}
}
