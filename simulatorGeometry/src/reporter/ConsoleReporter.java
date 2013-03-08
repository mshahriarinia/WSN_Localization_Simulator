package reporter;

import java.util.List;

import node.Node;
import node.type.Anchor;
import control.ZXControl;

public class ConsoleReporter {

	public static void consoleStatictics(List<Node> nodes) {
		if (ZXControl.CONSOLE_REPORTER) {
			System.out.println("Max Deg: " + getMaxDegree(nodes));
			System.out.println("Min Deg: " + getMinDegree(nodes));
			System.out.println("Avg Deg: " + getAvgDegree(nodes));
			int anchorCount = getAnchors(nodes);
			System.out.println("Nodes: " + nodes.size() + "  Anchors: "
					+ anchorCount + "  Anchor Percent: " + anchorCount
					/ (nodes.size() + 0.0) * 100);

			getZeroNeighboringNodes(nodes);
		}
	}

	// public static void printGraph(List<Node> nodes) {
	// try {
	// FileOutputStream graphFile = new FileOutputStream(
	// Controller.OUTPUT_DIRECTORY
	// + Controller.OUTPUT_FILE_GRAPH_PREFIX + ".txt");
	// PrintStream graphStream = new PrintStream(graphFile);
	// ;
	// for (Node node : nodes) {
	// List<Neighbor1> ln = node.getNeighbors();
	// graphStream.print((node instanceof Anchor) ? "A" : "L");
	// graphStream.print(node.getId());
	// graphStream.print("[");
	// graphStream.print(ln.size());
	// graphStream.print("]");
	// graphStream.print("\t");
	// for (Neighbor1 neighbor : ln) {
	// graphStream
	// .print(neighbor.getOtherNode(node).getId() + " ");
	// }
	// graphStream.println();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	private static void getZeroNeighboringNodes(List<Node> nodes) {
		int count0 = 0;
		int count1 = 0;
		int count2 = 0;
		String coordinates = "";
		for (Node node : nodes) {
			if (node.getNeighbors().size() == 0) {
				count0++;
				coordinates += ZXControl.pointToString(node.getCenter());
			}
		}
		System.out.println(count0 + " Nodes with 0 neighbors:\t\t\t"
				+ coordinates);

		coordinates = "";

		for (Node node : nodes) {
			if (node.getNeighbors().size() == 1) {
				count1++;
				coordinates += ZXControl.pointToString(node.getCenter());
			}
		}
		System.out.println(count1 + " Nodes with 1 neighbors:\t\t\t"
				+ coordinates);

		coordinates = "";

		for (Node node : nodes) {
			if (node.getNeighbors().size() == 2) {
				count2++;
				coordinates += ZXControl.pointToString(node.getCenter());
			}
		}
		System.out.println(count2 + " Nodes with 2 neighbors:\t\t\t"
				+ coordinates);
	}

	private static int getAnchors(List<Node> nodes2) {
		int count = 0;
		for (Node node : nodes2) {
			if (node instanceof Anchor)
				count++;
		}
		return count;
	}

	private static int getMaxDegree(List<Node> nodes) {
		int max = 0;
		for (Node node : nodes) {
			int deg = node.getNeighbors().size();
			if (deg > max)
				max = deg;
		}
		return max;
	}

	private static int getMinDegree(List<Node> nodes) {
		int min = 0;
		for (Node node : nodes) {
			int deg = node.getNeighbors().size();
			if (deg < min)
				min = deg;
		}
		return min;
	}

	public static double getAvgDegree(List<Node> nodes) {
		int sum = 0;
		for (Node node : nodes) {
			sum = sum + node.getNeighbors().size();
		}
		return sum / (nodes.size() + 0.0);
	}
}
