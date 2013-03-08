package node.localization.tools.sectorBased;

import java.awt.geom.Point2D;
import java.util.List;

import node.Neighbor;
import node.Node;
import control.ZXControl;

/**
 * This is an algorithm specific data to be stored in messages
 */
public class NodeNeighborSectorNo {

	private Neighbor neighbor;

	private Node node;

	private int noisySectorNo;

	/**
	 * node sends its own position and says which sector the neighbor belongs
	 * to.
	 * 
	 * @param node
	 * @param position
	 * @param neighbor
	 * @param sectorNo
	 */
	public NodeNeighborSectorNo(Node node, List<Sector> listSectors,
			Neighbor neighbor) {
		this.node = node;
		this.neighbor = neighbor;

		noisySectorNo = getNoisySectorNo(getSectorNo(node.getCenter(), neighbor
				.getOtherNode(node).getCenter()));
	}

	

	/**
	 * return the neighbor angle in relation to the horizontal axis.
	 * 
	 * @param node
	 * @param neighbor
	 * @return
	 */
	private static double getAngle(Point2D center, Point2D subordinatePoint) {

		double translatedNeighborToCenterX = subordinatePoint.getX()
				- center.getX();
		double translatedNeighborToCenterY = subordinatePoint.getY()
				- center.getY();

		double angle = Math.atan2(translatedNeighborToCenterY,
				translatedNeighborToCenterX);
		if (angle < 0)
			angle += 2 * Math.PI;
		return angle;
	}

	private static int getSectorNo(Point2D center, Point2D subordinatePoint) {
		double angle = getAngle(center, subordinatePoint);
		double sectorRate = angle / ZXControl.SECTOR_ANGLE;
		return (int) (Math.floor(sectorRate));
	}

	private static int getNoisySectorNo(int sectorNo) {
		boolean failiure = Math.random() < ZXControl.SECTOR_DETECTION_FAILIURE_PROBABILITY;
		if (failiure) {
			if (Math.random() < 0.5)
				return sectorNo - 1;
			else
				return sectorNo + 1;
		} else
			return sectorNo;
	}

	public int getNoisySectorNo() {
		return noisySectorNo;
	}

	public Sector getNoisySector() {
		return node.getAngulationManager().getListSectors().get(noisySectorNo);
	}

	public Neighbor getNeighbor() {
		return neighbor;
	}

	@Override
	public String toString() {
		return "Node[" + node.getId().getValue() + "] Neighbor["
				+ neighbor.getOtherNode(node).getId().getValue() + "] Sector["
				+ noisySectorNo + "]";
	}

//	 public static void main(String[] args) {
//	 double d = getSectorNo(new Point2D.Double(231.39, 264.09), new Point2D.Double(260.50, 229.96));
//	 System.out.println(d);
//	 }
}
