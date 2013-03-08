package simulator.localizable;

import geom.MyRectArea;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import node.Destination;
import node.NodeList;
import node.localization.tools.NodePosition;
import node.tools.Message;
import node.type.LostNode;
import simulator.localizable.State.LocalizationPhase;
import algorithms.basic.COG;
import algorithms.basic.Proximity;
import algorithms.centralizedBestAnchoredFirst.CentralizedBestAnchoredFirst;
import algorithms.discreteField.DiscreteFieldLocalization;
import algorithms.discreteField.angular.AngularPositioning;
import algorithms.discreteField.clustering.Clusterable;
import algorithms.discreteField.clustering.HierarchicalClustering;
import algorithms.discreteField.dpdv.DiscreteDV;
import algorithms.discreteField.lpd.LinkProbabilityDistribution;
import algorithms.dv.DV;
import algorithms.ms.RangeFreeMassSpring;
import control.ZXControl;
import control.ZXControl.ETYPE;

/**
 * Mass-Spring Lost Node
 */
public class MSLostNode extends LostNode implements Clusterable {

	public static final int NOT_AVAILABLE = -1;

	private LocalizationPhase localizationPhase = LocalizationPhase.ANCHORS_RECEPTION;

	private Rectangle2D area;

	private int averageHopSize;

	private AngularPositioning angularPositioning;

	private HierarchicalClustering hierarchicalClustering;

	private RangeFreeMassSpring rangeFreeMassSpring;

	private DiscreteFieldLocalization discreteFieldLocalization;

	private CentralizedBestAnchoredFirst centralizedBestAnchoredFirst;

	private int MAX_FAILED_ESTIMATIONS_COUNT;

	private boolean hasLongDifferenceFlag;

	private boolean executed;

	public MSLostNode(NodeList nodeList, Rectangle2D area, double x, double y) {
		super(nodeList, x, y);
		this.area = area;
		averageHopSize = AnchorAvgHopSizeWrapper.NOT_AVAILABLE;
	}

	public MSLostNode(NodeList nodeList, Rectangle2D area, Point2D center,
			int radius) {
		super(nodeList, center, radius);
		this.area = area;
		averageHopSize = AnchorAvgHopSizeWrapper.NOT_AVAILABLE;
	}

	public MSLostNode(NodeList nodeList, Rectangle2D area, Point2D center) {
		super(nodeList, center);
		this.area = area;
		averageHopSize = AnchorAvgHopSizeWrapper.NOT_AVAILABLE;
	}

	@Override
	public void reset() {
		super.reset();
		localizationPhase = LocalizationPhase.ANCHORS_RECEPTION;
		averageHopSize = AnchorAvgHopSizeWrapper.NOT_AVAILABLE;
		angularPositioning = null;
		rangeFreeMassSpring = null;
		discreteFieldLocalization = null;
	}

	/**
	 * Returns true if there was something to relay or there was something for
	 * ourself or was waiting in anchor reception
	 */
	@Override
	public boolean execute(int cycleNo) {

		executed = false;
		hasLongDifferenceFlag = false;

		if (isLogged())
			System.out.print("");

		executed = initAverageHopSize() || executed;
		executed = getInboxManager().manageInbox() || executed;

		switch (localizationPhase) {
		case ANCHORS_RECEPTION:
			executed = state_ANCHORS_RECEPTION(cycleNo) || executed;
			;
			break;
		case SEND_INITIAL_POSE:
			executed = state_SEND_INITIAL_POSE(cycleNo) || executed;
			;
			break;
		case MASS_SPRING_REFINING:
			executed = state_MASS_SPRING_REFINING() || executed;
			;
			break;
		case ANGULAR_REFINING:
			executed = state_ANGULAR_REFINING() || executed;
			;
			break;
		case CLUSTERING:
			executed = state_HIERARCHICAL_CLUSTERING() || executed;
			;// Should be performed until there is no higher level cluster
				// neighbor
			break;
		}
		getInboxManager().clearReceivedMessageList();
		return executed;
	}

