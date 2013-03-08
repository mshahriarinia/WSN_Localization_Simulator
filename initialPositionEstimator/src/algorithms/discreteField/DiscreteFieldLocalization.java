package algorithms.discreteField;

import geom.checkerBoard.FieldCheckerBoard;
import geom.cluster.ClusteredPositionEstimator;
import geom.cluster.ClusteredRegion;
import geom.image.FieldArray;
import geom.image.Filter;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;

import control.ZXControl;

import node.Node;
import simulator.localizable.MSLostNode;

public abstract class DiscreteFieldLocalization {

	private double fieldClusteringHighPassFilterThreshold;

	private FieldCheckerBoard fieldCB;

	private ClusteredPositionEstimator cPE;

	private double[][] fieldArr;

	private MSLostNode mslNode;

	private int possibleRectsCount;

	public DiscreteFieldLocalization(MSLostNode node,
			int MAX_FAILED_ESTIMATIONS_COUNT) {
		this.mslNode = node;
		fieldCB = new FieldCheckerBoard(node.getArea(), node
				.getAverageHopSize(), true);

		fieldArr = new double[fieldCB.getXAxisSlicesNo()][fieldCB
				.getYAxisSlicesNo()];
		initField();

		setPossibleRectsCount(fieldArr);

		if (node.isLogged())
		System.out.print("");
			
		double[][] filteredFieldArr = Filter.meanFilterRestoreZero(fieldArr, 3);
		if (node.isLogged())
			log(fieldArr, filteredFieldArr);
		fieldArr = filteredFieldArr;

		if (node.isLogged())
			System.out.print("");

		setFieldClusteringHighPassFilterThreshold(node, fieldArr);

		cPE = new ClusteredPositionEstimator(fieldCB, node,
				fieldClusteringHighPassFilterThreshold,
				MAX_FAILED_ESTIMATIONS_COUNT) {

			@Override
			public Point2D getCalculatedCenter(ClusteredRegion cR) {
				if (cR == null)
					return null;

				return cR.getWeightedCenter();
			}

			@Override
			public double getValue(int xIndex, int yIndex) {
				return fieldArr[xIndex][yIndex];
			}

			@Override
			public double getValue(Rectangle2D rect) {
				Point2D rectIndex = fieldCB.getRectIndex(rect);
				return fieldArr[(int) rectIndex.getX()][(int) rectIndex.getY()];
			}
		};

		System.out.print("mslNode " + node.getId().getValue()
				+ " fieldClusteringHighPassFilterThreshold = "
				+ fieldClusteringHighPassFilterThreshold + "\t");
	}

	private void initField() {
		fieldArr = new double[fieldCB.getXAxisSlicesNo()][fieldCB
				.getYAxisSlicesNo()];
		FieldArray.setImpossibleAreas(fieldCB, mslNode, fieldArr);
		if(getNode().isLogged())
			System.out.print("");
		fillFieldMatrix();
	}

	public double[][] getFieldArr() {
		return fieldArr;
	}

	public FieldCheckerBoard getFieldCB() {
		return fieldCB;
	}

	public Point2D getEstimate() {
		return cPE.getNodePosition();
	}

	public ClusteredPositionEstimator getCPE() {
		return cPE;
	}

	public MSLostNode getNode() {
		return mslNode;
	}

	public void setFieldClusteringHighPassFilterThreshold(
			double fieldClusteringHighPassFilterThreshold) {
		this.fieldClusteringHighPassFilterThreshold = fieldClusteringHighPassFilterThreshold;
	}

	private void setPossibleRectsCount(double[][] fieldArr) {
		for (int i = 0; i < fieldArr.length; i++) {
			for (int j = 0; j < fieldArr[0].length; j++) {
				if (fieldArr[i][j] != FieldArray.IMPOSSIBLE)
					possibleRectsCount++;
			}
		}
	}

	public int getPossibleRectsCount() {
		return possibleRectsCount;
	}

	protected abstract void fillFieldMatrix();

	protected abstract void setFieldClusteringHighPassFilterThreshold(
			Node node, double[][] fieldArr);

	private void log(double[][] fieldArea, double[][] filteredFieldArea) {
		FileOutputStream exeFile = null;
		PrintStream ps = null;
		NumberFormat numberFormat;
		numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(14);
		numberFormat.setMinimumFractionDigits(14);
		try {
			exeFile = new FileOutputStream(ZXControl.OUTPUT_DIRECTORY
					+ "/Nodes - DPDV/" + "fieldArea" + ".txt");
			ps = new PrintStream(exeFile);

			ps.println("FIELD_area");
			for (int j1 = 0; j1 < fieldArea.length; j1++) {
				for (int j2 = 0; j2 < fieldArea[0].length; j2++) {
					// if (filteredArea[j2][j1] == 0 || filteredArea[j2][j1] ==
					// -1)
					// ps.print('a' + "\t");
					// else
					ps.print(numberFormat.format(fieldArea[j2][j1]) + "\t");
				}
				ps.println();
			}
			ps.println("filteredArea");
			for (int j1 = 0; j1 < filteredFieldArea.length; j1++) {
				for (int j2 = 0; j2 < filteredFieldArea[0].length; j2++) {
					// if (filteredArea[j2][j1] == 0 || filteredArea[j2][j1] ==
					// -1)
					// ps.print('a' + "\t");
					// else
					ps.print(numberFormat.format(filteredFieldArea[j2][j1])
							+ "\t");
				}
				ps.println();
			}
			ps.close();
			exeFile.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
