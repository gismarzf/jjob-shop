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

	private int maxOperations;

	// log4j, new one automatically adds class name
	private static Logger logger = LogManager.getLogger();

	private List<Operation> operations = new ArrayList<Operation>();

	public Excel(int maxOperations) {
		setMaxOperations(maxOperations);
		getInput();

	}

	public void export(Optimizer opt, MyLogger log) {

		int[] arcDirections =
				opt.getBestSolution().getArcDirections();
		double functional = opt.getBestSolution().getFunctional();
		double[][] programming =
				opt.getBestSolution().getProgramming();
		int[][] caminoCritico =
				opt.getBestSolution().getCaminoCritico();

		String metaheuristica = opt.getChooseSolution().toString();

		try {

			File file =
					File.createTempFile(
							"z="
									+ (int) functional
									+ " t="
									+ Math.round(opt
											.getTimer()
											.getElapsedMinutes())
									+ " "
									+ opt.getChooseSolution()
									+ "--",
							".xlsm",
							new File("."));

			Workbook wb =
					WorkbookFactory.create(getClass()
							.getResourceAsStream("/input.xlsm"));

			XSSFSheet sheet = (XSSFSheet) wb.getSheet("OUTPUT");

			FileOutputStream out = new FileOutputStream(file);

			for (int i = 0; i < arcDirections.length + 1; i++) {
				sheet.createRow(i);
			}

			sheet
					.getRow(0)
					.createCell(0)
					.setCellValue("Operacion:");
			sheet.getRow(0).createCell(1).setCellValue("Inicio:");
			sheet.getRow(0).createCell(2).setCellValue("Fin:");

			sheet.getRow(0).createCell(4).setCellValue("Arco");
			sheet
					.getRow(0)
					.createCell(5)
					.setCellValue("Direccion:");

			sheet
					.getRow(0)
					.createCell(7)
					.setCellValue("Funcional:");
			sheet.getRow(0).createCell(8).setCellValue(functional);

			sheet
					.getRow(0)
					.createCell(9)
					.setCellValue("Metaheuristica:");
			sheet
					.createRow(1)
					.createCell(10)
					.setCellValue(metaheuristica);

			sheet
					.getRow(0)
					.createCell(11)
					.setCellValue("Camino Critico:");

			for (int i = 0; i < programming.length; i++) {
				sheet.getRow(i + 1).createCell(0).setCellValue(i);
				sheet
						.getRow(i + 1)
						.createCell(1)
						.setCellValue(programming[i][0]);
				sheet
						.getRow(i + 1)
						.createCell(2)
						.setCellValue(programming[i][1]);
			}

			for (int i = 0; i < arcDirections.length; i++) {

				sheet
						.getRow(i + 1)
						.createCell(4)
						.setCellValue((double) i);
				sheet
						.getRow(i + 1)
						.createCell(5)
						.setCellValue((double) arcDirections[i]);
			}

			// operation: source( not included)
			sheet.getRow(1).createCell(11).setCellValue(-1);
			for (int i = 0; i < caminoCritico.length; i++) {
				sheet
						.getRow(i + 2)
						.createCell(11)
						.setCellValue(caminoCritico[i][0]);
				sheet
						.getRow(i + 1)
						.createCell(12)
						.setCellValue(caminoCritico[i][1]);
			}

			sheet = (XSSFSheet) wb.getSheet("LOG");
			for (int i = 0; i < log.getLogList().size(); i++) {
				sheet
						.createRow(i)
						.createCell(0)
						.setCellValue(log.getLogList().get(i)[0]);
				sheet
						.getRow(i)
						.createCell(1)
						.setCellValue(log.getLogList().get(i)[1]);
				sheet
						.getRow(i)
						.createCell(2)
						.setCellValue(Main.FUNCIONAL_OPTIMO);
			}

			wb.write(out);
			out.close();

		} catch (IOException | InvalidFormatException e) {
			logger.error("No pude exportar a excel!");
			logger.error(e);
		}
	}

	public void getInput() {

		// read excel file with input table
		try {

			Workbook wb =
					WorkbookFactory.create(getClass()
							.getResourceAsStream("/input.xlsm"));

			XSSFSheet sheet = (XSSFSheet) wb.getSheet("INPUT");
			parseSheet(sheet);

		} catch (IOException | InvalidFormatException e) {
			logger.error("No pude leer excel!");
			logger.error(e);
		}

	}

	public int getMaxOperations() {

		return maxOperations;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setMaxOperations(int maxOperations) {
		this.maxOperations = maxOperations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	private void parseSheet(XSSFSheet sheet) {

		int index, jobNo, machineNo, length;

		// row 0 is headers, so gotta move by 1
		for (int i = 1; i < maxOperations + 1; i++) {
			Operation op = new Operation();

			index =
					(int) sheet
							.getRow(i)
							.getCell(0)
							.getNumericCellValue();
			jobNo =
					(int) sheet
							.getRow(i)
							.getCell(1)
							.getNumericCellValue();
			machineNo =
					(int) sheet
							.getRow(i)
							.getCell(2)
							.getNumericCellValue();
			length =
					(int) sheet
							.getRow(i)
							.getCell(3)
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
}
