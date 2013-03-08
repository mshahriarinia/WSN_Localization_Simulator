package reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import simulator.localizable.State;
import control.ZXControl;
import control.ZXControl.ETYPE;

public class MSAngularReportSummarizer {

	public static String SUMMARY_FILE_NAME = "Summary.txt";

	static String ANGULAR = "ANGLE_REFINING";
	// State
	// .toString(State.LocalizationPhase.ANGULAR_REFINING);

	static String MASS_SPRING = "MS_REFINING";

	static String SEND_INITIAL = State
			.toString(State.LocalizationPhase.SEND_INITIAL_POSE);

	static String path = ZXControl.OUTPUT_DIRECTORY +
	//1005 + "/";
	"runs/";

	public static void summarize() {
		PrintStream ps = null;
		File file = null;
		try {
			ps = new PrintStream(new File(ZXControl.OUTPUT_DIRECTORY
					+ SUMMARY_FILE_NAME));

			file = new File(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		File[] listFiles = file.getAbsoluteFile().listFiles();

		ps.print("fileNo");
		ps.print("\t");
		ps.print("nodeCount");
		ps.print("\t");
		ps.print("lostCount");
		ps.print("\t");
		ps.print("anchorPercent");
		ps.print("\t");
		ps.print("avgDeg");
		ps.print("\t");

		ps.print("lpdMaxMemory");
		ps.print("\t");
		ps.print("dpdvMaxMemory");
		ps.print("\t");

		ps.print("lpdTotalMessage");
		ps.print("\t");
		ps.print("lpdAvgMessage");
		ps.print("\t");
		ps.print("dpdvTotalMessage");
		ps.print("\t");
		ps.print("dpdvAvgMessage");
		ps.print("\t");
		ps.print("dvTotalMessage");
		ps.print("\t");
		ps.print("dvAvgMessage");
		ps.print("\t");
		ps.print("cogTotalMessage");
		ps.print("\t");
		ps.print("cogAvgMessage");
		ps.print("\t");
		ps.print("proximityTotalMessage");
		ps.print("\t");
		ps.print("proximityAvgMessage");
		ps.print("\t");
		ps.print("lpdTotalMessage_AnchorReception");
		ps.print("\t");
		ps.print("lpdAvgMessage_AnchorReception");
		ps.print("\t");
		ps.print("lpdTotalMessageSendInitial");
		ps.print("\t");
		ps.print("lpdAvgMessageSendInitial");
		ps.print("\t");
		ps.print("lpdTotalMessageMS");
		ps.print("\t");
		ps.print("lpdAvgMessageMS");
		ps.print("\t");
		ps.print("lpdTotalMessageAngular");
		ps.print("\t");
		ps.print("lpdAvgMessageAngular");
		ps.print("\t");

		ps.print("dpdvTotalMessage_AnchorReception");
		ps.print("\t");
		ps.print("dpdvAvgMessage_AnchorReception");
		ps.print("\t");
		ps.print("dpdvTotalMessageSendInitial");
		ps.print("\t");
		ps.print("dpdvAvgMessageSendInitial");
		ps.print("\t");
		ps.print("dpdvTotalMessageMS");
		ps.print("\t");
		ps.print("dpdvAvgMessageMS");
		ps.print("\t");
		ps.print("dpdvTotalMessageAngular");
		ps.print("\t");
		ps.print("dpdvAvgMessageAngular");
		ps.print("\t");

		ps.print("lpdAvgErr");
		ps.print("\t");
		ps.print("lpdMaxErr");
		ps.print("\t");
		ps.print("msLPDAvgErr");
		ps.print("\t");
		ps.print("msLPDMaxErr");
		ps.print("\t");
		ps.print("amsLPDAvgErr");
		ps.print("\t");
		ps.print("amsLPDMaxErr");
		ps.print("\t");

		ps.print("dpdvAvgErr");
		ps.print("\t");
		ps.print("dpdvMaxErr");
		ps.print("\t");
		ps.print("msDPDVAvgErr");
		ps.print("\t");
		ps.print("msDPDVMaxErr");
		ps.print("\t");
		ps.print("amsDPDVAvgErr");
		ps.print("\t");
		ps.print("amsDPDVMaxErr");
		ps.print("\t");

		ps.print("dvAvgErr");
		ps.print("\t");
		ps.print("dvMaxErr");
		ps.print("\t");

		ps.print("cogAvgErr");
		ps.print("\t");
		ps.print("cogMaxErr");
		ps.print("\t");

		ps.print("proximityAvgErr");
		ps.print("\t");
		ps.print("proximityMaxErr");
		ps.println();

		for (int i = 0; i < listFiles.length; i++) {

			System.out.println(listFiles[i].getName());

			if (listFiles[i].getName().contains("Network")) {
				String prefix = listFiles[i].getName().split(" ")[0];

				try {
					MSAAggReportTuple msaAgg = getSummary(prefix);
					print(ps, msaAgg, true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

	}

	public static void print(PrintStream ps, MSAAggReportTuple msaAgg,
			boolean printPercent) {
		ps.print(msaAgg.fileNo);
		ps.print("\t");
		ps.print(msaAgg.nodeCount);
		ps.print("\t");
		ps.print(msaAgg.lostCount);
		ps.print("\t");
		ps.print(myRound(msaAgg.anchorPercent));
		ps.print("\t");
		ps.print(myRound(msaAgg.avgDeg));
		ps.print("\t");

		ps.print(msaAgg.lpdMaxMemory);
		ps.print("\t");
		ps.print(msaAgg.dpdvMaxMemory);
		ps.print("\t");

		ps.print(msaAgg.lpdTotalMessage);
		ps.print("\t");
		ps.print(msaAgg.lpdAvgMessage);
		ps.print("\t");
		ps.print(msaAgg.dpdvTotalMessage);
		ps.print("\t");
		ps.print(msaAgg.dpdvAvgMessage);
		ps.print("\t");
		ps.print(msaAgg.dvTotalMessage);
		ps.print("\t");
		ps.print(msaAgg.dvAvgMessage);
		ps.print("\t");
		ps.print(msaAgg.cogTotalMessage);
		ps.print("\t");
		ps.print(msaAgg.cogAvgMessage);
		ps.print("\t");
		ps.print(msaAgg.proximityTotalMessage);
		ps.print("\t");
		ps.print(msaAgg.proximityAvgMessage);
		ps.print("\t");
		ps.print(msaAgg.lpdTotalMessage_AnchorReception);
		ps.print("\t");
		ps.print(msaAgg.lpdAvgMessage_AnchorReception);
		ps.print("\t");
		ps.print(msaAgg.lpdTotalMessageSendInitial);
		ps.print("\t");
		ps.print(msaAgg.lpdAvgMessageSendInitial);
		ps.print("\t");
		ps.print(msaAgg.lpdTotalMessageMS);
		ps.print("\t");
		ps.print(msaAgg.lpdAvgMessageMS);
		ps.print("\t");
		ps.print(msaAgg.lpdTotalMessageAngular);
		ps.print("\t");
		ps.print(msaAgg.lpdAvgMessageAngular);
		ps.print("\t");

		ps.print(msaAgg.dpdvTotalMessage_AnchorReception);
		ps.print("\t");
		ps.print(msaAgg.dpdvAvgMessage_AnchorReception);
		ps.print("\t");
		ps.print(msaAgg.dpdvTotalMessageSendInitial);
		ps.print("\t");
		ps.print(msaAgg.dpdvAvgMessageSendInitial);
		ps.print("\t");
		ps.print(msaAgg.dpdvTotalMessageMS);
		ps.print("\t");
		ps.print(msaAgg.dpdvAvgMessageMS);
		ps.print("\t");
		ps.print(msaAgg.dpdvTotalMessageAngular);
		ps.print("\t");
		ps.print(msaAgg.dpdvAvgMessageAngular);
		ps.print("\t");

		double devider = (printPercent ? 100 / (msaAgg.r + 0.0) : 1);

		ps.print(msaAgg.lpdAvgErr / devider);
		ps.print("\t");
		ps.print(msaAgg.lpdMaxErr / devider);
		ps.print("\t");
		ps.print(msaAgg.msLPDAvgErr / devider);
		ps.print("\t");
		ps.print(msaAgg.msLPDMaxErr / devider);
		ps.print("\t");
		ps.print(msaAgg.amsLPDAvgErr / devider);
		ps.print("\t");
		ps.print(msaAgg.amsLPDMaxErr / devider);
		ps.print("\t");

		ps.print(msaAgg.dpdvAvgErr / devider);
		ps.print("\t");
		ps.print(msaAgg.dpdvMaxErr / devider);
		ps.print("\t");
		ps.print(msaAgg.msDPDVAvgErr / devider);
		ps.print("\t");
		ps.print(msaAgg.msDPDVMaxErr / devider);
		ps.print("\t");
		ps.print(msaAgg.amsDPDVAvgErr / devider);
		ps.print("\t");
		ps.print(msaAgg.amsDPDVMaxErr / devider);
		ps.print("\t");

		ps.print(msaAgg.dvAvgErr / devider);
		ps.print("\t");
		ps.print(msaAgg.dvMaxErr / devider);
		ps.print("\t");

		ps.print(msaAgg.cogAvgErr / devider);
		ps.print("\t");
		ps.print(msaAgg.cogMaxErr / devider);
		ps.print("\t");

		ps.print(msaAgg.proximityAvgErr / devider);
		ps.print("\t");
		ps.print(msaAgg.proximityMaxErr / devider);
		ps.println();

	}

	private static MSAAggReportTuple getSummary(String prefix) {

		File reportFile;
		Scanner sc;
		MSAAggReportTuple msaAgg = new MSAAggReportTuple();
		msaAgg.fileNo = Integer.parseInt(prefix);
		try {
			setAnchorPercent_AvgDeg_NodeCount_LostCount(prefix, msaAgg);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		List<MSAReportTuple[]> listAllCycles;

		double[] sendInitialErr;
		int[] sendInitialMessageCount;

		double[] msErr;
		int[] msMessageCount;

		double[] angularErr;
		int[] angularMessageCount;

		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////
		// ///////////////////////////////////////////////////// lpd
		{
			reportFile = new File(path + prefix + " - "
					+ ZXControl.getTitle(ETYPE.LPD) + ".txt");
			sc = null;
			try {
				sc = new Scanner(reportFile);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

		//	System.out.println("working lpd" + prefix);

			listAllCycles = getListAllCycles(sc, msaAgg.nodeCount);

			for (int n = 0; n < msaAgg.nodeCount; n++) {
				// total message
				msaAgg.lpdTotalMessage += listAllCycles.get(listAllCycles
						.size() - 1)[n].messageSent;

				// max memory
				if (msaAgg.lpdMaxMemory < listAllCycles.get(listAllCycles
						.size() - 1)[n].RectNo)
					msaAgg.lpdMaxMemory = listAllCycles.get(listAllCycles
							.size() - 1)[n].RectNo;
			}// averaged over all nodes
			msaAgg.lpdAvgMessage = msaAgg.lpdTotalMessage
					/ (msaAgg.nodeCount + 0.0);
			//

			// come from last cycle to first and get the best results of each
			// phase

			sendInitialErr = new double[msaAgg.nodeCount];
			sendInitialMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(sendInitialErr, -1);

			msErr = new double[msaAgg.nodeCount];
			msMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(msErr, -1);

			angularErr = new double[msaAgg.nodeCount];
			angularMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(angularErr, -1);

			// init best errors
			for (int c = listAllCycles.size() - 1; c > 10; c--) {
				for (int n = 0; n < msaAgg.nodeCount; n++) {

			
						listAllCycles.get(c)[n].Localization_Phase
								.equals(ANGULAR);
					

					if (listAllCycles.get(c)[n].Localization_Phase
							.equals(ANGULAR)) {

						angularMessageCount[n] += getMessageCount(
								listAllCycles, c, n);

						if (angularErr[n] == -1)
							angularErr[n] = listAllCycles.get(c)[n].err;

					} else if (listAllCycles.get(c)[n].Localization_Phase
							.equals(MASS_SPRING)) {

						int count = getMessageCount(listAllCycles, c, n);
						if (count != 0)
							System.out.print("");
						msMessageCount[n] += count;

						if (msErr[n] == -1)
							msErr[n] = listAllCycles.get(c)[n].err;

					} else if (listAllCycles.get(c)[n].Localization_Phase
							.equals(SEND_INITIAL)) {

						sendInitialMessageCount[n] += getMessageCount(
								listAllCycles, c, n);

						if (sendInitialErr[n] == -1)
							sendInitialErr[n] = listAllCycles.get(c)[n].err;
					}
				}
			}
			msaAgg.lpdTotalMessageAngular = sum(angularMessageCount);
			msaAgg.lpdTotalMessageMS = sum(msMessageCount);
			msaAgg.lpdTotalMessageSendInitial = sum(sendInitialMessageCount);

			msaAgg.lpdAvgMessageAngular = msaAgg.lpdTotalMessageAngular
					/ (msaAgg.lostCount + 0.0);
			msaAgg.lpdAvgMessageMS = msaAgg.lpdTotalMessageMS
					/ (msaAgg.lostCount + 0.0);
			msaAgg.lpdAvgMessageSendInitial = msaAgg.lpdTotalMessageSendInitial
					/ (msaAgg.lostCount + 0.0);

			msaAgg.lpdAvgErr = average(sendInitialErr);
			msaAgg.msLPDAvgErr = average(msErr);
			msaAgg.amsLPDAvgErr = average(angularErr);

			msaAgg.lpdMaxErr = max(sendInitialErr);
			msaAgg.msLPDMaxErr = max(msErr);
			msaAgg.amsLPDMaxErr = max(angularErr);
		}

		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////
		// ///////////////////////////////////////////////////// dpdv

		{
			reportFile = new File(path + prefix + " - "
					+ ZXControl.getTitle(ETYPE.DISCRETE_DV) + ".txt");
			try {
				sc = new Scanner(reportFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		//	System.out.println("working dpdv" + prefix);

			listAllCycles = getListAllCycles(sc, msaAgg.nodeCount);

			for (int n = 0; n < msaAgg.nodeCount; n++) {
				// total message
				msaAgg.dpdvTotalMessage += listAllCycles.get(listAllCycles
						.size() - 1)[n].messageSent;

				// max memory
				if (msaAgg.dpdvMaxMemory < listAllCycles.get(listAllCycles
						.size() - 1)[n].RectNo)
					msaAgg.dpdvMaxMemory = listAllCycles.get(listAllCycles
							.size() - 1)[n].RectNo;
			}// averaged over all nodes
			msaAgg.dpdvAvgMessage = msaAgg.dpdvTotalMessage
					/ (msaAgg.nodeCount + 0.0);
			//

			// come from last cycle to first and get the best results of each
			// phase

			sendInitialErr = new double[msaAgg.nodeCount];
			sendInitialMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(sendInitialErr, -1);

			msErr = new double[msaAgg.nodeCount];
			msMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(msErr, -1);

			angularErr = new double[msaAgg.nodeCount];
			angularMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(angularErr, -1);

			// init best errors
			for (int c = listAllCycles.size() - 1; c > 10; c--) {
				for (int n = 0; n < msaAgg.nodeCount; n++) {
					if (listAllCycles.get(c)[n].Localization_Phase
							.equals(ANGULAR)) {

						angularMessageCount[n] += getMessageCount(
								listAllCycles, c, n);

						if (angularErr[n] == -1)
							angularErr[n] = listAllCycles.get(c)[n].err;

					} else if (listAllCycles.get(c)[n].Localization_Phase
							.equals(MASS_SPRING)) {

						msMessageCount[n] += getMessageCount(listAllCycles, c,
								n);

						if (msErr[n] == -1)
							msErr[n] = listAllCycles.get(c)[n].err;

					} else if (listAllCycles.get(c)[n].Localization_Phase
							.equals(SEND_INITIAL)) {

						sendInitialMessageCount[n] += getMessageCount(
								listAllCycles, c, n);

						if (sendInitialErr[n] == -1)
							sendInitialErr[n] = listAllCycles.get(c)[n].err;
					}
				}
			}
			msaAgg.dpdvTotalMessageAngular = sum(angularMessageCount);
			msaAgg.dpdvTotalMessageMS = sum(msMessageCount);
			msaAgg.dpdvTotalMessageSendInitial = sum(sendInitialMessageCount);

			msaAgg.dpdvAvgMessageAngular = msaAgg.dpdvTotalMessageAngular
					/ (msaAgg.lostCount + 0.0);
			msaAgg.dpdvAvgMessageMS = msaAgg.dpdvTotalMessageMS
					/ (msaAgg.lostCount + 0.0);
			msaAgg.dpdvAvgMessageSendInitial = msaAgg.dpdvTotalMessageSendInitial
					/ (msaAgg.lostCount + 0.0);

			msaAgg.dpdvAvgErr = average(sendInitialErr);
			msaAgg.msDPDVAvgErr = average(msErr);
			msaAgg.amsDPDVAvgErr = average(angularErr);

			msaAgg.dpdvMaxErr = max(sendInitialErr);
			msaAgg.msDPDVMaxErr = max(msErr);
			msaAgg.amsDPDVMaxErr = max(angularErr);
		}

		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////
		// ///////////////////////////////////////////////////// dv

		{

			reportFile = new File(path + prefix + " - "
					+ ZXControl.getTitle(ETYPE.DV) + ".txt");
			try {
				sc = new Scanner(reportFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			//System.out.println("working dv" + prefix);

			listAllCycles = getListAllCycles(sc, msaAgg.nodeCount);

			for (int n = 0; n < msaAgg.nodeCount; n++) {
				// total message
				msaAgg.dvTotalMessage += listAllCycles
						.get(listAllCycles.size() - 1)[n].messageSent;

				// max memory
				// if (msaAgg.dvMaxMemory < listAllCycles.get(listAllCycles
				// .size() - 1)[n].RectNo)
				// msaAgg.dvMaxMemory = listAllCycles.get(listAllCycles
				// .size() - 1)[n].RectNo;
			}// averaged over all nodes
			msaAgg.dvAvgMessage = msaAgg.dvTotalMessage
					/ (msaAgg.nodeCount + 0.0);
			//

			// come from last cycle to first and get the best results of each
			// phase

			sendInitialErr = new double[msaAgg.nodeCount];
			sendInitialMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(sendInitialErr, -1);

			msErr = new double[msaAgg.nodeCount];
			msMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(msErr, -1);

			angularErr = new double[msaAgg.nodeCount];
			angularMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(angularErr, -1);

			// init best errors
			for (int c = listAllCycles.size() - 1; c > 10; c--) {
				for (int n = 0; n < msaAgg.nodeCount; n++) {
					if (listAllCycles.get(c)[n].Localization_Phase
							.equals(ANGULAR)) {

						angularMessageCount[n] += getMessageCount(
								listAllCycles, c, n);

						if (angularErr[n] == -1)
							angularErr[n] = listAllCycles.get(c)[n].err;

					} else if (listAllCycles.get(c)[n].Localization_Phase
							.equals(MASS_SPRING)) {

						msMessageCount[n] += getMessageCount(listAllCycles, c,
								n);

						if (msErr[n] == -1)
							msErr[n] = listAllCycles.get(c)[n].err;

					} else if (listAllCycles.get(c)[n].Localization_Phase
							.equals(SEND_INITIAL)) {

						sendInitialMessageCount[n] += getMessageCount(
								listAllCycles, c, n);

						if (sendInitialErr[n] == -1)
							sendInitialErr[n] = listAllCycles.get(c)[n].err;
					}
				}
			}
			msaAgg.dvAvgErr = average(sendInitialErr);
			msaAgg.dvMaxErr = max(sendInitialErr);

		}

		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////
		// ///////////////////////////////////////////////////// COG

		{

			reportFile = new File(path + prefix + " - "
					+ ZXControl.getTitle(ETYPE.CENTROID) + ".txt");
			try {
				sc = new Scanner(reportFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			//System.out.println("working cog" + prefix);

			listAllCycles = getListAllCycles(sc, msaAgg.nodeCount);

			for (int n = 0; n < msaAgg.nodeCount; n++) {
				// total message
				msaAgg.cogTotalMessage += listAllCycles.get(listAllCycles
						.size() - 1)[n].messageSent;

				// max memory
				// if (msaAgg.cogMaxMemory < listAllCycles.get(listAllCycles
				// .size() - 1)[n].RectNo)
				// msaAgg.cogMaxMemory = listAllCycles.get(listAllCycles
				// .size() - 1)[n].RectNo;
			}// averaged over all nodes
			msaAgg.cogAvgMessage = msaAgg.cogTotalMessage
					/ (msaAgg.nodeCount + 0.0);
			//

			// come from last cycle to first and get the best results of each
			// phase

			sendInitialErr = new double[msaAgg.nodeCount];
			sendInitialMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(sendInitialErr, -1);

			msErr = new double[msaAgg.nodeCount];
			msMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(msErr, -1);

			angularErr = new double[msaAgg.nodeCount];
			angularMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(angularErr, -1);

			// init best errors
			for (int c = listAllCycles.size() - 1; c > 10; c--) {
				for (int n = 0; n < msaAgg.nodeCount; n++) {
					if (listAllCycles.get(c)[n].Localization_Phase
							.equals(ANGULAR)) {

						angularMessageCount[n] += getMessageCount(
								listAllCycles, c, n);

						if (angularErr[n] == -1)
							angularErr[n] = listAllCycles.get(c)[n].err;

					} else if (listAllCycles.get(c)[n].Localization_Phase
							.equals(MASS_SPRING)) {

						msMessageCount[n] += getMessageCount(listAllCycles, c,
								n);

						if (msErr[n] == -1)
							msErr[n] = listAllCycles.get(c)[n].err;

					} else if (listAllCycles.get(c)[n].Localization_Phase
							.equals(SEND_INITIAL)) {

						sendInitialMessageCount[n] += getMessageCount(
								listAllCycles, c, n);

						if (sendInitialErr[n] == -1)
							sendInitialErr[n] = listAllCycles.get(c)[n].err;
					}
				}
			}
			msaAgg.cogAvgErr = average(sendInitialErr);
			msaAgg.cogMaxErr = max(sendInitialErr);

		}

		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////
		// ///////////////////////////////////////////////////// proximity

		{

			reportFile = new File(path + prefix + " - "
					+ ZXControl.getTitle(ETYPE.PROXIMITY) + ".txt");
			try {
				sc = new Scanner(reportFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			//System.out.println("working proximity" + prefix);

			listAllCycles = getListAllCycles(sc, msaAgg.nodeCount);

			for (int n = 0; n < msaAgg.nodeCount; n++) {
				// total message
				msaAgg.proximityTotalMessage += listAllCycles.get(listAllCycles
						.size() - 1)[n].messageSent;

				// max memory
				// if (msaAgg.proximityMaxMemory <
				// listAllCycles.get(listAllCycles
				// .size() - 1)[n].RectNo)
				// msaAgg.proximityMaxMemory = listAllCycles.get(listAllCycles
				// .size() - 1)[n].RectNo;
			}// averaged over all nodes
			msaAgg.proximityAvgMessage = msaAgg.proximityTotalMessage
					/ (msaAgg.nodeCount + 0.0);
			//

			// come from last cycle to first and get the best results of each
			// phase

			sendInitialErr = new double[msaAgg.nodeCount];
			sendInitialMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(sendInitialErr, -1);

			msErr = new double[msaAgg.nodeCount];
			msMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(msErr, -1);

			angularErr = new double[msaAgg.nodeCount];
			angularMessageCount = new int[msaAgg.nodeCount];
			Arrays.fill(angularErr, -1);

			// init best errors
			for (int c = listAllCycles.size() - 1; c > 10; c--) {
				for (int n = 0; n < msaAgg.nodeCount; n++) {
					if (listAllCycles.get(c)[n].Localization_Phase
							.equals(ANGULAR)) {

						angularMessageCount[n] += getMessageCount(
								listAllCycles, c, n);

						if (angularErr[n] == -1)
							angularErr[n] = listAllCycles.get(c)[n].err;

					} else if (listAllCycles.get(c)[n].Localization_Phase
							.equals(MASS_SPRING)) {

						msMessageCount[n] += getMessageCount(listAllCycles, c,
								n);

						if (msErr[n] == -1)
							msErr[n] = listAllCycles.get(c)[n].err;

					} else if (listAllCycles.get(c)[n].Localization_Phase
							.equals(SEND_INITIAL)) {

						sendInitialMessageCount[n] += getMessageCount(
								listAllCycles, c, n);

						if (sendInitialErr[n] == -1)
							sendInitialErr[n] = listAllCycles.get(c)[n].err;
					}
				}
			}
			msaAgg.proximityAvgErr = average(sendInitialErr);
			msaAgg.proximityMaxErr = max(sendInitialErr);

		}

		msaAgg.lpdTotalMessage_AnchorReception = msaAgg.lpdTotalMessage
				- msaAgg.cogTotalMessage;
		msaAgg.lpdAvgMessage_AnchorReception = msaAgg.lpdTotalMessage_AnchorReception
				/ (msaAgg.lostCount + 0.0);

		msaAgg.dpdvTotalMessage_AnchorReception = msaAgg.dpdvTotalMessage
				- msaAgg.cogTotalMessage;
		msaAgg.dpdvAvgMessage_AnchorReception = msaAgg.dpdvTotalMessage
				/ (msaAgg.lostCount + 0.0);

		return msaAgg;
	}

	private static int getMessageCount(List<MSAReportTuple[]> listAllCycles,
			int c, int n) {
		return listAllCycles.get(c)[n].messageSent
				- listAllCycles.get(c - 1)[n].messageSent;
	}

	private static void setAnchorPercent_AvgDeg_NodeCount_LostCount(
			String prefix, MSAAggReportTuple msaAgg) throws Exception {
		Scanner sc = new Scanner(new File(path + prefix + " - " + "Network"
				+ ".txt"));

		sc.nextLine();// width
		sc.nextLine();// height
		sc.next();
		msaAgg.r = sc.nextInt();
		sc.nextLine();// n r
		sc.nextLine();// a r
		sc.next();
		msaAgg.nodeCount = sc.nextInt();
		sc.nextLine();// n count
		sc.nextLine();// threshold
		sc.nextLine();// noise

		sc.next();
		msaAgg.anchorPercent = sc.nextDouble();
		int anchors = (int) Math
				.ceil(((msaAgg.nodeCount * msaAgg.anchorPercent) / 100.0));
		msaAgg.lostCount = msaAgg.nodeCount - anchors;
		sc.next();
		msaAgg.avgDeg = sc.nextDouble();
		sc.close();
	}

	private static List<MSAReportTuple[]> getListAllCycles(Scanner sc,
			int nodeCount) {
		List<MSAReportTuple[]> listAllCycles = new ArrayList<MSAReportTuple[]>();
		String s;

		while (sc.hasNext()) {
			s = sc.nextLine();
			if (sc.hasNextLine()) {
				s = sc.nextLine();

				MSAReportTuple[] cycleReport = new MSAReportTuple[nodeCount];
				boolean hasNewTuple = false;
				for (int i = 0; i < nodeCount; i++) {
					hasNewTuple = false;

					// s = sc.next();
					if (sc.hasNextInt()) {
						cycleReport[i] = new MSAReportTuple();
						hasNewTuple = true;
						cycleReport[i].id = sc.nextInt();
						cycleReport[i].Type = sc.next().charAt(0);
						cycleReport[i].Localization_Phase = sc.next();
						cycleReport[i].RectNo = sc.nextInt();
						cycleReport[i].avgHopLen = sc.nextInt();
						cycleReport[i].hopToAnchor = sc.nextInt();
						cycleReport[i].msJumpSize = sc.nextInt();
						cycleReport[i].degree = sc.nextInt();
						cycleReport[i].anchorDeg = sc.nextInt();
						cycleReport[i].messageSent = sc.nextInt();
						cycleReport[i].x = sc.nextDouble();
						cycleReport[i].y = sc.nextDouble();
						cycleReport[i].x_ = sc.nextDouble();
						cycleReport[i].y_ = sc.nextDouble();
						String ss = sc.next();
						cycleReport[i].err = Double.parseDouble(ss);
						ss = sc.nextLine();
					}
				}

				if (hasNewTuple)
					listAllCycles.add(cycleReport);
			}
		}
		return listAllCycles;
	}

	static int sum(int[] arr) {
		int sum = 0;
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}
		return sum;
	}

	static double average(double[] arr) {
		double sum = 0;
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}
		return sum / arr.length;
	}

	static double max(double[] arr) {
		double max = arr[0];
		for (int i = 0; i < arr.length; i++) {
			if (max < arr[i])
				max = arr[i];
		}
		return max;
	}

	static int myRound(double d) {
		int floor = (int) Math.floor(d);
		if (d <= floor + 0.5)
			return floor;
		else
			return floor + 1;
	}

	public static void main(String[] args) {
		summarize();
	}

}
