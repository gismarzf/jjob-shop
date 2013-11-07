package org.gismarzf.jjobshop;

import java.util.List;

import com.google.common.collect.Lists;

public class Neighbourhood {

	private List<Solution> neighbourhood = Lists
		.newArrayList();

	private NeighbourhoodCreateBehaviour newNeighbourhood;

	public static Neighbourhood newCriticalNeighbourhood(
		Solution currentSolution, TabuList tabuList) {

		Neighbourhood nbh = new Neighbourhood();

		nbh.newNeighbourhood =
			new NeighbourhoodCreateCriticalBehaviour();
		nbh.neighbourhood =
			nbh.newNeighbourhood.get(
				currentSolution, tabuList);

		return nbh;
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

}
