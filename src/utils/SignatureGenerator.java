package blockchain.utils;

import java.security.*;

public class SignatureGenerator {

    public static byte[] generate(byte[] data, PrivateKey privateKey) throws NoSuchAlgorithmException,
                                                                                InvalidKeyException,
                                                                                SignatureException {
        final var rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(privateKey);
        rsa.update(data);

        return rsa.sign();
    }

}