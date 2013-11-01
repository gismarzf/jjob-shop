package org.gismarzf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
	// log4j, new one automatically adds class name
	private static Logger logger = LogManager.getLogger();

	public static void main(String[] args) {

		ExcelReader in = new ExcelReader();
		in.setMaxOperations(300);
		logger.trace("Leer excel...");
		in.getInput();
		logger.trace("****");
		Model m = new Model();
		m.setJobCount(3);
		m.setMachineCount(10);
		m.setOpxJob(10);
		m.setOpxMachine(3);
		m.setOperations(in.getOperations());
		logger.trace("Obteniendo arcos...");
		m.createArcs();
		logger.trace("****");

	}
}
