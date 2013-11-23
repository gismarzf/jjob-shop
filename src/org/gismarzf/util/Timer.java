package org.gismarzf.util;

public class Timer {

	private long maxTiempo;
	private long start;

	public Timer(long maxTiempo) {
		this.maxTiempo = maxTiempo;
	}

	public void setStart() {
		start = System.currentTimeMillis();
	}

	public double getElapsedMinutes() {
		return (double) getElapsed() / (double) (60 * 1000);
	}

	public double getRemainingMinutes() {
		return ((double) getRemaining() / (double) (60 * 1000));
	}

	public long getRemaining() {
		return maxTiempo - (System.currentTimeMillis() - start);
	}

	public long getElapsed() {
		return System.currentTimeMillis() - start;
	}

}
