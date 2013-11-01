package org.gismarzf;

import java.util.ArrayList;
import java.util.List;

public class Operation {

	private int index, jobNo, machineNo, length;
	private List<Arc> conjunctiveArcs = new ArrayList<Arc>();
	private List<Arc> disjunctiveArcs = new ArrayList<Arc>();

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<Arc> getConjunctiveArcs() {
		return conjunctiveArcs;
	}

	public void setConjunctiveArcs(List<Arc> conyuntiveArcs) {
		this.conjunctiveArcs = conyuntiveArcs;
	}

	public List<Arc> getDisjunctiveArcs() {
		return disjunctiveArcs;
	}

	public void setDisjunctiveArcs(List<Arc> disyuntiveArcs) {
		this.disjunctiveArcs = disyuntiveArcs;
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

}
