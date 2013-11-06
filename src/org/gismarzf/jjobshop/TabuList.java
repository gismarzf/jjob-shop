package org.gismarzf.jjobshop;

import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gismarzf.jjobshop.Arc.Type;

import com.google.common.collect.Queues;

public class TabuList {

	public TabuList(int maxSize) {
		this.maxSize = maxSize;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	private int maxSize;
	private Queue<Integer> tabuList = Queues
		.newArrayDeque();

	// log4j, new one automatically adds class name
	private static Logger logger = LogManager.getLogger();

	public void add(Arc e) {
		assert !tabuList.contains(e.getArcIndex());

		if (tabuList.size() >= maxSize)
			tabuList.poll();

		tabuList.offer(e.getArcIndex());

	}

	public boolean isTabu(Arc a) {
		if (tabuList.contains(a.getArcIndex())
			&& (a.getType() == Type.Disjunctive)) {
			logger.trace("****");
			logger.trace("Salto arco tabu: "
				+ a.getArcIndex());
			return true;
		}

		return false;
	}
}
