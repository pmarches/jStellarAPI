package jstellarapi.core;


public class StellarAddress extends StellarIdentifier {

//	public static final StellarAddress STELLAR_ROOT_ACCOUNT=new StellarAddress("gHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh");
//	public static final StellarAddress STELLAR_ADDRESS_ZERO=new StellarAddress("grrrrrrrrrrrrrrrrrrrrhoLvTp");
//	public static final StellarAddress STELLAR_ADDRESS_ONE=new StellarAddress("grrrrrrrrrrrrrrrrrrrBZbvji");
//	public static final StellarAddress STELLAR_ADDRESS_NEUTRAL=Stellar_ADDRESS_ONE;
//	public static final StellarAddress STELLAR_ADDRESS_NAN=new StellarAddress("grrrrrrrrrrrrrrrrrrn5RM1rHd");
	public static final StellarAddress STELLAR_ADDRESS_JSTELLARAPI=new StellarAddress("gB2ZjFkenMnZLGZHEckAXn7xzTpn1omFti");
	public static final StellarAddress STELLAR_ADDRESS_PMARCHES=new StellarAddress("gDh15vDi6gN4ZXvxNPoo2wZ2KNmMRBGto1");

	public StellarAddress(byte[] payloadBytes) {
		super(payloadBytes, 0);
	}

	public StellarAddress(String string) {
		super(string);
	}
}
