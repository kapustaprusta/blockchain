package blockchain.client;

import java.time.Instant;
import java.time.Duration;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import blockchain.blockchain.Blockchain;
import blockchain.transaction.Transaction;
import blockchain.utils.HashGenerator;
import blockchain.block.BlockMetaData;
import blockchain.transaction.AwardTransaction;


public final class Miner extends Client implements Runnable {

    private final int _minerId;

    public Miner(int minerId, Blockchain blockchain) throws NoSuchAlgorithmException {
        super(String.format("miner%d", minerId), blockchain);
        _minerId = minerId;
    }

    @Override
    public void run() {
        try {
            doWork();
        } catch (Exception ignored) {}
    }

    private void doWork() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        while (_blockchain.isNotReady()) {
            final var blockId       = _blockchain.nextBlockId();
            final var blockReward   = _blockchain.blockReward();
            final var prevBlockHash = _blockchain.lastBlockHash();
            final var transactionId = _blockchain.firstTransactionId();

            Transaction transaction;
            BlockMetaData nextBlockMetaData;
            Instant startedAt = Instant.now();

            do {
                transaction = new AwardTransaction(
                        transactionId,
                        _name,
                        blockReward,
                        _keysPair.publicKey()
                );
                transaction.sign(_keysPair.privateKey());

                nextBlockMetaData  = new BlockMetaData(
                        blockId,
                        _minerId,
                        prevBlockHash,
                        HashGenerator.generate(),
                        Duration.between(startedAt, Instant.now()
                    )
                );
            } while (!_blockchain.addBlockMetaData(transaction, nextBlockMetaData) && blockId == _blockchain.nextBlockId());
        }
    }

}
