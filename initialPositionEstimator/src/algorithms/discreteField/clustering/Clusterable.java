package algorithms.discreteField.clustering;

public interface Clusterable {

	public HierarchicalClustering getHierarchicalClustering();

	public boolean canStartClustering(int cycleNo);

	public boolean state_HIERARCHICAL_CLUSTERING();

}
