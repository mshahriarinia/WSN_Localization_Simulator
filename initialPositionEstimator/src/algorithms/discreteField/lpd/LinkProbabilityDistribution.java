package algorithms.discreteField.lpd;

import geom.Circle;
import geom.RandomVariable;
import geom.image.FieldArray;

import java.awt.geom.Point2D;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import node.Node;
import node.type.Anchor;
import simulator.localizable.MSLostNode;
import algorithms.discreteField.DiscreteFieldLocalization;
import control.ZXControl;

/**
 *TODO Graph (average hop size)-(maximum radio range)-(actual multi hop
 * distance)-(avg hop size up to max radio range mapper actual multi-hop length) <br>
 * <br>
 * check the effect of sector count on accuracy-0u
 */
public class LinkProbabilityDistribution extends DiscreteFieldLocalization {

	private static final int MAX_HOPS_ANCHOR = 4;

	private static final int CORRECTIVE_SECTOR_COUNT = 8;

	private double avgRadius;

	/**
	 * Logarithmically computed area matrix.
	 */
	private LPDGeom lpdGeom;

	private int sectorsCount;

	@Override
	protected void setFieldClusteringHighPassFilterThreshold(Node node,
			double[][] fieldArr) {
		List<Double> l = new ArrayList<Double>();

		for (int i = 0; i < fieldArr.length; i++) {
			for (int j = 0; j < fieldArr[0].length; j++) {
				if (fieldArr[i][j] > 0)
					l.add(fieldArr[i][j]);
			}
		}
		RandomVariable<Double> r = new RandomVariable<Double>(l) {
			@Override
			public double getValue(Double randomEvent) {
				return randomEvent;
			}
		};

		setFieldClusteringHighPassFilterThreshold(r
				.getCustomConfidenceInterval());

	}

	private void init() {
		// avgRadius = getNode().getAverageHopSize();
		avgRadius = getNode().getRadius() + 0.2 * getNode().getRadius();
		// avgRadius=15/0;
		lpdGeom = new LPDGeom(getFieldCB(), avgRadius);

		int minHopsToAnchors = getNode().getMinHop2Anchors();
		if (minHopsToAnchors > MAX_HOPS_ANCHOR)
			sectorsCount = CORRECTIVE_SECTOR_COUNT;
		else
			sectorsCount = LPDGeom.SECTORS_COUNT;
	}

	/**
	 * @param node
	 * @param avgRadius
	 *            : might be different from the actual radius (something like an
	 *            average radius)
	 */
	public LinkProbabilityDistribution(MSLostNode mslNode,
			int MAX_FAILED_ESTIMATIONS_COUNT) {
		super(mslNode, MAX_FAILED_ESTIMATIONS_COUNT);
//		LogLPD.LOG_AREA(getNode(), getFieldCB(), getFieldArr());
	}

	private void setOneHopAnchorPercentages(Anchor anchor) {
		List<Point2D> circlePoints = lpdGeom.getCirclePoints(new Circle(anchor
				.getCalculatedCenter(), avgRadius));
		int integral;
		for (int i = 0; i < getFieldArr().length; i++) {
			for (int j = 0; j < getFieldArr()[0].length; j++) {
				if (getFieldArr()[i][j] != FieldArray.IMPOSSIBLE) {
					integral = lpdGeom.integral(circlePoints, getFieldCB()
							.getRectangle(i, j));
					getFieldArr()[i][j] += integral;
				}
			}
		}
	}

