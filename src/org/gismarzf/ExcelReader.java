package org.gismarzf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager; // this is the new one
import org.apache.logging.log4j.Logger; // this is the new one
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExcelReader {

	public ExcelReader(int maxOperations) {
		setMaxOperations(maxOperations);
		getInput();

	}

	// log4j, new one automatically adds class name
	private static Logger logger = LogManager.getLogger();

	private int maxOperations;
	private List<Operation> operations = new ArrayList<Operation>();

	public int getMaxOperations() {

		return maxOperations;
	}

	public void setMaxOperations(int maxOperations) {
		this.maxOperations = maxOperations;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	public void getInput() {

		logger.info("Leer excel...");

		// read excel file with input table
		try {
			Workbook wb = WorkbookFactory.create(new File(
					"src/resources/input.xlsx"));
			parseSheet((XSSFSheet) wb.getSheet("DATA"));

		} catch (IOException | InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("****");
	}

	private void parseSheet(XSSFSheet sheet) {

		int index, jobNo, machineNo, length;

		// row 0 is headers, so gotta move by 1
		for (int i = 1; i < maxOperations + 1; i++) {
			Operation op = new Operation();

			index = (int) sheet.getRow(i).getCell(0).getNumericCellValue();
			jobNo = (int) sheet.getRow(i).getCell(1).getNumericCellValue();
			machineNo = (int) sheet.getRow(i).getCell(2).getNumericCellValue();
			length = (int) sheet.getRow(i).getCell(3).getNumericCellValue();

			// set fields
			op.setIndex(index - 1); // because the xls sheet starts at 1
			op.setJobNo(jobNo);
			op.setMachineNo(machineNo);
			op.setLength(length);

			// add to arraylist
			operations.add(op);

			// log
			logger.trace("Agregar operacion: No. " + op.getIndex()
					+ ", Trabajo: " + op.getJobNo() + ", Maquina: "
					+ op.getMachineNo() + ", Duracion: " + op.getLength()
					+ " min.");

		}

	}
}
