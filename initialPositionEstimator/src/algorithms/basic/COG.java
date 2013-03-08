package algorithms.basic;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import node.Node;
import node.localization.tools.NodePositionDistance;
import node.type.Anchor;

public class COG {
	/**
	 * COG of the closest anchors in hopCount
	 */
	public static Point2D getInitCOG(Node node) {

		NodePositionDistance[] npdArr = node.getMemory().getAnchorsNPDs();

		int minHop = 1000;

		for (NodePositionDistance npd : npdArr) {
			if (minHop > npd.getHopCount())
				minHop = npd.getHopCount();
		}

		List<Anchor> minHopAnchors = new ArrayList<Anchor>();

		for (NodePositionDistance npd : npdArr) {
			if (minHop == npd.getHopCount())
				minHopAnchors.add((Anchor) npd.getNode());
		}

		double x = 0, y = 0;
		for (Anchor a : minHopAnchors) {
			x += a.getCalculatedCenter().getX();
			y += a.getCalculatedCenter().getY();
		}
		x /= minHopAnchors.size();
		y /= minHopAnchors.size();

		return new Point2D.Double(x, y);
	}
}
