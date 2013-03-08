package algorithms.discreteField.dpdv;

import geom.RandomVariable;
import geom.image.FieldArray;

import java.util.ArrayList;
import java.util.List;

import node.Node;
import simulator.localizable.MSLostNode;
import algorithms.discreteField.DiscreteFieldLocalization;
import algorithms.discreteField.dpdv.acp.AreaCirclePercentAccumulation;

/**
 * 
 * this is about the initial position estimation obtained by the rectangular
 * board made by anchors and possibility portion of circle in each rectangle
 * 
 * possible upgrades:
 * <p>
 * 1. node can determine how much of each circles resides out side of the
 * rectangle and can extend the refs areas to include it => not so close to mind
 * or useful
 * <p>
 * 2. node can reside on the circle instead of in the middle of the refs area
 * block => good
 * <p>
 * 3. initial mass-spring=> good
 * <p>
 * Use threshold to make different clusters <> Use threshold to more regions in
 * a cluster means more weight to be chosen------------------------
 * <p>
 * COmpare this CLUSTER_HIGH_PASS_FILTER_THRESHOLD for other sample graphs : The
 * results showed improvements.
 * <p>
 * Parameters important on choosing a cluster: 1. cluster population. 2. Cluster
 * population sum and average value. 3. Neighboring constraints (more impact
 * from nodes closer to anchors)
 * <p>
 * We need to consider incremental approach.
 */
public class DiscreteDV extends DiscreteFieldLocalization {

	private ListACPA listACPA;

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

	@Override
	protected void fillFieldMatrix() {
		listACPA = new ListACPA(getNode(), getFieldCB());
		listACPA.initializeNetworkACPAs();

		int newline = 0;
		int i = 0;
		for (AreaCirclePercentAccumulation acpa : listACPA.getListACPAs()) {
			if (getFieldArr()[newline][i] != FieldArray.IMPOSSIBLE) {
				getFieldArr()[newline][i] = acpa.getPercent();
			}
			i++;
			if (i >= getFieldCB().getXAxisSlicesNo()) {
				i = 0;
				newline++;
			}
		}
	}

	public DiscreteDV(MSLostNode mslNode, int MAX_FAILED_ESTIMATIONS_COUNT) {
		super(mslNode, MAX_FAILED_ESTIMATIONS_COUNT);
//		LogACPAs.LOG(mslNode, getFieldCB(), listACPA, getFieldArr(), getCPE());
	}

	/**
	 * @ TODO OO ACPA Comparison Tip: if a rectangle has more anchors with less
	 * percent its better than a rect with one anchor and more percent
	 * <p>
	 * For every call reinitialize the process to the hope that more neighboring
	 * info is available
	 * 
	 * @param node
	 * @return Either the calculated unique point or list of possible regions to
	 *         decide on later
	 */
}