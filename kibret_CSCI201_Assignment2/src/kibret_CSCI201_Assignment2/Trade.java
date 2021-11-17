package kibret_CSCI201_Assignment2;

import java.util.concurrent.*;

public class Trade extends Thread {
	Schedule.Task task;
	Schedule schedule;
	Semaphore semaphore;

	public Trade(Schedule schedule, Schedule.Task task, Semaphore semaphore) {
		this.schedule = schedule;
		this.task = task;
		this.semaphore = semaphore;
	}

	/**
	 * Trading function using locks
	 */
	public void run() {
		String output = null;

		if (task.getTransCsv() != 0) {
			if (task.getTransCsv() > 0) {

				output = " Starting sale ";
			} else if (task.getTransCsv() < 0) {
				output = " Starting purchase ";
			}

			try {

				semaphore.acquire();

				System.out.println("[" + Utility.getZeroTimestamp() + "]" + output + "of " + Math.abs(task.getTransCsv())
						+ " stocks of " + task.getTickerCsv());
				Thread.sleep(1000);
				output = output.replace("Starting", "Finishing");
				System.out.println("[" + Utility.getZeroTimestamp() + "]" + output + "of " + Math.abs(task.getTransCsv())
						+ " stocks of " + task.getTickerCsv());
			}

			catch (InterruptedException e) {
			} finally {
				semaphore.release();
			}

		}
	}

}
