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
							.getFunctional() && tl.isTabu(sol
							.getMove()))) {

				bestSol = sol;
				bestFunctional = sol.getFunctional();

			}
		}

		tl.add(bestSol.getMove());

		return bestSol;
	}

	public String getStatus(Optimizer opt) {

		String msg =
				String.format(
						"****\nTamaño lista tabu: %s, este funcional: %s (Arco: %s)"
								+ ", mejor funcional: %s",
						tl.getSize(),
						opt.getThisSolution().getFunctional(),
						opt.getThisSolution().getMove(),
						opt.getBestSolution().getFunctional());
		return msg;

	}

	public String toString() {
		return "lista Tabu con max=" + tl.getMaxSize();
	}

}