	/**
	 * 
	 * @return True if initial estimation was successful
	 */
	private boolean initialEstimation() {
		initMAX_FAILED_ESTIMATIONS_COUNT();

		Point2D p = null;
		if (isLogged())
			System.out.print("");
		switch (ZXControl.Initial_POSITION_ALGORITHM) {
		case CENTRALIZED_BEST_ANCHORED_FIRST:

			if (centralizedBestAnchoredFirst == null) {
				centralizedBestAnchoredFirst = new CentralizedBestAnchoredFirst(
						this);
			}
			p = centralizedBestAnchoredFirst.getEstimate();
			break;
		case LPD:
			if (discreteFieldLocalization == null) {
				discreteFieldLocalization = new LinkProbabilityDistribution(
						this, MAX_FAILED_ESTIMATIONS_COUNT);
			}
			p = discreteFieldLocalization.getEstimate();
			break;
		case DISCRETE_DV:
			if (discreteFieldLocalization == null)
				discreteFieldLocalization = new DiscreteDV(this,
						MAX_FAILED_ESTIMATIONS_COUNT);
			p = discreteFieldLocalization.getEstimate();
			break;
		case PROXIMITY:
			p = Proximity.getInitProximity(this);
			break;
		case CENTROID:
			p = COG.getInitCOG(this);
			break;
		case DV:
			p = DV.getInitDV(this);
			break;
		}

		boolean success = true;
		if (p != null)
			MyRectArea.setInDomainValue(p, 0, ZXControl.FIELD_WIDTH, 0,
					ZXControl.FIELD_HEIGHT);
		setHasLongDifference(p, getCalculatedCenter());
		if (p == null) {
			success = false;
			String report = "~null " + getId();
			System.out.println(report);
			getExecutionReporter().writeln(report);
		} else {
			setCalculatedCenter(p);
		}
		return success;
	}

	/**
	 * basic algorithms do not have other steps after initial position
	 * estimation.
	 * 
	 * @return
	 */
	public static boolean IS_BASIC_ALGORITHM() {
		ETYPE algorithm = ZXControl.Initial_POSITION_ALGORITHM;
		if (algorithm == ETYPE.CENTROID || algorithm == ETYPE.DV
				|| algorithm == ETYPE.PROXIMITY
				|| algorithm == ETYPE.CENTRALIZED_BEST_ANCHORED_FIRST)
			return true;
		else
			return false;
	}

	private boolean state_ANCHORS_RECEPTION(int cycleNo) {
		if (cycleNo > ZXControl.MAX_ANCHORS_BROADCAST) {
			if (averageHopSize != AnchorAvgHopSizeWrapper.NOT_AVAILABLE) {
				localizationPhase = LocalizationPhase.SEND_INITIAL_POSE;
				MAX_FAILED_ESTIMATIONS_COUNT = ZXControl.MAX_ANCHORS_BROADCAST
						/ 2 + getMinHop2Anchors();
			}
		}
		return true;
	}

	/**
	 * 
	 * @return Always true which means it executed something. Due to
	 *         difficulties it might remain in SEND_INITIAL_POSE phase.
	 */
	private boolean state_SEND_INITIAL_POSE(int cycleNo) {
		boolean estimationSuccess = initialEstimation();

		if (estimationSuccess) {
			String report = "init "
					+ ZXControl.getTitle(ZXControl.Initial_POSITION_ALGORITHM)
					+ " " + getId();
			System.out.println(report);
			getExecutionReporter().writeln(report);
		}

		if (estimationSuccess && hasLongDifferenceFlag) {
			sendAssumedPosition();
		}

		// cut off the initial results of basic algorithms
		if (IS_BASIC_ALGORITHM())
			return false;

		if (canStartRefiningPhase(cycleNo)) {
			// localizationPhase = LocalizationPhase.MASS_SPRING_REFINING;
			localizationPhase = LocalizationPhase.CLUSTERING;
			// TODO skip other refinings and jump to clustering for testing
			// purposes for now
		}

		return true;
	}

	public boolean canStartRefiningPhase(int cycleNo) {
		if (IS_BASIC_ALGORITHM()) {
			return true;
		} else {
			if (cycleNo > ZXControl.MAX_ANCHORS_BROADCAST
					+ MAX_FAILED_ESTIMATIONS_COUNT) {
				// TODO all node goes to the refining stage all together
				return true;
			}
		}
		return false;
	}

