package org.gismarzf.jjobshop;

public interface ChooseNextSolutionBehaviour {

	public Solution choose(
		Solution thisSolution, Solution bestSolution,
		TabuList tl, Neighbourhood nbh);
}
