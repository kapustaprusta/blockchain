package blockchain.utils;

import java.time.Instant;

public record HashResult(
        String  hashSum,
        String  magicNumber,
        Instant timestamp
) {}
