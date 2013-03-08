package gui;

public class SimulatorConsole {

	public static void main(String[] args) {

		/**
		 * use two threads to maximize utilizing a dual core CPU
		 */

		SimulationBatchExecutionThread et1 = new SimulationBatchExecutionThread(
		// 0, 50);
				// 121, 175);
				156, 175);
		SimulationBatchExecutionThread et2 = new SimulationBatchExecutionThread(
		// 51, 115);
				// 176, 259);
				205, 259);

		Thread t1 = new Thread(et1);
		Thread t2 = new Thread(et2);
		t1.start();
		t2.start();
	}
}
