package org.gismarzf.jjobshop;

import org.gismarzf.jjobshop.Arc.Type;

public class CreateNeighbourhoodAsCritical
	implements CreateNeighbourhoodBehaviour, LogAble {

	@Override
	public Neighbourhood create(Solution s) {

		Neighbourhood nbh = new Neighbourhood();

		for (Arc e : s.getCriticalPath()) {

			if (e.getType() == Type.Disjunctive) {

				Solution nextSolution = new Solution(s);

				nextSolution.switchDirectionOfArc(e);

				try {

					assert !nextSolution.hasError();
					nextSolution.calculateCriticalPath();

					nbh
						.getNeighbourhood().add(
							nextSolution);

				} catch (AssertionError ae) {

					logger
						.error("Cambiando la direccion de un arco de camino "
							+ "critico genero una solucion invalida..");
				}
			}
		}

		return nbh;

	}

}
