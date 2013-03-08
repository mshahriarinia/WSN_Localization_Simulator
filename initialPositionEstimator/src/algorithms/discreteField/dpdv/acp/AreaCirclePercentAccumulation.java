package algorithms.discreteField.dpdv.acp;

import geom.image.FieldArray;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import node.Node;
import node.type.Anchor;
import simulator.localizable.MSLostNode;
import control.ZXControl;

/**
 * This class accumulates the portion (percent) of the circles perimeter in a
 * rectangular area. Each circle is centered in an anchor with the radius equal
 * to the multi-hop distance to the mslNode.
 * <p>
 * So, we'll have the overall percent of of existence of the mslNode in a
 * rectangle, according to all anchors.
 */

/**
 * @ TODO remove worst ACPS obtained from anchors that are more than a specified
 * hop-distance away but keep at least some to give a initial estimate
 */

public abstract class AreaCirclePercentAccumulation {

	private MSLostNode mslNode;

	private Rectangle2D rect;

	private List<AreaCirclePercent> listACP;

	/**
	 * This is used to store the accumulated percent.
	 */
	private double percent;

	public AreaCirclePercentAccumulation(MSLostNode mslNode, Rectangle2D rect) {
		this.mslNode = mslNode;
		this.rect = rect;
		listACP = new ArrayList<AreaCirclePercent>();
		percent = 0;
	}

	public void addAreaCirclePercent(AreaCirclePercent acp) {
		listACP.add(acp);
	}

	public MSLostNode getMSLostNode() {
		return mslNode;
	}

	public Rectangle2D getRect() {
		return rect;
	}

	public List<AreaCirclePercent> getListACP() {
		return listACP;
	}

	public static Comparator<AreaCirclePercentAccumulation> COMPARATOR = new Comparator<AreaCirclePercentAccumulation>() {
		@Override
		public int compare(AreaCirclePercentAccumulation o1,
				AreaCirclePercentAccumulation o2) {
			return (o1.getPercent() - o2.getPercent() > 0) ? 1 : -1;
		}
	};

	/**
	 * After calling updateAccumulation, returns the accumulated percent of this
	 * ACPA
	 * 
	 * @return
	 */
	public double getPercent() {
		return percent;
	}

	/**
	 * preferably it uses the AND of the existence of possibility percent in an
	 * area
	 */
	abstract public void updateAccumulation();

	public void or(List<AreaCirclePercent> listValidACP) {
		percent = 0;

		if (FieldArray.isImpossible(mslNode, rect)) {
			percent = 0;
			return;
		}

		for (AreaCirclePercent acp : listValidACP) {
			percent += acp.getPercent()
					* Math.pow(0.7, acp.getRadius() / mslNode.getRadius());
		}
	}

	public AreaCirclePercent getACP(Anchor anchor) {
		for (AreaCirclePercent acp : listACP) {
			if (anchor.equals(acp.getAnchor())) {
				return acp;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "ACPA " + ZXControl.doubleToString(percent) + " " + mslNode
				+ ZXControl.rectangleToString(rect);
	}

	/**
	 * Calculates all ACPs associated with a rectangular area(an ACPA) for a
	 * mslNode and an anchor. and fills the ACPA object with ACPs
	 * 
	 * @param acpa
	 * @param mslNode
	 * @param anchor
	 */
	private void calculateAllACPs(Node node, Anchor anchor) {
		if (node.isLogged()) {
			System.out.print("");
		}

		this.addAreaCirclePercent(new AreaCirclePercent(this.getRect(),
				mslNode, anchor) {
			@Override
			protected void setPercent() {
				setPercent(PERCENT_TYPE_ENUM.ARC_LENGTH);
			}
		});
	}

	public void calculateAllACPs(Node node) {
		for (Anchor anchor : node.getMemory().getAnchors()) {
			calculateAllACPs(node, anchor);
		}
	}
}
// private List<AreaCirclePercent> getUsefulACPs() {
// AreaCirclePercent[] acpArr = listACP.toArray(new AreaCirclePercent[0]);
// Arrays.sort(acpArr, AreaCirclePercent.COMPARATOR);
//
// List<AreaCirclePercent> listValidACP = new ArrayList<AreaCirclePercent>();
// listValidACP.addAll(Arrays.asList(acpArr));
//
// int remainingACPCount = listValidACP.size();
//
// for (int i = listValidACP.size() - 1; i >= 0; i--) {
// double radius = listValidACP.get(i).getRadius();
// if (remainingACPCount > ZXControl.MINIMUM_ACP_COUNT_TO_KEEP
// && radius > mslNode.getNodeList().getZXControl().MAXIMUM_ACP_RADIUS)
// listValidACP.remove(i);
// remainingACPCount = listValidACP.size();
// }
// return listValidACP;
// }