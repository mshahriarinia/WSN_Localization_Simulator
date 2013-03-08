package algorithms.discreteField.clustering;

import java.awt.geom.Point2D;

import control.ZXControl;

import node.ID;

/**
 * this could be broadcast with proper ttl
 * 
 * @author Morteza
 * 
 */
public class ClusteringMessageData {

	public ClusteringMessageData(ID nodeId, Point2D location, int depth) {
		this.nodeId = nodeId;
		this.location = location;
		this.depth = depth;
	}

	private ID nodeId;

	private Point2D location;

	public ID getNodeId() {
		return nodeId;
	}

	public Point2D getLocation() {
		return location;
	}

	public int getDepth() {
		return depth;
	}

	private int depth;

	@Override
	public String toString() {
		return "Cluster Message Data - Cluster Head: " + nodeId
				+ " at location: " + ZXControl.pointToString(location);
	}
}
