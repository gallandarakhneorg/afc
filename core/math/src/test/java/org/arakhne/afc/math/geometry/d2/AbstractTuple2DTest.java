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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d2.d.Tuple2d;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractTuple2DTest<T extends Tuple2D> extends AbstractMathTestCase {
	
	@Rule
	public CoordinateSystem2DTestRule csTestRule = new CoordinateSystem2DTestRule();

	protected T t;
	
	protected abstract T createTuple(double x, double y);
	
	protected abstract boolean isIntCoordinates();

	@Before
	public void setUp() {
		this.t = createTuple(1, -2);
	}
	
	@After
	public void tearDown() {
		this.t = null;
	}
	
	@Test
	public void absolute() {
		this.t.absolute();
		assertEpsilonEquals(1, this.t.getX());
		assertEpsilonEquals(2, this.t.getY());
	}

	@Test
	public void absoluteT() {
		Tuple2D c = new Tuple2d();
		this.t.absolute(c);
		assertEpsilonEquals(1, this.t.getX());
		assertEpsilonEquals(-2, this.t.getY());
		assertEpsilonEquals(1, c.getX());
		assertEpsilonEquals(2, c.getY());
	}

	@Test
	public void addIntInt() {
		this.t.add(6, 7);
		assertEpsilonEquals(7, this.t.getX());
		assertEpsilonEquals(5, this.t.getY());
	}

	@Test
	public void addDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		this.t.add(6.5, 7.5);
		assertEpsilonEquals(7.5, this.t.getX());
		assertEpsilonEquals(5.5, this.t.getY());
	}

	@Test
	public void addDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		this.t.add(6.5, 7.5);
		assertEquals(8, this.t.ix());
		assertEquals(6, this.t.iy());
	}

	@Test
	public void addXInt() {
		this.t.addX(6);
		assertEpsilonEquals(7, this.t.getX());
		assertEpsilonEquals(-2, this.t.getY());
	}

	@Test
	public void addXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		this.t.addX(6.5);
		assertEpsilonEquals(7.5, this.t.getX());
		assertEpsilonEquals(-2, this.t.getY());
	}

	@Test
	public void addXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		this.t.addX(6.5);
		assertEquals(8, this.t.ix());
		assertEquals(-2, this.t.iy());
	}

	@Test
	public void addYInt() {
		this.t.addY(6);
		assertEpsilonEquals(1, this.t.getX());
		assertEpsilonEquals(4, this.t.getY());
	}

	@Test
	public void addYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		this.t.addY(6.5);
		assertEpsilonEquals(1, this.t.getX());
		assertEpsilonEquals(4.5, this.t.getY());
	}

	@Test
	public void addYDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		this.t.addY(6.5);
		assertEquals(1, this.t.ix());
		assertEquals(5, this.t.iy());
	}

	@Test
	public void negateT() {
		Tuple2D c = new Tuple2d();
		this.t.negate(c);
		assertEpsilonEquals(0, this.t.getX());
		assertEpsilonEquals(0, this.t.getY());

		c = new Tuple2d(25, -45);
		this.t.negate(c);
		assertEpsilonEquals(-25, this.t.getX());
		assertEpsilonEquals(45, this.t.getY());
	}

	@Test
	public void negate() {
		this.t.negate();
		assertEpsilonEquals(-1, this.t.getX());
		assertEpsilonEquals(2, this.t.getY());
	}

	@Test
	public void scaleIntT() {
		Tuple2D c = new Tuple2d(2, -1);
		this.t.scale(4, c);
		assertEpsilonEquals(8, this.t.getX());
		assertEpsilonEquals(-4, this.t.getY());
	}

	@Test
	public void scaleDoubleT_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Tuple2D c = new Tuple2d(2, -1);
		this.t.scale(4.5, c);
		assertEpsilonEquals(9, this.t.getX());
		assertEpsilonEquals(-4.5, this.t.getY());
	}

	@Test
	public void scaleDoubleT_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Tuple2D c = new Tuple2d(2, -1);
		this.t.scale(4.5, c);
		assertEquals(9, this.t.ix());
		assertEquals(-4, this.t.iy());
	}

	@Test
	public void scaleInt() {
		this.t.scale(4);
		assertEpsilonEquals(4, this.t.getX());
		assertEpsilonEquals(-8, this.t.getY());
	}

	@Test
	public void scaleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		this.t.scale(4.5);
		assertEpsilonEquals(4.5, this.t.getX());
		assertEpsilonEquals(-9, this.t.getY());
	}

	@Test
	public void scaleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		this.t.scale(4.5);
		assertEquals(5, this.t.ix());
		assertEquals(-9, this.t.iy());
	}

	@Test
	public void setTuple2D() {
		Tuple2D c = new Tuple2d(-45, 78);
		this.t.set(c);
		assertEpsilonEquals(-45, this.t.getX());
		assertEpsilonEquals(78, this.t.getY());
	}

	@Test
	public void setIntInt() {
		this.t.set(-45, 78);
		assertEpsilonEquals(-45, this.t.getX());
		assertEpsilonEquals(78, this.t.getY());
	}

	@Test
	public void setDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		this.t.set(-45.5, 78.5);
		assertEpsilonEquals(-45.5, this.t.getX());
		assertEpsilonEquals(78.5, this.t.getY());
	}

	@Test
	public void setDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		this.t.set(-45.5, 78.5);
		assertEquals(-45, this.t.ix());
		assertEquals(79, this.t.iy());
	}

	@Test
	public void setIntArray() {
		this.t.set(new int[]{-45, 78});
		assertEpsilonEquals(-45, this.t.getX());
		assertEpsilonEquals(78, this.t.getY());
	}

	@Test
	public void setDoubleArray_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		this.t.set(new double[]{-45.5, 78.5});
		assertEpsilonEquals(-45.5, this.t.getX());
		assertEpsilonEquals(78.5, this.t.getY());
	}

	@Test
	public void setDoubleArray_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		this.t.set(new double[]{-45.5, 78.5});
		assertEquals(-45, this.t.ix());
		assertEquals(79, this.t.iy());
	}

	@Test
	public void getX() {
		assertEpsilonEquals(1., this.t.getX());
	}

	@Test
	public void ix() {
		assertEquals(1, this.t.ix());
	}

	@Test
	public void setXInt() {
		this.t.setX(45);
		assertEpsilonEquals(45, this.t.getX());
		assertEpsilonEquals(-2, this.t.getY());
	}

	@Test
	public void setXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		this.t.setX(45.5);
		assertEpsilonEquals(45.5, this.t.getX());
		assertEpsilonEquals(-2, this.t.getY());
	}

	@Test
	public void setXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		this.t.setX(45.5);
		assertEquals(46, this.t.ix());
		assertEquals(-2, this.t.iy());
	}

	@Test
	public void getY() {
		assertEpsilonEquals(-2., this.t.getY());
	}

	@Test
	public void iy() {
		assertEquals(-2, this.t.iy());
	}

	@Test
	public void setYInt() {
		this.t.setY(45);
		assertEpsilonEquals(1, this.t.getX());
		assertEpsilonEquals(45, this.t.getY());
	}

	@Test
	public void setYDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		this.t.setY(45.5);
		assertEquals(1, this.t.ix());
		assertEquals(46, this.t.iy());
	}

	@Test
	public void setYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		this.t.setY(45.5);
		assertEpsilonEquals(1, this.t.getX());
		assertEpsilonEquals(45.5, this.t.getY());
	}

	@Test
	public void subIntInt() {
		this.t.sub(45, 78);
		assertEpsilonEquals(-44, this.t.getX());
		assertEpsilonEquals(-80, this.t.getY());
	}

	@Test
	public void subXInt() {
		this.t.subX(45);
		assertEpsilonEquals(-44, this.t.getX());
		assertEpsilonEquals(-2, this.t.getY());
	}

	@Test
	public void subYInt() {
		this.t.subY(78);
		assertEpsilonEquals(1, this.t.getX());
		assertEpsilonEquals(-80, this.t.getY());
	}

	@Test
	public void subDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		this.t.sub(45.5, 78.5);
		assertEpsilonEquals(-44.5, this.t.getX());
		assertEpsilonEquals(-80.5, this.t.getY());
	}

	@Test
	public void subDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		this.t.sub(45.5, 78.5);
		assertEquals(-44, this.t.ix());
		assertEquals(-80, this.t.iy());
	}

	@Test
	public void subXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		this.t.subX(45.5);
		assertEpsilonEquals(-44.5, this.t.getX());
		assertEpsilonEquals(-2, this.t.getY());
	}

	@Test
	public void subXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		this.t.subX(45.5);
		assertEquals(-44, this.t.ix());
		assertEquals(-2, this.t.iy());
	}

	@Test
	public void subYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		this.t.subY(78.5);
		assertEpsilonEquals(1, this.t.getX());
		assertEpsilonEquals(-80.5, this.t.getY());
	}

	@Test
	public void subYDouble_ifi() {
		Assume.assumeFalse(isIntCoordinates());
		this.t.subY(78.5);
		assertEquals(1, this.t.ix());
		assertEquals(-80, this.t.iy());
	}

	@Test
	public void equals_notEquals() {
		Tuple2D c = new Tuple2d();
		assertFalse(this.t.equals(c));
	}
		
	@Test
	public void equals_equals() {
		Tuple2D c = new Tuple2d(1, -2);
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

	@Test
	public void testClone() {
		Tuple2D clone = this.t.clone();
		assertNotNull(clone);
		assertNotSame(this.t, clone);
		assertEpsilonEquals(this.t.getX(), clone.getX());
		assertEpsilonEquals(this.t.getY(), clone.getY());
	}

}
