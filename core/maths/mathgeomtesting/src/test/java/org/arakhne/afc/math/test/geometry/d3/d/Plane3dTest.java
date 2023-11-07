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

import static org.junit.jupiter.api.Assertions.*;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.PlaneClassification;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
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
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.arakhne.afc.math.geometry.d3.d.Segment3d;
import org.arakhne.afc.math.geometry.d3.d.Sphere3d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.test.AbstractMathTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class Plane3dTest extends AbstractMathTestCase {

	private static final double A = 2.;

	private static final double NORMAL_X = .8164965809277261;

	private static final double B = 1.;

	private static final double NORMAL_Y = .4082482904638631;

	private static final double C = 1.;

	private static final double NORMAL_Z = .4082482904638631;

	private static final double D = 4;

	private Plane3d plane;

	protected Point3D<?, ?, ?> createPoint() {
		return new Point3d();
	}

	protected Point3D<?, ?, ?> createPoint(double x, double y, double z) {
		return new Point3d(x, y, z);
	}
	
	protected Vector3D<?, ?, ?> createVector(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	protected Vector3D<?, ?, ?> createVector() {
		return new Vector3d();
	}

	protected Segment3afp<?, ?, ?, ?, ?, ?, ?> createSegment(double x1, double y1, double z1, double x2, double y2, double z2) {
		return new Segment3d(x1, y1, z1, x2, y2, z2);
	}

	protected Segment3afp<?, ?, ?, ?, ?, ?, ?> createSegment() {
		return new Segment3d();
	}

	protected Sphere3afp<?, ?, ?, ?, ?, ?, ?> createSphere(double x, double y, double z, double radius) {
		return new Sphere3d(x, y, z, radius);
	}

	protected Box3afp<?, ?, ?, ?, ?, ?, ?> createBox(double minx, double miny, double minz, double maxx, double maxy, double maxz) {
		return new AlignedBox3d(minx, miny, minz, maxx - minx, maxy - miny, maxz - minz);
	}

	@BeforeEach
	public void setUp() {
		this.plane = new Plane3d(A, B, C, D);
	}

	@DisplayName("toGeogebra")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void toGeogebra(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals("0.8164965809277261*x+0.4082482904638631*y+0.4082482904638631*z+4.0=0.0", this.plane.toGeogebra());
	}

	@DisplayName("calculatesPlanePointDistance(double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlanePointDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(4, Plane3afp.calculatesPlanePointDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0, 0, 0));
		assertEpsilonEquals(-4, Plane3afp.calculatesPlanePointDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 0, 0, 0));
		assertEpsilonEquals(-2.94022093788, Plane3afp.calculatesPlanePointDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0));
		assertEpsilonEquals(2.940220937885, Plane3afp.calculatesPlanePointDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -5, -7, 0));
		assertEpsilonEquals(5.6329931618, Plane3afp.calculatesPlanePointDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -1, 8, -2));
		assertEpsilonEquals(-5.6329931618, Plane3afp.calculatesPlanePointDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -1, 8, -2));
	}

	@DisplayName("calculatesPlanePlaneDistance(double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlanePlaneDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(A, B, C, D, A, B, C, D));
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, A, B, C, D));
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(A, B, C, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D));
		assertEpsilonEquals(18., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 18, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertEpsilonEquals(18., Plane3afp.calculatesPlanePlaneDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D + 18, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		//
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X - 1, NORMAL_Y, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X, NORMAL_Y - 1, NORMAL_Z, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertEpsilonEquals(0., Plane3afp.calculatesPlanePlaneDistance(NORMAL_X, NORMAL_Y, NORMAL_Z - 1, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D));
	}

	@DisplayName("calculatesPlanePlaneIntersection(double,double,double,double,double,double,double,double,Point3D,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlanePlaneIntersectionPoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D<?, ?, ?> p1;
		Point3D<?, ?, ?> p2;

		p1 = createPoint();
		p2 = createPoint();
		assertFalse(Plane3afp.calculatesPlanePlaneIntersection(A, B, C, D, A, B, C, D, p1, p2));

		p1 = createPoint();
		p2 = createPoint();
		assertFalse(Plane3afp.calculatesPlanePlaneIntersection(A, B, C, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D, p1, p2));

		p1 = createPoint();
		p2 = createPoint();
		assertFalse(Plane3afp.calculatesPlanePlaneIntersection(A, B, C, D, -NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, p1, p2));
		
		p1 = createPoint();
		p2 = createPoint();
		assertFalse(Plane3afp.calculatesPlanePlaneIntersection(A, B, C, D, -NORMAL_X, -NORMAL_Y, -NORMAL_Z, D, p1, p2));

		p1 = createPoint();
		p2 = createPoint();
		assertTrue(Plane3afp.calculatesPlanePlaneIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 1., 2., 0., 5., p1, p2));
		assertEpsilonEquals(createPoint(-3.4848395590568755, -0.7575802204715616, -2.070699632547397), p1);
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-2.6683429781291492, -1.1658285109354247, -3.2954445039389864), p2);
		} else {
			assertEpsilonEquals(createPoint(-4.301336139984602, -0.3493319300076985, -0.8459547611558078), p2);
		}
	}

	@DisplayName("calculatesPlanePlaneIntersection(double,double,double,double,double,double,double,double,Point3D,Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlanePlaneIntersectionPoint3DVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D<?, ?, ?> p;
		Vector3D<?, ?, ?> v;

		p = createPoint();
		v = createVector();
		assertFalse(Plane3afp.calculatesPlanePlaneIntersection(A, B, C, D, A, B, C, D, p, v));

		p = createPoint();
		v = createVector();
		assertFalse(Plane3afp.calculatesPlanePlaneIntersection(A, B, C, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D, p, v));

		p = createPoint();
		v = createVector();
		assertFalse(Plane3afp.calculatesPlanePlaneIntersection(A, B, C, D, -NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, p, v));
		
		p = createPoint();
		v = createVector();
		assertFalse(Plane3afp.calculatesPlanePlaneIntersection(A, B, C, D, -NORMAL_X, -NORMAL_Y, -NORMAL_Z, D, p, v));

		p = createPoint();
		v = createVector();
		assertTrue(Plane3afp.calculatesPlanePlaneIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 1., 2., 0., 5., p, v));
		assertEpsilonEquals(createPoint(-3.4848395590568755, -.7575802204715616, -2.070699632547397), p);
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createVector(.8164965809277261, -.4082482904638631, -1.2247448713915892), v);
		} else {
			assertEpsilonEquals(createVector(-.8164965809277261, .4082482904638631, 1.2247448713915892), v);
		}
	}

	@DisplayName("calculatesPlanePlaneIntersection(double,double,double,double,double,double,double,double,Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlanePlaneIntersectionSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp<?, ?, ?, ?, ?, ?, ?> segment;

		segment = createSegment();
		assertFalse(Plane3afp.calculatesPlanePlaneIntersection(A, B, C, D, A, B, C, D, segment));

		segment = createSegment();
		assertFalse(Plane3afp.calculatesPlanePlaneIntersection(A, B, C, D, NORMAL_X, NORMAL_Y, NORMAL_Z, D, segment));

		segment = createSegment();
		assertFalse(Plane3afp.calculatesPlanePlaneIntersection(A, B, C, D, -NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, segment));
		
		segment = createSegment();
		assertFalse(Plane3afp.calculatesPlanePlaneIntersection(A, B, C, D, -NORMAL_X, -NORMAL_Y, -NORMAL_Z, D, segment));

		segment = createSegment();
		assertTrue(Plane3afp.calculatesPlanePlaneIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 1., 2., 0., 5., segment));
		assertEpsilonEquals(createPoint(-3.4848395590568755, -0.7575802204715616, -2.070699632547397), segment.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-2.6683429781291492, -1.1658285109354247, -3.2954445039389864), segment.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.301336139984602, -0.3493319300076985, -0.8459547611558078), segment.getP2());
		}
	}

	@DisplayName("calculatesPlanePointProjection(double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlanePointProjection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D<?, ?, ?> p;

		p = createPoint();
		Plane3afp.calculatesPlanePointProjection(
			NORMAL_X, NORMAL_Y, NORMAL_Z, D,
			0, 0, 0,
			p);
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);

		p = createPoint();
		Plane3afp.calculatesPlanePointProjection(
			-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
			0, 0, 0,
			p);
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);

		p = createPoint();
		Plane3afp.calculatesPlanePointProjection(
			NORMAL_X, NORMAL_Y, NORMAL_Z, D,
			-5, -7, 0,
			p);
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint();
		Plane3afp.calculatesPlanePointProjection(
			-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
			-5, -7, 0,
			p);
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint();
		Plane3afp.calculatesPlanePointProjection(
			NORMAL_X, NORMAL_Y, NORMAL_Z, D,
			-1, 8, -2,
			p);
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);

		p = createPoint();
		Plane3afp.calculatesPlanePointProjection(
			-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D,
			-1, 8, -2,
			p);
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852), p);
	}

	@DisplayName("calculatesPlaneSegmentIntersectionFactor(double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlaneSegmentIntersectionFactor(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertNaN(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2., 2., -2.));
		assertNaN(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2., 2., -2., 0., 0., 0.));

		assertEpsilonEquals(1., Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.));

		assertEpsilonEquals(.576350527713689, Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -5, -7, 0));
		assertEpsilonEquals(0.423649472286, Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, 0., 0., 0.));

		assertNaN(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -9, -2., -1, -5, -7, 0));
		assertNaN(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, -9, -2., -1));
		
		assertInfinity(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertInfinity(Plane3afp.calculatesPlaneSegmentIntersectionFactor(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814));
	}

	@DisplayName("calculatesPlaneSegmentIntersection(double,double,double,double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlaneSegmentIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D<?, ?, ?> p;
		
		p = createPoint();
		assertFalse(Plane3afp.calculatesPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2., 2., -2., p));

		p = createPoint();
		assertFalse(Plane3afp.calculatesPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2., 2., -2., 0., 0., 0., p));

		p = createPoint();
		assertTrue(Plane3afp.calculatesPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814, p));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint();
		assertTrue(Plane3afp.calculatesPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0., p));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint();
		assertTrue(Plane3afp.calculatesPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -5, -7, 0, p));
		assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);

		p = createPoint();
		assertTrue(Plane3afp.calculatesPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, 0., 0., 0., p));
		assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);

		p = createPoint();
		assertFalse(Plane3afp.calculatesPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -9, -2., -1, -5, -7, 0, p));

		p = createPoint();
		assertFalse(Plane3afp.calculatesPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, -9, -2., -1, p));

		p = createPoint();
		assertTrue(Plane3afp.calculatesPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523, p));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = createPoint();
		assertTrue(Plane3afp.calculatesPlaneSegmentIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814, p));
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getNormal(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(createVector(NORMAL_X, NORMAL_Y, NORMAL_Z), this.plane.getNormal());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getNormalX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(NORMAL_X, this.plane.getNormalX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getNormalY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(NORMAL_Y, this.plane.getNormalY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getNormalZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(NORMAL_Z, this.plane.getNormalZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getEquationComponentA(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getEquationComponentB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getEquationComponentC(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getEquationComponentD(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(D, this.plane.getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.clear();
		assertEpsilonEquals(1., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(0., this.plane.getEquationComponentC());
		assertEpsilonEquals(0., this.plane.getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void negate(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.negate();
		assertEpsilonEquals(-NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(-NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(-NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(-D, this.plane.getEquationComponentD());
		//
		this.plane.negate();
		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(D, this.plane.getEquationComponentD());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void absolute(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.absolute();
		assertEpsilonEquals(0.8164965809277261, this.plane.getEquationComponentA());
		assertEpsilonEquals(0.4082482904638631, this.plane.getEquationComponentB());
		assertEpsilonEquals(0.4082482904638631, this.plane.getEquationComponentC());
		assertEpsilonEquals(4, this.plane.getEquationComponentD());
		//
		Plane3d p = new Plane3d(-1, 2, -3, 4);
		// length = 3.741657387
		// x = -0.267261242
		// y = 0.534522484
		// z = -0.801783726
		p.absolute();
		assertEpsilonEquals(.267261242, p.getEquationComponentA());
		assertEpsilonEquals(.534522484, p.getEquationComponentB());
		assertEpsilonEquals(.801783726, p.getEquationComponentC());
		assertEpsilonEquals(4, p.getEquationComponentD());
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

		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
				this.plane.getProjection(createPoint(0, 0, 0)));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814),
				this.plane.getProjection(createPoint(-5, -7, 0)));
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852),
				this.plane.getProjection(createPoint(-1, 8, -2)));

		//
		this.plane.negate();
		
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
				this.plane.getProjection(createPoint(0, 0, 0)));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814),
				this.plane.getProjection(createPoint(-5, -7, 0)));
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852),
				this.plane.getProjection(createPoint(-1, 8, -2)));
	}

	@DisplayName("getProjection(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getProjectionDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
				this.plane.getProjection(0, 0, 0));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814),
				this.plane.getProjection(-5, -7, 0));
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852),
				this.plane.getProjection(-1, 8, -2));

		//
		this.plane.negate();
		
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523),
				this.plane.getProjection(0, 0, 0));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814),
				this.plane.getProjection(-5, -7, 0));
		assertEpsilonEquals(createPoint(-5.59931965704, 5.70034017147, -4.29965982852),
				this.plane.getProjection(-1, 8, -2));
	}

	@DisplayName("setPivot(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPivotDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.setPivot(0, 0, 0);
		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(0., this.plane.getEquationComponentD());
		//
		this.plane.setPivot(1, 2, 3);
		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(-2.8577380332470415, this.plane.getEquationComponentD());
		//
		this.plane.setPivot(-5, 4, -1);
		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(2.857738033247041, this.plane.getEquationComponentD());
	}

	@DisplayName("setPivot(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPivotPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.setPivot(createPoint(0, 0, 0));
		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(0., this.plane.getEquationComponentD());
		//
		this.plane.setPivot(createPoint(1, 2, 3));
		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(-2.8577380332470415, this.plane.getEquationComponentD());
		//
		this.plane.setPivot(createPoint(-5, 4, -1));
		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(2.857738033247041, this.plane.getEquationComponentD());
	}

	@DisplayName("translate(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.translate(1, 2, 3);
		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(1.1422619667529594, this.plane.getEquationComponentD());
	}

	@DisplayName("translate(Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.translate(createVector(1, 2, 3));
		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(1.1422619667529594, this.plane.getEquationComponentD());
	}

	@DisplayName("translate(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.translate(7);
		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(11, this.plane.getEquationComponentD());
		//
		this.plane.translate(-25);
		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(-14, this.plane.getEquationComponentD());
	}

	@DisplayName("rotate(Quaternion,null)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void rotateQuaternionPoint3D_null(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		
		this.plane.rotate(q, null);

		assertEpsilonEquals(0.9525793444, this.plane.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, this.plane.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, this.plane.getEquationComponentC());
		assertEpsilonEquals(2.44444444444, this.plane.getEquationComponentD());
	}

	@DisplayName("rotate(Quaternion,getPivot())")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void rotateQuaternionPoint3D_pivot(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);

		Point3D<?, ?, ?> pv = this.plane.getPivot().clone();
		this.plane.rotate(q, pv);

		assertEpsilonEquals(0.9525793444, this.plane.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, this.plane.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, this.plane.getEquationComponentC());
		assertEpsilonEquals(2.44444444444, this.plane.getEquationComponentD());
	}

	@DisplayName("rotate(Quaternion,(0,0,0)) #1")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void rotateQuaternionPoint3D_origin(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);

		this.plane.rotate(q, createPoint(0,0,0));

		assertEpsilonEquals(0.9525793444, this.plane.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, this.plane.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, this.plane.getEquationComponentC());
		assertEpsilonEquals(4., this.plane.getEquationComponentD());
	}

	@DisplayName("rotate(Quaternion,(1,-2,3))")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void rotateQuaternionPoint3D_pts(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);

		this.plane.rotate(q, createPoint(1,-2,3));

		assertEpsilonEquals(0.9525793444, this.plane.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, this.plane.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, this.plane.getEquationComponentC());
		assertEpsilonEquals(4.8164965809, this.plane.getEquationComponentD());
	}

	@DisplayName("set(Plane3D) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPlane3D_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.set(new PlaneXZ3d(true, -4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(1., this.plane.getEquationComponentB());
		assertEpsilonEquals(0., this.plane.getEquationComponentC());
		assertEpsilonEquals(4., this.plane.getEquationComponentD());
		//
		this.plane.set(new PlaneXZ3d(true, 4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(1., this.plane.getEquationComponentB());
		assertEpsilonEquals(0., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4., this.plane.getEquationComponentD());
		//
		this.plane.set(new PlaneXZ3d(false, 4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(-1., this.plane.getEquationComponentB());
		assertEpsilonEquals(0., this.plane.getEquationComponentC());
		assertEpsilonEquals(4., this.plane.getEquationComponentD());
		//
		this.plane.set(new PlaneXZ3d(false, -4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(-1., this.plane.getEquationComponentB());
		assertEpsilonEquals(0., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4., this.plane.getEquationComponentD());
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
		//
		this.plane.set(new PlaneXY3d(true, 4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4, this.plane.getEquationComponentD());
		//
		this.plane.set(new PlaneXY3d(false, 4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(4, this.plane.getEquationComponentD());
		//
		this.plane.set(new PlaneXY3d(false, -4));
		assertEpsilonEquals(0., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(-1., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4, this.plane.getEquationComponentD());
	}

	@DisplayName("set(Plane3D) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPlane3D_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.set(new PlaneYZ3d(true, -4));
		assertEpsilonEquals(1., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(0., this.plane.getEquationComponentC());
		assertEpsilonEquals(4., this.plane.getEquationComponentD());
		//
		this.plane.set(new PlaneYZ3d(true, 4));
		assertEpsilonEquals(1., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(0., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4., this.plane.getEquationComponentD());
		//
		this.plane.set(new PlaneYZ3d(false, 4));
		assertEpsilonEquals(-1., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(0., this.plane.getEquationComponentC());
		assertEpsilonEquals(4., this.plane.getEquationComponentD());
		//
		this.plane.set(new PlaneYZ3d(false, -4));
		assertEpsilonEquals(-1., this.plane.getEquationComponentA());
		assertEpsilonEquals(0., this.plane.getEquationComponentB());
		assertEpsilonEquals(0., this.plane.getEquationComponentC());
		assertEpsilonEquals(-4., this.plane.getEquationComponentD());
	}

	@DisplayName("set(Plane3D) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPlane3D_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.set(new Plane3d(1, -2, 3, -4));
		// Length = 3.741657387
		// x = 0.267261242
		// y = -0.534522484
		// z = 0.801783726
		assertEpsilonEquals(.267261242, this.plane.getEquationComponentA());
		assertEpsilonEquals(-.534522484, this.plane.getEquationComponentB());
		assertEpsilonEquals(.801783726, this.plane.getEquationComponentC());
		assertEpsilonEquals(-4, this.plane.getEquationComponentD());
	}

	@DisplayName("set(double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.plane.set(1, -2, 3, -4);
		// Length = 3.741657387
		// x = 0.267261242
		// y = -0.534522484
		// z = 0.801783726
		assertEpsilonEquals(.267261242, this.plane.getEquationComponentA());
		assertEpsilonEquals(-.534522484, this.plane.getEquationComponentB());
		assertEpsilonEquals(.801783726, this.plane.getEquationComponentC());
		assertEpsilonEquals(-4, this.plane.getEquationComponentD());

		this.plane.set(1, 0, 0, -4);
		assertEpsilonEquals(1, this.plane.getEquationComponentA());
		assertEpsilonEquals(0, this.plane.getEquationComponentB());
		assertEpsilonEquals(0, this.plane.getEquationComponentC());
		assertEpsilonEquals(-4, this.plane.getEquationComponentD());

		this.plane.set(0, 1, 0, -4);
		assertEpsilonEquals(0, this.plane.getEquationComponentA());
		assertEpsilonEquals(1, this.plane.getEquationComponentB());
		assertEpsilonEquals(0, this.plane.getEquationComponentC());
		assertEpsilonEquals(-4, this.plane.getEquationComponentD());

		this.plane.set(0, 0, 1, -4);
		assertEpsilonEquals(0, this.plane.getEquationComponentA());
		assertEpsilonEquals(0, this.plane.getEquationComponentB());
		assertEpsilonEquals(1, this.plane.getEquationComponentC());
		assertEpsilonEquals(-4, this.plane.getEquationComponentD());
	}

	@DisplayName("clone()")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void testClone(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Plane3d p = this.plane.clone();
		assertEpsilonEquals(0.8164965809277261, this.plane.getEquationComponentA());
		assertEpsilonEquals(0.4082482904638631, this.plane.getEquationComponentB());
		assertEpsilonEquals(0.4082482904638631, this.plane.getEquationComponentC());
		assertEpsilonEquals(4, this.plane.getEquationComponentD());
	}

	@DisplayName("getDistanceTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(4, this.plane.getDistanceTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(-2.94022093788, this.plane.getDistanceTo(createPoint(-5, -7, 0)));
		assertEpsilonEquals(5.6329931618, this.plane.getDistanceTo(createPoint(-1, 8, -2)));
		this.plane.negate();
		assertEpsilonEquals(-4, this.plane.getDistanceTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(2.940220937885, this.plane.getDistanceTo(createPoint(-5, -7, 0)));
		assertEpsilonEquals(-5.6329931618, this.plane.getDistanceTo(createPoint(-1, 8, -2)));
	}

	@DisplayName("getDistanceTo(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(4, this.plane.getDistanceTo(0, 0, 0));
		assertEpsilonEquals(-2.94022093788, this.plane.getDistanceTo(-5, -7, 0));
		assertEpsilonEquals(5.6329931618, this.plane.getDistanceTo(-1, 8, -2));
		this.plane.negate();
		assertEpsilonEquals(-4, this.plane.getDistanceTo(0, 0, 0));
		assertEpsilonEquals(2.940220937885, this.plane.getDistanceTo(-5, -7, 0));
		assertEpsilonEquals(-5.6329931618, this.plane.getDistanceTo(-1, 8, -2));
	}

	@DisplayName("getDistanceTo(Plane3D) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToPlane3D_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, -1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, -1.25)));
		//
		this.plane.negate();
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(true, -1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXZ3d(false, -1.25)));
	}

	@DisplayName("getDistanceTo(Plane3D) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToPlane3D_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(true, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(true, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(true, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(true, -1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(false, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(false, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(false, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(false, -1.25)));
		//
		this.plane.negate();
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(true, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(true, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(true, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(true, -1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(false, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(false, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(false, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneXY3d(false, -1.25)));
	}

	@DisplayName("getDistanceTo(Plane3D) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToPlane3D_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, -1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, -1.25)));
		//
		this.plane.negate();
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(true, -1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, 4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, 1.25)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, -4.5)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new PlaneYZ3d(false, -1.25)));
	}

	@DisplayName("getDistanceTo(Plane3D) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceToPlane3D_generalPlan(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0., this.plane.getDistanceTo(new Plane3d(A, B, C, D)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new Plane3d(NORMAL_X, NORMAL_Y, NORMAL_Z, D)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new Plane3d(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D)));
		assertEpsilonEquals(18., this.plane.getDistanceTo(new Plane3d(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 18)));
		assertEpsilonEquals(18., this.plane.getDistanceTo(new Plane3d(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D + 18)));
		//
		assertEpsilonEquals(0., this.plane.getDistanceTo(new Plane3d(NORMAL_X - 1, NORMAL_Y, NORMAL_Z, D)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new Plane3d(NORMAL_X, NORMAL_Y - 1, NORMAL_Z, D)));
		assertEpsilonEquals(0., this.plane.getDistanceTo(new Plane3d(NORMAL_X, NORMAL_Y, NORMAL_Z - 1, D)));
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
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., 0., 1., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., 0., 1., -1.25));
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., 0., 1., 4.5));
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., 0., -1., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., 0., -1., 1.25));
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., 0., -1., 4.5));
		//
		this.plane.negate();
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., 0., 1., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., 0., 1., -1.25));
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., 0., 1., 4.5));
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., 0., -1., -4.5));
		assertEpsilonEquals(0., this.plane.getDistanceTo(0., 0., -1., 1.25));
		assertEpsilonEquals(0, this.plane.getDistanceTo(0., 0., -1., 4.5));
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
		assertEpsilonEquals(0., this.plane.getDistanceTo(A, B, C, D));
		assertEpsilonEquals(0., this.plane.getDistanceTo(NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertEpsilonEquals(0., this.plane.getDistanceTo(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D));
		assertEpsilonEquals(18., this.plane.getDistanceTo(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 18));
		assertEpsilonEquals(18., this.plane.getDistanceTo(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D + 18));
		//
		assertEpsilonEquals(0., this.plane.getDistanceTo(NORMAL_X + 1, NORMAL_Y, NORMAL_Z, D));
		assertEpsilonEquals(0., this.plane.getDistanceTo(NORMAL_X, NORMAL_Y + 1, NORMAL_Z, D));
		assertEpsilonEquals(0., this.plane.getDistanceTo(NORMAL_X, NORMAL_Y, NORMAL_Z + 1, D));
	}

	@DisplayName("getIntersection(Plane3D) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionPlane3D_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp<?, ?, ?, ?, ?, ?, ?> s;

		s = this.plane.getIntersection(new PlaneXZ3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		//
		this.plane.negate();

		s = this.plane.getIntersection(new PlaneXZ3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(new PlaneXZ3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
	}

	@DisplayName("getIntersection(Plane3D) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionPlane3D_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3d s;

		s = this.plane.getIntersection(new PlaneXY3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		}

		s = this.plane.getIntersection(new PlaneXY3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		}

		s = this.plane.getIntersection(new PlaneXY3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		}

		s = this.plane.getIntersection(new PlaneXY3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		}

		s = this.plane.getIntersection(new PlaneXY3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		}

		s = this.plane.getIntersection(new PlaneXY3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		}

		//
		this.plane.negate();

		s = this.plane.getIntersection(new PlaneXY3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		}

		s = this.plane.getIntersection(new PlaneXY3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		}

		s = this.plane.getIntersection(new PlaneXY3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		}

		s = this.plane.getIntersection(new PlaneXY3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		}

		s = this.plane.getIntersection(new PlaneXY3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		}

		s = this.plane.getIntersection(new PlaneXY3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		}
	}

	@DisplayName("getIntersection(Plane3D) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionPlane3D_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3d s;

		s = this.plane.getIntersection(new PlaneYZ3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		//
		this.plane.negate();

		s = this.plane.getIntersection(new PlaneYZ3d(true, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(true, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(false, -4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(false, 4.5));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(true, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(new PlaneYZ3d(false, 1.25));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
	}

	@DisplayName("getIntersection(Plane3D) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionPlane3D_generalplane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3d s;

		s = this.plane.getIntersection(new Plane3d(A, B, C, D));
		assertNull(s);

		s = this.plane.getIntersection(new Plane3d(NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertNull(s);

		s = this.plane.getIntersection(new Plane3d(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D));
		assertNull(s);
		
		s = this.plane.getIntersection(new Plane3d(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1));
		assertNull(s);

		s = this.plane.getIntersection(new Plane3d(1., 2., 0., 5.));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.6019338608427405, -4.2892030133281045, -0.30488823611912585), s.getP1());
		assertEpsilonColinear(createVector(-0.82, 0.41, 1.22), s.getDirection());

		//
		this.plane.negate();

		s = this.plane.getIntersection(new Plane3d(A, B, C, D));
		assertNull(s);

		s = this.plane.getIntersection(new Plane3d(NORMAL_X, NORMAL_Y, NORMAL_Z, D));
		assertNull(s);

		s = this.plane.getIntersection(new Plane3d(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D));
		assertNull(s);
		
		s = this.plane.getIntersection(new Plane3d(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1));
		assertNull(s);

		s = this.plane.getIntersection(new Plane3d(1., 2., 0., 5.));
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.6019338608427405, -4.2892030133281045, -0.30488823611912585), s.getP1());
		assertEpsilonColinear(createVector(-0.82, 0.41, 1.22), s.getDirection());
	}

	@DisplayName("getIntersection(double,double,double,double) with XZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionDoubleDoubleDoubleDouble_xz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp<?, ?, ?, ?, ?, ?, ?> s;

		s = this.plane.getIntersection(0., 1., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(0., 1., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(0., -1., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(0., -1., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(0., 1., 0., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(0., -1., 0., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		//
		this.plane.negate();

		s = this.plane.getIntersection(0., 1., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(0., 1., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(0., -1., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -4.5, -1.059591794226542), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(0., -1., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, 4.5, -02.8595917942265423), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(0., 1., 0., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());

		s = this.plane.getIntersection(0., -1., 0., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, 1.25, -2.2095917942265424), s.getP1());
		assertEpsilonColinear(createVector(-0.40824829046386313, 0.0, 0.8164965809277263), s.getDirection());
	}

	@DisplayName("getIntersection(double,double,double,double) with XY plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionDoubleDoubleDoubleDouble_xy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3d s;

		s = this.plane.getIntersection(0., 0., 1., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		}

		s = this.plane.getIntersection(0., 0., 1., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		}

		s = this.plane.getIntersection(0., 0., -1., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		}

		s = this.plane.getIntersection(0., 0., -1., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		}

		s = this.plane.getIntersection(0., 0., 1., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		}

		s = this.plane.getIntersection(0., 0., -1., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		}

		//
		this.plane.negate();

		s = this.plane.getIntersection(0., 0., 1., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		}

		s = this.plane.getIntersection(0., 0., 1., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		}

		s = this.plane.getIntersection(0., 0., -1., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-2.119183588453084, -1.059591794226542, -4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-2.527431878916947, -0.2430952132988159, -4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-1.710935297989221, -1.8760883751542683, -4.5), s.getP2());
		}

		s = this.plane.getIntersection(0., 0., -1., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-5.719183588453085, -2.8595917942265423, 4.5), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-6.127431878916948, -2.043095213298816, 4.5), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-5.3109352979892215, -3.6760883751542686, 4.5), s.getP2());
		}

		s = this.plane.getIntersection(0., 0., 1., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		}

		s = this.plane.getIntersection(0., 0., -1., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.419183588453085, -2.2095917942265424, 1.25), s.getP1());
		if (cs.isLeftHanded()) {
			assertEpsilonEquals(createPoint(-4.827431878916948, -1.3930952132988161, 1.25), s.getP2());
		} else {
			assertEpsilonEquals(createPoint(-4.010935297989222, -3.0260883751542687, 1.25), s.getP2());
		}
	}

	@DisplayName("getIntersection(double,double,double,double) with YZ plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionDoubleDoubleDoubleDouble_yz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3afp<?, ?, ?, ?, ?, ?, ?> s;

		s = this.plane.getIntersection(1., 0., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(1., 0., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(-1., 0., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(-1., 0., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(1., 0., 0., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(-1., 0., 0., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		//
		this.plane.negate();

		s = this.plane.getIntersection(1., 0., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(1., 0., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(-1., 0., 0., -4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-4.5, -0.39897948556635543, -0.39897948556635543), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(-1., 0., 0., 4.5);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(4.5, -9.398979485566356, -9.398979485566356), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(1., 0., 0., -1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());

		s = this.plane.getIntersection(-1., 0., 0., 1.25);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(1.2499999999999998, -6.148979485566355, -6.148979485566355), s.getP1());
		assertEpsilonColinear(createVector(0.0, 0.7071067811865475, -0.7071067811865475), s.getDirection());
	}

	@DisplayName("getIntersection(double,double,double,double) with general plane")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionDoubleDoubleDoubleDouble_generalPlane(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Segment3d s;

		s = this.plane.getIntersection(A, B, C, D);
		assertNull(s);

		s = this.plane.getIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		assertNull(s);

		s = this.plane.getIntersection(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D);
		assertNull(s);
		
		s = this.plane.getIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1);
		assertNull(s);

		s = this.plane.getIntersection(1., 2., 0., 5.);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-3.4848395590568755, -0.7575802204715616, -2.070699632547397), s.getP1());
		assertEpsilonColinear(createVector(-0.82, 0.41, 1.22), s.getDirection());

		//
		this.plane.negate();

		s = this.plane.getIntersection(A, B, C, D);
		assertNull(s);

		s = this.plane.getIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D);
		assertNull(s);

		s = this.plane.getIntersection(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D);
		assertNull(s);
		
		s = this.plane.getIntersection(NORMAL_X, NORMAL_Y, NORMAL_Z, D + 1);
		assertNull(s);

		s = this.plane.getIntersection(1., 2., 0., 5.);
		assertNotNull(s);
		assertEpsilonEquals(createPoint(-3.4848395590568755, -0.7575802204715616, -2.070699632547397), s.getP1());
		assertEpsilonColinear(createVector(-0.82, 0.41, 1.22), s.getDirection());
	}

	@DisplayName("getIntersection(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getIntersectionSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3d p;

		assertNull(this.plane.getIntersection(createSegment(0., 0., 0., -2., 2., -2.)));

		assertNull(this.plane.getIntersection(createSegment(-2., 2., -2., 0., 0., 0.)));

		p = this.plane.getIntersection(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = this.plane.getIntersection(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = this.plane.getIntersection(createSegment(0., 0., 0., -5, -7, 0));
		assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);

		p = this.plane.getIntersection(createSegment(-5, -7, 0, 0., 0., 0.));
		assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);

		assertNull(this.plane.getIntersection(createSegment(-9, -2., -1, -5, -7, 0)));

		assertNull(this.plane.getIntersection(createSegment(-5, -7, 0, -9, -2., -1)));

		p = this.plane.getIntersection(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = this.plane.getIntersection(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);

		//
		this.plane.negate();

		assertNull(this.plane.getIntersection(createSegment(0., 0., 0., -2., 2., -2.)));

		assertNull(this.plane.getIntersection(createSegment(-2., 2., -2., 0., 0., 0.)));

		p = this.plane.getIntersection(createSegment(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = this.plane.getIntersection(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = this.plane.getIntersection(createSegment(0., 0., 0., -5, -7, 0));
		assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);

		p = this.plane.getIntersection(createSegment(-5, -7, 0, 0., 0., 0.));
		assertEpsilonEquals(createPoint(-2.881752638568445, -4.034453693995823, 0.0), p);

		assertNull(this.plane.getIntersection(createSegment(-9, -2., -1, -5, -7, 0)));

		assertNull(this.plane.getIntersection(createSegment(-5, -7, 0, -9, -2., -1)));

		p = this.plane.getIntersection(createSegment(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertEpsilonEquals(createPoint(-2.599319657044237, -5.799659828522119, 1.2003401714778814), p);

		p = this.plane.getIntersection(createSegment(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		assertEpsilonEquals(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523), p);
	}

	@DisplayName("classifies(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createPoint(0, 0, 0)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createPoint(-8, -4, -4)));
		//
		this.plane.negate();
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createPoint(0, 0, 0)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createPoint(-8, -4, -4)));
	}

	@DisplayName("classifies(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(-8, -4, -4));
		//
		this.plane.negate();
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(-8, -4, -4));
	}

	@DisplayName("classifiesPlanePoint(double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticClassifiesPlanePoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePoint(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePoint(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePoint(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -8, -4, -4));
		//
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlanePoint(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 0, 0, 0));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlanePoint(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlanePoint(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -8, -4, -4));
	}

	@DisplayName("classifiesPlaneSegment(double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticClassifiesPlaneSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2., 2., -2.));
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2., 2., -2., 0., 0., 0.));

		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.));

		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., -5, -7, 0));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, 0., 0., 0.));

		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -9, -2., -1, -5, -7, 0));
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5, -7, 0, -9, -2., -1));
		
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSegment(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814));
	}

	@DisplayName("claculatesPlaneSphereDistance(double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlaneSphereDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertEpsilonEquals(2., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -1.6329931618554523, -0.8164965809277261, -0.8164965809277261, 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.45, -1.22, -1.22, 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -4.08, -2.04, -2.04, 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -4.8989794856, -2.4494897428, -2.4494897428, 2.));
		assertEpsilonEquals(-1., Plane3afp.calculatesPlaneSphereDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5.7154760665, -2.8577380332, -2.8577380332, 2.));
	}

	@DisplayName("classifiesPlaneSphere(double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticClassifiesPlaneSphere(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., 2.));
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -1.6329931618554523, -0.8164965809277261, -0.8164965809277261, 2.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -2.45, -1.22, -1.22, 2.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -4.08, -2.04, -2.04, 2.));
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -4.8989794856, -2.4494897428, -2.4494897428, 2.));
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneSphere(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5.7154760665, -2.8577380332, -2.8577380332, 2.));
	}

	@DisplayName("classifies(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(new Sphere3d(0., 0., 0., 2.)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Sphere3d(-2.45, -1.22, -1.22, 2.)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Sphere3d(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Sphere3d(-4.08, -2.04, -2.04, 2.)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(new Sphere3d(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
		//
		this.plane.negate();

		assertSame(PlaneClassification.BEHIND, this.plane.classifies(new Sphere3d(0., 0., 0., 2.)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Sphere3d(-2.45, -1.22, -1.22, 2.)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Sphere3d(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Sphere3d(-4.08, -2.04, -2.04, 2.)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(new Sphere3d(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
	}

	@DisplayName("classifies(double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(0., 0., 0., 2.));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(-2.45, -1.22, -1.22, 2.));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(-3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(-4.08, -2.04, -2.04, 2.));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(-5.7154760665, -2.8577380332, -2.8577380332, 2.));
		//
		this.plane.negate();

		assertSame(PlaneClassification.BEHIND, this.plane.classifies(0., 0., 0., 2.));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(-2.45, -1.22, -1.22, 2.));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(-3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(-4.08, -2.04, -2.04, 2.));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(-5.7154760665, -2.8577380332, -2.8577380332, 2.));
	}

	@DisplayName("calculatesPlaneAlignedBoxDistance(double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticCalculatesPlaneAlignedBoxDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(4., Plane3afp.calculatesPlaneAlignedBoxDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., 2., 2., 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneAlignedBoxDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5., -2., -1., -3., 0., 1.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneAlignedBoxDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -6., -3., -1., -4., -1., 1.));
		assertEpsilonEquals(-1.307227776, Plane3afp.calculatesPlaneAlignedBoxDistance(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -8., -3., -2., -6., -1., 0.));

		assertEpsilonEquals(-4., Plane3afp.calculatesPlaneAlignedBoxDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 0., 0., 0., 2., 2., 2.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneAlignedBoxDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -5., -2., -1., -3., 0., 1.));
		assertEpsilonEquals(0., Plane3afp.calculatesPlaneAlignedBoxDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -6., -3., -1., -4., -1., 1.));
		assertEpsilonEquals(1.307227776, Plane3afp.calculatesPlaneAlignedBoxDistance(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -8., -3., -2., -6., -1., 0.));
	}

	@DisplayName("classifiesPlaneAlignedBox(double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticClassifiesPlaneAlignedBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneAlignedBox(NORMAL_X, NORMAL_Y, NORMAL_Z, D, 0., 0., 0., 2., 2., 2.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneAlignedBox(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -5., -2., -1., -3., 0., 1.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneAlignedBox(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -6., -3., -1., -4., -1., 1.));
		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneAlignedBox(NORMAL_X, NORMAL_Y, NORMAL_Z, D, -8., -3., -2., -6., -1., 0.));

		assertSame(PlaneClassification.BEHIND, Plane3afp.classifiesPlaneAlignedBox(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, 0., 0., 0., 2., 2., 2.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneAlignedBox(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -5., -2., -1., -3., 0., 1.));
		assertSame(PlaneClassification.COINCIDENT, Plane3afp.classifiesPlaneAlignedBox(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -6., -3., -1., -4., -1., 1.));
		assertSame(PlaneClassification.IN_FRONT_OF, Plane3afp.classifiesPlaneAlignedBox(-NORMAL_X, -NORMAL_Y, -NORMAL_Z, -D, -8., -3., -2., -6., -1., 0.));
	}

	@DisplayName("classifies(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createBox(0., 0., 0., 2., 2., 2.)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createBox(-5., -2., -1., -3., 0., 1.)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createBox(-6., -3., -1., -4., -1., 1.)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createBox(-8., -3., -2., -6., -1., 0.)));
		//
		this.plane.negate();
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(createBox(0., 0., 0., 2., 2., 2.)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createBox(-5., -2., -1., -3., 0., 1.)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(createBox(-6., -3., -1., -4., -1., 1.)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(createBox(-8., -3., -2., -6., -1., 0.)));
	}

	@DisplayName("classifies(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(0., 0., 0., 2., 2., 2.));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(-5., -2., -1., -3., 0., 1.));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(-6., -3., -1., -4., -1., 1.));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(-8., -3., -2., -6., -1., 0.));
		//
		this.plane.negate();
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(0., 0., 0., 2., 2., 2.));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(-5., -2., -1., -3., 0., 1.));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(-6., -3., -1., -4., -1., 1.));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(-8., -3., -2., -6., -1., 0.));
	}

	@DisplayName("classifies(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void classifiesSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(new Segment3d(0., 0., 0., -2., 2., -2.)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(new Segment3d(-2., 2., -2., 0., 0., 0.)));

		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Segment3d(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Segment3d(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));

		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Segment3d(0., 0., 0., -5, -7, 0)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Segment3d(-5, -7, 0, 0., 0., 0.)));

		assertSame(PlaneClassification.BEHIND, this.plane.classifies(new Segment3d(-9, -2., -1, -5, -7, 0)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(new Segment3d(-5, -7, 0, -9, -2., -1)));
		
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Segment3d(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Segment3d(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));

		//
		this.plane.negate();

		assertSame(PlaneClassification.BEHIND, this.plane.classifies(new Segment3d(0., 0., 0., -2., 2., -2.)));
		assertSame(PlaneClassification.BEHIND, this.plane.classifies(new Segment3d(-2., 2., -2., 0., 0., 0.)));

		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Segment3d(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Segment3d(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));

		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Segment3d(0., 0., 0., -5, -7, 0)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Segment3d(-5, -7, 0, 0., 0., 0.)));

		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(new Segment3d(-9, -2., -1, -5, -7, 0)));
		assertSame(PlaneClassification.IN_FRONT_OF, this.plane.classifies(new Segment3d(-5, -7, 0, -9, -2., -1)));
		
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Segment3d(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertSame(PlaneClassification.COINCIDENT, this.plane.classifies(new Segment3d(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
	}

	@DisplayName("intersects(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.plane.intersects(createPoint(0, 0, 0)));
		assertTrue(this.plane.intersects(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertFalse(this.plane.intersects(createPoint(-8, -4, -4)));
		//
		this.plane.negate();
		assertFalse(this.plane.intersects(createPoint(0, 0, 0)));
		assertTrue(this.plane.intersects(createPoint(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertFalse(this.plane.intersects(createPoint(-8, -4, -4)));
	}

	@DisplayName("intersects(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.plane.intersects(0, 0, 0));
		assertTrue(this.plane.intersects(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertFalse(this.plane.intersects(-8, -4, -4));
		//
		this.plane.negate();
		assertFalse(this.plane.intersects(0, 0, 0));
		assertTrue(this.plane.intersects(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523));
		assertFalse(this.plane.intersects(-8, -4, -4));
	}

	@DisplayName("intersects(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.plane.intersects(new Sphere3d(0., 0., 0., 2.)));
		assertTrue(this.plane.intersects(new Sphere3d(-2.45, -1.22, -1.22, 2.)));
		assertTrue(this.plane.intersects(new Sphere3d(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		assertTrue(this.plane.intersects(new Sphere3d(-4.08, -2.04, -2.04, 2.)));
		assertFalse(this.plane.intersects(new Sphere3d(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
		//
		this.plane.negate();

		assertFalse(this.plane.intersects(new Sphere3d(0., 0., 0., 2.)));
		assertTrue(this.plane.intersects(new Sphere3d(-2.45, -1.22, -1.22, 2.)));
		assertTrue(this.plane.intersects(new Sphere3d(-3.27, -1.6329931618554523, -1.6329931618554523, 2.)));
		assertTrue(this.plane.intersects(new Sphere3d(-4.08, -2.04, -2.04, 2.)));
		assertFalse(this.plane.intersects(new Sphere3d(-5.7154760665, -2.8577380332, -2.8577380332, 2.)));
	}

	@DisplayName("intersects(double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		assertFalse(this.plane.intersects(0., 0., 0., 2.));
		assertTrue(this.plane.intersects(-2.45, -1.22, -1.22, 2.));
		assertTrue(this.plane.intersects(-3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		assertTrue(this.plane.intersects(-4.08, -2.04, -2.04, 2.));
		assertFalse(this.plane.intersects(-5.7154760665, -2.8577380332, -2.8577380332, 2.));
		//
		this.plane.negate();

		assertFalse(this.plane.intersects(0., 0., 0., 2.));
		assertTrue(this.plane.intersects(-2.45, -1.22, -1.22, 2.));
		assertTrue(this.plane.intersects(-3.27, -1.6329931618554523, -1.6329931618554523, 2.));
		assertTrue(this.plane.intersects(-4.08, -2.04, -2.04, 2.));
		assertFalse(this.plane.intersects(-5.7154760665, -2.8577380332, -2.8577380332, 2.));
	}

	@DisplayName("intersects(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.plane.intersects(createBox(0., 0., 0., 2., 2., 2.)));
		assertTrue(this.plane.intersects(createBox(-5., -2., -1., -3., 0., 1.)));
		assertTrue(this.plane.intersects(createBox(-6., -3., -1., -4., -1., 1.)));
		assertFalse(this.plane.intersects(createBox(-8., -3., -2., -6., -1., 0.)));
		//
		this.plane.negate();
		assertFalse(this.plane.intersects(createBox(0., 0., 0., 2., 2., 2.)));
		assertTrue(this.plane.intersects(createBox(-5., -2., -1., -3., 0., 1.)));
		assertTrue(this.plane.intersects(createBox(-6., -3., -1., -4., -1., 1.)));
		assertFalse(this.plane.intersects(createBox(-8., -3., -2., -6., -1., 0.)));
	}

	@DisplayName("intersects(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.plane.intersects(0., 0., 0., 2., 2., 2.));
		assertTrue(this.plane.intersects(-5., -2., -1., -3., 0., 1.));
		assertTrue(this.plane.intersects(-6., -3., -1., -4., -1., 1.));
		assertFalse(this.plane.intersects(-8., -3., -2., -6., -1., 0.));
		//
		this.plane.negate();
		assertFalse(this.plane.intersects(0., 0., 0., 2., 2., 2.));
		assertTrue(this.plane.intersects(-5., -2., -1., -3., 0., 1.));
		assertTrue(this.plane.intersects(-6., -3., -1., -4., -1., 1.));
		assertFalse(this.plane.intersects(-8., -3., -2., -6., -1., 0.));
	}

	@DisplayName("intersects(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertFalse(this.plane.intersects(new Segment3d(0., 0., 0., -2., 2., -2.)));
		assertFalse(this.plane.intersects(new Segment3d(-2., 2., -2., 0., 0., 0.)));
		assertTrue(this.plane.intersects(new Segment3d(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		assertTrue(this.plane.intersects(new Segment3d(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));
		assertTrue(this.plane.intersects(new Segment3d(0., 0., 0., -5, -7, 0)));
		assertTrue(this.plane.intersects(new Segment3d(-5, -7, 0, 0., 0., 0.)));
		assertFalse(this.plane.intersects(new Segment3d(-9, -2., -1, -5, -7, 0)));
		assertFalse(this.plane.intersects(new Segment3d(-5, -7, 0, -9, -2., -1)));
		assertTrue(this.plane.intersects(new Segment3d(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertTrue(this.plane.intersects(new Segment3d(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		
		//
		this.plane.negate();

		assertFalse(this.plane.intersects(new Segment3d(0., 0., 0., -2., 2., -2.)));
		assertFalse(this.plane.intersects(new Segment3d(-2., 2., -2., 0., 0., 0.)));
		assertTrue(this.plane.intersects(new Segment3d(0., 0., 0., -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
		assertTrue(this.plane.intersects(new Segment3d(-2.599319657044237, -5.799659828522119, 1.2003401714778814, 0., 0., 0.)));
		assertTrue(this.plane.intersects(new Segment3d(0., 0., 0., -5, -7, 0)));
		assertTrue(this.plane.intersects(new Segment3d(-5, -7, 0, 0., 0., 0.)));
		assertFalse(this.plane.intersects(new Segment3d(-9, -2., -1, -5, -7, 0)));
		assertFalse(this.plane.intersects(new Segment3d(-5, -7, 0, -9, -2., -1)));
		assertTrue(this.plane.intersects(new Segment3d(-2.599319657044237, -5.799659828522119, 1.2003401714778814, -3.2659863237109046, -1.6329931618554523, -1.6329931618554523)));
		assertTrue(this.plane.intersects(new Segment3d(-3.2659863237109046, -1.6329931618554523, -1.6329931618554523, -2.599319657044237, -5.799659828522119, 1.2003401714778814)));
	}

	@DisplayName("transform(Transform3D,null) with translation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D_null_translation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Transform3D transform = new Transform3D();
		transform.makeTranslationMatrix(6, -3, 1);
		
		this.plane.transform(transform, null);

		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(-0.0824829046, this.plane.getEquationComponentD());
	}

	@DisplayName("transform(Transform3D,null) with rotation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D_null_rotation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		
		this.plane.transform(transform, null);

		assertEpsilonEquals(0.9525793444, this.plane.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, this.plane.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, this.plane.getEquationComponentC());
		assertEpsilonEquals(2.4444444444, this.plane.getEquationComponentD());
	}

	@DisplayName("transform(Transform3D,null) with rotation+translation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D_null_rotationTranslation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		transform.setTranslation(6, -3, 1);
		
		this.plane.transform(transform, null);

		assertEpsilonEquals(0.9525793444, this.plane.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, this.plane.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, this.plane.getEquationComponentC());
		assertEpsilonEquals(-3.4071143855375916, this.plane.getEquationComponentD());
	}

	@DisplayName("transform(Transform3D,getPivot()) with translation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D_pivot_translation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Transform3D transform = new Transform3D();
		transform.makeTranslationMatrix(6, -3, 1);
		
		this.plane.transform(transform, this.plane.getPivot().clone());

		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(-0.0824829046, this.plane.getEquationComponentD());
	}

	@DisplayName("transform(Transform3D,getPivot()) with rotation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D_pivot_rotation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		
		this.plane.transform(transform, this.plane.getPivot().clone());

		assertEpsilonEquals(0.9525793444, this.plane.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, this.plane.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, this.plane.getEquationComponentC());
		assertEpsilonEquals(2.4444444444, this.plane.getEquationComponentD());
	}

	@DisplayName("transform(Transform3D,getPivot()) with rotation+translation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D_pivot_rotationTranslation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		transform.setTranslation(6, -3, 1);
		
		this.plane.transform(transform, this.plane.getPivot().clone());

		assertEpsilonEquals(0.9525793444, this.plane.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, this.plane.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, this.plane.getEquationComponentC());
		assertEpsilonEquals(-3.4071143855375916, this.plane.getEquationComponentD());
	}

	@DisplayName("transform(Transform3D,(0,0,0)) with translation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D_origin_translation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Transform3D transform = new Transform3D();
		transform.makeTranslationMatrix(6, -3, 1);
		
		this.plane.transform(transform, createPoint(0, 0, 0));

		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(-0.0824829046, this.plane.getEquationComponentD());
	}

	@DisplayName("transform(Transform3D,(0,0,0)) with rotation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D_origin_rotation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);

		this.plane.transform(transform, createPoint(0,0,0));

		assertEpsilonEquals(0.9525793444, this.plane.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, this.plane.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, this.plane.getEquationComponentC());
		assertEpsilonEquals(4, this.plane.getEquationComponentD());
	}

	@DisplayName("transform(Transform3D,(0,0,0)) with rotation+translation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D_origin_rotationTranslation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		transform.setTranslation(6, -3, 1);
		
		this.plane.transform(transform, createPoint(0, 0, 0));

		assertEpsilonEquals(0.9525793444, this.plane.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, this.plane.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, this.plane.getEquationComponentC());
		assertEpsilonEquals(-1.85155883, this.plane.getEquationComponentD());
	}

	@DisplayName("transform(Transform3D,(1,-2,3)) with translation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D_pts_translation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Transform3D transform = new Transform3D();
		transform.makeTranslationMatrix(6, -3, 1);
		
		this.plane.transform(transform, createPoint(1, -2, 3));

		assertEpsilonEquals(NORMAL_X, this.plane.getEquationComponentA());
		assertEpsilonEquals(NORMAL_Y, this.plane.getEquationComponentB());
		assertEpsilonEquals(NORMAL_Z, this.plane.getEquationComponentC());
		assertEpsilonEquals(-0.0824829046, this.plane.getEquationComponentD());
	}

	@DisplayName("transform(Transform3D,(1,-2,3)) with rotation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D_pts_rotation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		
		this.plane.transform(transform, createPoint(1,-2,3));

		assertEpsilonEquals(0.9525793444, this.plane.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, this.plane.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, this.plane.getEquationComponentC());
		assertEpsilonEquals(4.8164965809, this.plane.getEquationComponentD());
	}

	@DisplayName("transform(Transform3D,(1,-2,3)) with rotation+translation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D_pts_rotationTranslation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(-1, 1, -1, Math.PI / 3.);

		Transform3D transform = new Transform3D();
		transform.makeRotationMatrix(q);
		transform.setTranslation(6, -3, 1);
		
		this.plane.transform(transform, createPoint(1, -2, 3));

		assertEpsilonEquals(0.9525793444, this.plane.getEquationComponentA());
		assertEpsilonEquals(-0.1360827635, this.plane.getEquationComponentB());
		assertEpsilonEquals(-0.272165527, this.plane.getEquationComponentC());
		assertEpsilonEquals(-1.0350622491, this.plane.getEquationComponentD());
	}

}