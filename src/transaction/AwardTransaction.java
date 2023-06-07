package blockchain.transaction;

import java.security.*;

import blockchain.client.Client;

public final class AwardTransaction extends Transaction {

    private final String _minerName;

    public AwardTransaction(int id, String minerName, int amount, PublicKey publicKey) {
        super(id, amount, publicKey);
        _minerName = minerName;
    }

    @Override
    public int summarizeClientBalance(Client client) {
        return (client.name().equals(_minerName)) ? _amount : 0;
    }

    public String transactionData() {
        return "%s gets %d VC".formatted(_minerName, _amount);
    }
}
