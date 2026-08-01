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

import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.Plane3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneXY3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneXZ3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneYZ3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractPlane3DTestCase<T extends Plane3afp<T, ?, ?, ?, ?>, B extends AlignedBox3afp<?, ?, ?, ?, ?, B>> extends AbstractMathTestCase {

	protected abstract TestShapeFactory3d<? extends Point3D, ? extends Vector3D, ? extends Quaternion, B> createFactory();

	protected abstract T getP();

	/** Shape factory.
	 */
	protected TestShapeFactory3d<? extends Point3D, ? extends Vector3D, ? extends Quaternion, B> factory;

	@BeforeEach
	public void setUp() {
		this.factory = createFactory();
	}

	public final Segment3afp<?, ?, ?, ?, ?, ?, B> createSegment(double x1, double y1, double z1, double x2, double y2, double z2) {
		return this.factory.createSegment(x1, y1, z1, x2, y2, z2);
	}
	
	public final B createAlignedBox(double x, double y, double z, double width, double height, double depth) {
		return this.factory.createAlignedBox(x, y, z, width, height, depth);
	}

	public final B createAlignedBoxFromPoints(double x1, double y1, double z1, double x2, double y2, double z2) {
		return this.factory.createAlignedBox(x1, y1, z1, x2 - x1, y2 - y1, z2 - z1);
	}

	public final Sphere3afp<?, ?, ?, ?, ?, B> createSphere(double x, double y, double z, double radius) {
		return this.factory.createSphere(x, y, z, radius);
	}
	
	public final MultiShape3afp<?, ?, ?, ?, ?, ?, B> createMultiShape() {
		return this.factory.createMultiShape();
	}

	public static Point3D createTmpPoint(double x, double y, double z) {
		return new InnerComputationPoint3D(x, y, z);
	}

	public final Point3D createPoint(double x, double y, double z) {
		return this.factory.createPoint(x, y, z);
	}

	public final Quaternion createQuaternion(double x, double y, double z, double w) {
		return this.factory.createQuaternion(x, y, z, w);
	}

	public final Quaternion createAxisAngle(double x, double y, double z, double angle) {
		return this.factory.createAxisAngle(x, y, z, angle);
	}

	public final Vector3D createVector(double x, double y, double z) {
		return this.factory.createVector(x, y, z);
	}

	public final Path3afp<?, ?, ?, ?, ?, B> createPath() {
		return this.factory.createPath();
	}

	public final Transform3D createTransform() {
		return this.factory.createTransform();
	}

	public final Plane3afp createPlane(double a, double b, double c, double d) {
		return this.factory.createPlane(a, b, c, d);
	}

	public final PlaneXY3afp createPlaneXY(double z, boolean positive) {
		return this.factory.createPlaneXY(positive, z);
	}

	public final PlaneXZ3afp createPlaneXZ(double y, boolean positive) {
		return this.factory.createPlaneXZ(positive, y);
	}

	public final PlaneYZ3afp createPlaneYZ(double x, boolean positive) {
		return this.factory.createPlaneYZ(positive, x);
	}

	public final Path3afp<?, ?, ?, ?, ?, B> createPolyline(double... coordinates) {
		Path3afp<?, ?, ?, ?, ?, B>  path = createPath();
		path.moveTo(coordinates[0], coordinates[1], coordinates[2]);
		for (int i = 3; i < coordinates.length; i += 3) {
			path.lineTo(coordinates[i], coordinates[i + 1], coordinates[i + 2]);
		}
		return path;
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.factory = null;
	}
	
	@DisplayName("clone")
	@Nested
	public class CloneTest {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var cl = getP().clone();
			assertNotSame(getP(), cl);
			assertEpsilonEquals(getP().getEquationComponentA(), cl.getEquationComponentA());
			assertEpsilonEquals(getP().getEquationComponentB(), cl.getEquationComponentB());
			assertEpsilonEquals(getP().getEquationComponentC(), cl.getEquationComponentC());
			assertEpsilonEquals(getP().getEquationComponentD(), cl.getEquationComponentD());
		}
	}

}
