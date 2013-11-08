package org.gismarzf.jjobshop;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements LogAble {

	private static final int maxOperations = 300;
	private static final int maxJobs = 30;
	private static Logger logger = LogManager.getLogger();

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		System.out.println("Max items en lista tabu:");
		int maxTabu = in.nextInt();

		System.out.println("Max tiempo (minutos):");
		long maxTiempo = in.nextInt() * 60 * 1000; // convert minutes to
													// milliseconds

		logger.info("Leer excel...");
		Excel er = new Excel(maxOperations);

		Optimizer opt = new Optimizer();
		opt.setInitialSolution(maxJobs, er.getOperations());
		opt.setAsCriticalNeighbourhood();
		opt.setAsTabuList(maxTabu);

		logger.info("Empezando busqueda...");
		long start = System.currentTimeMillis();
		while (((System.currentTimeMillis() - start) < maxTiempo)) {

			opt.calculateNeighbourhood();
			opt.calculateNextSolution();

			logger
				.info("****"
					+ " Tamaño lista tabu: "
					+ opt.getTl().getSize()
					+ ", restan: "
					+ ((maxTiempo - (System
						.currentTimeMillis() - start)) / (60 * 1000))
					+ " minutos..");

			logger.info("Este funcional: "
				+ opt.getThisSolution().getFunctional()
				+ " (Move: "
				+ opt.getThisSolution().getMove() + ")");

			logger.info("Mejor funcional: "
				+ opt.getBestSolution().getFunctional());
		}

		er.export(opt.getBestSolution(), opt.getTl());

		logger
			.info("Terminado despues de "
				+ ((System.currentTimeMillis() - start) / (60 * 1000))
				+ " minutos con max. lista tabu de "
				+ maxTabu);

		logger.info("Funcional: "
			+ opt.getBestSolution().getFunctional());

		in.next();
		in.close();
	}
}
