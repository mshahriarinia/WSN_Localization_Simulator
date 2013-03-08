package geom;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class LineSegment1 {

	private static final long serialVersionUID = 6037935406557443723L;
	private Line2D line;

	public LineSegment1(Point2D end1, Point2D end2) {
		if (end1.getX() == end2.getX() && end1.getY() == end2.getY())
			throw new RuntimeException("A node as two node ends of a line.");
		line = new Line2D.Double(end1, end2);
	}

	public LineSegment1() {
		this(new Point2D.Double(0, 0), new Point2D.Double(1, 0));
	}

	public void setLine(Point2D end1, Point2D end2) {
		line.setLine(end1, end2);
	}

	public Point2D getEnd1() {
		return line.getP1();
	}

	public Point2D getEnd2() {
		return line.getP2();
	}

	public boolean isVertical() {
		return line.getP1().getX() == line.getP2().getX();
	}

	public boolean isHorizontal() {
		return line.getP1().getY() == line.getP2().getY();
	}

	/**
	 * Sometimes a point on a line gives distances close to zero but not zero,
	 * because of the double precision difficulties.
	 * 
	 * @param p
	 * @return
	 */
	public boolean intersectsInSegment(Point2D p) {
		return (line.ptLineDist(p) <= 0.00001)
				&& (((p.getX() >= line.getX1() && p.getX() <= line.getX2()) || (p
						.getX() <= line.getX1() && p.getX() >= line.getX2())) && (((p
						.getY() >= line.getY1() && p.getY() <= line.getY2()) || (p
						.getY() <= line.getY1() && p.getY() >= line.getY2()))));
	}
}
