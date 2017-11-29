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

package org.arakhne.afc.math.geometry.d3.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 3D Quaternion with 4 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class Quaternion4d implements Quaternion {

	private static final long serialVersionUID = -4945695526525762784L;

	private double x;

	private double y;

	private double z;

	private double w;

	/** Construct a zero point.
     */
	public Quaternion4d() {
		//
	}

	/** Constructor.
	 * @param quaternion the quaternion to copy.
	 */
	public Quaternion4d(Quaternion quaternion) {
		this(quaternion.getX(), quaternion.getY(), quaternion.getZ(), quaternion.getW());
	}

	/** Constructor.
	 * @param axisAngle the axis-angle to copy.
	 */
	public Quaternion4d(Quaternion.AxisAngle axisAngle) {
		assert axisAngle != null : AssertMessages.notNullParameter();
		setAxisAngle(axisAngle.getAxis(), axisAngle.getAngle());
	}

	/** Constructor.
	 * @param angles the Euler angles to copy.
	 */
	public Quaternion4d(Quaternion.EulerAngles angles) {
		assert angles != null : AssertMessages.notNullParameter();
		setEulerAngles(angles);
	}

	/** Constructor.
	 * @param tuple is the tuple to copy.
	 */
	public Quaternion4d(int[] tuple) {
		this((double) tuple[0], (double) tuple[1], (double) tuple[2], (double) tuple[3]);
	}

	/** Constructor.
	 * @param tuple is the tuple to copy.
	 */
	public Quaternion4d(double[] tuple) {
		this(tuple[0], tuple[1], tuple[2], tuple[3]);
	}

	/** Construct a quaternion with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     * @param w w coordinate.
     */
	public Quaternion4d(int x, int y, int z, int w) {
		this((double) x, (double) y, (double) z, (double) w);
	}

	/** Construct a quaternion with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     * @param w w coordinate.
     */
	public Quaternion4d(float x, float y, float z, float w) {
		this((double) x, (double) y, (double) z, (double) w);
	}

	/** Construct a quaternion with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     * @param w w coordinate.
     */
	public Quaternion4d(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/** Construct a quaternion with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     * @param w w coordinate.
     */
	public Quaternion4d(long x, long y, long z, long w) {
		this((double) x, (double) y, (double) z, (double) w);
	}

	/** Create a quaternion from the axis-angle.
	 *
	 * @param x the x coordinate of the axis.
	 * @param y the y coordinate of the axis.
	 * @param z the z coordinate of the axis.
	 * @param angle the rotation angle.
	 * @return the quaternion.
	 */
	public static Quaternion4d newAxisAngle(double x, double y, double z, double angle) {
		final Quaternion4d quat = new Quaternion4d();
		quat.setAxisAngle(x, y, z, angle);
		return quat;
	}

	/** Create a quaternion from the Euler angles.
	 *
	 * @param attitude the attitude angle.
	 * @param bank the bank angle.
	 * @param heading the heading angle.
	 * @param system the coordinate system that indicate the up direction.
	 * @return the quaternion.
	 */
	public static Quaternion4d newEulerAngles(double attitude, double bank, double heading,
			CoordinateSystem3D system) {
		final Quaternion4d quat = new Quaternion4d();
		quat.setEulerAngles(attitude, bank, heading, system);
		return quat;
	}

	@Pure
	@Override
	public Quaternion4d clone() {
		try {
			return (Quaternion4d) super.clone();
        } catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Pure
	@Override
	public boolean equals(Object t1) {
		try {
			return equals((Quaternion) t1);
        } catch (AssertionError e) {
			throw e;
		} catch (Throwable e2) {
			return false;
		}
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(this.x);
		bits = 31 * bits + Double.hashCode(this.y);
		bits = 31 * bits + Double.hashCode(this.z);
		bits = 31 * bits + Double.hashCode(this.w);
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public String toString() {
		final JsonBuffer objectDescription = new JsonBuffer();
		toJson(objectDescription);
        return objectDescription.toString();
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public int ix() {
		return (int) Math.round(this.x);
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public int iy() {
		return (int) Math.round(this.y);
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public double getZ() {
		return this.z;
	}

	@Override
	public int iz() {
		return (int) Math.round(this.z);
	}

	@Override
	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public double getW() {
		return this.w;
	}

	@Override
	public int iw() {
		return (int) Math.round(this.w);
	}

	@Override
	public void setW(int w) {
		this.w = w;
	}

	@Override
	public void setW(double w) {
		this.w = w;
	}

}
