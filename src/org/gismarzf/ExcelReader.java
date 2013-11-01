package org.gismarzf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExcelReader {

	private int maxOperations;

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

	private List<Operation> operations = new ArrayList<Operation>();

	public ExcelReader() {
		// TODO Auto-generated constructor stub
	}

	public void getInput() {

		// read excel file with input table
		try {
			Workbook wb = WorkbookFactory.create(new File("src/resources/input.xlsx"));
			parseSheet((XSSFSheet) wb.getSheet("DATA"));

		} catch (InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void parseSheet(XSSFSheet sheet) {

		int index, jobNo, machineNo, length;

		// + 1 b/c the indexes are fucked up apparently
		for (int i = 1; i < maxOperations + 1; i++) {
			Operation op = new Operation();

			// cells start at 0 (weird?)
			index = (int) sheet.getRow(i).getCell(0).getNumericCellValue();
			jobNo = (int) sheet.getRow(i).getCell(1).getNumericCellValue();
			machineNo = (int) sheet.getRow(i).getCell(2).getNumericCellValue();
			length = (int) sheet.getRow(i).getCell(3).getNumericCellValue();

			op.setIndex(index);
			op.setJobNo(jobNo);
			op.setMachineNo(machineNo);
			op.setLength(length);

			operations.add(op);
		}

	}
}
