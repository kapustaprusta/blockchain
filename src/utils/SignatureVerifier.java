package blockchain.utils;

import java.security.*;

public class SignatureVerifier {

    public static boolean verify(byte[] data, PublicKey publicKey, byte[] signature) throws NoSuchAlgorithmException,
                                                                                                InvalidKeyException,
                                                                                                SignatureException {
        final var sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(publicKey);
        sig.update(data);

        return sig.verify(signature);
    }

}