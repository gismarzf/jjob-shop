package org.gismarzf.jjobshop;

public class ChooseNextSolutionWithTabuList
	implements ChooseNextSolutionBehaviour, LogAble {

	public ChooseNextSolutionWithTabuList() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Solution choose(
		Solution thisSolution, Solution bestSolution,
		TabuList tl, Neighbourhood nbh) {

		double bestFunctional = Integer.MAX_VALUE;
		Solution bestSol = null;

		for (Solution sol : nbh.getNeighbourhood()) {
			if ((sol.getFunctional() < bestFunctional && !tl
				.isTabu(sol.getMove()))
				|| (sol.getFunctional() < bestSolution
					.getFunctional() && tl.isTabu(sol
					.getMove()))) {
				bestSol = sol;
				bestFunctional = sol.getFunctional();
			}
		}

		return bestSol;
	}

}
