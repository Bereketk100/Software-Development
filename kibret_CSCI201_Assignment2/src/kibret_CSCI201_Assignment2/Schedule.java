package kibret_CSCI201_Assignment2;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Schedule {

	/**
	 * Stock Trades Schedule Keep the track of tasks
	 */

	private BlockingQueue<Task> blockingQueue = new LinkedBlockingDeque<>();

	public Schedule() {
		this.setBlockingQueue(blockingQueue);
	}

	public BlockingQueue<Task> getBlockingQueue() {
		return blockingQueue;
	}

	public void setBlockingQueue(BlockingQueue<Task> blockingQueue) {
		this.blockingQueue = blockingQueue;
	}

	/**
	 * Inner class to store task object
	 */

	public static class Task {
		private Integer initiatedCsv;
		private String tickerCsv = "";
		private Integer transCsv;

		public Task(int initiatedCsv, String tickerCsv, int transCsv) {
			this.initiatedCsv = initiatedCsv;
			this.tickerCsv = tickerCsv;
			this.transCsv = transCsv;
		}

		public int getInitiatedCsv() {
			return initiatedCsv;
		}

		public void setInitiatedCsv(int initiatedCsv) {
			this.initiatedCsv = initiatedCsv;
		}

		public String getTickerCsv() {
			return tickerCsv;
		}

		public void setTickerCsv(String tickerCsv) {
			this.tickerCsv = tickerCsv;
		}

		public int getTransCsv() {
			return transCsv;
		}

		public void setTransCsv(int transCsv) {
			this.transCsv = transCsv;
		}

		/**
		 * String representation of a shelter
		 */
		@Override
		public String toString() {
			String output = "";
			output += "Initiated: " + getInitiatedCsv() + " ";

			output += "Ticker: " + getTickerCsv() + " ";
			output += "Bought/Sold: " + getTransCsv() + "\n\n";

			return output;
		}

	}

}
