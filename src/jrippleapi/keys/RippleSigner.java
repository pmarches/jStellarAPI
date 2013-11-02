package jrippleapi.keys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import javax.xml.bind.DatatypeConverter;

import jrippleapi.core.RipplePrivateKey;
import jrippleapi.serialization.RippleBinarySchema.BinaryFormatField;
import jrippleapi.serialization.RippleBinarySerializer;
import jrippleapi.serialization.RippleBinaryObject;

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
	RippleBinarySerializer binSer=new RippleBinarySerializer();

	public RippleSigner(RipplePrivateKey privateKey) {
		this.privateKey=privateKey;
	}

	//see https://ripple.com/forum/viewtopic.php?f=2&t=3206&p=13277&hilit=json+rpc#p13277
	//    https://ripple.com/wiki/User:Singpolyma/Transaction_Signing
	//
	//TODO return a signed RippleSerializedObject instead
	//TODO do not modify the input RippleSerializedObject
	public byte[] sign(RippleBinaryObject serObjToSign) throws Exception {
		if(serObjToSign.getField(BinaryFormatField.TxnSignature)!=null){
			throw new Exception("Object already signed");
		}
		byte[] hashOfBytes = generateHashFromSerializedObject(serObjToSign);

		//Sign the hash
		ECDSASignature signature = signHash(hashOfBytes);

		//Add the Signature to the serializedObject as a field
		serObjToSign.putField(BinaryFormatField.TxnSignature, signature.encodeToDER());
		serObjToSign.putField(BinaryFormatField.SigningPubKey, signature.publicSigningKey.getEncoded());

		//Convert to bytes again
		byte[] signedBytes = binSer.writeSerializedObject(serObjToSign).array();
		//Prefix bytesToSign with the magic sigining prefix (32bit) 'TXN\0'
		byte[] prefixedSignedBytes = new byte[signedBytes.length+4];
		prefixedSignedBytes[0]=(byte) 'T';
		prefixedSignedBytes[1]=(byte) 'X';
		prefixedSignedBytes[2]=(byte) 'N';
		prefixedSignedBytes[3]=(byte) 0;
		System.arraycopy(signedBytes, 0, prefixedSignedBytes, 4, signedBytes.length);

		//Hash again, this wields the TransactionID
		byte[] hashOfTransaction = RippleDeterministicKeyGenerator.halfSHA512(prefixedSignedBytes);
		System.out.println("hashOfTX "+DatatypeConverter.printHexBinary(hashOfTransaction));
		return hashOfTransaction;
	}

	private byte[] generateHashFromSerializedObject(RippleBinaryObject serObjToSign) {
		byte[] bytesToSign = binSer.writeSerializedObject(serObjToSign).array();

		//Prefix bytesToSign with the magic hashing prefix (32bit) 'STX\0'
		byte[] prefixedBytesToHash = new byte[bytesToSign.length+4];
		prefixedBytesToHash[0]=(byte) 'S';
		prefixedBytesToHash[1]=(byte) 'T';
		prefixedBytesToHash[2]=(byte) 'X';
		prefixedBytesToHash[3]=(byte) 0;
		System.arraycopy(bytesToSign, 0, prefixedBytesToHash, 4, bytesToSign.length);
		//Hash256
		byte[] hashOfBytes = RippleDeterministicKeyGenerator.halfSHA512(prefixedBytesToHash);
		return hashOfBytes;
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

    public static class ECDSASignature {
        public BigInteger r, s;
		private ECPoint publicSigningKey;

        public ECDSASignature(BigInteger r, BigInteger s, ECPoint publicSigningKey) {
            this.r = r;
            this.s = s;
            this.publicSigningKey= publicSigningKey;
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

    //TODO do not modify serObj
	public boolean verify(RippleBinaryObject serObj) {
		try {
			byte[] signatureBytes= (byte[]) serObj.removeField(BinaryFormatField.TxnSignature);
			byte[] hashToVerify = generateHashFromSerializedObject(serObj);
			byte[] signingPubKey = (byte[]) serObj.getField(BinaryFormatField.SigningPubKey);

			ECDSASigner signer = new ECDSASigner();
			ECPublicKeyParameters publicKey = new ECPublicKeyParameters(RippleDeterministicKeyGenerator.SECP256K1_PARAMS.getCurve().decodePoint(signingPubKey), RippleDeterministicKeyGenerator.SECP256K1_PARAMS);
			signer.init(false, publicKey);
			
			ASN1InputStream decoder = new ASN1InputStream(signatureBytes);
			DLSequence seq = (DLSequence) decoder.readObject();
			DERInteger r = (DERInteger) seq.getObjectAt(0);
			DERInteger s = (DERInteger) seq.getObjectAt(1);
			decoder.close();
			// OpenSSL deviates from the DER spec by interpreting these values as unsigned, though they should not be
			// Thus, we always use the positive versions.
			// See: http://r6.ca/blog/20111119T211504Z.html
			return signer.verifySignature(hashToVerify, r.getPositiveValue(), s.getPositiveValue());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
