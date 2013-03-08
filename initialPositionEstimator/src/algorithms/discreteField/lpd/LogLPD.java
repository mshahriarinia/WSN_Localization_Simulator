package algorithms.discreteField.lpd;

import geom.checkerBoard.FieldCheckerBoard;

import java.io.FileOutputStream;
import java.io.PrintStream;

import node.Node;
import control.ZXControl;

public class LogLPD {

	// introduce two arrays one for an incremental case and other for log-based
	// one
	public static void LOG_AREA(Node node, FieldCheckerBoard fieldCB,
//			int[][] area, 
			double[][] areaLog) {
		// if (node.isLogged()) {
		FileOutputStream exeFile = null;
		PrintStream ps = null;
		try {
			exeFile = new FileOutputStream(ZXControl.OUTPUT_DIRECTORY + "/Nodes - LPD/"
					+ "LPD node" + node.getId().getValue() + " .txt");
			ps = new PrintStream(exeFile);

			ps.print(node);
			int xIndex = fieldCB.getContainingRectangleXIndex(node.getCenter()
					.getX());
			int yIndex = fieldCB.getContainingRectangleYIndex(node.getCenter()
					.getY());
			ps.print("          Rectangle[");
			ps.print(xIndex);
			ps.print(", ");
			ps.print(yIndex);
			ps.println("]");

//			for (int j = 0; j < area.length; j++) {
//				for (int i = 0; i < area.length; i++) {
//
//					if (j == yIndex && i == xIndex)
//						ps.print("X");
//
//					if (area[i][j] == -1)
//						ps.print("a\t");
//					else
//						ps.print(Math.round(area[i][j]) + "\t");
//				}
//				ps.println();
//			}
			ps.println();
			ps.println();
			for (int j = 0; j < areaLog.length; j++) {
				for (int i = 0; i < areaLog.length; i++) {
					if (j == yIndex && i == xIndex)
						ps.print("X");

					if (areaLog[i][j] == -1)
						ps.print("a\t");
					else
						ps
								.print(ZXControl.doubleToString(areaLog[i][j])
										+ "\t");
				}
				ps.println();
			}
			ps.close();
			exeFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
	}

	// private void LOG_Exec(double[][] area, int tabs) {
	// if (node.isLogged()) {
	// for (int j = 0; j < area.length; j++) {
	// printExecTabs(tabs);
	// for (int i = 0; i < area.length; i++) {
	// // ps.print(ZXControl.doubleToString(area[i][j]) +
	// // "\t");
	// if (area[i][j] == -1) {
	// lpdExecLogPrint("a\t");
	// } else {
	// lpdExecLogPrint((int) area[i][j] + "\t");
	// }
	// }
	// lpdExecLogPrintln(0, "");
	// }
	// }
	// lpdExecLogPrintln(0, "");
	// }
	//
	// static FileOutputStream exeFile = null;
	// static PrintStream ps = null;
	// static {
	// try {
	// exeFile = new FileOutputStream(ZXControl.OUTPUT_DIRECTORY
	// + "LPD ExecLog" + ".txt");
	// ps = new PrintStream(exeFile);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// void printExecTabs(int c) {
	// for (int i = 0; i < c; i++)
	// ps.print("\t");
	// }
	//
	// void lpdExecLogPrintln(int tabs, Object o) {
	// // printExecTabs(tabs);
	// // ps.println(o);
	// }
	//
	// void lpdExecLogPrint(Object o) {
	// // ps.print(o);
	// }

}
