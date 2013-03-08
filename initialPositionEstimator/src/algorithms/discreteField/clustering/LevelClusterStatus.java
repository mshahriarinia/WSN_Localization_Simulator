package algorithms.discreteField.clustering;

import node.ID;
import node.Neighbor;

/**
 * Status of the node (either cluster-head or follower) in a specific cluster
 * depth.
 * 
 * @author Morteza
 * 
 */
public class LevelClusterStatus {

	private ID clusterHeadNodeId;

	/**
	 * level of hierarchy. 1st level is 2 hop neighborhood. 2nd is two hop
	 * cluster heads, and so on
	 */
	private int level;

	private Neighbor neighbor;

	public ID getClusterHeadID() {
		return clusterHeadNodeId;
	}

	public int getLevel() {
		return level;
	}

	/**
	 * 
	 * @param clusterHeadNodeId
	 * @param level
	 * @param closestNeighbor
	 *            : the neighbor that has informed the node about that cluster
	 *            head, this would be null if at this level the cluster head is
	 *            the node itself
	 */
	public LevelClusterStatus(ID clusterHeadNodeId, int level,
			Neighbor closestNeighbor) {
		this.clusterHeadNodeId = clusterHeadNodeId;
		this.level = level;
	}

	public Neighbor getNeighbor() {
		return neighbor;
	}

}
