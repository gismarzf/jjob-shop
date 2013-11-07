package org.gismarzf.jjobshop;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface NeighbourhoodCreateBehaviour {

	// log4j, new one automatically adds class name
	public static Logger logger = LogManager.getLogger();

	public List<Solution> get(Solution s, TabuList tl);
}
