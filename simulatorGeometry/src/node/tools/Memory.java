package node.tools;

import java.util.ArrayList;
import java.util.List;

import node.Node;
import node.Resetable;
import node.localization.tools.NodePosition;
import node.localization.tools.NodePositionDistance;
import node.type.Anchor;

/**
 * Contains long-term data to be kept in node's memory. received messages are
 * temporary and are not kept here, only their content is kept.
 * 
 */
public class Memory implements Resetable {

	private Node node;

	private List<NodePosition> listNodePosition;

	/**
	 * list of broadcast messages that the node has already relayed
	 */
	private List<TimedData> listRelayedMessages;

	/**
	 * This is a list to contain any type of data that later might be usefu; to
	 * be stored in memory.
	 */
	private List<TimedData> listVarietyData;

	public Memory(Node node) {
		this.node = node;
		listNodePosition = new ArrayList<NodePosition>();
		listRelayedMessages = new ArrayList<TimedData>();
		listVarietyData = new ArrayList<TimedData>();
	}

	public NodePosition getLatestNodePosition(Node node) {
		for (NodePosition nodePosition : listNodePosition) {
			if (nodePosition.getNode() == node) {
				return nodePosition;
			}
		}
		return null;
	}

	public List<NodePosition> getNeighborsNodePositions() {
		List<NodePosition> listNeighborsNPs = new ArrayList<NodePosition>();
		for (NodePosition np : listNodePosition) {
			if (node.isInNeighborArea(np.getNode()))
				listNeighborsNPs.add(np);
		}
		return listNeighborsNPs;
	}

	public void update(TimedData timedData) {
		if (timedData.getData() instanceof NodePositionDistance) {
			update((NodePositionDistance) timedData.getData());
		} else if (timedData.getData() instanceof NodePosition) {
			update((NodePosition) timedData.getData());
		} else {
			listVarietyData.add(timedData);
		}
	}

	/**
	 * NodePosition is only for direct neighbors that don't send hop count.
	 * 
	 * @param node
	 */
	private void update(NodePosition nodePosition) {
		for (NodePosition np : listNodePosition) {
			if (np.getNode() == nodePosition.getNode()) {
				listNodePosition.remove(np);
				break;
			}
		}
		listNodePosition.add(nodePosition);
	}

	/**
	 * Always save the shortest hop counts and shortest accumulated distance.
	 * <p>
	 * Each nodes either sends a node position or a npd not both
	 * <p>
	 * 
	 * @param nodePositionDistance
	 */
	private void update(NodePositionDistance nodePositionDistance) {
		double accDist = nodePositionDistance.getAccumulatedDistance();
		int hops = nodePositionDistance.getHopCount();

		NodePositionDistance oldNPD = getNPD(nodePositionDistance.getNode());
		if (oldNPD != null) {
			/**
			 * It can happen that the hop counts be more with less distance we
			 * assume the shorter of each. for example a wide 2 hop way and a
			 * narrow 3-hop where the 3 hops has less distance.
			 * <p>
			 * TODO ACTION range-based/free. range-based keep less-distant 3-hop
			 * than farther 2-hop range free 2 hop is better
			 */
			if (accDist > oldNPD.getAccumulatedDistance())
				accDist = oldNPD.getAccumulatedDistance();
			if (hops > oldNPD.getHopCount())
				hops = oldNPD.getHopCount();

			listNodePosition.remove(oldNPD);
		}
		/**
		 * No need to make a new object. but for better clarity and not changing
		 * the npd of a path, we create a new object here.
		 */
		NodePositionDistance newNPD = new NodePositionDistance(
				nodePositionDistance.getNode(),
				nodePositionDistance.getPosition(), hops, accDist);
		listNodePosition.add(newNPD);
	}

	private NodePositionDistance getNPD(Node node) {
		for (NodePosition np : listNodePosition) {
			if (np instanceof NodePositionDistance)
				if (np.getNode() == node) {
					return (NodePositionDistance) np;
				}
		}
		return null;
	}

	/**
	 * uses queue of all relayed messages.
	 * <p>
	 * 
	 * @param messageData
	 * @return
	 */
	public boolean hasAlreadyRelayed(TimedData tdNew) {
		if (tdNew.getData() instanceof NodePosition) {
			NodePosition npNew = (NodePosition) tdNew.getData();
			for (TimedData tdSaved : listRelayedMessages) {

				if (tdSaved.getData() instanceof NodePosition) {
					NodePosition npSaved = (NodePosition) tdSaved.getData();
					if ((npSaved.getNode() == npNew.getNode())
							&& (tdSaved.getCycleNo() == tdNew.getCycleNo())) {
						return true;
					}
				}
			}
		} else
			return listRelayedMessages.contains(tdNew);
		return false;
	}

	public void saveRelayMessage(TimedData timedData) {
		listRelayedMessages.add(timedData);
	}

	public List<Anchor> getAnchors() {
		List<Anchor> list = new ArrayList<Anchor>();
		for (NodePosition np : listNodePosition) {
			if (np.getNode() instanceof Anchor) {
				list.add((Anchor) np.getNode());
			}
		}
		return list;
	}

	/**
	 * 
	 * @return all heard anchors node-position-distances
	 */
	public NodePositionDistance[] getAnchorsNPDs() {
		List<Anchor> anchors = getAnchors();
		NodePositionDistance[] npdArr = new NodePositionDistance[anchors.size()];

		for (int i = 0; i < npdArr.length; i++) {
			npdArr[i] = (NodePositionDistance) getLatestNodePosition(anchors
					.get(i));
		}
		return npdArr;
	}

	public int getHopCount(Node node) {
		for (NodePosition np : listNodePosition) {
			if (np.getNode() == node) {
				return ((NodePositionDistance) np).getHopCount();
			}
		}
		throw new RuntimeException("Invalid node hop count required.");
	}

	public double getHopsDistance(Node node) {
		for (NodePosition np : listNodePosition) {
			if (np.getNode() == node) {
				return ((NodePositionDistance) np).getAccumulatedDistance();
			}
		}
		throw new RuntimeException("Invalid node hop count required.");
	}

	/**
	 * 
	 * @return This is a list to contain any type of data that later might be
	 *         useful to be stored in memory.
	 */
	public List<TimedData> getListVarietyData() {
		return listVarietyData;
	}

	public Message getMessageOfTimedData(TimedData timedData) {
		for (Message m : node.getInboxManager().getInbox()) {
			if (m.getTimedData().equals(timedData))
				return m;
		}
		return null;
	}

	@Override
	public void reset() {
		listNodePosition.clear();
		listRelayedMessages.clear();
		listVarietyData.clear();
	}
}