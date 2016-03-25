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
package org.arakhne.afc.math.geometry.d2.fpfx;

import static org.junit.Assert.*;
import org.arakhne.afc.math.geometry.d2.AbstractTuple2DTest;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.junit.Test;

import javafx.beans.property.DoubleProperty;

@SuppressWarnings("all")
public class Tuple2fxTest extends AbstractTuple2DTest<Tuple2fx> {

	@Override
	protected boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	protected Tuple2fx createTuple(double x, double y) {
		return new Tuple2fx(x, y);
	}
	
	@Test
	public void xProperty() {
		DoubleProperty property = this.t.xProperty();
		assertNotNull(property);
		DoubleProperty property2 = this.t.xProperty();
		assertSame(property, property2);
		assertEpsilonEquals(1, property.get());
	}

	@Test
	public void yProperty() {
		DoubleProperty property = this.t.yProperty();
		assertNotNull(property);
		DoubleProperty property2 = this.t.yProperty();
		assertSame(property, property2);
		assertEpsilonEquals(-2, property.get());
	}

	@Test
	public void xPropertySetter() {
		assertEpsilonEquals(1, this.t.getX());
		assertEpsilonEquals(-2, this.t.getY());
		DoubleProperty property = this.t.xProperty();
		property.set(345.);
		assertEpsilonEquals(345., this.t.getX());
		assertEpsilonEquals(-2, this.t.getY());
	}

	@Test
	public void yPropertySetter() {
		assertEpsilonEquals(1, this.t.getX());
		assertEpsilonEquals(-2, this.t.getY());
		DoubleProperty property = this.t.yProperty();
		property.set(345.);
		assertEpsilonEquals(1, this.t.getX());
		assertEpsilonEquals(345., this.t.getY());
	}
	
	@Override
	public void testClone() {
		super.testClone();
	}

}
