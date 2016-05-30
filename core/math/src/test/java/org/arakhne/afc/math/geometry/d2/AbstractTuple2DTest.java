/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.d2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d2.d.Tuple2d;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractTuple2DTest<T extends Tuple2D, TT extends Tuple2D> extends AbstractMathTestCase {
	
	@Rule
	public CoordinateSystem2DTestRule csTestRule = new CoordinateSystem2DTestRule();

	protected TT t;
	
	public TT getT() {
		return this.t;
	}
	
	@Before
	public void setUp() {
		this.t = createTuple(1, -2);
	}
	
	@After
	public void tearDown() {
		this.t = null;
	}

	public abstract TT createTuple(double x, double y);
	
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
	public final void equals_notEquals() {
		Tuple2D c = new Tuple2d();
		assertFalse(getT().equals(c));
	}
		
	@Test
	public final void equals_equals() {
		Tuple2D c = new Tuple2d(1, -2);
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
		Tuple2D clone = getT().clone();
		assertNotNull(clone);
		assertNotSame(getT(), clone);
		assertEpsilonEquals(getT().getX(), clone.getX());
		assertEpsilonEquals(getT().getY(), clone.getY());
	}

	@Test
	public void absolute() {
		getT().absolute();
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(2, getT().getY());
	}

	@Test
	public void absoluteT() {
		Tuple2D c = new Tuple2d();
		getT().absolute(c);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(1, c.getX());
		assertEpsilonEquals(2, c.getY());
	}

	@Test
	public void addIntInt() {
		getT().add(6, 7);
		assertEpsilonEquals(7, getT().getX());
		assertEpsilonEquals(5, getT().getY());
	}

	@Test
	public void addDoubleDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().add(6.5, 7.5);
		assertEpsilonEquals(7.5, getT().getX());
		assertEpsilonEquals(5.5, getT().getY());
	}

	@Test
	public void addDoubleDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().add(6.5, 7.5);
		assertEquals(8, getT().ix());
		assertEquals(6, getT().iy());
	}

	@Test
	public void addXInt() {
		getT().addX(6);
		assertEpsilonEquals(7, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
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
	}

	@Test
	public void addYInt() {
		getT().addY(6);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(4, getT().getY());
	}

	@Test
	public void addYDouble_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		getT().addY(6.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(4.5, getT().getY());
	}

	@Test
	public void addYDouble_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		getT().addY(6.5);
		assertEquals(1, getT().ix());
		assertEquals(5, getT().iy());
	}

	@Test
	public void negateT() {
		Tuple2D c = new Tuple2d();
		getT().negate(c);
		assertEpsilonEquals(0, getT().getX());
		assertEpsilonEquals(0, getT().getY());

		c = new Tuple2d(25, -45);
		getT().negate(c);
		assertEpsilonEquals(-25, getT().getX());
		assertEpsilonEquals(45, getT().getY());
	}

	@Test
	public void negate() {
		getT().negate();
		assertEpsilonEquals(-1, getT().getX());
		assertEpsilonEquals(2, getT().getY());
	}

	@Test
	public void scaleIntT() {
		Tuple2D c = new Tuple2d(2, -1);
		getT().scale(4, c);
		assertEpsilonEquals(8, getT().getX());
		assertEpsilonEquals(-4, getT().getY());
	}

	@Test
	public void scaleDoubleT_iffp() {
		Assume.assumeFalse(isIntCoordinates());
		Tuple2D c = new Tuple2d(2, -1);
		getT().scale(4.5, c);
		assertEpsilonEquals(9, getT().getX());
		assertEpsilonEquals(-4.5, getT().getY());
	}

	@Test
	public void scaleDoubleT_ifi() {
		Assume.assumeTrue(isIntCoordinates());
		Tuple2D c = new Tuple2d(2, -1);
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
	public void setTuple2D() {
		Tuple2D c = new Tuple2d(-45, 78);
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
