package org.gismarzf.jjobshop;

import java.util.Random;

public class ChooseNextSolutionWithSA
implements ChooseNextSolutionBehaviour {

	private double temperature;
	private double factor;

	public ChooseNextSolutionWithSA(double temp, double factor) {
		this.temperature = temp;
		this.factor = factor;
	}

	@Override
	public Solution choose(Optimizer opt) {
		// TODO Auto-generated method stub

		Random rnd = new Random();
		Solution randSolution =
		opt.getNbh().get(rnd.nextInt(opt.getNbh().size()));

		temperature = temperature * factor;

		double delta =
		randSolution.getFunctional()
		- opt.getThisSolution().getFunctional();

		if (randSolution.getFunctional() <= opt
		.getThisSolution().getFunctional()) {
			return randSolution;

		} else if ((randSolution.getFunctional() > opt
		.getThisSolution().getFunctional())
		&& rnd.nextDouble() > Math.exp(delta / temperature)) {

			return randSolution;

		} else {

			return opt.getThisSolution();
		}
	}

	@Override
	public String getStatus(Optimizer opt) {
		return ("****"
		+ "\n" + "Temperatura: " + Math.round(temperature)
		+ ". Este funcional: "
		+ opt.getThisSolution().getFunctional() + " (Move: "
		+ opt.getThisSolution().getMove() + ")" + "\n"
		+ "Mejor funcional: " + opt
		.getBestSolution().getFunctional());
	}

	public String toString() {
		return "Recocido simulado con t ="
		+ temperature + ", factor = " + factor;
	}

}
