package blockchain.client;

import blockchain.blockchain.Blockchain;
import blockchain.utils.KeysPair;
import blockchain.utils.KeysGenerator;
import blockchain.transaction.WalletTransaction;

import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public abstract class Client {

    protected final String     _name;
    protected final KeysPair   _keysPair;
    protected final Blockchain _blockchain;

    public Client(String name, Blockchain blockchain) throws NoSuchAlgorithmException {
        _name       = name;
        _blockchain = blockchain;
        _keysPair   = KeysGenerator.generate(1024);
    }

    public String name() {
        return _name;
    }

    public synchronized void sendMoneyTo(int amount, Client receiver) throws IllegalStateException,
                                                                                NoSuchAlgorithmException,
                                                                                SignatureException,
                                                                                InvalidKeyException {
        final var balance = _blockchain.balance(this);
        if (balance < amount) {
            throw new IllegalStateException("balance cannot be negative");
        }

        final var transactionId = _blockchain.nextTransactionId();
        final var transaction   = new WalletTransaction(
                transactionId,
                amount,
                _name,
                receiver._name,
                _keysPair.publicKey()
        );
        transaction.sign(_keysPair.privateKey());
        _blockchain.addTransaction(transaction);
    }

}

