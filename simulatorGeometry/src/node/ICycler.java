package node;

import reporter.ExecutionReporter;


public interface ICycler {

	public int getCycleNo();

	public boolean isCurrent(int cycleNo);
	
	public ExecutionReporter getExecutionReporter();

}
