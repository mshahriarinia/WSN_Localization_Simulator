package algorithms.discreteField.angular;

import geom.checkerBoard.FieldCheckerBoard;
import geom.image.Filter;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import node.Node;
import node.localization.tools.sectorBased.NodeNeighborSectorNo;
import node.localization.tools.sectorBased.Sector;
import node.tools.TimedData;
import control.ZXControl;

/**
 * This is performed after initial position estimation.<br>
 * <br>
 * 1. Combine this info with the current rects<br>
 * <br>
 * 2. use lower resolution rects<br>
 * <br>
 * what's the bound?<br>
 * <br>
 * from each neighbor circle choose the min and max x, y<br>
 * <br>
 * the largest area will be the due<br>
 * <br>
 */
public class AngularPositioning {

	private int execCount = 0;

	private final int MAX_Exec_Count = 4 + 2;
	// 2 to allow farther neighbors to anchors start

	private Node node;

	private boolean init;

	/**
	 * list of NodeNeighborSectorNo from neighbors point of view.<br>
	 * <br>
	 * Node :list neighbors<br>
	 * <br>
	 * neighbor : this node
	 */
	private List<NodeNeighborSectorNo> listSelfNNSNs;

	public AngularPositioning(Node node) {
		this.node = node;
		init = false;
		listSelfNNSNs = new ArrayList<NodeNeighborSectorNo>();
	}

	public Point2D getEstimate() {
		execCount++;
		if (node.isLogged())
			System.out.print("");

		if (!init) {
			init = true;
			node.getAngulationManager().sendNNSNMessages();
		}

		addNewNNSNs();

		// TODO check the proper timing of starting the algorithm
		// TODO currently let's discard the previous estimate and just count on
		// the current estimate

		if (listSelfNNSNs.size() == node.getNeighbors().size())
			return getEstimate(listSelfNNSNs);
		else
			return null;
	}

	private Point2D getEstimate(List<NodeNeighborSectorNo> listNNSNs) {
		Rectangle2D surroundingRectangle = getSurroundingRectangle();
		int sliceLength = node.getRadius() / 10;

		FieldCheckerBoard fieldCB = new FieldCheckerBoard(surroundingRectangle,
				sliceLength, false);

		double[][] areaHitsCounter = new double[fieldCB.getXAxisSlicesNo()][fieldCB
				.getYAxisSlicesNo()];// /////////////////// array col,row

		setHitsCounts(fieldCB, areaHitsCounter, listNNSNs);
		
//		if (node.isLogged())
//			report(areaHitsCounter);
		
		areaHitsCounter =	Filter.meanFilterRestoreZero(areaHitsCounter, 3);

//		if (node.isLogged())
//			report(areaHitsCounter);

		return getWeightedCentroid(fieldCB, areaHitsCounter);
	}

	private Point2D getWeightedCentroid(FieldCheckerBoard fieldCB, double[][] area) {
		double x = 0;
		double y = 0;
		double sumValues = 0;

		for (int i = 0; i < area.length; i++) {
			for (int j = 0; j < area[0].length; j++) {
				sumValues += area[i][j];
				x += area[i][j] * fieldCB.getRectangle(i, j).getCenterX();
				y += area[i][j] * fieldCB.getRectangle(i, j).getCenterY();
			}
		}
		x /= sumValues;
		y /= sumValues;

		return new Point2D.Double(x, y);
	}

	private void setHits(FieldCheckerBoard fieldCB, double[][] area, Sector sector) {
		Rectangle2D rect;
		int a1 = area[0].length;
		int a2 = area.length;

		for (int i = 0; i < a2; i++) {
			for (int j = 0; j < a1; j++) {
				rect = fieldCB.getRectangle(i, j);
				boolean intersects = sector.intersects(rect);
				boolean containsCentroid = sector.containsCentroid(rect);
				if (intersects || containsCentroid)
					area[i][j]++;// ///////////////////////// array col,row
			}
		}
	}

	/**
	 * counts the number of sectors containing at least part of the rectangle.
	 * 
	 * @param fieldCB
	 * @param area
	 * @param listSectors
	 */
	private void setHitsCounts(FieldCheckerBoard fieldCB, double[][] area,
			List<NodeNeighborSectorNo> listNNSNs) {
		for (NodeNeighborSectorNo nnsn : listNNSNs) {
			setHits(fieldCB, area, nnsn.getNoisySector());
		}
	}

	/**
	 * surrounding rectangular area, by the idea of neighbors
	 * 
	 * @return
	 */
	private Rectangle2D getSurroundingRectangle() {
		double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;

		for (NodeNeighborSectorNo nnsn : listSelfNNSNs) {
			minX = Math.min(minX, nnsn.getNoisySector().getMinX());
			minY = Math.min(minY, nnsn.getNoisySector().getMinY());

			maxX = Math.max(maxX, nnsn.getNoisySector().getMaxX());
			maxY = Math.max(maxY, nnsn.getNoisySector().getMaxY());
		}

		if (minX < 0)
			minX = 0;
		if (minY < 0)
			minY = 0;

		if (maxX > ZXControl.FIELD_WIDTH)
			maxX = ZXControl.FIELD_WIDTH;
		if (maxY > ZXControl.FIELD_HEIGHT)
			maxY = ZXControl.FIELD_HEIGHT;
		return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
	}

	/**
	 * NodeNeighborSectorNo,<br>
	 * <br>
	 * each node only send NNSNs just once.
	 */
	private void addNewNNSNs() {
		List<TimedData> listProcessed = new ArrayList<TimedData>();
		for (TimedData td : node.getMemory().getListVarietyData()) {
			if (td.getData() instanceof NodeNeighborSectorNo) {
				listSelfNNSNs.add((NodeNeighborSectorNo) td.getData());
				listProcessed.add(td);
			}
		}
		node.getMemory().getListVarietyData().removeAll(listProcessed);
	}

	private static void report(double[][] area) {
		FileOutputStream exeFile = null;
		PrintStream ps = null;

		try {
			exeFile = new FileOutputStream(ZXControl.OUTPUT_DIRECTORY
					+ "angular Info" + ".txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ps = new PrintStream(exeFile);

		for (int j2 = area[0].length - 1; j2 >= 0; j2--) {
			for (int j1 = 0; j1 < area.length; j1++) {

				ps.print(area[j1][j2] + "\t");
			}
			ps.println();
		}

		ps.close();
		try {
			exeFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		Line2D l = new Line2D.Double(49.99, 28.29, 17.46, -50.23);

		Line2D lh = new Line2D.Double(0, 24, 56, 24);

		System.out.println(l.intersectsLine(lh));
	}

	public boolean isFinished() {
		return execCount > MAX_Exec_Count;
	}
}
