package jrippleapi.beans;

public class RipplePublicGeneratorAddress extends RippleIdentifier {

	public RipplePublicGeneratorAddress(byte[] payloadBytes) {
		super(payloadBytes, 41);
	}

}
