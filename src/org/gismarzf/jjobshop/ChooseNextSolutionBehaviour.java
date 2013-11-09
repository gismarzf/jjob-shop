package org.gismarzf.jjobshop;

public interface ChooseNextSolutionBehaviour {

	public Solution choose(Optimizer opt);

	public String getStatus(Optimizer opt);

}
