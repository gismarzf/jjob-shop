package org.gismarzf.jjobshop;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gismarzf.jjobshop.Arc.Type;

import com.google.common.collect.Lists;

public class CreateNeighbourhoodAsCritical
	implements
	CreateNeighbourhoodBehaviour {

	// log4j, new one automatically adds class name
	private static Logger logger = LogManager.getLogger();

	@Override
	public List<Solution> create(Solution s) {

		List<Solution> nbh = Lists.newArrayList();

		for (Arc e : s.getCriticalPath()) {

			if (e.getType() == Type.Disjunctive) {

				Solution nextSolution = new Solution(s);

				nextSolution.switchDirectionOfArc(e);

				try {

					assert !nextSolution.hasError();
					nextSolution.calculateCriticalPath();
					nbh.add(nextSolution);

				} catch (AssertionError ae) {

					logger.error("Cambiando la direccion de un arco de camino "
						+ "critico genero una solucion invalida..");
				}
			}
		}

		return nbh;

	}

}
