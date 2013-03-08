package reporter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import node.Node;
import node.NodeList;
import control.ZXControl;

/**
 * Reports errors in localization of the network in separate files. named on
 * cycle count and algorithm.
 */
public class NetworkErrorCycleReporter {
	/**
	 * chases error improvement in network in each cycle in a separate file
	 * 
	 * @param l
	 * @param cycleNo
	 */
	public static void networkErrorReportFile(NodeList nodeList, int cycleNo) {
		if (ZXControl.NETWORK_ERROR_CYCLE_REPORTER) {
			FileOutputStream os = null;

			PrintStream ps;

			try {
				os = new FileOutputStream(ZXControl.OUTPUT_DIRECTORY
						+ "Cycle Report//"
						+ ZXControl
								.getTitle(ZXControl.Initial_POSITION_ALGORITHM)
						+ " " + cycleNo + ".txt");
			} catch (Exception e) {
				e.printStackTrace();
			}
			ps = new PrintStream(os);

			ps.println("Message Count\t" + nodeList.getMessageCount());

			ps.println();
			for (Node n : nodeList.getListNodes()) {
				ps.print(n.getId().getValue());
				ps.print("\t");
				ps.print(ZXControl.doubleToString(n.getCenter().getX()));
				ps.print("\t");
				ps.print(ZXControl.doubleToString(n.getCenter().getY()));
				if (n.getCalculatedCenter() != null) {
					ps.print("\t");
					ps.print(ZXControl.doubleToString(n.getCalculatedCenter()
							.getX()));
					ps.print("\t");
					ps.print(ZXControl.doubleToString(n.getCalculatedCenter()
							.getY()));
					ps.print("\t");
					ps.println(ZXControl.doubleToString(n.getCenter().distance(
							n.getCalculatedCenter())));
				} else
					ps.println();
			}

			ps.close();
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
