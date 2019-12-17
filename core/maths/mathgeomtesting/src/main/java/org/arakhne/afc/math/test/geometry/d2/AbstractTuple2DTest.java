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

package org.arakhne.afc.math.test.geometry.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.d.Tuple2d;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
public abstract class AbstractTuple2DTest<TT extends Tuple2D> extends AbstractMathTestCase {
	
	protected TT t;
	
	public TT getT() {
		return this.t;
	}
	
	@BeforeEach
	public void setUp() {
		this.t = createTuple(1, -2);
	}
	
	@AfterEach
	public void tearDown() {
		this.t = null;
	}

	public abstract TT createTuple(double x, double y);
	
	public abstract boolean isIntCoordinates();
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void getX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1., getT().getX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void ix(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(1, getT().ix());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void getY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-2., getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void iy(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void equals_notEquals(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new Tuple2d();
		assertFalse(getT().equals(c));
	}
		
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void equals_equals(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new Tuple2d(1, -2);
		assertTrue(getT().equals(c));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void equals_same(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(getT().equals(getT()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void equals_null(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(getT().equals(null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public final void testCloneTuple(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D clone = getT().clone();
		assertNotNull(clone);
		assertNotSame(getT(), clone);
		assertEpsilonEquals(getT().getX(), clone.getX());
		assertEpsilonEquals(getT().getY(), clone.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void absolute(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().absolute();
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void absoluteT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new Tuple2d();
		getT().absolute(c);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(1, c.getX());
		assertEpsilonEquals(2, c.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().add(6, 7);
		assertEpsilonEquals(7, getT().getX());
		assertEpsilonEquals(5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addDoubleDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().add(6.5, 7.5);
		assertEpsilonEquals(7.5, getT().getX());
		assertEpsilonEquals(5.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addDoubleDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().add(6.5, 7.5);
		assertEquals(8, getT().ix());
		assertEquals(6, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addXInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().addX(6);
		assertEpsilonEquals(7, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addXDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().addX(6.5);
		assertEpsilonEquals(7.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addXDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().addX(6.5);
		assertEquals(8, getT().ix());
		assertEquals(-2, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addYInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().addY(6);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(4, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addYDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().addY(6.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(4.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addYDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().addY(6.5);
		assertEquals(1, getT().ix());
		assertEquals(5, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void negateT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new Tuple2d();
		getT().negate(c);
		assertEpsilonEquals(0, getT().getX());
		assertEpsilonEquals(0, getT().getY());

		c = new Tuple2d(25, -45);
		getT().negate(c);
		assertEpsilonEquals(-25, getT().getX());
		assertEpsilonEquals(45, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void negate(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().negate();
		assertEpsilonEquals(-1, getT().getX());
		assertEpsilonEquals(2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void scaleIntT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new Tuple2d(2, -1);
		getT().scale(4, c);
		assertEpsilonEquals(8, getT().getX());
		assertEpsilonEquals(-4, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void scaleDoubleT_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Tuple2D c = new Tuple2d(2, -1);
		getT().scale(4.5, c);
		assertEpsilonEquals(9, getT().getX());
		assertEpsilonEquals(-4.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void scaleDoubleT_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Tuple2D c = new Tuple2d(2, -1);
		getT().scale(4.5, c);
		assertEquals(9, getT().ix());
		assertEquals(-4, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void scaleInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().scale(4);
		assertEpsilonEquals(4, getT().getX());
		assertEpsilonEquals(-8, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void scaleDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().scale(4.5);
		assertEpsilonEquals(4.5, getT().getX());
		assertEpsilonEquals(-9, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void scaleDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().scale(4.5);
		assertEquals(5, getT().ix());
		assertEquals(-9, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setTuple2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new Tuple2d(-45, 78);
		getT().set(c);
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().set(-45, 78);
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setDoubleDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().set(-45.5, 78.5);
		assertEpsilonEquals(-45.5, getT().getX());
		assertEpsilonEquals(78.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setDoubleDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().set(-45.5, 78.5);
		assertEquals(-45, getT().ix());
		assertEquals(79, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIntArray(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().set(new int[]{-45, 78});
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setDoubleArray_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().set(new double[]{-45.5, 78.5});
		assertEpsilonEquals(-45.5, getT().getX());
		assertEpsilonEquals(78.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setDoubleArray_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().set(new double[]{-45.5, 78.5});
		assertEquals(-45, getT().ix());
		assertEquals(79, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setXInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().setX(45);
		assertEpsilonEquals(45, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setXDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().setX(45.5);
		assertEpsilonEquals(45.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setXDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().setX(45.5);
		assertEquals(46, getT().ix());
		assertEquals(-2, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setYInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().setY(45);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(45, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setYDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().setY(45.5);
		assertEquals(1, getT().ix());
		assertEquals(46, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setYDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().setY(45.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(45.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().sub(45, 78);
		assertEpsilonEquals(-44, getT().getX());
		assertEpsilonEquals(-80, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subXInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().subX(45);
		assertEpsilonEquals(-44, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subYInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().subY(78);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-80, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subDoubleDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().sub(45.5, 78.5);
		assertEpsilonEquals(-44.5, getT().getX());
		assertEpsilonEquals(-80.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subDoubleDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().sub(45.5, 78.5);
		assertEquals(-44, getT().ix());
		assertEquals(-80, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subXDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().subX(45.5);
		assertEpsilonEquals(-44.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subXDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().subX(45.5);
		assertEquals(-44, getT().ix());
		assertEquals(-2, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subYDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().subY(78.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-80.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void subYDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().subY(78.5);
		assertEquals(1, getT().ix());
		assertEquals(-80, getT().iy());
	}

}
