package org.gismarzf.jjobshop;

public class Operation {

	private int index, jobNo, machineNo, length;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getJobNo() {
		return jobNo;
	}

	public int getLength() {
		return length;
	}

	public int getMachineNo() {
		return machineNo;
	}

	public void setJobNo(int jobNo) {
		this.jobNo = jobNo;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setMachineNo(int machineNo) {
		this.machineNo = machineNo;
	}

	public String toString() {
		return Integer.toString(index);
	}
}
