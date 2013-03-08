package algorithms.centralizedBestAnchoredFirst;

import java.awt.geom.Point2D;

import simulator.localizable.MSLostNode;

import algorithms.dv.DV;

import node.ID;
import node.Node;
import node.NodeList;
import node.localization.tools.NodePositionDistance;

/**
 * In this algorithm we have a centralized greedy manner at which chooses the
 * next ndoe to be localized based on how many anchors it has in its commodity
 * and after localization it changes its type into being an anchor.
 * 
 * @author Morteza
 * @since May 17, 2011
 * 
 */
public class CentralizedBestAnchoredFirst {
	private Node node;

	int lastExecutedCycle;

	public CentralizedBestAnchoredFirst(Node node) {
		this.node = node;
		lastExecutedCycle = -1;
	}

	public Point2D getEstimate() {
		int currentCycle = node.getNodeList().getZXControl().getICycler()
				.getCycleNo();
		if (lastExecutedCycle < currentCycle) {
			lastExecutedCycle = currentCycle;
			ID id = localizeNextNode(node.getNodeList());
			if (node.getId() == id)
				return node.getCalculatedCenter();
		}

		return null;
	}

	private ID localizeNextNode(NodeList nodeList) {

		MSLostNode msLostNode = getNextNodeToLocalize(nodeList);
		if (msLostNode != null) {
			msLostNode.setCalculatedCenter(DV.getInitDV(msLostNode));
			return msLostNode.getId();
		}
		return null;
	}

	/**
	 * Two policies to choose: 1. node which the sum of its distance to anchors
	 * 2. node closest to one anchor.
	 * 
	 * @param nodeList
	 * @return
	 */
	private MSLostNode getNextNodeToLocalize(NodeList nodeList) {

		// node with minimum of sum of distances to anchors.
		int minimumSumDistToAnchor = Integer.MAX_VALUE;
		MSLostNode selectedNode = null;
		for (Node n : nodeList.getListNodes()) {

			if (n instanceof MSLostNode && n.getCalculatedCenter() == null) {

				NodePositionDistance npdArr[] = n.getMemory().getAnchorsNPDs();
				int sumDist = 0;
				for (int i = 0; i < npdArr.length; i++) {
					sumDist += npdArr[i].getHopCount();
				}
				System.out.println(node.getNodeList().getZXControl()
						.getICycler().getCycleNo()
						+ " " + node + "checking " + n);
				if (minimumSumDistToAnchor > sumDist) {
					minimumSumDistToAnchor = sumDist;
					selectedNode = (MSLostNode) n;
				}

			}
		}
		return selectedNode;
	}

}
