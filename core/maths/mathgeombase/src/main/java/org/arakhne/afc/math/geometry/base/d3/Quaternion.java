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

package org.arakhne.afc.math.geometry.base.d3;

import java.io.Serializable;

import org.arakhne.afc.math.GeogebraUtil;
import org.arakhne.afc.math.GnuOctaveUtil;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.vmutil.annotations.GroovyOperator;
import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.arakhne.afc.vmutil.json.JsonableObject;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * A unit quaternion in 4D form, represented by four components {@code (x, y, z, w)} and
 * constrained to have unit length.
 *
 * <p>A quaternion extends complex numbers and is commonly used to represent
 * 3D rotations in a compact, stable, and interpolation-friendly way.
 * This class stores a quaternion as:
 *
 * <p style="margin-left:1em">
 * <math xmlns="http://www.w3.org/1998/Math/MathML" display="block">
 *   <mi>q</mi><mo>=</mo><mi>x</mi><mi>i</mi><mo>+</mo><mi>y</mi><mi>j</mi>
 *   <mo>+</mo><mi>z</mi><mi>k</mi><mo>+</mo><mi>w</mi>
 * </math>
 *
 * <p>where {@code (x, y, z)} is the vector part and {@code w} is the scalar part.
 *
 * <h2>Normalization invariant</h2>
 *
 * <p>This type represents only normalized (unit) quaternions. Therefore:
 *
 * <p style="margin-left:1em">
 * <math xmlns="http://www.w3.org/1998/Math/MathML" display="block">
 *   <msup><mi>x</mi><mn>2</mn></msup>
 *   <mo>+</mo>
 *   <msup><mi>y</mi><mn>2</mn></msup>
 *   <mo>+</mo>
 *   <msup><mi>z</mi><mn>2</mn></msup>
 *   <mo>+</mo>
 *   <msup><mi>w</mi><mn>2</mn></msup>
 *   <mo>=</mo>
 *   <mn>1</mn>
 * </math>
 *
 * <p>Maintaining this invariant is essential for valid rotation behavior.
 *
 * <h2>Rotation interpretation</h2>
 *
 * <p>A unit quaternion can encode a rotation by angle
 * <math xmlns="http://www.w3.org/1998/Math/MathML"><mi>&#x03B8;</mi></math>
 * around a unit axis
 * <math xmlns="http://www.w3.org/1998/Math/MathML">
 *   <mrow><mo>(</mo><msub><mi>u</mi><mi>x</mi></msub><mo>,</mo><msub><mi>u</mi><mi>y</mi>
 *   </msub><mo>,</mo><msub><mi>u</mi><mi>z</mi></msub><mo>)</mo></mrow>
 * </math>:
 *
 * <p style="margin-left:1em">
 * <math xmlns="http://www.w3.org/1998/Math/MathML" display="block">
 *   <mi>x</mi><mo>=</mo><msub><mi>u</mi><mi>x</mi></msub><mi>sin</mi><mo>(</mo><mi>&#x03B8;</mi><mo>/</mo><mn>2</mn><mo>)</mo>
 *   <mspace width="1em"/>
 *   <mi>y</mi><mo>=</mo><msub><mi>u</mi><mi>y</mi></msub><mi>sin</mi><mo>(</mo><mi>&#x03B8;</mi><mo>/</mo><mn>2</mn><mo>)</mo>
 *   <mspace width="1em"/>
 *   <mi>z</mi><mo>=</mo><msub><mi>u</mi><mi>z</mi></msub><mi>sin</mi><mo>(</mo><mi>&#x03B8;</mi><mo>/</mo><mn>2</mn><mo>)</mo>
 *   <mspace width="1em"/>
 *   <mi>w</mi><mo>=</mo><mi>cos</mi><mo>(</mo><mi>&#x03B8;</mi><mo>/</mo><mn>2</mn><mo>)</mo>
 * </math>
 *
 * <h2>Conjugate and inverse</h2>
 *
 * <p>For a unit quaternion:
 *
 * <p style="margin-left:1em">
 * <math xmlns="http://www.w3.org/1998/Math/MathML" display="block">
 *   <msup><mi>q</mi><mrow><mo>-</mo><mn>1</mn></mrow></msup>
 *   <mo>=</mo>
 *   <mover><mi>q</mi><mo>&#x00AF;</mo></mover>
 *   <mo>=</mo>
 *   <mo>(</mo><mo>-</mo><mi>x</mi><mo>,</mo><mo>-</mo><mi>y</mi><mo>,</mo><mo>-</mo><mi>z</mi><mo>,</mo><mi>w</mi><mo>)</mo>
 * </math>
 *
 * <p>This property makes inversion efficient when composing or undoing rotations.
 *
 * <h2>Notes</h2>
 * <ul>
 *   <li>{@code q} and {@code -q} represent the same spatial rotation.</li>
 *   <li>Use normalization-preserving operations to avoid numerical drift.</li>
 *   <li>Quaternion composition is not commutative:
 *     <math xmlns="http://www.w3.org/1998/Math/MathML">
 *       <mi>p</mi><mi>q</mi><mo>&#x2260;</mo><mi>q</mi><mi>p</mi>
 *     </math>.
 *   </li>
 * </ul>
 *
 * <h2>Other Rotation Representations</h2>
 *
 * <p>Other representations of an rotation are available from this class:
 * axis-angle, and Euler angles.
 *
 * <h3>Axis Angles</h3>
 *
 * <p>The axis–angle representation of a rotation parameterizes a rotation in a three-dimensional
 * Euclidean space by two values: a unit vector, indicating the direction of an axis of rotation, and
 * an angle describing the magnitude of the rotation about the axis.
 * The rotation occurs in the sense prescribed by the (left/right)-hand rule.
 * <img src="doc-files/axis_angle.png" alt="[Axis-Angle Representation]">
 *
 * <h3>Euler Angles</h3>
 *
 * <p>The term "Euler Angle" is used for any representation of 3 dimensional
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
@SuppressWarnings("checkstyle:magicnumber")
public interface Quaternion<RP extends Point3D<? super RP, ? super RV, ? super RQ>,
		RV extends Vector3D<? super RV, ? super RP, ? super RQ>,
		RQ extends Quaternion<? super RP, ? super RV, ? super RQ>> extends Cloneable, Serializable, JsonableObject {

	/** Default value that represents the maximal approximation allowed in the quaternion's components.
	 * @since 18.0
	 */
	double EPS = 0.000001;

	/** Clone this quaternion.
	 *
	 * @return the clone.
	 */
	@Pure
	RQ clone();

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
	@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:parameternumber"})
	static boolean isEpsilonEquals(double t1x, double t1y, double t1z, double t1w,
			double t2x, double t2y, double t2z, double t2w, double epsilon) {
		var diff = t1x - t2x;
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
		var amag = Math.sqrt(x * x + y * y + z * z);
		final double qx;
		final double qy;
		final double qz;
		final double qw;
		if (amag < EPS) {
			qw = 0.;
			qx = 0.;
			qy = 0.;
			qz = 0.;
		} else {
			amag = 1. / amag;
			final var mag = Math.sin(angle / 2.);
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
		var mag = x * x + y * y + z * z;
		if (mag > EPS) {
			mag = Math.sqrt(mag);
			final var invMag = 1. / mag;
			final var iv = new ImmutableVector3D(x * invMag, y * invMag, z * invMag);
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
	 * @since 18.0
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 */
	static QuaternionComponents computeWithEulerAngles(double attitude, double bank, double heading, CoordinateSystem3D system) {
		final var cs = system == null ? CoordinateSystem3D.getDefaultCoordinateSystem() : system;

		final var c1 = Math.cos(heading / 2.);
		final var s1 = Math.sin(heading / 2.);
		final var c2 = Math.cos(attitude / 2.);
		final var s2 = Math.sin(attitude / 2.);
		final var c3 = Math.cos(bank / 2.);
		final var s3 = Math.sin(bank / 2.);

		// Source: http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm
		// Standard used: XZY_RIGHT_HAND
		final var c1c2 = c1 * c2;
		final var s1s2 = s1 * s2;
		final var w = c1c2 * c3 - s1s2 * s3;
		final var x = c1c2 * s3 + s1s2 * c3;
		final var y = s1 * c2 * c3 + c1 * s2 * s3;
		final var z = c1 * s2 * c3 - s1 * c2 * s3;

		var comps = new QuaternionComponents(x, y, z, w);
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
	 * @throws IllegalArgumentException if the system is not valid.
	 * @since 18.0
	 */
	static EulerAngles computeEulerAngles(double x, double y, double z, double w, CoordinateSystem3D system) {
		// See http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
		// Standard used on the website: XZY_RIGHT_HAND
		final var cs = system == null ? CoordinateSystem3D.getDefaultCoordinateSystem() : system;
		switch (cs) {
		case XZY_RIGHT_HAND:
			return EulerAnglesTools.computeEulerAnglesXZYR(x, y, z, w);
		case XZY_LEFT_HAND:
			return EulerAnglesTools.computeEulerAnglesXZYL(x, y, z, w);
		case XYZ_LEFT_HAND:
			return EulerAnglesTools.computeEulerAnglesXYZL(x, y, z, w);
		case XYZ_RIGHT_HAND:
			return EulerAnglesTools.computeEulerAnglesXYZR(x, y, z, w);
		default:
			throw new IllegalArgumentException();
		}
	}

	/** Replies the X coordinate.
	 *
	 * @return x
	 */
	@Pure
	double getX();

	/** Set the X coordinate. This function does not ensure that the quaternion is normalized.
	 *
	 * @param x x coordinate.
	 */
	void setX(double x);

	/** Set the X coordinate. This function does not ensure that the quaternion is normalized.
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

	/** Set the Y coordinate. This function does not ensure that the quaternion is normalized.
	 *
	 * @param y y coordinate.
	 */
	void setY(double y);

	/** Set the Y coordinate. This function does not ensure that the quaternion is normalized.
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

	/** Set the Z coordinate. This function does not ensure that the quaternion is normalized.
	 *
	 * @param z z coordinate.
	 */
	void setZ(double z);

	/** Set the Z coordinate. This function does not ensure that the quaternion is normalized.
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

	/** Set the W coordinate. This function does not ensure that the quaternion is normalized.
	 *
	 * @param w w coordinate.
	 */
	void setW(double w);

	/** Set the W coordinate. This function does not ensure that the quaternion is normalized.
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
	 * The result is the quaternion {@code (-x, -y, -z, w)} without normalization.
	 * @param quaternion the source vector
	 */
	void conjugate(Quaternion<?, ?, ?> quaternion);

	/**
	 * Sets the value of this quaternion to the conjugate of itself.
	 * The result is the quaternion {@code (-x, -y, -z, w)} without normalization.
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
	 * @param quaternion the other quaternion
	 */
	default void mul(Quaternion<?, ?, ?> quaternion) {
		mul(this, quaternion);
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
	 * @param quaternion the other quaternion
	 */
	default void mulInverse(Quaternion<?, ?, ?> quaternion) {
		mulInverse(this, quaternion);
	}

	/**
	 * Sets the value of this quaternion to quaternion inverse of quaternion q1.
	 * The result is the NORMALIZED quaternion from {@code (-x, -y, -z, w)}.
	 * @param quaternion the quaternion to be inverted
	 */
	void inverse(Quaternion<?, ?, ?> quaternion);

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
	 * @param quaternion the quaternion to be normalized.
	 */
	void normalize(Quaternion<?, ?, ?> quaternion);

	/**
	 * Normalizes the value of this quaternion in place.
	 */
	default void normalize() {
		normalize(this);
	}

	/** Set the quaternion coordinates. The quaternion is normalized after the call to this function.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @param z z coordinate.
	 * @param w w coordinate.
	 */
	void set(double x, double y, double z, double w);

	/** Set the quaternion coordinates. The quaternion is normalized after the call to this function.
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
	 * The quaternion is normalized after the call to this function.
	 *
	 * @param axis is the axis of rotation.
	 * @param angle is the rotation around the axis.
	 */
	default void setAxisAngle(Vector3D<?, ?, ?> axis, double angle) {
		assert axis != null;
		final var comps = computeWithAxisAngle(axis.getX(), axis.getY(), axis.getZ(), angle);
		set(comps.x(), comps.y(), comps.z(), comps.w());
	}

	/**
	 * Sets the value of this quaternion to the equivalent rotation
	 * of the Axis-Angle arguments.
	 * The quaternion is normalized after the call to this function.
	 *
	 * @param x is the x coordinate of the rotation axis
	 * @param y is the y coordinate of the rotation axis
	 * @param z is the z coordinate of the rotation axis
	 * @param angle is the rotation around the axis in radians.
	 */
	default void setAxisAngle(double x, double y, double z, double angle) {
		final var comps = computeWithAxisAngle(x, y, z, angle);
		set(comps.x(), comps.y(), comps.z(), comps.w());
	}

	/** Sets the value of this quaternion to the equivalent rotation
	 * of the Axis-Angle arguments.
	 * The quaternion is normalized after the call to this function.
	 *
	 * @param axisangle the Axis-Angle object.
	 * @since 18.0
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 */
	default void setAxisAngle(AxisAngle axisangle)  {
		assert axisangle != null;
		final var comps = computeWithAxisAngle(axisangle.x, axisangle.y, axisangle.z, axisangle.angle);
		set(comps.x(), comps.y(), comps.z(), comps.w());
	}

	/** Replies the rotation axis-angle represented by this quaternion.
	 *
	 * @return the rotation axis-angle.
	 */
	@Pure
	default RV getAxis() {
		final var axis = getAxisAngle().axis;
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
	 *  @param quaternion the other quaternion
	 *  @param alpha the alpha interpolation parameter
	 */
	default void interpolate(Quaternion<?, ?, ?> quaternion, double alpha) {
		interpolate(this, quaternion, alpha);
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
		final var comps = computeWithEulerAngles(angles.attitude(), angles.bank(), angles.heading(), angles.system());
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
		final var comps = computeWithEulerAngles(attitude, bank, heading, null);
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
		final var comps = computeWithEulerAngles(attitude, bank, heading, system);
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
	UnmodifiableQuaternion<?, ?, ?> toUnmodifiable();

	/**
	 * Returns {@code true} if all of the data members of quaternion are
	 * equal to the corresponding data members in this object.
	 * @param quaternion the quaternion with which the comparison is made
	 * @return true or false
	 */
	@Pure
	default boolean equals(Quaternion<?, ?, ?> quaternion) {
		try {
			return getX() == quaternion.getX()
					&& getY() == quaternion.getY()
					&& getZ() == quaternion.getZ()
					&& getW() == quaternion.getW();
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
		buffer.add("x", Double.valueOf(getX())); //$NON-NLS-1$
		buffer.add("y", Double.valueOf(getY())); //$NON-NLS-1$
		buffer.add("z", Double.valueOf(getZ())); //$NON-NLS-1$
		buffer.add("w", Double.valueOf(getW())); //$NON-NLS-1$
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
	 * Sets the value of the quaternion to the quaternion product of
	 * this quaternion and quaternion (r = this * quaternion).
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param quaternion the other quaternion
	 * @return the result of the multiplication.
	 * @since 18.0
	 * @see #mul(Quaternion)
	 */
	@Pure
	@XtextOperator("*")
	default RQ operator_multiply(Quaternion<?, ?, ?> quaternion) {
		final RQ result = getGeomFactory().newQuaternion(getX(), getY(), getZ(), getW());
		result.mul(quaternion);
		return result;
	}

	/**
	 * Sets the value of the quaternion to the quaternion division of
	 * this quaternion and quaternion (r = this / quaternion).
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param quaternion the other quaternion
	 * @return the result of the division.
	 * @since 18.0
	 * @see #mulInverse(Quaternion)
	 */
	@Pure
	@XtextOperator("/")
	default RQ operator_divide(Quaternion<?, ?, ?> quaternion) {
		final RQ result = getGeomFactory().newQuaternion(getX(), getY(), getZ(), getW());
		result.mulInverse(quaternion);
		return result;
	}

	/**
	 * Replies the inverse (-this) of the current quaternion, i.e., {@code (-x, -y, -z, w)}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @return the inverse of the quaternion.
	 * @since 18.0
	 * @see #inverse()
	 */
	@Pure
	@XtextOperator("(-)")
	default RQ operator_minus() {
		return getGeomFactory().newQuaternion(-getX(), -getY(), -getZ(), getW());
	}

	/**
	 * Sets the value of the quaternion to the quaternion product of
	 * this quaternion and quaternion (r = this * quaternion).
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param quaternion the other quaternion
	 * @return the result of the multiplication.
	 * @since 18.0
	 * @see #mul(Quaternion)
	 */
	@Pure
	@ScalaOperator("*")
	default RQ $times(Quaternion<?, ?, ?> quaternion) {
		return operator_multiply(quaternion);
	}

	/**
	 * Sets the value of the quaternion to the quaternion division of
	 * this quaternion and quaternion (r = this / quaternion).
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param quaternion the other quaternion
	 * @return the result of the division.
	 * @since 18.0
	 * @see #mulInverse(Quaternion)
	 */
	@Pure
	@ScalaOperator("/")
	default RQ $div(Quaternion<?, ?, ?> quaternion) {
		return operator_divide(quaternion);
	}

	/** Replies the inverse (-this) of the current quaternion, i.e., {@code (-x, -y, -z, w)}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @return the inverse of the quaternion.
	 * @since 18.0
	 * @see #inverse()
	 */
	@Pure
	@ScalaOperator("(-)")
	default RQ $minus() {
		return operator_minus();
	}

	/** Replies this quaternion with a Geogebra-compatible form.
	 *
	 * @return the Geogebra representation of the quaternion.
	 * @since 18.0
	 */
	default String toGeogebra() {
		return GeogebraUtil.toTupleDefinition(4, getX(), getY(), getZ(), getW());
	}

	/**
	 * Sets the value of the quaternion to the quaternion product of
	 * this quaternion and quaternion (r = this * quaternion).
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
	 *
	 * @param quaternion the other quaternion
	 * @return the result of the multiplication.
	 * @since 18.0
	 * @see #mul(Quaternion)
	 */
	@Pure
	@GroovyOperator("*")
	default RQ multiply(Quaternion<?, ?, ?> quaternion) {
		return operator_multiply(quaternion);
	}

	/**
	 * Sets the value of the quaternion to the quaternion division of
	 * this quaternion and quaternion (r = this / quaternion).
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
	 *
	 * @param quaternion the other quaternion
	 * @return the result of the division.
	 * @since 18.0
	 * @see #mulInverse(Quaternion)
	 */
	@Pure
	@GroovyOperator("/")
	default RQ div(Quaternion<?, ?, ?> quaternion) {
		return operator_divide(quaternion);
	}

	/**
	 * Replies the inverse (-this) of the current quaternion, i.e., {@code (-x, -y, -z, w)}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
	 *
	 * @return the inverse of the quaternion.
	 * @since 18.0
	 * @see #inverse()
	 */
	@Pure
	@GroovyOperator("(-)")
	default RQ negative() {
		return operator_minus();
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

	/** Tools related to the computation of of Euler Angles.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	final class EulerAnglesTools {

		private static final Creator EULER_ANGLE_XZY_R_CREATOR = (a, b, h) -> new EulerAngles(a, b, h, CoordinateSystem3D.XZY_RIGHT_HAND);

		private static final Creator EULER_ANGLE_XZY_L_CREATOR = (a, b, h) -> new EulerAngles(-a, -b, -h, CoordinateSystem3D.XZY_LEFT_HAND);

		private static final Creator EULER_ANGLE_XYZ_L_CREATOR = (a, b, h) -> new EulerAngles(a, b, h, CoordinateSystem3D.XYZ_LEFT_HAND);

		private static final Creator EULER_ANGLE_XYZ_R_CREATOR = (a, b, h) -> new EulerAngles(-a, -b, -h, CoordinateSystem3D.XYZ_RIGHT_HAND);

		private EulerAnglesTools() {
			//
		}

		private static EulerAngles computeEulerAnglesXZY(double x, double y, double z, double w, Creator creator) {
			// See http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
			// Order of application angles: Heading / Attitude / Bank
			// Heading = rotation about y axis
			// Attitude = rotation about z axis
			// Bank = rotation about x axis
			// Order: YZX
			final var sinAttitude = Math.clamp(2. * (w * z - x * y), -1., 1.);

			// Gimbal lock: attitude = +-90 deg, heading/bank axes align -> only their sum/difference is defined.
			if (Math.abs(sinAttitude) > 0.9999999) {
				final var heading = (sinAttitude > 0. ? -2. : 2.) * Math.atan2(x, w);
				return creator.createEulerAngles(Math.asin(sinAttitude), 0., heading);
			}

			final var heading = Math.atan2(2. * (x * z + w * y), 1. - 2. * (y * y + z * z));
			final var attitude = Math.asin(sinAttitude);
			final var bank = Math.atan2(2. * (w * x + y * z), 1. - 2. * (x * x + z * z));
			return creator.createEulerAngles(attitude, bank, heading);
		}

		private static EulerAngles computeEulerAnglesXYZ(double x, double y, double z, double w, Creator creator) {
			// See http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
			// Coordinate system: X (front), Y (up), Z (right) -> left-handed
			// Order of application angles: Heading / Attitude / Bank
			// Heading = rotation about z axis
			// Attitude = rotation about y axis
			// Bank = rotation about x axis
			// Order: ZYX
			final var sinAttitude = Math.clamp(-2. * (x * z + w * y), -1., 1.);

			// Gimbal lock: attitude = +-90 deg, heading/bank axes align -> only their sum/difference is defined.
			if (Math.abs(sinAttitude) > 0.9999999) {
				final var heading = (sinAttitude > 0. ? 2. : -2.) * Math.atan2(x, w);
				return creator.createEulerAngles(Math.asin(sinAttitude), 0., heading);
			}

			final var heading = Math.atan2(2. * (x * y - w * z), 1. - 2. * (y * y + z * z));
			final var attitude = Math.asin(sinAttitude);
			final var bank = Math.atan2(2. * (y * z - w * x), 1. - 2. * (x * x + y * y));
			return creator.createEulerAngles(attitude, bank, heading);
		}

		/** Convert quaternion to Euler angles assuming the {@link CoordinateSystem3D#XZY_RIGHT_HAND XZY right-handed coordinate system}.
		 *
		 * @param x x coordinate of the quaternion.
		 * @param y y coordinate of the quaternion.
		 * @param z z coordinate of the quaternion.
		 * @param w w coordinate of the quaternion.
		 * @return the Euler angles.
		 */
		public static EulerAngles computeEulerAnglesXZYR(double x, double y, double z, double w) {
			// See http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
			// Order of application angles: Heading / Attitude / Bank
			// Heading = rotation about y axis
			// Attitude = rotation about z axis
			// Bank = rotation about x axis
			// Order: YZX
			return computeEulerAnglesXZY(x, y, z, w, EULER_ANGLE_XZY_R_CREATOR);
		}

		/** Convert quaternion to Euler angles assuming the {@link CoordinateSystem3D#XZY_LEFT_HAND XZY left-handed coordinate system}.
		 *
		 * @param x x coordinate of the quaternion.
		 * @param y y coordinate of the quaternion.
		 * @param z z coordinate of the quaternion.
		 * @param w w coordinate of the quaternion.
		 * @return the Euler angles.
		 */
		public static EulerAngles computeEulerAnglesXZYL(double x, double y, double z, double w) {
			// Coordinate system: X (front), Z (up), Y (left) -> left-handed
			// Order of application angles: Heading / Attitude / Bank
			// Heading = rotation about y axis
			// Attitude = rotation about z axis
			// Bank = rotation about x axis
			// Order: YZX
			// Note: relative to the right-handed variant, flipping the Y axis reverses the sign
			// of all three angles (same underlying quaternion components x, y, z, w).
			return computeEulerAnglesXZY(x, y, z, w, EULER_ANGLE_XZY_L_CREATOR);
		}

		/** Convert quaternion to Euler angles assuming the {@link CoordinateSystem3D#XYZ_LEFT_HAND XYZ left-handed coordinate system}.
		 *
		 * @param x x coordinate of the quaternion.
		 * @param y y coordinate of the quaternion.
		 * @param z z coordinate of the quaternion.
		 * @param w w coordinate of the quaternion.
		 * @return the Euler angles.
		 */
		public static EulerAngles computeEulerAnglesXYZL(double x, double y, double z, double w) {
			// See http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
			// Coordinate system: X (front), Y (up), Z (right) -> left-handed
			// Order of application angles: Heading / Attitude / Bank
			// Heading = rotation about z axis
			// Attitude = rotation about y axis
			// Bank = rotation about x axis
			// Order: ZYX
			return computeEulerAnglesXYZ(x, y, z, w, EULER_ANGLE_XYZ_L_CREATOR);
		}

		/** Convert quaternion to Euler angles assuming the {@link CoordinateSystem3D#XYZ_RIGHT_HAND XYZ right-handed coordinate system}.
		 *
		 * @param x x coordinate of the quaternion.
		 * @param y y coordinate of the quaternion.
		 * @param z z coordinate of the quaternion.
		 * @param w w coordinate of the quaternion.
		 * @return the Euler angles.
		 */
		public static EulerAngles computeEulerAnglesXYZR(double x, double y, double z, double w) {
			// See http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm
			// Coordinate system: X (front), Y (up), Z (right) -> left-handed
			// Order of application angles: Heading / Attitude / Bank
			// Heading = rotation about z axis
			// Attitude = rotation about y axis
			// Bank = rotation about x axis
			// Order: ZYX
			// Note: relative to the left-handed variant, flipping the Z axis reverses the sign
			// of all three angles (same underlying quaternion components x, y, z, w).
			return computeEulerAnglesXYZ(x, y, z, w, EULER_ANGLE_XYZ_R_CREATOR);
		}

		private interface Creator {
			EulerAngles createEulerAngles(double attitude, double bank, double heading);
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
