package node.tools;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import node.Destination;
import node.ICycler;
import node.ID;
import node.Neighbor;
import node.Node;
import node.Resetable;
import node.localization.tools.NodePositionDistance;
import control.ZXControl;

public class InboxManager implements Resetable {

	private Node node;

	private List<Message> inbox;

	public InboxManager(Node node) {
		this.node = node;
		inbox = new ArrayList<Message>();
	}

	public boolean manageInbox() {
		preProcessInbox();
		boolean executed = readInbox();
		boolean anythingToRelay = relayBroadcastMessages();

		return executed || anythingToRelay;
	}

	/**
	 * Adds the hop count and neighbor distance to multi-hop localization
	 * messages
	 */
	private void adjustMultiHopMessages() {

		if (node.isLogged())
			System.out.print("");

		ICycler iCycler = node.getNodeList().getZXControl().getICycler();

		List<Message> multiHopMessages = new ArrayList<Message>();
		List<Message> adjustedMultiHopMessages = new ArrayList<Message>();

		for (Message message : inbox) {
			if (iCycler.isCurrent(message.getCycleNo())
					&& message.getTo().getDestination() == ID.BROADCAST) {

				Message newMessage;

				Object data = message.getTimedData().getData();
				if (data instanceof NodePositionDistance) {

					NodePositionDistance hoppedCloneNPD = ((NodePositionDistance) data)
							.getHoppedClone(node.getNeighbor(message.getFrom()));

					// keep the timed data to the time of the actual
					// originator
					TimedData cloneTimeData = message.getTimedData().clone();
					cloneTimeData.setData(hoppedCloneNPD);

					// Why a node position from a neighbor is broadcast?
					// Irrelevant question: since node positions are not multi
					// hop messages they will not get down to here anyway/
					newMessage = new Message(node.getId(), new Destination(
							message.getTo().getTtl() - 1), cloneTimeData);
					newMessage.setCurrent();

					adjustedMultiHopMessages.add(newMessage);
					multiHopMessages.add(message);
				}

			}
		}

		inbox.removeAll(multiHopMessages);
		inbox.addAll(adjustedMultiHopMessages);
	}

	/**
	 * Adjust multi hop messages(Adds the hop count and neighbor distance to
	 * multi-hop localization messages.) <br>
	 * <br>
	 * This is the place to add any other required pre-processes.
	 */
	private void preProcessInbox() {
		adjustMultiHopMessages();
	}

	/**
	 * 
	 * @return true if there is any message for the node
	 */
	private boolean readInbox() {
		// List<Message> inbox = getInbox();
		if (inbox.isEmpty())
			return false;

		ICycler iCycler = node.getNodeList().getZXControl().getICycler();
		for (Message message : inbox) {
			if (iCycler.isCurrent(message.getCycleNo())
					&& !isBadMessage(message)) {
				handle(message);
			}
		}
		return true;
	}

	/**
	 * Do the proper handlings for the message: either save it or update
	 * somewhere, etc
	 * 
	 * @param message
	 */
	private void handle(Message message) {
		node.getMemory().update(message.getTimedData());
	}

	/**
	 * A geographic messages not a relay, but has a predetermined destination.
	 * 
	 * @return true if there was any broadcast message to relay.
	 */
	private boolean relayBroadcastMessages() {
		ICycler iCycler = node.getNodeList().getZXControl().getICycler();
		boolean anythingToRelay = false;
		for (Message message : inbox) {
			if (iCycler.isCurrent(message.getCycleNo())
					&& message.getTo().getDestination() == ID.BROADCAST
					&& message.getTo().getTtl() > 0
					&& !node.getMemory().hasAlreadyRelayed(
							message.getTimedData())) {

				TimedData td = message.getTimedData();

				if (td.getData() instanceof NodePositionDistance)
					if (((NodePositionDistance) td.getData()).getHopCount() > ZXControl.MAX_ANCHORS_BROADCAST)
						continue;

				iCycler.getExecutionReporter().write("brdcast\t\t");

				Destination destination = new Destination(message.getTo()
						.getTtl() - 1);

				Message newMessage = new Message(node.getId(), destination, td);
				send(newMessage);
				node.getMemory().saveRelayMessage(td);
				anythingToRelay = true;
			}
		}

		return anythingToRelay;
	}

