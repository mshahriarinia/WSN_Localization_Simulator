package reporter;

import java.util.Comparator;

public class ReportTuple {
	public int fileNo;
	public int radius;
	public double averageDegree;
	public double anchorPercent;
	public double myIniAvg;
	public double myIniMax;
	public double dvAvg;
	public double dvMax;
	public double cogAvg;
	public double cogMax;
	public double proximityAvg;
	public double proximityMax;

	public static Comparator<ReportTuple> averageDegreeComparator = new Comparator<ReportTuple>() {
		@Override
		public int compare(ReportTuple o1, ReportTuple o2) {
			return (o1.averageDegree - o2.averageDegree > 0) ? 1 : -1;
		}
	};

	public static Comparator<ReportTuple> anchorPercentComparator = new Comparator<ReportTuple>() {
		@Override
		public int compare(ReportTuple o1, ReportTuple o2) {
			return (o1.anchorPercent - o2.anchorPercent > 0) ? 1 : -1;
		}
	};

	public static String getTitles(String delimeter) {
		return "fileNo" + delimeter + "radius" + delimeter + "Degree"
				+ delimeter + "Anchors" + delimeter + "MyIni AVG" + delimeter
				+ "MyIni Max" + delimeter + "DV AVG" + delimeter + "DV Max"
				+ delimeter + "COG AVG" + delimeter + "COG Max" + delimeter
				+ "Proximity AVG" + delimeter + "Proximity Max";
	}

	public void reset() {
		fileNo = -1;
		radius = 0;
		averageDegree = 0;
		anchorPercent = 0;
		myIniAvg = 0;
		myIniMax = 0;
		dvAvg = 0;
		dvMax = 0;
		cogAvg = 0;
		cogMax = 0;
		proximityAvg = 0;
		proximityMax = 0;
	}

	public void divideAverages(int nodeCount) {
		averageDegree /= nodeCount;
		anchorPercent /= nodeCount;
		myIniAvg /= nodeCount;
		dvAvg /= nodeCount;
		cogAvg /= nodeCount;
		proximityAvg /= nodeCount;
	}

	public void devideMAximums(int nodeCount) {
		myIniMax /= nodeCount;
		dvMax /= nodeCount;
		cogMax /= nodeCount;
		proximityMax /= nodeCount;
	}

}
