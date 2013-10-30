package jrippleapi.core;

public class RipplePublickKeyAddress extends RippleIdentifier {

	public RipplePublickKeyAddress(byte[] payloadBytes) {
		super(payloadBytes, 35);
	}

}
