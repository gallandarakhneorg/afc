/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d1.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.base.d1.Tuple1D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractTuple1DTestCase<TT extends Tuple1D<?, ?, ?, ?>, RS extends Segment1D<?, ?>>
		extends AbstractMathTestCase {
	
	protected TT t;
	
	protected RS s;

	public TT getT() {
		return this.t;
	}

	public RS getS() {
		return this.s;
	}

	@BeforeEach
	public void setUp() {
		this.s = createSegment();
		this.t = createTuple(1, -2);
	}
	
	@AfterEach
	public void tearDown() {
		this.t = null;
		this.s = null;
	}

	public abstract TT createTuple(double x, double y);
	
	public abstract RS createSegment();

	public abstract boolean isIntCoordinates();
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("getX")
	public final void getX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1., getT().getX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("ix")
	public final void ix(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(1, getT().ix());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("getY")
	public final void getY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-2., getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("iy")
	public final void iy(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("== notequal")
	public final void equals_notEquals(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new InnerComputationVector2D();
		assertFalse(getT().equals(c));
	}
		
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("== equal")
	public final void equals_equals(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new InnerComputationVector2D(1, -2);
		assertTrue(getT().equals(c));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("== same")
	public final void equals_same(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(getT().equals(getT()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("== null")
	public final void equals_null(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(getT().equals(null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("clone")
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
	@DisplayName("absolute()")
	public void absolute(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().absolute();
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("absolute(Tuple2D)")
	public void absoluteT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new InnerComputationVector2D();
		getT().absolute(c);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(1, c.getX());
		assertEpsilonEquals(2, c.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("add(int, int)")
	public void addIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().add(6, 7);
		assertEpsilonEquals(7, getT().getX());
		assertEpsilonEquals(5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("add(double, double)")
	public void addDoubleDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().add(6.5, 7.5);
		assertEpsilonEquals(7.5, getT().getX());
		assertEpsilonEquals(5.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("add(double, double)")
	public void addDoubleDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().add(6.5, 7.5);
		assertEquals(8, getT().ix());
		assertEquals(6, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("addX(int)")
	public void addXInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().addX(6);
		assertEpsilonEquals(7, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("addX(double)")
	public void addXDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().addX(6.5);
		assertEpsilonEquals(7.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("addX(double)")
	public void addXDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().addX(6.5);
		assertEquals(8, getT().ix());
		assertEquals(-2, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("addY(int)")
	public void addYInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().addY(6);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(4, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("addY(double)")
	public void addYDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().addY(6.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(4.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("addY(double)")
	public void addYDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().addY(6.5);
		assertEquals(1, getT().ix());
		assertEquals(5, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("negate(Tuple2D)")
	public void negateT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new InnerComputationVector2D();
		getT().negate(c);
		assertEpsilonEquals(0, getT().getX());
		assertEpsilonEquals(0, getT().getY());

		c = new InnerComputationVector2D(25, -45);
		getT().negate(c);
		assertEpsilonEquals(-25, getT().getX());
		assertEpsilonEquals(45, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("negate()")
	public void negate(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().negate();
		assertEpsilonEquals(-1, getT().getX());
		assertEpsilonEquals(2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("scale(int, Tuple2D)")
	public void scaleIntT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new InnerComputationVector2D(2, -1);
		getT().scale(4, c);
		assertEpsilonEquals(8, getT().getX());
		assertEpsilonEquals(-4, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("scale(double, Tuple2D)")
	public void scaleDoubleT_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Tuple2D c = new InnerComputationVector2D(2, -1);
		getT().scale(4.5, c);
		assertEpsilonEquals(9, getT().getX());
		assertEpsilonEquals(-4.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("scale(double, Tuple2D)")
	public void scaleDoubleT_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Tuple2D c = new InnerComputationVector2D(2, -1);
		getT().scale(4.5, c);
		assertEquals(9, getT().ix());
		assertEquals(-4, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("scale(int)")
	public void scaleInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().scale(4);
		assertEpsilonEquals(4, getT().getX());
		assertEpsilonEquals(-8, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("scale(double)")
	public void scaleDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().scale(4.5);
		assertEpsilonEquals(4.5, getT().getX());
		assertEpsilonEquals(-9, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("scale(double)")
	public void scaleDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().scale(4.5);
		assertEquals(5, getT().ix());
		assertEquals(-9, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("set(Tuple2D)")
	public void setTuple2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Tuple2D c = new InnerComputationVector2D(-45, 78);
		getT().set(c);
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("set(int, int)")
	public void setIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().set(-45, 78);
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("set(double, double)")
	public void setDoubleDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().set(-45.5, 78.5);
		assertEpsilonEquals(-45.5, getT().getX());
		assertEpsilonEquals(78.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("set(double, double)")
	public void setDoubleDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().set(-45.5, 78.5);
		assertEquals(-45, getT().ix());
		assertEquals(79, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("set(int[])")
	public void setIntArray(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().set(new int[]{-45, 78});
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("set(double[])")
	public void setDoubleArray_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().set(new double[]{-45.5, 78.5});
		assertEpsilonEquals(-45.5, getT().getX());
		assertEpsilonEquals(78.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("set(double[])")
	public void setDoubleArray_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().set(new double[]{-45.5, 78.5});
		assertEquals(-45, getT().ix());
		assertEquals(79, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("setX(int)")
	public void setXInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().setX(45);
		assertEpsilonEquals(45, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("setX(double)")
	public void setXDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().setX(45.5);
		assertEpsilonEquals(45.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("setX(double)")
	public void setXDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().setX(45.5);
		assertEquals(46, getT().ix());
		assertEquals(-2, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("setY(int)")
	public void setYInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().setY(45);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(45, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("setY(double)")
	public void setYDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().setY(45.5);
		assertEquals(1, getT().ix());
		assertEquals(46, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("setY(double)")
	public void setYDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().setY(45.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(45.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("sub(int, int)")
	public void subIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().sub(45, 78);
		assertEpsilonEquals(-44, getT().getX());
		assertEpsilonEquals(-80, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("subX(int)")
	public void subXInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().subX(45);
		assertEpsilonEquals(-44, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("subY(int)")
	public void subYInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getT().subY(78);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-80, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("sub(double, double)")
	public void subDoubleDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().sub(45.5, 78.5);
		assertEpsilonEquals(-44.5, getT().getX());
		assertEpsilonEquals(-80.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("sub(double, double)")
	public void subDoubleDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().sub(45.5, 78.5);
		assertEquals(-44, getT().ix());
		assertEquals(-80, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("subX(double)")
	public void subXDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().subX(45.5);
		assertEpsilonEquals(-44.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("subX(double)")
	public void subXDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().subX(45.5);
		assertEquals(-44, getT().ix());
		assertEquals(-2, getT().iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("subY(double)")
	public void subYDouble_iffp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().subY(78.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-80.5, getT().getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@DisplayName("subY(double)")
	public void subYDouble_ifi(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().subY(78.5);
		assertEquals(1, getT().ix());
		assertEquals(-80, getT().iy());
	}

	@Test
	@DisplayName("getSegment")
	public void getSegment() {
		assertSame(getS(), getT().getSegment());
	}

}
