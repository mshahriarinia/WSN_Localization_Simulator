package geom.image;

import geom.Circle;
import geom.checkerBoard.FieldCheckerBoard;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import node.Node;
import node.type.Anchor;

public class FieldArray {

	public static final int IMPOSSIBLE = -1;

	public static void setImpossibleAreas(FieldCheckerBoard fieldCB, Node node,
			double[][] area) {
		if (node.isLogged())
			System.out.print("");
		for (int i = 0; i < area.length; i++) {
			for (int j = 0; j < area.length; j++) {
				Rectangle2D rect = fieldCB.getRectangle(i, j);
				if (isImpossible(node, rect)) {
					area[i][j] = IMPOSSIBLE;
				}
			}
		}
	}

	/**
	 * TODO assign those rectangles that contain less than a specified percent
	 * of the circle as impossible. Make a 3D graph of (circle percent to set
	 * impossible)-(average degree)-(percent of wrongly set impossible rects) <br>
	 * <br>
	 * 
	 * @param node
	 * @param rect
	 * @return
	 */
	public static boolean isImpossible(Node node, Rectangle2D rect) {

		List<Anchor> list = node.getMemory().getAnchors();
		int ANCHOR_RADIUS = node.getNodeList().getZXControl().ANCHOR_RADIUS;
		/* remove */
		for (Anchor anchor : list) {
			/*
			 * percent of a rect where resides inside a one hop distance of an
			 * anchor where the node is farther is zero
			 */
			int hopCount = node.getMemory().getHopCount(anchor);
			Point2D center = anchor.getCenter();

			if (node.isLogged())
				System.out.print("");

			boolean containsALLInOneHop = Circle.containsAll(center,
					ANCHOR_RADIUS, rect);

			boolean containsFarALL = Circle.containsAll(center, hopCount
					* ANCHOR_RADIUS, rect);

			boolean containsFarSome = Circle.containsSome(center, hopCount
					* ANCHOR_RADIUS, rect);

			if (hopCount > 1 && containsALLInOneHop) {
				return true;
			} else if (!(containsFarSome || containsFarALL)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * used to choose the closest pole to the weighted center instead of just
	 * choosing weighted center
	 * 
	 * @param arr
	 *            TODO gePoles
	 * @return
	 */
	public static double[][] gePoles(double[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {

			}
		}
		return null;
	}

}
