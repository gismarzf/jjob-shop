package org.gismarzf.jjobshop;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

	public static final double FUNCIONAL_OPTIMO = 1784.0;

	private static final int MAX_OPERATIONS = 300;
	private static final int MAX_JOBS = 30;
	private static Logger logger = LogManager.getLogger();

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		System.out.println("Max tiempo (minutos):");
		long maxTiempo = in.nextInt() * 60 * 1000; // convert minutes to
		// milliseconds

		Optimizer opt = new Optimizer(maxTiempo);

		int meta = -1;
		while ((meta != 0) && (meta != 1)) {
			System.out.println("Metaheuristica? 0 : recocido, 1: tabu");
			meta = in.nextInt();
		}

		if (meta == 0) {

			System.out.println("Temperatura:");
			double temperatura = in.nextDouble();
			System.out.println("Factor descenso temperatura:");
			double factor = in.nextDouble();

			opt.setChooseSolution(new ChooseNextSolutionWithSA(temperatura,
				factor));

		} else if (meta == 1) {

			System.out.println("Max items en lista tabu:");
			int maxTabu = in.nextInt();
			opt.setChooseSolution(new ChooseNextSolutionWithTabuList(
				maxTabu));

		}

		logger.info("Leer excel...");
		Excel er = new Excel(MAX_OPERATIONS);
		logger.info("Empezando busqueda...");

		opt.setInitialSolution(MAX_JOBS, er.getOperations());
		opt.setCreateNeighbourhood(new CreateNeighbourhoodAsCritical());
		opt.getTimer().setStart();

		MyLogger mlogger = new MyLogger(opt);

		while ((opt.getBestSolution().getFunctional() > FUNCIONAL_OPTIMO)
			&& opt.getTimer().getRemaining() > 0) {

			opt.calculateNeighbourhood();
			opt.calculateNextSolution();

		}

		er.export(opt, mlogger);

		logger.info("Terminado despues de "
			+ Math.round(opt.getTimer().getElapsedMinutes())
			+ " minutos con metaheuristica: " + opt.getChooseSolution());

		logger
			.info("Funcional: " + opt.getBestSolution().getFunctional());

		in.next();
		in.close();
	}
}
