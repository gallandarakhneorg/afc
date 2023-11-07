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

package org.arakhne.afc.math.geometry.d3;

import java.io.Serializable;

import org.arakhne.afc.math.GeogebraUtil;
import org.arakhne.afc.math.GnuOctaveUtil;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.arakhne.afc.vmutil.json.JsonableObject;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

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
 * @param <RP> is the type of point that can be returned by this quaternion.
 * @param <RV> is the type of vector that can be returned by this quaternion.
 * @param <RQ> is the type of quaternion that can be returned by this quaternion.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Quaternion<RP extends Point3D<? super RP, ? super RV, ? super RQ>,
RV extends Vector3D<? super RV, ? super RP, ? super RQ>,
RQ extends Quaternion<? super RP, ? super RV, ? super RQ>> extends Cloneable, Serializable, JsonableObject {

	/** Default value that represents the maximal approximation allowed in the quaternion's components.
	 * @since 18.0 
	 */
	final static double EPS = 0.000001;

	/**
	 * Returns true if the L-infinite distance between the two quaternions is less than or equal to the epsilon parameter,
	 * otherwise returns false.  The L-infinite
	 * distance is equal to MAX[abs(x1-x2), abs(y1-y2), abs(z1-z2), abs(w1-w2)].
	 * @param t1x the x coordinate of the first quaternion.
	 * @param t1y the y coordinate of the first quaternion.
	 * @param t1z the z coordinate of the first quaternion.
	 * @param t1w the w coordinate of the first quaternion.
	 * @param t2x the x coordinate of the second quaternion.
	 * @param t2y the y coordinate of the second quaternion.
	 * @param t2z the z coordinate of the second quaternion.
	 * @param t2w the w coordinate of the second quaternion.
	 * @param epsilon  the threshold value
	 * @return  true or false
	 */
	@Pure
	static boolean isEpsilonEquals(double t1x, double t1y, double t1z, double t1w, double t2x, double t2y, double t2z, double t2w, double epsilon) {
		double diff;

		diff = t1x - t2x;
		if (Double.isNaN(diff) || (diff < 0 ? -diff : diff) > epsilon) {
			return false;
		}

		diff = t1y - t2y;
		if (Double.isNaN(diff) || (diff < 0 ? -diff : diff) > epsilon) {
			return false;
		}

		diff = t1z - t2z;
		if (Double.isNaN(diff) || (diff < 0 ? -diff : diff) > epsilon) {
			return false;
		}

		diff = t1w - t2w;
		if (Double.isNaN(diff) || (diff < 0 ? -diff : diff) > epsilon) {
			return false;
		}

		return true;
	}

	/**
	 * Compute the value of the a quaternion that is the equivalent rotation of the Axis-Angle arguments.
	 *
	 * @param x is the x coordinate of the rotation axis
	 * @param y is the y coordinate of the rotation axis
	 * @param z is the z coordinate of the rotation axis
	 * @param angle is the rotation around the axis in radians.
	 * @return the components {@code (x, y, z, w)} of the quaternion.
	 * @since 18.0 
	 */
	static QuaternionComponents computeWithAxisAngle(double x, double y, double z, double angle) {
		final double qx, qy, qz, qw;
		double amag = Math.sqrt(x * x + y * y + z * z);
		if (amag < EPS) {
			qw = 0.;
			qx = 0.;
			qy = 0.;
			qz = 0.;
		} else {  
			amag = 1. / amag; 
			final double mag = Math.sin(angle / 2.);
			qw = Math.cos(angle / 2.);
			qx = x * amag * mag;
			qy = y * amag * mag;
			qz = z * amag * mag;
		}
		return new QuaternionComponents(qx, qy, qz, qw);
	}

	/**
	 * Replies the rotation axis represented by the given quaternion.
	 *
	 * @param x the x coordinate of the quaternion.
	 * @param y the y coordinate of the quaternion.
	 * @param z the z coordinate of the quaternion.
	 * @param w the w coordinate of the quaternion.
	 * @return the axis angle
	 * @since 18.0 
	 */
	static AxisAngle computeAxisAngle(double x, double y, double z, double w) {
		double mag = x * x + y * y + z * z;  

		if (mag > EPS) {
			mag = Math.sqrt(mag);
			final double invMag = 1. / mag;
			final ImmutableVector3D iv = new ImmutableVector3D(x * invMag, y * invMag, z * invMag);
			return new AxisAngle(iv.getX(), iv.getY(), iv.getZ(), 2. * Math.atan2(mag, w), iv);
		}
		return new AxisAngle(0, 0, 1, 0, new ImmutableVector3D(0, 0, 1));
	}

	/** Compute the quaternion components with the Euler angles.
	 * The {@link CoordinateSystem3D#getDefaultCoordinateSystem() default coordinate system}
	 * is used from applying the Euler angles.
	 *
	 * @param attitude is the rotation around left vector.
	 * @param bank is the rotation around front vector.
	 * @param heading is the rotation around top vector.
	 * @param system the coordinate system to use for applying the Euler angles.
	 * @return the quaternion components.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 * @since 18.0
	 */
	static QuaternionComponents computeWithEulerAngles(double attitude, double bank, double heading, CoordinateSystem3D system) {
		final CoordinateSystem3D cs = system == null ? CoordinateSystem3D.getDefaultCoordinateSystem() : system;

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

		QuaternionComponents comps = new QuaternionComponents(x, y, z, w);
		comps = CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(comps, cs);
		return comps;
	}

	/**
	 * Replies the Euler angles represented by the given quaternion.
	 *
	 * @param x the x coordinate of the quaternion.
	 * @param y the y coordinate of the quaternion.
	 * @param z the z coordinate of the quaternion.
	 * @param w the w coordinate of the quaternion.
	 * @param system the coordinate system to use as the reference for the Euler angles.
	 * @return the Euler angles.
	 * @since 18.0 
	 */
	static EulerAngles computeEulerAngles(double x, double y, double z, double w, CoordinateSystem3D system) {
		// See http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
		// Standard used: XZY_RIGHT_HAND
		final QuaternionComponents q = system.toSystem(new QuaternionComponents(x, y, z, w), CoordinateSystem3D.XZY_RIGHT_HAND);

		final double sqw = q.w * q.w;
		final double sqx = q.x * q.x;
		final double sqy = q.y * q.y;
		final double sqz = q.z * q.z;
		final double unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise is correction factor
		final double test = q.x * q.y + q.z * q.w;

		if (MathUtil.compareEpsilon(test, .5 * unit) >= 0) { // singularity at north pole
			return new EulerAngles(
					2. * Math.atan2(q.x, q.w), // heading
					MathConstants.DEMI_PI, // attitude
					0.,
					system);
		}
		if (MathUtil.compareEpsilon(test, -.5 * unit) <= 0) { // singularity at south pole
			return new EulerAngles(
					-2. * Math.atan2(q.x, q.w), // heading
					-MathConstants.DEMI_PI, // attitude
					0.,
					system);
		}
		return new EulerAngles(
				Math.atan2(2. * q.y * q.w - 2. * q.x * q.z, sqx - sqy - sqz + sqw),
				Math.asin(2. * test / unit),
				Math.atan2(2. * q.x * q.w - 2. * q.y * q.z, -sqx + sqy - sqz + sqw),
				system);
	}

	/** Replies the X coordinate.
	 *
	 * @return x
	 */
	@Pure
	double getX();

	/** Replies the X coordinate.
	 *
	 * @return x
	 * @since 18.0
	 */
	@Pure
	int ix();

	/** Set the X coordinate.
	 *
	 * @param x x coordinate.
	 */
	void setX(double x);

	/** Set the X coordinate.
	 *
	 * @param x x coordinate.
	 * @since 18.0
	 */
	void setX(int x);

	/** Replies the Y coordinate.
	 *
	 * @return y
	 */
	@Pure
	double getY();

	/** Replies the Y coordinate.
	 *
	 * @return y
	 * @since 18.0
	 */
	@Pure
	int iy();

	/** Set the Y coordinate.
	 *
	 * @param y y coordinate.
	 */
	void setY(double y);

	/** Set the Y coordinate.
	 *
	 * @param y y coordinate.
	 * @since 18.0
	 */
	void setY(int y);

	/** Replies the Z coordinate.
	 *
	 * @return z
	 */
	@Pure
	double getZ();

	/** Replies the Z coordinate.
	 *
	 * @return z
	 * @since 18.0
	 */
	@Pure
	int iz();

	/** Set the Z coordinate.
	 *
	 * @param z z coordinate.
	 */
	void setZ(double z);

	/** Set the Z coordinate.
	 *
	 * @param z z coordinate.
	 * @since 18.0
	 */
	void setZ(int z);

	/** Replies the W coordinate.
	 *
	 * @return w
	 */
	@Pure
	double getW();

	/** Replies the W coordinate.
	 *
	 * @return w
	 * @since 18.0
	 */
	@Pure
	int iw();

	/** Set the W coordinate.
	 *
	 * @param w w coordinate.
	 */
	void setW(double w);

	/** Set the W coordinate.
	 *
	 * @param w w coordinate.
	 * @since 18.0
	 */
	void setW(int w);

	/**
	 * Returns true if the L-infinite distance between this quaternion
	 * and the given quaternion is less than or equal to the epsilon parameter,
	 * otherwise returns false.  The L-infinite
	 * distance is equal to MAX[abs(x1-x2), abs(y1-y2), abs(z1-z2), abs(w1-w2)].
	 * @param t  the quaternion to be compared to this quaternion
	 * @param epsilon  the threshold value
	 * @return  true or false
	 */
	@Pure
	@Inline(value = "$4.isEpsilonEquals($1, $2, $3)", imported = {Quaternion.class})
	default boolean epsilonEquals(Quaternion<?, ?, ?> t, double epsilon) {
		return isEpsilonEquals(getX(), getY(), getZ(), getW(), t.getX(), t.getY(), t.getZ(), t.getW(), epsilon);
	}

	/** Replies the geometry factory associated to this point.
	 *
	 * @return the factory.
	 * @since 18.0
	 */
	@Pure
	GeomFactory3D<RV, RP, RQ> getGeomFactory();

	/**
	 * Sets the value of this quaternion to the conjugate of quaternion q1.
	 * The result is the quaternion {@code (-x, -y, -z, w)}.
	 * @param q the source vector
	 */
	void conjugate(Quaternion<?, ?, ?> q);

	/**
	 * Sets the value of this quaternion to the conjugate of itself.
	 * The result is the quaternion {@code (-x, -y, -z, w)}.
	 */
	default void conjugate() {
		conjugate(this);
	}

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * quaternions q1 and q2 (this = q1 * q2).
	 * Note that this is safe for aliasing (e.g. this can be q1 or q2).
	 * @param q1 the first quaternion
	 * @param q2 the second quaternion
	 */
	void mul(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2);

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * itself and q1 (this = this * q1).
	 * @param q the other quaternion
	 */
	default void mul(Quaternion<?, ?, ?> q) {
		mul(this, q);
	}

	/**
	 * Multiplies quaternion q1 by the inverse of quaternion q2 and places
	 * the value into this quaternion.  The value of both argument quaternions
	 * is preservered (this = q1 * q2^-1).
	 * @param q1 the first quaternion
	 * @param q2 the second quaternion
	 */
	void mulInverse(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2);

	/**
	 * Multiplies this quaternion by the inverse of quaternion q1 and places
	 * the value into this quaternion.  The value of the argument quaternion
	 * is preserved (this = this * q^-1).
	 * @param q the other quaternion
	 */
	default void mulInverse(Quaternion<?, ?, ?> q) {
		mulInverse(this, q);
	}

	/**
	 * Sets the value of this quaternion to quaternion inverse of quaternion q1.
	 * The result is the NORMALIZED quaternion from {@code (-x, -y, -z, w)}.
	 * @param q the quaternion to be inverted
	 */
	void inverse(Quaternion<?, ?, ?> q);

	/**
	 * Sets the value of this quaternion to the quaternion inverse of itself.
	 * The result is the NORMALIZED quaternion from {@code (-x, -y, -z, w)}.
	 */
	default void inverse() {
		inverse(this);
	}

	/**
	 * Sets the value of this quaternion to the normalized value
	 * of quaternion q1.
	 * @param q the quaternion to be normalized.
	 */
	void normalize(Quaternion<?, ?, ?> q);

	/**
	 * Normalizes the value of this quaternion in place.
	 */
	default void normalize() {
		normalize(this);
	}

	/** Set the quaternion coordinates.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @param z z coordinate.
	 * @param w w coordinate.
	 */
	void set(double x, double y, double z, double w);

	/** Set the quaternion coordinates.
	 *
	 * @param quat the quaternion to copy.
	 */
	default void set(Quaternion<?, ?, ?> quat) {
		assert quat != null;
		set(quat.getX(), quat.getY(), quat.getZ(), quat.getW());
	}

	/**
	 * Sets the value of this quaternion to the equivalent rotation
	 * of the Axis-Angle arguments.
	 * @param axis is the axis of rotation.
	 * @param angle is the rotation around the axis.
	 */
	default void setAxisAngle(Vector3D<?, ?, ?> axis, double angle) {
		assert axis != null;
		final QuaternionComponents comps = computeWithAxisAngle(axis.getX(), axis.getY(), axis.getZ(), angle);
		set(comps.x(), comps.y(), comps.z(), comps.w());
	}

	/**
	 * Sets the value of this quaternion to the equivalent rotation
	 * of the Axis-Angle arguments.
	 * @param x is the x coordinate of the rotation axis
	 * @param y is the y coordinate of the rotation axis
	 * @param z is the z coordinate of the rotation axis
	 * @param angle is the rotation around the axis in radians.
	 */
	default void setAxisAngle(double x, double y, double z, double angle) {
		final QuaternionComponents comps = computeWithAxisAngle(x, y, z, angle);
		set(comps.x(), comps.y(), comps.z(), comps.w());
	}

	/** Sets the value of this quaternion to the equivalent rotation
	 * of the Axis-Angle arguments.
	 *
	 * @param axisangle the Axis-Angle object.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 * @since 18.0
	 */
	default void setAxisAngle(AxisAngle axisangle)  {
		assert axisangle != null;
		final QuaternionComponents comps = computeWithAxisAngle(axisangle.x, axisangle.y, axisangle.z, axisangle.angle);
		set(comps.x(), comps.y(), comps.z(), comps.w());
	}

	/** Replies the rotation axis-angle represented by this quaternion.
	 *
	 * @return the rotation axis-angle.
	 */
	@Pure
	default RV getAxis() {
		final Vector3D<?, ?, ?> axis = getAxisAngle().axis;
		return getGeomFactory().newVector(axis.getX(), axis.getY(), axis.getZ());
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
		return getAxisAngle().angle;
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
		return computeAxisAngle(getX(), getY(), getZ(), getW());
	}

	/**
	 *  Performs a great circle interpolation between this quaternion
	 *  and the quaternion parameter and places the result into this
	 *  quaternion.
	 *  @param q the other quaternion
	 *  @param alpha the alpha interpolation parameter
	 */
	default void interpolate(Quaternion<?, ?, ?> q, double alpha) {
		interpolate(this, q, alpha);
	}

	/**
	 *  Performs a great circle interpolation between quaternion q1
	 *  and quaternion q2 and places the result into this quaternion.
	 *  @param q1 the first quaternion
	 *  @param q2 the second quaternion
	 *  @param alpha the alpha interpolation parameter
	 */
	void interpolate(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2, double alpha);

	/** Set the quaternion with the Euler angles.
	 *
	 * @param angles the Euler angles.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 */
	default void setEulerAngles(EulerAngles angles) {
		assert angles != null;
		final QuaternionComponents comps = computeWithEulerAngles(angles.attitude(), angles.bank(), angles.heading(), angles.system());
		set(comps.x(), comps.y(), comps.z(), comps.w());
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
		final QuaternionComponents comps = computeWithEulerAngles(attitude, bank, heading, null);
		set(comps.x(), comps.y(), comps.z(), comps.w());
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
		final QuaternionComponents comps = computeWithEulerAngles(attitude, bank, heading, system);
		set(comps.x(), comps.y(), comps.z(), comps.w());
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
		return computeEulerAngles(getX(), getY(), getZ(), getW(), null);
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
	default EulerAngles getEulerAngles(CoordinateSystem3D system) {
		return computeEulerAngles(getX(), getY(), getZ(), getW(), system);
	}

	/** Replies an unmodifiable copy of this vector.
	 *
	 * @return an unmodifiable copy.
	 * @since 18.0
	 */
	@Pure
	UnmodifiableQuaternion<RP, RV, RQ> toUnmodifiable();

	/**
	 * Returns {@code true} if all of the data members of quaternion are
	 * equal to the corresponding data members in this object.
	 * @param quaternion the quaternion with which the comparison is made
	 * @return true or false
	 */
	@Pure
	default boolean equals(Quaternion<?, ?, ?> quaternion) {
		try {
			return getX() == quaternion.getX() && getY() == quaternion.getY() && getZ() == quaternion.getZ() && getW() == quaternion.getW();
		} catch (Throwable exception) {
			return false;
		}
	}

	/**
	 * Returns true if the Object t is of type Quaternion and all of the
	 * data members of t are equal to the corresponding data members in
	 * this object.
	 * @param t  the object with which the comparison is made
	 * @return  true or false
	 */
	@Pure
	@Override
	boolean equals(Object t);

	/**
	 * Returns a hash code value based on the data values in this
	 * object.  Two different Tuple2f objects with identical data values
	 * (i.e., Quaternion.equals returns true) will return the same hash
	 * code value.  Two objects with different data members may return the
	 * same hash value, although this is not likely.
	 * @return the integer hash code value
	 */
	@Pure
	@Override
	int hashCode();

	@Override
	default void toJson(JsonBuffer buffer) {
		buffer.add("x", getX()); //$NON-NLS-1$
		buffer.add("y", getY()); //$NON-NLS-1$
		buffer.add("z", getZ()); //$NON-NLS-1$
		buffer.add("w", getW()); //$NON-NLS-1$
		buffer.add("q", toGnuOctave()); //$NON-NLS-1$
	}

	/** Replies the representation of the quaternion for GNU octave (or Matlab).
	 *
	 * @return the octave representation.
	 * @since 18.0
	 */
	default String toGnuOctave() {
		return GnuOctaveUtil.toQuaternionDefinition(getX(), getY(), getZ(), getW());
	}

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * itself and q1 (this = this * q1).
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param q the other quaternion
	 * @see #mul(Quaternion)
	 * @since 18.0
	 */
	@Pure
	@XtextOperator("*")
	default RQ operator_multiply(Quaternion<?, ?, ?> q) {
		final RQ result = getGeomFactory().newQuaternion(getX(), getY(), getZ(), getW());
		result.mul(q);
		return result;
	}

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * itself and q1 (this = this / q1).
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param q the other quaternion
	 * @see #mulInverse(Quaternion)
	 * @since 18.0
	 */
	@Pure
	@XtextOperator("/")
	default RQ operator_divide(Quaternion<?, ?, ?> q) {
		final RQ result = getGeomFactory().newQuaternion(getX(), getY(), getZ(), getW());
		result.mulInverse(q);
		return result;
	}

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * itself and q1 (this = this * q1).
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param q the other quaternion
	 * @see #mul(Quaternion)
	 * @since 18.0
	 */
	@Pure
	@ScalaOperator("*")
	default RQ $times(Quaternion<?, ?, ?> q) {
		return operator_multiply(q);
	}

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * itself and q1 (this = this / q1).
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param q the other quaternion
	 * @see #mulInverse(Quaternion)
	 * @since 18.0
	 */
	@Pure
	@ScalaOperator("/")
	default RQ $divide(Quaternion<?, ?, ?> q) {
		return operator_divide(q);
	}

	/** Replies this quaternion with a Geogebra-compatible form.
	 *
	 * @return the Geogebra representation of the quaternion.
	 * @since 18.0
	 */
	default String toGeogebra() {
		return GeogebraUtil.toTupleDefinition(4, getX(), getY(), getZ(), getW());
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
	 * @param attitude the attitude angle defined by Euler.
	 * @param bank the bank angle defined by Euler.
	 * @param heading the heading angle defined by Euler.
	 * @param system the coordinate system in which the Euler angles are defined.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	record EulerAngles(double attitude, double bank, double heading, CoordinateSystem3D system) {
		//
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
	 * @param x the X coordinate of the rotation axis.
	 * @param y the Y coordinate of the rotation axis.
	 * @param z the Z coordinate of the rotation axis.
	 * @param angle the rotation angle around the rotation axis in radians.
	 * @param axis the axis (x, y, z).
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	record AxisAngle(double x, double y, double z, double angle, UnmodifiableVector3D<?, ?, ?> axis) {
		//
	}


	/** A representation of the components of the quaternion.
	 *
	 * @param x the X component of the quaternion.
	 * @param y the Y component of the quaternion.
	 * @param z the Z component of the quaternion.
	 * @param w the W component of the quaternion.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	record QuaternionComponents(double x, double y, double z, double w) {
		//
	}

}
