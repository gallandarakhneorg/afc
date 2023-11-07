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

package org.arakhne.afc.math.geometry.d3.i;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.UnmodifiableQuaternion;
import org.arakhne.afc.math.geometry.d3.Vector3D;
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
public class Quaternion4i implements Quaternion<Point3i, Vector3i, Quaternion4i> {

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
	public Quaternion4i() {
		//
	}

	/** Construct a quaternion with the given components.
	 *
	 * @param x x component.
	 * @param y y component.
	 * @param z z component.
	 * @param w w component.
	 */
	public Quaternion4i(double x, double y, double z, double w) {
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
	public Quaternion4i(Quaternion<?, ?, ?> q) {
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
	public Quaternion4i(Vector3D<?, ?, ?> axis, double angle) {
		setAxisAngle(axis, angle);
	}

	/** Construct a quaternion from an axis-angle representation.
	 *
	 * @param axisangle is the axis and angle of the rotation.
	 */
	public Quaternion4i(AxisAngle axisangle) {
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
	public Quaternion4i(double attitude, double bank, double heading) {
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
	public Quaternion4i(double attitude, double bank, double heading, CoordinateSystem3D system) {
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
	public Quaternion4i(EulerAngles eulerAngles) {
		setEulerAngles(eulerAngles);
	}

	@Pure
	@Override
	public Quaternion4i clone() {
		try {
			return (Quaternion4i)super.clone(); 
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
	public void conjugate(Quaternion<?, ?, ?> q1) {
		this.x = -q1.getX();
		this.y = -q1.getY();
		this.z = -q1.getZ();
		this.w = q1.getW();
	}

	@Pure
	@Override
	public String toString() {
		final JsonBuffer objectDescription = new JsonBuffer();
		toJson(objectDescription);
        return objectDescription.toString();
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add("x", getX()); //$NON-NLS-1$
		buffer.add("y", getY()); //$NON-NLS-1$
		buffer.add("z", getZ()); //$NON-NLS-1$
		buffer.add("w", getW()); //$NON-NLS-1$
	}

	@Override
	public GeomFactory3D<Vector3i, Point3i, Quaternion4i> getGeomFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void conjugate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mul(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mul(Quaternion<?, ?, ?> q) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mulInverse(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mulInverse(Quaternion<?, ?, ?> q) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inverse(Quaternion<?, ?, ?> q) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inverse() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void normalize(Quaternion<?, ?, ?> q) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void normalize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set(double x, double y, double z, double w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void interpolate(Quaternion<?, ?, ?> q, double alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void interpolate(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2, double alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UnmodifiableQuaternion<Point3i, Vector3i, Quaternion4i> toUnmodifiable() {
		// TODO Auto-generated method stub
		return null;
	}

}
