package reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import control.ZXControl;

public class MakeTable {

	public static void main(String[] args) {
		MakeTable m = new MakeTable();

		double[] d = {};
		double[][] data = { { 7, 8, 9 }, { 1, 2, 3 }, { 4, 5, 6 }, { 1, 8, 11 } };

		m.getTable(data);
	}

	// 3 column data
	public static double[][] getTable(double[][] data) {

		double[] xSorted = getX(data);
		double[] ySorted = getY(data);

		Arrays.sort(xSorted);
		Arrays.sort(ySorted);

		double[] x = removeduplicates(xSorted);
		double[] y = removeduplicates(ySorted);

		Arrays.sort(x);
		Arrays.sort(y);

		double[][] xy = new double[x.length][y.length];

		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < y.length; j++) {
				xy[i][j] = -1;
				double xVal = x[i];
				double yVal = y[j];

				xy[i][j] = getVal(xVal, yVal, data);
			}
		}

		PrintStream ps = null;
		try {
			ps = System.out;
			
			ps.print("\t");
			for(int i = 0; i < x.length; i++){
				ps.print(x[i]);
				ps.print("\t");
			}
			
			ps.println();

			for (int j = 0; j < xy[0].length; j++) {
				ps.print(y[j]);
				ps.print("\t");
				for (int i = 0; i < xy.length; i++) {

					ps.print(xy[i][j]);
					ps.print("\t");
				}
				ps.println();
			}
			
//			for(int i = 0; i < y.length; i++){
//				ps.println(y[i]);
//			}
			ps.println();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static private double getVal(double xVal, double yVal, double[][] data) {

		for (int i = 0; i < data.length; i++) {

			if (data[i][0] == xVal && data[i][1] == yVal)
				return data[i][2];
		}

		return -1;
	}

	static double[] getX(double[][] data) {
		double[] x = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			x[i] = data[i][0];
		}
		return x;
	}

	static double[] getY(double[][] data) {
		double[] y = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			y[i] = data[i][1];
		}
		return y;
	}

	static double[] removeduplicates(double[] arr) {
		Arrays.sort(arr);
		List<Double> list = new ArrayList<Double>();
		for (int i = 0; i < arr.length; i++) {
			if (!list.contains(arr[i]))
				list.add(arr[i]);
		}
		double[] newArr = new double[list.size()];
		for (int i = 0; i < newArr.length; i++) {
			newArr[i] = list.get(i);
		}
		return newArr;
	}

}
