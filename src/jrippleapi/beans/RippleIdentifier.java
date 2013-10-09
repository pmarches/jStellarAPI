package jrippleapi.beans;

import java.security.MessageDigest;
import java.security.Security;
import java.util.Arrays;

import jrippleapi.keys.RippleBase58;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RippleIdentifier {
	String humanReadableIdentifier;
	byte[] addressBytes;
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public RippleIdentifier(byte[] payloadBytes, int identifierType){
		try {
			addressBytes = payloadBytes;
			byte[] versionPayloadChecksumBytes=new byte[1+payloadBytes.length+4];
			versionPayloadChecksumBytes[0]=(byte) identifierType;
			System.arraycopy(payloadBytes, 0, versionPayloadChecksumBytes, 1, payloadBytes.length);

			MessageDigest mda = MessageDigest.getInstance("SHA-256", "BC");
			mda.update(versionPayloadChecksumBytes, 0, 1+payloadBytes.length);
			byte[] firstHash = mda.digest();
			mda.reset();
			System.arraycopy(mda.digest(firstHash ), 0, versionPayloadChecksumBytes, 1+payloadBytes.length, 4);
			humanReadableIdentifier=RippleBase58.encode(versionPayloadChecksumBytes);
		} catch (Exception e) {
			throw new RuntimeException("message digest exception", e);
		}
	}
	
	public RippleIdentifier(String stringID) {
		this.humanReadableIdentifier = stringID;
		byte[] stridBytes = RippleBase58.decode(stringID);
		addressBytes = Arrays.copyOfRange(stridBytes, 1, 21);
	}

	@Override
	public String toString() {
		return humanReadableIdentifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(addressBytes);
		result = prime
				* result
				+ ((humanReadableIdentifier == null) ? 0
						: humanReadableIdentifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RippleIdentifier other = (RippleIdentifier) obj;
		if (!Arrays.equals(addressBytes, other.addressBytes))
			return false;
		if (humanReadableIdentifier == null) {
			if (other.humanReadableIdentifier != null)
				return false;
		} else if (!humanReadableIdentifier
				.equals(other.humanReadableIdentifier))
			return false;
		return true;
	}

}
