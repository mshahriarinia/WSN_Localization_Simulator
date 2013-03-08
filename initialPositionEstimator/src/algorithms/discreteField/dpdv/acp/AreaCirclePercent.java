package algorithms.discreteField.dpdv.acp;

import geom.Circle;

import java.awt.geom.Rectangle2D;

import node.type.Anchor;
import simulator.localizable.MSLostNode;
import control.ZXControl;

/**
 * This class is assumed to store the relative percent of the circle in an area
 * 
 */
public abstract class AreaCirclePercent {

	public static enum PERCENT_TYPE_ENUM {
		ARC_LENGTH, SECTOR_DEGREE
	};

	private PERCENT_TYPE_ENUM percent_type;

	private Rectangle2D rect;

	private MSLostNode mslNode;

	private Anchor anchor;

	private double radius;

	private double percent;

	public AreaCirclePercent(Rectangle2D rect, MSLostNode mslNode, Anchor anchor) {
		this.rect = rect;
		this.mslNode = mslNode;
		this.anchor = anchor;
		int hopCount = mslNode.getMemory().getHopCount(anchor);
		this.radius = hopCount * mslNode.getAverageHopSize();

		if (mslNode.isLogged())
			System.out.print("");

		if (hopCount > 1
				&& Circle.containsAll(anchor.getCalculatedCenter(), radius,
						rect)) {
			percent = 0;
		} else {
			setPercent();
		}
	}

	protected void setPercent(PERCENT_TYPE_ENUM percent_type) {
		this.percent_type = percent_type;
		switch (percent_type) {
		case ARC_LENGTH:
			double arc = ArcLengthEstimator.estimateArcLength(rect, anchor
					.getCalculatedCenter(), radius);

			double perimeter = ArcLengthEstimator.getCirclePerimeter(anchor
					.getCalculatedCenter(), radius);

			percent = arc / perimeter;
			break;
		case SECTOR_DEGREE:
			break;
		}
	}

	/**
	 * re-definable method
	 */
	abstract protected void setPercent();

	public PERCENT_TYPE_ENUM getPercent_type() {
		return percent_type;
	}

	public Rectangle2D getRect() {
		return rect;
	}

	public MSLostNode getMSLostNode() {
		return mslNode;
	}

	public Anchor getAnchor() {
		return anchor;
	}

	public double getRadius() {
		return radius;
	}

	public double getPercent() {
		return percent;
	}

	@Override
	public String toString() {
		return "L: " + getMSLostNode().getId() + " A: " + getAnchor().getId()
				+ " R: " + ZXControl.doubleToString(getRadius()) + " Percent: "
				+ ZXControl.doubleToString(getPercent() * 100) + " Rect: "
				+ ZXControl.rectangleToString(getRect());
	}

}