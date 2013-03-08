package simulator.localizable;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import algorithms.discreteField.clustering.Clusterable;
import algorithms.discreteField.clustering.HierarchicalClustering;

import control.ZXControl;

import node.Destination;
import node.ID;
import node.NodeList;
import node.localization.tools.NodePositionDistance;
import node.tools.Message;
import node.tools.TimedData;
import node.type.Anchor;

/**
 * At first send all messages: npd+angular to each neigh
 */
public class MSAnchor extends Anchor implements Clusterable {

	private int averageHopSize;

	private boolean hasBroadCastPosition = false;

	private boolean hasBroadCastNNSN = false;

	private Rectangle2D area;

	private HierarchicalClustering hierarchicalClustering;// TODO

	// average hop count to other anchors (used to estimate when to start
	// clustering)
	private int averageHopCount;

	public MSAnchor(NodeList nodeList, Rectangle2D area, double x, double y) {
		super(nodeList, x, y);
		setCalculatedCenter();
		this.area = area;
	}

	public MSAnchor(NodeList nodeList, Rectangle2D area, Point2D center,
			int radius) {
		super(nodeList, center, radius);
		setCalculatedCenter();
		this.area = area;
	}

	public MSAnchor(NodeList nodeList, Rectangle2D area, Point2D center) {
		super(nodeList, center);
		setCalculatedCenter();
		this.area = area;
	}

	@Override
	public void reset() {
		super.reset();
		hasBroadCastPosition = false;
		hasBroadCastNNSN = false;
		averageHopSize = 0;
	}

	@Override
	public boolean execute(int cycleNo) {

		boolean executed = false;

		boolean broadCastInThisCycle = false;
		if (!hasBroadCastPosition) {
			broadcastOwnPosition();

			// anchors will eventually know positions of their neighbors like
			// lostnodes will do
			// it doesnt differ at all if they wait until they actually get a
			// message or not

			hasBroadCastPosition = true;
			broadCastInThisCycle = true;
		}

		if (!hasBroadCastNNSN && !MSLostNode.IS_BASIC_ALGORITHM()) {
			if (getAngulationManager() != null) {
				getAngulationManager().sendNNSNMessages();
				hasBroadCastNNSN = true;
				broadCastInThisCycle = true;
			}
		}

		boolean broadCastAvgHopSizeInThisCycle = false;
		if (cycleNo == ZXControl.MAX_ANCHORS_BROADCAST + 1) {
			broadCastAvgHopSizeInThisCycle = true;
			broadCastAvgHopSize();
		}

		if (canStartClustering(cycleNo)) {
			executed = executed || state_HIERARCHICAL_CLUSTERING();
		}

		AverageHopSizeMessageHandler.handleAverageHopSizeMessages(this);
		executed = executed || getInboxManager().manageInbox();

		getInboxManager().clearReceivedMessageList();
		return broadCastInThisCycle || broadCastAvgHopSizeInThisCycle
				|| executed;
	}

	@Override
	public boolean canStartClustering(int cycleNo) {
		return cycleNo > ZXControl.MAX_ANCHORS_BROADCAST + averageHopCount / 2;
	}

	@Override
	public boolean state_HIERARCHICAL_CLUSTERING() {
		if (hierarchicalClustering == null) {
			hierarchicalClustering = new HierarchicalClustering(this);
			String report = "Node " + getId().getValue()
					+ " startes Hierarchical Clustering.";
			System.out.println(report);

			getExecutionReporter().writeln(report);
		}
		if (!hierarchicalClustering.isFinished()) {
			hierarchicalClustering.execute();

			return true;
		} else
			return false;
	}

	private void broadCastAvgHopSize() {
		double sumDistance = 0;
		int sumHops = 0;

		for (NodePositionDistance npd : getMemory().getAnchorsNPDs()) {
			sumDistance += getCalculatedCenter().distance(npd.getPosition());
			sumHops += npd.getHopCount();
		}

		if (getMemory().getAnchorsNPDs().length != 0) {
			averageHopCount = sumHops / getMemory().getAnchorsNPDs().length;
		}

		averageHopSize = (int) Math.ceil(sumDistance / sumHops);

		AnchorAvgHopSizeWrapper averageHopSize = new AnchorAvgHopSizeWrapper(
				this, this.averageHopSize);

		Message message = new Message(getId(), new Destination(
				ZXControl.MAX_ANCHORS_BROADCAST), averageHopSize);
		getInboxManager().send(message);
		getMemory().getListVarietyData().add(message.getTimedData());
	}

	public int getAverageHopSize() {
		return averageHopSize;
	}

	/**
	 * Anchors save their own broadcast message so that it is not relayed
	 */
	private void broadcastOwnPosition() {
		setCalculatedCenter();
		NodePositionDistance nodePositionDistance = new NodePositionDistance(
				this, getCenter(), 0, 0);
		TimedData td = new TimedData(getNodeList().getZXControl().getICycler(),
				nodePositionDistance);
		getMemory().saveRelayMessage(td);
		getInboxManager()
				.send(new Message(getId(), new Destination(
						ZXControl.MAX_ANCHORS_BROADCAST), nodePositionDistance));
	}

	/**
	 * LATER maybe we can change the initial position of anchors at later time
	 */
	private void setCalculatedCenter() {
		super.setCalculatedCenter(getCenter());
	}

	public Rectangle2D getArea() {
		return area;
	}

	@Override
	public HierarchicalClustering getHierarchicalClustering() {
		return hierarchicalClustering;
	}

}
