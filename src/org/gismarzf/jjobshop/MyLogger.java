package org.gismarzf.jjobshop;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

public class MyLogger implements Observer {

	// log4j, new one automatically adds class name
	private static Logger logger = LogManager.getLogger();

	private List<Double[]> logList = Lists.newArrayList();
	private Observable opt;

	public List<Double[]> getLogList() {
		return logList;
	}

	public MyLogger(Observable opt) {
		this.opt = opt;
		this.opt.registerObserver(this);
	}

	@Override
	public void update(Optimizer opt) {
		logger.info(opt.getStatus());
		logger.info("Minutos restantes: "
			+ Math.round(opt.getTimer().getRemainingMinutes()));

		Double[] logArray = new Double[2];
		logArray[0] = opt.getTimer().getElapsedMinutes();
		logArray[1] = opt.getThisSolution().getFunctional();
		logList.add(logArray);
	}
}
