package guiInterface;

/**
 * Provides the primitive graphic utilities.
 */
public interface IPrimitiveGUIInterface {

	public void drawPoint(RGB rgb, int x, int y);

	public void drawRect(RGB rgb, int x0, int y0, int width, int height);

	public void drawLine(RGB rgb, int x0, int y0, int x1, int y1);

	public void drawString(RGB rgb, String s, int x, int y);

	public void drawCircle(RGB rgb, int x, int y, int r);

	public void redraw();
	
	public void resetCanvas();

	public void saveCanvasImage(int index);
}
