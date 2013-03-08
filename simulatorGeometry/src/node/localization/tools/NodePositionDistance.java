package node.localization.tools;

import java.awt.geom.Point2D;
import java.util.Comparator;

import node.Neighbor;
import node.Node;
import control.ZXControl;

/**
 * 
 * this class wraps a node position and the distance it has traveled duplicates
 * are made when readingInbox and going to save the message to hold the specific
 * info of each path
 * 
 */
public class NodePositionDistance extends NodePosition {

	private int hopCount;

	private double accumulatedDistance;

	public NodePositionDistance(Node node, Point2D position, int hopCount,
			double accumulatedDistance) {
		super(node);
		this.hopCount = hopCount;
		this.accumulatedDistance = accumulatedDistance;
	}

	private NodePositionDistance(NodePositionDistance npd) {
		super(npd.getNode());
		hopCount = npd.hopCount;
		accumulatedDistance = npd.accumulatedDistance;
	}

	/**
	 * Increments hop count by one and adds the hop distance to the accumulated
	 * distance
	 * 
	 * @param distance
	 */
	private void incHopAndDistance(double distance) {
		hopCount++;
		accumulatedDistance += distance;
	}

	public int getHopCount() {
		return hopCount;
	}

	/**
	 * As a node position passes hops, the hop size will be incremented by the
	 * intermediate node
	 * 
	 * @return
	 */
	public double getAccumulatedDistance() {
		return accumulatedDistance;
	}

	/**
	 * Creates a clone of the npd message for every receiver and adds the
	 * neighboring info to the npd to be stored in memory.
	 * <p>
	 * The receiver determines if it can measure the distance or not.
	 * <p>
	 * TODO ACTION for rage free or range-based setup edit here
	 * 
	 * @param neighbor
	 * @return
	 */
	public NodePositionDistance getHoppedClone(Neighbor neighbor) {
		NodePositionDistance npd = new NodePositionDistance(this);

		npd.incHopAndDistance(neighbor.getNoisyDistance());
		return npd;
	}

	public static Comparator<NodePositionDistance> COMPARATOR = new Comparator<NodePositionDistance>() {
		@Override
		public int compare(NodePositionDistance o1, NodePositionDistance o2) {
			return (o1.getAccumulatedDistance() - o2.getAccumulatedDistance() > 0) ? 1
					: -1;
		}
	};

	@Override
	public String toString() {
		return "NPD-" + getNode().getId()
				+ ZXControl.pointToString(getPosition()) + " HopCount: "
				+ hopCount + " ACC Distance: "
				+ ZXControl.doubleToString(accumulatedDistance);
	}
}
