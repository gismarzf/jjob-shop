package org.gismarzf.jjobshop;

import org.jgrapht.graph.DefaultWeightedEdge;

@SuppressWarnings("serial")
public class Arc extends DefaultWeightedEdge {

	public enum Type {
		Conjunctive, Disjunctive, Sink, Source;
	}

	private int arcIndex = -1;
	private Type type;

	public boolean equals(Arc b) {
		return ((this.getType() == b.getType() && this
			.getArcIndex() == b.getArcIndex()));
	}

	public int getArcIndex() {
		return arcIndex;
	}

	public Type getType() {
		return type;
	}

	public void setArcIndex(int index) {

		this.arcIndex = index;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String toString() {

		if ((type == Type.Conjunctive)
			|| (type == Type.Disjunctive)) {
			return Integer.toString(arcIndex) + " (" + type
				+ "-" + (Operation) this.getSource() + ">"
				+ (Operation) this.getTarget() + ")";
		} else {
			return null;
		}

	}

	public int direction() {
		((Operation) getSource()).getIndex();

		if (((Operation) getSource()).getIndex() < ((Operation) getTarget())
			.getIndex()) {
			return 1;
		} else {
			return 0;
		}
	}
}
