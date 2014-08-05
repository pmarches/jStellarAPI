package jstellarapi.core;

import jstellarapi.keys.StellarDeterministicKeyGenerator;

public class StellarSeedAddress extends StellarIdentifier {
	private static final long serialVersionUID = 1845189349528742766L;

	public StellarSeedAddress(byte[] payloadBytes) {
		super(payloadBytes, 33);
		if(payloadBytes.length!=16){
			throw new RuntimeException("The seed bytes should be 16 bytes in length. They can be random bytes, or the first 128bit of the SHA512 hash of the passphrase");
		}
	}
	
	public StellarSeedAddress(String stringID) {
		super(stringID);
	}

	public StellarPrivateKey getPrivateKey(int accountNumber) {
		StellarDeterministicKeyGenerator generator = new StellarDeterministicKeyGenerator(payloadBytes);
		StellarPrivateKey signingPrivateKey = generator.getAccountPrivateKey(accountNumber);
//		ECPoint publicSigningKey = generator.getAccountPublicPoint(accountNumber);
//		String pubKeyStr=DatatypeConverter.printHexBinary(publicSigningKey.getEncoded());
//		System.out.println("pubkey "+pubKeyStr);
		return signingPrivateKey;
	}

	public StellarAddress getPublicStellarAddress() {
		return getPrivateKey(0).getPublicKey().getAddress();
	}
}
