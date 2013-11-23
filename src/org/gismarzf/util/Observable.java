package org.gismarzf.util;

public interface Observable {
	public void registerObserver(Object o);

	public void removeObserver(Object o);

	public void notifyAllObservers();
}
