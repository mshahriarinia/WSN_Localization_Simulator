package geom.cluster;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import node.Neighbor;
import node.Node;
import node.localization.tools.NodePosition;

public class HandleClusters {

//	public static List<ClusterVote> maxVotes1(Node node,
//			List<ClusteredRegion> listCRs) {
//		List<ClusterVote> listCVs = HandleClusters.voteClusters1(node, listCRs);
//		List<ClusterVote> listMaxCVs = ClusterVote.getMaxVotes(listCVs);
//
//		return listMaxCVs;
//	}

//	/**
//	 * Initializes a list of cluster votes to be used in a voting process.
//	 * 
//	 * @param listClusteredRegions
//	 * @return
//	 */
//	private static List<ClusterVote> initListCV(List<ClusteredRegion> listCRs) {
//		List<ClusterVote> listClusterVote = new ArrayList<ClusterVote>();
//		for (ClusteredRegion cR : listCRs) {
//			listClusterVote.add(new ClusterVote(cR));
//		}
//		return listClusterVote;
//	}
//
//	/**
//	 * The neighbor passed as parameter votes for only one of the clusters in
//	 * the list of cluster votes, which is the closest one. By vote we mean
//	 * increment vote number.
//	 * 
//	 * @param np
//	 * @param listCV
//	 */
//	private static void vote1(NodePosition np, List<ClusterVote> listCV) {
//		Point2D neighborPosition = np.getPosition();
//		ClusterVote minCV = listCV.get(0);
//		double minDistance = neighborPosition.distance(minCV
//				.getClusteredRegion().getWeightedCenter());
//
//		double dist = 0;
//		for (ClusterVote cv : listCV) {
//			dist = neighborPosition.distance(cv.getClusteredRegion()
//					.getWeightedCenter());
//			if (dist < minDistance) {
//				minDistance = dist;
//				minCV = cv;
//			}
//		}
//		minCV.incVote();
//	}

	public static boolean isOnlyOneCluster(List<ClusteredRegion> listCRs) {
		return listCRs.size() == 1;
	}

//	public static List<ClusterVote> voteClusters1(Node node,
//			List<ClusteredRegion> listCRs) {
//		List<ClusterVote> listCVs = initListCV(listCRs);
//
//		for (Neighbor n : node.getNeighbors()) {
//			NodePosition np = node.getMemory().getLatestNodePosition(
//					n.getOtherNode(node));
//
//			if (np != null) {// TODO neighbors send node-pose-possible clusters
//				// #-percentage of 1 clustered neighbors voted
//				// for that cluster. if percentage was more than
//				// 60% use the vote otherwise ignore it
//				vote1(np, listCVs);
//			}
//		}
//		return listCVs;
//	}
}
