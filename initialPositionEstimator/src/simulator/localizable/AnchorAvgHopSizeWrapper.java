package simulator.localizable;

import node.type.Anchor;

public class AnchorAvgHopSizeWrapper {

	public static final int NOT_AVAILABLE = -1;

	private int averageHopSize;

	private Anchor anchor;

	public AnchorAvgHopSizeWrapper(Anchor anchor, int averageHopSize) {
		this.anchor = anchor;
		this.averageHopSize = averageHopSize;
	}

	public int getAverageHopSize() {
		return averageHopSize;
	}

	public Anchor getAnchor() {
		return anchor;
	}
	
	@Override
	public String toString() {
		return "A"+anchor.getId()+" AverageHopSize "+averageHopSize;
	}
}
