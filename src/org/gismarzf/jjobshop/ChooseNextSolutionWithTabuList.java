package org.gismarzf.jjobshop;

public class ChooseNextSolutionWithTabuList
implements
ChooseNextSolutionBehaviour {

	private TabuList tl;

	public ChooseNextSolutionWithTabuList(int maxTabu) {
		this.tl = new TabuList(maxTabu);
	}

	public Solution choose(Optimizer opt) {

		double bestFunctional = Integer.MAX_VALUE;
		Solution bestSol = null;

		for (Solution sol : opt.getNbh()) {
			if ((sol.getFunctional() < bestFunctional && !tl
			.isTabu(sol.getMove()))
			|| (sol.getFunctional() < opt
			.getBestSolution()
			.getFunctional() && tl.isTabu(sol.getMove()))) {

				bestSol = sol;
				bestFunctional = sol.getFunctional();

			}
		}

		tl.add(bestSol.getMove());

		return bestSol;
	}

	public String getStatus(Optimizer opt) {
		return ("****"
		+ "\n"
		+ "Tamaño lista tabu: "
		+ tl.getSize()
		+ ". "
		+ "Este funcional: "
		+ opt.getThisSolution().getFunctional()
		+ " (Move: "
		+ opt.getThisSolution().getMove()
		+ ")"
		+ "\n"
		+ "Mejor funcional: " + opt
		.getBestSolution()
		.getFunctional());

	}

	public String toString() {
		return "Lista Tabu (Max: " + tl.getMaxSize() + " )";
	}

}
