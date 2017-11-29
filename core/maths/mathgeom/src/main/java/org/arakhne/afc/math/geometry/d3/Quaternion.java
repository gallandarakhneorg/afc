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

package org.arakhne.afc.math.geometry.d3;

import java.io.Serializable;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.matrix.Matrix3d;
import org.arakhne.afc.math.matrix.Matrix4d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.arakhne.afc.vmutil.json.JsonableObject;

/** A 4 element unit quaternion represented by x, y, z, w coordinates.
 * The quaternion is always normalized.
 *
 * <h3>Other Rotation Representations</h3>
 *
 * <p>Other representations of an rotation are available from this class:
 * axis-angle, and Euler angles.
 *
 * <h4>Axis Angles</h4>
 * The axis–angle representation of a rotation parameterizes a rotation in a three-dimensional
 * Euclidean space by two values: a unit vector, indicating the direction of an axis of rotation, and
 * an angle describing the magnitude of the rotation about the axis.
 * The rotation occurs in the sense prescribed by the (left/right)-hand rule.
 * <img src="doc-files/axis_angle.png" alt="[Axis-Angle Representation]">
 *
 * <h4>Euler Angles</h4>
 * The term "Euler Angle" is used for any representation of 3 dimensional
 * rotations where the rotation is decomposed into 3 separate angles.
 *
 * <p>There is no single set of conventions and standards in this area,
 * therefore the following conventions was choosen:<ul>
 * <li>angle applied first:	heading;</li>
 * <li>angle applied second: attitude;</li>
 * <li>angle applied last: bank</li>
 * </ul>
 *
 * <p>Examples: NASA aircraft standard and telescope standard
 * <img src="doc-files/euler_plane.gif" alt="[NASA Aircraft Standard]">
 * <img src="doc-files/euler_telescop.gif" alt="[Telescope Standard]">
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Quaternion extends Cloneable, Serializable, JsonableObject {

	/**
	 * Returns true if all of the data members of Quaternion quat are
	 * equal to the corresponding data members in this Quaternion.
	 * @param quat the quaternnion with which the comparison is made
	 * @return {@code true} if equal.
	 */
	@Pure
	default boolean equals(Quaternion quat) {
		try {
			return getX() == quat.getX() && getY() == quat.getY() && getZ() == quat.getZ() && getW() == quat.getW();
		} catch (Throwable exception) {
			return false;
		}
	}

	/** Clone this quaternion.
	 *
	 * @return the clone.
	 */
	Quaternion clone();

	/** Replies the X coordinate.
	 *
	 * @return x
	 */
	@Pure
	double getX();

	/** Replies the X coordinate.
	 *
	 * @return x
	 */
	@Pure
	int ix();

	/** Set the X coordinate.
	 *
	 * @param x x coordinate.
	 */
	void setX(int x);

	/** Set the X coordinate.
	 *
	 * @param x x coordinate.
	 */
	void setX(double x);

	/** Replies the Y coordinate.
	 *
	 * @return y
	 */
	@Pure
	double getY();

	/** Replies the Y coordinate.
	 *
	 * @return y
	 */
	@Pure
	int iy();

	/** Set the Y coordinate.
	 *
	 * @param y y coordinate.
	 */
	void setY(int y);

	/** Set the Y coordinate.
	 *
	 * @param y y coordinate.
	 */
	void setY(double y);

	/** Replies the Z coordinate.
	 *
	 * @return z
	 */
	@Pure
	double getZ();

	/** Replies the Z coordinate.
	 *
	 * @return z
	 */
	@Pure
	int iz();

	/** Set the Z coordinate.
	 *
	 * @param z z coordinate.
	 */
	void setZ(int z);

	/** Set the Z coordinate.
	 *
	 * @param z z coordinate.
	 */
	void setZ(double z);

	/** Replies the W coordinate.
	 *
	 * @return w
	 */
	@Pure
	double getW();

	/** Replies the W coordinate.
	 *
	 * @return w
	 */
	@Pure
	int iw();

	/** Set the W coordinate.
	 *
	 * @param w w coordinate.
	 */
	void setW(int w);

	/** Set the W coordinate.
	 *
	 * @param w w coordinate.
	 */
	void setW(double w);

	/**
	 * Returns true if the L-infinite distance between this tuple
	 * and tuple t1 is less than or equal to the epsilon parameter,
	 * otherwise returns false.  The L-infinite
	 * distance is equal to MAX[abs(x1-x2), abs(y1-y2)].
	 * @param quat  the tuple to be compared to this tuple
	 * @param epsilon  the threshold value
	 * @return  true or false
	 */
	@Pure
	default boolean epsilonEquals(Quaternion quat, double epsilon) {
		return (Math.abs(quat.getX() - getX()) <= epsilon)
			    && (Math.abs(quat.getY() - getY()) <= epsilon)
			    && (Math.abs(quat.getZ() - getZ()) <= epsilon)
			    && (Math.abs(quat.getW() - getW()) <= epsilon);
	}

	/** Replies the norm of the quaternion.
	 *
	 * <p>The norm is {@code x*x + y*y + z*z + w*w}.
	 *
	 * @return the norm.
	 */
	default double norm() {
		final double x = getX();
		final double y = getY();
		final double z = getZ();
		final double w = getW();
		return x * x + y * y + z * z + w * w;
	}

	/**
	 * Sets the value of this quaternion to the conjugate of quaternion q1, i.e. the negation of quat, except w.
	 *
	 * <p>The result is: {@code this = (-quat.x, -quat.y, -quat.z, quat.w)}.
	 *
	 * @param quat the source vector
	 * @see #inverse(Quaternion)
	 */
	default void conjugate(Quaternion quat) {
		setX(-quat.getX());
		setY(-quat.getY());
		setZ(-quat.getZ());
		setW(quat.getW());
	}

	/**
	 * Sets the value of this quaternion to the conjugate of itself, i.e. the negation of itself, except w.
	 *
	 * <p>The result is: {@code this = (-this.x, -this.y, -this.z, this.w)}.
	 *
	 * @see #inverse()
	 */
	default void conjugate() {
		conjugate(this);
	}

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * quaternions q1 and q2.
	 *
	 * <p>{@code this = quat1 * quat2}.
	 *
	 * <p>Note that this is safe for aliasing (e.g. this can be q1 or q2).
	 *
	 * @param quat1 the first quaternion
	 * @param quat2 the second quaternion
	 */
	default void mul(Quaternion quat1, Quaternion quat2) {
		final double x1 = quat1.getX();
		final double y1 = quat1.getY();
		final double z1 = quat1.getZ();
		final double w1 = quat1.getW();
		final double x2 = quat2.getX();
		final double y2 = quat2.getY();
		final double z2 = quat2.getZ();
		final double w2 = quat2.getW();
		set(
				x1 * w2 + w1 * x2 + y1 * z2 - z1 * y2,
				y1 * w2 + w1 * y2 + z1 * x2 - x1 * z2,
				z1 * w2 + w1 * z2 + x1 * y2 - y1 * x2,
				w1 * w2 - x1 * x2 - y1 * y2 - z1 * z2);
	}

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * itself and q1.
	 *
	 * <p>{@code this = this * quat}.
	 *
	 * @param quat the other quaternion
	 */
	default void mul(Quaternion quat) {
		mul(this, quat);
	}

	/**
	 * Multiplies quaternion q1 by the inverse of quaternion q2 and places
	 * the value into this quaternion.  The value of both argument quaternions
	 * is preservered.
	 *
	 * <p>{@code this = quat1 * (quat2)^(-1)}.
	 *
	 * @param quat1 the first quaternion
	 * @param quat2 the second quaternion
	 */
	default void mulInverse(Quaternion quat1, Quaternion quat2) {
		final double x1 = quat1.getX();
		final double y1 = quat1.getY();
		final double z1 = quat1.getZ();
		final double w1 = quat1.getW();
		final double x2 = quat2.getX();
		final double y2 = quat2.getY();
		final double z2 = quat2.getZ();
		final double w2 = quat2.getW();
		double norm = norm();
		// zero-div may occur.
		norm = norm == 0. ? norm : 1. / norm;
		// store on stack once for aliasing-safty
		set(
				(x1 * w2 - w1 * x2 - y1 * z2 + z1 * y2) * norm,
				(y1 * w2 - w1 * y2 - z1 * x2 + x1 * z2) * norm,
				(z1 * w2 - w1 * z2 - x1 * y2 + y1 * x2) * norm,
				(w1 * w2 + x1 * x2 + y1 * y2 + z1 * z2) * norm);
	}

	/**
	 * Multiplies this quaternion by the inverse of quaternion q1 and places
	 * the value into this quaternion.  The value of the argument quaternion
	 * is preserved.
	 *
	 * <p>{@code this = this * (quat)^(-1)}.
	 *
	 * @param quat the other quaternion
	 */
	default void mulInverse(Quaternion quat) {
		mulInverse(this, quat);
	}

	/**
	 * Sets the value of this quaternion to quaternion inverse of quaternion quat.
	 *
	 * <p>{@code this = (-quat.x, -quat.y, -quat.z, quat.w) / quat.norm()}.
	 *
	 * @param quat the quaternion to be inverted
	 * @see #conjugate(Quaternion)
	 */
	default void inverse(Quaternion quat) {
		final double n = quat.norm();
		// zero-div may occur.
		set(
				-quat.getX() / n,
				-quat.getY() / n,
				-quat.getZ() / n,
				quat.getW() / n);
	}

	/**
	 * Sets the value of this quaternion to the quaternion inverse of itself.
	 *
	 * <p>{@code this = (-this.x, -this.y, -this.z, this.w) / this.norm()}.
	 *
	 * @see #conjugate()
	 */
	default void inverse() {
		inverse(this);
	}

	/**
	 * Sets the value of this quaternion to the normalized value
	 * of quaternion quat.
	 *
	 * <p>{@code this = (quat.x, quat.y, quat.z, quat.w) / sqrt(quat.norm()}.
	 *
	 * @param quat the quaternion to be normalized.
	 */
	default void normalize(Quaternion quat) {
		final double n = Math.sqrt(quat.norm());
		// zero-div may occur.
		set(
				quat.getX() / n,
				quat.getY() / n,
				quat.getZ() / n,
				quat.getW() / n);
	}

	/**
	 * Normalizes the value of this quaternion in place.
	 *
	 * <p>{@code this = (this.x, this.y, this.z, this.w) / sqrt(this.norm()}.
	 */
	default void normalize() {
		normalize(this);
	}

	/** Sets the value of this quaternion to the rotational component of
	 * the passed matrix.
	 *
	 * @param m00
	 *           the [0][0] element
	 * @param m01
	 *           the [0][1] element
	 * @param m02
	 *           the [0][2] element
	 * @param m10
	 *           the [1][0] element
	 * @param m11
	 *           the [1][1] element
	 * @param m12
	 *           the [1][2] element
	 * @param m20
	 *           the [2][0] element
	 * @param m21
	 *           the [2][1] element
	 * @param m22
	 *           the [2][2] element
	 */
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
	default void setFromMatrix(double m00, double m01, double m02,
			double m10, double m11, double m12,
			double m20, double m21, double m22) {
		// From Ken Shoemake
		// (ftp://ftp.cis.upenn.edu/pub/graphics/shoemake)
		final double x;
		final double y;
		final double z;
		final double w;
		final double tr = m00 + m11 + m22;
		if (tr >= 0.) {
			double scale = Math.sqrt(tr + 1.);
			w = scale * .5;
			scale = .5 / scale;
			x = (m21 - m12) * scale;
			y = (m02 - m20) * scale;
			z = (m10 - m01) * scale;
		} else {
			final double max = Math.max(Math.max(m00, m11), m22);
			if (max == m00) {
				double scale = Math.sqrt(m00 - (m11 + m22) + 1.);
				x = scale * .5;
				scale = .5 / scale;
				y = (m01 + m10) * scale;
				z = (m20 + m02) * scale;
				w = (m21 - m12) * scale;
			} else if (max == m11) {
				double scale = Math.sqrt(m11 - (m22 + m00) + 1.);
				y = scale * .5;
				scale = .5 / scale;
				z = (m12 + m21) * scale;
				x = (m01 + m10) * scale;
				w = (m02 - m20) * scale;
			} else {
				double scale = Math.sqrt(m22 - (m00 + m11) + 1.);
				z = scale * .5;
				scale = .5 / scale;
				x = (m20 + m02) * scale;
				y = (m12 + m21) * scale;
				w = (m10 - m01) * scale;
			}
		}
		set(x, y,  z, w);
	}

	/**
	 * Sets the value of this quaternion to the rotational component of
	 * the passed matrix.
	 * @param mat the matrix
	 */
	default void setFromMatrix(Matrix4d mat) {
		setFromMatrix(
				mat.getM00(), mat.getM01(), mat.getM02(),
				mat.getM10(), mat.getM11(), mat.getM12(),
				mat.getM20(), mat.getM21(), mat.getM22());
	}

	/**
	 * Sets the value of this quaternion to the rotational component of
	 * the passed matrix.
	 * @param m1 the Matrix3f
	 */
	default void setFromMatrix(Matrix3d m1) {
		setFromMatrix(
				m1.getM00(), m1.getM01(), m1.getM02(),
				m1.getM10(), m1.getM11(), m1.getM12(),
				m1.getM20(), m1.getM21(), m1.getM22());
	}

	/** Set the quaternion coordinates.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @param z z coordinate.
	 * @param w w coordinate.
	 */
	default void set(double x, double y, double z, double w) {
		setX(x);
		setY(y);
		setZ(z);
		setW(w);
	}

	/** Set the quaternion coordinates.
	 *
	 * @param quat the quaternion to copy.
	 */
	default void set(Quaternion quat) {
		assert quat != null : AssertMessages.notNullParameter();
		set(quat.getX(), quat.getY(), quat.getZ(), quat.getW());
	}

	/**
	 * Sets the value of this quaternion to the equivalent rotation
	 * of the Axis-Angle arguments.
	 *
	 * <p>{@code n = sqrt(axis.x*axis.x+axis.y*axis.y+axis.z*axis.z)}<br/>
	 * {@code this = (axis.x*sin(angle/2)*n, axis.y*sin(angle/2)*n, axis.z*sin(angle/2)*n, cos(angle/2))}
	 *
	 * @param axisAngle is the axis of rotation and the the rotation angle around the axis.
	 */
	default void setAxisAngle(AxisAngle axisAngle) {
		assert axisAngle != null : AssertMessages.notNullParameter(0);
		setAxisAngle(axisAngle.getX(), axisAngle.getY(), axisAngle.getZ(), axisAngle.getAngle());
	}

	/**
	 * Sets the value of this quaternion to the equivalent rotation
	 * of the Axis-Angle arguments.
	 *
	 * <p>{@code n = sqrt(axis.x*axis.x+axis.y*axis.y+axis.z*axis.z)}<br/>
	 * {@code this = (axis.x*sin(angle/2)*n, axis.y*sin(angle/2)*n, axis.z*sin(angle/2)*n, cos(angle/2))}
	 *
	 * @param axis is the axis of rotation.
	 * @param angle is the rotation around the axis.
	 */
	default void setAxisAngle(Vector3D<?, ?> axis, double angle) {
		assert axis != null : AssertMessages.notNullParameter(0);
		setAxisAngle(axis.getX(), axis.getY(), axis.getZ(), angle);
	}

	/**
	 * Sets the value of this quaternion to the equivalent rotation
	 * of the Axis-Angle arguments.
	 *
	 * <p>{@code n = sqrt(x*x+y*y+z*z)}<br/>
	 * {@code this = (x*sin(angle/2)*n, y*sin(angle/2)*n, z*sin(angle/2)*n, cos(angle/2))}
	 *
	 * @param x is the x coordinate of the rotation axis
	 * @param y is the y coordinate of the rotation axis
	 * @param z is the z coordinate of the rotation axis
	 * @param angle is the rotation around the axis.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	default void setAxisAngle(double x, double y, double z, double angle) {
		double nx = x;
		double ny = y;
		double nz = z;
		final double norm = Math.sqrt(nx * nx + ny * ny + nz * nz);
		// zero-div may occur.
		final double scale = Math.sin(.5 * angle) / norm;
		nx *= scale;
		ny *= scale;
		nz *= scale;
		set(nx, ny, nz, Math.cos(.5 * angle));
	}

	/** Replies the rotation axis-angle represented by this quaternion.
	 *
	 * @return the rotation axis-angle.
	 */
	@Pure
	default Vector3D<?, ?> getAxis() {
		return getAxisAngle().getAxis();
	}

	/** Replies the rotation angle represented by this quaternion.
	 *
	 * @return the rotation axis
	 * @see #setAxisAngle(Vector3D, double)
	 * @see #setAxisAngle(double, double, double, double)
	 * @see #getAxis()
	 */
	@Pure
	default double getAngle() {
		return getAxisAngle().getAngle();
	}

	/** Replies the rotation axis represented by this quaternion.
	 *
	 * @return the rotation axis
	 * @see #setAxisAngle(Vector3D, double)
	 * @see #setAxisAngle(double, double, double, double)
	 * @see #getAngle()
	 */
	@Pure
	default AxisAngle getAxisAngle() {
		final double x = getX();
		final double y = getY();
		final double z = getZ();
		final double w = getW();
		// |sin a/2|, w = cos a/2
		final double sina2 = Math.sqrt(x * x + y * y + z * z);
		// 0 <= angle <= PI , because 0 < sin_a2
		final double angle = 2. * Math.atan2(sina2, w);
		return new AxisAngle(x, y, z, angle);
	}

	/**
	 * Performs a great circle interpolation between this quaternion
	 * and the quaternion parameter and places the result into this
	 * quaternion.
	 *
	 * <p>{@code this = this + (quat - this) * alpha}
	 *
	 * @param quat  the other quaternion
	 * @param alpha  the alpha interpolation parameter
	 */
	default void interpolate(Quaternion quat, double alpha) {
		interpolate(this, quat, alpha);
	}

	/**
	 * Performs a great circle interpolation between quaternion quat1
	 * and quaternion quat2 and places the result into this quaternion.
	 *
	 * <p>{@code this = quat1 + (quat2 - quat1) * alpha}
	 *
	 * @param quat1  the first quaternion
	 * @param quat2  the second quaternion
	 * @param alpha  the alpha interpolation parameter
	 */
	default void interpolate(Quaternion quat1, Quaternion quat2, double alpha) {
		// zero-div may occur.
		final double n1 = Math.sqrt(quat1.norm());
		final double x1 = quat1.getX() / n1;
		final double y1 = quat1.getY() / n1;
		final double z1 = quat1.getZ() / n1;
		final double w1 = quat1.getW() / n1;
		// zero-div may occur.
		final double n2 = Math.sqrt(quat2.norm());
		final double x2 = quat2.getX() / n2;
		final double y2 = quat2.getY() / n2;
		final double z2 = quat2.getZ() / n2;
		final double w2 = quat2.getW() / n2;

		// t is cosine (dot product)
		double t = x1 * x2 + y1 * y2 + z1 * z2 + w1 * w2;

		// same quaternion (avoid domain error)
		if (1. <= Math.abs(t)) {
		    return;
		}

		// t is now theta
		t = Math.acos(t);

		final double sint = Math.sin(t);

		// same quaternion (avoid zero-div)
		if (sint == 0.) {
		    return;
		}

		final double s = Math.sin((1. - alpha) * t) / sint;
		t = Math.sin(alpha * t) / sint;

		// set values
		set(
				s * x1 + t * x2,
				s * y1 + t * y2,
				s * z1 + t * z2,
				s * w1 + t * w2);
	}

	/** Set the quaternion with the Euler angles.
	 * The {@link CoordinateSystem3D#getDefaultCoordinateSystem() default coordinate system}
	 * is used from applying the Euler angles.
	 *
	 * @param angles the Euler angles.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 */
	default void setEulerAngles(EulerAngles angles) {
		assert angles != null : AssertMessages.notNullParameter();
		setEulerAngles(angles.getAttitude(), angles.getBank(), angles.getHeading(), CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** Set the quaternion with the Euler angles.
	 * The {@link CoordinateSystem3D#getDefaultCoordinateSystem() default coordinate system}
	 * is used from applying the Euler angles.
	 *
	 * @param attitude is the rotation around left vector.
	 * @param bank is the rotation around front vector.
	 * @param heading is the rotation around top vector.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 */
	default void setEulerAngles(double attitude, double bank, double heading) {
		setEulerAngles(attitude, bank, heading, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** Set the quaternion with the Euler angles.
	 *
	 * @param attitude is the rotation around left vector.
	 * @param bank is the rotation around front vector.
	 * @param heading is the rotation around top vector.
	 * @param system the coordinate system to use for applying the Euler angles.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 */
	default void setEulerAngles(double attitude, double bank, double heading, CoordinateSystem3D system) {
		assert system != null : AssertMessages.notNullParameter(3);

		final double c1 = Math.cos(heading / 2.);
		final double s1 = Math.sin(heading / 2.);
		final double c2 = Math.cos(attitude / 2.);
		final double s2 = Math.sin(attitude / 2.);
		final double c3 = Math.cos(bank / 2.);
		final double s3 = Math.sin(bank / 2.);

		// Source: http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm
		// Standard used: XZY_RIGHT_HAND
		final double c1c2 = c1 * c2;
		final double s1s2 = s1 * s2;
		final double w = c1c2 * c3 - s1s2 * s3;
		final double x = c1c2 * s3 + s1s2 * c3;
		final double y = s1 * c2 * c3 + c1 * s2 * s3;
		final double z = c1 * s2 * c3 - s1 * c2 * s3;

		set(x, y, z, w);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(this, system);
	}

	/**
	 * Replies the Euler's angles that corresponds to the quaternion.
	 * The {@link CoordinateSystem3D#getDefaultCoordinateSystem() default coordinate system}
	 * is used from applying the Euler angles.
	 *
	 * @return the heading, attitude and bank angles.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm">Quaternion to Euler</a>
	 */
	@Pure
	default EulerAngles getEulerAngles() {
		return getEulerAngles(CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the Euler's angles that corresponds to the quaternion.
	 *
	 * @param system is the coordinate system used to define the up, left and front vectors.
	 * @return the heading, attitude and bank angles.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm">Quaternion to Euler</a>
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	default EulerAngles getEulerAngles(CoordinateSystem3D system) {
		assert system != null : AssertMessages.notNullParameter();

		// See http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
		// Standard used: XZY_RIGHT_HAND
		final Quaternion quat = clone();
		system.toSystem(quat, CoordinateSystem3D.XZY_RIGHT_HAND);

		final double sqw = quat.getW() * quat.getW();
		final double sqx = quat.getX() * quat.getX();
		final double sqy = quat.getY() * quat.getY();
		final double sqz = quat.getZ() * quat.getZ();
		// if normalised is one, otherwise is correction factor
		final double unit = sqx + sqy + sqz + sqw;
		final double test = quat.getX() * quat.getY() + quat.getZ() * quat.getW();

		if (MathUtil.compareEpsilon(test, .5 * unit) >= 0) {
			// singularity at north pole
			return new EulerAngles(
					2. * Math.atan2(quat.getX(), quat.getW()),
					MathConstants.DEMI_PI,
					0.,
					system);
		}
		if (MathUtil.compareEpsilon(test, -.5 * unit) <= 0) {
			// singularity at south pole
			return new EulerAngles(
					-2. * Math.atan2(quat.getX(), quat.getW()),
					-MathConstants.DEMI_PI,
					0.,
					system);
		}
		return new EulerAngles(
				Math.atan2(2. * quat.getY() * quat.getW() - 2. * quat.getX() * quat.getZ(), sqx - sqy - sqz + sqw),
				Math.asin(2. * test / unit), Math.atan2(2. * quat.getX() * quat.getW() - 2. * quat.getY() * quat.getZ(), -sqx + sqy - sqz + sqw),
				system);
	}

	@Override
	default void toJson(JsonBuffer buffer) {
		buffer.add("x", getX()); //$NON-NLS-1$
		buffer.add("y", getY()); //$NON-NLS-1$
		buffer.add("z", getZ()); //$NON-NLS-1$
		buffer.add("w", getW()); //$NON-NLS-1$
	}

	/** A representation of Euler Angles.
	 * The term "Euler Angle" is used for any representation of 3 dimensional
	 * rotations where the rotation is decomposed into 3 separate angles.
	 *
	 * <p>There is no single set of conventions and standards in this area,
	 * therefore the following conventions was choosen:<ul>
	 * <li>angle applied first:	heading;</li>
	 * <li>angle applied second: attitude;</li>
	 * <li>angle applied last: bank</li>
	 * </ul>
	 *
	 * <p>Examples: NASA aircraft standard and telescope standard
	 * <img src="doc-files/euler_plane.gif" alt="[NASA Aircraft Standard]">
	 * <img src="doc-files/euler_telescop.gif" alt="[Telescope Standard]">
	 *
	 * <p><strong>For creating an instance of this class, you must invoke
	 * {@link Quaternion#getEulerAngles(CoordinateSystem3D)}.</strong>
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	final class EulerAngles implements Cloneable, Serializable, JsonableObject {

		private static final long serialVersionUID = -1532832128836084395L;

		private final double attitude;

		private final double bank;

		private final double heading;

		private final CoordinateSystem3D system;

		/** Constructor.
		 *
		 * @param attitude the attitude angle.
		 * @param bank the bank angle.
		 * @param heading the heading angle.
		 * @param system the coordinate system that indicate the up direction.
		 */
		public EulerAngles(double attitude, double bank, double heading, CoordinateSystem3D system) {
			this.attitude = attitude;
			this.bank = bank;
			this.heading = heading;
			this.system = system;
		}

		/** Replies the attitude, the rotation around left vector.
		 *
		 * @return the attitude angle.
		 */
		@Pure
		public double getAttitude() {
			return this.attitude;
		}

		/** Replies the bank, the rotation around front vector.
		 *
		 * @return the bank angle.
		 */
		@Pure
		public double getBank() {
			return this.bank;
		}

		/** Replies the heading, the rotation around top vector.
		 *
		 * @return the heading angle.
		 */
		@Pure
		public double getHeading() {
			return this.heading;
		}

		/** Replies coordinate system used for obtaining the euler angles.
		 *
		 * @return the coordinate system.
		 */
		@Pure
		public CoordinateSystem3D getCoordinateSystem() {
			return this.system;
		}

		@Override
		public EulerAngles clone() {
			try {
				return (EulerAngles) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new Error(e);
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof EulerAngles) {
				final EulerAngles ea = (EulerAngles) obj;
				return this.attitude == ea.attitude && this.heading == ea.heading
						&& this.bank == ea.bank && this.system == ea.system;
			}
			return false;
		}

		@Override
		public int hashCode() {
			int bits = 1;
			bits = 31 * bits + Double.hashCode(this.heading);
			bits = 31 * bits + Double.hashCode(this.bank);
			bits = 31 * bits + Double.hashCode(this.attitude);
			bits = 31 * bits + (this.system == null ? 0 : this.system.hashCode());
			return bits ^ (bits >> 31);
		}

		@Override
		public String toString() {
			final JsonBuffer buffer = new JsonBuffer();
			toJson(buffer);
			return buffer.toString();
		}

		@Override
		public void toJson(JsonBuffer buffer) {
			buffer.add("attitude", getAttitude()); //$NON-NLS-1$
			buffer.add("bank", getBank()); //$NON-NLS-1$
			buffer.add("heading", getHeading()); //$NON-NLS-1$
			buffer.add("coordinateSystem", getCoordinateSystem()); //$NON-NLS-1$
		}

	}

	/** A representation of axis-angle.
	 * The axis–angle representation of a rotation parameterizes a rotation in a three-dimensional
	 * Euclidean space by two values: a unit vector, indicating the direction of an axis of rotation, and
	 * an angle describing the magnitude of the rotation about the axis.
	 * The rotation occurs in the sense prescribed by the (left/right)-hand rule.
	 * <img src="doc-files/axis_angle.png" alt="[Axis-Angle Representation]">
	 *
	 * <p><strong>For creating an instance of this class, you must invoke
	 * {@link Quaternion#getAxisAngle()}.</strong>
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	final class AxisAngle implements Cloneable, Serializable, JsonableObject {

		private static final long serialVersionUID = -7228694369177792159L;

		private final double x;

		private final double y;

		private final double z;

		private final double angle;

		/** Constructor.
		 *
		 * @param x x coordinate of the axis.
		 * @param y y coordinate of the axis.
		 * @param z z coordinate of the axis.
		 * @param angle rotation angle around the axis.
		 */
		public AxisAngle(double x, double y, double z, double angle) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.angle = angle;
		}

		/** Replies the rotation axis.
		 *
		 * @return the rotation axis.
		 */
		@Pure
		public Vector3D<?, ?> getAxis() {
			return new Vector3d(this.x, this.y, this.z);
		}

		/** Replies the x coordinate of the rotation axis.
		 *
		 * @return x coordinate of the rotation axis.
		 */
		@Pure
		public double getX() {
			return this.x;
		}

		/** Replies the y coordinate of the rotation axis.
		 *
		 * @return y coordinate of the rotation axis.
		 */
		@Pure
		public double getY() {
			return this.y;
		}

		/** Replies the z coordinate of the rotation axis.
		 *
		 * @return z coordinate of the rotation axis.
		 */
		@Pure
		public double getZ() {
			return this.z;
		}

		/** Replies the rotation angle.
		 *
		 * @return the rotation angle.
		 */
		@Pure
		public double getAngle() {
			return this.angle;
		}

		@Override
		public AxisAngle clone() {
			try {
				return (AxisAngle) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new Error(e);
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof AxisAngle) {
				final AxisAngle aa = (AxisAngle) obj;
				return this.x == aa.x && this.y == aa.y && this.z == aa.z && this.angle == aa.angle;
			}
			return false;
		}

		@Override
		public int hashCode() {
			int bits = 1;
			bits = 31 * bits + Double.hashCode(this.x);
			bits = 31 * bits + Double.hashCode(this.y);
			bits = 31 * bits + Double.hashCode(this.z);
			bits = 31 * bits + Double.hashCode(this.angle);
			return bits ^ (bits >> 31);
		}

		@Override
		public String toString() {
			final JsonBuffer buffer = new JsonBuffer();
			toJson(buffer);
			return buffer.toString();
		}

		@Override
		public void toJson(JsonBuffer buffer) {
			buffer.add("x", this.x); //$NON-NLS-1$
			buffer.add("y", this.y); //$NON-NLS-1$
			buffer.add("z", this.z); //$NON-NLS-1$
			buffer.add("angle", this.angle); //$NON-NLS-1$
		}

	}

}
