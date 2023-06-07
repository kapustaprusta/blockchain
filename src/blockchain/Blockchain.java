package blockchain.blockchain;

import java.util.ArrayList;
import java.util.function.ToIntFunction;

import blockchain.block.Block;
import blockchain.block.BlockMetaData;
import blockchain.client.Client;
import blockchain.transaction.Transaction;

public class Blockchain {

	private int _numberOfZeros;
	private int _nextTransactionId;
	private int _firstTransactionId;

	private final int _blockReward;
	private final int _numberOfBlocks;

	private Block _Blockchain_block;
	private final ArrayList<Block> _Blockchain_blocks;

	private static volatile Blockchain _instance;

	private Blockchain(int numberOfBlocks) {
		_numberOfZeros      = 1;
		_nextTransactionId  = 1;
		_firstTransactionId = 1;

		_blockReward    = 100;
		_numberOfBlocks = numberOfBlocks;

		_Blockchain_block = new Block();
		_Blockchain_blocks = new ArrayList<>();
	}

	public static Blockchain getInstance(int numberOfBlock) {
		if (_instance == null) {
			synchronized (Blockchain.class) {
				if (_instance == null)
					_instance = new Blockchain(numberOfBlock);
			}
		}

		return _instance;
	}

	// blocks
	public synchronized boolean addBlockMetaData(
			Transaction transaction,
			BlockMetaData blockMetaData
	) {
		if (!validateNextBlockMetaData(blockMetaData)) {
			return false;
		}

		_Blockchain_block.setMetaData(
				transaction,
				blockMetaData
		);

		_Blockchain_blocks.add(_Blockchain_block);
		_Blockchain_block = new Block();

		_firstTransactionId =
				++_nextTransactionId;

		printLastBlock();
		adjustNumberOfZeroes();

		return true;
	}

	public int blockReward() {
		return _blockReward;
	}

	public synchronized int nextBlockId() {
		return _Blockchain_blocks.size() + 1;
	}

	public synchronized String lastBlockHash() {
		return (lastBlock() == null) ? "0" : lastBlock().metaData()._hash();
	}

	private Block prevBlock(Block block) {
		final var blockId = block.metaData()._id();
		return (blockId == 1) ? null : _Blockchain_blocks.get(blockId - 1 - 1);
	}

	public synchronized String prevBlockHash(Block block) {
		final var prevBlock = prevBlock(block);
		return (prevBlock == null) ? "0" : prevBlock.metaData()._hash();
	}

	public synchronized int prevBlockMaxMessageId(Block block) {
		final var prevBlock = prevBlock(block);
		return (prevBlock == null) ? 0 : prevBlock.maxTransactionId();
	}

	private Block lastBlock() {
		Block lastBlock = null;
		if (!_Blockchain_blocks.isEmpty()) {
			lastBlock = _Blockchain_blocks.get(_Blockchain_blocks.size() - 1);
		}

		return lastBlock;
	}

	private void printLastBlock() {
		if (_Blockchain_blocks.isEmpty()) {
			return;
		}

		System.out.println("Block:");
		System.out.print(lastBlock());
	}

	private boolean validateBlock(Block block) {
		if (block == null) {
			return false;
		}

		return block.verifyTransactions() &&
				block.minTransactionId() > prevBlockMaxMessageId(block) &&
				block.metaData()._prevHash().equals(prevBlockHash(block));
	}

	private boolean validateNextBlockMetaData(BlockMetaData blockMetaData) {
		if (blockMetaData == null) {
			return false;
		}

		return lastBlockHash().equals(blockMetaData._prevHash()) &&
				blockMetaData._hash().startsWith("0".repeat(_numberOfZeros));
	}

	// transactions
	public synchronized int nextTransactionId() {
		return ++_nextTransactionId;
	}

	public synchronized int firstTransactionId() {
		return _firstTransactionId;
	}

	public synchronized void addTransaction(Transaction transaction) {
		if (!validateNextTransaction(transaction)) {
			return;
		}

		_Blockchain_block.addTransaction(transaction.id(), transaction);
	}

	private boolean validateNextTransaction(Transaction transaction) {
		return transaction != null &&
				transaction.verify() &&
				transaction.id() > _Blockchain_block.maxTransactionId();
	}

	// state
	public synchronized boolean isNotReady() {
		return _Blockchain_blocks.size() != _numberOfBlocks;
	}

	public synchronized boolean validate() {
		if (_Blockchain_blocks.isEmpty()) {
			return true;
		}

		for (var block : _Blockchain_blocks) {
			if (!validateBlock(block)) {
				return false;
			}
		}

		return true;
	}

	private void adjustNumberOfZeroes() {
		if (_Blockchain_blocks.isEmpty()) {
			return;
		}

		final var lastBlock = lastBlock();
		if (lastBlock == null) {
			return;
		}

		System.out.print(
				"N stays the same\n\n"
		);

//		final var lastBlockElapsedTimeSec = lastBlock.metaData()._elapsedTime().toSeconds();
//		if (lastBlockElapsedTimeSec < 10) {
//			_numberOfZeros++;
//			System.out.printf(
//					"N was increased to %d\n\n",
//					_numberOfZeros
//			);
//		} else if (lastBlockElapsedTimeSec > 50) {
//			_numberOfZeros--;
//			System.out.print(
//					"N was decreased by 1\n\n"
//			);
//		} else {
//			System.out.print(
//					"N stays the same\n\n"
//			);
//		}
	}

	// balances
	public synchronized int balance(Client client) {
		if (client == null) {
			return 0;
		}

		final ToIntFunction<Block> summarizeClientBalanceFunc =
				(Block block) -> block.summarizeClientBalance(client);

		return _Blockchain_blocks.stream()
				.mapToInt(summarizeClientBalanceFunc).sum();
	}

	@Override
	public String toString() {
		var outputBuilder = new StringBuilder();
		for (var block : _Blockchain_blocks) {
			outputBuilder.append(String.format("Block:\n%s\n", block));
		}

		return outputBuilder.toString();
	}

}