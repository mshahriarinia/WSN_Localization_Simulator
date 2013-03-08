package gui;

import guiInterface.IPrimitiveGUIInterface;
import guiInterface.RGB;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import control.ZXControl;

/**
 * Implements the primitive graphic utilities.
 */
public class SWTUIInterface implements IPrimitiveGUIInterface {

	private GC gc;

//	Shell shell;
	
	int shellX,shellY;
	

	public SWTUIInterface(Shell shell, GC gc) {
//		this.shell = shell;
		shellX=shell.getBounds().width;
		shellY=shell.getBounds().width;
		this.gc = gc;
	}

	@Override
	public void saveCanvasImage(int index) {
		Image image = new Image(Display.getCurrent(), 500, 500);

		// Image image = new Image(display, canvasSize.x, canvasSize.y);
		gc.copyArea(image, 0, 0);
		gc.dispose();

		ImageData data = image.getImageData();
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { data };
		loader.save(ZXControl.OUTPUT_DIRECTORY
				+ ZXControl.OUTPUT_FILE_DIAG_REPORT + "//img " + index,
				SWT.IMAGE_BMP);

		image.dispose();
	}

	@Override
	public void drawPoint(RGB rgb, int x, int y) {
		Color back = gc.getBackground();
		Color c = SWTUIConstants.getSWTColor(rgb);
		gc.setBackground(c);
		gc.fillOval(x - 2, y - 2, 4, 4);
		gc.setBackground(back);
	}

	@Override
	public void drawRect(RGB rgb, int x0, int y0, int width, int height) {
		Color fore = gc.getForeground();
		Color c = SWTUIConstants.getSWTColor(rgb);
		gc.setForeground(c);
		gc.drawRectangle(x0, y0, width, height);
		gc.setForeground(fore);
	}

	@Override
	public void drawLine(RGB rgb, int x1, int y1, int x2, int y2) {
		Color fore = gc.getForeground();
		Color c = SWTUIConstants.getSWTColor(rgb);
		gc.setForeground(c);
		gc.drawLine(x1, y1, x2, y2);
		gc.setForeground(fore);
	}

	@Override
	public void redraw() {
		// shell.redraw();
	}

	@Override
	public void resetCanvas() {
		gc.fillRectangle(0, 0, shellX, shellY);
	}

	@Override
	public void drawString(RGB rgb, String s, int x, int y) {
		Color fore = gc.getForeground();
		Color c = SWTUIConstants.getSWTColor(rgb);
		gc.setForeground(c);
		gc.drawString(s, x, y, true);
		gc.setForeground(fore);
	}

	@Override
	public void drawCircle(RGB rgb, int x, int y, int r) {
		Color fore = gc.getForeground();
		Color c = SWTUIConstants.getSWTColor(rgb);
		gc.setForeground(c);
		gc.drawOval(x - r, y - r, 2 * r, 2 * r);
		gc.setForeground(fore);
	}
}
