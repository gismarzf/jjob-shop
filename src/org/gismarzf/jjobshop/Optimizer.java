package org.gismarzf.jjobshop;

import java.util.List;

public class Optimizer implements LogAble {

	private Solution bestSolution;
	private Solution thisSolution;
	private Neighbourhood nbh;
	private TabuList tl;

	private ChooseNextSolutionBehaviour chooseSolution;
	private CreateNeighbourhoodBehaviour createNeighbourhood;

	public void calculateNeighbourhood() {
		nbh = new Neighbourhood();
		nbh = createNeighbourhood.create(thisSolution);
	}

	public Solution getBestSolution() {
		return bestSolution;
	}

	public void setBestSolution(Solution bestSolution) {
		this.bestSolution = bestSolution;
	}

	public Solution getThisSolution() {
		return thisSolution;
	}

	public void setThisSolution(Solution thisSolution) {
		this.thisSolution = thisSolution;
	}

	public TabuList getTl() {
		return tl;
	}

	public void setTl(TabuList tl) {
		this.tl = tl;
	}

	public void calculateNextSolution() {

		thisSolution =
			chooseSolution.choose(
				thisSolution, bestSolution, tl, nbh);

		if (thisSolution.getFunctional() < bestSolution
			.getFunctional()) {
			bestSolution = new Solution(thisSolution);
		}

		tl.add(thisSolution.getMove());

	}

	public void setAsCriticalNeighbourhood() {
		createNeighbourhood =
			new CreateNeighbourhoodAsCritical();
	}

	public void setInitialSolution(
		int maxJobs, List<Operation> operations) {

		thisSolution = new Solution(maxJobs, operations);

		bestSolution = new Solution(thisSolution);
	}

	public void setAsTabuList(int maxTabu) {
		tl = new TabuList(maxTabu);

		chooseSolution =
			new ChooseNextSolutionWithTabuList();
	}
}
