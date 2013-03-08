package geom;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Circle {

	private Point2D center;
	private double radius;

	public Circle(Point2D center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	public static boolean containsAll(Point2D center, double radius,
			Rectangle2D area) {
		Ellipse2D y = new Ellipse2D.Double(center.getX() - radius, center
				.getY()
				- radius, 2 * radius, 2 * radius);
		return y.contains(area.getMinX(), area.getMinY())
				&& y.contains(area.getMaxX(), area.getMaxY());
	}

	public static boolean containsSome(Point2D center, double radius,
			Rectangle2D area) {
		Circle c = new Circle(center, radius);
		return geom.MyRectArea.isHit(area, c);
	}

	public Point2D getCenter() {
		return center;
	}

	public double getR() {
		return radius;
	}

}
