package node;

import java.awt.geom.Point2D;

/**
 * Destination is either an ID of a node (neighbor of the sender) or the
 * geometric location of the destination. Since we don't use routing tables and
 * only use geographic routing, even if we know the destination node id, we have
 * to use its estimated or accurate location (if available) to communicate data. <br>
 * <br>
 * TTL is only definable at broadcast messages, hence it is defined at the
 * destination portion of the message. Since destination is replicated for each
 * neighbor, <b>an already used destination shouldn't be used again.</b>
 * 
 * @author Morteza
 * 
 */
public class Destination {

	private ID id;

	private Point2D point2d;

	private int ttl = -1;// ttl not set

	public ID getId() {
		return id;
	}

	public Point2D getPoint2d() {
		return point2d;
	}

	public int getTtl() {
		return ttl;
	}

	public Destination(ID id) {
		if (id.equals(ID.BROADCAST))
			throw new RuntimeException("Broadcast improperly utilized.");
		this.id = id;
	}

	/**
	 * broadcast with ttl
	 * 
	 * @param ttl
	 */
	public Destination(int ttl) {
		this.id = ID.BROADCAST;
		this.ttl = ttl;
	}

	/**
	 * used for geographic routing
	 * 
	 * @param point2d
	 */
	public Destination(Point2D point2d) {
		this.point2d = point2d;
	}

	public Object getDestination() {
		if (id != null)
			return id;
		else
			return point2d;
	}

	@Override
	public String toString() {
		if (id != null)
			return "Destination id: " + id
					+ ((ttl != -1) ? " TTL: " + ttl : "");
		else
			return "Destination location: " + id
					+ ((ttl != -1) ? " TTL: " + ttl : "");
	}
}
