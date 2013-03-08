package reporter;

/**
 * aggregated mass spring angular report tuple
 */

// These algorithms are not performed separate it was impossible for far nodes
// and close to perform simultaneously . but if we assume it did. the result of
// their performance is as:
public class MSAAggReportTuple {
	int fileNo;//=
		int nodeCount;//
	int lostCount;//
	int r;//

	double anchorPercent;//=
	double avgDeg;//=

	int lpdMaxMemory;//=
	int dpdvMaxMemory;//

	// message
	int lpdTotalMessage;//=
	double lpdAvgMessage;//=
	int dpdvTotalMessage;//
	double dpdvAvgMessage;//
	int dvTotalMessage;//
	double dvAvgMessage;//
	int cogTotalMessage;//
	double cogAvgMessage;//
	int proximityTotalMessage;//
	double proximityAvgMessage;//
	// for our alg overhead
	int lpdTotalMessage_AnchorReception;//i
	double lpdAvgMessage_AnchorReception;//i
	int lpdTotalMessageSendInitial;//=
	double lpdAvgMessageSendInitial;//=
	int lpdTotalMessageMS;//=
	double lpdAvgMessageMS;//=
	int lpdTotalMessageAngular;//=
	double lpdAvgMessageAngular;//=

	int dpdvTotalMessage_AnchorReception;//
	double dpdvAvgMessage_AnchorReception;//
	int dpdvTotalMessageSendInitial;//
	double dpdvAvgMessageSendInitial;//
	int dpdvTotalMessageMS;//
	double dpdvAvgMessageMS;//
	int dpdvTotalMessageAngular;//
	double dpdvAvgMessageAngular;//

	// err
	double lpdAvgErr;//=
	double lpdMaxErr;//=
	double msLPDAvgErr;//=
	double msLPDMaxErr;//=
	double amsLPDAvgErr;//=// FROM THE START OF MS TO ITS END,
	double amsLPDMaxErr;//=

	public double dpdvAvgErr;//
	public double dpdvMaxErr;//
	double msDPDVAvgErr;//
	double msDPDVMaxErr;//
	double amsDPDVAvgErr;//
	double amsDPDVMaxErr;//

	public double dvAvgErr;//
	public double dvMaxErr;//

	public double cogAvgErr;//
	public double cogMaxErr;//

	public double proximityAvgErr;//
	public double proximityMaxErr;//

}
