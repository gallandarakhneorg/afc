/**
 * 
 * fr.utbm.v3g.core.math.Tuple3dTest.java
 *
 * Copyright (c) 2008-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
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
package org.arakhne.afc.math.geometry.d3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3DTestRule;
import org.arakhne.afc.math.geometry.d3.d.Tuple3d;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractTuple3DTest<T extends Tuple3D, TT extends Tuple3D> extends AbstractMathTestCase {
	
	@Rule
	public CoordinateSystem3DTestRule csTestRule = new CoordinateSystem3DTestRule();

	protected TT t;
	
	public TT getT() {
		return this.t;
	}
	
	@Before
	public void setUp() {
		this.t = createTuple(1, -2, 5);
	}
	
	@After
	public void tearDown() {
		this.t = null;
	}

	public abstract TT createTuple(double x, double y, double z);
	
	public abstract boolean isIntCoordinates();
	
	@Test
	public final void getX() {
		assertEpsilonEquals(1., getT().getX());
	}

	@Test
	public final void ix() {
		assertEquals(1, getT().ix());
	}
	
	@Test
	public final void getY() {
		assertEpsilonEquals(-2., getT().getY());
	}
	
	@Test
	public final void iy() {
		assertEquals(-2, getT().iy());
	}

	@Test
	public final void getZ() {
		assertEpsilonEquals(5., getT().getZ());
	}

	@Test
	public final void iz() {
		assertEquals(5, getT().iz());
	}

	@Test
	public final void equals_notEquals() {
		Tuple3D c = new Tuple3d();
		assertFalse(getT().equals(c));
	}
		
	@Test
	public final void equals_equals() {
		Tuple3D c = new Tuple3d(1, -2, 5);
		assertTrue(getT().equals(c));
	}

	@Test
	public final void equals_same() {
		assertTrue(getT().equals(getT()));
	}

	@Test
	public final void equals_null() {
		assertFalse(getT().equals(null));
	}

	@Test
	public final void testCloneTuple() {
		Tuple3D clone = getT().clone();
		assertNotNull(clone);
		assertNotSame(getT(), clone);
		assertEpsilonEquals(getT().getX(), clone.getX());
		assertEpsilonEquals(getT().getY(), clone.getY());
		assertEpsilonEquals(getT().getZ(), clone.getZ());
	}

	@Test
	public void absolute() {
		getT().absolute();
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(2, getT().getY());
		assertEpsilonEquals(5, getT().getZ());
	}

	@Test
	public void absoluteT() {
		Tuple3D c = new Tuple3d();
		getT().absolute(c);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(5, getT().getZ());
		assertEpsilonEquals(1, c.getX());
		assertEpsilonEquals(2, c.getY());
		assertEpsilonEquals(5, c.getZ());
	}

	@Test
	public void addIntInt() {
		getT().add(6, 7, 1);
		assertEpsilonEquals(7, getT().getX());
		assertEpsilonEquals(5, getT().getY());
		assertEpsilonEquals(6, getT().getZ());
	}

	@Test
	public void addDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().add(6.5, 7.5, 5.5);
		assertEpsilonEquals(7.5, getT().getX());
		assertEpsilonEquals(5.5, getT().getY());
		assertEpsilonEquals(10.5, getT().getY());
	}

	@Test
	public void addDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().add(6.5, 7.5, 5.5);
		assertEquals(8, getT().ix());
		assertEquals(6, getT().iy());
		assertEquals(11, getT().iz());
	}

	@Test
	public void addXInt() {
		getT().addX(6);
		assertEpsilonEquals(7, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(5, getT().getZ());
	}

	@Test
	public void addXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().addX(6.5);
		assertEpsilonEquals(7.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@Test
	public void addXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().addX(6.5);
		assertEquals(8, getT().ix());
		assertEquals(-2, getT().iy());
		assertEquals(5, getT().iz());
	}
	
	@Test
	public void addYInt() {
		getT().addY(6);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(4, getT().getY());
		assertEpsilonEquals(5, getT().getZ());
	}
	
	@Test
	public void addYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().addY(6.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(4.5, getT().getY());
		assertEpsilonEquals(5, getT().getZ());
	}
	
	@Test
	public void addYDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().addY(6.5);
		assertEquals(1, getT().ix());
		assertEquals(5, getT().iy());
		assertEquals(5, getT().iz());
	}

	@Test
	public void addZInt() {
		getT().addZ(5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(5, getT().getZ());
	}

	@Test
	public void addZDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().addZ(5.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(10.5, getT().getZ());
	}

	@Test
	public void addZDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().addZ(5.5);
		assertEquals(1, getT().ix());
		assertEquals(-2, getT().iy());
		assertEquals(11, getT().iz());
	}

	@Test
	public void negateT() {
		Tuple3D c = new Tuple3d();
		getT().negate(c);
		assertEpsilonEquals(0, getT().getX());
		assertEpsilonEquals(0, getT().getY());
		assertEpsilonEquals(0, getT().getZ());

		c = new Tuple3d(25, -45, -17);
		getT().negate(c);
		assertEpsilonEquals(-25, getT().getX());
		assertEpsilonEquals(45, getT().getY());
		assertEpsilonEquals(17, getT().getZ());
	}

	@Test
	public void negate() {
		getT().negate();
		assertEpsilonEquals(-1, getT().getX());
		assertEpsilonEquals(2, getT().getY());
		assertEpsilonEquals(-5, getT().getZ());
	}

	@Test
	public void scaleIntT() {
		Tuple3D c = new Tuple3d(2, -1, -2);
		getT().scale(4, c);
		assertEpsilonEquals(8, getT().getX());
		assertEpsilonEquals(-4, getT().getY());
		assertEpsilonEquals(-8, getT().getY());
	}

	@Test
	public void scaleDoubleT_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Tuple3D c = new Tuple3d(2, -1);
		getT().scale(4.5, c);
		assertEpsilonEquals(9, getT().getX());
		assertEpsilonEquals(-4.5, getT().getY());
	}

	@Test
	public void scaleDoubleT_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Tuple3D c = new Tuple3d(2, -1);
		getT().scale(4.5, c);
		assertEquals(9, getT().ix());
		assertEquals(-4, getT().iy());
	}

	@Test
	public void scaleInt() {
		getT().scale(4);
		assertEpsilonEquals(4, getT().getX());
		assertEpsilonEquals(-8, getT().getY());
	}

	@Test
	public void scaleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().scale(4.5);
		assertEpsilonEquals(4.5, getT().getX());
		assertEpsilonEquals(-9, getT().getY());
	}

	@Test
	public void scaleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().scale(4.5);
		assertEquals(5, getT().ix());
		assertEquals(-9, getT().iy());
	}

	@Test
	public void setTuple3D() {
		Tuple3D c = new Tuple3d(-45, 78);
		getT().set(c);
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
	}

	@Test
	public void setIntInt() {
		getT().set(-45, 78);
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
	}

	@Test
	public void setDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().set(-45.5, 78.5);
		assertEpsilonEquals(-45.5, getT().getX());
		assertEpsilonEquals(78.5, getT().getY());
	}

	@Test
	public void setDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().set(-45.5, 78.5);
		assertEquals(-45, getT().ix());
		assertEquals(79, getT().iy());
	}

	@Test
	public void setIntArray() {
		getT().set(new int[]{-45, 78});
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
	}

	@Test
	public void setDoubleArray_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().set(new double[]{-45.5, 78.5});
		assertEpsilonEquals(-45.5, getT().getX());
		assertEpsilonEquals(78.5, getT().getY());
	}

	@Test
	public void setDoubleArray_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().set(new double[]{-45.5, 78.5});
		assertEquals(-45, getT().ix());
		assertEquals(79, getT().iy());
	}

	@Test
	public void setXInt() {
		getT().setX(45);
		assertEpsilonEquals(45, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@Test
	public void setXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().setX(45.5);
		assertEpsilonEquals(45.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@Test
	public void setXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().setX(45.5);
		assertEquals(46, getT().ix());
		assertEquals(-2, getT().iy());
	}

	@Test
	public void setYInt() {
		getT().setY(45);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(45, getT().getY());
	}

	@Test
	public void setYDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().setY(45.5);
		assertEquals(1, getT().ix());
		assertEquals(46, getT().iy());
	}

	@Test
	public void setYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().setY(45.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(45.5, getT().getY());
	}

	@Test
	public void subIntInt() {
		getT().sub(45, 78);
		assertEpsilonEquals(-44, getT().getX());
		assertEpsilonEquals(-80, getT().getY());
	}

	@Test
	public void subXInt() {
		getT().subX(45);
		assertEpsilonEquals(-44, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@Test
	public void subYInt() {
		getT().subY(78);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-80, getT().getY());
	}

	@Test
	public void subDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().sub(45.5, 78.5);
		assertEpsilonEquals(-44.5, getT().getX());
		assertEpsilonEquals(-80.5, getT().getY());
	}

	@Test
	public void subDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().sub(45.5, 78.5);
		assertEquals(-44, getT().ix());
		assertEquals(-80, getT().iy());
	}

	@Test
	public void subXDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().subX(45.5);
		assertEpsilonEquals(-44.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@Test
	public void subXDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().subX(45.5);
		assertEquals(-44, getT().ix());
		assertEquals(-2, getT().iy());
	}

	@Test
	public void subYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().subY(78.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-80.5, getT().getY());
	}

	@Test
	public void subYDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().subY(78.5);
		assertEquals(1, getT().ix());
		assertEquals(-80, getT().iy());
	}

}
