package node.type;

import java.awt.geom.Point2D;

import node.Node;
import node.NodeList;
import control.ZXControl;

public abstract class Anchor extends Node {
	public Anchor(NodeList nodeList, double x, double y) {
		super(nodeList, x, y);
	}

	public Anchor(NodeList nodeList, Point2D center, int radius) {
		super(nodeList, center, radius);
	}

	public Anchor(NodeList nodeList, Point2D center) {
		super(nodeList, center);
	}

	@Override
	protected void initCalculatedCenter(Node n) {
		super.setCalculatedCenter(getCenter());
	}

	@Override
	public String toString() {
		return "A" + getId().getValue() + ZXControl.pointToString(getCenter());
	}
}
