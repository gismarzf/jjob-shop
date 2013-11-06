package org.gismarzf.jjobshop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
// this is the new one
// this is the new one

public class Excel {

	public Excel(int maxOperations) {
		setMaxOperations(maxOperations);
		getInput();

	}

	private int maxOperations;
	private List<Operation> operations = new ArrayList<Operation>();

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger();

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

		// read excel file with input table
		try {

			Workbook wb = WorkbookFactory.create(getClass()
				.getResourceAsStream("/input.xlsm"));

			XSSFSheet sheet = (XSSFSheet) wb
				.getSheet("INPUT");
			parseSheet(sheet);

		} catch (IOException | InvalidFormatException e) {
			e.printStackTrace();
		}

	}

	private void parseSheet(XSSFSheet sheet) {

		int index, jobNo, machineNo, length;

		// row 0 is headers, so gotta move by 1
		for (int i = 1; i < maxOperations + 1; i++) {
			Operation op = new Operation();

			index = (int) sheet.getRow(i).getCell(0)
				.getNumericCellValue();
			jobNo = (int) sheet.getRow(i).getCell(1)
				.getNumericCellValue();
			machineNo = (int) sheet.getRow(i).getCell(2)
				.getNumericCellValue();
			length = (int) sheet.getRow(i).getCell(3)
				.getNumericCellValue();

			// set fields
			op.setIndex(index - 1); // because the xls sheet starts at 1
			op.setJobNo(jobNo);
			op.setMachineNo(machineNo);
			op.setLength(length);

			// add to arraylist
			operations.add(op);

		}

	}

	public void export(int[] arcDirections,
		double[][] programming, double functional,
		int maxTabuList, int[][] caminoCritico) {

		try {

			File file = File.createTempFile(
				((int) functional) + "-output", ".xlsm",
				new File("."));

			Workbook wb = WorkbookFactory.create(getClass()
				.getResourceAsStream("/input.xlsm"));

			XSSFSheet sheet = (XSSFSheet) wb
				.getSheet("OUTPUT");

			FileOutputStream out = new FileOutputStream(
				file);

			sheet.createRow(0);
			sheet.getRow(0).createCell(0).setCellValue(
				"Operacion:");
			sheet.getRow(0).createCell(1).setCellValue(
				"Inicio:");
			sheet.getRow(0).createCell(2).setCellValue(
				"Fin:");

			sheet.getRow(0).createCell(4).setCellValue(
				"Arco");
			sheet.getRow(0).createCell(5).setCellValue(
				"Direccion:");

			sheet.getRow(0).createCell(7).setCellValue(
				"Funcional:");
			sheet.getRow(0).createCell(8).setCellValue(
				functional);

			sheet.getRow(0).createCell(9).setCellValue(
				"Max Lista Tabu:");
			sheet.getRow(0).createCell(10).setCellValue(
				(double) maxTabuList);

			sheet.getRow(0).createCell(11).setCellValue(
				"Camino Critico:");

			for (int i = 0; i < arcDirections.length; i++) {
				sheet.createRow(i + 1);
			}

			for (int i = 0; i < programming.length; i++) {
				sheet.getRow(i + 1).createCell(0)
					.setCellValue(i);
				sheet.getRow(i + 1).createCell(1)
					.setCellValue(programming[i][0]);
				sheet.getRow(i + 1).createCell(2)
					.setCellValue(programming[i][1]);
			}

			for (int i = 0; i < arcDirections.length; i++) {

				sheet.getRow(i + 1).createCell(4)
					.setCellValue((double) i);
				sheet
					.getRow(i + 1)
					.createCell(5)
					.setCellValue((double) arcDirections[i]);
			}

			// operation: source( not included)
			sheet.getRow(1).createCell(11).setCellValue(-1);
			for (int i = 0; i < caminoCritico.length; i++) {
				sheet.getRow(i + 2).createCell(11)
					.setCellValue(caminoCritico[i][0]);
				sheet.getRow(i + 1).createCell(12)
					.setCellValue(caminoCritico[i][1]);
			}

			wb.write(out);

			out.close();

		} catch (IOException | InvalidFormatException e) {
			e.printStackTrace();
		}
	}
}
