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
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.Plane3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneXY3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneXZ3afp;
import org.arakhne.afc.math.geometry.d3.afp.PlaneYZ3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.d3.tests.AbstractMathTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

	public abstract void absolute(CoordinateSystem3D cs);
	
	public abstract void clear(CoordinateSystem3D cs);

	public abstract void getEquationComponentA(CoordinateSystem3D cs);

	public abstract void getEquationComponentB(CoordinateSystem3D cs);

	public abstract void getEquationComponentC(CoordinateSystem3D cs);

	public abstract void getEquationComponentD(CoordinateSystem3D cs);

	public abstract void getNormal(CoordinateSystem3D cs);
	
	public abstract void getNormalX(CoordinateSystem3D cs);

	public abstract void getNormalY(CoordinateSystem3D cs);

	public abstract void getNormalZ(CoordinateSystem3D cs);

	public abstract void getPivot(CoordinateSystem3D cs);

	public abstract void translateDouble(CoordinateSystem3D cs);

	public abstract void translateDoubleDoubleDouble(CoordinateSystem3D cs);

	public abstract void translateVector3D(CoordinateSystem3D cs);

	public abstract void negate(CoordinateSystem3D cs);

	public abstract void normalize(CoordinateSystem3D cs);

	public abstract void getProjectionDoubleDoubleDouble(CoordinateSystem3D cs);

	public abstract void getProjectionPoint3D(CoordinateSystem3D cs);

	public abstract void setPivotDoubleDoubleDouble(CoordinateSystem3D cs);

	public abstract void setPivotPoint3D(CoordinateSystem3D cs);

	public abstract void getDistanceToPlane3D_generalPlane(CoordinateSystem3D cs);
	
	public abstract void getDistanceToPlane3D_xy(CoordinateSystem3D cs);

	public abstract void getDistanceToPlane3D_xz(CoordinateSystem3D cs);

	public abstract void getDistanceToPlane3D_yz(CoordinateSystem3D cs);

	public abstract void getIntersectionDoubleDoubleDoubleDouble_generalPlane(CoordinateSystem3D cs);
	
	public abstract void getIntersectionDoubleDoubleDoubleDouble_xy(CoordinateSystem3D cs);

	public abstract void getIntersectionDoubleDoubleDoubleDouble_xz(CoordinateSystem3D cs);

	public abstract void getIntersectionDoubleDoubleDoubleDouble_yz(CoordinateSystem3D cs);

	public abstract void getIntersectionPlane3D_generalPlane(CoordinateSystem3D cs);
	
	public abstract void getIntersectionPlane3D_xy(CoordinateSystem3D cs);

	public abstract void getIntersectionPlane3D_xz(CoordinateSystem3D cs);

	public abstract void getIntersectionPlane3D_yz(CoordinateSystem3D cs);

	public abstract void operator_addDouble(CoordinateSystem3D cs);
	
	public abstract void operator_addVector3D(CoordinateSystem3D cs);
	
	public abstract void operator_andPlane3D(CoordinateSystem3D cs);
	
	public abstract void operator_andPoint3D(CoordinateSystem3D cs);
	
	public abstract void operator_equalsPlane3D(CoordinateSystem3D cs);
	
	public abstract void operator_notEqualsPlane3D(CoordinateSystem3D cs);
	
	public abstract void operator_minus(CoordinateSystem3D cs);
	
	public abstract void operator_minusDouble(CoordinateSystem3D cs);
	
	public abstract void operator_minusVector3D(CoordinateSystem3D cs);
	
	public abstract void operator_multiplyQuaternion(CoordinateSystem3D cs);
	
	public abstract void operator_multiplyTransform3D(CoordinateSystem3D cs);
	
	public abstract void operator_plusDouble(CoordinateSystem3D cs);
	
	public abstract void operator_plusVector3D(CoordinateSystem3D cs);
	
	public abstract void operator_removeDouble(CoordinateSystem3D cs);
	
	public abstract void operator_removeVector3D(CoordinateSystem3D cs);
	
	public abstract void operator_upToPlane3D(CoordinateSystem3D cs);
	
	public abstract void operator_upToPoint3D(CoordinateSystem3D cs);
	
	public abstract void rotateDoubleDoubleDoubleDouble(CoordinateSystem3D cs);
	
	public abstract void rotateQuaternion(CoordinateSystem3D cs);
	
	public abstract void rotateQuaternionPoint3D(CoordinateSystem3D cs);
	
	public abstract void rotateVector3DDouble(CoordinateSystem3D cs);
	
	public abstract void rotateVector3DDoublePoint3D(CoordinateSystem3D cs);
	
	public abstract void setDoubleDoubleDoubleDouble(CoordinateSystem3D cs);
	
	public abstract void setDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs);
	
	public abstract void setPlane3D_generalPlane(CoordinateSystem3D cs);
	
	public abstract void setPlane3D_xy(CoordinateSystem3D cs);

	public abstract void setPlane3D_xz(CoordinateSystem3D cs);

	public abstract void setPlane3D_yz(CoordinateSystem3D cs);

	public abstract void setPoint3DPoint3DPoint3D(CoordinateSystem3D cs);
	
	public abstract void setPoint3DVector3D(CoordinateSystem3D cs);
	
	public abstract void setPoint3DVector3DVector3D(CoordinateSystem3D cs);
	
	public abstract void transformTransform3D(CoordinateSystem3D cs);
	
	public abstract void transformTransform3DPoint3D(CoordinateSystem3D cs);
	
	public abstract void calculatesPlaneAlignedBoxDistance(CoordinateSystem3D cs);
	
	public abstract void calculatesPlanePointDistance(CoordinateSystem3D cs);
	
	public abstract void calculatesPlaneSphereDistance(CoordinateSystem3D cs);

	public abstract void calculatesPlanePlaneDistance(CoordinateSystem3D cs);

	public abstract void classifiesPoint3D(CoordinateSystem3D cs);

	public abstract void classifiesPlaneAlignedBox(CoordinateSystem3D cs);
	
	public abstract void classifiesPlanePlane(CoordinateSystem3D cs);
	
	public abstract void classifiesPlanePoint(CoordinateSystem3D cs);
	
	public abstract void classifiesPlaneSegment(CoordinateSystem3D cs);
	
	public abstract void classifiesPlaneSphere(CoordinateSystem3D cs);
	
	public abstract void findsPlanePlaneIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoublePoint3DPoint3D(CoordinateSystem3D cs);
	
	public abstract void findsPlanePlaneIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoublePoint3DVector3D(CoordinateSystem3D cs);

	public abstract void findsPlanePlaneIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoublePointVector3DReceiver(CoordinateSystem3D cs);
	
	public abstract void findsPlanePlaneIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleSegment3afp(CoordinateSystem3D cs);
	
	public abstract void findsPlanePointProjection(CoordinateSystem3D cs);
	
	public abstract void findsPlanePointProjectionWithPlaneNormal(CoordinateSystem3D cs);
	
	public abstract void findsPlaneSegmentIntersection(CoordinateSystem3D cs);
	
	public abstract void calculatesPlaneSegmentIntersectionFactor(CoordinateSystem3D cs);
	
	public abstract void classifiesBox3afp(CoordinateSystem3D cs);
	
	public abstract void classifiesDoubleDoubleDouble(CoordinateSystem3D cs);
	
	public abstract void classifiesDoubleDoubleDoubleDouble(CoordinateSystem3D cs);
	
	public abstract void classifiesDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs);

	public abstract void classifiesPlane3D(CoordinateSystem3D cs);
	
	public abstract void classifiesSegment3afp(CoordinateSystem3D cs);
	
	public abstract void classifiesSphere3afp(CoordinateSystem3D cs);

	public abstract void getDistanceToDoubleDoubleDoubleDouble_generalPlane(CoordinateSystem3D cs);

	public abstract void getDistanceToDoubleDoubleDoubleDouble_xy(CoordinateSystem3D cs);

	public abstract void getDistanceToDoubleDoubleDoubleDouble_yz(CoordinateSystem3D cs);

	public abstract void getDistanceToDoubleDoubleDoubleDouble_xz(CoordinateSystem3D cs);

	public abstract void getDistanceToPoint3D(CoordinateSystem3D cs);
	
	public abstract void getDistanceToDoubleDoubleDouble(CoordinateSystem3D cs);
	
	public abstract void getIntersectionSegment3afp(CoordinateSystem3D cs);

	public abstract void intersectsBox3afp(CoordinateSystem3D cs);

	public abstract void intersectsDoubleDoubleDouble(CoordinateSystem3D cs);

	public abstract void intersectsPoint3D(CoordinateSystem3D cs);

	public abstract void intersectsDoubleDoubleDoubleDouble(CoordinateSystem3D cs);

	public abstract void intersectsPlane3D(CoordinateSystem3D cs);

	public abstract void intersectsDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs);

	public abstract void intersectsSegment3afp(CoordinateSystem3D cs);

	public abstract void intersectsSphere3afp(CoordinateSystem3D cs);

	public abstract void operator_andBox3afp(CoordinateSystem3D cs);

	public abstract void operator_andSegment3afp(CoordinateSystem3D cs);

	public abstract void operator_andSphere3afp(CoordinateSystem3D cs);

	public abstract void equalsPlane3D(CoordinateSystem3D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("clone")
	public final void testClone(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var cl = getP().clone();
		assertNotSame(getP(), cl);
		assertEpsilonEquals(getP().getEquationComponentA(), cl.getEquationComponentA());
		assertEpsilonEquals(getP().getEquationComponentB(), cl.getEquationComponentB());
		assertEpsilonEquals(getP().getEquationComponentC(), cl.getEquationComponentC());
		assertEpsilonEquals(getP().getEquationComponentD(), cl.getEquationComponentD());
	}

}
