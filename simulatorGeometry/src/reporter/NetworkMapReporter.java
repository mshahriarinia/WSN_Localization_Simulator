package reporter;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import node.Neighbor;
import node.Node;
import node.type.Anchor;
import node.type.LostNode;
import control.ZXControl;

public class NetworkMapReporter {
	public static void printNetwork(ZXControl zxControl, List<Node> nodes, String fileNamePrefix) {
		if (ZXControl.GRAPH_REPORTER) {
			try {
				FileOutputStream graphFile = new FileOutputStream(
						ZXControl.OUTPUT_DIRECTORY
								+ fileNamePrefix + " - "
								+ ZXControl.OUTPUT_FILE_GRAPH_PREFIX + ".txt");
				PrintStream graphStream = new PrintStream(graphFile);

				String delimiter = "\t\t";

				graphStream.print("FIELD_WIDTH" + delimiter);
				graphStream.println(ZXControl.FIELD_WIDTH);
				graphStream.print("FIELD_HEIGHT" + delimiter);
				graphStream.println(ZXControl.FIELD_HEIGHT);
				graphStream.print("NODE_RADIUS" + delimiter);
				graphStream.println(zxControl.NODE_RADIUS);
				graphStream.print("ANCHOR_RADIUS" + delimiter);
				graphStream.println(zxControl.ANCHOR_RADIUS);
				graphStream.print("NODE_COUNT" + delimiter);
				graphStream.println(zxControl.NODE_COUNT);
				graphStream.print("UPDATE_THRESHOLD_TO_TRANSMIT" + delimiter);
				graphStream.println(zxControl.UPDATE_THRESHOLD_TO_TRANSMIT);
				graphStream.print("MAX_DISTANCE_NOISE_RANGE_FRACTION"
						+ delimiter);
				graphStream
						.println(zxControl.MAX_DISTANCE_NOISE_RANGE_FRACTION);
				graphStream.print("ANCHOR_PERCENT" + delimiter);
				graphStream.println(zxControl.getANCHOR_PERCENT());

				graphStream.print("AVERAGE_DEGREE" + delimiter);
				graphStream.println(ConsoleReporter.getAvgDegree(nodes));

				for (Node node : nodes) {
					printNode(graphStream, node);
				}

				graphStream.close();
				graphFile.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static final String delimiter = "\t";

	private static void printNode(PrintStream printStream, Node node) {
		if (node instanceof Anchor)
			printStream.print('A');
		else if (node instanceof LostNode)
			printStream.print('L');
		else
			throw new RuntimeException("Invalid node.");
		printStream.print(delimiter);
		printStream.print(node.getId());
		printStream.print(delimiter);
		printStream.print(node.getCenter().getX());
		printStream.print(delimiter);
		printStream.print(node.getCenter().getY());
		printStream.print(delimiter);
		printStream.print(node.getNeighbors().size());
		printStream.print(delimiter);
		List<Neighbor> temp = node.getNeighbors();
		for (Neighbor n : temp) {
			printStream.print(n.getOtherNode(node).getId());
			printStream.print(delimiter);
			printStream.print(n.getNoisyDistance());
			printStream.print(delimiter);
		}
		printStream.println();
	}
}
