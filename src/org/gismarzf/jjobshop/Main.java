package org.gismarzf.jjobshop;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

	private static Logger logger = LogManager.getLogger();

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		System.out.println("Max items en lista tabu:");
		int maxTabu = in.nextInt();

		System.out.println("Max tiempo (minutos):");
		long maxTiempo = in.nextInt() * 60 * 1000; // convert minutes to seconds

		logger.info("Leer excel...");
		Excel er = new Excel(300);

		logger.info("Obteniendo grafo disyuntivo...");
		Solution s = new Solution(30, er.getOperations());
		Solution best = new Solution(s);

		TabuList tl = new TabuList(maxTabu);

		logger.info("Empezando busqueda...");

		long start = System.currentTimeMillis();

		while (((System.currentTimeMillis() - start) < maxTiempo)) {

			Neighbourhood nbh =
				new Neighbourhood(
					s,
					s.getCriticalPath(),
					tl,
					true);
			s = nbh.solutionWithBestFunctional(best, tl);

			if (s == null) {
				logger
					.error("No puedo generar mas vecindarios"
						+ " porque todos posibles vecinos estan tabu!!!");
				logger.error("Lista tabu tiene "
					+ tl.getSize()
					+ " items.");
				break;
			}

			if (s.getFunctional() < best.getFunctional())
				best = new Solution(s);

			logger
				.info("**** Restan: "
					+ ((maxTiempo - (System
						.currentTimeMillis() - start)) / (60 * 1000))
					+ " minutos..");
			logger.info("Este funcional: "
				+ s.getFunctional());
			logger.info("Mejor funcional: "
				+ best.getFunctional());

			tl.add(s.getMove());
		}

		er.export(best.getArcDirections(), best
			.getProgramming(), best.getFunctional(), tl
			.getMaxSize(), best.getCaminoCritico());

		logger
			.info("Terminado despues de "
				+ ((System.currentTimeMillis() - start) / (60 * 1000))
				+ " minutos con max. lista tabu de "
				+ maxTabu);
		logger.info("Funcional: " + best.getFunctional());

		in.next();
		in.close();
	}
}
