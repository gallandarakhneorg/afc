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

package org.arakhne.afc.math.test.geometry.d3.d;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.PlaneClassification;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.Box3afp;
import org.arakhne.afc.math.geometry.d3.afp.Plane3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneXY3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.d3.d.AlignedBox3d;
import org.arakhne.afc.math.geometry.d3.d.Plane3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneXY3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneXZ3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneYZ3d;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.math.geometry.d3.d.Segment3d;
import org.arakhne.afc.math.geometry.d3.d.Sphere3d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.test.AbstractMathTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PlaneXY3dTest extends AbstractMathTestCase {

	private PlaneXY3d plane;

	protected Point3D<?, ?, ?> createPoint(double x, double y, double z) {
		return new Point3d(x, y, z);
	}
	
	protected Point3D<?, ?, ?> createPoint() {
		return new Point3d();
	}

	protected Vector3D<?, ?, ?> createVector(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}
	
	protected Vector3D<?, ?, ?> createVector() {
		return new Vector3d();
	}

	protected Segment3afp<?, ?, ?, ?, ?, ?, ?> createSegment() {
		return new Segment3d();
	}

	protected Segment3afp<?, ?, ?, ?, ?, ?, ?> createSegment(double x1, double y1, double z1, double x2, double y2, double z2) {
		return new Segment3d(x1, y1, z1, x2, y2, z2);
	}

	protected Sphere3afp<?, ?, ?, ?, ?, ?, ?> createSphere(double x, double y, double z, double radius) {
		return new Sphere3d(x, y, z, radius);
	}

	protected Box3afp<?, ?, ?, ?, ?, ?, ?> createBox(double minx, double miny, double minz, double maxx, double maxy, double maxz) {
		return new AlignedBox3d(minx, miny, minz, maxx - minx, maxy - miny, maxz - minz);
	}

	protected Plane3afp<?, ?, ?, ?, ?> createPlane(double a, double b, double c, double d) {
		return new Plane3d(a, b, c ,d);
	}

	@BeforeEach
	public void setUp() {
		this.plane = new PlaneXY3d(false, 1.25);
	}

	@DisplayName("toGeogebra with negative normal")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void toGeogebra_negative(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals("0.0*x+0.0*y-1.0*z+1.25=0.0", this.plane.toGeogebra());
	}

	@DisplayName("toGeogebra with positive normal")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void toGeogebra_positive(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PlaneXY3d np = new PlaneXY3d(true, 1.25);
		assertEquals("0.0*x+0.0*y+1.0*z-1.25=0.0", np.toGeogebra());
	}

	@DisplayName("calculatesPlaneXYSegmentIntersectionFactor(boolean,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlaneXYSegmentIntersectionFactor(CoordinateSystem3D cs) {
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

	@DisplayName("calculatesPlaneXYSegmentIntersection(boolean,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlaneXYSegmentIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D<?, ?, ?> p = createPoint();

		assertFalse(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, -51.2,
				47.1, -7.9, .5,
				p));

		assertFalse(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 47.2,
				47.1, -7.9, 2.,
				p));
	
		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 0.,
				47.1, -7.9, 2.,
				p));
		assertEpsilonEquals(createPoint(41.625, -7.0375, 1.25), p);

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 2.,
				47.1, -7.9, 0.,
				p));
		assertEpsilonEquals(createPoint(37.975, -6.4625, 1.25), p);

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 0.,
				p));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 0.,
				47.1, -7.9, 1.25,
				p));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 2.,
				p));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 2.,
				47.1, -7.9, 1.25,
				p));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 1.25,
				p));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		//

		assertFalse(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, -51.2,
				47.1, -7.9, .5,
				p));

		assertFalse(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 47.2,
				47.1, -7.9, 2.,
				p));

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 0.,
				47.1, -7.9, 2.,
				p));
		assertEpsilonEquals(createPoint(41.625, -7.0375, 1.25), p);

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 2.,
				47.1, -7.9, 0.,
				p));
		assertEpsilonEquals(createPoint(37.975, -6.4625, 1.25), p);

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 0.,
				p));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 0.,
				47.1, -7.9, 1.25,
				p));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 2.,
				p));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				true, 1.25,
				32.5, -5.6, 2.,
				47.1, -7.9, 1.25,
				p));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		assertTrue(PlaneXY3afp.calculatesPlaneXYSegmentIntersection(
				false, 1.25,
				32.5, -5.6, 1.25,
				47.1, -7.9, 1.25,
				p));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getNormal(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(createVector(0, 0, -1), this.plane.getNormal());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getNormalX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.plane.getNormalX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getNormalY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.plane.getNormalY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getNormalZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-1, this.plane.getNormalZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getEquationComponentA(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.plane.getEquationComponentA());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getEquationComponentB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.plane.getEquationComponentB());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getEquationComponentC(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-1, this.plane.getEquationComponentC());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getEquationComponentD(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.25, this.plane.getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.clear();
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(0., this.plane.getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void negate(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.negate();
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-1.25, this.plane.getEquationComponentD());
		//
		this.plane.negate();
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(1.25, this.plane.getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void absolute(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.absolute();
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-1.25, this.plane.getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void normalize(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(this.plane, this.plane.normalize());
	}

	@DisplayName("getProjection(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getProjectionPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(createPoint(0, 0, 1.25), this.plane.getProjection(createPoint(0, 0, 0)));
		assertEpsilonEquals(createPoint(125, -458, 1.25), this.plane.getProjection(createPoint(125, -458, -145)));
		assertEpsilonEquals(createPoint(-145, 458, 1.25), this.plane.getProjection(createPoint(-145, 458, 18)));
	}

	@DisplayName("getProjection(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getProjectionDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(createPoint(0, 0, 1.25), this.plane.getProjection(0, 0, 0));
		assertEpsilonEquals(createPoint(125, -458, 1.25), this.plane.getProjection(125, -458, -145));
		assertEpsilonEquals(createPoint(-145, 458, 1.25), this.plane.getProjection(-145, 458, 18));
	}

	@DisplayName("setPivot(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPivotDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.setPivot(0, 0, 0);
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(0, this.plane.getEquationComponentD());
		//
		this.plane.setPivot(1, 2, 3);
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(3, this.plane.getEquationComponentD());
		//
		this.plane.setPivot(4, 5, 63);
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(63, this.plane.getEquationComponentD());
	}

	@DisplayName("setPivot(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPivotPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.setPivot(createPoint(0, 0, 0));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(0, this.plane.getEquationComponentD());
		//
		this.plane.setPivot(createPoint(1, 2, 3));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(3, this.plane.getEquationComponentD());
		//
		this.plane.setPivot(createPoint(4, 5, 63));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(63, this.plane.getEquationComponentD());
	}

	@DisplayName("translate(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.translate(5.69);
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(6.94, this.plane.getEquationComponentD());
	}

	@DisplayName("translate(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.translate(148, 5.69, 569);
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(570.25, this.plane.getEquationComponentD());
	}

	@DisplayName("translate(Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.translate(createVector(148, 5.69, 569));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(570.25, this.plane.getEquationComponentD());
	}

	@DisplayName("set(Plane3D) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPlane3D_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.set(new PlaneXZ3d(true, -4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(4., this.plane.getEquationComponentD());
		assertEpsilonEquals(-4., this.plane.getZ());
		//
		this.plane.set(new PlaneXZ3d(true, 4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4., this.plane.getEquationComponentD());
		assertEpsilonEquals(4., this.plane.getZ());
		//
		this.plane.set(new PlaneXZ3d(false, 4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(4., this.plane.getEquationComponentD());
		assertEpsilonEquals(-4., this.plane.getZ());
		//
		this.plane.set(new PlaneXZ3d(false, -4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4., this.plane.getEquationComponentD());
		assertEpsilonEquals(4., this.plane.getZ());
	}

	@DisplayName("set(Plane3D) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPlane3D_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.set(new PlaneXY3d(true, -4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(4, this.plane.getEquationComponentD());
		assertEpsilonEquals(-4, this.plane.getZ());
		//
		this.plane.set(new PlaneXY3d(true, 4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4, this.plane.getEquationComponentD());
		assertEpsilonEquals(4, this.plane.getZ());
		//
		this.plane.set(new PlaneXY3d(false, 4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(4, this.plane.getEquationComponentD());
		assertEpsilonEquals(4, this.plane.getZ());
		//
		this.plane.set(new PlaneXY3d(false, -4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4, this.plane.getEquationComponentD());
		assertEpsilonEquals(-4, this.plane.getZ());
	}

	@DisplayName("set(Plane3D) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPlane3D_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.set(new PlaneYZ3d(true, -4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(4., this.plane.getEquationComponentD());
		assertEpsilonEquals(-4., this.plane.getZ());
		//
		this.plane.set(new PlaneYZ3d(true, 4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4., this.plane.getEquationComponentD());
		assertEpsilonEquals(4., this.plane.getZ());
		//
		this.plane.set(new PlaneXZ3d(false, 4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(4., this.plane.getEquationComponentD());
		assertEpsilonEquals(-4., this.plane.getZ());
		//
		this.plane.set(new PlaneYZ3d(false, -4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4., this.plane.getEquationComponentD());
		assertEpsilonEquals(4., this.plane.getZ());
	}

	@DisplayName("set(Plane3D) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPlane3D_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.set(new Plane3d(2, -1, 3, -4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4, this.plane.getEquationComponentD());
		assertEpsilonEquals(4, this.plane.getZ());
		//
		this.plane.set(new Plane3d(0, 0, 1, -18));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-18, this.plane.getEquationComponentD());
		assertEpsilonEquals(18, this.plane.getZ());
	}

	@DisplayName("set(double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.set(1, 2, 3, 4);
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(4, this.plane.getEquationComponentD());
		assertEpsilonEquals(-4, this.plane.getZ());
		//
		this.plane.set(1, 2, 3, -4);
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4, this.plane.getEquationComponentD());
		assertEpsilonEquals(4, this.plane.getZ());
		//
		this.plane.set(-1, -2, -3, 4);
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(4, this.plane.getEquationComponentD());
		assertEpsilonEquals(4, this.plane.getZ());
		//
		this.plane.set(-1, -2, -3, -4);
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4, this.plane.getEquationComponentD());
		assertEpsilonEquals(-4, this.plane.getZ());
	}

	@DisplayName("clone()")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void testClone(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PlaneXY3d p = this.plane.clone();
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(1.25, this.plane.getEquationComponentD());
	}

	@DisplayName("calculatesPlaneXYPointDistance(boolean,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlaneXYPointDistance(CoordinateSystem3D cs) {
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

	@DisplayName("classifiesPlaneXYPoint(boolean,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticClassifiesPlaneXYPoint(CoordinateSystem3D cs) {
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

	@DisplayName("classifiesPlaneXYSphere(boolean,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticClassifiesPlaneXYSphere(CoordinateSystem3D cs) {
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

	@DisplayName("classifiesPlaneXYSegment(boolean,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticClassifiesPlaneXYSegment(CoordinateSystem3D cs) {
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

	@DisplayName("classifiesPlaneXYAlignedBox(boolean,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticClassifiesPlaneXYAlignedBox(CoordinateSystem3D cs) {
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

	@DisplayName("getDistanceTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.25, this.plane.getDistanceTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(createPoint(0, 0, 1.25)));
		assertEpsilonEquals(11.25, this.plane.getDistanceTo(createPoint(0, 0, -10)));
		assertEpsilonEquals(-8.75, this.plane.getDistanceTo(createPoint(0, 0, 10)));
	}

	@DisplayName("getDistanceTo(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1.25, this.plane.getDistanceTo(0, 0, 0));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0, 0, 1.25));
		assertEpsilonEquals(11.25, this.plane.getDistanceTo(0, 0, -10));
		assertEpsilonEquals(-8.75, this.plane.getDistanceTo(0, 0, 10));
	}

	@DisplayName("getDistanceTo(Plane3D) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToPlane3D_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, 4.5)));
		//
		this.plane.negate();
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, 4.5)));
	}

	@DisplayName("getDistanceTo(Plane3D) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToPlane3D_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-3.25, this.plane.getDistanceTo(new PlaneXY3d(true, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(true, 1.25)));
		assertEpsilonEquals(5.75, this.plane.getDistanceTo(new PlaneXY3d(true, -4.5)));
		assertEpsilonEquals(5.75, this.plane.getDistanceTo(new PlaneXY3d(false, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(false, 1.25)));
		assertEpsilonEquals(-3.25, this.plane.getDistanceTo(new PlaneXY3d(false, 4.5)));
		//
		this.plane.negate();
		assertEpsilonEquals(3.25, this.plane.getDistanceTo(new PlaneXY3d(true, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(true, 1.25)));
		assertEpsilonEquals(-5.75, this.plane.getDistanceTo(new PlaneXY3d(true, -4.5)));
		assertEpsilonEquals(-5.75, this.plane.getDistanceTo(new PlaneXY3d(false, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(false, 1.25)));
		assertEpsilonEquals(3.25, this.plane.getDistanceTo(new PlaneXY3d(false, 4.5)));
	}

	@DisplayName("getDistanceTo(Plane3D) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToPlane3D_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, 4.5)));
		//
		this.plane.negate();
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, 4.5)));
	}

	@DisplayName("getDistanceTo(Plane3D) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToPlane3D_generalPlan(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., this.plane.getDistanceTo(createPlane(1., 0., 1., -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(createPlane(0., 1., 1., -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(createPlane(1., 1., 0., -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(createPlane(1., 1., 1., -4.5)));
		//
		assertEpsilonEquals(0., this.plane.getDistanceTo(createPlane(1., 0., 1., -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(createPlane(0., 1., 1., -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(createPlane(1., 1., 0., -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(createPlane(1., 1., 1., -4.5)));
	}

	@DisplayName("calculatesPlaneXYPlaneDistance(boolean,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlaneXYPlaneDistance(CoordinateSystem3D cs) {
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

	@DisplayName("getDistanceTo(double,double,double,double) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToDoubleDoubleDoubleDouble_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., 1., 0., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., 1., 0., -1.25));
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., 1., 0., 4.5));
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., -1., 0., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., -1., 0., 1.25));
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., -1., 0., 4.5));
		//
		this.plane.negate();
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., 1., 0., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., 1., 0., -1.25));
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., 1., 0., 4.5));
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., -1., 0., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., -1., 0., 1.25));
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., -1., 0., 4.5));
	}

	@DisplayName("getDistanceTo(double,double,double,double) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToDoubleDoubleDoubleDouble_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-3.25, this.plane.getDistanceTo(0., 0., 1., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., 0., 1., -1.25));
		assertEpsilonEquals(5.75, this.plane.getDistanceTo(0., 0., 1., 4.5));
		assertEpsilonEquals(5.75, this.plane.getDistanceTo(0., 0., -1., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., 0., -1., 1.25));
		assertEpsilonEquals(-3.25, this.plane.getDistanceTo(0., 0., -1., 4.5));
		//
		this.plane.negate();
		assertEpsilonEquals(3.25, this.plane.getDistanceTo(0., 0., 1., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., 0., 1., -1.25));
		assertEpsilonEquals(-5.75, this.plane.getDistanceTo(0., 0., 1., 4.5));
		assertEpsilonEquals(-5.75, this.plane.getDistanceTo(0., 0., -1., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., 0., -1., 1.25));
		assertEpsilonEquals(3.25, this.plane.getDistanceTo(0., 0., -1., 4.5));
	}

	@DisplayName("getDistanceTo(double,double,double,double) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToDoubleDoubleDoubleDouble_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., this.plane.getDistanceTo(1., 0., 0., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(1., 0., 0., -1.25));
		assertEpsilonEquals(0., this.plane.getDistanceTo(1., 0., 0., 4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(-1., 0., 0., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(-1., 0., 0., 1.25));
		assertEpsilonEquals(0., this.plane.getDistanceTo(-1., 0., 0., 4.5));
		//
		this.plane.negate();
		assertEpsilonEquals(0., this.plane.getDistanceTo(1., 0., 0., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(1., 0., 0., -1.25));
		assertEpsilonEquals(0., this.plane.getDistanceTo(1., 0., 0., 4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(-1., 0., 0., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(-1., 0., 0., 1.25));
		assertEpsilonEquals(0., this.plane.getDistanceTo(-1., 0., 0., 4.5));
	}

	@DisplayName("getDistanceTo(double,double,double,double) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToDoubleDoubleDoubleDouble_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(0., this.plane.getDistanceTo(1., 0., 1., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., 1., 1., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(1., 1., 0., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(1., 1., 1., -4.5));
		//
		this.plane.negate();
		assertEpsilonEquals(0., this.plane.getDistanceTo(1., 0., 1., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., 1., 1., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(1., 1., 0., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(1., 1., 1., -4.5));
	}

	@DisplayName("getIntersection(Plane3D) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionPlane3D_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp<?, ?, ?, ?, ?, ?, ?> s;

		s = this.plane.getIntersection(new PlaneXZ3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., -4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., -4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 1.25, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 1.25, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		//
		this.plane.negate();
		s = this.plane.getIntersection(new PlaneXZ3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., -4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., -4.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 1.25, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 1.25, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1, 0, 0), s.getDirection());
	}

	@DisplayName("getIntersection(Plane3D) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionPlane3D_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertNull(this.plane.getIntersection(new PlaneXY3d(true, 4.5)));
		assertNull(this.plane.getIntersection(new PlaneXY3d(true, -4.5)));
		assertNull(this.plane.getIntersection(new PlaneXY3d(false, 4.5)));
		assertNull(this.plane.getIntersection(new PlaneXY3d(false, -4.5)));

		assertNull(this.plane.getIntersection(new PlaneXY3d(true, 1.25)));
		assertNull(this.plane.getIntersection(new PlaneXY3d(false, 1.25)));

		//
		this.plane.negate();
		assertNull(this.plane.getIntersection(new PlaneXY3d(true, 4.5)));
		assertNull(this.plane.getIntersection(new PlaneXY3d(true, -4.5)));
		assertNull(this.plane.getIntersection(new PlaneXY3d(false, 4.5)));
		assertNull(this.plane.getIntersection(new PlaneXY3d(false, -4.5)));

		assertNull(this.plane.getIntersection(new PlaneXY3d(true, 1.25)));
		assertNull(this.plane.getIntersection(new PlaneXY3d(false, 1.25)));
	}

	@DisplayName("getIntersection(Plane3D) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionPlane3D_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp<?, ?, ?, ?, ?, ?, ?> s;

		s = this.plane.getIntersection(new PlaneYZ3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		//
		this.plane.negate();
		s = this.plane.getIntersection(new PlaneYZ3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.25, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0, 1, 0), s.getDirection());
	}

	@DisplayName("getIntersection(Plane3D) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionPlane3D_generalplane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		final Point3D<?, ?, ?> p = createPoint();
		final Vector3D<?, ?, ?> v = createVector();

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 1., 1., 0., -1.25, p, v));
		assertEpsilonEquals(createPoint(.625, .625, 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 1., 1., 0., 1.25, p, v));
		assertEpsilonEquals(createPoint(-.625, -.625, 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 0., 1., 1., -1.25, p, v));
		assertEpsilonEquals(createPoint(0., 0., 1.25), p);
		assertEpsilonColinear(createVector(1., 0., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 0., 1., 1., 1.25, p, v));
		assertEpsilonEquals(createPoint(0., -2.5, 1.25), p);
		assertEpsilonColinear(createVector(1., 0., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 1., 0., 1., -1.25, p, v));
		assertEpsilonEquals(createPoint(0, 0, 1.25), p);
		assertEpsilonColinear(createVector(0., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 1., 0., 1., 1.25, p, v));
		assertEpsilonEquals(createPoint(-2.5, 0, 1.25), p);
		assertEpsilonColinear(createVector(0., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 1., 1., 1., -1.25, p, v));
		assertEpsilonEquals(createPoint(0., 0., 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 1., 1., 1., 1.25, p, v));
		assertEpsilonEquals(createPoint(-1.25, -1.25, 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);

		//

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 1., 1., 0., -1.25, p, v));
		assertEpsilonEquals(createPoint(.625, .625, 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 1., 1., 0., 1.25, p, v));
		assertEpsilonEquals(createPoint(-.625, -.625, 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 0., 1., 1., -1.25, p, v));
		assertEpsilonEquals(createPoint(0., 0., 1.25), p);
		assertEpsilonColinear(createVector(1., 0., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 0., 1., 1., 1.25, p, v));
		assertEpsilonEquals(createPoint(0., -2.5, 1.25), p);
		assertEpsilonColinear(createVector(1., 0., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 1., 0., 1., -1.25, p, v));
		assertEpsilonEquals(createPoint(0, 0, 1.25), p);
		assertEpsilonColinear(createVector(0., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 1., 0., 1., 1.25, p, v));
		assertEpsilonEquals(createPoint(-2.5, 0, 1.25), p);
		assertEpsilonColinear(createVector(0., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 1., 1., 1., -1.25, p, v));
		assertEpsilonEquals(createPoint(0., 0., 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 1., 1., 1., 1.25, p, v));
		assertEpsilonEquals(createPoint(-1.25, -1.25, 1.25), p);
		assertEpsilonColinear(createVector(-1., 1., 0.), v);
	}

	@DisplayName("calculatesPlaneXYPlaneIntersection(boolean,double,double,double,double,double,Point3D,Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlaneXYPlaneIntersectionPoint3DVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		final Point3D<?, ?, ?> p = createPoint();
		final Vector3D<?, ?, ?> v = createVector();

		assertFalse(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 0., 0., 1., -4.5, p, v));
		assertFalse(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 0., 0., 1., 4.5, p, v));
		assertFalse(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 0., 0., -1., -4.5, p, v));
		assertFalse(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 0., 0., -1., 4.5, p, v));

		assertFalse(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 0., 0., 1., -1.25, p, v));
		assertFalse(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 0., 0., -1., 1.25, p, v));

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 1., 0., 1., -1.25, p, v));
		assertEpsilonEquals(createPoint(0., 0., 1.25), p);
		assertEpsilonColinear(createVector(0., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 1., 0., 1., 1.25, p, v));
		assertEpsilonEquals(createPoint(-2.5, 0., 1.25), p);
		assertEpsilonColinear(createVector(0., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 0., 1., 1., -1.25, p, v));
		assertEpsilonEquals(createPoint(0., 0., 1.25), p);
		assertEpsilonColinear(createVector(1., 0., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 0., 1., 1., 1.25, p, v));
		assertEpsilonEquals(createPoint(0., -2.5, 1.25), p);
		assertEpsilonColinear(createVector(1., 0., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 1., 1., 0., -1.25, p, v));
		assertEpsilonEquals(createPoint(.625, .625, 1.25), p);
		assertEpsilonColinear(createVector(1., -1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 1., 1., 0., 1.25, p, v));
		assertEpsilonEquals(createPoint(-.625, -.625, 1.25), p);
		assertEpsilonColinear(createVector(1., -1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 1., 1., 1., -1.25, p, v));
		assertEpsilonEquals(createPoint(0., 0., 1.25), p);
		assertEpsilonColinear(createVector(1., -1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(true, 1.25, 1., 1., 1., 1.25, p, v));
		assertEpsilonEquals(createPoint(-1.25, -1.25, 1.25), p);
		assertEpsilonColinear(createVector(1., -1., 0.), v);

		//

		assertFalse(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 0., 0., 1., -4.5, p, v));
		assertFalse(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 0., 0., 1., 4.5, p, v));
		assertFalse(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 0., 0., -1., -4.5, p, v));
		assertFalse(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 0., 0., -1., 4.5, p, v));

		assertFalse(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 0., 0., 1., -1.25, p, v));
		assertFalse(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 0., 0., -1., 1.25, p, v));

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 1., 0., 1., -1.25, p, v));
		assertEpsilonEquals(createPoint(0., 0., 1.25), p);
		assertEpsilonColinear(createVector(0., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 1., 0., 1., 1.25, p, v));
		assertEpsilonEquals(createPoint(-2.5, 0., 1.25), p);
		assertEpsilonColinear(createVector(0., 1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 0., 1., 1., -1.25, p, v));
		assertEpsilonEquals(createPoint(0., 0., 1.25), p);
		assertEpsilonColinear(createVector(1., 0., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 0., 1., 1., 1.25, p, v));
		assertEpsilonEquals(createPoint(0., -2.5, 1.25), p);
		assertEpsilonColinear(createVector(1., 0., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 1., 1., 0., -1.25, p, v));
		assertEpsilonEquals(createPoint(.625, .625, 1.25), p);
		assertEpsilonColinear(createVector(1., -1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 1., 1., 0., 1.25, p, v));
		assertEpsilonEquals(createPoint(-.625, -.625, 1.25), p);
		assertEpsilonColinear(createVector(1., -1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 1., 1., 1., -1.25, p, v));
		assertEpsilonEquals(createPoint(0., 0., 1.25), p);
		assertEpsilonColinear(createVector(1., -1., 0.), v);

		assertTrue(PlaneXY3afp.calculatesPlaneXYPlaneIntersection(false, 1.25, 1., 1., 1., 1.25, p, v));
		assertEpsilonEquals(createPoint(-1.25, -1.25, 1.25), p);
		assertEpsilonColinear(createVector(1., -1., 0.), v);
	}

	@DisplayName("getIntersection(double,double,double,double) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionDoubleDoubleDoubleDouble_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		final Segment3afp<?, ?, ?, ?, ?, ?, ?> s;

		assertNull(this.plane.getIntersection(0., 0., 1., -4.5));
		assertNull(this.plane.getIntersection(0., 0., 1., 4.5));
		assertNull(this.plane.getIntersection(0., 0., -1., -4.5));
		assertNull(this.plane.getIntersection(0., 0., -1., -4.5));

		assertNull(this.plane.getIntersection(0., 0., 1., -1.25));
		assertNull(this.plane.getIntersection(0., 0., -1., 1.25));

		//
		this.plane.negate();
		assertNull(this.plane.getIntersection(0., 0., 1., -4.5));
		assertNull(this.plane.getIntersection(0., 0., 1., 4.5));
		assertNull(this.plane.getIntersection(0., 0., -1., 4.5));
		assertNull(this.plane.getIntersection(0., 0., -1., -4.5));

		assertNull(this.plane.getIntersection(0., 0., 1., -1.25));
		assertNull(this.plane.getIntersection(0., 0., -1., 1.25));
	}

	@DisplayName("getIntersection(double,double,double,double) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionDoubleDoubleDoubleDouble_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3d s;

		assertNull(this.plane.getIntersection(0., 0., 1., -4.5));

		assertNull(this.plane.getIntersection(0., 0., 1., 4.5));

		assertNull(this.plane.getIntersection(0., 0., -1., -4.5));

		assertNull(this.plane.getIntersection(0., 0., -1., -4.5));

		assertNull(this.plane.getIntersection(0., 0., 1., -1.25));

		assertNull(this.plane.getIntersection(0., 0., -1., 1.25));

		//
		this.plane.negate();

		assertNull(this.plane.getIntersection(0., 0., 1., -4.5));

		assertNull(this.plane.getIntersection(0., 0., 1., 4.5));

		assertNull(this.plane.getIntersection(0., 0., -1., 4.5));

		assertNull(this.plane.getIntersection(0., 0., -1., -4.5));

		assertNull(this.plane.getIntersection(0., 0., 1., -1.25));

		assertNull(this.plane.getIntersection(0., 0., -1., 1.25));
	}

	@DisplayName("getIntersection(double,double,double,double) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionDoubleDoubleDoubleDouble_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3d s;

		s = this.plane.getIntersection(1., 0., 0., -4.5);
		assertEpsilonEquals(createPoint(4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(1., 0., 0., 4.5);
		assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(-1., 0., 0., -4.5);
		assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(-1., 0., 0., -4.5);
		assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(1., 0., 0., -1.25);
		assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(-1., 0., 0., 1.25);
		assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		//
		this.plane.negate();

		s = this.plane.getIntersection(1., 0., 0., -4.5);
		assertEpsilonEquals(createPoint(4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(1., 0., 0., 4.5);
		assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(-1., 0., 0., 4.5);
		assertEpsilonEquals(createPoint(4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(-1., 0., 0., -4.5);
		assertEpsilonEquals(createPoint(-4.5, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(1., 0., 0., -1.25);
		assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(-1., 0., 0., 1.25);
		assertEpsilonEquals(createPoint(1.25, 0, 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());
	}

	@DisplayName("getIntersection(double,double,double,double) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionDoubleDoubleDoubleDouble_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3d s;

		s = this.plane.getIntersection(1., 1., 0., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(.625, .625, 1.25), s.getP1());
		assertEpsilonColinear(createVector(-1., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(1., 1., 0., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-.625, -.625, 1.25), s.getP1());
		assertEpsilonColinear(createVector(-1., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(0., 1., 1., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(1., 0., 0.), s.getDirection());

		s = this.plane.getIntersection(0., 1., 1., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., -2.5, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1., 0., 0.), s.getDirection());

		s = this.plane.getIntersection(1., 0., 1., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(1., 0., 1., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.5, 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(0., 1., 0.), s.getDirection());

		s = this.plane.getIntersection(1., 1., 1., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(0., 0., 1.25), s.getP1());
		assertEpsilonColinear(createVector(1., -1., 0.), s.getDirection());

		s = this.plane.getIntersection(1., 1., 1., 1.25);
		assertEpsilonEquals(createPoint(-1.25, -1.25, 1.25), s.getP1());
		assertEpsilonColinear(createVector(1., -1., 0.), s.getDirection());
	}

	@DisplayName("getIntersection(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3d p;

		assertNull(this.plane.getIntersection(createSegment(
				32.5, -5.6, -51.2,
				47.1, -7.9, .5)));

		assertNull(this.plane.getIntersection(createSegment(
				32.5, -5.6, 47.2,
				47.1, -7.9, 2.)));

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 0.,
				47.1, -7.9, 2.));
		assertEpsilonEquals(createPoint(41.625, -7.0375, 1.25), p);

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 2.,
				47.1, -7.9, 0.));
		assertEpsilonEquals(createPoint(37.975, -6.4625, 1.25), p);

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 1.25,
				47.1, -7.9, 0.));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 0.,
				47.1, -7.9, 1.25));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 1.25,
				47.1, -7.9, 2.));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 1.25,
				47.1, -7.9, 1.25));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		//

		this.plane.negate();
		assertNull(this.plane.getIntersection(createSegment(
				32.5, -5.6, -51.2,
				47.1, -7.9, .5)));

		assertNull(this.plane.getIntersection(createSegment(
				32.5, -5.6, 47.2,
				47.1, -7.9, 2.)));
	
		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 0.,
				47.1, -7.9, 2.));
		assertEpsilonEquals(createPoint(41.625, -7.0375, 1.25), p);

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 2.,
				47.1, -7.9, 0.));
		assertEpsilonEquals(createPoint(37.975, -6.4625, 1.25), p);

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 1.25,
				47.1, -7.9, 0.));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 0.,
				47.1, -7.9, 1.25));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 1.25,
				47.1, -7.9, 2.));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 2.,
				47.1, -7.9, 1.25));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 1.25,
				47.1, -7.9, 1.25));
		assertEpsilonEquals(createPoint(32.5, -5.6, 1.25), p);

		p = this.plane.getIntersection(createSegment(
				32.5, -5.6, 2.,
				47.1, -7.9, 1.25));
		assertEpsilonEquals(createPoint(47.1, -7.9, 1.25), p);
	}

	@DisplayName("classifies(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createPoint(0, 0, 0)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createPoint(0, 0, 1.25)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createPoint(0, 0, -10)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createPoint(0, 0, 10)));
		//
		this.plane.negate();
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createPoint(0, 0, 0)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createPoint(0, 0, 1.25)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createPoint(0, 0, -10)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createPoint(0, 0, 10)));
	}

	@DisplayName("classifies(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(0, 0, 1.25));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(0, 0, -10));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(0, 0, 10));
		//
		this.plane.negate();
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(0, 0, 1.25));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(0, 0, -10));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(0, 0, 10));
	}

	@DisplayName("classifies(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createSphere(0, 0, 0, 1)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createSphere(0, 0, 0, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createSphere(0, 0, 0, 2)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createSphere(0, 0, 3, 1)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createSphere(0, 0, 3, 1.75)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createSphere(0, 0, 3, 2)));
		//
		this.plane.negate();
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createSphere(0, 0, 0, 1)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createSphere(0, 0, 0, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createSphere(0, 0, 0, 2)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createSphere(0, 0, 3, 1)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createSphere(0, 0, 3, 1.75)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createSphere(0, 0, 3, 2)));
	}

	@DisplayName("classifies(double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(0, 0, 0, 1));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(0, 0, 0, 1.25));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(0, 0, 0, 2));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(0, 0, 3, 1));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(0, 0, 3, 1.75));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(0, 0, 3, 2));
		//
		this.plane.negate();
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(0, 0, 0, 1));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(0, 0, 0, 1.25));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(0, 0, 0, 2));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(0, 0, 3, 1));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(0, 0, 3, 1.75));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(0, 0, 3, 2));
	}

	@DisplayName("classifies(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createBox(0, 0, 0, 1, 1, 1)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createBox(0, 0, 0, 1, 1, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createBox(0, 0, 0, 1, 1, 2)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createBox(2, 2, 2, 3, 3, 3)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createBox(2, 2, 1.25, 3, 3, 3)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createBox(2, 2, 1, 3, 3, 3)));
		//
		this.plane.negate();
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createBox(0, 0, 0, 1, 1, 1)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createBox(0, 0, 0, 1, 1, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createBox(0, 0, 0, 1, 1, 2)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createBox(2, 2, 2, 3, 3, 3)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createBox(2, 2, 1.25, 3, 3, 3)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createBox(2, 2, 1, 3, 3, 3)));
	}

	@DisplayName("classifies(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(0, 0, 0, 1, 1, 1));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(0, 0, 0, 1, 1, 1.25));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(0, 0, 0, 1, 1, 2));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(2, 2, 2, 3, 3, 3));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(2, 2, 1.25, 3, 3, 3));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(2, 2, 1, 3, 3, 3));
		//
		this.plane.negate();
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(0, 0, 0, 1, 1, 1));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(0, 0, 0, 1, 1, 1.25));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(0, 0, 0, 1, 1, 2));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(2, 2, 2, 3, 3, 3));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(2, 2, 1.25, 3, 3, 3));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(2, 2, 1, 3, 3, 3));
	}

	@DisplayName("classifies(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createSegment(0, 0, 0, 1, 1, 1)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createSegment(0, 0, 0, 1, 1, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createSegment(0, 0, 0, 1, 1, 2)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createSegment(3, 3, 3, 2, 2, 2)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createSegment(3, 3, 3, 2, 2, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createSegment(3, 3, 3, 2, 2, 1)));
		//
		this.plane.negate();
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createSegment(0, 0, 0, 1, 1, 1)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createSegment(0, 0, 0, 1, 1, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createSegment(0, 0, 0, 1, 1, 2)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createSegment(3, 3, 3, 2, 2, 2)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createSegment(3, 3, 3, 2, 2, 1.25)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createSegment(3, 3, 3, 2, 2, 1)));
	}

	@DisplayName("intersects(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.plane.intersects(createPoint(0, 0, 0)));
		assertTrue(this.plane.intersects(createPoint(0, 0, 1.25)));
		assertFalse(this.plane.intersects(createPoint(0, 0, -10)));
		assertFalse(this.plane.intersects(createPoint(0, 0, 10)));
		//
		this.plane.negate();
		assertFalse(this.plane.intersects(createPoint(0, 0, 0)));
		assertTrue(this.plane.intersects(createPoint(0, 0, 1.25)));
		assertFalse(this.plane.intersects(createPoint(0, 0, -10)));
		assertFalse(this.plane.intersects(createPoint(0, 0, 10)));
	}

	@DisplayName("intersects(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.plane.intersects(0, 0, 0));
		assertTrue(this.plane.intersects(0, 0, 1.25));
		assertFalse(this.plane.intersects(0, 0, -10));
		assertFalse(this.plane.intersects(0, 0, 10));
		//
		this.plane.negate();
		assertFalse(this.plane.intersects(0, 0, 0));
		assertTrue(this.plane.intersects(0, 0, 1.25));
		assertFalse(this.plane.intersects(0, 0, -10));
		assertFalse(this.plane.intersects(0, 0, 10));
	}

	@DisplayName("intersects(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.plane.intersects(createSphere(0, 0, 0, 1)));
		assertFalse(this.plane.intersects(createSphere(0, 0, 0, 1.25)));
		assertTrue(this.plane.intersects(createSphere(0, 0, 0, 2)));
		assertFalse(this.plane.intersects(createSphere(0, 0, 3, 1)));
		assertFalse(this.plane.intersects(createSphere(0, 0, 3, 1.75)));
		assertTrue(this.plane.intersects(createSphere(0, 0, 3, 2)));
		//
		this.plane.negate();
		assertFalse(this.plane.intersects(createSphere(0, 0, 0, 1)));
		assertFalse(this.plane.intersects(createSphere(0, 0, 0, 1.25)));
		assertTrue(this.plane.intersects(createSphere(0, 0, 0, 2)));
		assertFalse(this.plane.intersects(createSphere(0, 0, 3, 1)));
		assertFalse(this.plane.intersects(createSphere(0, 0, 3, 1.75)));
		assertTrue(this.plane.intersects(createSphere(0, 0, 3, 2)));
	}

	@DisplayName("intersects(double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.plane.intersects(0, 0, 0, 1));
		assertFalse(this.plane.intersects(0, 0, 0, 1.25));
		assertTrue(this.plane.intersects(0, 0, 0, 2));
		assertFalse(this.plane.intersects(0, 0, 3, 1));
		assertFalse(this.plane.intersects(0, 0, 3, 1.75));
		assertTrue(this.plane.intersects(0, 0, 3, 2));
		//
		this.plane.negate();
		assertFalse(this.plane.intersects(0, 0, 0, 1));
		assertFalse(this.plane.intersects(0, 0, 0, 1.25));
		assertTrue(this.plane.intersects(0, 0, 0, 2));
		assertFalse(this.plane.intersects(0, 0, 3, 1));
		assertFalse(this.plane.intersects(0, 0, 3, 1.75));
		assertTrue(this.plane.intersects(0, 0, 3, 2));
	}

	@DisplayName("intersects(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.plane.intersects(createBox(0, 0, 0, 1, 1, 1)));
		assertFalse(this.plane.intersects(createBox(0, 0, 0, 1, 1, 1.25)));
		assertTrue(this.plane.intersects(createBox(0, 0, 0, 1, 1, 2)));
		assertFalse(this.plane.intersects(createBox(2, 2, 2, 3, 3, 3)));
		assertFalse(this.plane.intersects(createBox(2, 2, 1.25, 3, 3, 3)));
		assertTrue(this.plane.intersects(createBox(2, 2, 1, 3, 3, 3)));
		//
		this.plane.negate();
		assertFalse(this.plane.intersects(createBox(0, 0, 0, 1, 1, 1)));
		assertFalse(this.plane.intersects(createBox(0, 0, 0, 1, 1, 1.25)));
		assertTrue(this.plane.intersects(createBox(0, 0, 0, 1, 1, 2)));
		assertFalse(this.plane.intersects(createBox(2, 2, 2, 3, 3, 3)));
		assertFalse(this.plane.intersects(createBox(2, 2, 1.25, 3, 3, 3)));
		assertTrue(this.plane.intersects(createBox(2, 2, 1, 3, 3, 3)));
	}

	@DisplayName("intersects(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.plane.intersects(0, 0, 0, 1, 1, 1));
		assertFalse(this.plane.intersects(0, 0, 0, 1, 1, 1.25));
		assertTrue(this.plane.intersects(0, 0, 0, 1, 1, 2));
		assertFalse(this.plane.intersects(2, 2, 2, 3, 3, 3));
		assertFalse(this.plane.intersects(2, 2, 1.25, 3, 3, 3));
		assertTrue(this.plane.intersects(2, 2, 1, 3, 3, 3));
		//
		this.plane.negate();
		assertFalse(this.plane.intersects(0, 0, 0, 1, 1, 1));
		assertFalse(this.plane.intersects(0, 0, 0, 1, 1, 1.25));
		assertTrue(this.plane.intersects(0, 0, 0, 1, 1, 2));
		assertFalse(this.plane.intersects(2, 2, 2, 3, 3, 3));
		assertFalse(this.plane.intersects(2, 2, 1.25, 3, 3, 3));
		assertTrue(this.plane.intersects(2, 2, 1, 3, 3, 3));
	}

	@DisplayName("intersects(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.plane.intersects(createSegment(0, 0, 0, 1, 1, 1)));
		assertFalse(this.plane.intersects(createSegment(0, 0, 0, 1, 1, 1.25)));
		assertTrue(this.plane.intersects(createSegment(0, 0, 0, 1, 1, 2)));
		assertFalse(this.plane.intersects(createSegment(3, 3, 3, 2, 2, 2)));
		assertFalse(this.plane.intersects(createSegment(3, 3, 3, 2, 2, 1.25)));
		assertTrue(this.plane.intersects(createSegment(3, 3, 3, 2, 2, 1)));
		//
		assertFalse(this.plane.intersects(createSegment(0, 0, 0, 1, 1, 1)));
		assertFalse(this.plane.intersects(createSegment(0, 0, 0, 1, 1, 1.25)));
		assertTrue(this.plane.intersects(createSegment(0, 0, 0, 1, 1, 2)));
		assertFalse(this.plane.intersects(createSegment(3, 3, 3, 2, 2, 2)));
		assertFalse(this.plane.intersects(createSegment(3, 3, 3, 2, 2, 1.25)));
		assertTrue(this.plane.intersects(createSegment(3, 3, 3, 2, 2, 1)));
	}

}