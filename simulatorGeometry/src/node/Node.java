package node;

import geom.MyRectArea;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import node.localization.tools.NodePositionDistance;
import node.localization.tools.sectorBased.AngulationManager;
import node.tools.InboxManager;
import node.tools.Memory;
import reporter.ExecutionReporter;
import control.ZXControl;

/**
 * TODO Here we have not implemented code for multi-hop direct message
 * reception.<br>
 * <br>
 * 
 * TODO transformation of the coordinate system <br>
 * <br>
 * TODO each node can have the square mesh with the true directions, but with
 * the transformation difference of its own; which can not be solved because of
 * the lack of the equations. So it will find the true limits for its
 * coordinates, if they are ever worth it.
 */
public abstract class Node implements Resetable {

	private ID id;

	private Point2D center;

	private Point2D calculatedCenter;

	private int radius;

	private List<Neighbor> listNeighbors;

	public boolean isReceiveMode() {
		return receiveMode;
	}

	private InboxManager inboxManager;

	private AngulationManager angulationManager;

	private boolean receiveMode = true;

	private Memory memory;

	private NodeList nodeList;

	private ExecutionReporter executionReporter;

	public Node(NodeList nodeList, Point2D center, int radius) {
		this.center = center;
		this.radius = radius;

		this.nodeList = nodeList;

		id = new ID(this);
		listNeighbors = new ArrayList<Neighbor>();
		inboxManager = new InboxManager(this);
		if (ZXControl.ANGLE_ENABLED)
			angulationManager = new AngulationManager(this);

		memory = new Memory(this);

		this.nodeList.addNode(this);

		executionReporter = getNodeList().getZXControl().getICycler()
				.getExecutionReporter();
	}

	public InboxManager getInboxManager() {
		return inboxManager;
	}

	public AngulationManager getAngulationManager() {
		return angulationManager;
	}

	public Node(NodeList nodeList, Point2D center) {
		this(nodeList, center, nodeList.getZXControl().NODE_RADIUS);
	}

	public Node(NodeList nodeList, double x, double y) {
		this(nodeList, new Point2D.Double(x, y),
				nodeList.getZXControl().NODE_RADIUS);
	}

	public NodeList getNodeList() {
		return nodeList;
	}

	protected void initCalculatedCenter(Node n) {
		Random rX = new Random();
		Random rY = new Random();

		double x = n.getCenter().getX() - rX.nextDouble()
				* (n.getRadius() / 10.0);
		double y = n.getCenter().getY() - rY.nextDouble()
				* (n.getRadius() / 10);
		n.setCalculatedCenter(new Point2D.Double(x, y));

	}

	public Point2D getCalculatedCenter() {
		return calculatedCenter;
	}

	public void setCalculatedCenter(Point2D assumedCenter) {
		this.calculatedCenter = assumedCenter;
		MyRectArea.setInDomainValue(calculatedCenter, 0, ZXControl.FIELD_WIDTH,
				0, ZXControl.FIELD_HEIGHT);
		if (angulationManager != null)
			angulationManager.initListSectors();
	}

	public Point2D getCenter() {
		return center;
	}

	public int getRadius() {
		return radius;
	}

	public Memory getMemory() {
		return memory;
	}

	public double getRealDistance(Node node) {
		return getCenter().distance(node.getCenter());
	}

	/**
	 * 
	 * @param node
	 * @return Noisy distance to a neighbor.
	 */
	public double getNoisyDistance(Node node) {
		for (Neighbor neighbor : listNeighbors) {
			if (neighbor.includes(node))
				return neighbor.getNoisyDistance();
		}
		throw new RuntimeException("Not neighbor,  required noisy distance");
	}

	public boolean isInNeighborArea(Node node) {
		return getRealDistance(node) <= getRadius();
	}

	/**
	 * only called when the node is being added to the total node list and while
	 * setting the neighboring nodes
	 * 
	 * @param neighbor
	 */
	protected void addNeighbor(Neighbor neighbor) {
		if (!listNeighbors.contains(neighbor))
			listNeighbors.add(neighbor);
	}

	public ID getId() {
		return id;
	}

	public List<Neighbor> getNeighbors() {
		return listNeighbors;
	}

	public Neighbor getNeighbor(ID id) {
		for (Neighbor n : listNeighbors) {
			if (n.getOtherNode(this).getId().equals(id))
				return n;
		}
		throw new RuntimeException("Invalid neighbor required. " + this + " "
				+ id);
	}

	/**
	 * wait time depending on hop count
	 * 
	 * @return
	 */
	public int getMinHop2Anchors() {
		int minHop = Integer.MAX_VALUE;
		for (NodePositionDistance npd : getMemory().getAnchorsNPDs()) {
			if (minHop > npd.getHopCount())
				minHop = npd.getHopCount();
		}
		if (minHop == Integer.MAX_VALUE)
			minHop = -1;
		return minHop;
	}

	@Override
	public String toString() {
		String s = id.getValue() + ZXControl.pointToString(getCenter())
				+ ZXControl.pointToString(getCalculatedCenter()) + " Dist: ";
		if (getCalculatedCenter() == null)
			s += "null";
		else
			s += ZXControl.doubleToString(getCenter().distance(
					getCalculatedCenter()));
		return s;
	}

	/**
	 * Used to debug logged node.
	 * 
	 * @return
	 */
	public boolean isLogged() {
		return getId().getValue() == getNodeList().getZXControl().LOG_NODE;
	}

	/**
	 * the executable code of each node
	 * 
	 * @return true if anything was executed, false if there was nothing new to
	 *         execute
	 */
	public abstract boolean execute(int cycleNo);

	@Override
	public void reset() {
		memory.reset();
		inboxManager.reset();
		angulationManager.reset();
		calculatedCenter = null;
	}

	//public void actClusterHead() {
		/**
		 * broadcast cluster head location. <br>
		 * all nodes store routing table regarding cluster-heads, besides their
		 * calculated physical location <br>
		 * determine with what TTL of packets how many of cluster heads will
		 * know (to grow their count exponentially, no: if everybody knows about
		 * everybody there is no asking required! so only up to that percent is
		 * allowed to know about a cluster head.) <br>
		 * clusters can if a cluster does not have anchors: how do they
		 * localize? anchor-free localization -
		 */

	//}

	public ExecutionReporter getExecutionReporter() {
		return executionReporter;
	}

}
