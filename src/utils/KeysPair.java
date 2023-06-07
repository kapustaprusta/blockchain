package blockchain.utils;

import java.security.PrivateKey;
import java.security.PublicKey;

public record KeysPair(
        PublicKey publicKey,
        PrivateKey privateKey
) {}