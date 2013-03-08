package geom;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import node.Node;

/**
 * gets the ralationship between an area and a node Area is the area produced by
 * the checkboard of refs centers
 * 
 */
public class MyRectArea extends Rectangle2D.Double {

	private static final long serialVersionUID = -4400937688747979807L;

	public static double getArea(Rectangle2D rect) {
		return rect.getWidth() * rect.getHeight();
	}

	// /**
	// *
	// * @param rect
	// * @return Center Of Gravity of the rectangle in the network field
	// */
	// public static Point2D getCentroid(Rectangle2D rect) {
	// double minX = getInDomainValue(rect.getMinX(), 0, ZXControl.FIELD_WIDTH);
	// double maxX = getInDomainValue(rect.getMaxX(), 0, ZXControl.FIELD_WIDTH);
	//
	// double minY = getInDomainValue(rect.getMinY(), 0,
	// ZXControl.FIELD_HEIGHT);
	// double maxY = getInDomainValue(rect.getMaxY(), 0,
	// ZXControl.FIELD_HEIGHT);
	//
	// return new Point2D.Double((minX + maxX) / 2, (minY + maxY) / 2);
	// }

	/**
	 * gets if the rect is hit by the reference node by the specified distance
	 * 
	 * @param rect
	 * @param node
	 * @param radius
	 * @return
	 */
	public static boolean isHit(Rectangle2D rect, Node node, double radius) {
		return isHit(rect, new Circle(node.getCenter(), radius));
	}

	public static boolean isHit(Rectangle2D rect, Circle circle) {
		Point2D pSW = new Point2D.Double(rect.getMinX(), rect.getMinY());
		Point2D pSE = new Point2D.Double(rect.getMaxX(), rect.getMinY());
		Point2D pNW = new Point2D.Double(rect.getMinX(), rect.getMaxY());
		Point2D pNE = new Point2D.Double(rect.getMaxX(), rect.getMaxY());

		LineSegment1 lineSegment = new LineSegment1();

		lineSegment.setLine(pSW, pSE);
		if (isHit(lineSegment, circle))
			return true;

		lineSegment.setLine(pSE, pNE);
		if (isHit(lineSegment, circle))
			return true;

		lineSegment.setLine(pNE, pNW);
		if (isHit(lineSegment, circle))
			return true;

		lineSegment.setLine(pNW, pSW);
		if (isHit(lineSegment, circle))
			return true;

		return false;
	}

	private static boolean isHit(LineSegment1 lineSegment, Circle circle) {

		if (lineSegment.isVertical()) {
			double x = lineSegment.getEnd1().getX();

			if (isHitX(x, circle)) {
				List<Point2D> hits = getHitsX(x, circle);
				for (Point2D p : hits) {
					if (lineSegment.intersectsInSegment(p))
						return true;
				}
			}
		} else if (lineSegment.isHorizontal()) {
			double y = lineSegment.getEnd1().getY();

			if (isHitY(y, circle)) {
				List<Point2D> hits = getHitsY(y, circle);
				for (Point2D p : hits) {
					if (lineSegment.intersectsInSegment(p))
						return true;
				}
			}
		} else {
			// LATER: sloppy lines
		}
		return false;
	}

	private static boolean isHitX(double x, Circle circle) {
		return getHitsX(x, circle) != null;
	}

	private static boolean isHitY(double y, Circle circle) {
		return getHitsY(y, circle) != null;
	}

	/**
	 * gets hits of the horizontal line x by the node perimeter
	 * 
	 * @param x
	 *            axis slide
	 * @param node
	 * @return
	 */
	private static List<Point2D> getHitsX(double x, Circle circle) {
		List<Point2D> list = new ArrayList<Point2D>();

		double r2 = Math.pow(circle.getR(), 2);
		double deltaX2 = Math.pow(x - circle.getCenter().getX(), 2);

		Point2D p;
		if (r2 < deltaX2)
			list.clear();// no hit
		else if (r2 == deltaX2) {
			p = new Point2D.Double(x, circle.getCenter().getY());
			list.add(p);
		} else {
			double deltaY = Math.sqrt(r2 - deltaX2);

			p = new Point2D.Double(x, circle.getCenter().getY() + deltaY);
			list.add(p);
			p = new Point2D.Double(x, circle.getCenter().getY() - deltaY);
			list.add(p);
		}

		return list;
	}

	/**
	 * gets hits of the vertical line y by the node perimeter
	 * 
	 * @param y
	 * @param node
	 * @return
	 */
	private static List<Point2D> getHitsY(double y, Circle circle) {
		List<Point2D> list = new ArrayList<Point2D>();

		double r2 = Math.pow(circle.getR(), 2);
		double deltaY2 = Math.pow(y - circle.getCenter().getY(), 2);

		Point2D p;
		if (r2 < deltaY2)
			list.clear();// no hit
		else if (r2 == deltaY2) {
			p = new Point2D.Double(circle.getCenter().getX(), y);
			list.add(p);
		} else {
			double deltaX = Math.sqrt(r2 - deltaY2);

			p = new Point2D.Double(circle.getCenter().getX() + deltaX, y);
			list.add(p);
			p = new Point2D.Double(circle.getCenter().getX() - deltaX, y);
			list.add(p);
		}

		return list;
	}

	/**
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return The value that is more than min and less than max.
	 */
	public static double getInDomainValue(double value, double min, double max) {
		if (value <= min)
			return min;
		else if (value >= max)
			return max;
		else
			return value;
	}

	public static void setInDomainValue(Point2D p, Rectangle2D rect) {
		setInDomainValue(p, rect.getMinX(), rect.getMaxX(), rect.getMinY(),
				rect.getMaxY());
	}

	public static void setInDomainValue(Point2D p, double minX, double maxX,
			double minY, double maxY) {
		double x = p.getX(), y = p.getY();

		if (x > maxX)
			x = maxX;
		if (x < minX)
			x = minX;

		if (y > maxY)
			y = maxY;
		if (y < minY)
			y = minY;

		p.setLocation(x, y);
	}
}
