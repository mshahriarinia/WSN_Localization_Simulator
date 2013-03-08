package reporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import control.ZXControl;

public class LPDMaxMemoryMakeTabe {

	static String title;

	public static void main(String[] args)

	{
		try {
			Scanner sc = new Scanner(new File(ZXControl.OUTPUT_DIRECTORY
					+ MSAngularReportSummarizerAggregator.aggregateFileName));

			// load tuples
			List<MSAAggReportTuple> listTuples = new ArrayList<MSAAggReportTuple>();

			title = sc.nextLine();

			while (sc.hasNextInt()) {
				listTuples.add(MSAngularReportSummarizerAggregator.read(sc));
			}
			sc.close();

			double[][] data = new double[listTuples.size()][3];

			for (int i = 0; i < listTuples.size(); i++) {
				data[i][0] = listTuples.get(i).anchorPercent;
				data[i][1] = listTuples.get(i).avgDeg;
				data[i][2] = listTuples.get(i).amsDPDVAvgErr

































;
			}

			MakeTable.getTable(data);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
