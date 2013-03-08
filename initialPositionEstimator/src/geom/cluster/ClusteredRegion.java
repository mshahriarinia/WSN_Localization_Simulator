package geom.cluster;

import geom.RandomVariable;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public abstract class ClusteredRegion {

//	private double highPassFilterThreshold = Double.NEGATIVE_INFINITY;

	private List<Rectangle2D> listClusterRects;

	private Point2D centroid;

	private Point2D weightedCenter;

	public ClusteredRegion() {
		listClusterRects = new ArrayList<Rectangle2D>();
	}

	public List<Rectangle2D> getListClusterRects() {
		return listClusterRects;
	}

	public void addRect(Rectangle2D rect) {
		listClusterRects.add(rect);
	}

	public int getRegionsCount() {
		return listClusterRects.size();
	}

	// public Point2D getCentroid() {
	// if (centroid == null) {
	// double x = 0, y = 0;
	// for (Rectangle2D rect : listClusterRects) {
	// x += MyRectArea1.getCentroid(rect).getX();
	// y += MyRectArea1.getCentroid(rect).getY();
	// }
	// x /= listClusterRects.size();
	// y /= listClusterRects.size();
	// centroid = new Point2D.Double(x, y);
	// }
	// return centroid;
	// }

	public Point2D getWeightedCenter() {
		if (weightedCenter == null) {
			double x = 0, y = 0;
			Point2D p = null;
			double sumPercent = 0;

			for (Rectangle2D rect : listClusterRects) {

				double value = getValue(rect);
				// p = MyRectArea1.getCentroid(rect);
				x += value * rect.getCenterX();
				y += value * rect.getCenterY();
				sumPercent += value;
			}
			x /= sumPercent;
			y /= sumPercent;

			weightedCenter = new Point2D.Double(x, y);
		}
		return weightedCenter;
	}

//	private double getHighPassFilterThreshold() {
//		List<Double> l = new ArrayList<Double>();
//		if (highPassFilterThreshold == Double.NEGATIVE_INFINITY) {
//			for (Rectangle2D rect : listClusterRects) {
//				l.add(getValue(rect));
//			}
//
//			RandomVariable<Double> r = new RandomVariable<Double>(l) {
//				@Override
//				public double getValue(Double randomEvent) {
//					return randomEvent;
//				}
//			};
//
//			highPassFilterThreshold = r.getCustomConfidenceInterval();
//		}
//
//		return highPassFilterThreshold;
//	}

	// protected List<ClusteredRegion> getHighPassFilter1(
	// List<ClusteredRegion> listCR, double clusterHighPassFilterThreshold) {
	// List<ClusteredRegion> newList = new ArrayList<ClusteredRegion>();
	// for (ClusteredRegion cr : listCR) {
	// newList.add(getHighPassFilter(cr, clusterHighPassFilterThreshold));
	// }
	// return newList;
	// }
	//
	// private ClusteredRegion getHighPassFilter(ClusteredRegion
	// clusteredRegion,
	// double clusterHighPassFilterThreshold) {
	//
	// ClusteredRegion filteredCR = new ClusteredRegion() {
	// @Override
	// public double getValue(Rectangle2D rect) {
	// return getValue(rect);
	// }
	// };
	// for (Rectangle2D rect : listClusterRects) {
	// if (getValue(rect) > clusterHighPassFilterThreshold)
	// filteredCR.addRect(rect);
	// }
	// return filteredCR;
	// }

	public abstract double getValue(Rectangle2D rect);
}
