package algorithms.discreteField.dpdv;

import geom.checkerBoard.FieldCheckerBoard;
import geom.cluster.ClusteredPositionEstimator;

import java.awt.geom.Point2D;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.List;

import node.type.Anchor;
import simulator.localizable.MSLostNode;
import algorithms.discreteField.dpdv.acp.AreaCirclePercentAccumulation;
import control.ZXControl;

public class LogACPAs {

	public static void LOG(MSLostNode node, FieldCheckerBoard fieldCB,
			ListACPA listACPA, double[][] filteredArea,
			ClusteredPositionEstimator cpe) {

		NumberFormat numberFormat;
		numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(14);
		numberFormat.setMinimumFractionDigits(14);

		FileOutputStream exeFile = null;
		PrintStream ps = null;

		List<Anchor> listAnchors = node.getMemory().getAnchors();

		int xIndex = fieldCB.getContainingRectangleXIndex(node.getCenter()
				.getX());
		int yIndex = fieldCB.getContainingRectangleYIndex(node.getCenter()
				.getY());

		try {
			exeFile = new FileOutputStream(ZXControl.OUTPUT_DIRECTORY
					+ "/Nodes - DPDV/" + node.getId().getValue() + ".txt");
			ps = new PrintStream(exeFile);

			ps.println("FIELD_CLUSTERS");
			cpe.LOG_FIELD_CLUSTERS(ps);

			// print final accumulation
			double[][] arr = new double[fieldCB.getXAxisSlicesNo()][fieldCB
					.getYAxisSlicesNo()];

			int newline = 0;
			int i = 0;
			for (AreaCirclePercentAccumulation acpa : listACPA.getListACPAs()) {
				arr[newline][i] = acpa.getPercent();

				i++;
				if (i >= fieldCB.getXAxisSlicesNo()) {
					i = 0;
					newline++;
				}
			}

			//
			//
			//
			//
			// print filtered area
			ps.println("filteredArea");
			for (int j1 = 0; j1 < filteredArea.length; j1++) {
				for (int j2 = 0; j2 < filteredArea[0].length; j2++) {
					// if (filteredArea[j2][j1] == 0 || filteredArea[j2][j1] ==
					// -1)
					// ps.print('a' + "\t");
					// else
					ps.print(numberFormat.format(filteredArea[j2][j1]) + "\t");
				}
				ps.println();
			}

			//
			//
			//
			//
			// print ACPAs
			ps.println("ACPAs");
			for (int k = 0; k < fieldCB.getYAxisSlicesNo(); k++) {
				for (int m = 0; m < fieldCB.getXAxisSlicesNo(); m++) {
					if (k == yIndex && m == xIndex)
						ps.print("X");
					// if (arr[m][k] == 0)
					// ps.print('a' + "\t");
					// else
					ps.print(numberFormat.format(arr[m][k]) + "\t");
				}
				ps.println();
			}
			ps.println();

			//
			//
			//
			//

			// print ACPs
			for (Anchor anchor : listAnchors) {
				ps.println(anchor);

				fill(arr);

				for (AreaCirclePercentAccumulation acpa : listACPA
						.getListACPAs()) {
					// if (debugRect(acpa, ZXControl.DEBUG_RECT_X,
					// ZXControl.DEBUG_RECT_Y))
					// System.out.print("");

					Point2D p = fieldCB.getRectIndex(acpa.getACP(anchor)
							.getRect());
					arr[(int) p.getX()][(int) p.getY()] = acpa.getACP(anchor)
							.getPercent() * 100;
				}

				// print whole area
				for (int k = 0; k < fieldCB.getYAxisSlicesNo(); k++) {
					for (int m = 0; m < fieldCB.getXAxisSlicesNo(); m++) {
						if (k == yIndex && m == xIndex)
							ps.print("X");
						if (arr[m][k] == 0)
							ps.print('a' + "\t");
						else
							ps.print(numberFormat.format(arr[m][k]) + "\t");
					}
					ps.println();
				}
				ps.println();
			}

			ps.close();
			exeFile.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private static void fill(double[][] arr) {
		for (int j = 0; j < arr.length; j++) {
			for (int j2 = 0; j2 < arr.length; j2++) {
				arr[j][j2] = 0;
			}
		}

	}

	// /**
	// * tests if we have reached a specified rectangle
	// *
	// * @param acpa
	// */
	// public static boolean debugRect(AreaCirclePercentAccumulation acpa,
	// int xIndex, int yIndex) {
	// int sliceLength = ((FieldCheckerBoard) acpa.getNode().getNodeList()
	// .getSimulationInfoContext()).getSliceLength();
	// if (acpa.getNode().isLogged())
	// if (acpa.getRect().getMinX() == xIndex * sliceLength)
	// if (acpa.getRect().getMinY() == yIndex * sliceLength)
	// return true;
	// return false;
	// }
}
