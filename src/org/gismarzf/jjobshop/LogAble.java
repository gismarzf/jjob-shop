package org.gismarzf.jjobshop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface LogAble {

	// log4j, new one automatically adds class name
	public static Logger logger = LogManager.getLogger();
}
