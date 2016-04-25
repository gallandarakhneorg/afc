/* 
 * $Id$
 * 
 * Copyright (C) 2010-2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d3;

import java.io.Serializable;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.arakhne.afc.math.matrix.Matrix4f;
import org.eclipse.xtext.xbase.lib.Pure;

/** A 4 element unit quaternion represented by x,y,z,w coordinates. 
 * The quaternion is always normalized.
 * 
 * <h3>Other Rotation Representations</h3>
 * 
 * Other representations of an rotation are available from this class:
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
 * <p>
 * There is no single set of conventions and standards in this area, 
 * therefore the following conventions was choosen:<ul>
 * <li>angle applied first:	heading;</li>
 * <li>angle applied second: attitude;</li>
 * <li>angle applied last: bank</li>
 * </ul>
 * <p>
 * Examples: NASA aircraft standard and telescope standard
 * <img src="doc-files/euler_plane.gif" alt="[NASA Aircraft Standard]">
 * <img src="doc-files/euler_telescop.gif" alt="[Telescope Standard]">
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Quaternion extends Cloneable, Serializable {

	/** Replies the X coordinate.
	 * 
	 * @return x
	 */
	@Pure
	double getX();

	/** Set the X coordinate.
	 * 
	 * @param x1
	 */
	void setX(double x1);

	/** Replies the Y coordinate.
	 * 
	 * @return y
	 */
	@Pure
	double getY();

	/** Set the Y coordinate.
	 * 
	 * @param y1
	 */
	void setY(double y1);

	/** Replies the Z coordinate.
	 * 
	 * @return z
	 */
	@Pure
	double getZ();

	/** Set the Z coordinate.
	 * 
	 * @param z1
	 */
	void setZ(double z1);

	/** Replies the W coordinate.
	 * 
	 * @return w
	 */
	@Pure
	double getW();

	/** Set the W coordinate.
	 * 
	 * @param w1
	 */
	void setW(double w1);

	/**
	 * Returns true if the L-infinite distance between this tuple
	 * and tuple t1 is less than or equal to the epsilon parameter, 
	 * otherwise returns false.  The L-infinite
	 * distance is equal to MAX[abs(x1-x2), abs(y1-y2)]. 
	 * @param t1  the tuple to be compared to this tuple
	 * @param epsilon  the threshold value  
	 * @return  true or false
	 */
	@Pure
	boolean epsilonEquals(Quaternion t1, double epsilon);

	/**
	 * Sets the value of this quaternion to the conjugate of quaternion q1.
	 * @param q1 the source vector
	 */
	void conjugate(Quaternion q1);

	/**
	 * Sets the value of this quaternion to the conjugate of itself.
	 */
	void conjugate();

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * quaternions q1 and q2 (this = q1 * q2).  
	 * Note that this is safe for aliasing (e.g. this can be q1 or q2).
	 * @param q1 the first quaternion
	 * @param q2 the second quaternion
	 */
	void mul(Quaternion q1, Quaternion q2);

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * itself and q1 (this = this * q1).  
	 * @param q1 the other quaternion
	 */
	void mul(Quaternion q1);

	/** 
	 * Multiplies quaternion q1 by the inverse of quaternion q2 and places
	 * the value into this quaternion.  The value of both argument quaternions 
	 * is preservered (this = q1 * q2^-1).
	 * @param q1 the first quaternion
	 * @param q2 the second quaternion
	 */ 
	void mulInverse(Quaternion q1, Quaternion q2);

	/**
	 * Multiplies this quaternion by the inverse of quaternion q1 and places
	 * the value into this quaternion.  The value of the argument quaternion
	 * is preserved (this = this * q^-1).
	 * @param q1 the other quaternion
	 */
	void mulInverse(Quaternion q1);
	
	/**
	 * Sets the value of this quaternion to quaternion inverse of quaternion q1.
	 * @param q1 the quaternion to be inverted
	 */
	void inverse(Quaternion q1);

	/**
	 * Sets the value of this quaternion to the quaternion inverse of itself.
	 */
	void inverse();

	/**
	 * Sets the value of this quaternion to the normalized value
	 * of quaternion q1.
	 * @param q1 the quaternion to be normalized.
	 */
	void normalize(Quaternion q1);
	
	/**
	 * Normalizes the value of this quaternion in place.
	 */
	void normalize();

	/**
	 * Sets the value of this quaternion to the rotational component of
	 * the passed matrix.
	 * @param m1 the Matrix4f
	 */
	void setFromMatrix(Matrix4f m1);

	/**
	 * Sets the value of this quaternion to the rotational component of
	 * the passed matrix.
	 * @param m1 the Matrix3f
	 */
	void setFromMatrix(Matrix3f m1);

	/** Set the quaternion coordinates.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param w1
	 */
	void set(double x1, double y1, double z1, double w1);

	/** Set the quaternion coordinates.
	 * 
	 * @param q
	 */
	void set(Quaternion q);

	/**
	 * Sets the value of this quaternion to the equivalent rotation
	 * of the Axis-Angle arguments.
	 * @param axis is the axis of rotation.
	 * @param angle is the rotation around the axis.
	 */
	void setAxisAngle(Vector3D axis, double angle);

	/**
	 * Sets the value of this quaternion to the equivalent rotation
	 * of the Axis-Angle arguments.
	 * @param x1 is the x coordinate of the rotation axis
	 * @param y1 is the y coordinate of the rotation axis
	 * @param z1 is the z coordinate of the rotation axis
	 * @param angle is the rotation around the axis.
	 */
	void setAxisAngle(double x1, double y1, double z1, double angle);

	/** Replies the rotation axis-angle represented by this quaternion.
	 * 
	 * @return the rotation axis-angle.
	 */
	@Pure
	Vector3D getAxis();

	/** Replies the rotation angle represented by this quaternion.
	 * 
	 * @return the rotation axis
	 * @see #setAxisAngle(Vector3D, double)
	 * @see #setAxisAngle(double, double, double, double)
	 * @see #getAxis()
	 */
	@Pure
	double getAngle();

	/** Replies the rotation axis represented by this quaternion.
	 * 
	 * @return the rotation axis
	 * @see #setAxisAngle(Vector3D, double)
	 * @see #setAxisAngle(double, double, double, double)
	 * @see #getAngle()
	 */
	@SuppressWarnings("synthetic-access")
	@Pure
	AxisAngle getAxisAngle();

	/**
	 *  Performs a great circle interpolation between this quaternion
	 *  and the quaternion parameter and places the result into this
	 *  quaternion.
	 *  @param q1  the other quaternion
	 *  @param alpha  the alpha interpolation parameter
	 */
	void interpolate(Quaternion q1, double alpha);

	/** 
	 *  Performs a great circle interpolation between quaternion q1
	 *  and quaternion q2 and places the result into this quaternion. 
	 *  @param q1  the first quaternion
	 *  @param q2  the second quaternion
	 *  @param alpha  the alpha interpolation parameter 
	 */   
	void interpolate(Quaternion q1, Quaternion q2, double alpha);
	
	/** Set the quaternion with the Euler angles.
	 *
	 * @param angles the Euler angles.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 */
	@SuppressWarnings("synthetic-access")
	void setEulerAngles(EulerAngles angles);

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
	void setEulerAngles(double attitude, double bank, double heading);

	/** Set the quaternion with the Euler angles.
	 *
	 * @param attitude is the rotation around left vector.
	 * @param bank is the rotation around front vector.
	 * @param heading is the rotation around top vector.
	 * @param system the coordinate system to use for applying the Euler angles.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 */
	void setEulerAngles(double attitude, double bank, double heading, CoordinateSystem3D system);

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
	EulerAngles getEulerAngles();

	/**
	 * Replies the Euler's angles that corresponds to the quaternion.
	 * 
	 * @param system is the coordinate system used to define the up,left and front vectors.
	 * @return the heading, attitude and bank angles.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm">Quaternion to Euler</a>
	 */
	@Pure
	EulerAngles getEulerAngles(CoordinateSystem3D system);
	
	/** A representation of Euler Angles.
	 * The term "Euler Angle" is used for any representation of 3 dimensional 
	 * rotations where the rotation is decomposed into 3 separate angles. 
	 * <p>
	 * There is no single set of conventions and standards in this area, 
	 * therefore the following conventions was choosen:<ul>
	 * <li>angle applied first:	heading;</li>
	 * <li>angle applied second: attitude;</li>
	 * <li>angle applied last: bank</li>
	 * </ul>
	 * <p>
	 * Examples: NASA aircraft standard and telescope standard
	 * <img src="doc-files/euler_plane.gif" alt="[NASA Aircraft Standard]">
	 * <img src="doc-files/euler_telescop.gif" alt="[Telescope Standard]">
	 * 
	 * <strong>For creating an instance of this class, you must invoke
	 * {@link Quaternion#getEulerAngles(CoordinateSystem3D)}. 
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	static final class EulerAngles implements Cloneable, Serializable {

		private static final long serialVersionUID = -1532832128836084395L;

		private final double attitude;
		private final double bank;
		private final double heading;
		private final CoordinateSystem3D system;

		private EulerAngles(double attitude1, double bank1, double heading1, CoordinateSystem3D system1) {
			this.attitude = attitude1;
			this.bank = bank1;
			this.heading = heading1;
			this.system = system1;
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
		private CoordinateSystem3D getSystem() {
			return this.system;
		}

	}

	/** A representation of axis-angle.
	 * The axis–angle representation of a rotation parameterizes a rotation in a three-dimensional
	 * Euclidean space by two values: a unit vector, indicating the direction of an axis of rotation, and
	 * an angle describing the magnitude of the rotation about the axis.
	 * The rotation occurs in the sense prescribed by the (left/right)-hand rule.
	 * <img src="doc-files/axis_angle.png" alt="[Axis-Angle Representation]">
	 * 
	 * <strong>For creating an instance of this class, you must invoke
	 * {@link Quaternion#getAxisAngle()}. 
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static final class AxisAngle implements Cloneable, Serializable {

		private static final long serialVersionUID = -7228694369177792159L;
		
		private final double x;
		private final double y;
		private final double z;
		private final double angle;

		private AxisAngle(double x1, double y1, double z1, double angle1) {
			this.x = x1;
			this.y = y1;
			this.z = z1;
			this.angle = angle1;
		}

		/** Replies the rotation axis.
		 *
		 * @return the rotation axis.
		 */
		@Pure
		public Vector3D getAxis() {
			return null;
			//TODO:	return new Vector3f(this.x, this.y, this.z);
		}

		/** Replies the rotation angle.
		 *
		 * @return the rotation angle.
		 */
		@Pure
		public double getAngle() {
			return this.angle;
		}

	}

}