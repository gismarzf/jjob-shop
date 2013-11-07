package org.gismarzf.jjobshop;

import java.util.List;

import org.gismarzf.jjobshop.Arc.Type;

import com.google.common.collect.Lists;

public class CreateNeighbourhoodAsCriticalBehaviour
	implements CreateNeighbourhoodBehaviour {

	public CreateNeighbourhoodAsCriticalBehaviour() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Solution> get(Solution s, TabuList tl) {

		List<Solution> neighbourhood = Lists.newArrayList();

		for (Arc e : s.getCriticalPath()) {

			if (e.getType() == Type.Disjunctive) {

				Solution nextSolution = new Solution(s);

				nextSolution.switchDirectionOfArc(e);

				try {

					assert !nextSolution.hasError();
					nextSolution.calculateCriticalPath();

					neighbourhood.add(nextSolution);

				} catch (AssertionError ae) {

					logger
						.error("Cambiando la direccion de un arco de camino "
							+ "critico genero una solucion invalida..");
				}
			}
		}

		return neighbourhood;

	}

}
