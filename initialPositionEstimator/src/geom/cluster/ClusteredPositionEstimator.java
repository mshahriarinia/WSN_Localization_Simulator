package geom.cluster;

import geom.checkerBoard.FieldCheckerBoard;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import node.Neighbor;
import node.Node;
import node.localization.tools.NodePosition;

/**
 * TODO We need to inspect more about if we choose the true cluster among all <br>
 * <br>
 * if we lose the true node position rectangle and irs rate <br>
 * <br>
 * the formula to determine node position based on filtered cluster rects or
 * main rects or ...
 */
public abstract class ClusteredPositionEstimator {
	private int failedEstimationCount;

	private Node node;
	private Point2D positionEstimate;

	private double clusterHighPassFilterThreshold;

	private List<ClusteredRegion> listCRs;

	FieldCheckerBoard fieldCB;

	//TODO What's this???
	private int MAX_FAILED_ESTIMATIONS_COUNT;

	private MatrixClusterer matrixClusterer;

	public ClusteredPositionEstimator(FieldCheckerBoard fieldCB, Node node,
			double clusterHighPassFilterThreshold,
			int MAX_FAILED_ESTIMATIONS_COUNT) {
		failedEstimationCount = 0;
		positionEstimate = null;
		this.node = node;

		this.clusterHighPassFilterThreshold = clusterHighPassFilterThreshold;

		this.fieldCB = fieldCB;
		this.MAX_FAILED_ESTIMATIONS_COUNT = MAX_FAILED_ESTIMATIONS_COUNT;

		init();
	}

	private void init() {
		final ClusteredPositionEstimator thisCPE = this;
		matrixClusterer = new MatrixClusterer(fieldCB,
				clusterHighPassFilterThreshold) {
			@Override
			public double getValue(int xIndex, int yIndex) {
				return thisCPE.getValue(xIndex, yIndex);
			}

			@Override
			public double getValue(Rectangle2D rect) {
				return thisCPE.getValue(rect);
			}
		};
		if (node.isLogged())
			System.out.print("");

		listCRs = matrixClusterer.getClusteredRegions();

	};

	public void LOG_FIELD_CLUSTERS(PrintStream ps) {
		matrixClusterer.LOG_FIELD_CLUSTERS(ps);
	}

	public Point2D getNodePosition() {

		if (node.isLogged())
			System.out.print("");

		if (HandleClusters.isOnlyOneCluster(listCRs)) {
			if (positionEstimate == null)
				positionEstimate = getCalculatedCenter(listCRs.get(0));
		} else {
			failedEstimationCount++;

			// List<ClusterVote> maxCVs = HandleClusters.maxVotes(node,
			// listCRs);
			// positionEstimate = getNodePosition(maxCVs);

			positionEstimate = getCalculatedCenter(getClusteredRegion_ClusterDistance(listCRs));

		}
		return positionEstimate;
	}

	private ClusteredRegion getClusteredRegion_ClusterDistance(
			List<ClusteredRegion> listCRs) {
		double sumDist;
		double minDist = Double.MAX_VALUE;
		int minIndex = 0;
		
		List<Double> listClusterDistance = new ArrayList<Double>();

		int neighborNPDCount = 0;
		for (int i = 0; i < listCRs.size(); i++) {
			sumDist = 0;
			for (Neighbor n : node.getNeighbors()) {
				NodePosition np = node.getMemory().getLatestNodePosition(
						n.getOtherNode(node));
				if (np != null) {
					neighborNPDCount++;
					sumDist += np.getPosition().distance(
							listCRs.get(i).getWeightedCenter());
				}
			}
			listClusterDistance.add(sumDist);

			if (sumDist < minDist) {
				minIndex = i;
				minDist = sumDist;
			}
		}
		if (neighborNPDCount > 0)
			return listCRs.get(minIndex);
		else
			return null;
	}

	// private Point2D getNodePosition(List<ClusterVote> maxCVs) {
	// if (maxCVs.size() == 1)
	// return getCalculatedCenter(maxCVs.get(0).getClusteredRegion());
	//
	// // choose on of the maxClusterVotes by random and estimate
	// if (failedEstimationCount > MAX_FAILED_ESTIMATIONS_COUNT)
	// return getCalculatedCenter(maxCVs.get(0).getClusteredRegion());
	//
	// return null;
	// }

	public abstract Point2D getCalculatedCenter(ClusteredRegion cR);

	public abstract double getValue(int xIndex, int yIndex);

	public abstract double getValue(Rectangle2D rect);
}
