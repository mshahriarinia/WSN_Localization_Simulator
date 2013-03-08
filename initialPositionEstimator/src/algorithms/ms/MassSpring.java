package algorithms.ms;

import java.awt.geom.Point2D;

import node.Node;

public class MassSpring {
	private static final int RELAX_ITERATIONS = 20;
	private Node node;

	public MassSpring(Node node) {
		this.node = node;
	}

	private void relax() {
//		for (int i = 0; i < RELAX_ITERATIONS; i++) {
//			Point2D f = Force.getRangeBasedSigmaF(node);
//			// System.out.println(this + "  -- Force: "
//			// + Controller.pointToString(f));
//			Force.updateCalculatedCenter(node, f);
//		}
	}

	private void stateRefining() {
		// force
		// Point2D p1 = new Point2D.Double(getCalculatedCenter().getX(),
		// getCalculatedCenter().getY());
		// relax();
		// Point2D p2 = getCalculatedCenter();
		//
		// if (isLongDifference(p1, p2)) {
		// sendAssumedPosition();
		// return true;
		// }
		// return false;
	}
}
