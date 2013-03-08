package geom.image;

public class Filter {
	
	
	public static double[][] meanFilterRestoreZero(double[][] arr, int count) {
		double[][] filteredFieldArr = null;
		for (int i = 0; i < count; i++) {
			filteredFieldArr = Filter.meanFilter(arr);
			Filter.restoreZeroRects(filteredFieldArr, arr);
			arr = filteredFieldArr;
		}
		return filteredFieldArr;
	}

	
	/**
	 * we dont add negative values to the filter result 
	 * @param arr
	 * @return
	 */
	public static double[][] meanFilter(double[][] arr) {

		double[][] result = new double[arr.length][arr[0].length];

		copyImpossibleTuples(result, arr);

		int[][] meanFilter = new int[][] { { 1, 1, 1 }, { 1, 1, 1 },
				{ 1, 1, 1 } };

		int[][] filter = meanFilter;
		int filterExtension = (filter.length - 1) / 2;

		double sum = 0;
		double value = 0;
		int invalidCount = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j] >= 0) {
					sum = 0;
					value = getValue(arr, meanFilter, i, j, -1, -1,
							filterExtension);
					if (value < 0)
						invalidCount++;
					else
						sum += value;
					//
					value = getValue(arr, meanFilter, i, j, 0, -1,
							filterExtension);
					if (value < 0)
						invalidCount++;
					else
						sum += value;
					//
					value = getValue(arr, meanFilter, i, j, +1, -1,
							filterExtension);
					if (value < 0)
						invalidCount++;
					else
						sum += value;
					//
					// ///////////////////////////////////////////////////////////////
					//
					value = getValue(arr, meanFilter, i, j, -1, 0,
							filterExtension);
					if (value < 0)
						invalidCount++;
					else
						sum += value;
					//
					value = getValue(arr, meanFilter, i, j, 0, 0,
							filterExtension);
					if (value < 0)
						invalidCount++;
					else
						sum += value;
					//
					value = getValue(arr, meanFilter, i, j, +1, 0,
							filterExtension);
					if (value < 0)
						invalidCount++;
					else
						sum += value;
					//
					// ///////////////////////////////////////////////////////////////
					//
					value = getValue(arr, meanFilter, i, j, -1, +1,
							filterExtension);
					if (value < 0)
						invalidCount++;
					else
						sum += value;
					//
					value = getValue(arr, meanFilter, i, j, 0, +1,
							filterExtension);
					if (value < 0)
						invalidCount++;
					else
						sum += value;
					//
					value = getValue(arr, meanFilter, i, j, +1, +1,
							filterExtension);
					if (value < 0)
						invalidCount++;
					else
						sum += value;

					result[i][j] = sum / (filter.length * filter.length);
				}
			}
		}
		return result;
	}

	private static void copyImpossibleTuples(double[][] result, double[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j] < 0) {
					result[i][j] = arr[i][j];
				}
			}
		}
	}

	/**
	 * multiplies array by filter
	 * @param filterExtension
	 *            1: index + its adjacent indices
	 * @return
	 */
	private static double getValue(double[][] arr, int[][] filter, int i,
			int j, int iDisplacement, int jDisplacement, int filterExtension) {
		if (isValid2DIndex(arr, i + iDisplacement, j + jDisplacement))
			return filter[iDisplacement + filterExtension][jDisplacement
					+ filterExtension]
					* arr[i + iDisplacement][j + jDisplacement];
		else
			return -1;
	}

	private static boolean isValid2DIndex(double[][] arr, int i, int j) {
		return i >= 0 && j >= 0 && i < arr.length && j < arr[0].length;
	}
	
	
	/**
	 * preserve those rects which we have estimate a probability of zero to
	 * zero. That is after filter they might get some great value that is not
	 * desirable.
	 * 
	 * @param result
	 * @param main
	 */
	public static void restoreZeroRects(double[][] result, double[][] main) {
		for (int i = 0; i < main.length; i++) {
			for (int j = 0; j < main[0].length; j++) {
				if (main[i][j] == 0)
					result[i][j] = 0;
			}
		}
	}
}
