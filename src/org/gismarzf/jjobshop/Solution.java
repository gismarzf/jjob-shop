package org.gismarzf.jjobshop;

import java.util.ArrayList;
import java.util.List;

import org.gismarzf.jjobshop.Arc.Type;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.google.common.collect.Lists;

public class Solution {

	private List<Arc> criticalPath = Lists.newArrayList();

	private SimpleDirectedWeightedGraph<Operation, Arc> dGraph =
		new SimpleDirectedWeightedGraph<Operation, Arc>(
			Arc.class);

	private List<Arc> disjunctiveArcs = Lists
		.newArrayList();

	private int errors;
	private double functional;;
	private int jobCount, opxMachine;
	private Arc move = new Arc();
	private List<Operation> operations = Lists
		.newArrayList();
	private Operation sink = new Operation();
	private Operation source = new Operation();

	public Solution(int jobCount, List<Operation> operations) {
		this.jobCount = jobCount;
		this.opxMachine = jobCount;
		this.operations = operations;
		this.errors = -1;
		this.functional = -1;

		createGraph();
		calculateCriticalPath();
	}

	@SuppressWarnings("unchecked")
	public Solution(Solution b) {
		this.criticalPath.addAll(b.criticalPath);
		this.disjunctiveArcs.addAll(b.disjunctiveArcs);
		this.errors = b.errors;
		this.functional = b.functional;
		this.jobCount = b.jobCount;
		this.move = (Arc) b.move.clone();
		this.operations.addAll(b.operations);
		this.opxMachine = b.opxMachine;
		this.source = b.source;
		this.sink = b.sink;

		// this might fail
		this.dGraph =
			(SimpleDirectedWeightedGraph<Operation, Arc>) b.dGraph
				.clone();
	}

	private static int machineCount = 10, opxJob = 10;

	public List<Arc> calculateCriticalPath() {

		if (hasError())
			return null;

		BellmanFordShortestPath<Operation, Arc> sp =
			new BellmanFordShortestPath<Operation, Arc>(
				dGraph, source);

		criticalPath = sp.getPathEdgeList(sink);
		calculateFunctional();

		return criticalPath;
	}

	public double calculateFunctional() {
		assert !criticalPath.isEmpty();

		double sum = 0;
		for (Arc e : criticalPath) {
			sum = sum + dGraph.getEdgeWeight(e);
		}

		setFunctional(-sum);
		return -sum;
	}

	public int countErrors() {
		CycleDetector<Operation, Arc> cd =
			new CycleDetector<Operation, Arc>(dGraph);

		int count = 0;
		for (Operation operation : operations) {
			if (cd.detectCyclesContainingVertex(operation))
				count++;
		}
		errors = count;
		return count;
	}

	public void createGraph() {

		source = new Operation();
		source.setIndex(-1); //
		sink = new Operation();
		sink.setIndex(-2);
		dGraph.addVertex(source);

		for (Operation operation : operations) {
			dGraph.addVertex(operation);
		}

		dGraph.addVertex(sink);

		createConjunctiveArcs();
		createDisjunctiveArcs();

	}

	public int[] getArcDirections() {
		int[] arcDirections =
			new int[disjunctiveArcs.size()];

		for (Arc a : disjunctiveArcs) {
			arcDirections[a.getArcIndex()] = a.direction();
		}

		return arcDirections;
	}

	public int[][] getCaminoCritico() {
		int[][] cc = new int[getCriticalPath().size()][2];

		for (int i = 0; i < getCriticalPath().size(); i++) {
			cc[i][0] =
				dGraph.getEdgeTarget(
					getCriticalPath().get(i)).getIndex();
			if (getCriticalPath().get(i).getType() == Type.Disjunctive) {
				cc[i][1] =
					getCriticalPath().get(i).getArcIndex();
			}
		}

		return cc;
	}

	public List<Arc> getCriticalPath() {
		return criticalPath;
	}

	public
		SimpleDirectedWeightedGraph<Operation, Arc>
		getdGraph() {
		return dGraph;
	}

	public List<Arc> getDisjunctiveArcs() {
		return disjunctiveArcs;
	}

	public int getErrors() {
		return errors;
	}

	public double getFunctional() {
		return functional;
	}

