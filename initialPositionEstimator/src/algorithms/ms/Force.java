package algorithms.ms;

import java.awt.geom.Point2D;

import node.Node;
import node.localization.tools.NodePosition;

public class Force {

	public static Point2D getVector(Node node, NodePosition np) {
		Point2D p1 = node.getCalculatedCenter();
		Point2D p2 = np.getPosition();
		return new Point2D.Double(p2.getX() - p1.getX(), p2.getY() - p1.getY());
	}

	public static double getVectorLength(Point2D vector) {
		return Math.sqrt(vector.getX() * vector.getX() + vector.getY()
				* vector.getY());
	}

	//	
	//	
	//	
	//	
	//	
	//	
	//	
	//	
	//	
	//	
	//	
	//	
	//	
	//
	//	
	//	
	//	

	// // private static final float FORCE_FRACTION_CAUSE = 2.5F;
	//
	// public static Point2D getRangeBasedSigmaF(Node node) {
	// List<Neighbor> listNeighbors = node.getNeighbors();
	// double x = 0, y = 0;
	// double actualLen = 0;
	// for (Neighbor neighbor : listNeighbors) {
	//
	// NodePosition latestNeighborPosition = node.getMemory()
	// .getLatestNodePosition(neighbor.getOtherNode(node));
	//
	/**
	 * TODO Available information:
	 * <p>
	 * 1. # of hops of each side of a link to anchors.
	 * <p>
	 * 2. Certainty (number of possible clustered regions and population of
	 * each)
	 * <p>
	 * 3. # of neighbors of each
	 */
	//
	// if (latestNeighborPosition != null) {
	//
	// actualLen = node.getNoisyDistance(latestNeighborPosition
	// .getNode());
	//
	// Point2D v = Force.getVector(node, latestNeighborPosition);
	//
	// double vLen = ZXControl.getVectorLength(v);
	//
	// double sign = -Math.signum(actualLen - vLen);
	//
	// // fraction = (actualLen - vLen) / (vLen * 3);
	// // y = y + fraction;// * v.getY();
	//
	// x = x + sign * v.getX();// / vLen;// unit vector
	//
	// y = y + sign * v.getY();
	// }
	// }
	// return new Point2D.Double(x, y);
	// }
	//
	//	
	//
	// public static void updateCalculatedCenter(Node node, Point2D sigmaF) {
	// Point2D p = node.getCalculatedCenter();
	// double x, y;
	// // x = p.getX() + FORCE_FRACTION_CAUSE * sigmaF.getX();
	// // y = p.getY() + FORCE_FRACTION_CAUSE * sigmaF.getY();
	//
	// // if (sigmaF.getX() > 0)
	// // x = -1;
	// // else if (sigmaF.getX() < 0)
	// // x = 1;
	// // else
	// // x = 0;
	// //
	// // if (sigmaF.getY() > 0)
	// // y = -1;
	// // else if (sigmaF.getY() < 0)
	// // y = 1;
	// // else
	// // y = 0;
	//
	// // x = - Math.max(-5, Math.min(5, sigmaF.getX()));
	// // y = - Math.max(-5, Math.min(5, sigmaF.getY()));
	//
	// double vLen = ZXControl.getVectorLength(sigmaF);
	// vLen = (vLen == 0) ? 1 : vLen;
	//
	// x = p.getX() - sigmaF.getX() / (2 * vLen);
	// y = p.getY() - sigmaF.getY() / (2 * vLen);
	//
	// p.setLocation(x, y);
	// }
	/**
	 * TODO To improve the forces used, it's a good idea to use a ratio of the
	 * measured length of the mutual nodes. Because, those that are at a close
	 * vicinity will have a better accuracy at determining the distance, whilst
	 * those further have lower certainty of the measurements. For example, a
	 * unit of error in calculated distance in close nodes has more impact and
	 * importance than a unit error between two nodes with maximum range
	 * distances.
	 * <p>
	 * TODO A solution to this problem could be to divide the force attained by
	 * subtracting the calculated node positions from the actual distance, by
	 * the average of the initial and refined calculated distances
	 * </p>
	 */

	/**
	 * TODO Distance Vector length: future policies can be used based on the
	 * certainty of the position of the node, it's mass, current speed, it's
	 * spring constant
	 */
}