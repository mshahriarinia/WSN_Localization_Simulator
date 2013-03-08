package geom.cluster;

import geom.RandomVariable;
import geom.checkerBoard.FieldCheckerBoard;

import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import control.ZXControl;

public abstract class MatrixClusterer {

	private double clusterHighPassFilterThreshold;

	private static final int VISITED = -1;

	private static final int NOT_VISITED = 0;

	private FieldCheckerBoard fieldCB;

	/**
	 * used to assign cluster numbers
	 */
	private int[][] fieldArr;

	public MatrixClusterer(FieldCheckerBoard fieldCB,
			double clusterHighPassFilterThreshold) {
		this.fieldCB = fieldCB;
		fieldArr = new int[fieldCB.getXAxisSlicesNo()][fieldCB
				.getYAxisSlicesNo()];
		this.clusterHighPassFilterThreshold = clusterHighPassFilterThreshold;
	}

	private int assignClusterTags(int[][] fieldArr) {
		int clustersNo = 0;
		// check up-down rows then left-right
		for (int i = 0; i < fieldCB.getXAxisSlicesNo(); i++) {
			for (int j = 0; j < fieldCB.getYAxisSlicesNo(); j++) {

				if (j == 11)
					System.out.print("");
				if (fieldArr[i][j] == NOT_VISITED) {
					// ExecutionReporter.writeln("\tFor Cluster");
					if (cluster(fieldArr, clustersNo + 1, i, j)) {
						clustersNo++;
					}
				}
			}
		}
		return clustersNo;
	}

	/**
	 * Recursive method which sets region numbers to separate region clusters in
	 * the fieldArray. Y increases from top to down.
	 * 
	 * @param r
	 *            : HuntRegionRecursiveData
	 * @param clusterNo
	 * @param i
	 *            : rectangle x index
	 * @param j
	 *            : rectangle y index
	 * @return if it has changed the regions table (found a new region), not
	 *         only visited
	 */
	private boolean cluster(int[][] fieldArr, int clusterNo, int i, int j) {
		// ExecutionReporter.write("\t\tcluster " + clusterNo + " [" + i + ", "
		// + j + "] ");

		double value = getValue(i, j);

		// ExecutionReporter.writeln("\t\tACPA Index: " + index + " " + acpa);
		if (value < clusterHighPassFilterThreshold) {
			// ExecutionReporter.writeln("VISITED");
			fieldArr[i][j] = VISITED;
			return false;
		} else {
			// ExecutionReporter.writeln("Cluster Tag=" + clusterNo);

			// %%|
			// %O|
			// %||
			/**
			 * | means check. % means it's been already checked and we dont
			 * check as go deeper
			 */
			fieldArr[i][j] = clusterNo;

			if (isVisitable(fieldArr, i, j + 1))
				cluster(fieldArr, clusterNo, i, j + 1);

			if (isVisitable(fieldArr, i, j - 1))
				cluster(fieldArr, clusterNo, i, j - 1);

			if (isVisitable(fieldArr, i + 1, j + 1))
				cluster(fieldArr, clusterNo, i + 1, j + 1);
			if (isVisitable(fieldArr, i + 1, j))
				cluster(fieldArr, clusterNo, i + 1, j);
			if (isVisitable(fieldArr, i + 1, j - 1))
				cluster(fieldArr, clusterNo, i + 1, j - 1);

			if (isVisitable(fieldArr, i - 1, j + 1))
				cluster(fieldArr, clusterNo, i - 1, j + 1);
			if (isVisitable(fieldArr, i - 1, j))
				cluster(fieldArr, clusterNo, i - 1, j);
			if (isVisitable(fieldArr, i - 1, j - 1))
				cluster(fieldArr, clusterNo, i - 1, j - 1);

			return true;
		}
	}

	/**
	 * network field array is asumed to be in the same direction of UI forms (x
	 * from left to right, y up to down)
	 * <p>
	 * array to store regions of neighboring possible rects
	 * 
	 * @param fieldCB
	 * @param listACPAs
	 * @return the clustered regions rectangles in an array of list of rects
	 */
	public List<ClusteredRegion> getClusteredRegions() {

		int regionsCount = assignClusterTags(fieldArr);
		// LOG_FIELD_CLUSTERS();
		List<ClusteredRegion> listClusteredRegions = getEmptyCRs(regionsCount);

		int regionIndex = 0;
		for (int i = 0; i < fieldCB.getXAxisSlicesNo(); i++) {
			for (int j = 0; j < fieldCB.getYAxisSlicesNo(); j++) {
				if (fieldArr[i][j] != VISITED) {
					regionIndex = fieldArr[i][j] - 1;
//					if (getValue(i, j) == 0) TODO Why only in LPD
//						throw new RuntimeException("0 percent - index " + i + " " + j);
					listClusteredRegions.get(regionIndex).addRect(
							fieldCB.getRectangle(i, j));
				}
			}
		}

		RandomVariable<ClusteredRegion> cRRandomVar = new RandomVariable<ClusteredRegion>(
				listClusteredRegions) {
			@Override
			// TODO check 95% results
			public double getValue(ClusteredRegion randomEvent) {
				return ((ClusteredRegion) randomEvent).getRegionsCount();
			}
		};

		double confidenceInterval = cRRandomVar.getCustomConfidenceInterval();

		List<ClusteredRegion> result95 = cRRandomVar.getCustomConfidenceList();

		return result95;
	}

	private List<ClusteredRegion> getEmptyCRs(int count) {
		final MatrixClusterer mC = this;
		List<ClusteredRegion> listClusteredRegions = new ArrayList<ClusteredRegion>();
		for (int i = 0; i < count; i++) {
			listClusteredRegions.add(new ClusteredRegion() {
				@Override
				public double getValue(Rectangle2D rect) {
					return mC.getValue(rect);
				}
			});
		}
		return listClusteredRegions;
	}

	private void LOG_FIELD_CLUSTERS() {
		FileOutputStream exeFile = null;
		PrintStream ps = null;
		try {
			exeFile = new FileOutputStream(ZXControl.OUTPUT_DIRECTORY
					+ "/matrixClustererLog" + ".txt");
			ps = new PrintStream(exeFile);
			LOG_FIELD_CLUSTERS(ps);

			ps.close();
			exeFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void LOG_FIELD_CLUSTERS(PrintStream ps) {
		for (int j = 0; j < fieldArr[0].length; j++) {
			for (int i = 0; i < fieldArr.length; i++) {
				ps.print(fieldArr[i][j] + "\t");
			}
			ps.println();
		}
	}

	private boolean isVisitable(int[][] fieldArr, int i, int j) {
		if ((i < 0 || i >= fieldCB.getXAxisSlicesNo())
				|| (j < 0 || j >= fieldCB.getYAxisSlicesNo())
				|| (fieldArr[i][j] != NOT_VISITED))
			return false;
		return true;
	}

	public abstract double getValue(int xIndex, int yIndex);

	public abstract double getValue(Rectangle2D rect);

}
