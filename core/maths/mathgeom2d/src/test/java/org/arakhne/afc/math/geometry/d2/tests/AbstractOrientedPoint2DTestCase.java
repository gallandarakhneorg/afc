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

package org.arakhne.afc.math.geometry.d2.tests;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.OrientedPoint2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public abstract class AbstractOrientedPoint2DTestCase<P extends OrientedPoint2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		TT extends Tuple2D>
		extends AbstractPoint2DTestCase<P, V, TT> {

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("getTangentX")
    public void getTangentX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		assertEpsilonEquals(0., point.getTangentX());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("itx")
    public void itx(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		assertEquals(0, point.itx());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("setTangentX(int)")
    public void setTangentX_int(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.setTangentX(4536);
		assertEpsilonEquals(4536., point.getTangentX());
		assertEquals(4536, point.itx());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("setTangentX(double)")
    public void setTangentX_double(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.setTangentX(4536.125);
		assertEpsilonEquals(4536.125, point.getTangentX());
		assertEquals(4536, point.itx());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("getTangentY")
    public void getTangentY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		assertEpsilonEquals(0., point.getTangentY());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("ity")
    public void ity(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		assertEquals(0, point.ity());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("setTangentY(int)")
    public void setTangentY_int(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.setTangentY(4536);
		assertEpsilonEquals(4536., point.getTangentY());
		assertEquals(4536, point.ity());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("setTangentY(double)")
    public void setTangentY_double(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.setTangentY(4536.6458);
		assertEpsilonEquals(4536.6458, point.getTangentY());
		assertEquals(4537, point.ity());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("getNormalX w/o tangent")
    public void getNormalX_notangent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		assertEpsilonEquals(0., point.getNormalX());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("inx w/o tangent")
    public void inx_notangent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		assertEquals(0, point.inx());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("getNormalY w/o tangent")
    public void getNormalY_notangent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		assertEpsilonEquals(0., point.getNormalY());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("iny w/o tangent")
    public void iny_notangent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		assertEquals(0, point.iny());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("getNormalX w/ tangent")
    public void getNormalX_tangent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.setTangent(4536.6458, 975.124);
		assertEpsilonEquals(-975.124, point.getNormalX());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("inx w/ tangent")
    public void inx_tangent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.setTangent(4536.6458, 975.124);
		assertEquals(-975, point.inx());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("getNormalY w/ tangent")
    public void getNormalY_tangent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.setTangent(4536.6458, 975.124);
		assertEpsilonEquals(4536.6458, point.getNormalY());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("iny w/ tangent")
    public void iny_tangent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.setTangent(4536.6458, 975.124);
		assertEquals(4537, point.iny());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("getPoint")
    public void getPoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		assertEpsilonEquals(new InnerComputationPoint2D(9773.45, 4521.78), point.getPoint());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("getTangent")
    public void getTangent_notangent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		assertEpsilonEquals(new InnerComputationPoint2D(0., 0.), point.getTangent());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("setTangent(Vector2D)")
    public void setTangent_Vector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.setTangent(createVector(4536.6458, 975.124));
		assertEpsilonEquals(new InnerComputationPoint2D(4536.6458, 975.124), point.getTangent());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("setTangent(double, double)")
    public void setTangent_doubledouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.setTangent(4536.6458, 975.124);
		assertEpsilonEquals(new InnerComputationPoint2D(4536.6458, 975.124), point.getTangent());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("setTangent(int, int)")
    public void setTangent_intint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.setTangent(4536, 975);
		assertEpsilonEquals(new InnerComputationPoint2D(4536., 975.), point.getTangent());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("getNormal")
    public void getNormal(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.setTangent(4536.6458, 975.124);
		assertEpsilonEquals(new InnerComputationPoint2D(-975.124, 4536.6458), point.getNormal());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("set(int, int, int, int)")
    public void set_intintintint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.set(45821, 97673, 4536, 975);
		assertEpsilonEquals(new InnerComputationPoint2D(45821., 97673.), point.getPoint());
		assertEpsilonEquals(new InnerComputationPoint2D(4536., 975.), point.getTangent());
		assertEpsilonEquals(new InnerComputationPoint2D(-975., 4536.), point.getNormal());
    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @DisplayName("set(double, double, double, double)")
    public void set_doubledoubledoubledouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		var point = createPoint(9773.45, 4521.78);
		point.set(45821.78, 97673.45, 4536.6458, 975.124);
		assertEpsilonEquals(new InnerComputationPoint2D(45821.78, 97673.45), point.getPoint());
		assertEpsilonEquals(new InnerComputationPoint2D(4536.6458, 975.124), point.getTangent());
		assertEpsilonEquals(new InnerComputationPoint2D(-975.124, 4536.6458), point.getNormal());
    }

}
