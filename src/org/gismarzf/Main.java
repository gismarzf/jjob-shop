package org.gismarzf;


public class Main {

	public static void main(String[] args) {

		ExcelReader er = new ExcelReader(300);
		Model m = new Model(3, er.getOperations());
		m.createGraph();

	}

}
