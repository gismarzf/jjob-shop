package org.gismarzf;

public class Main {

	public static void main(String[] args) {

		ExcelReader in = new ExcelReader();
		in.setMaxOperations(300);
		in.getInput();

		Model m = new Model();
		m.setJobCount(3);
		m.setMachineCount(10);
		m.setOpxJob(10);
		m.setOpxMachine(3);
		m.setOperations(in.getOperations());
		m.createArcs();

	}

}
