package simulator.localizable;

import java.util.ArrayList;
import java.util.List;

import node.Destination;
import node.ICycler;
import node.ID;
import node.Node;
import node.tools.Message;
import node.tools.TimedData;

public class AverageHopSizeMessageHandler {

	public static int handleAverageHopSizeMessages(Node node) {
		int result = AnchorAvgHopSizeWrapper.NOT_AVAILABLE;

		if (hasAlreadyReceived_AvgHopSize(node)) {
			removeAllAverageHopSizeMessages(node);
			return result;
		} else {
			List<Message> inbox = node.getInboxManager().getInbox();
			ICycler iCycler = node.getNodeList().getZXControl().getICycler();
			for (int i = 0; i < inbox.size(); i++) {
				Message m = inbox.get(i);
				if (m.getTimedData().getData() instanceof AnchorAvgHopSizeWrapper) {
					if (iCycler.isCurrent(m.getCycleNo())) {
						node.getMemory().getListVarietyData()
								.add(m.getTimedData());
						node.getInboxManager().send(
								new Message(node.getId(), new Destination(m
										.getTo().getTtl() - 1), m
										.getTimedData()));
						result = ((AnchorAvgHopSizeWrapper) m.getTimedData()
								.getData()).getAverageHopSize();
						break;
					}
				}
			}
			if (node.isLogged())
				System.out.print("");
			removeAllAverageHopSizeMessages(node);
			return result;
		}
	}

	private static void removeAllAverageHopSizeMessages(Node node) {
		List<Message> listRemovable = new ArrayList<Message>();
		ICycler iCycler = node.getNodeList().getZXControl().getICycler();
		for (Message m : node.getInboxManager().getInbox()) {
			if (m.getTimedData().getData() instanceof AnchorAvgHopSizeWrapper) {
				if (iCycler.isCurrent(m.getCycleNo()))
					listRemovable.add(m);
			}
		}
		node.getInboxManager().getInbox().removeAll(listRemovable);
	}

	private static boolean hasAlreadyReceived_AvgHopSize(Node node) {
		if (node instanceof MSAnchor) {
			return true;
		} else if (node instanceof MSLostNode) {
			for (TimedData td : node.getMemory().getListVarietyData()) {
				if (td.getData() instanceof AnchorAvgHopSizeWrapper)
					return true;
			}
			return false;
		}
		return false;
	}
}
