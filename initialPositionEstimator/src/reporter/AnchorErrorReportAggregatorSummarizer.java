package reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import control.ZXControl;

public class AnchorErrorReportAggregatorSummarizer {

	private static String delimeter = "\t";

	public static String aggregatedReportFileName = "000AggregatedReport.txt";

	public static void main(String[] args) {

		initialPositionErrorReportAggregationSummarization(5);
	}

	public static void initialPositionErrorReportAggregationSummarization(
			int aggregationStep) {
		PrintStream ps = null;

		String plainFileName = InitialEstimationErrorReportAggregator.plainReportFileName;

		try {
			ps = new PrintStream(new File(ZXControl.OUTPUT_DIRECTORY
					+ aggregatedReportFileName));
			Scanner sc = new Scanner(new File(ZXControl.OUTPUT_DIRECTORY
					+ plainFileName));

			ps.println(ReportTuple.getTitles(delimeter));

			// skip titles
			sc.nextLine();

			List<ReportTuple> l = new ArrayList<ReportTuple>();

			while (sc.hasNext()) {
				ReportTuple t = new ReportTuple();

				sc.next();// file no
				t.radius = sc.nextInt();// radius

				t.averageDegree = sc.nextDouble();
				t.anchorPercent = sc.nextDouble() / 100;
				t.myIniAvg = sc.nextDouble() / t.radius * 100;
				t.myIniMax = sc.nextDouble() / t.radius * 100;
				t.dvAvg = sc.nextDouble() / t.radius * 100;
				t.dvMax = sc.nextDouble() / t.radius * 100;
				t.cogAvg = sc.nextDouble() / t.radius * 100;
				t.cogMax = sc.nextDouble() / t.radius * 100;
				t.proximityAvg = sc.nextDouble() / t.radius * 100;
				t.proximityMax = sc.nextDouble() / t.radius * 100;
				l.add(t);
			}

			ReportTuple[] arr = l.toArray(new ReportTuple[0]);
			Arrays.sort(arr, ReportTuple.anchorPercentComparator);

			int minAnchorPercent = (int) Math.floor(arr[0].anchorPercent * 100);
			ReportTuple aggregate = new ReportTuple();
			int aggregatedCount = 0;

			for (int i = 0; i < arr.length; i++) {
				if (!(arr[i].anchorPercent * 100 < minAnchorPercent
						+ aggregationStep)) {
					aggregate.divideAverages(aggregatedCount);
					aggregate.devideMAximums(aggregatedCount);
					InitialEstimationErrorReportAggregator.print(ps, aggregate);

					aggregate.reset();
					aggregatedCount = 0;
					minAnchorPercent += aggregationStep;
				}
				aggregatedCount++;
				aggregate.averageDegree += arr[i].averageDegree;
				aggregate.anchorPercent += arr[i].anchorPercent;
				aggregate.myIniAvg += arr[i].myIniAvg;
				aggregate.myIniMax += arr[i].myIniMax;
				aggregate.dvAvg += arr[i].dvAvg;
				aggregate.dvMax += arr[i].dvMax;
				aggregate.cogAvg += arr[i].cogAvg;
				aggregate.cogMax += arr[i].cogMax;
				aggregate.proximityAvg += arr[i].proximityAvg;
				aggregate.proximityMax += arr[i].proximityMax;
			}

			sc.close();
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
