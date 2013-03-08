package geom.checkerBoard;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import control.SimulationInfoContext;

/**
 * A checker board with some slice length took from average hop or etc.
 */
public class FieldCheckerBoard implements SimulationInfoContext {

	private int xAxisSlicesNo;

	private int yAxisSlicesNo;

	private List<Rectangle2D> listAreas;

	private int sliceLength;

	private Rectangle2D area;

	/**
	 * Initializes according to the AverageHopSize of nodeList. Last rect in a
	 * row or column might not be as big as others but just to keep the left
	 * over parts up to avg hop size
	 * 
	 * @param nodeList
	 */
	public FieldCheckerBoard(Rectangle2D area, int sliceLength,
			boolean hasFieldExtention) {
		this.area = area;
		this.sliceLength = sliceLength;
		initListAreas(hasFieldExtention);
	}

	private void initListAreas(boolean hasFieldExtention) {

		int fieldExtentionAmount = 0;
		if (hasFieldExtention) {
			fieldExtentionAmount = sliceLength;
		}

		// +2 : one is for zero and one is for an upper multiplier of
		// sliceLength to cover the whole area
		double[] xArr = new double[(int) area.getWidth() / sliceLength + 2];
		setSliceValues(area.getMinX(), xArr);

		double[] yArr = new double[(int) area.getHeight() / sliceLength + 2];
		setSliceValues(area.getMinY(), yArr);

		// TODO - check extendion of area in results
		if (hasFieldExtention) {
			xArr = getExtendedArray(xArr, area.getMinX(), area.getMaxX(),
					fieldExtentionAmount);
			yArr = getExtendedArray(yArr, area.getMinY(), area.getMaxY(),
					fieldExtentionAmount);
		}

		xAxisSlicesNo = xArr.length - 1;
		yAxisSlicesNo = yArr.length - 1;

		Arrays.sort(xArr);
		Arrays.sort(yArr);

		listAreas = getListAreas(xArr, yArr);
	}

	private double[] getExtendedArray(double[] arr, double min, double max,
			int fieldExtentionAmount) {
		// fieldExtentionNo = 2 one for the beginning, one for end
		double[] extendedArr = Arrays.copyOf(arr, arr.length + 2);
		extendedArr[extendedArr.length - 1] = min - fieldExtentionAmount;
		extendedArr[extendedArr.length - 2] = max + fieldExtentionAmount;
		return extendedArr;
	}

	private void setSliceValues(double minValue, double[] arr) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = minValue + sliceLength * i;
		}
	}

	public static List<Rectangle2D> getListAreas(double[] xArr, double[] yArr) {
		List<Rectangle2D> listAreas = new ArrayList<Rectangle2D>();

		for (int i = 0; i < xArr.length; i++) {
			for (int j = 0; j < yArr.length; j++) {

				if ((i != xArr.length - 1) && (j != yArr.length - 1)) {
					double x0 = xArr[i];
					double y0 = yArr[j];

					double x1 = xArr[i + 1];
					double y1 = yArr[j + 1];

					double w = x1 - x0;
					double h = y1 - y0;

					Rectangle2D rect = new Rectangle2D.Double(x0, y0, w, h);

					listAreas.add(rect);
				}
			}
		}
		return listAreas;
	}

	/**
	 * In <b>column order:</b> xIndex * XAxisSlicesNo + yIndex;
	 * 
	 * @param xIndex
	 * @param yIndex
	 * @return
	 */
	public Rectangle2D getRectangle(int xIndex, int yIndex) {
		int oneDIndex = get2DTo1DIndex(xIndex, yIndex);
		return listAreas.get(oneDIndex);
	}

	/**
	 * Converts a 2D index of a rectangle in the field array to the 1D index in
	 * the list of rects received from fieldCheclerBoard. In a column order.
	 * 
	 * @param fieldCB
	 * @param xIndex
	 * @param yIndex
	 * @return
	 */
	public int get2DTo1DIndex(int xIndex, int yIndex) {
		int index = xIndex * getYAxisSlicesNo() + yIndex;
		return index;
	}

	/**
	 * 
	 * @param rect
	 * @return 2D index of rectangle in the field array
	 */
	public Point2D getRectIndex(Rectangle2D rect) {
		// avoid providing a negative index by adding one
		int xIndex = (int) Math.round(rect.getMinX() / getSliceLength()) + 1;
		int yIndex = (int) Math.round(rect.getMinY() / getSliceLength()) + 1;

		return new Point2D.Double(xIndex, yIndex);
	}

	/**
	 * 
	 * @param x
	 * @return the x index of the rectangle that contains x coordinate
	 */
	public int getContainingRectangleXIndex(double x) {
		int index = (int) Math.floor(x / sliceLength) + 1;
		if (index == xAxisSlicesNo - 1)
			if (x > listAreas.get(listAreas.size() - 1).getMaxX())
				return -1;
		return index;
	}

	public int getContainingRectangleYIndex(double y) {
		int index = (int) Math.floor(y / sliceLength) + 1;
		if (index == xAxisSlicesNo - 1)
			if (y > listAreas.get(listAreas.size() - 1).getMaxY())
				return -1;
		return index;
	}

	public boolean isValidRectIndex(int xIndex, int yIndex) {
		return xIndex >= 0 && yIndex >= 0 && xIndex <= xAxisSlicesNo - 1
				&& yIndex <= yAxisSlicesNo - 1;
	}

	public int getXAxisSlicesNo() {
		return xAxisSlicesNo;
	}

	public int getYAxisSlicesNo() {
		return yAxisSlicesNo;
	}

	public List<Rectangle2D> getListAreas() {
		return listAreas;
	}

	public int getSliceLength() {
		return sliceLength;
	}
}
