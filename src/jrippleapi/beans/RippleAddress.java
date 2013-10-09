package jrippleapi.beans;


public class RippleAddress extends RippleIdentifier {

	public RippleAddress(byte[] payloadBytes) {
		super(payloadBytes, 0);
	}

	public RippleAddress(String string) {
		super(string);
	}
}
