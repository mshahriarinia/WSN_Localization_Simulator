package algorithms.ms;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import node.Node;
import node.localization.tools.NodePosition;

public class RangeFreeMassSpring {

	private int execCount = 0;

	private final int MAX_Exec_Count = 4 + 2;
	// 2 to allow farther neighbors to anchors start

	private int jumpLength;

	private Node node;

	private List<List<Node>> listListOutRangeNodes;

	/**
	 * process a list of out range nps until they are in range
	 * 
	 * @param node
	 */
	public RangeFreeMassSpring(Node node) {
		this.node = node;
		jumpLength = node.getRadius() / 5;
		listListOutRangeNodes = new ArrayList<List<Node>>();
		
		List<Node> listNotInRangeNodes = getNotInRangeNeighbors();
		List<Node> listNewNotInRangeNodes = getNewlyNotInRangeNeighbors(
				listListOutRangeNodes, listNotInRangeNodes);
		listListOutRangeNodes.add(listNewNotInRangeNodes);
	}

	/**
	 * TODO check for invalid ClusteredRegion cR estimated positions<br>
	 * <br>
	 * before calling this method the CalculatedCenter should be set.
	 */

	public Point2D getNextEstimate() {
		execCount++;

		List<Node> listNotInRangeNodes = getNotInRangeNeighbors();
		if (!listNotInRangeNodes.isEmpty()) {
			List<Node> listNewNotInRangeNodes = getNewlyNotInRangeNeighbors(
					listListOutRangeNodes, listNotInRangeNodes);
			listListOutRangeNodes.add(listNewNotInRangeNodes);

			Point2D jump = jumpTowardsTheOutRangeNodes(listListOutRangeNodes
					.get(0));

			if (isListInRange(jump, listListOutRangeNodes.get(0)))
				listListOutRangeNodes.remove(0);

			return jump;
		}
		return null;
	}

	private boolean isListInRange(Point2D newEstimate, List<Node> listNodes) {
		for (Node n : listNodes) {
			Point2D neighborPosition = node.getMemory()
					.getLatestNodePosition(n).getPosition();
			if (neighborPosition.distance(node.getCalculatedCenter()) > node
					.getRadius())
				return false;
		}
		return true;
	}

	private List<Node> getNewlyNotInRangeNeighbors(
			List<List<Node>> listListOutRangeNodes,
			List<Node> listNotInRangeNodes) {
		List<Node> listNewNodes = new ArrayList<Node>();
		boolean isContained;
		for (Node n : listNotInRangeNodes) {
			isContained = false;
			for (List<Node> ln : listListOutRangeNodes) {
				if (ln.contains(n))
					isContained = true;
			}
			if (!isContained)
				listNewNodes.add(n);
		}
		return listNewNodes;
	}

	/**
	 * each not in range neighbors causes a distance/node range force. <br>
	 * after computing the sigmaF, node will move as much as jumpLength * some
	 * random. <br>
	 * <br>
	 * jumpLength =
	 */
	private Point2D jumpTowardsTheOutRangeNodes(List<Node> ListOutRangeNodes) {
		Point2D currentPosition = node.getCalculatedCenter();

		Point2D sigmaF = getSigmaF(node, ListOutRangeNodes);

		double random = Math.random();

		Point2D jumpedPosition = new Point2D.Double(currentPosition.getX()
				+ sigmaF.getX() * jumpLength * random, currentPosition.getY()
				+ sigmaF.getY() * jumpLength * random);

		return jumpedPosition;
	}

	private Point2D getSigmaF(Node node, List<Node> listNPs) {
		Point2D sigmaF = new Point2D.Double();

		for (Node n : listListOutRangeNodes.get(0)) {
			updateSigmaF(sigmaF, node, node.getMemory()
					.getLatestNodePosition(n));
		}
		return sigmaF;
	}

	/**
	 * each np will cause distance/node radius force
	 */
	private void updateSigmaF(Point2D sigmaF, Node node, NodePosition np) {
		double nodeRange = node.getRadius();

		Point2D vector = Force.getVector(node, np);
		double vectorLength = Force.getVectorLength(vector);
		Point2D unitVector = new Point2D.Double(vector.getX() / vectorLength,
				vector.getY() / vectorLength);

		double nodeRangeMultiplier = vectorLength / nodeRange;
		sigmaF.setLocation(sigmaF.getX() + nodeRangeMultiplier
				* unitVector.getX(), sigmaF.getY() + nodeRangeMultiplier
				* unitVector.getY());
	}

	private List<Node> getNotInRangeNeighbors() {
		List<NodePosition> listNP = node.getMemory()
				.getNeighborsNodePositions();

		List<Node> listNodes = new ArrayList<Node>();

		for (NodePosition np : listNP) {
			if (node.getCalculatedCenter().distance(np.getPosition()) > node
					.getRadius())
				listNodes.add(np.getNode());
		}
		return listNodes;
	}

	public boolean isFinished() {
		return execCount > MAX_Exec_Count || listListOutRangeNodes.isEmpty();
	}
	
	public int getJumpLength() {
		return jumpLength;
	}
}
