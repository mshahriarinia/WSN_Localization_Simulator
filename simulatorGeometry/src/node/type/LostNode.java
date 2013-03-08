package node.type;

import java.awt.geom.Point2D;

import node.Node;
import node.NodeList;

public abstract class LostNode extends Node {

	public LostNode(NodeList nodeList, double x, double y) {
		super(nodeList, x, y);
	}

	public LostNode(NodeList nodeList, Point2D center, int radius) {
		super(nodeList, center, radius);
	}

	public LostNode(NodeList nodeList, Point2D center) {
		super(nodeList, center);
	}

	@Override
	public String toString() {
		return "L" + super.toString();
	}
}
