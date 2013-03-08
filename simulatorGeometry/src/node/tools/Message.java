package node.tools;

import java.awt.geom.Point2D;

import control.ZXControl;

import node.Destination;
import node.ID;

/**
 * Message created specifically for the next cycle, unless <b>setCurrent()</b>
 * called
 */
public class Message {

	private ID from;

	private Destination to;

	private TimedData timedData;

	/**
	 * This message is to be processed in the provided cycle number. we should
	 * not process messages of the next cycle in the current cycle, even if its
	 * in our inbox
	 */
	private int cycleNo;

	/**
	 * 
	 * @param from
	 *            : physical sender at this cycle. if the original initiator is
	 *            required it should be stored somewhere in the data.
	 * @param to
	 * @param timedData
	 */
	public Message(ID from, Destination to, TimedData timedData) {
		this.from = from;
		this.to = to;
		this.timedData = timedData;
		cycleNo = getNextCycle();
	}

	public Message(ID from, Destination to, Object data) {
		this.from = from;
		this.to = to;
		this.timedData = new TimedData(from.getNode().getNodeList()
				.getZXControl().getICycler(), data);
		cycleNo = getNextCycle();
	}

	/**
	 * This message is always for the next cycle.
	 * 
	 * @return Current cycle plus one.
	 */
	private int getNextCycle() {
		return from.getNode().getNodeList().getZXControl().getICycler()
				.getCycleNo() + 1;
	}

	/**
	 * 
	 * @return The neighbor which sent or relayed the message
	 */
	public ID getFrom() {
		return from;
	}

	public Destination getTo() {
		return to;
	}

	public TimedData getTimedData() {
		return timedData;
	}

	public int getCycleNo() {
		return cycleNo;
	}

	/**
	 * Used to be processed in the current cycle. Used only to handle multi-hop
	 * messages for a node in the same cycle of receiving and both updating
	 * message's content based on hop info.
	 */
	public void setCurrent() {
		cycleNo = from.getNode().getNodeList().getZXControl().getICycler()
				.getCycleNo();
	}

	@Override
	public String toString() {
		return "M[" + from + "->" + getToToString() + " " + timedData + "]";
	}

	protected String getToToString() {
		if (to.getDestination() instanceof ID) {
			return to.getDestination().toString();
		} else if (to.getDestination() instanceof Point2D) {
			return ZXControl.pointToString((Point2D) to.getDestination());
		} else
			return to.toString();
	}
}
