package control;//

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;

import node.ICycler;

/**
 * This class is the main controller of the simulation parameters.
 * <p>
 * Different configurations of physical features of the network and algorithmic
 * capabilities of nodes are determined here.
 */
public class ZXControl {

	public static final boolean ANGLE_ENABLED = true;

	public static final int DEBUG_RECT_X = 8, DEBUG_RECT_Y = 0;

	public final int LOG_NODE = 26;

	private static final boolean ALL_REPORTERS = true;

	public static final boolean LOAD_FROM_FILE = true;
	// do not load from file
	public static boolean PRELOAD_NODE_RADIUS = !true || (!LOAD_FROM_FILE);

	public static boolean PRELOAD_ANCHOR_PERCENT = !true || (!LOAD_FROM_FILE);

	public static ETYPE Initial_POSITION_ALGORITHM = ETYPE.CENTRALIZED_BEST_ANCHORED_FIRST;

	public static final int SECTORS_COUNT = 4;

	public static final double SECTOR_ANGLE = 2 * Math.PI / SECTORS_COUNT;

	public static final double SECTOR_DETECTION_FAILIURE_PROBABILITY = 0;

	public static final int FIELD_WIDTH = 500;

	public static final int FIELD_HEIGHT = 500;

	public static final boolean HAS_FORBIDDEN_AREAS = false;

	/**
	 * max broadcast hops of an anchor node declaration message
	 */
	public static final int MAX_ANCHORS_BROADCAST = 10;

	public static final int MINIMUM_ACP_COUNT_TO_KEEP = 3;

	public static final boolean EXECUTION_REPORTER = ALL_REPORTERS && true;

	public static final boolean CONSOLE_REPORTER = ALL_REPORTERS && true;

	public static final boolean GRAPH_REPORTER = ALL_REPORTERS && true;

	public static final boolean NETWORK_ERROR_CYCLE_REPORTER = ALL_REPORTERS && !true;

	public static enum ETYPE {
		LPD, DISCRETE_DV, PROXIMITY, CENTROID, DV, CENTRALIZED_BEST_ANCHORED_FIRST
	};

	// ------non-static fields
	
	public int NODE_RADIUS = 100;

	public double MAX_DISTANCE_NOISE_RANGE_FRACTION = 0;// .25;

	public int NODE_COUNT = 200;

	private double ANCHOR_PERCENT = 5;

	private int ANCHOR_COUNT = (int) Math
			.ceil(((NODE_COUNT * ANCHOR_PERCENT) / 100.0));

	// anchorPercent = (int) Math.floor(anchorCount * 100.0 / n);

	public void setANCHOR_PERCENT(double anchor_percent) {
		ANCHOR_PERCENT = anchor_percent;
		ANCHOR_COUNT = (int) Math.ceil(((NODE_COUNT * ANCHOR_PERCENT) / 100.0));
	}

	public void setANCHOR_COUNT(int anchor_count) {
		ANCHOR_COUNT = anchor_count;
		ANCHOR_PERCENT = (int) Math.floor(anchor_count * 100.0 / NODE_COUNT);
	}
	
	public int getANCHOR_COUNT() {
		return ANCHOR_COUNT;
	}
	
	public double getANCHOR_PERCENT() {
		return ANCHOR_PERCENT;
	}

	

	public int ANCHOR_RADIUS = NODE_RADIUS;

	public double UPDATE_THRESHOLD_TO_TRANSMIT = NODE_RADIUS / 50.0;

	public final int MAXIMUM_ACP_RADIUS = 2 * NODE_RADIUS;

	private ICycler iCycler;

	private static final NumberFormat numberFormat;

	static {
		numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);

		numberFormat.setGroupingUsed(false);
	}

	public static String pointToString(Point2D point2D) {
		if (point2D == null)
			return "(null)";

		return "(" + doubleToString(point2D.getX()) + ", "
				+ doubleToString(point2D.getY()) + ")";
	}

	public static String doubleToString(double d) {
		return numberFormat.format(d);
	}

	public static String rectangleToString(Rectangle2D rect) {
		return "Rect X: " + doubleToString(rect.getMinX()) + " Y: "
				+ doubleToString(rect.getMinY()) + " X2: "
				+ doubleToString(rect.getMaxX()) + " Y2: "
				+ doubleToString(rect.getMaxY());
	}

	public static String OUTPUT_DIRECTORY = "C://temp//outputs//";
	static {
		// OUTPUT_DIRECTORY += "Article Data//avg deg, err//";
		// OUTPUT_DIRECTORY += "Article Data//AP, err//";
	}

	public static final String OUTPUT_FILE_EXECECUTION_PREFIX = "out";

	public static final String OUTPUT_FILE_GRAPH_PREFIX = "Network";

	public static final String OUTPUT_FILE_DIAG_REPORT = "diag";

	public ZXControl(ICycler iCycler) {
		this.iCycler = iCycler;
	}

	public ICycler getICycler() {
		return iCycler;
	}

	public boolean isOld(int cycleNo) {
		return iCycler.getCycleNo() > cycleNo;
	}

	public static String getTitle(ETYPE etype) {
		switch (etype) {
		case CENTROID:
			return "Centroid";
		case DISCRETE_DV:
			return "DISCRETE_DV";
		case DV:
			return "DV";
		case LPD:
			return "LPD";
		case PROXIMITY:
			return "PROXIMITY";

		}
		return "Unknown Etype";
	}
}