package jrippleapi.core;

import java.security.MessageDigest;
import java.security.Security;
import java.util.Arrays;

import jrippleapi.keys.RippleBase58;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RippleIdentifier {
	String humanReadableIdentifier;
	byte[] addressBytes;
	int identifierType;
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public RippleIdentifier(byte[] payloadBytes, int identifierType){
		addressBytes = payloadBytes;
		this.identifierType = identifierType;
	}
	
	public RippleIdentifier(String stringID) {
		this.humanReadableIdentifier = stringID;
		byte[] stridBytes = RippleBase58.decode(stringID);
		addressBytes = Arrays.copyOfRange(stridBytes, 1, stridBytes.length-4);
		identifierType = stridBytes[0]; //TODO check the checksum 
	}

	@Override
	public String toString() {
		if(humanReadableIdentifier==null){
			byte[] versionPayloadChecksumBytes=new byte[1+addressBytes.length+4];
			versionPayloadChecksumBytes[0]=(byte) identifierType;
			System.arraycopy(addressBytes, 0, versionPayloadChecksumBytes, 1, addressBytes.length);

			try {
				MessageDigest mda = MessageDigest.getInstance("SHA-256", "BC");
				mda.update(versionPayloadChecksumBytes, 0, 1+addressBytes.length);
				byte[] firstHash = mda.digest();
				mda.reset();
				System.arraycopy(mda.digest(firstHash ), 0, versionPayloadChecksumBytes, 1+addressBytes.length, 4);
				humanReadableIdentifier=RippleBase58.encode(versionPayloadChecksumBytes);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return humanReadableIdentifier;
	}

	public byte[] getBytes() {
		return addressBytes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(addressBytes);
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
		return true;
	}

}
