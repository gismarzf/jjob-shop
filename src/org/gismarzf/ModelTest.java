package org.gismarzf;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModelTest {
	ExcelReader er;
	Model m;

	@Before
	public void setUp() throws Exception {
		er = new ExcelReader(300);
		m = new Model(3, er.getOperations());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCalculateFunctional() {
		m.createGraph();
		assertTrue(m.calculateFunctional() == 1289.0);
	}

}
