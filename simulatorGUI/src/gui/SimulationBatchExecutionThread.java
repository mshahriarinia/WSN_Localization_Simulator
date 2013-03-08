package gui;

import java.io.FileNotFoundException;

import simulator.Simulator;

public class SimulationBatchExecutionThread implements Runnable {

	int from, to;

	public SimulationBatchExecutionThread(int from, int to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public void run() {
		for (int i = from; i <= to; i++) {
			Simulator simulator;
			try {
				// simulator = new Simulator(null, true, i + "");
//				simulator.run();
			} catch (Exception e) {
				if (e instanceof FileNotFoundException)
					System.err.print(i + " ");
				else
					e.printStackTrace();

			}

		}
	}

}
