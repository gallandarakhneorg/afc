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

package org.arakhne.afc.math.test.geometry.d3.afp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.afp.GeomFactory3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.arakhne.afc.math.test.AbstractMathTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractCurvePathElement3dTest extends AbstractMathTestCase {

	protected GeomFactory3afp<?, ?, ?, ?, ?> factory;

	private PathElement3afp element;

	protected abstract GeomFactory3afp<?, ?, ?, ?, ?> createFactory();

	@BeforeEach
	public void setUp() {
		this.factory = createFactory();
		this.element = this.factory.newCurvePathElement(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
	}

	@DisplayName("getType")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getType(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PathElementType.CURVE_TO, this.element.getType());
	}

	@DisplayName("getFromX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getFromX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, this.element.getFromX());
	}

	@DisplayName("getFromY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getFromY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2, this.element.getFromY());
	}

	@DisplayName("getFromZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getFromZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3, this.element.getFromZ());
	}

	@DisplayName("getCtrlX1")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCtrlX1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(4, this.element.getCtrlX1());
	}

	@DisplayName("getCtrlY1")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCtrlY1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.element.getCtrlY1());
	}

	@DisplayName("getCtrlZ1")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCtrlZ1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(6, this.element.getCtrlZ1());
	}

	@DisplayName("getCtrlX2")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCtrlX2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(7, this.element.getCtrlX2());
	}

	@DisplayName("getCtrlY2")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCtrlY2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(8, this.element.getCtrlY2());
	}

	@DisplayName("getCtrlZ2")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCtrlZ2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(9, this.element.getCtrlZ2());
	}

	@DisplayName("getToX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getToX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(10, this.element.getToX());
	}

	@DisplayName("getToY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getToY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(11, this.element.getToY());
	}

	@DisplayName("getToZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getToZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(12, this.element.getToZ());
	}

	@DisplayName("isEmpty")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void isEmpty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.element.isEmpty());

		PathElement3afp element1 = this.factory.newCurvePathElement(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		assertTrue(element1.isEmpty());
	}

	@DisplayName("isDrawable")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void isDrawable(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.element.isDrawable());

		PathElement3afp element1 = this.factory.newCurvePathElement(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		assertFalse(element1.isDrawable());
	}

}
