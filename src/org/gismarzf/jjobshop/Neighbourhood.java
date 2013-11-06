package org.gismarzf.jjobshop;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gismarzf.jjobshop.Arc.Type;

import com.google.common.collect.Lists;

public class Neighbourhood {

	public Neighbourhood(Solution currentSolution,
		List<Arc> edges, TabuList tabuList,
		boolean isCritical) {

		for (Arc e : edges) {

			if (e.getType() == Type.Disjunctive) {
				Solution nextSolution = new Solution(
					currentSolution);
				nextSolution.switchDirectionOfArc(e);

				if (isCritical) {
					try {
						assert !nextSolution.hasError();
						nextSolution
							.calculateCriticalPath();
						neighbourhood.add(nextSolution);
					} catch (AssertionError ae) {

						logger
							.error("Cambiando la direccion de un arco de camino "
								+ "critico genero una solucion invalida..");
					}
				} else {
					if (!nextSolution.hasError()) {
						nextSolution
							.calculateCriticalPath();
						neighbourhood.add(nextSolution);
					}
				}
			}
		}
	}

	// log4j, new one automatically adds class name
	private Logger logger = LogManager.getLogger();
	private List<Solution> neighbourhood = Lists
		.newArrayList();

	public Solution solutionWithMinErrors() {
		int bestErrors = Integer.MAX_VALUE;
		Solution bestSol = null;

		for (Solution sol : neighbourhood) {
			if (sol.getErrors() < bestErrors) {
				bestSol = sol;
				bestErrors = sol.getErrors();
			}
		}

		return bestSol;
	}

	public Solution solutionWithBestFunctional(
		Solution best, TabuList tl) {
		double bestFunctional = Integer.MAX_VALUE;
		Solution bestSol = null;

		for (Solution sol : neighbourhood) {
			if ((sol.getFunctional() < bestFunctional && !tl
				.isTabu(sol.getMove()))
				|| (sol.getFunctional() < best
					.getFunctional() && tl.isTabu(sol
					.getMove()))) {
				bestSol = sol;
				bestFunctional = sol.getFunctional();
			}
		}

		return bestSol;
	}

}
