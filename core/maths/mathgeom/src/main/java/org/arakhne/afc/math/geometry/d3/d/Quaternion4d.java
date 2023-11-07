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

package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.UnmodifiableQuaternion;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.InnerComputationQuaternionafp;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.eclipse.xtext.xbase.lib.Pure;

/** Quaternion with floating point coordinates.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public class Quaternion4d implements Quaternion<Point3d, Vector3d, Quaternion4d> {

	private static final long serialVersionUID = -630119828504693039L;

	/** x coordinate.
	 */
	double x;

	/** y coordinate.
	 */
	double y;

	/** z coordinate.
	 */
	double z;

	/** w coordinate.
	 */
	double w;

	/** Construct a zero quaternion.
	 */
	public Quaternion4d() {
		//
	}

	/** Construct a quaternion with the normalization of the given components.
	 *
	 * @param x x component.
	 * @param y y component.
	 * @param z z component.
	 * @param w w component.
	 */
	public Quaternion4d(double x, double y, double z, double w) {
		final double mag = 1. / Math.sqrt(x * x + y * y + z * z + w * w);
		this.x = x * mag;
		this.y = y * mag;
		this.z = z * mag;
		this.w = w * mag;
	}

	/** Construct a quaternion with the components of the given quaternion.
	 *
	 * @param q the quaternion to copy.
	 */
	public Quaternion4d(Quaternion<?, ?, ?> q) {
		this.x = q.getX();
		this.y = q.getY();
		this.z = q.getZ();
		this.w = q.getW();
	}

	/** Construct a quaternion from an axis-angle representation.
	 *
	 * @param axis is the axis of the rotation.
	 * @param angle is the rotation angle around the axis.
	 */
	public Quaternion4d(Vector3D<?, ?, ?> axis, double angle) {
		setAxisAngle(axis, angle);
	}

	/** Construct a quaternion from an axis-angle representation.
	 *
	 * @param axisangle is the axis and angle of the rotation.
	 */
	public Quaternion4d(AxisAngle axisangle) {
		setAxisAngle(axisangle);
	}

	/** Construct a quaternion from Euler angles.
	 * The {@link CoordinateSystem3D#getDefaultCoordinateSystem() default coordinate system}
	 * is used from applying the Euler angles.
	 *
	 * @param attitude is the rotation around left vector.
	 * @param bank is the rotation around front vector.
	 * @param heading is the rotation around top vector.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 */
	public Quaternion4d(double attitude, double bank, double heading) {
		this(attitude, bank, heading, null);
	}

	/** Construct a quaternion from Euler angles.
	 *
	 * @param attitude is the rotation around left vector.
	 * @param bank is the rotation around front vector.
	 * @param heading is the rotation around top vector.
	 * @param system the coordinate system to use for applying the Euler angles.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 */
	public Quaternion4d(double attitude, double bank, double heading, CoordinateSystem3D system) {
		setEulerAngles(attitude, bank, heading, system);
	}

	/** Construct a quaternion from Euler angles.
	 * The {@link CoordinateSystem3D#getDefaultCoordinateSystem() default coordinate system}
	 * is used from applying the Euler angles.
	 *
	 * @param eulerAngles the three Euler angles.
	 * @see <a href="http://en.wikipedia.org/wiki/Euler_angles">Euler Angles</a>
	 * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euler to Quaternion</a>
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 */
	public Quaternion4d(EulerAngles eulerAngles) {
		setEulerAngles(eulerAngles);
	}

	@Pure
	@Override
	public Quaternion4d clone() {
		try {
			return (Quaternion4d)super.clone(); 
		}
		catch(CloneNotSupportedException e) {   
			throw new Error(e);
		}
	}

	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(this.x);
		bits = 31 * bits + Double.hashCode(this.y);
		bits = 31 * bits + Double.hashCode(this.z);
		bits = 31 * bits + Double.hashCode(this.w);
		return bits ^ (bits >> 31);
	}

	@Override
	public void set(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public int ix() {
		return (int) this.x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public int iy() {
		return (int) this.y;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public double getZ() {
		return this.z;
	}

	@Override
	public int iz() {
		return (int) this.z;
	}

	@Override
	public void setZ(double z) {
		this.z=  z;
	}

	@Override
	public void setZ(int z) {
		this.z=  z;
	}

	@Override
	public double getW() {
		return this.w;
	}

	@Override
	public int iw() {
		return (int) this.w;
	}

	@Override
	public void setW(double w) {
		this.w = w;
	}

	@Override
	public void setW(int w) {
		this.w = w;
	}

	@Override
	public void normalize(Quaternion<?, ?, ?> q) {
		final double x = q.getX();
		final double y = q.getY();
		final double z = q.getZ();
		final double w = q.getW();
		double norm = x * x + y * y + z * z + w * w;
		if (norm > 0.) {
			norm = 1. / Math.sqrt(norm);
			this.x = x * norm;
			this.y = y * norm;
			this.z = z * norm;
			this.w = w * norm;
		} else {
			this.x = 0.;
			this.y = 0.;
			this.z = 0.;
			this.w = 0.;
		}
	}

	@Override
	public void normalize() {
		double norm = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
		if (norm > 0.) {
			norm = 1. / Math.sqrt(norm);
			this.x *= norm;
			this.y *= norm;
			this.z *= norm;
			this.w *= norm;
		} else {
			this.x = 0.;
			this.y = 0.;
			this.z = 0.;
			this.w = 0.;
		}
	}

	@Override
	public void inverse(Quaternion<?, ?, ?> q) {
		final double x = q.getX();
		final double y = q.getY();
		final double z = q.getZ();
		final double w = q.getW();
		final double norm = 1. / (w * w + x * x + y * y + z * z);
		this.w =  norm * w;
		this.x = -norm * x;
		this.y = -norm * y;
		this.z = -norm * z;
	}

	@Override
	public void conjugate(Quaternion<?, ?, ?> q) {
		final double x = q.getX();
		final double y = q.getY();
		final double z = q.getZ();
		final double w = q.getW();
		this.x = -x;
		this.y = -y;
		this.z = -z;
		this.w = w;
	}

	@Pure
	@Override
	public String toString() {
		final JsonBuffer objectDescription = new JsonBuffer();
		toJson(objectDescription);
		return objectDescription.toString();
	}

	@Override
	public GeomFactory3D<Vector3d, Point3d, Quaternion4d> getGeomFactory() {
		return GeomFactory3d.SINGLETON;
	}

	@Override
	public void mul(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2) {
		final double x1 = q1.getX();
		final double y1 = q1.getY();
		final double z1 = q1.getZ();
		final double w1 = q1.getW();
		final double x2 = q2.getX();
		final double y2 = q2.getY();
		final double z2 = q2.getZ();
		final double w2 = q2.getW();
		this.w = w1 * w2 - x1 * x2 - y1 * y2 - z1 * z2;
		this.x = w1 * x2 + w2 * x1 + y1 * z2 - z1 * y2;
		this.y = w1 * y2 + w2 * y1 - x1 * z2 + z1 * x2;
		this.z = w1 * z2 + w2 * z1 + x1 * y2 - y1 * x2;
	}

	@Override
	public void mulInverse(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2) {
		final InnerComputationQuaternionafp tempQuat = new InnerComputationQuaternionafp(
				q2.getX(), q2.getY(), q2.getZ(), q2.getW());  
		tempQuat.inverse(); 
		mul(q1, tempQuat);
	}

	@Override
	public void interpolate(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2, double alpha) {
		double x1 = q1.getX();
		double y1 = q1.getY();
		double z1 = q1.getZ();
		double w1 = q1.getW();
		final double x2 = q2.getX();
		final double y2 = q2.getY();
		final double z2 = q2.getZ();
		final double w2 = q2.getW();

		// From "Advanced Animation and Rendering Techniques"
		// by Watt and Watt pg. 364, function as implemented appeared to be 
		// incorrect.  Fails to choose the same quaternion for the double
		// covering. Resulting in change of direction for rotations.
		// Fixed function to negate the first quaternion in the case that the
		// dot product of q1 and this is negative. Second case was not needed. 

		double dot = x2 * x1 + y2 * y1 + z2 * z1 + w2 * w1;

		if (dot < 0.) {
			// negate quaternion
			x1 = -x1;
			y1 = -y1;
			z1 = -z1;
			w1 = -w1;
			dot = -dot;
		}

		final double s1;
		final double s2;
		if ((1. - dot) > EPS) {
			final double om = Math.acos(dot);
			final double sinom = Math.sin(om);
			s1 = Math.sin((1. - alpha) * om) / sinom;
			s2 = Math.sin(alpha * om) / sinom;
		} else {
			s1 = 1. - alpha;
			s2 = alpha;
		}
		this.w = s1 * w1 + s2 * w2;
		this.x = s1 * x1 + s2 * x2;
		this.y = s1 * y1 + s2 * y2;
		this.z = s1 * z1 + s2 * z2;
	}

	@Override
	public UnmodifiableQuaternion<Point3d, Vector3d, Quaternion4d> toUnmodifiable() {
		return new UnmodifiableQuaternion<>() {
			private static final long serialVersionUID = 8206237294816074755L;

			@Override
			public double getX() {
				return Quaternion4d.this.getX();
			}

			@Override
			public int ix() {
				return Quaternion4d.this.ix();
			}

			@Override
			public double getY() {
				return Quaternion4d.this.getY();
			}

			@Override
			public int iy() {
				return Quaternion4d.this.iy();
			}

			@Override
			public double getZ() {
				return Quaternion4d.this.getZ();
			}

			@Override
			public int iz() {
				return Quaternion4d.this.iz();
			}

			@Override
			public double getW() {
				return Quaternion4d.this.getW();
			}

			@Override
			public int iw() {
				return Quaternion4d.this.iw();
			}

			@Override
			public GeomFactory3D<Vector3d, Point3d, Quaternion4d> getGeomFactory() {
				return Quaternion4d.this.getGeomFactory();
			}
		};
	}

}
