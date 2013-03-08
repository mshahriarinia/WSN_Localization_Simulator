package node.localization.tools.sectorBased;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import node.Destination;
import node.Neighbor;
import node.Node;
import node.Resetable;
import node.tools.Message;
import control.ZXControl;

public class AngulationManager implements Resetable {

	private List<Sector> listSectors;

	private Node node;

	public AngulationManager(Node node) {
		this.node = node;
	}

	/**
	 * Send NodeNeighborSectorNumber of each neighbor to itself
	 * 
	 * @param node
	 */
	public void sendNNSNMessages() {
		NodeNeighborSectorNo npnsn;
		for (Neighbor n : node.getNeighbors()) {
			npnsn = new NodeNeighborSectorNo(node, listSectors, n);

			node.getInboxManager().send(
					new Message(node.getId(), new Destination(n.getOtherNode(
							node).getId()), npnsn));
		}
	}

	/**
	 * This means that when we are after MAX_ANCHORS_RECEPTION_HOPS at least a
	 * relay message has been received from every each of neighbors to be able
	 * to detect their sector.==> When a node knows its neighbor it means it has
	 * received sth from it, there for measured angle.
	 * 
	 * @param node
	 * @return
	 */
//	private boolean canSendNNSNMessages() {
//		if (ZXControl.ANGLE_ENABLED)
//			return node.getNodeList().getZXControl().getICycler().getCycleNo() > ZXControl.MAX_ANCHORS_BROADCAST;
//		else
//			return false;
//	}

	/**
	 * each time a node changes its calculated center this needs to be updated
	 */
	public void initListSectors() {
		List<Sector> listSectors = new ArrayList<Sector>();
		double sectorAngle = 2 * Math.PI / ZXControl.SECTORS_COUNT;
		Point2D A = node.getCalculatedCenter();
		Point2D B;
		double angle;
		double radius = node.getRadius();
		for (int i = 0; i < ZXControl.SECTORS_COUNT; i++) {
			angle = sectorAngle * i;
			B = new Point2D.Double(A.getX() + radius * Math.cos(angle),
					A.getY() + radius * Math.sin(angle));
			Sector sector = new Sector(B, A, radius, sectorAngle);
			listSectors.add(sector);
		}
		this.listSectors = listSectors;
	}

	public List<Sector> getListSectors() {
		return listSectors;
	}

	public Node getNode() {
		return node;
	}

	@Override
	public void reset() {
		if (listSectors != null)
			listSectors.clear();
	}
}
