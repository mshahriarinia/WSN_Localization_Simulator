package geom.checkerBoard;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;

import node.Node;
import node.NodeList;
import control.SimulationInfoContext;

/**
 * gets the rectangular areas made by drawing lines from references parallel to
 * the axes.
 * <p>
 * Assumes no two refs have same xes or ys.
 * 
 */
public class NodesCheckerBoard implements SimulationInfoContext {

	public static List<Rectangle2D> getListAreas(List<Node> list) {
		double[] xArr = null;
		double[] yArr = null;
		xArr = NodeList.getXArray(list);
		yArr = NodeList.getYArray(list);

		Arrays.sort(xArr);
		Arrays.sort(yArr);

		return FieldCheckerBoard.getListAreas(xArr, yArr);
	}
}