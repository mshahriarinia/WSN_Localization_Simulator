package node.tools;

import node.ICycler;

/**
 * Stores a data object and a time stamp. The public constructor stamps current
 * cycle number. when the data was observed originally.
 */
public class TimedData implements Cloneable {

	private Object data;

	private int cycleNo;

	/**
	 * stamps current cycle number
	 * 
	 * @param iCycler
	 * @param data
	 */
	public TimedData(ICycler iCycler, Object data) {
		setData(data);
		setCycleNo(iCycler.getCycleNo());
	}

	private TimedData(int cycleNo, Object data) {
		setData(data);
		setCycleNo(cycleNo);
	}

	private void setCycleNo(int cycleNo) {
		this.cycleNo = cycleNo;
	}

	public int getCycleNo() {
		return cycleNo;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * Returns a new object with the same cycle number but the data field set to
	 * null.
	 */
	@Override
	public TimedData clone() {
		return new TimedData(getCycleNo(), null);
	}

	@Override
	public String toString() {
		return data + "( t = " + cycleNo + " )";
	}
}
