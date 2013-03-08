package gui;

import guiInterface.RGB;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class SWTUIConstants {

	public static Color getSWTColor(RGB rgb) {
		return new Color(Display.getCurrent(), rgb.red, rgb.green, rgb.blue);
	}

	// public static Color MS_ANCHOR_COLOR = new Color(Display.getCurrent(),
	// GUIConstants.ANCHOR_COLOR.red, GUIConstants.ANCHOR_COLOR.green,
	// GUIConstants.ANCHOR_COLOR.blue);
	//
	// public static Color MS_LOST_NODE_COLOR = new Color(Display.getCurrent(),
	// GUIConstants.LOST_NODE_COLOR.red,
	// GUIConstants.LOST_NODE_COLOR.green,
	// GUIConstants.LOST_NODE_COLOR.blue);
	//
	// public static Color MS_EDGE_COLOR = new Color(Display.getCurrent(),
	// GUIConstants.EDGE_COLOR.red, GUIConstants.EDGE_COLOR.green,
	// GUIConstants.EDGE_COLOR.blue);
	//
	// public static Color MS_RECT_COLOR = new Color(Display.getCurrent(),
	// GUIConstants.RECT_COLOR.red, GUIConstants.RECT_COLOR.green,
	// GUIConstants.RECT_COLOR.blue);
}
