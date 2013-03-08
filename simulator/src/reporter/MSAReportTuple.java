package reporter;

import simulator.localizable.State;

/**
 * simple mass spring angular report tuple 
 */
public class MSAReportTuple {
	int id;
	char Type;
	String Localization_Phase;
	int RectNo;
	int avgHopLen;
	int hopToAnchor;
	int msJumpSize;
	int degree;
	int anchorDeg;
	int messageSent;
	double x, y, x_, y_, err;
}
