package algorithms.discreteField.clustering;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import node.Destination;
import node.ID;
import node.Neighbor;
import node.Node;
import node.tools.Message;
import node.tools.TimedData;

//keep the path of the packet in nodes. - it is definitely the shortest path.

/**
 * we need to store at each level which node is the next hop to cluster head. <br>
 * Count down cluster chance - at the end you will be either cluster head or not
 * for a specific depth. If you are , you broadcast it with a proper TTL,
 * otherwise rebroadcast the clusterhead message of the actual cluster head.
 * 
 * @author Morteza
 * 
 */

// TODO if two nodes declare themselves clusterheads but they are at the
// distance of the cluster, the node with higher id should be the cluster head,
// and the node with smaller id should revoke ts declaration of being a
// clusterhead (how to cancel all messages already sent and get back to the
// initil state?
// --does it really matter? --Ithink we can forget about this.

// TODO if a node is not a cluster head at any level, it will not proceed to the
// next round of count down for the next depth of clustering.
public class HierarchicalClustering {
	Node node;

	int MAX_CLUSTERING_ITERATIONS = 20;

	int startCycle;

	int currClusterDepth = 0;

	static final int CLUSTER_DEPTHS_IN_HOPS = 2;

	List<LevelClusterStatus> listLCS;

	private int clusterHeadChance;

	private boolean isPotentialClusterHead = true;

	Random rand = new Random();

	public HierarchicalClustering(Node node) {
		this.node = node;
		startCycle = node.getNodeList().getZXControl().getICycler()
				.getCycleNo();
		listLCS = new ArrayList<LevelClusterStatus>();
		resetClusterHeadChance();
	}

	public void execute() {

		boolean clusterHeadIsSet = false;

		clusterHeadIsSet = clusterHeadIsSet || handleClusteringMessages();
		if (clusterHeadIsSet)
			isPotentialClusterHead = false;

		if (isPotentialClusterHead)
			clusterHeadIsSet = clusterHeadIsSet || updateClusterHeadChance();

		if (clusterHeadIsSet)
			resetClusterHeadChance();
	}

	/**
	 * 
	 * Run different levels of hierarchical clustering to help localization and
	 * return the most updated location estimation based on the data found so
	 * far.<br>
	 * 
	 * 
	 */
	public Point2D getEstimate() {
		return applyClusterInfo();
	}

	/**
	 * If a cluster head message has been arrived, either join that cluster or
	 * inform the cluster head about the neighboring cluster
	 * 
	 * @return: if sth has been set as cluster head
	 */
	private boolean handleClusteringMessages() {
		List<TimedData> listTD = node.getMemory().getListVarietyData();

		boolean isSet = false;

		List<TimedData> listTDToRemove = new LinkedList<TimedData>();
		for (TimedData td : listTD) {
			if (td.getData() instanceof ClusteringMessageData) {
				ClusteringMessageData cmd = (ClusteringMessageData) td
						.getData();
				if (!hasClusterStatus(cmd.getDepth())) {

					Message message = node.getMemory()
							.getMessageOfTimedData(td);
					if (message != null) {
						addClusterStatus(cmd.getNodeId(), cmd.getDepth(),
								node.getNeighbor(message.getFrom()));
						// remove this TimedData from the memory, as it is
						listTDToRemove.add(td);

					}
					isSet = true;
				}
			}
		}
		node.getMemory().getListVarietyData().remove(listTDToRemove);
		return isSet;
	}

	/**
	 * apply all the cluster info gathered so much to enhance location
	 * estimation.
	 */
	private Point2D applyClusterInfo() {
		return null;// TODO
	}

	/**
	 * 
	 * @return true if it has been set cluster head.
	 */
	private boolean updateClusterHeadChance() {
		decClusterHeadChance();
		if (!hasClusterStatus(currClusterDepth))
			// random counting and reducing is due, hence it is the cluster head
			if (clusterHeadChance == 0) {
				declareClusterHead(node, currClusterDepth, null);
				return true;
			}
		return false;
	}

	/**
	 * Node declares itself as the cluster head in this depth of the hierarchy
	 * of clusters.
	 */
	private void declareClusterHead(Node node, int currClusterDepth,
			Neighbor neighbor) {
		addClusterStatus(node.getId(), currClusterDepth, neighbor);

		Message message = new Message(node.getId(), new Destination(
				currClusterDepth * CLUSTER_DEPTHS_IN_HOPS),
				new ClusteringMessageData(node.getId(), node
						.getCalculatedCenter(), currClusterDepth));
		node.getInboxManager().send(message);

	}

	/**
	 * adds the id as the cluster head of this cluster depth in the hierarchy of
	 * clusters.
	 * 
	 * @param id
	 * @param currClusterDepth
	 */
	private void addClusterStatus(ID id, int currClusterDepth, Neighbor neighbor) {
		listLCS.add(new LevelClusterStatus(id, currClusterDepth, neighbor));
	}

	/**
	 * checks if this node is already assigned a cluster status at this depth
	 * (of the hierarchy of the clusters)
	 * 
	 * @param depth
	 * @return
	 */
	private boolean hasClusterStatus(int depth) {
		for (LevelClusterStatus lcs : listLCS) {
			if (lcs.getLevel() == depth)
				return true;
		}
		return false;
	}

	public boolean isFinished() {
		return node.getNodeList().getZXControl().getICycler().getCycleNo()
				- startCycle >= MAX_CLUSTERING_ITERATIONS;
	}

	/**
	 * for higher clusters in the hierarchy consider node count in the cluster
	 */
	private void resetClusterHeadChance() {
		clusterHeadChance = rand.nextInt(node.getNeighbors().size());
		currClusterDepth++;
	}

	/**
	 * Count Down - Decreases cluster head chance, whichever node that reaches
	 * zero will be the cluster head
	 */
	private void decClusterHeadChance() {
		clusterHeadChance--;
	}

	/**
	 * 
	 * @return List of LevelClusterStatus
	 */
	public List<LevelClusterStatus> getListLCS() {
		return listLCS;
	}
}
