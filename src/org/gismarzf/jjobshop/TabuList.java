package org.gismarzf.jjobshop;

import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Queues;

public class TabuList {

	private int maxSize;

	private Queue<Integer> tabuList = Queues.newArrayDeque();

	public TabuList(int maxSize) {
		this.maxSize = maxSize;
	}

	// log4j, new one automatically adds class name
	private static Logger logger = LogManager.getLogger();

	public void add(Arc e) {
		assert !tabuList.contains(e);

		if (tabuList.size() >= maxSize)
			tabuList.poll();

		tabuList.offer(e.getArcIndex());

	}

	public int getMaxSize() {
		return maxSize;
	}

	public int getSize() {
		return tabuList.size();
	}

	public boolean isTabu(Arc a) {
		if (tabuList.contains(a.getArcIndex())) {
			logger.trace("****");
			logger.trace("Salto arco tabu: " + a.getArcIndex());
			return true;
		}

		return false;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
}
