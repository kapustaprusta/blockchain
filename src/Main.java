package blockchain;

import blockchain.client.Miner;
import blockchain.blockchain.Blockchain;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.security.NoSuchAlgorithmException;

public class Main {
	public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
		final int NUMBER_OF_BLOCKS = 15;

		final var blockChain = Blockchain.getInstance(NUMBER_OF_BLOCKS);
		final var threadsNum = Runtime.getRuntime().availableProcessors();
		final var threadPool = Executors.newFixedThreadPool(threadsNum);

		for (var minerId = 0; minerId < threadsNum; minerId++) {
			threadPool.submit(new Miner(minerId, blockChain));
		}

		threadPool.shutdown();
		threadPool.awaitTermination(86400, TimeUnit.SECONDS);
	}
}