	@Override
	protected void fillFieldMatrix() {
		init();
		List<Anchor> listAnchors = getNode().getMemory().getAnchors();
		List<Point2D> emptyList = new ArrayList<Point2D>();

		int minHopsToAnchors = getNode().getMinHop2Anchors();
		if (minHopsToAnchors < MAX_HOPS_ANCHOR)
			minHopsToAnchors = MAX_HOPS_ANCHOR;

		for (Anchor anchor : listAnchors) {
			int hops = getNode().getMemory().getHopCount(anchor);
			if (hops <= minHopsToAnchors) {
				inPrevCirclesCount = 0;
				// lpdExecLogPrintln(0, anchor);

				Point2D center = anchor.getCalculatedCenter();

				if (getNode().isLogged())
					System.out.print("");

				if (hops == 1) {
					setOneHopAnchorPercentages(anchor);
				} else {
					recursiveNodeGenerator(center.getX(), center.getY(), 1,
							emptyList, getFieldArr(), hops);
				}
			}
		}
	}

	/**
	 * Recursively generates new nodes obtained by adding
	 * center2SectorDisplacement to the current node to get to all possible
	 * positions for a node
	 * 
	 * @param father
	 * @param hopNo
	 * @param listCenters
	 * @param area
	 * @param maxHop
	 */
	private void recursiveNodeGenerator(double fatherX, double fatherY,
			int hopNo, List<Point2D> listCenters, double[][] area, int maxHop) {
		Point2D father = new Point2D.Double(fatherX, fatherY);
		if (LPDGeom.isInPrevCircles(father, listCenters, avgRadius)) {
			inPrevCirclesCount++;
			return;
		}
		listCenters.add(father);

		if (hopNo <= maxHop) {
			// lpdExecLogPrintln(hopNo, "middle: " + fatherX + " " + fatherY);
			for (int i = 0; i < sectorsCount; i++) {
				recursiveNodeGenerator(fatherX
						+ lpdGeom.getCenter2SectorDisplacement()[i].getX(),
						fatherY
								+ lpdGeom.getCenter2SectorDisplacement()[i]
										.getY(), hopNo + 1, listCenters, area,
						maxHop);
			}
		} else if (hopNo > maxHop) {
			// lpdExecLogPrintln(hopNo, fatherX + " " + fatherY);
			int x = getFieldCB().getContainingRectangleXIndex(fatherX);
			int y = getFieldCB().getContainingRectangleYIndex(fatherY);
			if (getFieldCB().isValidRectIndex(x, y)) {
				// lpdExecLogPrintln(hopNo, x + " " + y);
				if (area[x][y] != FieldArray.IMPOSSIBLE) {
					// lpdExecLogPrintln(hopNo, "Y");
					updateArea(x, y, maxHop);
					// LOG_SYSTEM(area, hopNo);
				}
			}
		}
		listCenters.remove(listCenters.size() - 1);
	}

	private void updateArea(int xIndex, int yIndex, int hops) {
		getFieldArr()[xIndex][yIndex] += 1 * Math.pow(1.0 / sectorsCount,
				hops - 1);
	}

	private static int inPrevCirclesCount = 0;

	// static FileOutputStream exeFile = null;
	// static PrintStream ps = null;
	// static {
	// try {
	// exeFile = new FileOutputStream(ZXControl.OUTPUT_DIRECTORY
	// + "LPD ExecLog" + ".txt");
	// ps = new PrintStream(exeFile);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// void printExecTabs(int c) {
	// for (int i = 0; i < c; i++)
	// ps.print("\t");
	// }
	//
	// void lpdExecLogPrintln(int tabs, Object o) {
	// printExecTabs(tabs);
	// ps.println(o);
	// }
	//
	// void lpdExecLogPrint(Object o) {
	// ps.print(o);
	// }
	//
	// private void LOG_SYSTEM(double[][] area, int tabs) {
	// if (getNode().isLogged()) {
	// for (int j = 0; j < area.length; j++) {
	// printExecTabs(tabs);
	// for (int i = 0; i < area.length; i++) {
	// // ps.print(ZXControl.doubleToString(area[i][j]) +
	// // "\t");
	// if (area[i][j] == -1) {
	// lpdExecLogPrint("a\t");
	// } else {
	// lpdExecLogPrint((int) area[i][j] + "\t");
	// }
	// }
	// lpdExecLogPrintln(0, "");
	// }
	// }
	// lpdExecLogPrintln(0, "");
	// }
}