	private boolean state_MASS_SPRING_REFINING() {
		if (rangeFreeMassSpring == null) {
			rangeFreeMassSpring = new RangeFreeMassSpring(this);
			String report = "MassSpring " + this.getId();
			System.out.println(report);
			getExecutionReporter().writeln(report);
		}

		if (!rangeFreeMassSpring.isFinished()) {
			Point2D newEstimate = rangeFreeMassSpring.getNextEstimate();
			updateCalculatedCenter(newEstimate);
			return true;
		} else {
			localizationPhase = LocalizationPhase.ANGULAR_REFINING;
			return false;
		}
	}

	private boolean state_ANGULAR_REFINING() {
		if (angularPositioning == null) {
			angularPositioning = new AngularPositioning(this);
			String report = "Node " + getId().getValue()
					+ " startes angular refining.";
			System.out.println(report);
			getExecutionReporter().writeln(report);
		}
		if (!angularPositioning.isFinished()) {
			Point2D newEstimate = angularPositioning.getEstimate();
			updateCalculatedCenter(newEstimate);
			return true;
		} else {
			localizationPhase = LocalizationPhase.CLUSTERING;
			return false;
		}
	}

	@Override
	public boolean canStartClustering(int cycleNo) {
		return canStartRefiningPhase(cycleNo);
	}

	@Override
	public boolean state_HIERARCHICAL_CLUSTERING() {
		if (hierarchicalClustering == null) {
			hierarchicalClustering = new HierarchicalClustering(this);
			String report = "Node " + getId().getValue()
					+ " startes Hierarchical Clustering.";
			System.out.println(report);

			getExecutionReporter().writeln(report);
		}
		if (!hierarchicalClustering.isFinished()) {
			hierarchicalClustering.execute();

			Point2D newEstimate = hierarchicalClustering.getEstimate();
			updateCalculatedCenter(newEstimate);

			return true;
		} else
			return false;
	}

	private void updateCalculatedCenter(Point2D newEstimate) {
		if (newEstimate != null) {
			setHasLongDifference(getCalculatedCenter(), newEstimate);

			if (hasLongDifferenceFlag) {
				setCalculatedCenter(newEstimate);
				sendAssumedPosition();
			}
		}
	}

	private void setHasLongDifference(Point2D p1, Point2D p2) {
		hasLongDifferenceFlag = p2 == null
				|| p1 == null
				|| p1.distance(p2) > getNodeList().getZXControl().UPDATE_THRESHOLD_TO_TRANSMIT;
		// ExecutionReporter.writeln("update: " + ZXControl.pointToString(p1)
		// + " -> " + ZXControl.pointToString(p2));
	}

	/**
	 * 
	 * @return if the estimation has long difference to the previous estimation
	 */
	boolean getHasLongDifferenceFlag() {
		return hasLongDifferenceFlag;
	}

	private void sendAssumedPosition() {
		getInboxManager()
				.send(new Message(getId(), new Destination(1),
						new NodePosition(this)));
	}

	public int getMAX_FAILED_ESTIMATIONS_COUNT() {
		return MAX_FAILED_ESTIMATIONS_COUNT;
	}

	public void initMAX_FAILED_ESTIMATIONS_COUNT() {
		MAX_FAILED_ESTIMATIONS_COUNT = ZXControl.MAX_ANCHORS_BROADCAST / 2
				+ getMinHop2Anchors();
	}

	public int getAverageHopSize() {
		if (averageHopSize == 0)// no anchor neighbor
			return getRadius();
		return averageHopSize;
	}

	public LocalizationPhase getLocalizationPhase() {
		return localizationPhase;
	}

	public DiscreteFieldLocalization getDiscreteFieldLocalization() {
		return discreteFieldLocalization;
	}

	public AngularPositioning getAngularPositioning() {
		return angularPositioning;
	}

	public RangeFreeMassSpring getRangeFreeMassSpring() {
		return rangeFreeMassSpring;
	}

	public Rectangle2D getArea() {
		return area;
	}

	@Override
	public HierarchicalClustering getHierarchicalClustering() {
		return hierarchicalClustering;
	}

	private boolean initAverageHopSize() {
		int tempAverageHopSize = AverageHopSizeMessageHandler
				.handleAverageHopSizeMessages(this);
		if (averageHopSize == AnchorAvgHopSizeWrapper.NOT_AVAILABLE) {
			averageHopSize = tempAverageHopSize;
			return true;
		}
		return false;
	}

	// ///////////////////

}
