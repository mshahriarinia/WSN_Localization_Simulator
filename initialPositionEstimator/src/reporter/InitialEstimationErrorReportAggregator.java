package reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import control.ZXControl;

public class InitialEstimationErrorReportAggregator {

	public static String plainReportFileName = "000plainReport.txt";

	private static String delimeter = "\t";

	public static void main(String[] args) {

		initialPositionErrorReportAggregation(ZXControl.OUTPUT_DIRECTORY);
	}

	public static void initialPositionErrorReportAggregation(String directory) {

		int reportCount = 0;

		PrintStream ps = null;
		try {
			ps = new PrintStream(new File(directory + plainReportFileName));
			ps.println(ReportTuple.getTitles(delimeter));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		ReportTuple t = new ReportTuple();

		for (int i = 0; i < 265; i++) {

			int nodeCount = 0;

			try {
				// skips if the file does not exist
				Scanner scanner = new Scanner(new File(directory + "a" + i
						+ ".txt"));

				reportCount++;

				t.reset();

				t.fileNo = i;

				double myIniTemp = 0;
				double dvTemp = 0;
				double cogTemp = 0;
				double proximityTemp = 0;

				while (scanner.hasNext()) {
					nodeCount++;

					// node id
					scanner.next();

					// node deg
					scanner.next();

					// x
					scanner.next();
					// y
					scanner.next();

					// my ini
					myIniTemp = scanner.nextDouble();
					t.myIniMax = getMax(t.myIniMax, myIniTemp);
					t.myIniAvg += myIniTemp;

					// dv
					dvTemp = scanner.nextDouble();
					t.dvMax = getMax(t.dvMax, dvTemp);
					t.dvAvg += dvTemp;
					// dvMax = getMax(dvMax, dvTemp);
					// dvSum += dvTemp;

					// cog
					cogTemp = scanner.nextDouble();
					t.cogMax = getMax(t.cogMax, cogTemp);
					t.cogAvg += cogTemp;

					// proximity
					proximityTemp = scanner.nextDouble();
					t.proximityMax = getMax(t.proximityMax, proximityTemp);
					t.proximityAvg += proximityTemp;
				}

				t.divideAverages(nodeCount);

				setR_AvgDeg_AnchorPercent(directory, t, i);

				print(ps, t);

				scanner.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(i + " " + nodeCount);
			}
		}
		ps.close();
	}

	/**
	 * load from Graph File
	 * 
	 * @param t
	 * @param i
	 */
	private static void setR_AvgDeg_AnchorPercent(String directory,
			ReportTuple t, int i) {
		try {
			Scanner sc = new Scanner(new File(directory + "graph" + i + ".txt"));
			String s;
			s = sc.next();// width
			s = sc.next();// width

			s = sc.next();// height
			s = sc.next();// height

			s = sc.next();// radius
			t.radius = sc.nextInt();

			s = sc.next();// anchor radius
			s = sc.next();// anchor radius

			s = sc.next();// node count
			s = sc.next();// node count

			s = sc.next();// threshold
			s = sc.next();// threshold

			s = sc.next();// noise
			s = sc.next();// noise

			s = sc.next();// a %
			s = sc.next();// a%

			boolean debug = false;

			if (debug)
				System.out.println(s);

			s = sc.next();// avg deg
			t.averageDegree = sc.nextDouble();// avg deg

			int nodeCount = 0;
			int anchorCount = 0;

			sc.nextLine();
			while (sc.hasNextLine()) {
				nodeCount++;
				char c = sc.next().charAt(0);
				sc.nextLine();
				if (c == 'A')
					anchorCount++;
			}
			t.anchorPercent = anchorCount / (nodeCount + 0.0);

			sc.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double getMax(double d1, double d2) {
		return (d1 > d2) ? d1 : d2;
	}

	private static void DELIM(PrintStream ps) {
		ps.print(delimeter);
	}

	public static void print(PrintStream ps, ReportTuple t) {
		ps.print(t.fileNo);
		DELIM(ps);
		// DELIM(ps);

		ps.print(t.radius);
		DELIM(ps);
		// DELIM(ps);

		ps.print(ZXControl.doubleToString(t.averageDegree));
		DELIM(ps);

		ps.print(ZXControl.doubleToString(t.anchorPercent * 100));
		DELIM(ps);

		printResults(ps, t);

		// print(ps, t.myIniAvg, t.myIniMax);
		// print(ps, t.dvAvg, t.dvMax);
		// print(ps, t.cogAvg, t.cogMax);
		// print(ps, t.proximityAvg, t.proximityMax);
		ps.println();
	}

	private static void printResults(PrintStream ps, ReportTuple t) {
		ps.print(ZXControl.doubleToString(t.myIniAvg));
		DELIM(ps);
		// DELIM(ps);
		ps.print(ZXControl.doubleToString(t.myIniMax));
		// DELIM(ps);
		DELIM(ps);

		ps.print(ZXControl.doubleToString(t.dvAvg));
		DELIM(ps);
		ps.print(ZXControl.doubleToString(t.dvMax));
		DELIM(ps);

		ps.print(ZXControl.doubleToString(t.cogAvg));
		DELIM(ps);
		ps.print(ZXControl.doubleToString(t.cogMax));
		DELIM(ps);
		// DELIM(ps);

		ps.print(ZXControl.doubleToString(t.proximityAvg));
		// DELIM(ps);
		// DELIM(ps);
		DELIM(ps);
		ps.print(ZXControl.doubleToString(t.proximityMax));

	}

	/**
	 * print average and max consecutively
	 * 
	 * @param ps
	 * @param avg
	 * @param max
	 */
	// public static void print(PrintStream ps, double avg, double max) {
	// ps.print(zxControl.doubleToString(avg));
	// DELIM(ps);
	// DELIM(ps);
	// ps.print(zxControl.doubleToString(max));
	// DELIM(ps);
	// }
}