	/**
	 * returns true if not related message and those messages that have already
	 * been relayed but received from another neighbor again
	 */
	private boolean isBadMessage(Message message) {
		return isNotRelated(message) || isAlreadyRelayed(message);
	}

	private boolean isNotRelated(Message message) {
		Destination destination = message.getTo();
		if (destination.getDestination() instanceof ID) {
			if (!(destination.getDestination() == node.getId())
					&& !(destination.getDestination() == ID.BROADCAST)) {
				return true;
			}
		}

		return false;
	}

	private boolean isAlreadyRelayed(Message message) {
		return node.getMemory().hasAlreadyRelayed(message.getTimedData());

	}

	private boolean receive(Message message) {
		ICycler iCycler = node.getNodeList().getZXControl().getICycler();
		iCycler.getExecutionReporter().writeln(
				"\t\t\t\treceived in " + node.getId() + " " + message);
		if (node.isReceiveMode()) {
			getInbox().add(message);
			return true;
		}
		return false;
	}

	/**
	 * sends direct messages directly.
	 * 
	 * @param message
	 */
	public void send(Message message) {

		/*
		 * double minDist = Double.max; Neighbor nextHop = null; for(Neigbor n:
		 * listNeighbors){ if(destination.distnance(n) < minDist){ minDist =
		 * destination.distnance(n); nextHop = n; }
		 */

		node.getNodeList().incMessageCount();

		ICycler iCycler = node.getNodeList().getZXControl().getICycler();
		iCycler.getExecutionReporter().writeln("\t\tsend " + message);
		iCycler.getExecutionReporter().writeln("\t\t\t received in { ");

		Object destination = message.getTo().getDestination();

		if (destination instanceof ID) {
			if (destination == ID.BROADCAST) {
				for (Neighbor neighbor : node.getNeighbors()) {
					Node neighborNode = neighbor.getOtherNode(node);
					neighborNode.getInboxManager().receive(message);
				}
			} else {
				node.getNodeList().getNodeID(((ID) destination).getValue())
						.getInboxManager().receive(message);
			}
		} else {

			Point2D p = (Point2D) destination;
			double minDist = Double.MAX_VALUE;
			Node nextHop = null;
			for (Neighbor n : node.getNeighbors()) {
				Node neighborNode = n.getOtherNode(node);
				if (p.distance(neighborNode.getCalculatedCenter()) < minDist) {
					minDist = p.distance(neighborNode.getCalculatedCenter());
					nextHop = neighborNode;
				}
			}
			nextHop.getInboxManager().receive(message);
		}

		iCycler.getExecutionReporter().writeln(" }");
	}

	/**
	 * Clears old or current messages.<br>
	 * <br>
	 * Called at the end of processing in each cycle to keep the specific
	 * message types for each newly implemented algorithms.<br>
	 * <br>
	 * This shall be called at the end of each cycle.
	 */
	public void clearReceivedMessageList() {
		ZXControl zxControl = node.getNodeList().getZXControl();
		for (int i = inbox.size() - 1; i >= 0; i--) {
			Message m = inbox.get(i);
			if (zxControl.isOld(m.getCycleNo())
					|| zxControl.getICycler().isCurrent(m.getCycleNo()))
				inbox.remove(i);
		}
	}

	/**
	 * Can be accessed by upper layers and algorithms. to process directly
	 * special messages.
	 * 
	 * @return
	 */
	public List<Message> getInbox() {
		return inbox;
	}

	@Override
	public void reset() {
		inbox.clear();
	}
}
