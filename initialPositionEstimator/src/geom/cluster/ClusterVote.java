package geom.cluster;

import java.util.ArrayList;
import java.util.List;

/**
 * This is used in the process of voting for a cluster when the node doubts
 * which to choose.
 */
public class ClusterVote {

	private ClusteredRegion clusteredRegion;
	private int vote;

	public ClusterVote(ClusteredRegion clusteredRegion) {
		this.clusteredRegion = clusteredRegion;
	}

	public ClusteredRegion getClusteredRegion() {
		return clusteredRegion;
	}

	public int getVote() {
		return vote;
	}

	public void incVote() {
		vote++;
	}

	public static List<ClusterVote> getMaxVotes(List<ClusterVote> listCV) {
		List<ClusterVote> lCV = new ArrayList<ClusterVote>();

		ClusterVote max = listCV.get(0);
		for (ClusterVote cv : listCV) {
			if (cv.getVote() >= max.getVote()) {
				max = cv;
			}
		}

		for (ClusterVote cv : listCV) {
			if (max.vote == cv.vote)
				lCV.add(max);
		}

		return lCV;
	}
}