	public Arc getMove() {
		return move;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public double[][] getProgramming() {

		double[][] prog;

		prog = new double[operations.size()][2];

		if (hasError())
			return null;

		BellmanFordShortestPath<Operation, Arc> sp =
			new BellmanFordShortestPath<Operation, Arc>(
				dGraph, source);

		for (int i = 0; i < operations.size(); i++) {

			prog[i][1] = -sp.getCost(operations.get(i));
			prog[i][0] =
				prog[operations.get(i).getIndex()][1]
					- operations.get(i).getLength();

		}

		return prog;
	}

	public boolean hasError() {
		CycleDetector<Operation, Arc> cd =
			new CycleDetector<Operation, Arc>(dGraph);
		return cd.detectCycles();
	}

	public int length() {
		return disjunctiveArcs.size();
	}

	public void setCriticalPath(List<Arc> criticalPath) {
		this.criticalPath = criticalPath;
	}

	public void setdGraph(
		SimpleDirectedWeightedGraph<Operation, Arc> dGraph) {
		this.dGraph = dGraph;
	}

	public
		void setDisjunctiveArcs(List<Arc> disjunctiveArcs) {
		this.disjunctiveArcs = disjunctiveArcs;
	}

	public void setErrors(int errors) {
		this.errors = errors;
	}

	public void setFunctional(double functional) {
		this.functional = functional;
	}

	public void setMove(Arc move) {
		this.move = move;
	}

	public void switchDirectionOfArc(Arc e) {

		// might break b/c i dont know if the dgraph cares about those edges
		// being from a different graph.
		Arc previousEdge =
			dGraph.getEdge(
				dGraph.getEdgeSource(e),
				dGraph.getEdgeTarget(e));

		// only switch direction if it is disjunctive
		if (previousEdge.getType() == Type.Disjunctive) {
			Operation previousSource =
				dGraph.getEdgeSource(previousEdge);
			Operation previousTarget =
				dGraph.getEdgeTarget(previousEdge);

			dGraph.removeEdge(previousEdge);
			disjunctiveArcs.remove(previousEdge);

			Operation newSource = previousTarget;
			Operation newTarget = previousSource;

			Arc newEdge =
				dGraph.addEdge(newSource, newTarget);
			newEdge.setArcIndex(previousEdge.getArcIndex());
			newEdge.setType(previousEdge.getType());
			dGraph.setEdgeWeight(
				newEdge, -newTarget.getLength());

			move = newEdge;

			disjunctiveArcs.add(newEdge);
		}
	}

	public String toString() {
		return Double.toString(getFunctional())
			+ " Move: " + move;
	}

	private void createConjunctiveArcs() {
		Arc e;

		// jobs
		for (int i = 0, index = 0; i < jobCount; i++) {
			// from source to each 1st in job
			e =
				dGraph.addEdge(
					source, operations.get(i * 10));
			dGraph.setEdgeWeight(e, -operations
				.get(i * 10).getLength());

			// and last operations
			e =
				dGraph
					.addEdge(
						operations.get((i * 10)
							+ (opxJob - 1)), sink);
			e.setType(Type.Sink);
			dGraph.setEdgeWeight(e, 0);

			for (int j = i * 10; j < (i + 1) * 10 - 1; j++, index++) {
				e =
					dGraph.addEdge(
						operations.get(j),
						operations.get(j + 1));
				dGraph.setEdgeWeight(
					e, -operations.get(j + 1).getLength());
				e.setArcIndex(index);
				e.setType(Type.Conjunctive);
			}
		}

	}

	private void createDisjunctiveArcs() {
		Arc e;

		// machines
		for (int i = 0, index = 0; i < machineCount; i++) {
			List<Operation> ops =
				returnOpsForMachine(i + 1);
			// machine indexes start at 1

			for (int j = 0; j < opxMachine; j++) {
				for (int j2 = 1; j2 < opxMachine - j; j2++, index++) {
					e =
						dGraph.addEdge(
							ops.get(j), ops.get(j + j2));
					dGraph.setEdgeWeight(e, -ops
						.get(j + j2).getLength());
					e.setArcIndex(index);
					e.setType(Type.Disjunctive);
					disjunctiveArcs.add(e);
				}
			}
		}
	}

	private List<Operation> returnOpsForMachine(int m) {
		List<Operation> ops = new ArrayList<Operation>();

		for (Operation o : operations) {
			if (o.getMachineNo() == m) {
				ops.add(o);
			}
		}

		return ops;
	}
}
