/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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
import org.junit.jupiter.api.DisplayName;
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
	public void setUp() {
		this.t = createTuple(1, -2, 0);
	}
	
	@AfterEach
	public void tearDown() {
		this.t = null;
	}

	public abstract TT createTuple(double x, double y, double z);
	
	public abstract boolean isIntCoordinates();

	@DisplayName("getX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1., getT().getX());
	}

	@DisplayName("ix")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void ix(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(1, getT().ix());
	}
	
	@DisplayName("getY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-2., getT().getY());
	}
	
	@DisplayName("iy")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void iy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, getT().iy());
	}

	@DisplayName("getZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getT().getZ());
	}

	@DisplayName("iz")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void iz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, getT().iz());
	}

	@DisplayName("equals(Tuple3D) inequality")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void equals_notEquals(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Tuple3D c = new Tuple3d();
		assertFalse(getT().equals(c));
	}
		
	@DisplayName("equals(Tuple3D) equality")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void equals_equals(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Tuple3D c = new Tuple3d(1, -2, 0);
		assertTrue(getT().equals(c));
	}

	@DisplayName("equals(Tuple3D) same")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void equals_same(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(getT().equals(getT()));
	}

	@DisplayName("equals(Tuple3D) null")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void equals_null(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getT().equals(null));
	}

	@DisplayName("clone")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void testCloneTuple(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Tuple3D clone = getT().clone();
		assertNotNull(clone);
		assertNotSame(getT(), clone);
		assertEpsilonEquals(getT().getX(), clone.getX());
		assertEpsilonEquals(getT().getY(), clone.getY());
		assertEpsilonEquals(getT().getZ(), clone.getZ());
	}

	@DisplayName("absolute")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void absolute(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().absolute();
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(2, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@DisplayName("absolute(Tuple3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void absoluteT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Tuple3D c = new Tuple3d();
		getT().absolute(c);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
		assertEpsilonEquals(1, c.getX());
		assertEpsilonEquals(2, c.getY());
		assertEpsilonEquals(0, c.getZ());
	}

	@DisplayName("add(int,int,int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().add(6, 7, 1);
		assertEpsilonEquals(7, getT().getX());
		assertEpsilonEquals(5, getT().getY());
		assertEpsilonEquals(1, getT().getZ());
	}

	@DisplayName("add(double,double,double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addDoubleDoubleDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().add(6.5, 7.5, 5.5);
		assertEpsilonEquals(7.5, getT().getX());
		assertEpsilonEquals(5.5, getT().getY());
		assertEpsilonEquals(5.5, getT().getY());
	}

	@DisplayName("add(double,double,double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addDoubleDoubleDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().add(6.5, 7.5, 5.5);
		assertEquals(8, getT().ix());
		assertEquals(6, getT().iy());
		assertEquals(11, getT().iz());
	}

	@DisplayName("addX(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addXInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().addX(6);
		assertEpsilonEquals(7, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@DisplayName("addX(double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addXDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().addX(6.5);
		assertEpsilonEquals(7.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@DisplayName("addX(double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addXDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().addX(6.5);
		assertEquals(8, getT().ix());
		assertEquals(-2, getT().iy());
		assertEquals(0, getT().iz());
	}
	
	@DisplayName("addY(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addYInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().addY(6);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(4, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}
	
	@DisplayName("addY(double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addYDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().addY(6.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(4.5, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}
	
	@DisplayName("addY(double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addYDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().addY(6.5);
		assertEquals(1, getT().ix());
		assertEquals(5, getT().iy());
		assertEquals(0, getT().iz());
	}

	@DisplayName("addZ(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addZInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().addZ(5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(5, getT().getZ());
	}

	@DisplayName("addZ(double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addZDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().addZ(5.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(5.5, getT().getZ());
	}

	@DisplayName("addZ(double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addZDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().addZ(5.5);
		assertEquals(1, getT().ix());
		assertEquals(-2, getT().iy());
		assertEquals(6, getT().iz());
	}

	@DisplayName("negate(Tuple3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void negateT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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

	@DisplayName("negate")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void negate(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().negate();
		assertEpsilonEquals(-1, getT().getX());
		assertEpsilonEquals(2, getT().getY());
		assertEpsilonEquals(-0, getT().getZ());
	}

	@DisplayName("scale(int,Tuple3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleIntT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Tuple3D c = new Tuple3d(2, -1, -2);
		getT().scale(4, c);
		assertEpsilonEquals(8, getT().getX());
		assertEpsilonEquals(-4, getT().getY());
		assertEpsilonEquals(-8, getT().getZ());
	}

	@DisplayName("scale(double,Tuple3D) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleDoubleT_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		Tuple3D c = new Tuple3d(2, -1, 0);
		getT().scale(4.5, c);
		assertEpsilonEquals(9, getT().getX());
		assertEpsilonEquals(-4.5, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@DisplayName("scale(double,Tuple3D) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleDoubleT_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		Tuple3D c = new Tuple3d(2, -1, 0);
		getT().scale(4.5, c);
		assertEquals(9, getT().ix());
		assertEquals(-4, getT().iy());
		assertEquals(0, getT().iz());
	}

	@DisplayName("scale(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().scale(4);
		assertEpsilonEquals(4, getT().getX());
		assertEpsilonEquals(-8, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@DisplayName("scale(double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().scale(4.5);
		assertEpsilonEquals(4.5, getT().getX());
		assertEpsilonEquals(-9, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@DisplayName("scale(double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void scaleDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().scale(4.5);
		assertEquals(5, getT().ix());
		assertEquals(-9, getT().iy());
		assertEquals(0, getT().iz());
	}

	@DisplayName("set(Tuple3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setTuple3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Tuple3D c = new Tuple3d(-45, 78, 1);
		getT().set(c);
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
		assertEpsilonEquals(1, getT().getZ());
	}

	@DisplayName("set(int,int,int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().set(-45, 78, 1);
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
		assertEpsilonEquals(1, getT().getZ());
	}

	@DisplayName("set(double,double,double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setDoubleDoubleDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().set(-45.5, 78.5, 1.1);
		assertEpsilonEquals(-45.5, getT().getX());
		assertEpsilonEquals(78.5, getT().getY());
		assertEpsilonEquals(1.1, getT().getZ());
	}

	@DisplayName("set(double,double,double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setDoubleDoubleDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().set(-45.5, 78.5, 1.1);
		assertEquals(-45, getT().ix());
		assertEquals(79, getT().iy());
		assertEquals(1, getT().iz());
	}

	@DisplayName("set(int[])")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIntArray(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().set(new int[]{-45, 78, 1});
		assertEpsilonEquals(-45, getT().getX());
		assertEpsilonEquals(78, getT().getY());
	}

	@DisplayName("set(double[]) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setDoubleArray_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().set(new double[]{-45.5, 78.5, 1.1});
		assertEpsilonEquals(-45.5, getT().getX());
		assertEpsilonEquals(78.5, getT().getY());
		assertEpsilonEquals(1.1, getT().getZ());
	}

	@DisplayName("set(double[]) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setDoubleArray_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().set(new double[]{-45.5, 78.5, 1.1});
		assertEquals(-45, getT().ix());
		assertEquals(79, getT().iy());
		assertEquals(1, getT().iz());
	}

	@DisplayName("setX(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setXInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().setX(45);
		assertEpsilonEquals(45, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@DisplayName("setX(double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setXDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().setX(45.5);
		assertEpsilonEquals(45.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
		assertEpsilonEquals(0, getT().getZ());
	}

	@DisplayName("setX(double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setXDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().setX(45.5);
		assertEquals(46, getT().ix());
		assertEquals(-2, getT().iy());
		assertEquals(0, getT().iz());
	}

	@DisplayName("setY(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setYInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().setY(45);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(45, getT().getY());
	}

	@DisplayName("setY(double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setYDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().setY(45.5);
		assertEquals(1, getT().ix());
		assertEquals(46, getT().iy());
	}

	@DisplayName("setY(double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setYDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().setY(45.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(45.5, getT().getY());
	}

	@DisplayName("setZ(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setZInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().setZ(45);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(45, getT().getZ());
	}

	@DisplayName("setZ(double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setZDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().setZ(45.5);
		assertEquals(1, getT().ix());
		assertEquals(46, getT().iz());
	}

	@DisplayName("setZ(double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setZDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().setZ(45.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(45.5, getT().getZ());
	}

	@DisplayName("sub(int,int,int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().sub(45, 78, 0);
		assertEpsilonEquals(-44, getT().getX());
		assertEpsilonEquals(-80, getT().getY());
	}

	@DisplayName("subX(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subXInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().subX(45);
		assertEpsilonEquals(-44, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@DisplayName("subY(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subYInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().subY(78);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-80, getT().getY());
	}

	@DisplayName("subZ(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subZInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getT().subZ(78);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-78, getT().getZ());
	}

	@DisplayName("sub(double,double,double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subDoubleDoubleDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().sub(45.5, 78.5, 0);
		assertEpsilonEquals(-44.5, getT().getX());
		assertEpsilonEquals(-80.5, getT().getY());
	}

	@DisplayName("sub(double,double,double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subDoubleDoubleDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().sub(45.5, 78.5, 0);
		assertEquals(-44, getT().ix());
		assertEquals(-80, getT().iy());
	}

	@DisplayName("subX(double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subXDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().subX(45.5);
		assertEpsilonEquals(-44.5, getT().getX());
		assertEpsilonEquals(-2, getT().getY());
	}

	@DisplayName("subX(double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subXDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().subX(45.5);
		assertEquals(-44, getT().ix());
		assertEquals(-2, getT().iy());
	}

	@DisplayName("subY(double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subYDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().subY(78.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-80.5, getT().getY());
	}

	@DisplayName("subY(double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subYDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().subY(78.5);
		assertEquals(1, getT().ix());
		assertEquals(-80, getT().iy());
	}

	@DisplayName("subZ(double) with double coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subZDouble_iffp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeFalse(isIntCoordinates());
		getT().subZ(78.5);
		assertEpsilonEquals(1, getT().getX());
		assertEpsilonEquals(-78.5, getT().getZ());
	}

	@DisplayName("subZ(double) with int coords")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void subZDouble_ifi(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assumeTrue(isIntCoordinates());
		getT().subZ(78.5);
		assertEquals(1, getT().ix());
		assertEquals(-79, getT().iz());
	}

}
