package node.localization.tools.sectorBased;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import control.ZXControl;

public class Sector {

	/**
	 * 0.01 A DEGREE, acceptable error for radian computation errors
	 */
	private static final double RADIAN_COMPUTATION_ERROR = (Math.PI * 0.01) / 180;

	/**
	 * A is the center of sector, B and C are the vertices with acute angle
	 * where in one anti-clock cycle, we first get to B then C.
	 */
	private Point2D A, B, C;

	private double radius;

	private double bacAngle;

	/**
	 * i vector transformed to A's position
	 */
	Line2D iVectInA;

	/**
	 * assumes b is at distance r from a
	 * 
	 * @param B
	 * @param A
	 * @param radius
	 * @param bacAngle
	 */
	public Sector(Point2D B, Point2D A, double radius, double bacAngle) {
		this.B = B;
		this.A = A;
		this.bacAngle = bacAngle;
		this.radius = radius;

		double translatedBToCenterX = B.getX() - A.getX();
		double translatedBToCenterY = B.getY() - A.getY();

		double iabAngle = Math
				.atan2(translatedBToCenterY, translatedBToCenterX);

		if (iabAngle < 0)
			iabAngle = 2 * Math.PI + iabAngle;

		double iacAngle = iabAngle + bacAngle;

		double cDeltaX = radius * Math.cos(iacAngle);
		double cDeltaY = radius * Math.sin(iacAngle);

		C = new Point2D.Double(A.getX() + cDeltaX, A.getY() + cDeltaY);

		iVectInA = getIVectInA();
	}

	/**
	 * B is the first node in the anti-clock direction
	 * 
	 * @param B
	 * @param A
	 *            - Center point.
	 * @param C
	 */
	public Sector(Point2D B, Point2D A, Point2D C) {
		this.A = A;
		radius = A.distance(B);
		if (radius - A.distance(C) > 0.001)
			throw new RuntimeException("Invalid sector points.");

		iVectInA = getIVectInA();
		double bAngle = Math.asin(iVectInA.ptLineDist(B) / radius);
		double cAngle = Math.asin(iVectInA.ptLineDist(C) / radius);
		if (bAngle < cAngle) {
			this.B = B;
			this.C = C;
		} else {
			this.B = C;
			this.C = B;
		}

		initSector();
	}

	private Line2D getIVectInA() {
		if (iVectInA == null)
			iVectInA = new Line2D.Double(A, new Point2D.Double(A.getX() + 1, A
					.getY()));
		return iVectInA;
	}

	private void initSector() {
		double a, b, c;
		a = B.distance(C);
		b = radius;
		c = radius;
		bacAngle = Math.acos((a * a - b * b - c * c) / (2 * b * c));
	}

	public boolean containsInfinitely(Point2D p) {
		double PAB_Angle = getAngle(p, A, B);
		double PAC_Angle = getAngle(p, A, C);
		double deg=Math.toDegrees(PAB_Angle);
		deg=Math.toDegrees(PAC_Angle);
		return PAB_Angle + PAC_Angle <= bacAngle+RADIAN_COMPUTATION_ERROR;
	}

	public double getAngle() {
		return bacAngle;
	}

	public static double getAngle(Point2D B, Point2D A, Point2D C) {
		double a, b, c;
		a = B.distance(C);
		b = C.distance(A);
		c = A.distance(B);
		return Math.acos((a * a - b * b - c * c) / (-2 * b * c));
	}

	public double getMaxX() {
		return Math.max(Math.max(A.getX(), B.getX()), C.getX());
	}

	public double getMaxY() {
		return Math.max(Math.max(A.getY(), B.getY()), C.getY());
	}

	public double getMinX() {
		return Math.min(Math.min(A.getX(), B.getX()), C.getX());
	}

	public double getMinY() {
		return Math.min(Math.min(A.getY(), B.getY()), C.getY());
	}

	public Point2D getA() {
		return A;
	}

	public Point2D getB() {
		return B;
	}

	public Point2D getC() {
		return C;
	}

	public boolean intersects(Rectangle2D rect) {
		boolean intersectsAB = rect.intersectsLine(A.getX(), A.getY(),
				B.getX(), B.getY());
		boolean intersectsAC = rect.intersectsLine(A.getX(), A.getY(),
				C.getX(), C.getY());
		return intersectsAB || intersectsAC;
	}

	public boolean containsCentroid(Rectangle2D rect) {
		Point2D centroid = new Point2D.Double(rect.getCenterX(), rect
				.getCenterY());
		return A.distance(centroid) <= radius && containsInfinitely(centroid);
	}

	@Override
	public String toString() {
		return "B" + ZXControl.pointToString(B) + " A"
				+ ZXControl.pointToString(A) + " C"
				+ ZXControl.pointToString(C);
	}

	// public static void main(String[] args) {
	// System.out.println(Math.PI / 4);
	// System.out.println(3 * Math.PI / 4);
	// System.out.println(Math.atan2(-1, -1));
	//	
	// // Sector s = new Sector(new Point2D.Double(1, 0),
	// // new Point2D.Double(0, 0), 1, Math.PI / 3 + Math.PI);
	// 
	// Sector s = new Sector(new Point2D.Double(-10, -9),
	// new Point2D.Double(-10, -10), 1, Math.PI / 6 );//+ Math.PI);
	//	 
	// System.out.println(s);
	// }
}