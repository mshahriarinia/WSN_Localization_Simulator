package node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import control.ZXControl;

public class NodeList {

	private int messageCount;

	/**
	 * every object knows to which node list it belongs so here it knows its
	 * controller (zxControl) and the iCycler(simulator)
	 */
	private ZXControl zxControl;

	// private SimulationInfoContext simulationInfoContext;

	 private double AVERAGE_HOP_SIZE = -1;

	private int nodeCounter = 0;

	private List<Node> listNodes = new ArrayList<Node>();

	public NodeList(ICycler iCycler) {
		this.zxControl = new ZXControl(iCycler);
	}

	// public void setSimulationInfoContext(
	// SimulationInfoContext simulationInfoContext) {
	// this.simulationInfoContext = simulationInfoContext;
	// }

	// /**
	// *
	// * @return the simulation specific context data related to a simulation
	// * execution
	// */
	// public SimulationInfoContext getSimulationInfoContext() {
	// return simulationInfoContext;
	// }

	public ZXControl getZXControl() {
		return zxControl;
	}

	public int getNextNodeCounter() {
		return ++nodeCounter;
	}

	public int getLastNodeCounter() {
		return nodeCounter;
	}

	public void setLastNodeCounter(int nodeCounter) {
		this.nodeCounter = nodeCounter;
	}

	public void addNode(Node node) {
		if (!listNodes.contains(node)) {
			setNeighbors(node);
			listNodes.add(node);
			// AVERAGE_HOP_SIZE = -1;
		}
	}

	public List<Node> getListNodes() {
		return listNodes;
	}

	private void setNeighbors(Node node) {
		for (Node temp : listNodes) {
			if (node.isInNeighborArea(temp)) {
				Neighbor neighbor = new Neighbor(node, temp);
				node.addNeighbor(neighbor);
				temp.addNeighbor(neighbor);
			}
		}
	}

	 public double getAverageHopSize() {
	 if (AVERAGE_HOP_SIZE != -1)
	 return AVERAGE_HOP_SIZE;
	 else {
	 int count = 0;
	 AVERAGE_HOP_SIZE = 0;
	 for (Node n : listNodes) {
	 for (Neighbor neigh : n.getNeighbors()) {
	 count++;
	 AVERAGE_HOP_SIZE += neigh.getPureDistance();
	 }
	 }
	 AVERAGE_HOP_SIZE /= (count * 2);
	
	 return AVERAGE_HOP_SIZE;
	 }
	 }

	public Node getNodeID(int id) {
		for (Node n : listNodes) {
			if (n.getId().getValue() == id)
				return n;
		}
		return null;
	}

	public static List<Node> sortNodesByX(List<Node> listNodes) {
		Node[] o = (Node[]) listNodes.toArray();
		Arrays.sort(o, new Comparator<Node>() {
			@Override
			public int compare(Node p1, Node p2) {
				return (int) Math.round(p1.getCenter().getX()
						- p2.getCenter().getX());
			}
		});
		List<Node> l = new ArrayList<Node>();
		l.addAll(Arrays.asList(o));
		return l;
	}

	public static List<Node> sortNodesByY(List<Node> listNodes) {
		Node[] o = (Node[]) listNodes.toArray();
		Arrays.sort(o, new Comparator<Node>() {
			@Override
			public int compare(Node p1, Node p2) {
				return (int) Math.round(p1.getCenter().getY()
						- p2.getCenter().getY());
			}
		});
		List<Node> l = new ArrayList<Node>();
		l.addAll(Arrays.asList(o));
		return l;
	}

	/**
	 * get the an array of all the xes
	 * 
	 * @return
	 */
	public static double[] getXArray(List<Node> list) {
		double arr[] = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i).getCenter().getX();
		}
		return arr;
	}

	/**
	 * get the an array of all the ys
	 * 
	 * @return
	 */
	public static double[] getYArray(List<Node> list) {
		double arr[] = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i).getCenter().getY();
		}
		return arr;
	}

	public void incMessageCount() {
		messageCount++;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public void reset() {
		for (Node node : listNodes) {
			node.reset();
		}

		messageCount = 0;
		// AVERAGE_HOP_SIZE=0;
	}
}
