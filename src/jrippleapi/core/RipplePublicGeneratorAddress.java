package jrippleapi.core;

public class RipplePublicGeneratorAddress extends RippleIdentifier {

	public RipplePublicGeneratorAddress(byte[] payloadBytes) {
		super(payloadBytes, 41);
	}

}
