package org.gismarzf.jjobshop;

import java.util.List;

import org.gismarzf.util.Observable;
import org.gismarzf.util.Observer;
import org.gismarzf.util.Timer;

import com.google.common.collect.Lists;

public class Optimizer implements Observable {

	private Solution bestSolution;
	private Solution thisSolution;
	private List<Solution> nbh;

	private ChooseNextSolutionBehaviour chooseSolution;
	private CreateNeighbourhoodBehaviour createNeighbourhood;

	private List<Object> observers;

	private String status;
	private Timer timer;

	public ChooseNextSolutionBehaviour getChooseSolution() {
		return chooseSolution;
	}

	public Optimizer(long maxTiempo) {
		observers = Lists.newArrayList();
		timer = new Timer(maxTiempo);
	}

	public String getStatus() {
		return status;
	}

	public Timer getTimer() {
		return timer;
	}

	public void registerObserver(Object o) {
		observers.add(o);
	}

	public void removeObserver(Object o) {
		if (observers.contains(o)) {
			observers.remove(o);
		}
	}

	public void notifyAllObservers() {
		for (Object o : observers) {
			Observer observer = (Observer) o;
			observer.update(this);
		}
	}

	public void calculateNeighbourhood() {
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

	public List<Solution> getNbh() {
		return nbh;
	}

	public void calculateNextSolution() {

		thisSolution = chooseSolution.choose(this);

		if (thisSolution.getFunctional() < bestSolution
				.getFunctional()) {
			bestSolution = new Solution(thisSolution);
		}

		status = chooseSolution.getStatus(this);

		notifyAllObservers();
	}

	public void setInitialSolution(
			int maxJobs,
			List<Operation> operations) {

		thisSolution = new Solution(maxJobs, operations);
		bestSolution = new Solution(thisSolution);
	}

	public void setChooseSolution(
			ChooseNextSolutionBehaviour chooseSolution) {
		this.chooseSolution = chooseSolution;
	}

	public void setCreateNeighbourhood(
			CreateNeighbourhoodBehaviour createNeighbourhood) {
		this.createNeighbourhood = createNeighbourhood;
	}
}
