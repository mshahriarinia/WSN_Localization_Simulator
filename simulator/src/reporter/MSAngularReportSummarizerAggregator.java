package reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import control.ZXControl;

public class MSAngularReportSummarizerAggregator {

	String title;

	public static String aggregateFileName = "Aggregated Tuples.txt";

	void aggregateAllMSAngularReportSummaries() {
		try {

			Scanner sc = new Scanner(new File(ZXControl.OUTPUT_DIRECTORY
			// + "Summary - not rounded.txt"));
					+ "Summary.txt"));

			// load tuples
			List<MSAAggReportTuple> listTuples = new ArrayList<MSAAggReportTuple>();

			title = sc.nextLine();

			while (sc.hasNextInt()) {
				listTuples.add(read(sc));
			}
			sc.close();

			// aggregate and remove aggregated tuples
			List<MSAAggReportTuple> listDeletableTuples = new ArrayList<MSAAggReportTuple>();

			for (int k = 0; k < 5; k++) {
				boolean isSomethingToDelete = true;
				// boolean oneMoreExecution = true;
				while (isSomethingToDelete) {

					isSomethingToDelete = false;

					int initialSize = listTuples.size();

					for (int i = 0; i < listTuples.size(); i++) {
						if (listTuples.get(i).fileNo == 2339)
							System.out.println();
					}

					MSAAggReportTuple msaAgg;

					for (int i = 0; i < initialSize; i++) {
						listDeletableTuples.clear();
						if (i < listTuples.size()) {
							msaAgg = listTuples.get(i);
							System.out.println(msaAgg.fileNo);
							if (msaAgg.fileNo == 2339)
								System.out.print("");

							for (int j = 0; j < listTuples.size(); j++) {
								if (listTuples.get(j).fileNo == 2339)
									System.out.println();
							}

							listDeletableTuples
									.addAll(aggregateAndGetDeletableTuples(
											listTuples, msaAgg));

							listTuples.removeAll(listDeletableTuples);
							if (listDeletableTuples.size() > 1)
								isSomethingToDelete = true;
						}
					}
					// if(isSomethingToDelete == false)
					// oneMoreExecution
				}
			}

			// print results
			PrintStream ps = new PrintStream(new File(
					ZXControl.OUTPUT_DIRECTORY + aggregateFileName));
			ps.println(title);
			for (MSAAggReportTuple msaAgg : listTuples) {
				MSAngularReportSummarizer.print(ps, msaAgg, false);
			}
			ps.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private List<MSAAggReportTuple> aggregateAndGetDeletableTuples(
			List<MSAAggReportTuple> listTuples, MSAAggReportTuple msaAgg) {

		List<MSAAggReportTuple> listDeletableTuples = new ArrayList<MSAAggReportTuple>();

		for (MSAAggReportTuple tempMSAAgg : listTuples) {
			if (msaAgg.anchorPercent == tempMSAAgg.anchorPercent
					&& msaAgg.avgDeg == tempMSAAgg.avgDeg) {
				listDeletableTuples.add(tempMSAAgg);
			}
		}

		listTuples.add(mean(listDeletableTuples));

		return listDeletableTuples;
	}

	MSAAggReportTuple mean(List<MSAAggReportTuple> listTuples) {
		MSAAggReportTuple msaAgg = new MSAAggReportTuple();
		for (MSAAggReportTuple tempMSAAgg : listTuples) {

			msaAgg.anchorPercent += tempMSAAgg.anchorPercent;

			msaAgg.avgDeg += tempMSAAgg.avgDeg;

			msaAgg.lpdMaxMemory = Math.max(msaAgg.lpdMaxMemory,
					tempMSAAgg.lpdMaxMemory);

			msaAgg.dpdvMaxMemory = Math.max(msaAgg.dpdvMaxMemory,
					tempMSAAgg.dpdvMaxMemory);

			msaAgg.lpdTotalMessage += tempMSAAgg.lpdTotalMessage;

			msaAgg.lpdAvgMessage += tempMSAAgg.lpdAvgMessage;

			msaAgg.dpdvTotalMessage += tempMSAAgg.dpdvTotalMessage;

			msaAgg.dpdvAvgMessage += tempMSAAgg.dpdvAvgMessage;

			msaAgg.dvTotalMessage += tempMSAAgg.dvTotalMessage;

			msaAgg.dvAvgMessage += tempMSAAgg.dvAvgMessage;

			msaAgg.cogTotalMessage += tempMSAAgg.cogTotalMessage;

			msaAgg.cogAvgMessage += tempMSAAgg.cogAvgMessage;

			msaAgg.proximityTotalMessage += tempMSAAgg.proximityTotalMessage;

			msaAgg.proximityAvgMessage += tempMSAAgg.proximityAvgMessage;

			msaAgg.lpdTotalMessage_AnchorReception += tempMSAAgg.lpdTotalMessage_AnchorReception;

			msaAgg.lpdAvgMessage_AnchorReception += tempMSAAgg.lpdAvgMessage_AnchorReception;

			msaAgg.lpdTotalMessageSendInitial += tempMSAAgg.lpdTotalMessageSendInitial;

			msaAgg.lpdAvgMessageSendInitial += tempMSAAgg.lpdAvgMessageSendInitial;

			msaAgg.lpdTotalMessageMS += tempMSAAgg.lpdTotalMessageMS;

			msaAgg.lpdAvgMessageMS += tempMSAAgg.lpdAvgMessageMS;

			msaAgg.lpdTotalMessageAngular += tempMSAAgg.lpdTotalMessageAngular;

			msaAgg.lpdAvgMessageAngular += tempMSAAgg.lpdAvgMessageAngular;

			//

			msaAgg.dpdvTotalMessage_AnchorReception += tempMSAAgg.dpdvTotalMessage_AnchorReception;

			msaAgg.dpdvAvgMessage_AnchorReception += tempMSAAgg.dpdvAvgMessage_AnchorReception;

			msaAgg.dpdvTotalMessageSendInitial += tempMSAAgg.dpdvTotalMessageSendInitial;

			msaAgg.dpdvAvgMessageSendInitial += tempMSAAgg.dpdvAvgMessageSendInitial;

			msaAgg.dpdvTotalMessageMS += tempMSAAgg.dpdvTotalMessageMS;

			msaAgg.dpdvAvgMessageMS += tempMSAAgg.dpdvAvgMessageMS;

			msaAgg.dpdvTotalMessageAngular += tempMSAAgg.dpdvTotalMessageAngular;

			msaAgg.dpdvAvgMessageAngular += tempMSAAgg.dpdvAvgMessageAngular;

			//

			msaAgg.lpdAvgErr += tempMSAAgg.lpdAvgErr;

			msaAgg.lpdMaxErr = Math.max(msaAgg.lpdMaxErr, tempMSAAgg.lpdMaxErr);

			msaAgg.msLPDAvgErr += tempMSAAgg.msLPDAvgErr;

			msaAgg.msLPDMaxErr = Math.max(msaAgg.msLPDMaxErr,
					tempMSAAgg.msLPDMaxErr);

			msaAgg.amsLPDAvgErr += tempMSAAgg.amsLPDAvgErr;

			msaAgg.amsLPDMaxErr = Math.max(msaAgg.amsLPDMaxErr,
					tempMSAAgg.amsLPDMaxErr);

			//

			msaAgg.dpdvAvgErr += tempMSAAgg.dpdvAvgErr;

			msaAgg.dpdvMaxErr = Math.max(msaAgg.dpdvMaxErr,
					tempMSAAgg.dpdvMaxErr);

			msaAgg.msDPDVAvgErr += tempMSAAgg.msDPDVAvgErr;

			msaAgg.msDPDVMaxErr = Math.max(msaAgg.msDPDVMaxErr,
					tempMSAAgg.msDPDVMaxErr);

			msaAgg.amsDPDVAvgErr += tempMSAAgg.amsDPDVAvgErr;

			msaAgg.amsDPDVMaxErr = Math.max(msaAgg.amsDPDVMaxErr,
					tempMSAAgg.amsDPDVMaxErr);

			//

			msaAgg.dvAvgErr += tempMSAAgg.dvAvgErr;

			msaAgg.dvMaxErr = Math.max(msaAgg.dvMaxErr, tempMSAAgg.dvMaxErr);

			//

			msaAgg.cogAvgErr += tempMSAAgg.cogAvgErr;

			msaAgg.cogMaxErr = Math.max(msaAgg.cogMaxErr, tempMSAAgg.cogMaxErr);

			//		

			msaAgg.proximityAvgErr += tempMSAAgg.proximityAvgErr;

			msaAgg.proximityMaxErr = Math.max(msaAgg.proximityMaxErr,
					tempMSAAgg.proximityMaxErr);

		}

		double doubleCount = listTuples.size() + 0.0;

		msaAgg.anchorPercent /= doubleCount;

		msaAgg.avgDeg /= doubleCount;

		// msaAgg.lpdMaxMemory = Math.max(msaAgg.lpdMaxMemory,
		// tempMSAAgg.lpdMaxMemory);

		// msaAgg.dpdvMaxMemory = Math.max(msaAgg.dpdvMaxMemory,
		// tempMSAAgg.dpdvMaxMemory);

		msaAgg.lpdTotalMessage /= doubleCount;

		msaAgg.lpdAvgMessage /= doubleCount;

		msaAgg.dpdvTotalMessage /= doubleCount;

		msaAgg.dpdvAvgMessage /= doubleCount;

		msaAgg.dvTotalMessage /= doubleCount;

		msaAgg.dvAvgMessage /= doubleCount;

		msaAgg.cogTotalMessage /= doubleCount;

		msaAgg.cogAvgMessage /= doubleCount;

		msaAgg.proximityTotalMessage /= doubleCount;

		msaAgg.proximityAvgMessage /= doubleCount;

		msaAgg.lpdTotalMessage_AnchorReception /= doubleCount;

		msaAgg.lpdAvgMessage_AnchorReception /= doubleCount;

		msaAgg.lpdTotalMessageSendInitial /= doubleCount;

		msaAgg.lpdAvgMessageSendInitial /= doubleCount;

		msaAgg.lpdTotalMessageMS /= doubleCount;

		msaAgg.lpdAvgMessageMS /= doubleCount;

		msaAgg.lpdTotalMessageAngular /= doubleCount;

		msaAgg.lpdAvgMessageAngular /= doubleCount;

		//

		msaAgg.dpdvTotalMessage_AnchorReception /= doubleCount;

		msaAgg.dpdvAvgMessage_AnchorReception /= doubleCount;

		msaAgg.dpdvTotalMessageSendInitial /= doubleCount;

		msaAgg.dpdvAvgMessageSendInitial /= doubleCount;

		msaAgg.dpdvTotalMessageMS /= doubleCount;

		msaAgg.dpdvAvgMessageMS /= doubleCount;

		msaAgg.dpdvTotalMessageAngular /= doubleCount;

		msaAgg.dpdvAvgMessageAngular /= doubleCount;

		//

		msaAgg.lpdAvgErr /= doubleCount;

		// msaAgg.lpdMaxErr = Math.max(msaAgg.lpdMaxErr, tempMSAAgg.lpdMaxErr);

		msaAgg.msLPDAvgErr /= doubleCount;

		// msaAgg.msLPDMaxErr = Math.max(msaAgg.msLPDMaxErr,
		// tempMSAAgg.msLPDMaxErr);

		msaAgg.amsLPDAvgErr /= doubleCount;

		// msaAgg.amsLPDMaxErr = Math.max(msaAgg.amsLPDMaxErr,
		// tempMSAAgg.amsLPDMaxErr);

		//

		msaAgg.dpdvAvgErr /= doubleCount;

		// msaAgg.dpdvMaxErr = Math.max(msaAgg.dpdvMaxErr,
		// tempMSAAgg.dpdvMaxErr);

		msaAgg.msDPDVAvgErr /= doubleCount;

		// msaAgg.msDPDVMaxErr = Math.max(msaAgg.msDPDVMaxErr,
		// tempMSAAgg.msDPDVMaxErr);

		msaAgg.amsDPDVAvgErr /= doubleCount;

		// msaAgg.amsDPDVMaxErr = Math.max(msaAgg.amsDPDVMaxErr,
		// tempMSAAgg.amsDPDVMaxErr);

		//

		msaAgg.dvAvgErr /= doubleCount;

		// msaAgg.dvMaxErr = Math.max(msaAgg.dvMaxErr, tempMSAAgg.dvMaxErr);

		//

		msaAgg.cogAvgErr /= doubleCount;

		// msaAgg.cogMaxErr = Math.max(msaAgg.cogMaxErr, tempMSAAgg.cogMaxErr);

		//		

		msaAgg.proximityAvgErr /= doubleCount;

		// msaAgg.proximityMaxErr = Math.max(msaAgg.proximityMaxErr,
		// tempMSAAgg.proximityMaxErr);

		return msaAgg;
	}

	public static MSAAggReportTuple read(Scanner sc) {
		MSAAggReportTuple msaAgg = new MSAAggReportTuple();

		msaAgg.fileNo = (int)Math.floor((sc.nextDouble()));;

		msaAgg.nodeCount = (int)Math.floor((sc.nextDouble()));;

		msaAgg.lostCount = (int)Math.floor((sc.nextDouble()));;

		msaAgg.anchorPercent = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.avgDeg = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.lpdMaxMemory = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.dpdvMaxMemory = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.lpdTotalMessage = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.lpdAvgMessage = sc.nextDouble();//

		msaAgg.dpdvTotalMessage = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.dpdvAvgMessage = sc.nextDouble();//

		msaAgg.dvTotalMessage = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.dvAvgMessage = sc.nextDouble();//

		msaAgg.cogTotalMessage = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.cogAvgMessage = sc.nextDouble();//

		msaAgg.proximityTotalMessage = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.proximityAvgMessage = sc.nextDouble();//

		msaAgg.lpdTotalMessage_AnchorReception = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.lpdAvgMessage_AnchorReception = sc.nextDouble();//

		msaAgg.lpdTotalMessageSendInitial = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.lpdAvgMessageSendInitial = sc.nextDouble();//

		msaAgg.lpdTotalMessageMS = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.lpdAvgMessageMS = sc.nextDouble();//

		msaAgg.lpdTotalMessageAngular = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.lpdAvgMessageAngular = sc.nextDouble();//

		//

		msaAgg.dpdvTotalMessage_AnchorReception = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.dpdvAvgMessage_AnchorReception = sc.nextDouble();//

		msaAgg.dpdvTotalMessageSendInitial = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.dpdvAvgMessageSendInitial = sc.nextDouble();//

		msaAgg.dpdvTotalMessageMS = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.dpdvAvgMessageMS = sc.nextDouble();//

		msaAgg.dpdvTotalMessageAngular = (int)Math.floor((sc.nextDouble()));;//

		msaAgg.dpdvAvgMessageAngular = sc.nextDouble();//

		//

		msaAgg.lpdAvgErr = sc.nextDouble();//

		msaAgg.lpdMaxErr = sc.nextDouble();//

		msaAgg.msLPDAvgErr = sc.nextDouble();//

		msaAgg.msLPDMaxErr = sc.nextDouble();//

		msaAgg.amsLPDAvgErr = sc.nextDouble();//

		msaAgg.amsLPDMaxErr = sc.nextDouble();//

		//

		msaAgg.dpdvAvgErr = sc.nextDouble();//

		msaAgg.dpdvMaxErr = sc.nextDouble();//

		msaAgg.msDPDVAvgErr = sc.nextDouble();//

		msaAgg.msDPDVMaxErr = sc.nextDouble();//

		msaAgg.amsDPDVAvgErr = sc.nextDouble();//

		msaAgg.amsDPDVMaxErr = sc.nextDouble();//

		//

		msaAgg.dvAvgErr = sc.nextDouble();

		msaAgg.dvMaxErr = sc.nextDouble();

		//

		msaAgg.cogAvgErr = sc.nextDouble();

		msaAgg.cogMaxErr = sc.nextDouble();

		//		

		msaAgg.proximityAvgErr = sc.nextDouble();

		msaAgg.proximityMaxErr = sc.nextDouble();
		sc.nextLine();
		return msaAgg;
	}

	public static void main(String[] args) {
		(new MSAngularReportSummarizerAggregator())
				.aggregateAllMSAngularReportSummaries();
	}

}
