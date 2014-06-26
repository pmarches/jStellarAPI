package jrippleapi.core;

import jrippleapi.keys.RippleDeterministicKeyGenerator;

public class RippleSeedAddress extends RippleIdentifier {
	private static final long serialVersionUID = 1845189349528742766L;

	public RippleSeedAddress(byte[] payloadBytes) {
		super(payloadBytes, 33);
		if(payloadBytes.length!=16){
			throw new RuntimeException("The seed bytes should be 16 bytes in length. They can be random bytes, or the first 128bit of the SHA512 hash of the passphrase");
		}
	}
	
	public RippleSeedAddress(String stringID) {
		super(stringID);
	}

	public RipplePrivateKey getPrivateKey(int accountNumber) {
		RippleDeterministicKeyGenerator generator = new RippleDeterministicKeyGenerator(payloadBytes);
		RipplePrivateKey signingPrivateKey = generator.getAccountPrivateKey(accountNumber);
//		ECPoint publicSigningKey = generator.getAccountPublicPoint(accountNumber);
//		String pubKeyStr=DatatypeConverter.printHexBinary(publicSigningKey.getEncoded());
//		System.out.println("pubkey "+pubKeyStr);
		return signingPrivateKey;
	}

	public RippleAddress getPublicRippleAddress() {
		return getPrivateKey(0).getPublicKey().getAddress();
	}
}
