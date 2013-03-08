package geom.areaPixelByPixel;

import geom.MyRectArea;

import java.awt.geom.Rectangle2D;
import java.util.List;

import node.Node;

public class Field {

	private static final double MAX_R = 3;

	private static final double RESOLUTION = 0.1 * MAX_R;

	private static final double MAX_X = 100;

	private static final double MAX_Y = 100;

	private PixelHitEntry[][] field = new PixelHitEntry[((int) Math.round(MAX_X
			/ RESOLUTION))][((int) Math.round(MAX_Y / RESOLUTION))];

	public PixelHitEntry[][] getField() {
		return field;
	}

	/**
	 * gets the area htcount regarding a node
	 * 
	 * @return
	 */
	public List<Area_HitCount> getArea_HitCountsList(List<Node> refs, Node node) {

		setPixelHitEntries(refs, node);
		List<Area_HitCount> area_HitCountList = Area_HitCount.init(refs);

		for (Area_HitCount area_HitCount : area_HitCountList) {

			for (int i = 0; i < field.length; i++) {
				for (int j = 0; j < field[0].length; j++) {
					Rectangle2D pixel = getPixel(i, j);

					if (area_HitCount.getArea().intersects(pixel)) {
						area_HitCount.addHitCounts(field[i][j].getRefs());
					}
				}
			}
		}

		return area_HitCountList;
	}

	private void setPixelHitEntries(List<Node> refs, Node node) {
		for (Node ref : refs) {
			setPixelHitEntries(ref, node);
		}
	}

	/**
	 * added the number of hits in a pixeled field counter, this could later
	 * result in refs checked counter to count the number of pixels in a refs
	 * check and then the percent of the circle in the refs check
	 * 
	 * @param field
	 * @param ref
	 * @param node
	 *            unknown node
	 */
	private void setPixelHitEntries(Node ref, Node node) {
		double distance = node.getRealDistance(ref);
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[0].length; j++) {
				Rectangle2D pixel = Field.getPixel(i, j);
				if (MyRectArea.isHit(pixel, ref, distance)) {
					field[i][j].addRef(ref);
				}
			}
		}
	}

	private static Rectangle2D getPixel(int i, int j) {
		return new Rectangle2D.Double(i * RESOLUTION, j * RESOLUTION,
				RESOLUTION, RESOLUTION);
	}
}
