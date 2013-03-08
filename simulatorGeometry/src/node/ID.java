package node;

import java.util.List;

public class ID {

	public static final ID BROADCAST = new ID(Integer.MIN_VALUE);

	private Node node;

	private int value;

	public ID(Node node) {
		value = node.getNodeList().getNextNodeCounter();
		this.node = node;
	}

	public ID(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(NodeList nodeList, int value) {
		this.value = value;
		if (value < node.getNodeList().getLastNodeCounter()
				|| !isUnique(nodeList))
			throw new RuntimeException("Bad id.");
		if (value >= node.getNodeList().getLastNodeCounter())
			node.getNodeList().setLastNodeCounter(value);
	}

	private boolean isUnique(NodeList nodeList) {
		List<Node> l = nodeList.getListNodes();
		for (Node temp : l) {
			if (temp != node)
				if (temp.getId().equals(this))
					return false;
		}
		return true;
	}

	public Node getNode() {
		return node;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ID)
			return ((ID) obj).value == value;
		return super.equals(obj);
	}

	@Override
	public String toString() {
		if (this.equals(BROADCAST))
			return "*";
		else
			return getValue() + "";
	}
}
