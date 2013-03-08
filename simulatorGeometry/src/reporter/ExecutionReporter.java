package reporter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import node.Neighbor;
import node.Node;
import control.ZXControl;

public class ExecutionReporter {

	private FileOutputStream exeFile;

	private PrintStream exeStream;

	public ExecutionReporter(String fileNameIndex) {
		if (ZXControl.EXECUTION_REPORTER) {
			try {
				exeFile = new FileOutputStream(ZXControl.OUTPUT_DIRECTORY
						+ fileNameIndex + " "
						+ ZXControl.OUTPUT_FILE_EXECECUTION_PREFIX + ".txt");
			} catch (Exception e) {
				e.printStackTrace();
			}
			exeStream = new PrintStream(exeFile);
		}
	}

	public void write(Object o) {
		if (ZXControl.EXECUTION_REPORTER) {
			exeStream.print(o.toString());
		}
	}

	public void writeln(Object o) {
		if (ZXControl.EXECUTION_REPORTER) {
			write(o.toString());
			exeStream.println();
		}
	}

	public void dispose() {
		if (ZXControl.EXECUTION_REPORTER) {
			try {
				exeStream.flush();
				exeStream.close();
				exeFile.flush();
				exeFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void reportDistanceToNeighbors(List<Node> l) {
		if (ZXControl.EXECUTION_REPORTER) {
			write("{{{{ distanceReport");
			for (Node n1 : l) {
				List<Neighbor> ln = n1.getNeighbors();
				write(n1);
				for (Neighbor ne : ln) {
					Node n2 = ne.getOtherNode(n1);

					write("  -  ");
					write(n2);
					write("  :RE=  ");
					write(ZXControl.doubleToString(n1.getRealDistance(n2)));
					write("  ?Calc=  ");
					if (n1.getCalculatedCenter() != null
							&& n2.getCalculatedCenter() != null)
						write(ZXControl.doubleToString(getCalculatedDistance(
								n1, n2)));
					else
						write("null");
				}
				writeln("");
			}
			writeln("");
			writeln("}distanceReport");
		}
	}

	private static double getCalculatedDistance(Node node1, Node node2) {
		return node1.getCalculatedCenter()
				.distance(node2.getCalculatedCenter());
	}

	public void writeEndOfCycle(int cycleCount) {
		if (ZXControl.EXECUTION_REPORTER) {

			writeln("-------- ########################################################################################################");

			writeln("-------- ########################################################################################################");

			writeln("-------- #######################################################%"
					+ cycleCount
					+ "################################################");

			writeln("-------- ########################################################################################################");

			writeln("-------- ########################################################################################################");
		}
	}

}
