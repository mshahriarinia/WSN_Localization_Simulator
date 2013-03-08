package algorithms.discreteField.dpdv;

import geom.checkerBoard.FieldCheckerBoard;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import node.Node;
import simulator.localizable.MSLostNode;
import algorithms.discreteField.dpdv.acp.AreaCirclePercent;
import algorithms.discreteField.dpdv.acp.AreaCirclePercentAccumulation;

public class ListACPA {

	private List<AreaCirclePercentAccumulation> listACPAs;

	private MSLostNode mslNode;

	private FieldCheckerBoard fieldCB;

	public ListACPA() {
		listACPAs = new ArrayList<AreaCirclePercentAccumulation>();
	}

	public ListACPA(ListACPA listACPA) {
		this(listACPA.mslNode, listACPA.fieldCB);
		listACPAs.addAll(listACPA.getListACPAs());

	}

	public ListACPA(MSLostNode mslNode, FieldCheckerBoard fieldCB) {
		this.mslNode = mslNode;
		this.fieldCB = fieldCB;
		listACPAs = new ArrayList<AreaCirclePercentAccumulation>();
	}

	public void initializeNetworkACPAs() {
		// List<Rectangle2D> listAreas = ((nodesCheckerBoard)
		// mslNode.getNodeList()
		// .getSimulationInfoContext()).getListAreas(listAnchors);

		List<Rectangle2D> listAreas = fieldCB.getListAreas();

		this.listACPAs = getAllACPAs(listAreas, mslNode);
	}

	/**
	 * gets all empty ACPAs of the network field
	 * 
	 * @param areas
	 * @param mslNode
	 * @return
	 */
	private List<AreaCirclePercentAccumulation> getAllACPAs(
			List<Rectangle2D> areas, MSLostNode mslNode) {
		AreaCirclePercentAccumulation acpa;
		for (Rectangle2D rect : areas) {
			acpa = new AreaCirclePercentAccumulation(mslNode, rect) {
				@Override
				public void updateAccumulation() {
					List<AreaCirclePercent> listValidACP = getListACP();

					or(listValidACP);

				}
			};

			// if (LogACPAs.debugRect(acpa, ZXControl.DEBUG_RECT_X,
			// ZXControl.DEBUG_RECT_Y))
			// System.out.print("");

			listACPAs.add(acpa);
			acpa.calculateAllACPs(mslNode);
			acpa.updateAccumulation();
		}

		return listACPAs;
	}

	public List<AreaCirclePercentAccumulation> getListACPAs() {
		return listACPAs;
	}

	public Node getNode() {
		return mslNode;
	}

	public AreaCirclePercentAccumulation getACPA(Rectangle2D rect) {
		for (AreaCirclePercentAccumulation acpa : listACPAs) {
			if (acpa.getRect().equals(rect))
				return acpa;
		}
		throw new RuntimeException("Invalid rectangle value requested.");
	}
}
