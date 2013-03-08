package algorithms.discreteField.dpdv.acp;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ArcLengthEstimator {

	// double deltaX;
	// deltaX = 0.0001;
	// deltaX = 1;
	// deltaX = 0.1;

	// deltaX = 5;

	public static final double DELTA_X = 0.1;

	public static final double DELTA_X_2 = DELTA_X * DELTA_X;

	// public static void main1(String[] args) {
	// Rectangle2D rect = new Rectangle2D.Double(0, 0, 1, 1);
	// Point2D circleCenter = new Point2D.Double(0, 0);
	// double radius = 1;
	// double sum = estimateArcLength(rect, circleCenter, radius);
	//		
	// Rectangle2D bigRec = new Rectangle2D.Double(-10, -10, 20,20);
	// double allP = estimateArcLength(bigRec, circleCenter, radius);
	//
	// System.out.println(sum);
	// System.out.println(allP);
	//		
	// System.out.println(getCirclePerimeter(circleCenter, radius));
	// }

	/**
	 * for the same reason as mentioned for estimateArcLength. there are some
	 * pieces of the integrating lines that will not be counted upon calculating
	 * the circumference of the circle
	 * 
	 * @param rect
	 * @param circleCenter
	 * @param radius
	 * @return
	 */
	public static double getCirclePerimeter(Point2D circleCenter, double radius) {
		double r = radius;
		Rectangle2D surroundingRect = new Rectangle2D.Double(circleCenter
				.getX()
				- r, circleCenter.getY() - r, 2 * r, 2 * r);
		r = estimateArcLength(surroundingRect, circleCenter, radius);
		if (r == 0)
			r = Double.MIN_NORMAL;
		return r;
	}

	/**
	 * This method estimates the arc length of the circle in the rectangle using
	 * an integrating approach.
	 * <p>
	 * one thing to keep in mind is that the first and last pieces of the arc
	 * are not included. because they fall on the perimeter of the rectangle2d
	 * and the method Rectangle2d.includes() will return false for such points
	 * </p>
	 * 
	 * @param rect
	 * @param circleCenter
	 * @param radius
	 * @return
	 */
	public static double estimateArcLength(Rectangle2D rect,
			Point2D circleCenter, double radius) {

		double sum = 0;

		if (doesNotSquareContainsSOMECircle(rect, circleCenter, radius)) {
			return 0;
		} else {
			double r = radius;// alias

			int count = (int) Math.round(rect.getWidth() / DELTA_X);

			double x1 = rect.getMinX();
			double x2 = x1 + DELTA_X;
			for (int i = 0; i < count; i++) {

				double sqrtBase1 = r * r - (x1 - circleCenter.getX())
						* (x1 - circleCenter.getX());
				double sqrtBase2 = r * r - (x2 - circleCenter.getX())
						* (x2 - circleCenter.getX());

				// circle contains X range
				if (sqrtBase1 >= 0 && sqrtBase2 >= 0) {
					double y1 = Math.sqrt(sqrtBase1) + circleCenter.getY();// positive
					double y2 = Math.sqrt(sqrtBase2) + circleCenter.getY();
					double deltaY = y2 - y1;

					// rectangle contains Y
					if (rect.contains(x1, y1) && rect.contains(x2, y2)) {
						sum += Math.sqrt(DELTA_X_2 + deltaY * deltaY);
					}

					// work with negative sqrt base
					y1 = -Math.sqrt(sqrtBase1) + circleCenter.getY();// Negative
					y2 = -Math.sqrt(sqrtBase2) + circleCenter.getY();
					deltaY = y2 - y1;

					// rectangle contains Y
					if (rect.contains(x1, y1) && rect.contains(x2, y2)) {
						sum += Math.sqrt(DELTA_X_2 + deltaY * deltaY);
					}
				}
				x1 += DELTA_X;
				x2 += DELTA_X;
			}
		}
		return sum;
	}

	private static boolean doesNotSquareContainsSOMECircle(Rectangle2D rect,
			Point2D circle, double r) {
		return ((rect.getMaxX() < circle.getX() - r)
				|| (rect.getMinX() > circle.getX() + r)
				|| (rect.getMaxY() < circle.getY() - r) || (rect.getMinY() > circle
				.getY()
				+ r));
	}
}
