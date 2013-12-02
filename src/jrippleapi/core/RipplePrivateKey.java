package jrippleapi.core;

import java.math.BigInteger;

import jrippleapi.keys.RippleDeterministicKeyGenerator;

import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

public class RipplePrivateKey extends RippleIdentifier {
	boolean isDeterministic=false; //Deterministic private keys should never be exported, since the public generator+one private key yields the private generator!  
	RipplePublicKey publicKey;
	
	public RipplePrivateKey(byte[] privateKeyBytes) {
		super(privateKeyBytes, 34);
		if(privateKeyBytes.length!=32){
			throw new RuntimeException("The private key must be of length 32 bytes");
		}
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
	
	public RipplePrivateKey(BigInteger privateKeyForAccount) {
		super(bigIntegerToBytes(privateKeyForAccount, 32), 34);
	}

	public RipplePublicKey getPublicKey(){
		if(publicKey!=null){
			return publicKey;
		}

		BigInteger privateBI=new BigInteger(1, this.payloadBytes);
		ECPoint uncompressed= RippleDeterministicKeyGenerator.SECP256K1_PARAMS.getG().multiply(privateBI);
		ECPoint publicPoint = new ECPoint.Fp(RippleDeterministicKeyGenerator.SECP256K1_PARAMS.getCurve(), uncompressed.getX(), uncompressed.getY(), true);
		publicKey = new RipplePublicKey(publicPoint.getEncoded());
		return publicKey;
	}

	public ECPrivateKeyParameters getECPrivateKey(){
		//Or return the BigInteger instead?
		BigInteger privateBI=new BigInteger(1, this.payloadBytes);
		ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(privateBI, RippleDeterministicKeyGenerator.SECP256K1_PARAMS);
		return privKey;
	}
}
