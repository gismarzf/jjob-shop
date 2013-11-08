package org.gismarzf.jjobshop;

import java.util.List;

import com.google.common.collect.Lists;

public class Neighbourhood {

	private List<Solution> neighbourhood = Lists
		.newArrayList();

	public List<Solution> getNeighbourhood() {
		return neighbourhood;
	}

	public void setNeighbourhood(
		List<Solution> neighbourhood) {
		this.neighbourhood = neighbourhood;
	}

}
