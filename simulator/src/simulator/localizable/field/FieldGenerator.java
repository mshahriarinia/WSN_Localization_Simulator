package simulator.localizable.field;

//
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import org.apache.commons.math.random.RandomDataImpl;

import node.Node;
import node.NodeList;
import simulator.localizable.MSAnchor;
import simulator.localizable.MSLostNode;
import control.ZXControl;

public class FieldGenerator {

	private Rectangle2D area;

	private Rectangle2D[] forbiddenAreas;

	private Random randomX, randomY;
	// private Random randomToBeAnchor;
	RandomDataImpl rd = new RandomDataImpl();

	int[][] clustreHeadsDimensions = new int[][] { { 50, 50 }, { 250, 50 },
			{ 50, 250 }, { 250, 250 }, { 450, 50 }, { 50, 450 }, { 250, 450 },
			{ 450, 250 }, { 450, 450 } };

	Point2D clustreHeads[] = null;
	// { new Point2D.Double(80, 80),
	// new Point2D.Double(420, 80), new Point2D.Double(80, 420),
	// new Point2D.Double(420, 420) };

	// Point2D clustreHeads[] = { new Point2D.Double(250, 250)};

	private NodeList nodeList;

	public FieldGenerator(NodeList nodeList, int width, int height) {

		// randomToBeAnchor = new Random();

		area = new Rectangle2D.Double(0, 0, width, height);

		randomX = new Random();
		randomY = new Random();

		Random randomForbiddenAreasCount = new Random();

		initClusterHeads(clustreHeadsDimensions);

		if (ZXControl.HAS_FORBIDDEN_AREAS) {
			int forbiddenAreasCount = randomForbiddenAreasCount.nextInt(4);
			forbiddenAreas = generateForbiddenAreas(forbiddenAreasCount);
		} else {
			forbiddenAreas = null;
		}

		this.nodeList = nodeList;
	}

	private void initClusterHeads(int[][] clustreHeadsDimensions) {
		clustreHeads = new Point2D[clustreHeadsDimensions.length];
		for (int i = 0; i < clustreHeadsDimensions.length; i++) {
			clustreHeads[i] = new Point2D.Double(clustreHeadsDimensions[i][0],
					clustreHeadsDimensions[i][1]);
		}
	}

	public Rectangle2D getArea() {
		return area;
	}

	public NodeList getNodeList() {
		return nodeList;
	}

	public void generateAllGraph() {

		GenerateClusteredActiveNodes(area, nodeList.getZXControl().NODE_COUNT);

		// TODO //GenerateActiveNodes(area, forbiddenAreas,
		// nodeList.getZXControl().NODE_COUNT);
	}

	private Rectangle2D[] generateForbiddenAreas(int count) {
		Rectangle2D[] arr = new Rectangle2D[count];// x0, y0, width, height

		for (int i = 0; i < arr.length; i++) {
			int x0 = getUniformRandom(randomX, area.getWidth());
			int y0 = getUniformRandom(randomY, area.getHeight());
			int width = getUniformRandom(randomX, area.getWidth() - x0);
			int height = getUniformRandom(randomY, area.getHeight() - y0);
			arr[i] = new Rectangle2D.Double(x0, y0, width, height);
		}
		return arr;
	}

	private int getUniformRandom(Random random, double max) {
		return random.nextInt((int) Math.round(max));
	}

	private void GenerateClusteredActiveNodes(Rectangle2D area, int nodeCount) {

		int clusterIndex;
		for (int i = 0; i < nodeCount; i++) {

			clusterIndex = randomX.nextInt(clustreHeads.length);

			Point2D newPoint = generateClusteredValidNodePoint(area,
					clustreHeads[clusterIndex]);

			Node node;
			if (i < nodeList.getZXControl().getANCHOR_COUNT())
				node = new MSAnchor(nodeList, area, newPoint);
			else
				node = new MSLostNode(nodeList, area, newPoint);
		}

	}

	private Point2D generateClusteredValidNodePoint(Rectangle2D area2,
			Point2D clusterHead) {
		Point2D p;
		while (!isValid(p = generateClusteredNodePoint(area, clusterHead),
				area2, forbiddenAreas)) {
			;
		}

		System.out.println(p);
		return p;
	}

	private Point2D generateClusteredNodePoint(Rectangle2D area2,
			Point2D clusterHead) {
		int offsetSign;
		offsetSign = randomX.nextBoolean() == true ? 1 : -1;
		int nodeRadius = nodeList.getZXControl().NODE_RADIUS;
		double mean = nodeRadius * 2;

		double x = clusterHead.getX() + offsetSign * rd.nextExponential(mean);
		offsetSign = randomX.nextBoolean() == true ? 1 : -1;
		double y = clusterHead.getY() + offsetSign * rd.nextExponential(mean);
		return new Point2D.Double(x, y);
	}

	private void GenerateActiveNodes(Rectangle2D area,
			Rectangle2D[] forbiddenAreas, int nodeCount) {
		for (int i = 0; i < nodeCount; i++) {
			Point2D newPoint = generateValidNodePoint(area, forbiddenAreas);
			Node node;
			if (i < nodeList.getZXControl().getANCHOR_COUNT())
				node = new MSAnchor(nodeList, area, newPoint);
			else
				node = new MSLostNode(nodeList, area, newPoint);
		}
	}

	// public Node generateNode(double x, double y) {
	// return generateNode(new Point2D.Double(x, y));
	// }
	//
	// public Node generateNode(Point2D point) {
	// Node n;
	// if (toBeAnchor()) {
	// n = new MSAnchor(nodeList, point);
	// } else {
	// n = new MSLostNode(nodeList, point);
	// }
	// return n;
	// }

	private Point2D generateValidNodePoint(Rectangle2D area,
			Rectangle2D[] forbiddenAreas) {
		Point2D p;
		while (!isValid(p = generateNodePoint(area, forbiddenAreas), area,
				forbiddenAreas))
			;
		return p;
	}

	private Point2D generateNodePoint(Rectangle2D area,
			Rectangle2D[] forbiddenAreas) {
		return new Point2D.Double(randomX.nextDouble() * area.getWidth(),
				randomY.nextDouble() * area.getHeight());
	}

	private static boolean isValid(Point2D point, Rectangle2D area,
			Rectangle2D[] forbiddenAreas) {
		if (area.contains(point)) {
			if (forbiddenAreas == null) {
				return true;
			} else {
				for (int i = 0; i < forbiddenAreas.length; i++) {
					if (forbiddenAreas[i].contains(point))
						return false;
				}
				return true;
			}
		}
		return false;
	}

	// /**
	// * 5% anchors
	// *
	// * @return
	// */
	// private boolean toBeAnchor() {
	// int rand = randomToBeAnchor.nextInt(100);
	// if (rand < nodeList.getZXControl().ANCHOR_PERCENT)
	// return true;
	// return false;
	// }

	public Rectangle2D[] getForbiddenAreas() {
		return forbiddenAreas;
	}
}
