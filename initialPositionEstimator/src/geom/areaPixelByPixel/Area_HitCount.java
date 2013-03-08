package geom.areaPixelByPixel;

import geom.checkerBoard.NodesCheckerBoard;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import node.Node;

/**
 * stores the two data associated together
 * 
 * @author Morteza
 * 
 */
public class Area_HitCount {
	private Rectangle2D area;
	private List<Node> refs;

	public Area_HitCount(Rectangle2D area) {
		this.area = area;
		refs = new ArrayList<Node>();
	}

	public List<Node> getHitingRefs() {
		return refs;
	}

	public void addHitCounts(List<Node> refs) {
		this.refs.addAll(refs);
	}

	public void addHitCounts(Node ref) {
		this.refs.add(ref);
	}

	public Rectangle2D getArea() {
		return area;
	}

	public static List<Area_HitCount> init(List<Node> refs) {
		List<Area_HitCount> areaHitCountList = new ArrayList<Area_HitCount>();

		List<Rectangle2D> areaList = NodesCheckerBoard.getListAreas(refs);
		for (Rectangle2D area : areaList) {
			areaHitCountList.add(new Area_HitCount(area));
		}

		return areaHitCountList;
	}
}
