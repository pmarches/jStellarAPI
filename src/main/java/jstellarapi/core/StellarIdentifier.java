package jstellarapi.core;

import java.io.Serializable;
import java.util.Arrays;

import jstellarapi.keys.StellarBase58;

import org.bouncycastle.crypto.digests.SHA256Digest;

public class StellarIdentifier implements Serializable {
	private static final long serialVersionUID = -6009723401818144454L;
	String humanReadableIdentifier;
	byte[] payloadBytes;
	int identifierType;
		
	/**
	 * @param payloadBytes
	 * @param identifierType : See ripple_data/protocol/RippleAddress.h
	 */
	public StellarIdentifier(byte[] payloadBytes, int identifierType){
		this.payloadBytes = payloadBytes;
		this.identifierType = identifierType;
	}
	
	public StellarIdentifier(String stringID) {
		this.humanReadableIdentifier = stringID;
		byte[] stridBytes = StellarBase58.decode(stringID);
		byte[] checksumArray = doubleSha256(stridBytes, 0, stridBytes.length-4);
		if(    checksumArray[0]!=stridBytes[stridBytes.length-4] 
			|| checksumArray[1]!=stridBytes[stridBytes.length-3] 
			|| checksumArray[2]!=stridBytes[stridBytes.length-2]
			|| checksumArray[3]!=stridBytes[stridBytes.length-1]){
			throw new RuntimeException("Checksum failed on identifier "+stringID);
		}

		payloadBytes = Arrays.copyOfRange(stridBytes, 1, stridBytes.length-4);
		identifierType = stridBytes[0];
	}

	@Override
	public String toString() {
		if(humanReadableIdentifier==null){
			byte[] versionPayloadChecksumBytes=new byte[1+payloadBytes.length+4];
			versionPayloadChecksumBytes[0]=(byte) identifierType;
			System.arraycopy(payloadBytes, 0, versionPayloadChecksumBytes, 1, payloadBytes.length);

			byte[] hashBytes = doubleSha256(versionPayloadChecksumBytes, 0, 1+payloadBytes.length);
			System.arraycopy(hashBytes, 0, versionPayloadChecksumBytes, 1+payloadBytes.length, 4);
			humanReadableIdentifier=StellarBase58.encode(versionPayloadChecksumBytes);
		}
		return humanReadableIdentifier;
	}

	protected byte[] doubleSha256(byte[] bytesToDoubleHash, int offset, int length) {
		SHA256Digest mda = new SHA256Digest();
		mda.update(bytesToDoubleHash, offset, length);
		byte[] hashBytes = new byte[32];
		mda.doFinal(hashBytes, 0);
		mda.reset();
		mda.update(hashBytes, 0, hashBytes.length);
		mda.doFinal(hashBytes, 0);
		return hashBytes;
	}

	public byte[] getBytes() {
		return payloadBytes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(payloadBytes);
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
		StellarIdentifier other = (StellarIdentifier) obj;
		if (!Arrays.equals(payloadBytes, other.payloadBytes))
			return false;
		return true;
	}

}
