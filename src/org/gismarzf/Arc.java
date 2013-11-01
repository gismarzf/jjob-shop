package org.gismarzf;

public class Arc {

	private Operation start, end;
	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	private String type;
	private Boolean direction;

	public Operation getStart() {
		return start;
	}

	public void setStart(Operation start) {
		this.start = start;
	}

	public Operation getEnd() {
		return end;
	}

	public void setEnd(Operation end) {
		this.end = end;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getDirection() {
		return direction;
	}

	public void setDirection(Boolean direction) {
		this.direction = direction;
	}

	public Operation pointsTo() {

		if (direction) {
			return end;
		} else {
			return start;
		}
	}

	public Operation pointsFrom() {

		if (direction) {
			return start;
		} else {
			return end;
		}
	}
}
