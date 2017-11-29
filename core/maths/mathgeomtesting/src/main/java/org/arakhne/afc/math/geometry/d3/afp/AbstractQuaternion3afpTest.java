/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.afp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Quaternion.AxisAngle;
import org.arakhne.afc.math.geometry.d3.Quaternion.EulerAngles;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.matrix.Matrix3d;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public abstract class AbstractQuaternion3afpTest<Q extends Quaternion> extends AbstractMathTestCase {

	protected Q quat;
	
	@Before
	public void setUp() throws Exception {
		this.quat = createAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
	}

	/** Create the quaternion to test.
	 * @return the quaternion to test.
	 */
	protected abstract Q createQuaternion(double x, double y, double z, double w);

	/** Create the quaternion to test.
	 * @return the quaternion to test.
	 */
	protected abstract Q createAxisAngle(double x, double y, double z, double angle);

	/** Create the quaternion to test.
	 * @return the quaternion to test.
	 */
	protected abstract Q createEulerAngles(double attitude, double bank, double heading);

	@After
	public void tearDown() {
		this.quat = null;
	}

	@Test
	public void equalsQuaternion() {
		assertTrue(this.quat.equals(this.quat));
		assertTrue(this.quat.equals(createAxisAngle(1, 2, 3, MathConstants.DEMI_PI)));
		assertFalse(this.quat.equals(createAxisAngle(1, 2, 3, -MathConstants.DEMI_PI)));
		assertFalse(this.quat.equals(createQuaternion(1, 2, 3, 4)));
		assertFalse(this.quat.equals(createQuaternion(1, 2, 3, 5)));
		assertFalse(this.quat.equals(createQuaternion(2, 4, 6, 4)));
	}

	@Test
	public void epsilonEqualsQuaternionDouble() {
		assertTrue(this.quat.equals(this.quat));
		assertTrue(this.quat.epsilonEquals(createAxisAngle(1, 2, 3, MathConstants.DEMI_PI), EPSILON));
		assertFalse(this.quat.epsilonEquals(createAxisAngle(1, 2, 3, -MathConstants.DEMI_PI), EPSILON));
		assertFalse(this.quat.epsilonEquals(createQuaternion(1, 2, 3, 4), EPSILON));
		assertFalse(this.quat.epsilonEquals(createQuaternion(1, 2, 3, 5), EPSILON));
		assertFalse(this.quat.epsilonEquals(createQuaternion(2, 4, 6, 4), EPSILON));
	}

	@Test
	public void testClone() {
		Quaternion q;
		q = this.quat.clone();
		assertEpsilonEquals(this.quat, q);
		assertNotEpsilonEquals(createQuaternion(0, 0, 0, 0), q);
	}

	@Test
	public void getX() {
		assertEpsilonEquals(0.18898, this.quat.getX());
	}

	@Test
	public void ix() {
		assertEquals(0, this.quat.ix());
		this.quat.setX(23.45);
		assertEquals(23, this.quat.ix());
		this.quat.setX(27.74);
		assertEquals(28, this.quat.ix());
	}

	@Test
	public void setXInt() {
		this.quat.setX(23);
		assertEpsilonEquals(23.0, this.quat.getX());
		this.quat.setX(27);
		assertEpsilonEquals(27.0, this.quat.getX());
	}

	@Test
	public void setXDouble() {
		this.quat.setX(23.45);
		assertEpsilonEquals(23.45, this.quat.getX());
		this.quat.setX(27.74);
		assertEpsilonEquals(27.74, this.quat.getX());
	}

	@Test
	public void getY() {
		assertEpsilonEquals(0.37796, this.quat.getY());
	}

	@Test
	public void iy() {
		assertEquals(0, this.quat.iy());
		this.quat.setY(23.45);
		assertEquals(23, this.quat.iy());
		this.quat.setY(27.74);
		assertEquals(28, this.quat.iy());
	}

	@Test
	public void setYInt() {
		this.quat.setY(23);
		assertEpsilonEquals(23.0, this.quat.getY());
		this.quat.setY(27);
		assertEpsilonEquals(27.0, this.quat.getY());
	}

	@Test
	public void setYDouble() {
		this.quat.setY(23.45);
		assertEpsilonEquals(23.45, this.quat.getY());
		this.quat.setY(27.74);
		assertEpsilonEquals(27.74, this.quat.getY());
	}

	@Test
	public void getZ() {
		assertEpsilonEquals(0.56695, this.quat.getZ());
	}

	@Test
	public void iz() {
		assertEquals(1, this.quat.iz());
		this.quat.setZ(23.45);
		assertEquals(23, this.quat.iz());
		this.quat.setZ(27.74);
		assertEquals(28, this.quat.iz());
	}

	@Test
	public void setZInt() {
		this.quat.setZ(23);
		assertEpsilonEquals(23.0, this.quat.getZ());
		this.quat.setZ(27);
		assertEpsilonEquals(27.0, this.quat.getZ());
	}

	@Test
	public void setZDouble() {
		this.quat.setZ(23.45);
		assertEpsilonEquals(23.45, this.quat.getZ());
		this.quat.setZ(27.74);
		assertEpsilonEquals(27.74, this.quat.getZ());
	}

	@Test
	public void getW() {
		assertEpsilonEquals(0.70711, this.quat.getW());
	}

	@Test
	public void iw() {
		assertEquals(1, this.quat.iw());
		this.quat.setW(23.45);
		assertEquals(23, this.quat.iw());
		this.quat.setW(27.74);
		assertEquals(28, this.quat.iw());
	}

	@Test
	public void setWInt() {
		this.quat.setW(23);
		assertEpsilonEquals(23.0, this.quat.getW());
		this.quat.setW(27);
		assertEpsilonEquals(27.0, this.quat.getW());
	}

	@Test
	public void setWDouble() {
		this.quat.setW(23.45);
		assertEpsilonEquals(23.45, this.quat.getW());
		this.quat.setW(27.74);
		assertEpsilonEquals(27.74, this.quat.getW());
	}

	@Test
	public void setDoubleDoubleDoubleDouble() {
		this.quat.set(1, 2, 3, 4);
		assertEpsilonEquals(createQuaternion(1, 2, 3, 4), this.quat);
	}

	@Test
	public void setQuaternion() {
		this.quat.set(createQuaternion(1, 2, 3, 4));
		assertEpsilonEquals(createQuaternion(1, 2, 3, 4), this.quat);
	}

	@Test
	public void setAxisAngleVector3DDouble() {
		this.quat.setAxisAngle(new Vector3d(1, 2, 3), 4);
		assertEpsilonEquals(createQuaternion(0.24302, 0.48604, 0.72905, -0.41615), this.quat);
	}

	@Test
	public void setAxisAngleDoubleDoubleDoubleDouble() {
		this.quat.setAxisAngle(1, 2, 3, 4);
		assertEpsilonEquals(createQuaternion(0.24302, 0.48604, 0.72905, -0.41615), this.quat);
	}

	@Test
	public void setAxisAngleAxisAngle() {
		this.quat.setAxisAngle(new AxisAngle(1, 2, 3, 4));
		assertEpsilonEquals(createQuaternion(0.24302, 0.48604, 0.72905, -0.41615), this.quat);
	}

	@Test
	public void getAxis() {
		assertEpsilonEquals(new Vector3d(0.18898, 0.37796, 0.56695), this.quat.getAxis());
	}

	@Test
	public void getAngle() {
		assertEpsilonEquals(1.5708, this.quat.getAngle());
	}

	@Test
	public void getAxisAngle() {
		AxisAngle aa = this.quat.getAxisAngle();
		assertEpsilonEquals(0.18898, aa.getX());
		assertEpsilonEquals(0.37796, aa.getY());
		assertEpsilonEquals(0.56695, aa.getZ());
		assertEpsilonEquals(1.5708, aa.getAngle());
	}

	@Test
	public void newAxisAngle() {
		Quaternion q = createAxisAngle(1, 2, 3, 4);
		assertEpsilonEquals(createQuaternion(0.24302, 0.48604, 0.72905, -0.41615), q);
	}

	@Test
	public void setEulerAnglesDoubleDoubleDoubleCoordinateSystem3D() {
		this.quat.setEulerAngles(1, 2, 3, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(createQuaternion(-0.31062, 0.71829, -0.50151, -0.36887), this.quat);
		this.quat.setEulerAngles(1, 2, 3, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(createQuaternion(-0.37289, -0.60203, -0.60203, -0.36887), this.quat);
		this.quat.setEulerAngles(1, 2, 3, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(createQuaternion(0.31062, 0.71829, 0.50151, -0.36887), this.quat);
		this.quat.setEulerAngles(1, 2, 3, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(createQuaternion(0.31062, 0.50151, -0.71829, -0.36887), this.quat);
	}

	@Test
	public void setEulerAnglesDoubleDoubleDouble() {
		this.quat.setEulerAngles(1, 2, 3);
		assertEpsilonEquals(createQuaternion(0.31062, 0.71829, 0.50151, -0.36887), this.quat);
	}

	@Test
	public void setEulerAnglesEulerAngles() {
		this.quat.setEulerAngles(new EulerAngles(1, 2, 3, CoordinateSystem3D.getDefaultCoordinateSystem()));
		assertEpsilonEquals(createQuaternion(0.31062, 0.71829, 0.50151, -0.36887), this.quat);
	}

	@Test
	public void getEulerAngles() {
		EulerAngles ea = this.quat.getEulerAngles();
		assertEpsilonEquals(1.50867, ea.getAttitude());
		assertEpsilonEquals(-0.55902, ea.getBank());
		assertEpsilonEquals(0.97793, ea.getHeading());
		assertSame(CoordinateSystem3D.getDefaultCoordinateSystem(), ea.getCoordinateSystem());
	}

	@Test
	public void getEulerAnglesCoordinateSystem3D() {
		EulerAngles ea = this.quat.getEulerAngles(CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(-1.47188, ea.getAttitude());
		assertEpsilonEquals(1.0087, ea.getBank());
		assertEpsilonEquals(0.47611, ea.getHeading());
		assertSame(CoordinateSystem3D.XYZ_LEFT_HAND, ea.getCoordinateSystem());
		//
		ea = this.quat.getEulerAngles(CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(-1.32581, ea.getAttitude());
		assertEpsilonEquals(1.09491, ea.getBank());
		assertEpsilonEquals(0.24498, ea.getHeading());
		assertSame(CoordinateSystem3D.XZY_LEFT_HAND, ea.getCoordinateSystem());
		//
		ea = this.quat.getEulerAngles(CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(1.50867, ea.getAttitude());
		assertEpsilonEquals(-0.55902, ea.getBank());
		assertEpsilonEquals(0.97793, ea.getHeading());
		assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, ea.getCoordinateSystem());
		//
		ea = this.quat.getEulerAngles(CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(1.35134, ea.getAttitude());
		assertEpsilonEquals(1.2365, ea.getBank());
		assertEpsilonEquals(-0.51397, ea.getHeading());
		assertSame(CoordinateSystem3D.XZY_RIGHT_HAND, ea.getCoordinateSystem());
	}

	@Test
	public void newEulerAngles() {
		Quaternion q = createEulerAngles(1, 2, 3);
		assertEpsilonEquals(createQuaternion(0.31062, 0.71829, 0.50151, -0.36887), q);
	}

	@Test
	public void setFromMatrixDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		Transform3D mat = new Transform3D();
		mat.makeRotationMatrix(MathConstants.DEMI_PI);
		this.quat.setFromMatrix(mat.getM00(), mat.getM01(), mat.getM02(),
				mat.getM10(), mat.getM11(), mat.getM12(),
				mat.getM20(), mat.getM21(), mat.getM22());
		assertEpsilonEquals(createQuaternion(0, 0, 0.70711, 0.70711), this.quat);
	}

	@Test
	public void setFromMatrixMatrix4d() {
		Transform3D mat = new Transform3D();
		mat.makeRotationMatrix(MathConstants.DEMI_PI);
		this.quat.setFromMatrix(mat);
		assertEpsilonEquals(createQuaternion(0, 0, 0.70711, 0.70711), this.quat);
	}

	@Test
	public void setFromMatrixMatrix3d() {
		Transform3D mat = new Transform3D();
		mat.makeRotationMatrix(MathConstants.DEMI_PI);
		Matrix3d mat2 = new Matrix3d(
				mat.getM00(), mat.getM01(), mat.getM02(),
				mat.getM10(), mat.getM11(), mat.getM12(),
				mat.getM20(), mat.getM21(), mat.getM22());
		this.quat.setFromMatrix(mat2);
		assertEpsilonEquals(createQuaternion(0, 0, 0.70711, 0.70711), this.quat);
	}

	@Test
	public void norm() {
		assertEpsilonEquals(1, this.quat.norm());
		this.quat.set(1, 2, 3, 4);
		assertEpsilonEquals(30, this.quat.norm());
	}

	@Test
	public void conjugateQuaternion() {
		this.quat.conjugate(this.quat);
		assertEpsilonEquals(createQuaternion(-0.18898, -0.37796, -0.56695, 0.70711), this.quat);
		this.quat.conjugate(createQuaternion(1, 2, 3, 4));
		assertEpsilonEquals(createQuaternion(-1, -2, -3, 4), this.quat);
	}

	@Test
	public void conjugate() {
		this.quat.conjugate();
		assertEpsilonEquals(createQuaternion(-0.18898, -0.37796, -0.56695, 0.70711), this.quat);
		this.quat.conjugate();
		assertEpsilonEquals(createQuaternion(0.18898, 0.37796, 0.56695, 0.70711), this.quat);
	}

	@Test
	public void inverseQuaternion() {
		this.quat.inverse(this.quat);
		assertEpsilonEquals(createQuaternion(-0.18898, -0.37796, -0.56695, 0.70711), this.quat);
		this.quat.inverse(createQuaternion(1, 2, 3, 4));
		assertEpsilonEquals(createQuaternion(-0.03333, -0.06667, -0.1, 0.13333), this.quat);
	}

	@Test
	public void inverse() {
		this.quat.inverse();
		assertEpsilonEquals(createQuaternion(-0.18898, -0.37796, -0.56695, 0.70711), this.quat);
		this.quat.inverse();
		assertEpsilonEquals(createQuaternion(0.18898, 0.37796, 0.56695, 0.70711), this.quat);
	}

	@Test
	public void normalizeQuaternion() {
		this.quat.normalize(this.quat);
		assertEpsilonEquals(createQuaternion(0.18898, 0.37796, 0.56695, 0.70711), this.quat);
		this.quat.normalize(createQuaternion(1, 2, 3, 4));
		assertEpsilonEquals(createQuaternion(0.18257, 0.36515, 0.54772, 0.7303), this.quat);
	}

	@Test
	public void normalize() {
		this.quat.normalize(this.quat);
		assertEpsilonEquals(createQuaternion(0.18898, 0.37796, 0.56695, 0.70711), this.quat);
		this.quat.set(1, 2, 3, 4);
		this.quat.normalize();
		assertEpsilonEquals(createQuaternion(0.18257, 0.36515, 0.54772, 0.7303), this.quat);
	}

	@Test
	public void mulQuaternionQuaternion() {
		Quaternion q1 = createQuaternion(0, 0, 0, 0);
		Quaternion q2 = createQuaternion(0, 0, 0, 0);
		this.quat.mul(q1, q2);
		assertEpsilonEquals(createQuaternion(0, 0, 0, 0), this.quat);
		q1 = createQuaternion(1, 2, 3, 4);
		q2 = createQuaternion(5, 6, 7, 8);
		this.quat.mul(q1, q2);
		assertEpsilonEquals(createQuaternion(24, 48, 48, -6), this.quat);
	}

	@Test
	public void mulQuaternion() {
		Quaternion q = createQuaternion(5, 6, 7, 8);
		this.quat.mul(q);
		assertEpsilonEquals(createQuaternion(4.29141, 8.77823, 8.72945, -1.52443), this.quat);
	}

	@Test
	public void mulInverseQuaternionQuaternion() {
		Quaternion q1 = createQuaternion(0, 0, 0, 0);
		Quaternion q2 = createQuaternion(0, 0, 0, 0);
		this.quat.mulInverse(q1, q2);
		assertEpsilonEquals(createQuaternion(0, 0, 0, 0), this.quat);
		q1 = createQuaternion(1, 2, 3, 4);
		q2 = createQuaternion(5, 6, 7, 8);
		this.quat.mulInverse(q1, q2);
		assertEpsilonEquals(createQuaternion(0, 0, 0, 0), this.quat);
	}

	@Test
	public void mulInverseQuaternion() {
		Quaternion q = createQuaternion(5, 6, 7, 8);
		this.quat.mulInverse(q);
		assertEpsilonEquals(createQuaternion(-1.26775, -2.73078, 0.34176, 12.83818), this.quat);
	}

	@Test
	public void interpolateQuaternionQuaternionDouble() {
		Quaternion q1 = createQuaternion(1, 2, 3, 4);
		Quaternion q2 = createQuaternion(5, 6, 7, 8);
		this.quat.interpolate(q1, q2, 0.6);
		assertEpsilonEquals(createQuaternion(0.30263, 0.4221, 0.54156, 0.66103), this.quat);
	}

	@Test
	public void interpolateQuaternionDouble() {
		Quaternion q = createQuaternion(5, 6, 7, 8);
		this.quat.interpolate(q, 0.6);
		assertEpsilonEquals(createQuaternion(0.30490, 0.42682, 0.54873, 0.65097), this.quat);
	}

}
