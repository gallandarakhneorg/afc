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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.OrientedPoint2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractPoint2DTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractOrientedPoint2DTestCase<P extends OrientedPoint2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		TT extends Tuple2D>
		extends AbstractPoint2DTestCase<P, V, TT> {

    @DisplayName("getTangentX")
    @Nested
    public class GetTangentX {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			assertEpsilonEquals(0., point.getTangentX());
	    }

    }

    @DisplayName("itx")
    @Nested
    public class ITX {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			assertEquals(0, point.itx());
        }

    }

    @DisplayName("setTangentX(int)")
    @Nested
    public class setTangentXInt {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			point.setTangentX(4536);
			assertEpsilonEquals(4536., point.getTangentX());
			assertEquals(4536, point.itx());
        }

    }

    @DisplayName("setTangentX(double)")
    @Nested
    public class setTangentXDouble {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			point.setTangentX(4536.125);
			assertEpsilonEquals(4536.125, point.getTangentX());
			assertEquals(4536, point.itx());
	    }

    }

    @DisplayName("getTangentY")
    @Nested
    public class GetTangentY {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			assertEpsilonEquals(0., point.getTangentY());
        }

    }

    @DisplayName("ity")
    @Nested
    public class ITY {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			assertEquals(0, point.ity());
        }
    }

    @DisplayName("setTangentY(int)")
    @Nested
    public class SetTangentYInt {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			point.setTangentY(4536);
			assertEpsilonEquals(4536., point.getTangentY());
			assertEquals(4536, point.ity());
        }

    }

    @DisplayName("setTangentY(double)")
    @Nested
    public class SetTangentYDouble {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			point.setTangentY(4536.6458);
			assertEpsilonEquals(4536.6458, point.getTangentY());
			assertEquals(4537, point.ity());
        }

    }

    @DisplayName("getNormalX")
    @Nested
    public class GetNormalX {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			assertEpsilonEquals(0., point.getNormalX());
        }

        @DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
    		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		var point = createPoint(9773.45, 4521.78);
    		point.setTangent(4536.6458, 975.124);
    		assertEpsilonEquals(-975.124, point.getNormalX());
        }

    }

    @DisplayName("inx")
    @Nested
    public class INX {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			assertEquals(0, point.inx());
        }

        @DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
    		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		var point = createPoint(9773.45, 4521.78);
    		point.setTangent(4536.6458, 975.124);
    		assertEquals(-975, point.inx());
        }

    }

    @DisplayName("getNormalY")
    @Nested
    public class GetNormalY {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			assertEpsilonEquals(0., point.getNormalY());
        }

        @DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
    		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		var point = createPoint(9773.45, 4521.78);
    		point.setTangent(4536.6458, 975.124);
    		assertEpsilonEquals(4536.6458, point.getNormalY());
        }

    }

    @DisplayName("iny")
    @Nested
    public class INY {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			assertEquals(0, point.iny());
        }

        @DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
    		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		var point = createPoint(9773.45, 4521.78);
    		point.setTangent(4536.6458, 975.124);
    		assertEquals(4537, point.iny());
        }

    }

    @DisplayName("getPoint")
    @Nested
    public class GetPoint {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			assertEpsilonEquals(new InnerComputationPoint2D(9773.45, 4521.78), point.getPoint());
        }

    }

    @DisplayName("getTangent")
    @Nested
    public class GetTangent {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			assertEpsilonEquals(new InnerComputationPoint2D(0., 0.), point.getTangent());
        }

    }

    @DisplayName("setTangent(Vector2D)")
    @Nested
    public class SetTangentVector2D {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			point.setTangent(createVector(4536.6458, 975.124));
			assertEpsilonEquals(new InnerComputationPoint2D(4536.6458, 975.124), point.getTangent());
        }

    }

    @DisplayName("setTangent(double,double)")
    @Nested
    public class SetTangentDoubleDouble {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			point.setTangent(4536.6458, 975.124);
			assertEpsilonEquals(new InnerComputationPoint2D(4536.6458, 975.124), point.getTangent());
        }

    }

    @DisplayName("setTangent(int,int)")
    @Nested
    public class SetTangentIntInt {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			point.setTangent(4536, 975);
			assertEpsilonEquals(new InnerComputationPoint2D(4536., 975.), point.getTangent());
        }

    }

    @DisplayName("getNormal")
    @Nested
    public class GetNormal {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			point.setTangent(4536.6458, 975.124);
			assertEpsilonEquals(new InnerComputationPoint2D(-975.124, 4536.6458), point.getNormal());
        }

    }

    @DisplayName("set(int,int,int,int)")
    @Nested
    public class SetIntIntIntInt {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			point.set(45821, 97673, 4536, 975);
			assertEpsilonEquals(new InnerComputationPoint2D(45821., 97673.), point.getPoint());
			assertEpsilonEquals(new InnerComputationPoint2D(4536., 975.), point.getTangent());
			assertEpsilonEquals(new InnerComputationPoint2D(-975., 4536.), point.getNormal());
        }

    }

    @DisplayName("set(double,double,double,double)")
    @Nested
    public class SetDoubleDoubleDoubleDouble {
	
        @DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var point = createPoint(9773.45, 4521.78);
			point.set(45821.78, 97673.45, 4536.6458, 975.124);
			assertEpsilonEquals(new InnerComputationPoint2D(45821.78, 97673.45), point.getPoint());
			assertEpsilonEquals(new InnerComputationPoint2D(4536.6458, 975.124), point.getTangent());
			assertEpsilonEquals(new InnerComputationPoint2D(-975.124, 4536.6458), point.getNormal());
        }

    }

}
