/**
 * 
 * fr.utbm.v3g.core.math.Tuple2dTest.java
 *
 * Copyright (c) 2008-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 *
 * Primary author : Olivier LAMOTTE (olivier.lamotte@utbm.fr) - 2015
 *
 */
package org.arakhne.afc.math.geometry.d2.ifx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.arakhne.afc.math.geometry.d2.AbstractTuple2DTest;
import org.junit.Test;

import javafx.beans.property.IntegerProperty;

@SuppressWarnings("all")
public class Tuple2ifxTest extends AbstractTuple2DTest<Tuple2ifx, Tuple2ifx> {

	@Override
	public boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	public Tuple2ifx createTuple(double x, double y) {
		return new Tuple2ifx(x, y);
	}

	@Test
	public void xProperty() {
		IntegerProperty property = this.t.xProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.t.xProperty();
		assertSame(property, property2);
		assertEquals(1, property.get());
	}

	@Test
	public void yProperty() {
		IntegerProperty property = this.t.yProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.t.yProperty();
		assertSame(property, property2);
		assertEquals(-2, property.get());
	}

	@Test
	public void xPropertySetter() {
		assertEquals(1, this.t.ix());
		assertEquals(-2, this.t.iy());
		IntegerProperty property = this.t.xProperty();
		property.set(345);
		assertEquals(345, this.t.ix());
		assertEquals(-2, this.t.iy());
	}

	@Test
	public void yPropertySetter() {
		assertEquals(1, this.t.ix());
		assertEquals(-2, this.t.iy());
		IntegerProperty property = this.t.yProperty();
		property.set(345);
		assertEquals(1, this.t.ix());
		assertEquals(345, this.t.iy());
	}

}
