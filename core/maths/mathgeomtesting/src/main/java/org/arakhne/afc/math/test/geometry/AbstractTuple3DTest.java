/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.test.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.Month;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.d.Tuple3d;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
public abstract class AbstractTuple3DTest<TT extends Tuple3D> extends AbstractMathTestCase {
	
	protected TT t;
	
	public TT getT() {
		return this.t;
	}
	
	@BeforeEach
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setUp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t = createTuple(1, -2, 0);
	}
	
	@AfterEach
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
		assertEpsilonEquals(0., getT().getZ());
	}

	@Test
	public final void iz() {
		assertEquals(0, getT().iz());
	}

	@Test
	public final void equals_notEquals() {
		Tuple3D c = new Tuple3d();
		assertFalse(getT().equals(c));
	}
		
	@Test
	public final void equals_equals() {
		Tuple3D c = new Tuple3d(1, -2, 0);
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
		assertEpsilonEquals(0, getT().getZ());
	}

	@Test
	public void absoluteT() {
		Tuple3D c = new Tuple3d();
		getT().absolute(c);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
		assertEpsilonEquals(1, c.getX());
		assertEpsilonEquals(2, c.getY());
		assertEpsilonEquals(0, c.getZ());
	}

	@Test
	public void addIntIntInt() {
		getT().add(6, 7, 1);
		assertEpsilonEquals(7, getT().getX());
		assertEpsilonEquals(5, getT().getY());
		assertEpsilonEquals(1, getT().getZ());
	}

	@Test
	public void addDoubleDouble_iffp() {
		assumeFalse(isIntCoordinates());
		getT().add(6.5, 7.5, 5.5);
		assertEpsilonEquals(7.5, getT().getX());
		assertEpsilonEquals(5.5, getT().getY());
		assertEpsilonEquals(5.5, getT().getY());
	}

	@Test
	public void addDoubleDouble_ifi() {
		assumeTrue(isIntCoordinates());
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
		assertEpsilonEquals(0, getT().getZ());
	}

	@Test
	public void addXDouble_iffp() {
		assumeFalse(isIntCoordinates());
		getT().addX(6.5);
		assertEpsilonEquals(7.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@Test
	public void addXDouble_ifi() {
		assumeTrue(isIntCoordinates());
		getT().addX(6.5);
		assertEquals(8, getT().ix());
		assertEquals(-2, getT().iy());
		assertEquals(0, getT().iz());
	}
	
	@Test
	public void addYInt() {
		getT().addY(6);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(4, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}
	
	@Test
	public void addYDouble_iffp() {
		assumeFalse(isIntCoordinates());
		getT().addY(6.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(4.5, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}
	
	@Test
	public void addYDouble_ifi() {
		assumeTrue(isIntCoordinates());
		getT().addY(6.5);
		assertEquals(1, getT().ix());
		assertEquals(5, getT().iy());
		assertEquals(0, getT().iz());
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
		assumeFalse(isIntCoordinates());
		getT().addZ(5.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(5.5, getT().getZ());
	}

	@Test
	public void addZDouble_ifi() {
		assumeTrue(isIntCoordinates());
		getT().addZ(5.5);
		assertEquals(1, getT().ix());
		assertEquals(-2, getT().iy());
		assertEquals(6, getT().iz());
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
		assertEpsilonEquals(-0, getT().getZ());
	}

	@Test
	public void scaleIntT() {
		Tuple3D c = new Tuple3d(2, -1, -2);
		getT().scale(4, c);
		assertEpsilonEquals(8, getT().getX());
		assertEpsilonEquals(-4, getT().getY());
		assertEpsilonEquals(-8, getT().getZ());
	}

	@Test
	public void scaleDoubleT_iffp() {
		assumeFalse(isIntCoordinates());
		Tuple3D c = new Tuple3d(2, -1, 0);
		getT().scale(4.5, c);
		assertEpsilonEquals(9, getT().getX());
		assertEpsilonEquals(-4.5, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@Test
	public void scaleDoubleT_ifi() {
		assumeTrue(isIntCoordinates());
		Tuple3D c = new Tuple3d(2, -1, 0);
		getT().scale(4.5, c);
		assertEquals(9, getT().ix());
		assertEquals(-4, getT().iy());
		assertEquals(0, getT().iz());
	}

	@Test
	public void scaleInt() {
		getT().scale(4);
		assertEpsilonEquals(4, getT().getX());
		assertEpsilonEquals(-8, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@Test
	public void scaleDouble_iffp() {
		assumeFalse(isIntCoordinates());
		getT().scale(4.5);
		assertEpsilonEquals(4.5, getT().getX());
		assertEpsilonEquals(-9, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@Test
	public void scaleDouble_ifi() {
		assumeTrue(isIntCoordinates());
		getT().scale(4.5);
		assertEquals(5, getT().ix());
		assertEquals(-9, getT().iy());
		assertEquals(0, getT().iz());
	}

	@Test
	public void setTuple3D() {
		Tuple3D c = new Tuple3d(-45, 78, 1);
		getT().set(c);
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
		assertEpsilonEquals(1, getT().getZ());
	}

	@Test
	public void setIntIntInt() {
		getT().set(-45, 78, 1);
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
		assertEpsilonEquals(1, getT().getZ());
	}

	@Test
	public void setDoubleDouble_iffp() {
		assumeFalse(isIntCoordinates());
		getT().set(-45.5, 78.5, 1.1);
		assertEpsilonEquals(-45.5, getT().getX());
		assertEpsilonEquals(78.5, getT().getY());
		assertEpsilonEquals(1.1, getT().getZ());
	}

	@Test
	public void setDoubleDouble_ifi() {
		assumeTrue(isIntCoordinates());
		getT().set(-45.5, 78.5, 1.1);
		assertEquals(-45, getT().ix());
		assertEquals(79, getT().iy());
		assertEquals(1, getT().iz());
	}

	@Test
	public void setIntArray() {
		getT().set(new int[]{-45, 78, 1});
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
	}

	@Test
	public void setDoubleArray_iffp() {
		assumeFalse(isIntCoordinates());
		getT().set(new double[]{-45.5, 78.5, 1.1});
		assertEpsilonEquals(-45.5, getT().getX());
		assertEpsilonEquals(78.5, getT().getY());
		assertEpsilonEquals(1.1, getT().getZ());
	}

	@Test
	public void setDoubleArray_ifi() {
		assumeTrue(isIntCoordinates());
		getT().set(new double[]{-45.5, 78.5, 1.1});
		assertEquals(-45, getT().ix());
		assertEquals(79, getT().iy());
		assertEquals(1, getT().iz());
	}

	@Test
	public void setXInt() {
		getT().setX(45);
		assertEpsilonEquals(45, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@Test
	public void setXDouble_iffp() {
		assumeFalse(isIntCoordinates());
		getT().setX(45.5);
		assertEpsilonEquals(45.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@Test
	public void setXDouble_ifi() {
		assumeTrue(isIntCoordinates());
		getT().setX(45.5);
		assertEquals(46, getT().ix());
		assertEquals(-2, getT().iy());
		assertEquals(0, getT().iz());
	}

	@Test
	public void setYInt() {
		getT().setY(45);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(45, getT().getY());
	}

	@Test
	public void setYDouble_ifi() {
		assumeTrue(isIntCoordinates());
		getT().setY(45.5);
		assertEquals(1, getT().ix());
		assertEquals(46, getT().iy());
	}

	@Test
	public void setYDouble_iffp() {
		assumeFalse(isIntCoordinates());
		getT().setY(45.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(45.5, getT().getY());
	}

	@Test
	public void subIntIntInt() {
		getT().sub(45, 78, 0);
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
		assumeFalse(isIntCoordinates());
		getT().sub(45.5, 78.5, 0);
		assertEpsilonEquals(-44.5, getT().getX());
		assertEpsilonEquals(-80.5, getT().getY());
	}

	@Test
	public void subDoubleDouble_ifi() {
		assumeTrue(isIntCoordinates());
		getT().sub(45.5, 78.5, 0);
		assertEquals(-44, getT().ix());
		assertEquals(-80, getT().iy());
	}

	@Test
	public void subXDouble_iffp() {
		assumeFalse(isIntCoordinates());
		getT().subX(45.5);
		assertEpsilonEquals(-44.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@Test
	public void subXDouble_ifi() {
		assumeTrue(isIntCoordinates());
		getT().subX(45.5);
		assertEquals(-44, getT().ix());
		assertEquals(-2, getT().iy());
	}

	@Test
	public void subYDouble_iffp() {
		assumeFalse(isIntCoordinates());
		getT().subY(78.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-80.5, getT().getY());
	}

	@Test
	public void subYDouble_ifi() {
		assumeTrue(isIntCoordinates());
		getT().subY(78.5);
		assertEquals(1, getT().ix());
		assertEquals(-80, getT().iy());
	}

}
