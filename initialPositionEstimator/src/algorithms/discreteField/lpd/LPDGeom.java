package algorithms.discreteField.lpd;

import geom.Circle;
import geom.checkerBoard.FieldCheckerBoard;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class LPDGeom {

	public static final int SECTORS_COUNT = 36;
	private double avgRadius;
	private FieldCheckerBoard fieldCB;

	/**
	 * x, y displacements of center to get to sector
	 */
	private Point2D[] center2SectorDisplacement;

	public LPDGeom(FieldCheckerBoard fieldCB, double avgRadius) {
		this.avgRadius = avgRadius;
		this.fieldCB = fieldCB;
	}

	public Point2D[] getCenter2SectorDisplacement() {
		if (center2SectorDisplacement == null) {
			center2SectorDisplacement = new Point2D[SECTORS_COUNT];

			for (int i = 0; i < SECTORS_COUNT; i++) {
				double angle = i * (2 * Math.PI) / (SECTORS_COUNT + 0.0);
				center2SectorDisplacement[i] = new Point2D.Double(avgRadius
						* Math.cos(angle), avgRadius * Math.sin(angle));
			}
		}
		return center2SectorDisplacement;
	}

	public List<Point2D> getCirclePoints(Circle circle) {
		Rectangle2D circleSquare = new Rectangle2D.Double(circle.getCenter()
				.getX()
				- circle.getR(), circle.getCenter().getY() - circle.getR(),
				2 * circle.getR(), 2 * circle.getR());
		double delta = 0.1 * fieldCB.getSliceLength();
		List<Point2D> listPoints = new ArrayList<Point2D>();
		for (double i = circleSquare.getMinX(); i < circleSquare.getMaxX(); i += delta) {
			for (double j = circleSquare.getMinY(); j < circleSquare.getMaxY(); j += delta) {
				listPoints.add(new Point2D.Double(i, j));
			}
		}

		List<Point2D> listPointsNotInCircle = new ArrayList<Point2D>();
		for (Point2D p : listPoints) {
			if (p.distance(circle.getCenter()) > circle.getR())
				listPointsNotInCircle.add(p);
		}
		listPoints.removeAll(listPointsNotInCircle);
		return listPoints;
	}

	public int integral(List<Point2D> circlePoints, Rectangle2D rectangle) {

		int pointsCount = 0;
		for (Point2D p : circlePoints) {
			if (rectangle.contains(p))
				pointsCount++;
		}

		// points no in sector count base
		int numInSECTORS_COUNT = (int) Math.round((pointsCount + 0.0)
				/ circlePoints.size() * SECTORS_COUNT);

		return numInSECTORS_COUNT;
	}

	/**
	 * Checks if a node is contained in the previous circles. ignoring the
	 * father circle
	 * 
	 * @param point
	 * @param listCenters
	 * @param radius
	 * @return
	 */
	public static boolean isInPrevCircles(Point2D point,
			List<Point2D> listCenters, double radius) {
		double delta = 0.0001;
		double R = radius + delta;
		for (int i = 0; i < listCenters.size() - 1; i++) {
			if (listCenters.get(i).distance(point) <= R)
				return true;
		}
		return false;
	}

}
