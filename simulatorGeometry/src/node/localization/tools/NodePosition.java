package node.localization.tools;

import java.awt.geom.Point2D;

import node.Node;
import control.ZXControl;

/**
 * Contains a node and a save d position of the node at the time of receiving a
 * message concerning the position of the node.
 * <p>
 * Used node instead of ID. node can show if it's anchor or not, to prevent
 * further low level handling of node types.
 * <p>
 * It does not have set methods. once a node creates a node position, nodes can
 * just send or store it. <b>Nodes are not allowed to change the contents</b>,
 * because it's stored it the memory of all nodes.
 * 
 */
public class NodePosition {

	private Node node;

	private Point2D position;

	public NodePosition(Node node) {
		this.node = node;
		this.position = node.getCalculatedCenter();
	}

	public Node getNode() {
		return node;
	}

	public Point2D getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return "NP-" + node.getId() + ZXControl.pointToString(getPosition());
	}
}
