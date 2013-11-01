package org.gismarzf;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Model {

	private List<Arc> conjunctiveArcs = new ArrayList<Arc>();
	private List<Arc> disjunctiveArcs = new ArrayList<Arc>();

	private List<Operation> operations;

	// log4j, new one automatically adds class name
	private static Logger logger = LogManager.getLogger();

	private int jobCount, machineCount, opxJob, opxMachine;

	public int getJobCount() {
		return jobCount;
	}

	public void setJobCount(int jobCount) {
		this.jobCount = jobCount;
	}

	public int getMachineCount() {
		return machineCount;
	}

	public void setMachineCount(int machineCount) {
		this.machineCount = machineCount;
	}

	public int getOpxJob() {
		return opxJob;
	}

	public void setOpxJob(int opxJob) {
		this.opxJob = opxJob;
	}

	public int getOpxMachine() {
		return opxMachine;
	}

	public void setOpxMachine(int opxMachine) {
		this.opxMachine = opxMachine;
	}

	public List<Arc> getConjunctiveArcs() {
		return conjunctiveArcs;
	}

	public void setConjunctiveArcs(List<Arc> conjunctiveArcs) {
		this.conjunctiveArcs = conjunctiveArcs;
	}

	public List<Arc> getDisjunctiveArcs() {
		return disjunctiveArcs;
	}

	public void setDisjunctiveArcs(List<Arc> disjunctiveArcs) {
		this.disjunctiveArcs = disjunctiveArcs;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	public void createArcs() {
		// TODO Auto-generated method stub
		// jobs
		for (int i = 0, index = 0; i < jobCount; i++) {
			for (int j = i * 10; j < (i + 1) * 10; j = j + 2, index++) {
				Arc a = new Arc();
				a.setStart(operations.get(j));
				a.setEnd(operations.get(j + 1));
				a.setDirection(true);
				a.setIndex(index);
				logger.trace("Nuevo arco conyuntivo (" + a.getIndex()
						+ "), inicio: " + operations.get(j).getIndex()
						+ ", fin: " + operations.get(j + 1).getIndex());

				// gotta also add the arc to operations
				operations.get(j).getConjunctiveArcs().add(a);
				operations.get(j + 1).getConjunctiveArcs().add(a);

				conjunctiveArcs.add(a);
			}
		}

		// machines
		for (int i = 0, index = 0; i < machineCount; i++) {
			List<Operation> ops = returnOpsForMachine(i + 1);
			// machine indexes start at 1

			for (int j = 0; j < opxMachine; j++) {

				for (int j2 = 1; j2 < opxMachine - j; j2++, index++) {
					Arc a = new Arc();

					a.setStart(ops.get(j));
					a.setEnd(ops.get(j + j2));
					a.setDirection(true);
					a.setIndex(index);

					logger.trace("Nuevo arco disyuntivo (" + a.getIndex()
							+ "), inicio: " + ops.get(j).getIndex() + ", fin: "
							+ ops.get(j + j2).getIndex());

					ops.get(j).getDisjunctiveArcs().add(a);
					ops.get(j + j2).getDisjunctiveArcs().add(a);

					disjunctiveArcs.add(a);

				}
			}
		}

	}

	private List<Operation> returnOpsForMachine(int m) {
		List<Operation> ops = new ArrayList<Operation>();

		for (Operation o : operations) {
			if (o.getMachineNo() == m) {
				ops.add(o);
			}
		}

		return ops;
	}

}
