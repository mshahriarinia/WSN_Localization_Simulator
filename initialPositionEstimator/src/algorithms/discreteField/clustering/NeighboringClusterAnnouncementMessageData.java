package algorithms.discreteField.clustering;

import node.ID;

/**
 * This message will inform the cluster head of a node about the neighboring
 * lcuster head. This message is relayed by nodes that belong to the cluster
 * that want to know about it. Since the members of a cluster already know their
 * link to their cluster head, they exactly know which node should be the next
 * step to get the message.
 * 
 * @author Morteza
 * 
 */
public class NeighboringClusterAnnouncementMessageData {
	private int depth;

	private ID neighboringClusterHeadID;

	public NeighboringClusterAnnouncementMessageData(
			ID neighboringClusterHeadID, int depth) {
		this.neighboringClusterHeadID = neighboringClusterHeadID;
		this.depth = depth;
	}

	public ID getNeighboringClusterHeadID() {
		return neighboringClusterHeadID;
	}

	public int getDepth() {
		return depth;
	}
}
