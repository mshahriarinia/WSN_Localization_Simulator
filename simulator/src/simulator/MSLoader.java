package simulator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import node.ID;
import node.Neighbor;
import node.Node;
import simulator.localizable.MSAnchor;
import simulator.localizable.MSLostNode;
import simulator.localizable.field.FieldGenerator;
import control.ZXControl;

public class MSLoader {

	private int anchorCount = 0;

	public void loadFile(File file, FieldGenerator fieldGenerator) throws Exception {
		List<TempNodeNeighbors> listNodeNeighbors = new ArrayList<TempNodeNeighbors>();
		Scanner scanner = new Scanner(file);

		scanner.next();
		scanner.next();
		// ZXControl.FIELD_WIDTH = Integer.parseInt(scanner.next());

		scanner.next();
		scanner.next();
		// ZXControl.FIELD_HEIGHT = Integer.parseInt(scanner.next());

		String s;

		ZXControl zxControl = fieldGenerator.getNodeList().getZXControl();

		if (!ZXControl.PRELOAD_NODE_RADIUS) {
			s = scanner.next();
			zxControl.NODE_RADIUS = Integer.parseInt(scanner.next());
		} else {
			s = scanner.next();
			s = scanner.next();
		}

		if (!ZXControl.PRELOAD_NODE_RADIUS) {
			s = scanner.next();
			zxControl.ANCHOR_RADIUS = Integer.parseInt(scanner.next());
		} else {
			s = scanner.next();
			s = scanner.next();
		}

		boolean debug = false;
		if (debug)
			System.out.println(s);

		s = scanner.next();
		zxControl.NODE_COUNT = Integer.parseInt(scanner.next());

		s = scanner.next();
		zxControl.UPDATE_THRESHOLD_TO_TRANSMIT = Float.parseFloat(scanner
				.next());

		s = scanner.next();
		zxControl.MAX_DISTANCE_NOISE_RANGE_FRACTION = Float.parseFloat(scanner
				.next());

		if (!ZXControl.PRELOAD_ANCHOR_PERCENT) {
			s = scanner.next();
			zxControl.setANCHOR_PERCENT(scanner.nextDouble());

		} else {
			s = scanner.next();
			s = scanner.next();
		}

		// skip average degree
		s = scanner.next();
		s = scanner.next();

		// Finished parameter loading

		for (int i = 0; i < zxControl.NODE_COUNT; i++) {
			TempNodeNeighbors tNN = new TempNodeNeighbors();

			Node node = makeNode(fieldGenerator, scanner);

			if (!ZXControl.PRELOAD_NODE_RADIUS) {
				tNN.setListTempNeighbor(getNeighbors(scanner, node));
				if (tNN.getListTempNeighbor().size() != 0) {
					tNN.setNode(tNN.getListTempNeighbor().get(0).getNode());
				}
				listNodeNeighbors.add(tNN);
			}

			scanner.nextLine();
		}

		scanner.close();

		if (!ZXControl.PRELOAD_NODE_RADIUS) {
			assignNeighborsDistanceInfo(listNodeNeighbors);
		}
	}
	
//	public void loadFile(FieldGenerator fieldGenerator) throws Exception {
//
//		
//
//		File f = new File(ZXControl.OUTPUT_DIRECTORY
//				+ "0000 - Base Network.txt");
//		loadFile(f, fieldGenerator);
//	}

	private List<TempNeighbor> getNeighbors(Scanner scanner, Node node) {
		List<TempNeighbor> listNeighbor = new ArrayList<TempNeighbor>();

		if (!ZXControl.PRELOAD_NODE_RADIUS) {
			int neighborCount = scanner.nextInt();

			for (int i = 0; i < neighborCount; i++) {
				TempNeighbor tn = new TempNeighbor();
				tn.setNode(node);
				tn.setId(scanner.nextInt());
				tn.setDistance(scanner.nextDouble());
				listNeighbor.add(tn);
			}
		}
		return listNeighbor;

	}

	private void assignNeighborsDistanceInfo(
			List<TempNodeNeighbors> listNodeNeighbors) {

		for (TempNodeNeighbors tnn : listNodeNeighbors) {
			Node node = tnn.getNode();

			List<TempNeighbor> listTempNeighbor = tnn.getListTempNeighbor();

			for (TempNeighbor tempNeighbor : listTempNeighbor) {
				Neighbor neighbor = node.getNeighbor(new ID(tempNeighbor
						.getId()));
				neighbor.setNoisyDistance(tempNeighbor.getDistance());
			}
		}
	}

	private Node makeNode(FieldGenerator fieldGenerator, Scanner scanner) {
		char nodeType = scanner.next().charAt(0);
		int id = scanner.nextInt();
		double x = scanner.nextDouble();
		double y = scanner.nextDouble();
		Node node = null;
		if (!ZXControl.PRELOAD_ANCHOR_PERCENT) {
			if (nodeType == 'L')
				node = new MSLostNode(fieldGenerator.getNodeList(),
						fieldGenerator.getArea(), x, y);
			else if (nodeType == 'A')
				node = new MSAnchor(fieldGenerator.getNodeList(),
						fieldGenerator.getArea(), x, y);
			else
				throw new RuntimeException("Unknown node type.");
		} else {
			if (anchorCount < fieldGenerator.getNodeList().getZXControl()
					.getANCHOR_COUNT()) {
				node = new MSAnchor(fieldGenerator.getNodeList(),
						fieldGenerator.getArea(), x, y);
				anchorCount++;
			} else {
				node = new MSLostNode(fieldGenerator.getNodeList(),
						fieldGenerator.getArea(), x, y);
			}
		}
		node.getId().setValue(fieldGenerator.getNodeList(), id);
		return node;
	}
}

class TempNeighbor {
	int id;
	double distance;
	Node node;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

}

class TempNodeNeighbors {
	private Node node;
	private List<TempNeighbor> listTempNeighbor;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public List<TempNeighbor> getListTempNeighbor() {
		return listTempNeighbor;
	}

	public void setListTempNeighbor(List<TempNeighbor> listTempNeighbor) {
		this.listTempNeighbor = listTempNeighbor;
	}
}
