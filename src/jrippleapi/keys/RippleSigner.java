package jrippleapi.keys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import jrippleapi.core.RipplePrivateKey;
import jrippleapi.serialization.RippleBinaryObject;
import jrippleapi.serialization.RippleBinarySchema.BinaryFormatField;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.math.ec.ECPoint;

public class RippleSigner {
	RipplePrivateKey privateKey;

	public RippleSigner(RipplePrivateKey privateKey) {
		this.privateKey=privateKey;
	}

	/**
	 * see https://ripple.com/forum/viewtopic.php?f=2&t=3206&p=13277&hilit=json+rpc#p13277
	 * see https://ripple.com/wiki/User:Singpolyma/Transaction_Signing
	 * @param serObjToSign
	 * @return
	 * @throws Exception
	 */
	public RippleBinaryObject sign(RippleBinaryObject serObjToSign) throws Exception {
		if(serObjToSign.getField(BinaryFormatField.TxnSignature)!=null){
			throw new Exception("Object already signed");
		}
		byte[] hashOfRBOBytes = serObjToSign.generateHashFromBinaryObject();

		ECDSASignature signature = signHash(hashOfRBOBytes);

		RippleBinaryObject signedRBO = new RippleBinaryObject(serObjToSign);
		//Add the Signature to the serializedObject as a field
		signedRBO.putField(BinaryFormatField.TxnSignature, signature.encodeToDER());
		signedRBO.putField(BinaryFormatField.SigningPubKey, signature.publicSigningKey.getEncoded());
		return signedRBO;
	}

	private ECDSASignature signHash(byte[] hashOfBytes) throws Exception {
		if(hashOfBytes.length!=32){
			throw new RuntimeException("can sign only a hash of 32 bytes");
		}
        
        ECDSASigner signer = new ECDSASigner();
		ECPrivateKeyParameters privKey = privateKey.getECPrivateKey();
        signer.init(true, privKey);
        BigInteger[] RandS = signer.generateSignature(hashOfBytes);
        return new ECDSASignature(RandS[0], RandS[1], privateKey.getPublicKey().getPublicPoint());
	}

	public boolean isSignatureVerified(RippleBinaryObject serObj) {
		try {
			byte[] signatureBytes= (byte[]) serObj.getField(BinaryFormatField.TxnSignature);
			if(signatureBytes==null){
				throw new RuntimeException("The specified  has no signature");
			}
			byte[] signingPubKeyBytes = (byte[]) serObj.getField(BinaryFormatField.SigningPubKey);
			if(signingPubKeyBytes==null){
				throw new RuntimeException("The specified  has no public key associated to the signature");
			}

			RippleBinaryObject unsignedRBO = serObj.getUnsignedCopy();
			byte[] hashToVerify = unsignedRBO.generateHashFromBinaryObject();

			ECDSASigner signer = new ECDSASigner();
			ECDSASignature signature = new ECDSASignature(signatureBytes, signingPubKeyBytes);
			signer.init(false, new ECPublicKeyParameters(signature.publicSigningKey, RippleDeterministicKeyGenerator.SECP256K1_PARAMS));
			return signer.verifySignature(hashToVerify, signature.r, signature.s);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static class ECDSASignature {
        public BigInteger r, s;
		private ECPoint publicSigningKey;

        public ECDSASignature(BigInteger r, BigInteger s, ECPoint publicSigningKey) {
            this.r = r;
            this.s = s;
            this.publicSigningKey= publicSigningKey;
        }

        public ECDSASignature(byte[] signatureDEREncodedBytes, byte[] signingPubKey) throws IOException {
        	publicSigningKey = RippleDeterministicKeyGenerator.SECP256K1_PARAMS.getCurve().decodePoint(signingPubKey);
			
			ASN1InputStream decoder = new ASN1InputStream(signatureDEREncodedBytes);
			DLSequence seq = (DLSequence) decoder.readObject();
			DERInteger r = (DERInteger) seq.getObjectAt(0);
			DERInteger s = (DERInteger) seq.getObjectAt(1);

			// OpenSSL deviates from the DER spec by interpreting these values as unsigned, though they should not be
			// Thus, we always use the positive versions.
			// See: http://r6.ca/blog/20111119T211504Z.html
			this.r = r.getPositiveValue();
			this.s = s.getPositiveValue();
			decoder.close();
		}

		/**
         * What we get back from the signer are the two components of a signature, r and s. To get a flat byte stream
         * of the type used by Bitcoin we have to encode them using DER encoding, which is just a way to pack the two
         * components into a structure.
         */
        public byte[] encodeToDER() {
            try {
                // Usually 70-72 bytes.
                ByteArrayOutputStream bos = new ByteArrayOutputStream(72);
                DERSequenceGenerator seq = new DERSequenceGenerator(bos);
                seq.addObject(new DERInteger(r));
                seq.addObject(new DERInteger(s));
                seq.close();
                return bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);  // Cannot happen.
            }
        }
    }

}
