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

package org.arakhne.afc.math.geometry.d3.tests.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Random;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.PlaneClassification;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.PointVector3DReceiver;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneXY3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.d.Plane3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneXY3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneXZ3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneYZ3d;
import org.arakhne.afc.math.geometry.d3.d.Segment3d;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;

@SuppressWarnings("all")
public abstract class AbstractPlaneXY3dTestCase<T extends PlaneXY3afp<T, ?, ?, ?, ?>, B extends AlignedBox3afp<?, ?, ?, ?, ?, B>> extends AbstractPlane3DTestCase<T, B> {

	private T plane;

	protected final T getP() {
		return this.plane;
	}

	protected abstract T createTestPlane(double z, boolean positive);

	@BeforeEach
	public final void setUp() {
		super.setUp();
		this.plane = createTestPlane(1.25, false);
	}

	@AfterEach
	public final void tearDown() throws Exception {
		this.plane = null;
	}

	@Override
	public final void calculatesPlanePlaneDistance(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void classifiesPlaneAlignedBox(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void findsPlanePlaneIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoublePoint3DPoint3D(
			CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void findsPlanePlaneIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoublePoint3DVector3D(
			CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void findsPlanePlaneIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoublePointVector3DReceiver(
			CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void findsPlanePlaneIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleSegment3afp(
			CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void findsPlanePointProjection(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void findsPlanePointProjectionWithPlaneNormal(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void findsPlaneSegmentIntersection(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void calculatesPlaneSegmentIntersectionFactor(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void calculatesPlaneAlignedBoxDistance(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void calculatesPlanePointDistance(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void calculatesPlaneSphereDistance(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void classifiesPlanePlane(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void classifiesPlanePoint(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void classifiesPlaneSegment(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@Override
	public final void classifiesPlaneSphere(CoordinateSystem3D cs) {
		// Ignore this test function because it is not related to the current type of plane.
	}

	@DisplayName("toGeogebra with negative normal")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void toGeogebra_negative(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals("0.0*x+0.0*y-1.0*z+1.25=0.0", getP().toGeogebra());
	}

	@DisplayName("toGeogebra with positive normal")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void toGeogebra_positive(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PlaneXY3d np = new PlaneXY3d(true, 1.25);
		assertEquals("0.0*x+0.0*y+1.0*z-1.25=0.0", np.toGeogebra());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getZ")
	public final void getZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.25, getP().getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getEquationComponentA")
	public final void getEquationComponentA(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().getEquationComponentA());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getEquationComponentB")
	public final void getEquationComponentB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().getEquationComponentB());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getEquationComponentC")
	public final void getEquationComponentC(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-1., getP().getEquationComponentC());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getEquationComponentD")
	public final void getEquationComponentD(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.25, getP().getEquationComponentD());
	}

	@DisplayName("setZ(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().setZ(123.589);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(123.589, getP().getEquationComponentD());
		assertEpsilonEquals(createPoint(0, 0, 123.589), getP().getPivot());

		getP().setZ(-453.154);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(-453.154, getP().getEquationComponentD());
		assertEpsilonEquals(createPoint(0, 0, -453.154), getP().getPivot());
		
		getP().setPositive(true);

		getP().setZ(123.589);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-123.589, getP().getEquationComponentD());
		assertEpsilonEquals(createPoint(0, 0, 123.589), getP().getPivot());

		getP().setZ(-453.154);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(453.154, getP().getEquationComponentD());
		assertEpsilonEquals(createPoint(0, 0, -453.154), getP().getPivot());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("setPositive")
	public final void setPositiveBoolean(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().setPositive(true);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		assertEpsilonEquals(createPoint(0, 0, 1.25), getP().getPivot());

		getP().setPositive(false);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(1.25, getP().getEquationComponentD());
		assertEpsilonEquals(createPoint(0, 0, 1.25), getP().getPivot());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getNormal")
	public final void getNormal(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(createVector(0., 0., -1.), getP().getNormal());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getNormalX")
	public final void getNormalX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonZero(getP().getNormalX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getNormalY")
	public final void getNormalY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonZero(getP().getNormalY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getNormalZ")
	public final void getNormalZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-1, getP().getNormalZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getPivot")
	public final void getPivot(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(createPoint(0., 0., 1.25), getP().getPivot());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("absolute")
	@Override
	public final void absolute(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().absolute();
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("clear")
	@Override
	public final void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().clear();
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(0., getP().getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("negate")
	public final void negate(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().negate();
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		//
		getP().negate();
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(1.25, getP().getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("calculatesPlaneXYPlaneDistance")
	public final void calculatesPlaneXYPlaneDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3.25, PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 0., 1., -4.5));
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 0., 1., -1.25));
		assertEpsilonEquals(-5.75, PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 0., 1., 4.5));
		assertEpsilonEquals(-5.75, PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 0., -1., -4.5));
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 0., -1., 1.25));
		assertEpsilonEquals(3.25, PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 0., -1., 4.5));
		//
		assertEpsilonEquals(-3.25, PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 0., 1., -4.5));
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 0., 1., -1.25));
		assertEpsilonEquals(5.75, PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 0., 1., 4.5));
		assertEpsilonEquals(5.75, PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 0., -1., -4.5));
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 0., -1., 1.25));
		assertEpsilonEquals(-3.25, PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 0., -1., 4.5));
		//
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 1., 0., 1., -4.5));
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 1., 0., 1., -4.5));
		//
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 0., 1., 1., -4.5));
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 0., 1., 1., -4.5));
		//
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 1., 1., 0., -4.5));
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 1., 1., 0., -4.5));
		//
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(true, 1.25, 1., 1., 1., -4.5));
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPlaneDistance(false, 1.25, 1., 1., 1., -4.5));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("calculatesPlaneXYPointDistance")
	public final void calculatesPlaneXYPointDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-1.25, PlaneXY3afp.calculatesPlaneXYPointDistance(true, 1.25, 0, 0, 0));
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPointDistance(true, 1.25, 0, 0, 1.25));
		assertEpsilonEquals(-11.25, PlaneXY3afp.calculatesPlaneXYPointDistance(true, 1.25, 0, 0, -10));
		assertEpsilonEquals(8.75, PlaneXY3afp.calculatesPlaneXYPointDistance(true, 1.25, 0, 0, 10));
		//
		assertEpsilonEquals(1.25, PlaneXY3afp.calculatesPlaneXYPointDistance(false, 1.25, 0, 0, 0));
		assertEpsilonEquals(0., PlaneXY3afp.calculatesPlaneXYPointDistance(false, 1.25, 0, 0, 1.25));
		assertEpsilonEquals(11.25, PlaneXY3afp.calculatesPlaneXYPointDistance(false, 1.25, 0, 0, -10));
		assertEpsilonEquals(-8.75, PlaneXY3afp.calculatesPlaneXYPointDistance(false, 1.25, 0, 0, 10));
	}

	@DisplayName("findsPlaneXYSegmentIntersection(boolean,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void findsPlaneXYSegmentIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D<?, ?, ?> p = createPoint(0, 0, 0);

		assertFalse(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, -51.2,
				47.1, -7.9, .5,
				p));

		assertFalse(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 47.2,
				47.1, -7.9, 2.,
				p));
	
		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 0.,
				47.1, -7.9, 2.,
				p));
		assertEpsilonEquals(createPoint(41.625, -7.0375, 1.25), p);

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 2.,
				47.1, -7.9, 0.,
				p));
		assertEpsilonEquals(createPoint(37.975, -6.4625, 1.25), p);

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 0.,
				p));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 0.,
				47.1, -7.9, 1.25,
				p));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 2.,
				p));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 2.,
				47.1, -7.9, 1.25,
				p));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 1.25,
				p));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		//

		assertFalse(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, -51.2,
				47.1, -7.9, .5,
				p));

		assertFalse(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 47.2,
				47.1, -7.9, 2.,
				p));

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 0.,
				47.1, -7.9, 2.,
				p));
		assertEpsilonEquals(createPoint(41.625, -7.0375, 1.25), p);

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 2.,
				47.1, -7.9, 0.,
				p));
		assertEpsilonEquals(createPoint(37.975, -6.4625, 1.25), p);

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 0.,
				p));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 0.,
				47.1, -7.9, 1.25,
				p));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 2.,
				p));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 2.,
				47.1, -7.9, 1.25,
				p));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		assertTrue(PlaneXY3afp.findsPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 1.25,
				p));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("calculatesPlaneXYSegmentIntersectionFactor")
	public final void calculatesPlaneXYSegmentIntersectionFactor(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertNaN(PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
				true, 1.25,
				32.5, -5.6, -51.2,
				47.1, -7.9, .5));
		assertNaN(PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
				true, 1.25,
				32.5, -5.6, 47.2,
				47.1, -7.9, 2.));
		assertEpsilonEquals(.625,
				PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
						true, 1.25,
						32.5, -5.6, 0.,
						47.1, -7.9, 2.));
		assertEpsilonEquals(.375,
				PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
						true, 1.25,
						32.5, -5.6, 2.,
						47.1, -7.9, 0.));
		assertEpsilonEquals(0.,
				PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
						true, 1.25,
						32.5, -5.6, 1.25,
						47.1, -7.9, 0.));
		assertEpsilonEquals(1.,
				PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
						true, 1.25,
						32.5, -5.6, 0.,
						47.1, -7.9, 1.25));
		assertEpsilonEquals(0.,
				PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
						true, 1.25,
						32.5, -5.6, 1.25,
						47.1, -7.9, 2.));
		assertEpsilonEquals(1.,
				PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
						true, 1.25,
						32.5, -5.6, 2.,
						47.1, -7.9, 1.25));
		assertInfinity(PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
				true, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 1.25));
		//
		assertNaN(PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
				false, 1.25,
				32.5, -5.6, -51.2,
				47.1, -7.9, .5));
		assertNaN(PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
				false, 1.25,
				32.5, -5.6, 47.2,
				47.1, -7.9, 2.));
		assertEpsilonEquals(.625,
				PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
						false, 1.25,
						32.5, -5.6, 0.,
						47.1, -7.9, 2.));
		assertEpsilonEquals(.375,
				PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
						false, 1.25,
						32.5, -5.6, 2.,
						47.1, -7.9, 0.));
		assertEpsilonEquals(0.,
				PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
						false, 1.25,
						32.5, -5.6, 1.25,
						47.1, -7.9, 0.));
		assertEpsilonEquals(1.,
				PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
						false, 1.25,
						32.5, -5.6, 0., 
						47.1, -7.9, 1.25));
		assertEpsilonEquals(0.,
				PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
						false, 1.25,
						32.5, -5.6, 1.25,
						47.1, -7.9, 2.));
		assertEpsilonEquals(1.,
				PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
						true, 1.25,
						32.5, -5.6, 2.,
						47.1, -7.9, 1.25));
		assertInfinity(PlaneXY3afp.calculatesPlaneXYSegmentIntersectionFactor(
				false, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 1.25));
	}

	@DisplayName("classifiesPlaneXYAlignedBox(boolean,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesPlaneXYAlignedBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYAlignedBox(true, 1.25, 0, 0, 0, 1, 1, 1));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYAlignedBox(true, 1.25, 0, 0, 0, 1, 1, 1.25));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYAlignedBox(true, 1.25, 0, 0, 0, 1, 1, 2));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYAlignedBox(true, 1.25, 2, 2, 2, 3, 3, 3));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYAlignedBox(true, 1.25, 2, 2, 1.25, 3, 3, 3));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYAlignedBox(true, 1.25, 2, 2, 1, 3, 3, 3));
		//
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYAlignedBox(false, 1.25, 0, 0, 0, 1, 1, 1));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYAlignedBox(false, 1.25, 0, 0, 0, 1, 1, 1.25));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYAlignedBox(false, 1.25, 0, 0, 0, 1, 1, 2));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYAlignedBox(false, 1.25, 2, 2, 2, 3, 3, 3));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYAlignedBox(false, 1.25, 2, 2, 1.25, 3, 3, 3));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYAlignedBox(false, 1.25, 2, 2, 1, 3, 3, 3));
	}

	@DisplayName("classifiesPlaneXYPlane(double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesPlaneXYPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 1., -3., 4., -4.));

		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., 1., 4.));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., 1., -4.));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., -1., -4.));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., -1., 4.));

		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., 1., 6.));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., 1., -6.));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., -1., -6.));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPlane(true, 4, 0., 0., -1., 6.));

		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 1., -3., 4., -4.));

		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., 1., 4.));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., 1., -4.));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., -1., -4.));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., -1., 4.));

		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., 1., 6.));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., 1., -6.));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., -1., -6.));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPlane(false, 4, 0., 0., -1., 6.));
	}

	@DisplayName("classifiesPlaneXYPoint(boolean,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesPlaneXYPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPoint(true, 1.25, 0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPoint(true, 1.25, 0, 0, 1.25));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPoint(true, 1.25, 0, 0, -10));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPoint(true, 1.25, 0, 0, 10));
		//
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPoint(false, 1.25, 0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYPoint(false, 1.25, 0, 0, 1.25));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYPoint(false, 1.25, 0, 0, -10));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYPoint(false, 1.25, 0, 0, 10));
	}

	@DisplayName("classifiesPlaneXYSegment(boolean,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesPlaneXYSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSegment(true, 1.25, 0, 0, 0, 1, 1, 1));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSegment(true, 1.25, 0, 0, 0, 1, 1, 1.25));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSegment(true, 1.25, 0, 0, 0, 1, 1, 2));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSegment(true, 1.25, 3, 3, 3, 2, 2, 2));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSegment(true, 1.25, 3, 3, 3, 2, 2, 1.25));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSegment(true, 1.25, 3, 3, 3, 2, 2, 1));
		//
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSegment(false, 1.25, 0, 0, 0, 1, 1, 1));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSegment(false, 1.25, 0, 0, 0, 1, 1, 1.25));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSegment(false, 1.25, 0, 0, 0, 1, 1, 2));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSegment(false, 1.25, 3, 3, 3, 2, 2, 2));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSegment(false, 1.25, 3, 3, 3, 2, 2, 1.25));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSegment(false, 1.25, 3, 3, 3, 2, 2, 1));
	}

	@DisplayName("classifiesPlaneXYSphere(boolean,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesPlaneXYSphere(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSphere(true, 1.25, 0, 0, 0, 1));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSphere(true, 1.25, 0, 0, 0, 1.25));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSphere(true, 1.25, 0, 0, 0, 2));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSphere(true, 1.25, 0, 0, 3, 1));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSphere(true, 1.25, 0, 0, 3, 1.75));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSphere(true, 1.25, 0, 0, 3, 2));
		//
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSphere(false, 1.25, 0, 0, 0, 1));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneXY3afp.classifiesPlaneXYSphere(false, 1.25, 0, 0, 0, 1.25));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSphere(false, 1.25, 0, 0, 0, 2));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSphere(false, 1.25, 0, 0, 3, 1));
		assertSame(PlaneClassification.BEHIND, PlaneXY3afp.classifiesPlaneXYSphere(false, 1.25, 0, 0, 3, 1.75));
		assertSame(PlaneClassification.COINCIDENT, PlaneXY3afp.classifiesPlaneXYSphere(false, 1.25, 0, 0, 3, 2));
	}

	@DisplayName("classifies(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 2)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(2, 2, 1.25, 3, 3, 3)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(2, 2, 1, 3, 3, 3)));
		//
		getP().negate();
		assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 2)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createAlignedBoxFromPoints(2, 2, 1.25, 3, 3, 3)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createAlignedBoxFromPoints(2, 2, 1, 3, 3, 3)));
	}

	@DisplayName("classifies(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPoint(0, 0, 0)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPoint(0, 0, 1.25)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPoint(0, 0, -10)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createPoint(0, 0, 10)));
		//
		getP().negate();
		assertSame(PlaneClassification.BEHIND, getP().classifies(createPoint(0, 0, 0)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createPoint(0, 0, 1.25)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createPoint(0, 0, -10)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createPoint(0, 0, 10)));
	}

	@DisplayName("classifies(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(0, 0, 0, 1)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(0, 0, 0, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(0, 0, 0, 2)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(0, 0, 3, 1)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(0, 0, 3, 1.75)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(0, 0, 3, 2)));
		//
		getP().negate();
		assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(0, 0, 0, 1)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createSphere(0, 0, 0, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(0, 0, 0, 2)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(0, 0, 3, 1)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSphere(0, 0, 3, 1.75)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSphere(0, 0, 3, 2)));
	}

	@DisplayName("findsPlaneXYPlaneIntersection(boolean,double,double,double,double,double,Point3D,Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void findsPlaneXYPlaneIntersectionBooleanDoubleDoubleDoubleDoubleDoublePoint3DVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		final Point3D<?, ?, ?> p = createPoint(0., 0., 0.);
		final Vector3D<?, ?, ?> v = createVector(0., 0., 0.);

		assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0., 1., -4.5, p, v));
		assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0., 1., 4.5, p, v));
		assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0., -1., -4.5, p, v));
		assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0., -1., 4.5, p, v));

		assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0., 1., -1.25, p, v));
		assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0., -1., 1.25, p, v));

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, p, v));
		assertEpsilonEquals(createPoint(0.517766953, 0, 1.25), p);
		assertEpsilonColinear(createVector(0., -1., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, p, v));
		assertEpsilonEquals(createPoint(-3.017766953, 0., 1.25), p);
		assertEpsilonColinear(createVector(0., -1., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, p, v));
		assertEpsilonEquals(createPoint(0, 0.517766953, 1.25), p);
		assertEpsilonColinear(createVector(1., 0., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, p, v));
		assertEpsilonEquals(createPoint(0., -3.017766953, 1.25), p);
		assertEpsilonColinear(createVector(1., 0., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, p, v));
		assertEpsilonEquals(createPoint(0.8838834765, 0.8838834765, 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, p, v));
		assertEpsilonEquals(createPoint(-0.8838834765, -0.8838834765, 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, p, v));
		assertEpsilonEquals(createPoint(0.4575317547, 0.4575317547, 1.25), p);
		assertEpsilonColinear(createVector(1., -1., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, p, v));
		assertEpsilonEquals(createPoint(-1.7075317547, -1.7075317547, 1.25), p);
		assertEpsilonColinear(createVector(1., -1., 0.), v);

		//

		assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0., 1., -4.5, p, v));
		assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0., 1., 4.5, p, v));
		assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0., -1., -4.5, p, v));
		assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0., -1., 4.5, p, v));

		assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0., 1., -1.25, p, v));
		assertFalse(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0., -1., 1.25, p, v));

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, p, v));
		assertEpsilonEquals(createPoint(0.517766953, 0, 1.25), p);
		assertEpsilonColinear(createVector(0., 1., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, p, v));
		assertEpsilonEquals(createPoint(-3.017766953, 0, 1.25), p);
		assertEpsilonColinear(createVector(0., 1., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, p, v));
		assertEpsilonEquals(createPoint(0., 0.517766953, 1.25), p);
		assertEpsilonColinear(createVector(-1., 0., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, p, v));
		assertEpsilonEquals(createPoint(0., -3.017766953, 1.25), p);
		assertEpsilonColinear(createVector(-1., 0., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, p, v));
		assertEpsilonEquals(createPoint(0.8838834765, 0.8838834765, 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, p, v));
		assertEpsilonEquals(createPoint(-0.8838834765, -0.8838834765, 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, p, v));
		assertEpsilonEquals(createPoint(0.4575317547, 0.4575317547, 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, p, v));
		assertEpsilonEquals(createPoint(-1.7075317547, -1.7075317547, 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);
	}

	@DisplayName("classifies(x,y,z)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 1.25));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, -10));
		assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 10));
		//
		getP().negate();
		assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 1.25));
		assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, -10));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 10));
	}

	@DisplayName("classifies(x,y,z,radius)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 0, 1));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 0, 1.25));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 0, 2));
		assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 3, 1));
		assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 3, 1.75));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 3, 2));
		//
		getP().negate();
		assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0, 1));
		assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0, 1.25));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 0, 2));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 3, 1));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 3, 1.75));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 3, 2));
	}

	@DisplayName("classifies(x1,y1,z1,x2,y2,z2)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 0, 1, 1, 1));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(0, 0, 0, 1, 1, 1.25));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 0, 1, 1, 2));
		assertSame(PlaneClassification.BEHIND, getP().classifies(2, 2, 2, 3, 3, 3));
		assertSame(PlaneClassification.BEHIND, getP().classifies(2, 2, 1.25, 3, 3, 3));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(2, 2, 1, 3, 3, 3));
		//
		getP().negate();
		assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0, 1, 1, 1));
		assertSame(PlaneClassification.BEHIND, getP().classifies(0, 0, 0, 1, 1, 1.25));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(0, 0, 0, 1, 1, 2));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(2, 2, 2, 3, 3, 3));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(2, 2, 1.25, 3, 3, 3));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(2, 2, 1, 3, 3, 3));
	}

	@DisplayName("classifies(Plane3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(1., -3., 4., -4.)));
		
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(0., 0., 1., -1.25)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(0., 0., 1., 1.25)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(0., 0., -1., 1.25)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(0., 0., -1., -1.25)));
		
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(0., 0., 1., 6.)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(0., 0., 1., -6.)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(0., 0., -1., -6.)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(0., 0., -1., 6.)));

		getP().negate();
		
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(1., -3., 4., -4.)));
		
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(0., 0., 1., -1.25)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(0., 0., 1., 1.25)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(new Plane3d(0., 0., -1., 1.25)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(0., 0., -1., -1.25)));
		
		assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(0., 0., 1., 6.)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(0., 0., 1., -6.)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(new Plane3d(0., 0., -1., -6.)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(new Plane3d(0., 0., -1., 6.)));
	}

	@DisplayName("classifies(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void classifiesSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(0, 0, 0, 1, 1, 1)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(0, 0, 0, 1, 1, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0, 0, 0, 1, 1, 2)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(3, 3, 3, 2, 2, 2)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(3, 3, 3, 2, 2, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(3, 3, 3, 2, 2, 1)));
		//
		getP().negate();
		assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(0, 0, 0, 1, 1, 1)));
		assertSame(PlaneClassification.BEHIND, getP().classifies(createSegment(0, 0, 0, 1, 1, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(0, 0, 0, 1, 1, 2)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(3, 3, 3, 2, 2, 2)));
		assertSame(PlaneClassification.IN_FRONT_OF, getP().classifies(createSegment(3, 3, 3, 2, 2, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, getP().classifies(createSegment(3, 3, 3, 2, 2, 1)));
	}

	@DisplayName("getDistanceTo(Plane3D) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceToPlane3D_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(true, 4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(true, 1.25)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(true, -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(false, -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(false, 1.25)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(false, 4.5)));
		//
		getP().negate();
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(true, 4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(true, 1.25)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(true, -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(false, -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(false, 1.25)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXZ3d(false, 4.5)));
	}

	@DisplayName("getDistanceTo(Plane3D) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceToPlane3D_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-3.25, getP().getDistanceTo(new PlaneXY3d(true, 4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(true, 1.25)));
		assertEpsilonEquals(5.75, getP().getDistanceTo(new PlaneXY3d(true, -4.5)));
		assertEpsilonEquals(5.75, getP().getDistanceTo(new PlaneXY3d(false, -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(false, 1.25)));
		assertEpsilonEquals(-3.25, getP().getDistanceTo(new PlaneXY3d(false, 4.5)));
		//
		getP().negate();
		assertEpsilonEquals(3.25, getP().getDistanceTo(new PlaneXY3d(true, 4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(true, 1.25)));
		assertEpsilonEquals(-5.75, getP().getDistanceTo(new PlaneXY3d(true, -4.5)));
		assertEpsilonEquals(-5.75, getP().getDistanceTo(new PlaneXY3d(false, -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneXY3d(false, 1.25)));
		assertEpsilonEquals(3.25, getP().getDistanceTo(new PlaneXY3d(false, 4.5)));
	}

	@DisplayName("getDistanceTo(Plane3D) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceToPlane3D_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(true, 4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(true, 1.25)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(true, -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(false, -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(false, 1.25)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(false, 4.5)));
		//
		getP().negate();
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(true, 4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(true, 1.25)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(true, -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(false, -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(false, 1.25)));
		assertEpsilonEquals(0., getP().getDistanceTo(new PlaneYZ3d(false, 4.5)));
	}

	@DisplayName("this .. Plane3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_upToPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(0., 0., -1., 1.25)));
		assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(0., 0., 1., -1.25)));
		assertEpsilonEquals(-3.25, getP().operator_upTo(new Plane3d(0., 0., 1., -4.5)));
		assertEpsilonEquals(5.75, getP().operator_upTo(new Plane3d(0., 0., 1., 4.5)));
		assertEpsilonEquals(5.75, getP().operator_upTo(new Plane3d(0., 0., -1., -4.5)));
		assertEpsilonEquals(-3.25, getP().operator_upTo(new Plane3d(0., 0., -1., 4.5)));
		//
		assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 0., 1., -4.5)));
		assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(0., 1., 1., -4.5)));
		assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 1., 0., -4.5)));
		assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 1., 1., -4.5)));
		//
		assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 0., 1., -4.5)));
		assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(0., 1., 1., -4.5)));
		assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 1., 0., -4.5)));
		assertEpsilonEquals(0., getP().operator_upTo(new Plane3d(1., 1., 1., -4.5)));
	}

	@DisplayName("getDistanceTo(Plane3D) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceToPlane3D_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 0., 1., -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(0., 1., 1., -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 1., 0., -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 1., 1., -4.5)));
		//
		assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 0., 1., -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(0., 1., 1., -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 1., 0., -4.5)));
		assertEpsilonEquals(0., getP().getDistanceTo(new Plane3d(1., 1., 1., -4.5)));
	}

	@DisplayName("this .. Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.25, getP().operator_upTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(0., getP().operator_upTo(createPoint(0, 0, 1.25)));
		assertEpsilonEquals(11.25, getP().operator_upTo(createPoint(0, 0, -10)));
		assertEpsilonEquals(-8.75, getP().operator_upTo(createPoint(0, 0, 10)));
	}

	@DisplayName("getDistanceTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.25, getP().getDistanceTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(0., getP().getDistanceTo(createPoint(0, 0, 1.25)));
		assertEpsilonEquals(11.25, getP().getDistanceTo(createPoint(0, 0, -10)));
		assertEpsilonEquals(-8.75, getP().getDistanceTo(createPoint(0, 0, 10)));
	}

	@DisplayName("getDistanceTo(x, y, z)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceToDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.25, getP().getDistanceTo(0, 0, 0));
		assertEpsilonEquals(0., getP().getDistanceTo(0, 0, 1.25));
		assertEpsilonEquals(11.25, getP().getDistanceTo(0, 0, -10));
		assertEpsilonEquals(-8.75, getP().getDistanceTo(0, 0, 10));
	}

	@DisplayName("getDistanceTo(double,double,double,double) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceToDoubleDoubleDoubleDouble_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 0., -1.25));
		assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., 4.5));
		assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., -1., 0., 1.25));
		assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., 4.5));
		//
		getP().negate();
		assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 0., -1.25));
		assertEpsilonEquals(0, getP().getDistanceTo(0., 1., 0., 4.5));
		assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., -1., 0., 1.25));
		assertEpsilonEquals(0, getP().getDistanceTo(0., -1., 0., 4.5));
	}

	@DisplayName("getDistanceTo(double,double,double,double) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceToDoubleDoubleDoubleDouble_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-3.25, getP().getDistanceTo(0., 0., 1., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., -1.25));
		assertEpsilonEquals(5.75, getP().getDistanceTo(0., 0., 1., 4.5));
		assertEpsilonEquals(5.75, getP().getDistanceTo(0., 0., -1., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., 1.25));
		assertEpsilonEquals(-3.25, getP().getDistanceTo(0., 0., -1., 4.5));
		//
		getP().negate();
		assertEpsilonEquals(3.25, getP().getDistanceTo(0., 0., 1., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 0., 1., -1.25));
		assertEpsilonEquals(-5.75, getP().getDistanceTo(0., 0., 1., 4.5));
		assertEpsilonEquals(-5.75, getP().getDistanceTo(0., 0., -1., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 0., -1., 1.25));
		assertEpsilonEquals(3.25, getP().getDistanceTo(0., 0., -1., 4.5));
	}

	@DisplayName("getDistanceTo(double,double,double,double) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceToDoubleDoubleDoubleDouble_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -1.25));
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., 4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 1.25));
		assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 4.5));
		//
		getP().negate();
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., -1.25));
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 0., 4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 1.25));
		assertEpsilonEquals(0., getP().getDistanceTo(-1., 0., 0., 4.5));
	}

	@DisplayName("getDistanceTo(double,double,double,double) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceToDoubleDoubleDoubleDouble_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 1., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 1., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(1., 1., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(1., 1., 1., -4.5));
		//
		getP().negate();
		assertEpsilonEquals(0., getP().getDistanceTo(1., 0., 1., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(0., 1., 1., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(1., 1., 0., -4.5));
		assertEpsilonEquals(0., getP().getDistanceTo(1., 1., 1., -4.5));
	}

	@DisplayName("getIntersection(double,double,double,double) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getIntersectionDoubleDoubleDoubleDouble_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;

		s = getP().getIntersection(0.707106781373, 0.707106781373, 0., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0.8838834765, 0.8838834765, 1.25), s.getP1());
		assertEpsilonColinear(createVector(-1., 1., 0.), s.getDirection());

		s = getP().getIntersection(0.707106781373, 0.707106781373, 0., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-0.8838834765, -0.8838834765, 1.25), s.getP1());
		assertEpsilonColinear(createVector(-1., 1., 0.), s.getDirection());

		s = getP().getIntersection(0., 0.707106781373, 0.707106781373, -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 0.517766953, 1.25), s.getP1());
		assertEpsilonColinear(createVector(-1., 0., 0.), s.getDirection());

		s = getP().getIntersection(0., 0.707106781373, 0.707106781373, 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., -3.017766953, 1.25), s.getP1());
		assertEpsilonColinear(createVector(-1., 0., 0.), s.getDirection());

		s = getP().getIntersection(0.707106781373, 0., 0.707106781373, -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0.517766953, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = getP().getIntersection(0.707106781373, 0., 0.707106781373, 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-3.017766953, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = getP().getIntersection(0.577350269, 0.577350269, 0.577350269, -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0.4575317547, 0.4575317547, 1.25), s.getP1());
		assertEpsilonColinear(createVector(-1., 1., 0.), s.getDirection());

		s = getP().getIntersection(0.577350269, 0.577350269, 0.577350269, 1.25);
		assertEpsilonEquals(createPoint(-1.7075317547, -1.7075317547, 1.25), s.getP1());
		assertEpsilonColinear(createVector(-1., 1., 0.), s.getDirection());
	}

	@DisplayName("getIntersection(double,double,double,double) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getIntersectionDoubleDoubleDoubleDouble_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		final Segment3afp<?, ?, ?, ?, ?, ?, ?> s;

		assertNull(getP().getIntersection(0., 0., 1., -4.5));
		assertNull(getP().getIntersection(0., 0., 1., 4.5));
		assertNull(getP().getIntersection(0., 0., -1., -4.5));
		assertNull(getP().getIntersection(0., 0., -1., -4.5));

		assertNull(getP().getIntersection(0., 0., 1., -1.25));
		assertNull(getP().getIntersection(0., 0., -1., 1.25));

		//
		getP().negate();
		assertNull(getP().getIntersection(0., 0., 1., -4.5));
		assertNull(getP().getIntersection(0., 0., 1., 4.5));
		assertNull(getP().getIntersection(0., 0., -1., 4.5));
		assertNull(getP().getIntersection(0., 0., -1., -4.5));

		assertNull(getP().getIntersection(0., 0., 1., -1.25));
		assertNull(getP().getIntersection(0., 0., -1., 1.25));
	}

	@DisplayName("getIntersection(double,double,double,double) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getIntersectionDoubleDoubleDoubleDouble_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3d s;

		assertNull(getP().getIntersection(0., 0., 1., -4.5));

		assertNull(getP().getIntersection(0., 0., 1., 4.5));

		assertNull(getP().getIntersection(0., 0., -1., -4.5));

		assertNull(getP().getIntersection(0., 0., -1., -4.5));

		assertNull(getP().getIntersection(0., 0., 1., -1.25));

		assertNull(getP().getIntersection(0., 0., -1., 1.25));

		//
		getP().negate();

		assertNull(getP().getIntersection(0., 0., 1., -4.5));

		assertNull(getP().getIntersection(0., 0., 1., 4.5));

		assertNull(getP().getIntersection(0., 0., -1., 4.5));

		assertNull(getP().getIntersection(0., 0., -1., -4.5));

		assertNull(getP().getIntersection(0., 0., 1., -1.25));

		assertNull(getP().getIntersection(0., 0., -1., 1.25));
	}

	@DisplayName("getIntersection(double,double,double,double) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getIntersectionDoubleDoubleDoubleDouble_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;

		s = getP().getIntersection(1., 0., 0., -4.5);
		assertEpsilonEquals(createPoint(4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = getP().getIntersection(1., 0., 0., 4.5);
		assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = getP().getIntersection(-1., 0., 0., -4.5);
		assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = getP().getIntersection(-1., 0., 0., -4.5);
		assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = getP().getIntersection(1., 0., 0., -1.25);
		assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = getP().getIntersection(-1., 0., 0., 1.25);
		assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		//
		getP().negate();

		s = getP().getIntersection(1., 0., 0., -4.5);
		assertEpsilonEquals(createPoint(4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = getP().getIntersection(1., 0., 0., 4.5);
		assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = getP().getIntersection(-1., 0., 0., 4.5);
		assertEpsilonEquals(createPoint(4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = getP().getIntersection(-1., 0., 0., -4.5);
		assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = getP().getIntersection(1., 0., 0., -1.25);
		assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = getP().getIntersection(-1., 0., 0., 1.25);
		assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
	}

	@DisplayName("getIntersection(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getIntersectionSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D<?, ?, ?> p;

		assertNull(getP().getIntersection(createSegment(
				32.5, -5.6, -51.2,
				47.1, -7.9, .5)));

		assertNull(getP().getIntersection(createSegment(
				32.5, -5.6, 47.2,
				47.1, -7.9, 2.)));

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 0.,
				47.1, -7.9, 2.));
		assertEpsilonEquals(createPoint(41.625, -7.0375, 1.25), p);

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 2.,
				47.1, -7.9, 0.));
		assertEpsilonEquals(createPoint(37.975, -6.4625, 1.25), p);

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 1.25,
				47.1, -7.9, 0.));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 0.,
				47.1, -7.9, 1.25));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 1.25,
				47.1, -7.9, 2.));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 1.25,
				47.1, -7.9, 1.25));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		//

		getP().negate();
		assertNull(getP().getIntersection(createSegment(
				32.5, -5.6, -51.2,
				47.1, -7.9, .5)));

		assertNull(getP().getIntersection(createSegment(
				32.5, -5.6, 47.2,
				47.1, -7.9, 2.)));
	
		p = getP().getIntersection(createSegment(
				32.5, -5.6, 0.,
				47.1, -7.9, 2.));
		assertEpsilonEquals(createPoint(41.625, -7.0375, 1.25), p);

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 2.,
				47.1, -7.9, 0.));
		assertEpsilonEquals(createPoint(37.975, -6.4625, 1.25), p);

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 1.25,
				47.1, -7.9, 0.));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 0.,
				47.1, -7.9, 1.25));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 1.25,
				47.1, -7.9, 2.));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 2.,
				47.1, -7.9, 1.25));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 1.25,
				47.1, -7.9, 1.25));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		p = getP().getIntersection(createSegment(
				32.5, -5.6, 2.,
				47.1, -7.9, 1.25));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);
	}

	@DisplayName("this && Box3afp")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		assertFalse(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1.25)));
		assertTrue(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 2)));
		assertFalse(getP().operator_and(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		assertFalse(getP().operator_and(createAlignedBoxFromPoints(2, 2, 1.25, 3, 3, 3)));
		assertTrue(getP().operator_and(createAlignedBoxFromPoints(2, 2, 1, 3, 3, 3)));
		//
		getP().negate();
		assertFalse(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		assertFalse(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1.25)));
		assertTrue(getP().operator_and(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 2)));
		assertFalse(getP().operator_and(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		assertFalse(getP().operator_and(createAlignedBoxFromPoints(2, 2, 1.25, 3, 3, 3)));
		assertTrue(getP().operator_and(createAlignedBoxFromPoints(2, 2, 1, 3, 3, 3)));
	}

	@DisplayName("intersects(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void intersectsBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		assertFalse(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1.25)));
		assertTrue(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 2)));
		assertFalse(getP().intersects(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		assertFalse(getP().intersects(createAlignedBoxFromPoints(2, 2, 1.25, 3, 3, 3)));
		assertTrue(getP().intersects(createAlignedBoxFromPoints(2, 2, 1, 3, 3, 3)));
		//
		getP().negate();
		assertFalse(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1)));
		assertFalse(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 1.25)));
		assertTrue(getP().intersects(createAlignedBoxFromPoints(0, 0, 0, 1, 1, 2)));
		assertFalse(getP().intersects(createAlignedBoxFromPoints(2, 2, 2, 3, 3, 3)));
		assertFalse(getP().intersects(createAlignedBoxFromPoints(2, 2, 1.25, 3, 3, 3)));
		assertTrue(getP().intersects(createAlignedBoxFromPoints(2, 2, 1, 3, 3, 3)));
	}

	@DisplayName("intersects(Plane3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// 1) Parallel, distinct plane -> no intersection
		//    z = 2  => 0*x+0*y-1*z+2 = 0
		assertFalse(getP().intersects(createPlane(0, 0, -1, 2)));

		// 2) Opposite normal, same geometric plane -> intersects (coincident)
		//    z = 1.25 => 0*x+0*y+1*z-1.25 = 0
		assertTrue(getP().intersects(createPlane(0, 0, 1, -1.25)));

		// 2) Orthogonal plane X=0 -> intersects (line parallel to Y axis at z=1.25)
		assertTrue(getP().intersects(createPlane(1, 0, 0, 0)));

		// 4) Orthogonal plane Y=0 -> intersects (line parallel to X axis at z=1.25)
		assertTrue(getP().intersects(createPlane(0, 1, 0, 0)));

		// 5) Oblique plane -> intersects
		//    x + y + z - 3 = 0
		assertTrue(getP().intersects(createPlane(1, 1, 1, -3)));

		// 6) Another parallel, distinct plane (same normal direction)
		//    z = -4 => 0*x+0*y-1*z-4 = 0
		assertFalse(getP().intersects(createPlane(0, 0, -1, -4)));

		// 7) Nearly parallel but not parallel (small x component in normal) -> intersects
		//    1e-12*x - z + 1.25 = 0
		assertTrue(getP().intersects(createPlane(1e-12, 0, -1, 1.25)));
	}

	@DisplayName("intersects(x, y, z)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void intersectsDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(0, 0, 0));
		assertTrue(getP().intersects(0, 0, 1.25));
		assertFalse(getP().intersects(0, 0, -10));
		assertFalse(getP().intersects(0, 0, 10));
		//
		getP().negate();
		assertFalse(getP().intersects(0, 0, 0));
		assertTrue(getP().intersects(0, 0, 1.25));
		assertFalse(getP().intersects(0, 0, -10));
		assertFalse(getP().intersects(0, 0, 10));
	}

	@DisplayName("this && Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().operator_and(createPoint(0, 0, 0)));
		assertTrue(getP().operator_and(createPoint(0, 0, 1.25)));
		assertFalse(getP().operator_and(createPoint(0, 0, -10)));
		assertFalse(getP().operator_and(createPoint(0, 0, 10)));
		//
		getP().negate();
		assertFalse(getP().operator_and(createPoint(0, 0, 0)));
		assertTrue(getP().operator_and(createPoint(0, 0, 1.25)));
		assertFalse(getP().operator_and(createPoint(0, 0, -10)));
		assertFalse(getP().operator_and(createPoint(0, 0, 10)));
	}

	@DisplayName("intersects(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void intersectsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(createPoint(0, 0, 0)));
		assertTrue(getP().intersects(createPoint(0, 0, 1.25)));
		assertFalse(getP().intersects(createPoint(0, 0, -10)));
		assertFalse(getP().intersects(createPoint(0, 0, 10)));
		//
		getP().negate();
		assertFalse(getP().intersects(createPoint(0, 0, 0)));
		assertTrue(getP().intersects(createPoint(0, 0, 1.25)));
		assertFalse(getP().intersects(createPoint(0, 0, -10)));
		assertFalse(getP().intersects(createPoint(0, 0, 10)));
	}

	@DisplayName("intersects(x, y, z, radius)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void intersectsDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(0, 0, 0, 1));
		assertFalse(getP().intersects(0, 0, 0, 1.25));
		assertTrue(getP().intersects(0, 0, 0, 2));
		assertFalse(getP().intersects(0, 0, 3, 1));
		assertFalse(getP().intersects(0, 0, 3, 1.75));
		assertTrue(getP().intersects(0, 0, 3, 2));
		//
		getP().negate();
		assertFalse(getP().intersects(0, 0, 0, 1));
		assertFalse(getP().intersects(0, 0, 0, 1.25));
		assertTrue(getP().intersects(0, 0, 0, 2));
		assertFalse(getP().intersects(0, 0, 3, 1));
		assertFalse(getP().intersects(0, 0, 3, 1.75));
		assertTrue(getP().intersects(0, 0, 3, 2));
	}

	@DisplayName("intersects(lx,ly,lz, ux,uy,uz)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void intersectsDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(0, 0, 0, 1, 1, 1));
		assertFalse(getP().intersects(0, 0, 0, 1, 1, 1.25));
		assertTrue(getP().intersects(0, 0, 0, 1, 1, 2));
		assertFalse(getP().intersects(2, 2, 2, 3, 3, 3));
		assertFalse(getP().intersects(2, 2, 1.25, 3, 3, 3));
		assertTrue(getP().intersects(2, 2, 1, 3, 3, 3));
		//
		getP().negate();
		assertFalse(getP().intersects(0, 0, 0, 1, 1, 1));
		assertFalse(getP().intersects(0, 0, 0, 1, 1, 1.25));
		assertTrue(getP().intersects(0, 0, 0, 1, 1, 2));
		assertFalse(getP().intersects(2, 2, 2, 3, 3, 3));
		assertFalse(getP().intersects(2, 2, 1.25, 3, 3, 3));
		assertTrue(getP().intersects(2, 2, 1, 3, 3, 3));
	}

	@DisplayName("this && Segment3afp")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().operator_and(createSegment(0, 0, 0, 1, 1, 1)));
		assertFalse(getP().operator_and(createSegment(0, 0, 0, 1, 1, 1.25)));
		assertTrue(getP().operator_and(createSegment(0, 0, 0, 1, 1, 2)));
		assertFalse(getP().operator_and(createSegment(3, 3, 3, 2, 2, 2)));
		assertFalse(getP().operator_and(createSegment(3, 3, 3, 2, 2, 1.25)));
		assertTrue(getP().operator_and(createSegment(3, 3, 3, 2, 2, 1)));
		//
		assertFalse(getP().operator_and(createSegment(0, 0, 0, 1, 1, 1)));
		assertFalse(getP().operator_and(createSegment(0, 0, 0, 1, 1, 1.25)));
		assertTrue(getP().operator_and(createSegment(0, 0, 0, 1, 1, 2)));
		assertFalse(getP().operator_and(createSegment(3, 3, 3, 2, 2, 2)));
		assertFalse(getP().operator_and(createSegment(3, 3, 3, 2, 2, 1.25)));
		assertTrue(getP().operator_and(createSegment(3, 3, 3, 2, 2, 1)));
	}

	@DisplayName("intersects(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void intersectsSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(createSegment(0, 0, 0, 1, 1, 1)));
		assertFalse(getP().intersects(createSegment(0, 0, 0, 1, 1, 1.25)));
		assertTrue(getP().intersects(createSegment(0, 0, 0, 1, 1, 2)));
		assertFalse(getP().intersects(createSegment(3, 3, 3, 2, 2, 2)));
		assertFalse(getP().intersects(createSegment(3, 3, 3, 2, 2, 1.25)));
		assertTrue(getP().intersects(createSegment(3, 3, 3, 2, 2, 1)));
		//
		assertFalse(getP().intersects(createSegment(0, 0, 0, 1, 1, 1)));
		assertFalse(getP().intersects(createSegment(0, 0, 0, 1, 1, 1.25)));
		assertTrue(getP().intersects(createSegment(0, 0, 0, 1, 1, 2)));
		assertFalse(getP().intersects(createSegment(3, 3, 3, 2, 2, 2)));
		assertFalse(getP().intersects(createSegment(3, 3, 3, 2, 2, 1.25)));
		assertTrue(getP().intersects(createSegment(3, 3, 3, 2, 2, 1)));
	}

	@DisplayName("this && Sphere3afp")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().operator_and(createSphere(0, 0, 0, 1)));
		assertFalse(getP().operator_and(createSphere(0, 0, 0, 1.25)));
		assertTrue(getP().operator_and(createSphere(0, 0, 0, 2)));
		assertFalse(getP().operator_and(createSphere(0, 0, 3, 1)));
		assertFalse(getP().operator_and(createSphere(0, 0, 3, 1.75)));
		assertTrue(getP().operator_and(createSphere(0, 0, 3, 2)));
		//
		getP().negate();
		assertFalse(getP().operator_and(createSphere(0, 0, 0, 1)));
		assertFalse(getP().operator_and(createSphere(0, 0, 0, 1.25)));
		assertTrue(getP().operator_and(createSphere(0, 0, 0, 2)));
		assertFalse(getP().operator_and(createSphere(0, 0, 3, 1)));
		assertFalse(getP().operator_and(createSphere(0, 0, 3, 1.75)));
		assertTrue(getP().operator_and(createSphere(0, 0, 3, 2)));
	}

	@DisplayName("intersects(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void intersectsSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().intersects(createSphere(0, 0, 0, 1)));
		assertFalse(getP().intersects(createSphere(0, 0, 0, 1.25)));
		assertTrue(getP().intersects(createSphere(0, 0, 0, 2)));
		assertFalse(getP().intersects(createSphere(0, 0, 3, 1)));
		assertFalse(getP().intersects(createSphere(0, 0, 3, 1.75)));
		assertTrue(getP().intersects(createSphere(0, 0, 3, 2)));
		//
		getP().negate();
		assertFalse(getP().intersects(createSphere(0, 0, 0, 1)));
		assertFalse(getP().intersects(createSphere(0, 0, 0, 1.25)));
		assertTrue(getP().intersects(createSphere(0, 0, 0, 2)));
		assertFalse(getP().intersects(createSphere(0, 0, 3, 1)));
		assertFalse(getP().intersects(createSphere(0, 0, 3, 1.75)));
		assertTrue(getP().intersects(createSphere(0, 0, 3, 2)));
	}

	@DisplayName("getIntersection(Plane3D) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getIntersectionPlane3D_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp<?, ?, ?, ?, ?, ?, ?> s;

		s = getP().getIntersection(new PlaneXZ3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = getP().getIntersection(new PlaneXZ3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., -4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = getP().getIntersection(new PlaneXZ3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = getP().getIntersection(new PlaneXZ3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., -4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = getP().getIntersection(new PlaneXZ3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 1.25, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = getP().getIntersection(new PlaneXZ3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 1.25, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		//
		getP().negate();
		s = getP().getIntersection(new PlaneXZ3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = getP().getIntersection(new PlaneXZ3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., -4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = getP().getIntersection(new PlaneXZ3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = getP().getIntersection(new PlaneXZ3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., -4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = getP().getIntersection(new PlaneXZ3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 1.25, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = getP().getIntersection(new PlaneXZ3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 1.25, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
	}

	@DisplayName("getIntersection(Plane3D) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getIntersectionPlane3D_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertNull(getP().getIntersection(new PlaneXY3d(true, 4.5)));
		assertNull(getP().getIntersection(new PlaneXY3d(true, -4.5)));
		assertNull(getP().getIntersection(new PlaneXY3d(false, 4.5)));
		assertNull(getP().getIntersection(new PlaneXY3d(false, -4.5)));

		assertNull(getP().getIntersection(new PlaneXY3d(true, 1.25)));
		assertNull(getP().getIntersection(new PlaneXY3d(false, 1.25)));

		//
		getP().negate();
		assertNull(getP().getIntersection(new PlaneXY3d(true, 4.5)));
		assertNull(getP().getIntersection(new PlaneXY3d(true, -4.5)));
		assertNull(getP().getIntersection(new PlaneXY3d(false, 4.5)));
		assertNull(getP().getIntersection(new PlaneXY3d(false, -4.5)));

		assertNull(getP().getIntersection(new PlaneXY3d(true, 1.25)));
		assertNull(getP().getIntersection(new PlaneXY3d(false, 1.25)));
	}

	@DisplayName("getIntersection(Plane3D) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getIntersectionPlane3D_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp<?, ?, ?, ?, ?, ?, ?> s;

		s = getP().getIntersection(new PlaneYZ3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = getP().getIntersection(new PlaneYZ3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = getP().getIntersection(new PlaneYZ3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = getP().getIntersection(new PlaneYZ3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = getP().getIntersection(new PlaneYZ3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = getP().getIntersection(new PlaneYZ3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		//
		getP().negate();
		s = getP().getIntersection(new PlaneYZ3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = getP().getIntersection(new PlaneYZ3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = getP().getIntersection(new PlaneYZ3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = getP().getIntersection(new PlaneYZ3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = getP().getIntersection(new PlaneYZ3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = getP().getIntersection(new PlaneYZ3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
	}

	private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, Segment3afp<?, ?, ?, ?, ?, ?, ?> s) {
		assertEpsilonEquals(p, s.getP1());
		var v0 = s.getP2().operator_minus(s.getP1());
		assertEpsilonColinear(v, v0);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("findsPlaneXYPlaneIntersection(bool,double,double,double,double,double,Segment3afp")
	public final void findsPlaneXYPlaneIntersectionBooleanDoubleDoubleDoubleDoubleDoubleSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		final var s = createSegment(0, 0, 0, 0, 0, 0);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, s));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, s));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, s));
		assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, s));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, s));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, s));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, s));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, s));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), s);

		//
		
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, s));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, s));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, s));
		assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, s));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, s));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, s));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, s));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), s);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, s));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), s);
	}

	private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, Point3D<?, ?, ?> a1, Point3D<?, ?, ?> a2) {
		assertEpsilonEquals(p, a1);
		var v0 = a2.operator_minus(a1);
		assertEpsilonColinear(v, v0);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("findsPlaneXYPlaneIntersection(bool,double,double,double,double,double,Point3D,Point3D")
	public final void findsPlaneXYPlaneIntersectionBooleanDoubleDoubleDoubleDoubleDoublePoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		final var p1 = createPoint(0, 0, 0);
		final var p2 = createPoint(0, 0, 0);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), p1, p2);

		//
		
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0., 0.707106781373, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, p1, p2));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), p1, p2);

		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, p1, p2));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), p1, p2);
	}

	private void assertReceiverInvoked(Point3D<?, ?, ?> p, Vector3D<?, ?, ?> v, PointVector3DReceiver r) {
		final var px = ArgumentCaptor.forClass(double.class);
		final var py = ArgumentCaptor.forClass(double.class);
		final var pz = ArgumentCaptor.forClass(double.class);
		final var vx = ArgumentCaptor.forClass(double.class);
		final var vy = ArgumentCaptor.forClass(double.class);
		final var vz = ArgumentCaptor.forClass(double.class);
		verify(r).set(px.capture(), py.capture(), pz.capture(), vx.capture(), vy.capture(), vz.capture());
		assertEpsilonEquals(p, createPoint(px.getValue(), py.getValue(), pz.getValue()));
		assertEpsilonColinear(v, createVector(vx.getValue(), vy.getValue(), vz.getValue()));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("findsPlaneXYPlaneIntersection(bool,double,double,double,double,double,PointVector3DReceiver")
	public final void findsPlaneXYPlaneIntersectionBooleanDoubleDoubleDoubleDoubleDoublePointVector3DReceiver(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		var r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., -1.25, r));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, r));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, r));
		assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, r));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0, 0.707106781373, -1.25, r));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.707106781373, 0, 0.707106781373, 1.25, r));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, r));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(1., -1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(true, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, r));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(1., -1., 0.), r);

		//

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0, -1.25, r));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0.707106781373, 0., 1.25, r));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, -1.25, r));
		assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0., 0.707106781373, 0.707106781373, 1.25, r));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), r);
		
		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0, 0.707106781373, -1.25, r));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.707106781373, 0, 0.707106781373, 1.25, r));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, -1.25, r));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), r);

		r = mock(PointVector3DReceiver.class);
		assertTrue(PlaneXY3afp.findsPlaneXYPlaneIntersection(false, 1.25, 0.577350269, 0.577350269, 0.577350269, 1.25, r));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), r);
	}

	@DisplayName("this && Plane3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		getP().setPositive(true);
		
		assertTrue(getP().operator_and(new Plane3d(1., 1., 0., -1.25)));
		assertTrue(getP().operator_and(new Plane3d(1., 1., 0., 1.25)));
		assertTrue(getP().operator_and(new Plane3d(0., 1., 1., -1.25)));
		assertTrue(getP().operator_and(new Plane3d(0., 1., 1., 1.25)));
		assertTrue(getP().operator_and(new Plane3d(1., 0., 1., -1.25)));
		assertTrue(getP().operator_and(new Plane3d(1., 0., 1., 1.25)));
		assertTrue(getP().operator_and(new Plane3d(1., 1., 1., -1.25)));
		assertTrue(getP().operator_and(new Plane3d(1., 1., 1., 1.25)));

		//
		getP().setPositive(false);
		
		assertTrue(getP().operator_and(new Plane3d(1., 1., 0., -1.25)));
		assertTrue(getP().operator_and(new Plane3d(1., 1., 0., 1.25)));
		assertTrue(getP().operator_and(new Plane3d(0., 1., 1., -1.25)));
		assertTrue(getP().operator_and(new Plane3d(0., 1., 1., 1.25)));
		assertTrue(getP().operator_and(new Plane3d(1., 0., 1., -1.25)));
		assertTrue(getP().operator_and(new Plane3d(1., 0., 1., 1.25)));
		assertTrue(getP().operator_and(new Plane3d(1., 1., 1., -1.25)));
		assertTrue(getP().operator_and(new Plane3d(1., 1., 1., 1.25)));
	}

	@DisplayName("getIntersection(Plane3D) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getIntersectionPlane3D_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp s;

		getP().setPositive(true);
		
		s = getP().getIntersection(new Plane3d(1., 1., 0., -1.25));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), s);

		s = getP().getIntersection(new Plane3d(1., 1., 0., 1.25));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), s);

		s = getP().getIntersection(new Plane3d(0., 1., 1., -1.25));
		assertReceiverInvoked(createPoint(0, 0.517766953, 1.25), createVector(1., 0., 0.), s);

		s = getP().getIntersection(new Plane3d(0., 1., 1., 1.25));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(1., 0., 0.), s);

		s = getP().getIntersection(new Plane3d(1., 0., 1., -1.25));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., -1., 0.), s);

		s = getP().getIntersection(new Plane3d(1., 0., 1., 1.25));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., -1., 0.), s);

		s = getP().getIntersection(new Plane3d(1., 1., 1., -1.25));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), s);

		s = getP().getIntersection(new Plane3d(1., 1., 1., 1.25));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), s);

		//
		getP().setPositive(false);
		
		s = getP().getIntersection(new Plane3d(1., 1., 0., -1.25));
		assertReceiverInvoked(createPoint(0.8838834765, 0.8838834765, 1.25), createVector(-1., 1., 0.), s);

		s = getP().getIntersection(new Plane3d(1., 1., 0., 1.25));
		assertReceiverInvoked(createPoint(-0.8838834765, -0.8838834765, 1.25), createVector(-1., 1., 0.), s);

		s = getP().getIntersection(new Plane3d(0., 1., 1., -1.25));
		assertReceiverInvoked(createPoint(0., 0.517766953, 1.25), createVector(-1., 0., 0.), s);

		s = getP().getIntersection(new Plane3d(0., 1., 1., 1.25));
		assertReceiverInvoked(createPoint(0., -3.017766953, 1.25), createVector(-1., 0., 0.), s);

		s = getP().getIntersection(new Plane3d(1., 0., 1., -1.25));
		assertReceiverInvoked(createPoint(0.517766953, 0, 1.25), createVector(0., 1., 0.), s);

		s = getP().getIntersection(new Plane3d(1., 0., 1., 1.25));
		assertReceiverInvoked(createPoint(-3.017766953, 0, 1.25), createVector(0., 1., 0.), s);

		s = getP().getIntersection(new Plane3d(1., 1., 1., -1.25));
		assertReceiverInvoked(createPoint(0.4575317547, 0.4575317547, 1.25), createVector(-1., 1., 0.), s);

		s = getP().getIntersection(new Plane3d(1., 1., 1., 1.25));
		assertReceiverInvoked(createPoint(-1.7075317547, -1.7075317547, 1.25), createVector(-1., 1., 0.), s);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void isPositive(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().isPositive());
		getP().setPositive(true);
		assertTrue(getP().isPositive());
	}

	@DisplayName("set(Plane3D) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setPlane3D_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(new PlaneXZ3d(true, -4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(4., getP().getEquationComponentD());
		assertEpsilonEquals(-4., getP().getZ());
		//
		getP().set(new PlaneXZ3d(true, 4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-4., getP().getEquationComponentD());
		assertEpsilonEquals(4., getP().getZ());
		//
		getP().set(new PlaneXZ3d(false, 4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(4., getP().getEquationComponentD());
		assertEpsilonEquals(-4., getP().getZ());
		//
		getP().set(new PlaneXZ3d(false, -4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-4., getP().getEquationComponentD());
		assertEpsilonEquals(4., getP().getZ());
	}

	@DisplayName("set(Plane3D) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setPlane3D_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(new PlaneXY3d(true, -4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(4, getP().getEquationComponentD());
		assertEpsilonEquals(-4, getP().getZ());
		//
		getP().set(new PlaneXY3d(true, 4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-4, getP().getEquationComponentD());
		assertEpsilonEquals(4, getP().getZ());
		//
		getP().set(new PlaneXY3d(false, 4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(4, getP().getEquationComponentD());
		assertEpsilonEquals(4, getP().getZ());
		//
		getP().set(new PlaneXY3d(false, -4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(-4, getP().getEquationComponentD());
		assertEpsilonEquals(-4, getP().getZ());
	}

	@DisplayName("set(Plane3D) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setPlane3D_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(new PlaneYZ3d(true, -4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(4., getP().getEquationComponentD());
		assertEpsilonEquals(-4., getP().getZ());
		//
		getP().set(new PlaneYZ3d(true, 4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-4., getP().getEquationComponentD());
		assertEpsilonEquals(4., getP().getZ());
		//
		getP().set(new PlaneYZ3d(false, 4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(4., getP().getEquationComponentD());
		assertEpsilonEquals(-4., getP().getZ());
		//
		getP().set(new PlaneYZ3d(false, -4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-4., getP().getEquationComponentD());
		assertEpsilonEquals(4., getP().getZ());
	}

	@DisplayName("set(Plane3D) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setPlane3D_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(new Plane3d(2, -1, 3, -4));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-4, getP().getEquationComponentD());
		assertEpsilonEquals(4, getP().getZ());
		//
		getP().set(new Plane3d(0, 0, 1, -18));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-18, getP().getEquationComponentD());
		assertEpsilonEquals(18, getP().getZ());
	}

	@DisplayName("set(double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(1, 2, 3, 4);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(4, getP().getEquationComponentD());
		assertEpsilonEquals(-4, getP().getZ());
		//
		getP().set(1, 2, 3, -4);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-4, getP().getEquationComponentD());
		assertEpsilonEquals(4, getP().getZ());
		//
		getP().set(-1, -2, -3, 4);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(4, getP().getEquationComponentD());
		assertEpsilonEquals(4, getP().getZ());
		//
		getP().set(-1, -2, -3, -4);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(-4, getP().getEquationComponentD());
		assertEpsilonEquals(-4, getP().getZ());
	}

	@DisplayName("set(double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void setDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(1, 2, 3, 4, 18, -42, 57, 1, -6);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(-15.0, getP().getEquationComponentD());
		assertEpsilonEquals(-15.0, getP().getZ());

		getP().set(1, 2, 3, 1, 2, 3, 1, 2, 3);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-3., getP().getEquationComponentD());
		assertEpsilonEquals(3, getP().getZ());
	}

	@DisplayName("set(Point3D,Point3D,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void setPoint3DPoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(createPoint(1, 2, 3), createPoint(4, 18, -42), createPoint(57, 1, -6));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(-15.0, getP().getEquationComponentD());
		assertEpsilonEquals(-15.0, getP().getZ());

		getP().set(createPoint(1, 2, 3), createPoint(1, 2, 3), createPoint(1, 2, 3));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-3., getP().getEquationComponentD());
		assertEpsilonEquals(3, getP().getZ());
	}

	@DisplayName("set(Point3D,Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void setPoint3DVector3D(CoordinateSystem3D cs) {
		getP().set(createPoint(1, 2, 3), createVector(0, 1, -42));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(3, getP().getEquationComponentD());
		assertEpsilonEquals(3, getP().getZ());

		getP().set(createPoint(1, 2, 3), createVector(0, 1, 42));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-3, getP().getEquationComponentD());
		assertEpsilonEquals(3, getP().getZ());
	}

	@DisplayName("set(Point3D,Vector3D,Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void setPoint3DVector3DVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().set(createPoint(1, 2, 3), createVector(3, 16, -45), createVector(56, -1, -9));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(3.0, getP().getEquationComponentD());
		assertEpsilonEquals(3.0, getP().getZ());

		getP().set(createPoint(1, 2, 3), createVector(56, -1, -9), createVector(3, 16, -45));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-3.0, getP().getEquationComponentD());
		assertEpsilonEquals(3.0, getP().getZ());

		getP().set(createPoint(1, 2, 3), createVector(0, 0, 0), createVector(1, 2, 3));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-3., getP().getEquationComponentD());
		assertEpsilonEquals(3, getP().getZ());

		getP().set(createPoint(1, 2, 3), createVector(1, 2, 3), createVector(0, 0, 0));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-3., getP().getEquationComponentD());
		assertEpsilonEquals(3, getP().getZ());

		getP().set(createPoint(1, 2, 3), createVector(0, 0, 0), createVector(0, 0, 0));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-3., getP().getEquationComponentD());
		assertEpsilonEquals(3, getP().getZ());
	}

	@DisplayName("-this")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_minus(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var r = getP().operator_minus();
		assertNotSame(getP(), r);
		assertEpsilonEquals(0., r.getEquationComponentA());
		assertEpsilonEquals(0., r.getEquationComponentB());
		assertEpsilonEquals(1., r.getEquationComponentC());
		assertEpsilonEquals(-1.25, r.getEquationComponentD());
	}

	@DisplayName("this += double")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_addDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().operator_add(5.69);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(6.94, getP().getEquationComponentD());
	}

	@DisplayName("this -= double")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_removeDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().operator_remove(5.69);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(-4.44, getP().getEquationComponentD());
	}

	@DisplayName("this + double")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_plusDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var r = getP().operator_plus(5.69);
		assertNotSame(getP(), r);
		assertEpsilonEquals(0., r.getEquationComponentA());
		assertEpsilonEquals(0., r.getEquationComponentB());
		assertEpsilonEquals(-1., r.getEquationComponentC());
		assertEpsilonEquals(6.94, r.getEquationComponentD());
	}

	@DisplayName("this - double")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void operator_minusDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var r = getP().operator_minus(5.69);
		assertNotSame(getP(), r);
		assertEpsilonEquals(0., r.getEquationComponentA());
		assertEpsilonEquals(0., r.getEquationComponentB());
		assertEpsilonEquals(-1., r.getEquationComponentC());
		assertEpsilonEquals(-4.44, r.getEquationComponentD());
	}

	@DisplayName("translate(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void translateDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().translate(5.69);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(6.94, getP().getEquationComponentD());
	}

	@DisplayName("this += Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().operator_add(createVector(148, 5.69, 569));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(570.25, getP().getEquationComponentD());
	}

	@DisplayName("this -= Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().operator_remove(createVector(148, 5.69, 569));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(-567.75, getP().getEquationComponentD());
	}

	@DisplayName("this + Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var r = getP().operator_plus(createVector(148, 5.69, 569));
		assertNotSame(getP(), r);
		assertEpsilonEquals(0., r.getEquationComponentA());
		assertEpsilonEquals(0., r.getEquationComponentB());
		assertEpsilonEquals(-1., r.getEquationComponentC());
		assertEpsilonEquals(570.25, r.getEquationComponentD());
	}

	@DisplayName("this -= Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var r = getP().operator_minus(createVector(148, 5.69, 569));
		assertNotSame(getP(), r);
		assertEpsilonEquals(0., r.getEquationComponentA());
		assertEpsilonEquals(0., r.getEquationComponentB());
		assertEpsilonEquals(-1., r.getEquationComponentC());
		assertEpsilonEquals(-567.75, r.getEquationComponentD());
	}

	@DisplayName("translate(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void translateDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().translate(148, 5.69, 569);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(570.25, getP().getEquationComponentD());
	}

	@DisplayName("translate(Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().translate(createVector(148, 5.69, 569));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(570.25, getP().getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void normalize(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(this.plane, getP().normalize());
	}

	@DisplayName("setPivot(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setPivotDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().setPivot(0, 0, 0);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(0, getP().getEquationComponentD());
		//
		getP().setPivot(1, 2, 3);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(3, getP().getEquationComponentD());
		//
		getP().setPivot(4, 5, 63);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(63, getP().getEquationComponentD());
	}

	@DisplayName("setPivot(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setPivotPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		getP().setPivot(createPoint(0, 0, 0));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(0, getP().getEquationComponentD());
		//
		getP().setPivot(createPoint(1, 2, 3));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(3, getP().getEquationComponentD());
		//
		getP().setPivot(createPoint(4, 5, 63));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(63, getP().getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("this * Transform3D")
	@Override
	public final void operator_multiplyTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// Translation Z, no rotation
		var tr = new Transform3D();
		tr.setTranslation(0, 0, 5.69);
		var r = getP().operator_multiply(tr);
		assertNotSame(getP(), r);
		assertEpsilonEquals(0., r.getEquationComponentA());
		assertEpsilonEquals(0., r.getEquationComponentB());
		assertEpsilonEquals(-1., r.getEquationComponentC());
		assertEpsilonEquals(6.94, r.getEquationComponentD());
		assertEpsilonEquals(6.94, r.getZ());

		// Translation XYZ, no rotation
		tr = new Transform3D();
		tr.setTranslation(5, 6, 3.69);
		r = getP().operator_multiply(tr);
		assertNotSame(getP(), r);
		assertEpsilonEquals(0., r.getEquationComponentA());
		assertEpsilonEquals(0., r.getEquationComponentB());
		assertEpsilonEquals(-1., r.getEquationComponentC());
		assertEpsilonEquals(4.94, r.getEquationComponentD());
		assertEpsilonEquals(4.94, r.getZ());

		// Translation Z, Rotation with quadrant flip
		tr = new Transform3D();
		tr.makeRotationMatrix(1.,  2.,  -1., Math.PI / 7.);
		tr.setTranslation(0, 0, 5.69);
		r = getP().operator_multiply(tr);
		assertNotSame(getP(), r);
		assertEpsilonEquals(0., r.getEquationComponentA());
		assertEpsilonEquals(0., r.getEquationComponentB());
		assertEpsilonEquals(1., r.getEquationComponentC());
		assertEpsilonEquals(-6.94, r.getEquationComponentD());
		assertEpsilonEquals(6.94, r.getZ());

		// Translation XYZ, Rotation without quadrant flip
		tr = new Transform3D();
		tr.makeRotationMatrix(0.,  0.,  -1., Math.PI / 7.);
		tr.setTranslation(5, 6, 3.69);
		r = getP().operator_multiply(tr);
		assertNotSame(getP(), r);
		assertEpsilonEquals(0., r.getEquationComponentA());
		assertEpsilonEquals(0., r.getEquationComponentB());
		assertEpsilonEquals(-1., r.getEquationComponentC());
		assertEpsilonEquals(4.94, r.getEquationComponentD());
		assertEpsilonEquals(4.94, r.getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("transform(Transform3D)")
	public final void transformTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// Translation Z, no rotation
		var tr = new Transform3D();
		tr.setTranslation(0, 0, 5.69);
		getP().transform(tr);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(6.94, getP().getEquationComponentD());
		assertEpsilonEquals(6.94, getP().getZ());

		// Translation XYZ, no rotation
		tr = new Transform3D();
		tr.setTranslation(5, 6, 3.69);
		getP().transform(tr);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(10.63, getP().getEquationComponentD());
		assertEpsilonEquals(10.63, getP().getZ());

		// Translation Z, Rotation with quadrant flip
		tr = new Transform3D();
		tr.makeRotationMatrix(1.,  2.,  -1., Math.PI / 7.);
		tr.setTranslation(0, 0, 5.69);
		getP().transform(tr);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-16.32, getP().getEquationComponentD());
		assertEpsilonEquals(16.32, getP().getZ());

		// Translation XYZ, Rotation without quadrant flip
		tr = new Transform3D();
		tr.makeRotationMatrix(0.,  0.,  -1., Math.PI / 7.);
		tr.setTranslation(5, 6, 3.69);
		getP().transform(tr);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-20.01, getP().getEquationComponentD());
		assertEpsilonEquals(20.01, getP().getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("transform(Transform3D,Point3D)")
	public final void transformTransform3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// Translation Z, no rotation
		var tr = new Transform3D();
		tr.setTranslation(0, 0, 5.69);
		getP().transform(tr, createPoint(-45, 6, -42));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(6.94, getP().getEquationComponentD());
		assertEpsilonEquals(6.94, getP().getZ());

		// Translation XYZ, no rotation
		tr = new Transform3D();
		tr.setTranslation(5, 6, 3.69);
		getP().transform(tr, createPoint(-2, -5, 18));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(-1., getP().getEquationComponentC());
		assertEpsilonEquals(10.63, getP().getEquationComponentD());
		assertEpsilonEquals(10.63, getP().getZ());

		// Translation Z, Rotation with quadrant flip
		tr = new Transform3D();
		tr.makeRotationMatrix(1.,  2.,  -1., Math.PI / 7.);
		tr.setTranslation(0, 0, 5.69);
		getP().transform(tr, createPoint(5, 6.5, 0));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-16.32, getP().getEquationComponentD());
		assertEpsilonEquals(16.32, getP().getZ());

		// Translation XYZ, Rotation without quadrant flip
		tr = new Transform3D();
		tr.makeRotationMatrix(0.,  0.,  -1., Math.PI / 7.);
		tr.setTranslation(5, 6, 3.69);
		getP().transform(tr, createPoint(9, -1, 0.5));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-20.01, getP().getEquationComponentD());
		assertEpsilonEquals(20.01, getP().getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getProjection(Point3D)")
	public final void getProjectionPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(createPoint(0, 0, 1.25), getP().getProjection(createPoint(0, 0, 0)));
		assertEpsilonEquals(createPoint(125, -458, 1.25), getP().getProjection(createPoint(125, -458, -145)));
		assertEpsilonEquals(createPoint(-145, 458, 1.25), getP().getProjection(createPoint(-145, 458, 18)));

		var rnd = new Random();
		for (int i = 0; i < 100; ++i) {
			var x = rnd.nextDouble(1000) * (rnd.nextBoolean() ? 1. : -1.);
			var y = rnd.nextDouble(1000) * (rnd.nextBoolean() ? 1. : -1.);
			var z = rnd.nextDouble(1000) * (rnd.nextBoolean() ? 1. : -1.);
			assertEpsilonEquals(createPoint(x, y, 1.25), getP().getProjection(createPoint(x, y, z)));
		}
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getProjection(double,double,double)")
	public final void getProjectionDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(createPoint(0, 0, 1.25), getP().getProjection(0, 0, 0));
		assertEpsilonEquals(createPoint(125, -458, 1.25), getP().getProjection(125, -458, -145));
		assertEpsilonEquals(createPoint(-145, 458, 1.25), getP().getProjection(-145, 458, 18));

		var rnd = new Random();
		for (int i = 0; i < 100; ++i) {
			var x = rnd.nextDouble(1000) * (rnd.nextBoolean() ? 1. : -1.);
			var y = rnd.nextDouble(1000) * (rnd.nextBoolean() ? 1. : -1.);
			var z = rnd.nextDouble(1000) * (rnd.nextBoolean() ? 1. : -1.);
			assertEpsilonEquals(createPoint(x, y, 1.25), getP().getProjection(x, y, z));
		}
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("rotate(double,double,double,double)")
	@Override
	public final void rotateDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// Rotation with quadrant flip
		getP().rotate(1.,  1., 0., 1.2 * Math.PI);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		assertEpsilonEquals(1.25, getP().getZ());

		// Rotation without quadrant flip
		getP().rotate(0.,  1., 0., Math.PI / 7.);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		assertEpsilonEquals(1.25, getP().getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("this * Quaternion")
	@Override
	public final void operator_multiplyQuaternion(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// Rotation with quadrant flip
		var r = getP().operator_multiply(createAxisAngle(1.,  1., 0., 1.2 * Math.PI));
		assertNotSame(getP(), r);
		assertEpsilonEquals(0., r.getEquationComponentA());
		assertEpsilonEquals(0., r.getEquationComponentB());
		assertEpsilonEquals(1., r.getEquationComponentC());
		assertEpsilonEquals(-1.25, r.getEquationComponentD());
		assertEpsilonEquals(1.25, r.getZ());

		// Rotation without quadrant flip
		r = getP().operator_multiply(createAxisAngle(0.,  1., 0., Math.PI / 7.));
		assertNotSame(getP(), r);
		assertEpsilonEquals(0., r.getEquationComponentA());
		assertEpsilonEquals(0., r.getEquationComponentB());
		assertEpsilonEquals(-1., r.getEquationComponentC());
		assertEpsilonEquals(1.25, r.getEquationComponentD());
		assertEpsilonEquals(1.25, r.getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("rotate(Quaternion)")
	@Override
	public final void rotateQuaternion(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// Rotation with quadrant flip
		getP().rotate(createAxisAngle(1.,  1., 0., 1.2 * Math.PI));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		assertEpsilonEquals(1.25, getP().getZ());

		// Rotation without quadrant flip
		getP().rotate(createAxisAngle(0.,  1., 0., Math.PI / 7.));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		assertEpsilonEquals(1.25, getP().getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("rotate(Quaternion,Point3D)")
	@Override
	public final void rotateQuaternionPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// Rotation with quadrant flip
		getP().rotate(createAxisAngle(1.,  1., 0., 1.2 * Math.PI), createPoint(1, 2, 3));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		assertEpsilonEquals(1.25, getP().getZ());

		// Rotation without quadrant flip
		getP().rotate(createAxisAngle(0.,  1., 0., Math.PI / 7.), createPoint(1, 2, 3));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		assertEpsilonEquals(1.25, getP().getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("rotate(Vector3D,double)")
	@Override
	public final void rotateVector3DDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// Rotation with quadrant flip
		getP().rotate(createVector(1.,  1., 0.), 1.2 * Math.PI);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		assertEpsilonEquals(1.25, getP().getZ());

		// Rotation without quadrant flip
		getP().rotate(createVector(0.,  1., 0.), Math.PI / 7.);
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		assertEpsilonEquals(1.25, getP().getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("rotate(Vector3D,double,Point3D)")
	@Override
	public final void rotateVector3DDoublePoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		// Rotation with quadrant flip
		getP().rotate(createVector(1.,  1., 0.), 1.2 * Math.PI, createPoint(1, 2, 3));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		assertEpsilonEquals(1.25, getP().getZ());

		// Rotation without quadrant flip
		getP().rotate(createVector(0.,  1., 0.), Math.PI / 7., createPoint(1, 2, 3));
		assertEpsilonEquals(0., getP().getEquationComponentA());
		assertEpsilonEquals(0., getP().getEquationComponentB());
		assertEpsilonEquals(1., getP().getEquationComponentC());
		assertEpsilonEquals(-1.25, getP().getEquationComponentD());
		assertEpsilonEquals(1.25, getP().getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("this == Plane3D")
	@Override
	public final void operator_equalsPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertFalse(getP().operator_equals(null));
		assertTrue(getP().operator_equals(getP()));
		assertTrue(getP().operator_equals(createPlaneXY(1.25, false)));
		assertFalse(getP().operator_equals(createPlaneXY(1.25, true)));
		assertTrue(getP().operator_equals(createPlane(0, 0, -1, 1.25)));
		assertFalse(getP().operator_equals(createPlane(0, 0, -1, -1.25)));
		assertFalse(getP().operator_equals(createPlane(0, 0, 1, -1.25)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("this != Plane3D")
	@Override
	public final void operator_notEqualsPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertTrue(getP().operator_notEquals(null));
		assertFalse(getP().operator_notEquals(getP()));
		assertFalse(getP().operator_notEquals(createPlaneXY(1.25, false)));
		assertTrue(getP().operator_notEquals(createPlaneXY(1.25, true)));
		assertFalse(getP().operator_notEquals(createPlane(0, 0, -1, 1.25)));
		assertTrue(getP().operator_notEquals(createPlane(0, 0, -1, -1.25)));
		assertTrue(getP().operator_notEquals(createPlane(0, 0, 1, -1.25)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("findsClosestPointRectangleXYSegment(double,double,double,double,double, double,double,double,double,double,double, Point3D,Point3D)")
	public final void findsClosestPointRectangleXYSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		final var onSegment = new InnerComputationPoint3D();
		final var onPlane = new InnerComputationPoint3D();
		
		// CASE 1: Segment entirely below and outside the rectangle.
		// Rectangle [1,2]x[2,3] at z=3. Segment (0,0,0)->(1,0.5,-5).
		// Both points are below z=3 and outside XY bounds.
		// Closest segment point = S1=(0,0,0), closest rect point = corner (1,2,3).
		// sqDist = (0-1)^2+(0-2)^2+(0-3)^2 = 1+4+9 = 14.
		assertEpsilonEquals(14., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				1., 2., 2., 3., 3.,
				0., 0., 0., 1., 0.5, -5.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(0., 0., 0.), onSegment);
		assertEpsilonEquals(createPoint(1., 2., 3.), onPlane);

		// CASE 2: Segment pierces the rectangle interior - distance must be 0.
		// Rectangle [-1,1]x[-1,1] at z=0. Segment (0,0,1)->(0,0,-1).
		// Parametric intersection at t=0.5 -> point (0,0,0) inside rectangle.
		assertEpsilonEquals(0., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				-1., -1., 1., 1., 0.,
				0., 0., 1., 0., 0., -1.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(0., 0., 0.), onSegment);
		assertEpsilonEquals(createPoint(0., 0., 0.), onPlane);

		// CASE 3: Segment pierces the rectangle at a corner - distance must be 0.
		// Rectangle [0,2]x[0,2] at z=0. Segment (0,0,1)->(0,0,-1).
		// Intersection at t=0.5 -> point (0,0,0) which is exactly corner (0,0,0).
		assertEpsilonEquals(0., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				0., 0., 2., 2., 0.,
				0., 0., 1., 0., 0., -1.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(0., 0., 0.), onSegment);
		assertEpsilonEquals(createPoint(0., 0., 0.), onPlane);

		// CASE 4: Segment pierces the plane but OUTSIDE the rectangle bounds.
		// Rectangle [1,2]x[1,2] at z=0. Segment (5,5,1)->(5,5,-1).
		// Intersection at (5,5,0) - outside XY. Closest rect point = corner (2,2,0).
		// Closest segment point is where the segment is closest to (2,2,0).
		// The segment is vertical at x=5,y=5. Closest point on segment to (2,2,0) is (5,5,0).
		// sqDist = (5-2)^2+(5-2)^2+(0-0)^2 = 9+9 = 18.
		assertEpsilonEquals(18., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				1., 1., 2., 2., 0.,
				5., 5., 1., 5., 5., -1.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(5., 5., 0.), onSegment);
		assertEpsilonEquals(createPoint(2., 2., 0.), onPlane);

		// CASE 5: Segment coplanar with the rectangle plane, inside rectangle bounds - distance 0.
		// Rectangle [-1,1]x[-1,1] at z=0. Segment (-0.5,0,0)->(0.5,0,0) lies in z=0.
		// Both points are inside the rectangle -> distance = 0.
		assertEpsilonEquals(0., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				-1., -1., 1., 1., 0.,
				-0.5, 0., 0., 0.5, 0., 0.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(-.5, 0., 0.), onSegment);
		assertEpsilonEquals(createPoint(-.5, 0., 0.), onPlane);

		// CASE 6: Segment coplanar with the rectangle plane, entirely outside rectangle bounds.
		// Rectangle [1,2]x[1,2] at z=0. Segment (4,0,0)->(5,0,0).
		// Closest rect point = (2,1,0) (corner), closest segment point = (4,0,0) (S1).
		// sqDist = (4-2)^2+(0-1)^2+(0-0)^2 = 4+1 = 5.
		assertEpsilonEquals(5., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				1., 1., 2., 2., 0.,
				4., 0., 0., 5., 0., 0.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(4., 0., 0.), onSegment);
		assertEpsilonEquals(createPoint(2., 1., 0.), onPlane);

		// CASE 7: Segment parallel to the XY plane (no z change), hovering above.
		// Rectangle [-1,1]x[-1,1] at z=0. Segment (-0.5,0,3)->(0.5,0,3).
		// Infinite number of solution points on segment -> select the first found (-0.5,0,3).
		// Closest point on the rectangle surface (-0.5, 0, 0).
		// sqDist = 0^2+0^2+3^2 = 9.
		assertEpsilonEquals(9., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				-1., -1., 1., 1., 0.,
				-0.5, 0., 3., 0.5, 0., 3.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(-.5, 0., 3.), onSegment);
		assertEpsilonEquals(createPoint(-.5, 0., 0.), onPlane);

		// CASE 8: Segment parallel to XY plane, outside XY bounds, hovering above.
		// Rectangle [0,1]x[0,1] at z=0. Segment (-3,0.5,2)->(-1,0.5,2).
		// Closest rect point = (0,0.5,0) (edge x=0 point clamped to rect).
		// Closest segment point = (-1,0.5,2) = S2 (closest end to rect).
		// sqDist = (-1-0)^2+(0.5-0.5)^2+(2-0)^2 = 1+0+4 = 5.
		assertEpsilonEquals(5., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				0., 0., 1., 1., 0.,
				-3., 0.5, 2., -1., 0.5, 2.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(-1., 0.5, 2.), onSegment);
		assertEpsilonEquals(createPoint(0., 0.5, 0.), onPlane);

		// CASE 9: Degenerate segment (both endpoints coincide) - point above interior.
		// Rectangle [-1,1]x[-1,1] at z=0. Segment (0,0,4)->(0,0,4) (point).
		// Closest rect point = (0,0,0), sqDist = 0^2+0^2+4^2 = 16.
		assertEpsilonEquals(16., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				-1., -1., 1., 1., 0.,
				0., 0., 4., 0., 0., 4.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(0., 0., 4.), onSegment);
		assertEpsilonEquals(createPoint(0., 0., 0.), onPlane);

		// CASE 10: Degenerate segment - point above a corner of the rectangle.
		// Rectangle [0,2]x[0,2] at z=0. Segment (0,0,3)->(0,0,3).
		// Closest rect point = (0,0,0) (corner), sqDist = 0^2+0^2+3^2 = 9.
		assertEpsilonEquals(9., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				0., 0., 2., 2., 0.,
				0., 0., 3., 0., 0., 3.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(0., 0., 3.), onSegment);
		assertEpsilonEquals(createPoint(0., 0., 0.), onPlane);

		// CASE 11: Segment endpoint on the rectangle plane, XY inside rect - dist 0.
		// Rectangle [-1,1]x[-1,1] at z=0. Segment (0,0,0)->(0,0,5).
		// S1=(0,0,0) is exactly on the plane and inside the rect -> distance = 0.
		assertEpsilonEquals(0., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				-1., -1., 1., 1., 0.,
				0., 0., 0., 0., 0., 5.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(0., 0., 0.), onSegment);
		assertEpsilonEquals(createPoint(0., 0., 0.), onPlane);

		// CASE 12: Segment endpoint exactly on a rect corner - dist 0.
		// Rectangle [0,2]x[0,2] at z=0. Segment (2,2,0)->(5,5,5).
		// S1=(2,2,0) is exactly at corner (2,2,0) -> distance = 0.
		assertEpsilonEquals(0., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				0., 0., 2., 2., 0.,
				2., 2., 0., 5., 5., 5.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(2., 2., 0.), onSegment);
		assertEpsilonEquals(createPoint(2., 2., 0.), onPlane);

		// CASE 13: Segment entirely above the rectangle (no z change, z > rz).
		// Rectangle [0,4]x[0,4] at z=5. Segment (1,1,10)->(3,3,10).
		// Closest rect point = interior projection of midpoint (2,2,10) -> (2,2,5).
		// sqDist = (2-2)^2+(2-2)^2+(10-5)^2 = 25.
		assertEpsilonEquals(25., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				0., 0., 4., 4., 5.,
				1., 1., 10., 3., 3., 10.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(1., 1., 10.), onSegment);
		assertEpsilonEquals(createPoint(1., 1., 5.), onPlane);

		// CASE 14: Segment above rect, closest point is near an edge (not corner).
		// Rectangle [0,4]x[0,4] at z=0. Segment (2,-3,2)->(2,3,2) spans across
		// the rect in Y. Closest segment point to plane interior is (2,0,2).
		// Closest rect point = (2,0,0) on bottom edge.
		// sqDist = (2-2)^2+(0-0)^2+(2-0)^2 = 4.
		assertEpsilonEquals(4., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				0., 0., 4., 4., 0.,
				2., -3., 2., 2., 3., 2.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(2., 0., 2.), onSegment);
		assertEpsilonEquals(createPoint(2., 0., 0.), onPlane);

		// CASE 15: Segment approaches the rectangle from one side in 3D (skewed).
		// Rectangle [0,4]x[0,4] at z=0. Segment (2,2,3)->(2,2,-3).
		// Segment crosses the plane at (2,2,0) which is inside the rect -> dist=0.
		assertEpsilonEquals(0., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				0., 0., 4., 4., 0.,
				2., 2., 3., 2., 2., -3.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(2., 2., 0.), onSegment);
		assertEpsilonEquals(createPoint(2., 2., 0.), onPlane);

		// CASE 16: Segment entirely below the rectangle plane (z1<rz, z2<rz), outside XY.
		// Rectangle [1,3]x[1,3] at z=5. Segment (0,0,-1)->(0,0,-3).
		// Both endpoints below plane, both outside XY. Closest seg point = S1=(0,0,-1).
		// Closest rect point = corner (1,1,5).
		// sqDist = (0-1)^2+(0-1)^2+(-1-5)^2 = 1+1+36 = 38.
		assertEpsilonEquals(38., PlaneXY3afp.findsClosestPointRectangleXYSegment(
				1., 1., 3., 3., 5.,
				0., 0., -1., 0., 0., -3.,
				onPlane, onSegment));
		assertEpsilonEquals(createPoint(0., 0., -1.), onSegment);
		assertEpsilonEquals(createPoint(1., 1., 5.), onPlane);
	}


	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("equals(Plane3D)")
	@Override
	public final void equalsPlane3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(getP().equals(null));
		assertTrue(getP().equals(getP()));
		assertFalse(getP().equals(createPlane(1, 0, 0, 0)));
		assertFalse(getP().equals(createPlane(-1, 0, 0, 0)));
		assertFalse(getP().equals(createPlane(0, 1, 0, 0)));
		assertFalse(getP().equals(createPlane(0, -1, 0, 0)));
		assertFalse(getP().equals(createPlane(0, 0, 1, 0)));
		assertFalse(getP().equals(createPlane(0, 0, -1, 0)));
		assertFalse(getP().equals(createPlane(1, 0, 0, 1.25)));
		assertFalse(getP().equals(createPlane(-1, 0, 0, 1.25)));
		assertFalse(getP().equals(createPlane(0, 1, 0, 1.25)));
		assertFalse(getP().equals(createPlane(0, -1, 0, 1.25)));
		assertFalse(getP().equals(createPlane(0, 0, 1, 1.25)));
		assertTrue(getP().equals(createPlane(0, 0, -1, 1.25)));
		assertFalse(getP().equals(createPlane(1, 0, 0, -1.25)));
		assertFalse(getP().equals(createPlane(-1, 0, 0, -1.25)));
		assertFalse(getP().equals(createPlane(0, 1, 0, -1.25)));
		assertFalse(getP().equals(createPlane(0, -1, 0, -1.25)));
		assertFalse(getP().equals(createPlane(0, 0, 1, -1.25)));
		assertFalse(getP().equals(createPlane(0, 0, -1, -1.25)));
	}

}
