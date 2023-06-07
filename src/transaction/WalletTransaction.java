package blockchain.transaction;

import java.security.*;

import blockchain.client.Client;

public final class WalletTransaction extends Transaction {

    private final String    _sender;
    private final String    _receiver;


    public WalletTransaction(int id, int amount, String sender, String receiver, PublicKey publicKey) {
        super(id, amount, publicKey);
        _sender    = sender;
        _receiver  = receiver;
    }

    @Override
    public int summarizeClientBalance(Client client) {
        int totalAmount = 0;
        var clientName  = client.name();

        if (clientName.equals(_sender)) {
            totalAmount -= _amount;
        } else if (clientName.equals(_receiver)) {
            totalAmount += _amount;
        }

        return totalAmount;
    }

    public String transactionData() {
        return "%s sent %d VC to %s".formatted(_sender, _amount, _receiver);
    }
}
