package reporter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import simulator.localizable.MSAnchor;
import simulator.localizable.MSLostNode;
import simulator.localizable.State;

import node.Neighbor;
import node.Node;
import node.NodeList;

import control.ZXControl;

/**
 * Reports Mass Spring Angular effects.
 */
public class MSAngularReporter {

	public static final String TITLE_LOST_NODE = "Lost";

	public static final String TITLE_ANCHOR_NODE = "Anchor";

	public static final int NOT_AVAILABLE = -1;

	private FileOutputStream os = null;

	private PrintStream ps;

	private NodeList nodeList;

	public MSAngularReporter(String fileNameIndex, NodeList nodeList) {
		this.nodeList = nodeList;
		try {
			os = new FileOutputStream(ZXControl.OUTPUT_DIRECTORY
					+ fileNameIndex + " - "
					+ ZXControl.getTitle(ZXControl.Initial_POSITION_ALGORITHM)
					+ ".txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ps = new PrintStream(os);

	}

	public void report(int cycleNo) {
		ps.println("#\t#\t#\t#\t#\t#\t#\t#\t#" + cycleNo
				+ " #\t#\t#\t#\t#\t#\t#");
		ps.println("id" + "\t" + "Type" + "\t" + "Localization_Phase" + "\t"
				+ "RectNo" + "\t" + "avgHopLen" + "\t" + "hopToAnchor" + "\t"
				+ "msJumpSize" + "\t" + "degree" + "\t" + "anchorDeg" + "\t"
				+ "messageSent" + "\t" + "x" + "\t" + "y" + "\t" + "x'" + "\t"
				+ "y'" + "\t" + "err");

		for (Node node : nodeList.getListNodes()) {

			ps.print(node.getId());
			ps.print("\t");

			ps.print(type(node));
			ps.print("\t");

			ps.print(localizationPhase(node));
			ps.print("\t");

			ps.print(rectCount(node));
			ps.print("\t");

			ps.print(usedAverageHopSize(node));
			ps.print("\t");

			// TODO does anchor store in its memory
			ps.print(node.getMinHop2Anchors());
			ps.print("\t");

			ps.print(msJumpLength(node));
			ps.print("\t");

			ps.print(node.getNeighbors().size());
			ps.print("\t");

			ps.print(anchorNeighbors(node));
			ps.print("\t");

			ps.print(node.getCenter().getX());
			ps.print("\t");

			ps.print(node.getCenter().getY());
			ps.print("\t");

			ps.print(node.getCalculatedCenter() != null ? node
					.getCalculatedCenter().getX() : NOT_AVAILABLE);
			ps.print("\t");

			ps.print(node.getCalculatedCenter() != null ? node
					.getCalculatedCenter().getY() : NOT_AVAILABLE);
			ps.print("\t");

			ps.print(node.getCalculatedCenter() != null ? node.getCenter()
					.distance(node.getCalculatedCenter()) : NOT_AVAILABLE);
			// ps.print("\t");
			//
			// ps.print();
			// ps.print("\t");

			ps.println();
		}

		ps.flush();
	}

	private int anchorNeighbors(Node node) {
		int count = 0;
		for (Neighbor n : node.getNeighbors()) {
			if (n.getOtherNode(node) instanceof MSAnchor)
				count++;
		}
		return count;
	}

	private int msJumpLength(Node node) {
		if (node instanceof MSLostNode) {
			if (((MSLostNode) node).getRangeFreeMassSpring() != null)
				return ((MSLostNode) node).getRangeFreeMassSpring()
						.getJumpLength();
			else
				return NOT_AVAILABLE;
		}
		return NOT_AVAILABLE;
	}

	private int usedAverageHopSize(Node node) {
		if (node instanceof MSLostNode)
			return ((MSLostNode) node).getAverageHopSize();
		else if (node instanceof MSAnchor)
			return ((MSAnchor) node).getAverageHopSize();
		else
			return NOT_AVAILABLE;
	}

	private int rectCount(Node node) {
		if (node instanceof MSLostNode) {
			if (((MSLostNode) node).getDiscreteFieldLocalization() != null)
				return ((MSLostNode) node).getDiscreteFieldLocalization()
						.getPossibleRectsCount();
			else
				return NOT_AVAILABLE;
		}
		return NOT_AVAILABLE;
	}

	private String type(Node node) {
		if (node instanceof MSLostNode)
			return TITLE_LOST_NODE;
		else if (node instanceof MSAnchor)
			return TITLE_ANCHOR_NODE;
		else
			return null;
	}

	private String localizationPhase(Node node) {
		if (node instanceof MSLostNode)
			return State.toString(((MSLostNode) node).getLocalizationPhase());
		else
			return type(node);
	}

	public void dispose() {
		ps.close();
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
