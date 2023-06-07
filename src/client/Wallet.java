package blockchain.client;

import blockchain.blockchain.Blockchain;

import java.security.NoSuchAlgorithmException;

public final class Wallet extends Client {

    public Wallet(String name, Blockchain blockchain) throws NoSuchAlgorithmException {
        super(name, blockchain);
    }

}
