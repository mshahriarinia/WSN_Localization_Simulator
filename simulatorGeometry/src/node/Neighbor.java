package node;

import java.util.Random;

public class Neighbor {

	private Node node1, node2;//

	private double noisyDistance;

	private Random DISTANCE_NOISE_GENERATOR = new Random();

	/**
	 * denotes a neighboring relation always node 1 is the smaller id and node2
	 * is the bigger
	 * 
	 * @param node1
	 * @param node2
	 */
	public Neighbor(Node node1, Node node2) {
		if (!node1.isInNeighborArea(node2))
			throw new RuntimeException("not really neighbor");

		if (node1.getId().getValue() < node2.getId().getValue()) {
			this.node1 = node1;
			this.node2 = node2;
		} else {
			this.node1 = node2;
			this.node2 = node1;
		}

		node1.addNeighbor(this);
		node2.addNeighbor(this);

		// TODO DONE: negative noise / positive noise: gaussian noise with zero
		// mean 1 stddev will solve it
		double actualDist = node1.getRealDistance(node2);
		double maxNoise = actualDist
				* node1.getNodeList().getZXControl().MAX_DISTANCE_NOISE_RANGE_FRACTION;
		double noise = DISTANCE_NOISE_GENERATOR.nextGaussian() * maxNoise;

		// currently we only reduce noise from actual distance
		if (noise < actualDist)
			noisyDistance = actualDist - noise;
		else
			noisyDistance = actualDist - noise / 100;
	}

	public Node getNode1() {
		return node1;
	}

	public Node getNode2() {
		return node2;
	}

	public double getPureDistance() {
		return node1.getCenter().distance(node2.getCenter());
	}

	public double getNoisyDistance() {
		// NodeList nl = getNode1().getNodeList();
		// return (nl.getZXControl().NODE_RADIUS + nl.getAverageHopSize()) / 2;
		// return zxControl.NODE_RADIUS *
		// nl.getAverageHopSize()
		return noisyDistance;
	}

	public void setNoisyDistance(double d) {
		noisyDistance = d;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Neighbor) {
			return ((Neighbor) obj).node1 == node1
					&& ((Neighbor) obj).node2 == node2;
		}
		return super.equals(obj);
	}

	public Node getOtherNode(Node node) {
		if (node1 == node) {
			return node2;
		} else if (node2 == node) {
			return node1;
		}
		throw new RuntimeException("Node not in neighbor, to get other node.");
	}

	public boolean includes(Node node) {
		return node1 == node || node2 == node;
	}

	@Override
	public String toString() {
		return "N[" + node1.getId() + "-" + node2.getId() + "]";
	}
}
