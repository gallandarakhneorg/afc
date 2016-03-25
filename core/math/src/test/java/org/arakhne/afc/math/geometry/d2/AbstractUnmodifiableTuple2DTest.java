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
package org.arakhne.afc.math.geometry.d2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d2.fp.Tuple2fp;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractUnmodifiableTuple2DTest extends AbstractMathTestCase {
	
	@Rule
	public CoordinateSystem2DTestRule csTestRule = new CoordinateSystem2DTestRule();

	private Tuple2D t;
	
	protected abstract Tuple2D createTuple(int x, int y);

	@Before
	public void setUp() {
		this.t = createTuple(1, -2);
	}
	
	@After
	public void tearDown() {
		this.t = null;
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void absolute() {
		this.t.absolute();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void absoluteT() {
		Tuple2D c = new Tuple2fp();
		this.t.absolute(c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addIntInt() {
		this.t.add(6, 7);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addDoubleDouble() {
		this.t.add(6.5, 7.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addXInt() {
		this.t.addX(6);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addXDouble() {
		this.t.addX(6.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addYInt() {
		this.t.addY(6);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addYDouble() {
		this.t.addY(6.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void negateT() {
		Tuple2D c = new Tuple2fp();
		this.t.negate(c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void negate() {
		this.t.negate();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleIntT() {
		Tuple2D c = new Tuple2fp(2, -1);
		this.t.scale(4, c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleDoubleT() {
		Tuple2D c = new Tuple2fp(2, -1);
		this.t.scale(4.5, c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleInt() {
		this.t.scale(4);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void scaleDouble() {
		this.t.scale(4.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setTuple2D() {
		Tuple2D c = new Tuple2fp(-45, 78);
		this.t.set(c);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setIntInt() {
		this.t.set(-45, 78);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setDoubleDouble() {
		this.t.set(-45.5, 78.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setIntArray() {
		this.t.set(new int[]{-45, 78});
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setDoubleArray() {
		this.t.set(new double[]{-45.5, 78.5});
	}

	@Test
	public void getX() {
		assertEquals(1., this.t.getX(), 0.);
	}

	@Test
	public void ix() {
		assertEquals(1, this.t.ix());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setXInt() {
		this.t.setX(45);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setXDouble() {
		this.t.setX(45.5);
	}

	@Test
	public void getY() {
		assertEquals(-2., this.t.getY(), 0.);
	}

	@Test
	public void iy() {
		assertEquals(-2, this.t.iy());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setYInt() {
		this.t.setY(45);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setYDouble() {
		this.t.setY(45.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void subIntInt() {
		this.t.sub(45, 78);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void subXInt() {
		this.t.subX(45);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void subYInt() {
		this.t.subY(78);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void subDoubleDouble() {
		this.t.sub(45.5, 78.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void subXDouble() {
		this.t.subX(45.5);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void subYDouble() {
		this.t.subY(78.5);
	}

	@Test
	public void equals_notEquals() {
		Tuple2D c = new Tuple2fp();
		assertFalse(this.t.equals(c));
	}
		
	@Test
	public void equals_equals() {
		Tuple2D c = new Tuple2fp(1, -2);
		assertTrue(this.t.equals(c));
	}

	@Test
	public void equals_same() {
		assertTrue(this.t.equals(this.t));
	}

	@Test
	public void equals_null() {
		assertFalse(this.t.equals(null));
	}

}
