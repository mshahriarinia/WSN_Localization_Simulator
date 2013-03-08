package geom.areaPixelByPixel;

import java.util.ArrayList;
import java.util.List;

import node.Node;

public class PixelHitEntry {

	private List<Node> refs = new ArrayList<Node>();

	/**
	 * is hit regarding the reference point
	 */
	public void addRef(Node node) {
		refs.add(node);
	}

	public List<Node> getRefs() {
		return refs;
	}

}
