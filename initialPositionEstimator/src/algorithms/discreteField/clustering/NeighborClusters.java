package algorithms.discreteField.clustering;

import java.util.LinkedList;
import java.util.List;

import node.ID;

public class NeighborClusters {
	private List<ID> listNeighborClusters;

	int depth;

	public NeighborClusters(int depth) {
		listNeighborClusters = new LinkedList<ID>();
		this.depth = depth;
	}

	public void addNeighborCluster(ID id) {
		listNeighborClusters.add(id);
	}
	
	public int getDepth() {
		return depth;
	}
	public List<ID> getListNeighborClusters() {
		return listNeighborClusters;
	}
}
