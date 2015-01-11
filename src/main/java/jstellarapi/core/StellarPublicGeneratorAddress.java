package jstellarapi.core;

public class StellarPublicGeneratorAddress extends StellarIdentifier {
	public StellarPublicGeneratorAddress(byte[] payloadBytes) {
		super(payloadBytes, 41);
	}

}
