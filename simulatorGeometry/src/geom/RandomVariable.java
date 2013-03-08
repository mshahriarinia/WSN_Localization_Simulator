package geom;

import java.util.ArrayList;
import java.util.List;

public abstract class RandomVariable<E> {

	private static final int customConfidenceMultiplier = 3;

	private List<E> listValues;

	private double average;

	private double variance;

	private double standardDeviation;

	public RandomVariable(List<E> listValues) {
		setListValues(listValues);
	}

	public void setListValues(List<E> listValues) {
		this.listValues = listValues;
		initAverage();
		initVariance();
		initStandardDeviation();
	}

	private void initStandardDeviation() {
		standardDeviation = Math.sqrt(variance);
	}

	private void initVariance() {
		variance = 0;
		double value;
		for (E o : listValues) {
			value = getValue(o);
			variance += (value - average) * (value - average);
		}
		variance /= listValues.size();
	}

	private void initAverage() {
		average = 0;
		double value;
		for (E o : listValues) {
			value = getValue(o);
			average += value;
		}
		average /= listValues.size();
	}

	private List<E> getMax95PercentConfidenceList() {
		double min95PercentConfidence = getMin95PercentConfidence();
		List<E> tempList = new ArrayList<E>();
		for (E e : listValues) {
			if (getValue(e) >= min95PercentConfidence)
				tempList.add(e);
		}
		return tempList;
	}

	/**
	 * The threshold that we apply our CustomConfidenceInterval in getting data
	 * 
	 * @return
	 */
	private double getCustomConfidenceIntervalThreshold() {
		return average / customConfidenceMultiplier;
	}

	/**
	 * if difference in data is negligible return getMin95PercentConfidence, else return those higher than 1/3 avg
	 * @return
	 */
	public double getCustomConfidenceInterval() {
		if (standardDeviation > getCustomConfidenceIntervalThreshold()) {
			return average - getCustomConfidenceIntervalThreshold();
		} else
			return getMin95PercentConfidence();
	}

	/**
	 * We might have two clusters with values 10,15. Here both are possible
	 * locations for a node.<br>
	 * <br>
	 * We might on the other hand have 82,8,8,14,5. Here non are acceptable
	 * locations but 82. <br>
	 * <br>
	 * In condition one if we want to consider 10 we have to use all but min95
	 * values; but in condition two. we can't either use 68 or 95. since the
	 * difference is because that stddev is much greater than average, we
	 * differentiate the two situations. that if for example stddev was greater
	 * than a fifth of average. then we only consider average minus its fifth
	 * (heuristically).
	 * 
	 * @return
	 */
	public List<E> getCustomConfidenceList() {
		if (standardDeviation > getCustomConfidenceIntervalThreshold()) {
			double minAcceptableValue = average
					- getCustomConfidenceIntervalThreshold();

			List<E> tempList = new ArrayList<E>();
			for (E e : listValues) {
				if (getValue(e) >= minAcceptableValue)
					tempList.add(e);
			}
			return tempList;
		} else {
			return getMax95PercentConfidenceList();
		}

	}

	private double getMin68PercentConfidence() {
		return average - standardDeviation;
	}

	private double getMax68PercentConfidence() {
		return average + standardDeviation;
	}

	private double getMin95PercentConfidence() {
		return average - 2 * standardDeviation;
	}

	private double getMax95PercentConfidence() {
		return average + 2 * standardDeviation;
	}

	public abstract double getValue(E randomEvent);

}
