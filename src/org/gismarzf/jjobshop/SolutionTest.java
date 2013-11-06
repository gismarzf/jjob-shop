package org.gismarzf.jjobshop;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SolutionTest {
	Excel er;
	Solution s;

	@Before
	public void setUp() throws Exception {
		er = new Excel(300);
		s = new Solution(3, er.getOperations());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCalculateFunctional() {
		assertTrue(s.getFunctional() == 1289.0);
	}

}
