 package blockchain.block;

import java.time.Instant;
import java.time.Duration;

import blockchain.utils.HashResult;

public record BlockMetaData(
        int              _id,
        int              _minerId,
        String           _hash,
        String           _prevHash,
        String           _magicNumber,
        Instant          _timestamp,
        Duration         _elapsedTime
) {

    public BlockMetaData() {
        this(
                -1,            // _id
                -1,            // _minerId
                "",            // _hash
                "",            // _prevHash
                "",            // _magicNumber
                Instant.EPOCH, // _timestamp
                Duration.ZERO  // _elapsedTime
        );
    }

    public BlockMetaData(
            int id,
            int minerId,
            String prevBlockHash,
            HashResult hashResult,
            Duration elapsedTime
    ) {
        this(
                id,
                minerId,
                hashResult.hashSum(),
                prevBlockHash,
                hashResult.magicNumber(),
                hashResult.timestamp(),
                elapsedTime
        );
    }

}
