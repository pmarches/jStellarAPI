package jstellarapi.core;

import java.math.BigInteger;
import java.util.Arrays;

import jstellarapi.keys.StellarDeterministicKeyGenerator;
import net.i2p.crypto.eddsa.math.GroupElement;

import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

public class StellarPrivateKey extends StellarIdentifier {
	StellarPublicKey publicKey;
	
	public StellarPrivateKey(byte[] privateKeyBytes) {
		super(privateKeyBytes, 101);
		if(privateKeyBytes.length!=32){
			throw new RuntimeException("The private key must be of length 32 bytes");
		}
	}

	public StellarPrivateKey(byte[] privateKeyBytes, byte[] publicKeyBytes) {
		super(privateKeyBytes, 101);
		if(privateKeyBytes.length!=32){
			throw new RuntimeException("The private key must be of length 32 bytes");
		}
		publicKey=new StellarPublicKey(publicKeyBytes);
	}

	public static byte[] bigIntegerToBytes(BigInteger biToConvert, int nbBytesToReturn){
		//toArray will return the minimum number of bytes required to encode the biginteger in two's complement.
		//Could be less than the expected number of bytes
		byte[] twosComplement = biToConvert.toByteArray();
		byte[] bytesToReturn=new byte[nbBytesToReturn];

		if((biToConvert.bitLength()+7)/8!=twosComplement.length){
			//Two's complement representation has a sign bit set on the most significant byte
			byte[] twosComplementWithoutSign = new byte[twosComplement.length-1];
			System.arraycopy(twosComplement, 1, twosComplementWithoutSign, 0, twosComplementWithoutSign.length);
			twosComplement=twosComplementWithoutSign;
		}

		int nbBytesOfPaddingRequired=nbBytesToReturn-twosComplement.length;
		if(nbBytesOfPaddingRequired<0){
			throw new RuntimeException("nbBytesToReturn "+nbBytesToReturn+" is too small");
		}
		System.arraycopy(twosComplement, 0, bytesToReturn, nbBytesOfPaddingRequired, twosComplement.length);

		return bytesToReturn;
	}
	
	public StellarPrivateKey(BigInteger privateKeyForAccount) {
		super(bigIntegerToBytes(privateKeyForAccount, 32), 34);
	}

	public StellarPublicKey getPublicKey(){
		if(publicKey!=null){
			return publicKey;
		}

        GroupElement A = StellarPublicKey.ed25519.getB().scalarMultiply(payloadBytes);
		byte[] encodedAndCompressedPublicKeyBytes=A.toByteArray();
		publicKey = new StellarPublicKey(encodedAndCompressedPublicKeyBytes);
		return publicKey;
	}

	public ECPrivateKeyParameters getECPrivateKey(){
		//Or return the BigInteger instead?
		BigInteger privateBI=new BigInteger(1, this.payloadBytes);
		ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(privateBI, StellarDeterministicKeyGenerator.SECP256K1_PARAMS);
		return privKey;
	}
}
