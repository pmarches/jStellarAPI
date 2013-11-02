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
	
	public RipplePrivateKey(BigInteger privateKeyForAccount) {
		super(new byte[32], 34); //new byte[] will be stored in payloadBytes
		//toArray will return the minimum number of bytes required to encode the biginteger. Could be less than 32bytes
		//in addition, toArray seems to have the first byte for the sign (should always be 0)
		byte[] bigIntegerBytes = privateKeyForAccount.toByteArray(); 
		int nbBytesMissing=33-bigIntegerBytes.length;
		System.arraycopy(bigIntegerBytes, nbBytesMissing+1, payloadBytes, 0, bigIntegerBytes.length-1);
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
