package org.gismarzf.jjobshop;

import java.util.Random;

public class ChooseNextSolutionWithSA
		implements
		ChooseNextSolutionBehaviour {

	private double initialTemperature;
	private double temperature;
	private double initialFactor;
	private double factor;

	public ChooseNextSolutionWithSA(double temp, double factor) {
		this.initialTemperature = temp;
		this.initialFactor = factor;
		this.temperature = temp;
		this.factor = factor;
	}

	@Override
	public Solution choose(Optimizer opt) {
		Random rnd = new Random();
		Solution randSolution =
				opt.getNbh().get(rnd.nextInt(opt.getNbh().size()));

		temperature = temperature * factor;

		double delta =
				opt.getThisSolution().getFunctional()
						- randSolution.getFunctional();

		if (randSolution.getFunctional() <= opt
				.getThisSolution()
				.getFunctional()) {
			return randSolution;

		} else if ((randSolution.getFunctional() > opt
				.getThisSolution()
				.getFunctional())
				&& rnd.nextDouble() < Math.exp(delta / temperature)) {

			return randSolution;

		} else {

			return opt.getThisSolution();
		}
	}

	@Override
	public String getStatus(Optimizer opt) {
		String msg =
				String.format(
						"****\n Temperatura: %s, "
								+ "este funcional: %s (Arco: %s), "
								+ "mejor funcional: %s ",
						Math.round(temperature),
						opt.getThisSolution().getFunctional(),
						opt.getThisSolution().getMove(),
						opt.getBestSolution().getFunctional());

		return msg;
	}

	public String toString() {
		return "recocido simulado con t="
				+ initialTemperature
				+ " factor="
				+ initialFactor;
	}

}
