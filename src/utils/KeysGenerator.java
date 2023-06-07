package blockchain.utils;

import java.security.*;

public class KeysGenerator {

    public static KeysPair generate(int keysLength) throws NoSuchAlgorithmException {
        final var keysGenerator = KeyPairGenerator.getInstance("RSA");
        keysGenerator.initialize(keysLength);
        final var keysPair = keysGenerator.generateKeyPair();

        return new KeysPair(keysPair.getPublic(), keysPair.getPrivate());
    }

}
