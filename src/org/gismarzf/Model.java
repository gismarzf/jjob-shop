package org.gismarzf;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.google.common.collect.Lists;

public class Model {

	public Model(int jobCount, List<Operation> operations) {
		this.jobCount = jobCount;
		this.opxMachine = jobCount;
		this.operations = operations;
	}

	// log4j, new one automatically adds class name
	private static Logger logger = LogManager.getLogger();
	private static int machineCount = 10, opxJob = 10;

	private List<Operation> operations;
	private List<DefaultWeightedEdge> conjunctiveArcs = Lists.newArrayList();
	private List<DefaultWeightedEdge> disjunctiveArcs = Lists.newArrayList();

	private SimpleDirectedWeightedGraph<Operation, DefaultWeightedEdge> dGraph;
	private Operation source, sink;

	private int jobCount, opxMachine;

	public List<Operation> getOperations() {
		return operations;
	}

	public double calculateFunctional() {
		BellmanFordShortestPath<Operation, DefaultWeightedEdge> sp = new
				BellmanFordShortestPath<Operation, DefaultWeightedEdge>(dGraph,
						source);

		return -sp.getCost(sink);
	}

	public int countErrors() {
		CycleDetector<Operation, DefaultWeightedEdge> cd =
				new CycleDetector<Operation, DefaultWeightedEdge>(dGraph);

		int count = 0;
		for (Operation operation : operations) {
			if (cd.detectCyclesContainingVertex(operation))
				count++;
		}

		return count;
	}

	public void createGraph() {

		logger.info("Obteniendo grafo disyuntivo...");

		DefaultWeightedEdge e;
		dGraph =
				new SimpleDirectedWeightedGraph<Operation, DefaultWeightedEdge>(
						DefaultWeightedEdge.class);

		source = new Operation();
		source.setIndex(-1); //

		sink = new Operation();
		sink.setIndex(-2);

		dGraph.addVertex(source);

		for (Operation operation : operations) {
			dGraph.addVertex(operation);
		}

		dGraph.addVertex(sink);

		// jobs
		for (int i = 0; i < jobCount; i++) {
			e = dGraph.addEdge(source, operations.get(i * 10));
			// from source to each 1st in job
			dGraph.setEdgeWeight(e, -operations.get(i * 10).getLength());
			// and last operations
			e = dGraph.addEdge(operations.get((i * 10) + (opxJob - 1)), sink);
			dGraph.setEdgeWeight(e, 0);

			for (int j = i * 10; j < (i + 1) * 10 - 1; j++) {
				e = dGraph.addEdge(operations.get(j), operations.get(j + 1));
				dGraph.setEdgeWeight(e, -operations.get(j + 1).getLength());
				conjunctiveArcs.add(e);
			}
		}

		// machines
		for (int i = 0; i < machineCount; i++) {
			List<Operation> ops = returnOpsForMachine(i + 1);
			// machine indexes start at 1

			for (int j = 0; j < opxMachine; j++) {

				for (int j2 = 1; j2 < opxMachine - j; j2++) {
					e = dGraph.addEdge(ops.get(j), ops.get(j + j2));
					dGraph.setEdgeWeight(e, -ops.get(j + j2).getLength());
					disjunctiveArcs.add(e);
				}
			}
		}

		logger.info("****");

	}

	public boolean[] extractBitVector() {

		boolean[] bv = new boolean[disjunctiveArcs.size()];

		for (int i = 0; i < disjunctiveArcs.size(); i++) {
			DefaultWeightedEdge e = disjunctiveArcs.get(i);

			// so, the direction is 1 if the arc points to the right or to the
			// down (the index is *higher*)
			bv[i] = (dGraph.getEdgeSource(e).getIndex() < dGraph.getEdgeTarget(
					e).getIndex());
		}

		return bv;
	}

	public boolean hasError() {
		CycleDetector<Operation, DefaultWeightedEdge> cd =
				new CycleDetector<Operation, DefaultWeightedEdge>(dGraph);
		return cd.detectCycles();
	}

	public void switchDirectionOfArc(int arcIndex) {
		assert arcIndex <= disjunctiveArcs.size();

		DefaultWeightedEdge previousEdge = disjunctiveArcs.get(arcIndex);

		Operation previousSource = dGraph.getEdgeSource(previousEdge);
		Operation previousTarget = dGraph.getEdgeTarget(previousEdge);
		dGraph.removeEdge(previousEdge);

		Operation newSource = previousTarget;
		Operation newTarget = previousSource;

		DefaultWeightedEdge newEdge = dGraph.addEdge(newSource, newTarget);

		disjunctiveArcs.set(arcIndex, newEdge);
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
