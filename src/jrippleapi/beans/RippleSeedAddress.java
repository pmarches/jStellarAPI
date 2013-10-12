package jrippleapi.beans;

public class RippleSeedAddress extends RippleIdentifier {

	public RippleSeedAddress(byte[] payloadBytes) {
		super(payloadBytes, 33);
	}
	
	public RippleSeedAddress(String stringID) {
		super(stringID);
	}
}
