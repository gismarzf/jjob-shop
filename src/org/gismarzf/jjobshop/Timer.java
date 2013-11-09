package org.gismarzf.jjobshop;

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
		// TODO Auto-generated method stub
		return maxTiempo - (System.currentTimeMillis() - start);
	}

	public long getElapsed() {
		// TODO Auto-generated method stub
		return System.currentTimeMillis() - start;
	}

}
