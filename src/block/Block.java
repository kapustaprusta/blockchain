package blockchain.block;

import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.function.ToIntFunction;

import blockchain.client.Client;
import blockchain.transaction.Transaction;

public class Block {

    private BlockMetaData                       _metaData;
    private final TreeMap<Integer, Transaction> _transactions;

    public Block() {
        _metaData     = new BlockMetaData();
        _transactions = new TreeMap<Integer, Transaction>();
    }

    public int summarizeClientBalance(Client client) {
        final ToIntFunction<Transaction> summarizeClientBalanceFunc =
                (Transaction transaction) -> transaction.summarizeClientBalance(client);

        return _transactions.values().stream()
                .mapToInt(summarizeClientBalanceFunc).sum();
    }

    public void setMetaData(Transaction transaction, BlockMetaData metaData) {
        _metaData = metaData;
        _transactions.put(transaction.id(), transaction);
    }

    public BlockMetaData metaData() {
        return _metaData;
    }

    public int maxTransactionId() {
        if (_transactions.keySet().isEmpty()) {
            return 0;
        }

        return _transactions.lastKey();
    }

    public int minTransactionId() {
        if (_transactions.keySet().isEmpty()) {
            return 0;
        }

        return _transactions.firstKey();
    }

    public void addTransaction(int transactionId, Transaction transaction) {
        _transactions.put(transactionId, transaction);
    }

    public boolean verifyTransactions() {
        return _transactions.values().stream()
                .filter(Transaction::verify)
                .count() ==
                _transactions.values().size();
    }

    @Override
    public String toString() {
        String blockData;
        if (_transactions.size() <= 1) {
            blockData = "no transactions";
        } else {
            blockData = _transactions.values().stream()
                    .skip(1)
                    .map(Transaction::transactionData)
                    .collect(Collectors.joining("\n", "\n", ""));
        }

        final var awardTransaction = _transactions.firstEntry();
        final var blockTimestamp   = _metaData._timestamp().toEpochMilli();
        final var blockElapsedTime = _metaData._elapsedTime().toSeconds();

        var minerInfo = "";
        if (awardTransaction != null) {
            minerInfo = awardTransaction.getValue().transactionData();
        }

        return String.format(
                """
                Created by: miner%d
                %s
                Id: %d
                Timestamp: %d
                Magic number: %s
                Hash of the previous block:
                %s
                Hash of the block:
                %s
                Block data:
                %s
                Block was generating for %d seconds
                """,
                _metaData._minerId(), minerInfo, _metaData._id(), blockTimestamp,
                _metaData._magicNumber(), _metaData._prevHash(), _metaData._hash(),
                blockData, blockElapsedTime
        );
    }
}
