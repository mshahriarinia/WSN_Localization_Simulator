package algorithms.basic;

import java.awt.geom.Point2D;
import java.util.Arrays;

import node.Node;
import node.localization.tools.NodePositionDistance;

public class Proximity {
	public static Point2D getInitProximity(Node node) {
		NodePositionDistance[] npdArr = node.getMemory().getAnchorsNPDs();
		Arrays.sort(npdArr, NodePositionDistance.COMPARATOR);
		return new Point2D.Double(npdArr[0].getPosition().getX(), npdArr[0]
				.getPosition().getY());
	}
}
