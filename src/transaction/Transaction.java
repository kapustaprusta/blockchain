package blockchain.transaction;

import java.security.*;

import blockchain.client.Client;
import blockchain.utils.SignatureGenerator;
import blockchain.utils.SignatureVerifier;

public abstract class Transaction {

    protected final int       _id;
    protected final int       _amount;
    protected byte[]          _signature;
    protected final PublicKey _publicKey;

    public Transaction(int id, int amount, PublicKey publicKey) {
        _id        = id;
        _amount    = amount;
        _publicKey = publicKey;
    }

    public int id() {
        return _id;
    }

    public int amount() {
        return _amount;
    }

    public abstract String transactionData();

    public abstract int summarizeClientBalance(Client client);

    public void sign(PrivateKey privateKey) throws NoSuchAlgorithmException,
                                                    SignatureException,
                                                    InvalidKeyException {
        final var dataToSign = _id + transactionData();
        _signature = SignatureGenerator.generate(dataToSign.getBytes(), privateKey);
    }

    public boolean verify() {
        boolean isValid = false;
        final var dataToVerify = _id + transactionData();

        try {
            isValid = SignatureVerifier.verify(dataToVerify.getBytes(), _publicKey, _signature);
        } catch (Exception ignored) {}

        return isValid;
    }

}
