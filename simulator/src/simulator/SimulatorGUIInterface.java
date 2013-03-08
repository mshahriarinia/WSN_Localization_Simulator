package simulator;

import guiInterface.GUIConstants;
import guiInterface.IPrimitiveGUIInterface;
import guiInterface.RGB;

import java.util.List;

import node.Node;
import node.NodeList;
import node.type.Anchor;
import simulator.localizable.MSAnchor;
import simulator.localizable.MSLostNode;
import algorithms.discreteField.clustering.Clusterable;
import algorithms.discreteField.clustering.LevelClusterStatus;
import control.ZXControl;

/**
 * Provides the methods to draw simulator areas and nodes.
 */
public class SimulatorGUIInterface {

	private IPrimitiveGUIInterface guiInterface;

	private List<Node> nodes;

	private NodeList nodeList;

	public SimulatorGUIInterface(IPrimitiveGUIInterface guiInterface,
			NodeList nodeList) {
		this.guiInterface = guiInterface;
		this.nodeList = nodeList;
		nodes = nodeList.getListNodes();
	}

	public void drawAll() {
		guiInterface.resetCanvas();
		drawNetworkArea();
		drawEdges();
		drawNodes();

		drawRefsAreas1();
		// if (fieldGenerator != null)
		// drawForbiddenAreas(fieldGenerator.getForbiddenAreas());
	}

	private void drawNetworkArea() {
		guiInterface.drawRect(GUIConstants.RECT_COLOR, 0, 0,
				ZXControl.FIELD_WIDTH, ZXControl.FIELD_HEIGHT);
	}

	private void drawNodes() {
		for (Object o : nodes) {
			drawNode((Node) o);
		}
	}

	private void drawCalculatedNodes() {
		for (Object o : nodes) {
			drawCalculatedNode((Node) o);
		}
	}

	public void drawNode(Node node) {
		if (node instanceof Anchor)
			drawTrueNode(GUIConstants.ANCHOR_COLOR, node);
		else {
			drawTrueNode(GUIConstants.LOST_NODE_TRUE_LOC_COLOR, node);
			drawTrueNode(GUIConstants.LOST_NODE_COLOR, node);

			if (node.getCalculatedCenter() != null)
				drawCalculatedNode(node);
		}
		// drawNodeID(node);
	}

	private RGB getRGB(Node node) {
		if (node instanceof Anchor)
			return GUIConstants.ANCHOR_COLOR;
		else
			return GUIConstants.LOST_NODE_COLOR;
	}

	public void drawCalculatedNode(Node node) {
		if (node.getCalculatedCenter() != null) {
			guiInterface.drawPoint(getRGB(node), (int) node
					.getCalculatedCenter().getX(), (int) node
					.getCalculatedCenter().getY());
			drawNodeID(node);
		}
	}

	private void drawNodeID(Node node) {
		guiInterface.drawString(getRGB(node), node.getId().toString(),
				(int) node.getCenter().getX(), (int) node.getCenter().getY());
	}

	public void drawRefsAreas1() {
		// List<Anchor> refs = new ArrayList<Anchor>();
		// List<Node> temp = nodes;
		// for (Node n : temp) {
		// if (n instanceof Anchor)
		// refs.add((Anchor) n);
		// }

		// List<Rectangle2D> areas = ((FieldCheckerBoard) nodeList
		// .getSimulationInfoContext()).getListAreas();
		// for (Rectangle2D rect : areas) {
		// guiInterface.drawRect(GUIConstants.LOST_NODE_TRUE_LOC_COLOR,
		// (int) rect.getMinX(), (int) rect.getMinY(), (int) rect
		// .getWidth(), (int) rect.getHeight());
		// }
	}

	private void drawTrueNode(RGB rgb, Node node) {
		int x = (int) node.getCenter().getX();
		int y = (int) node.getCenter().getY();
		guiInterface.drawPoint(rgb, x, y);

		if (node instanceof Clusterable) {
			Clusterable c = (Clusterable) node;
			if (c.getHierarchicalClustering() != null) {
				List<LevelClusterStatus> listLCS = c
						.getHierarchicalClustering().getListLCS();
				for (LevelClusterStatus lcs : listLCS) {
					if (lcs.getClusterHeadID() == node.getId()) {
						int radius = lcs.getLevel()
								* node.getNodeList().getZXControl().NODE_RADIUS;
						guiInterface.drawCircle(rgb, x, y, radius);
					}
				}
			}
		}
	}

	private void drawEdges() {
		Node nodeI, nodeJ;
		for (int i = 0; i < nodes.size(); i++) {
			nodeI = nodes.get(i);
			for (int j = i + 1; j < nodes.size(); j++) {
				nodeJ = nodes.get(j);
				if (nodeI.isInNeighborArea(nodeJ))
					guiInterface.drawLine(GUIConstants.EDGE_COLOR, (int) nodeI
							.getCenter().getX(),
							(int) nodeI.getCenter().getY(), (int) nodeJ
									.getCenter().getX(), (int) nodeJ
									.getCenter().getY());
			}
		}
	}

	private void drawCalculatedEdges() {
		Node nodeI, nodeJ;
		for (int i = 0; i < nodes.size(); i++) {
			nodeI = nodes.get(i);
			for (int j = i + 1; j < nodes.size(); j++) {
				nodeJ = nodes.get(j);
				if (nodeI.isInNeighborArea(nodeJ))
					if (nodeI.getCalculatedCenter() != null
							&& nodeJ.getCalculatedCenter() != null)
						guiInterface.drawLine(GUIConstants.EDGE_COLOR,
								(int) nodeI.getCalculatedCenter().getX(),
								(int) nodeI.getCalculatedCenter().getY(),
								(int) nodeJ.getCalculatedCenter().getX(),
								(int) nodeJ.getCalculatedCenter().getY());
			}
		}
	}

	public void drawLogNodeCircles() {
		Node logNode = nodeList.getNodeID(nodeList.getZXControl().LOG_NODE);
		for (Node n : logNode.getMemory().getAnchors()) {
			if (n instanceof Anchor) {
				drawLogNodeCircle(logNode, (Anchor) n);

				System.out.println(n + "   getHopCount   "
						+ (int) logNode.getMemory().getHopCount(n)
						+ "   getHopsDistance   "
						+ (int) logNode.getMemory().getHopsDistance(n));
			}
		}
	}

	private void drawLogNodeCircle(Node nLOG, Anchor n) {
		int averageHopSize = 0;
		if (nLOG instanceof MSLostNode)
			averageHopSize = ((MSLostNode) nLOG).getAverageHopSize();
		else if (nLOG instanceof MSAnchor)
			averageHopSize = ((MSAnchor) nLOG).getAverageHopSize();
		if (guiInterface != null) {
			guiInterface.drawCircle(GUIConstants.LOST_NODE_TRUE_LOC_COLOR,
					(int) n.getCenter().getX(), (int) n.getCenter().getY(),
					// (int) nLOG.getMemory().getHopsDistance(n));
					nLOG.getMemory().getHopCount(n) * averageHopSize);// zxControl.ANCHOR_RADIUS);
		}
	}

	// private void drawForbiddenAreas(Rectangle2D[] areas) {
	// if (areas != null)
	// for (Rectangle2D area : areas) {
	// guiInterface.drawRect(GUIConstants.RECT_COLOR, (int) area
	// .getMinX(), (int) area.getMinY(),
	// (int) area.getWidth(), (int) area.getHeight());
	// }
	// }

	public void drawAllCalculated() {
		drawNetworkArea();
		drawCalculatedEdges();
		drawCalculatedNodes();
	}
}
