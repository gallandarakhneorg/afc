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

package org.arakhne.afc.math.geometry.d3.d;

import java.lang.ref.SoftReference;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.Triangle3afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

/** A 3D triangle with three points with native coordinates.
 * This trianle is based on the representation built up with 3 points, i.e.
 * 9 double-precision-floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see PointObjectSegment3d
 */
public class Triangle3d extends AbstractShape3d<Triangle3d>
	implements Triangle3afp<Triangle3d, Triangle3d, PathElement3d, Point3d, Vector3d, Quaternion4d, AlignedBox3d> {

	private static final long serialVersionUID = 365901136123606599L;

	private double ax;

	private double ay;

	private double az;

	private double bx;

	private double by;

	private double bz;

	private double cx;

	private double cy;

	private double cz;

	private SoftReference<Vector3d> normal;

	private SoftReference<Quaternion4d> orientation;

	private Point3d pivot;

	/** Construct an empty triangle.
     */
	public Triangle3d() {
		//
	}

	/** Construct a triangle from the three given points.
     * @param p1 first point.
     * @param p2 second point.
     * @param p3 third point.
     */
	public Triangle3d(Point3D<?, ?, ?> p1, Point3D<?, ?, ?> p2, Point3D<?, ?, ?> p3) {
		this(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ());
	}

	/** Constructor by copy.
     * @param triangle the triangle to copy.
     */
	public Triangle3d(Triangle3afp<?, ?, ?, ?, ?, ?, ?> triangle) {
		this(triangle.getX1(), triangle.getY1(), triangle.getZ1(),
				triangle.getX2(), triangle.getY2(), triangle.getZ2(),
				triangle.getX3(), triangle.getY3(), triangle.getZ3());
	}

	/** Construct a triangle from the two given points.
     * @param x1 x coordinate of the first point.
     * @param y1 y coordinate of the first point.
     * @param z1 z coordinate of the first point.
     * @param x2 x coordinate of the second point.
     * @param y2 y coordinate of the second point.
     * @param z2 z coordinate of the second point.
     * @param x3 x coordinate of the third point.
     * @param y3 y coordinate of the third point.
     * @param z3 z coordinate of the third point.
     */
	public Triangle3d(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
		set(x1, y1, z1, x2, y2, z2, x3, y3, z3);
	}

	@Override
	public String toString() {
		return toGeogebra();
	}

	@Override
	public Triangle3d clone() {
		final var clone = super.clone();
		if (clone.pivot != null) {
			this.pivot = clone.pivot.clone();
		}
		return clone;
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		var bits = 1L;
		bits = 31 * bits + Double.hashCode(this.ax);
		bits = 31 * bits + Double.hashCode(this.ay);
		bits = 31 * bits + Double.hashCode(this.az);
		bits = 31 * bits + Double.hashCode(this.bx);
		bits = 31 * bits + Double.hashCode(this.by);
		bits = 31 * bits + Double.hashCode(this.bz);
		bits = 31 * bits + Double.hashCode(this.cx);
		bits = 31 * bits + Double.hashCode(this.cy);
		bits = 31 * bits + Double.hashCode(this.cz);
		return (int) (bits ^ (bits >> 31));
	}

	@Override
	public void setX1(double x) {
		if (this.ax != x) {
			this.ax = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY1(double y) {
		if (this.ay != y) {
			this.ay = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setZ1(double z) {
		if (this.az != z) {
			this.az = z;
			fireGeometryChange();
		}
	}

	@Override
	public void setX2(double x) {
		if (this.bx != x) {
			this.bx = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY2(double y) {
		if (this.by != y) {
			this.by = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setZ2(double z) {
		if (this.bz != z) {
			this.bz = z;
			fireGeometryChange();
		}
	}

	@Override
	public void setX3(double x) {
		if (this.cx != x) {
			this.cx = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY3(double y) {
		if (this.cy != y) {
			this.cy = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setZ3(double z) {
		if (this.cz != z) {
			this.cz = z;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getX1() {
		return this.ax;
	}

	@Pure
	@Override
	public double getY1() {
		return this.ay;
	}

	@Pure
	@Override
	public double getZ1() {
		return this.az;
	}

	@Pure
	@Override
	public double getX2() {
		return this.bx;
	}

	@Pure
	@Override
	public double getY2() {
		return this.by;
	}

	@Pure
	@Override
	public double getZ2() {
		return this.bz;
	}

	@Pure
	@Override
	public double getX3() {
		return this.cx;
	}

	@Pure
	@Override
	public double getY3() {
		return this.cy;
	}

	@Pure
	@Override
	public double getZ3() {
		return this.cz;
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
	public void set(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
		this.ax = x1;
		this.ay = y1;
		this.az = z1;
		this.bx = x2;
		this.by = y2;
		this.bz = z2;
		this.cx = x3;
		this.cy = y3;
		this.cz = z3;
		clearBufferedData();
		fireGeometryChange();
	}

	@Override
	public void setP1(double x, double y, double z) {
		if (this.ax != x || this.ay != y || this.az != z) {
			this.ax = x;
			this.ay = y;
			this.az = z;
			clearBufferedData();
			fireGeometryChange();
		}
	}

	@Override
	public void setP2(double x, double y, double z) {
		if (this.bx != x || this.by != y || this.bz != z) {
			this.bx = x;
			this.by = y;
			this.bz = z;
			clearBufferedData();
			fireGeometryChange();
		}
	}

	@Override
	public void setP3(double x, double y, double z) {
		if (this.cx != x || this.cy != y || this.cz != z) {
			this.cx = x;
			this.cy = y;
			this.cz = z;
			clearBufferedData();
			fireGeometryChange();
		}
	}

	/** Clear any buffered data.
	 * By default, the normal and the orientation are bufferred.
	 */
	protected void clearBufferedData() {
		this.normal = null;
		this.orientation = null;
	}

	@Override
	public Vector3d getNormal() {
		final var vref = this.normal;
		Vector3d v = null;
		if (vref != null) {
			v = vref.get();
		}
		if (v == null) {
			v = getGeomFactory().newVector();
			Vector3D.crossProduct(
					getX2() - getX1(),
					getY2() - getY1(),
					getZ2() - getZ1(),
					getX3() - getX1(),
					getY3() - getY1(),
					getZ3() - getZ1(),
					v);
			v.normalize();
			this.normal = new SoftReference<>(v);
		}
		return v;
	}

	@Override
	public Quaternion4d getOrientation() {
		final var vref = this.orientation;
		Quaternion4d orient = null;
		if (vref != null) {
			orient = vref.get();
		}
		if (orient == null) {
			final var cs = CoordinateSystem3D.getDefaultCoordinateSystem();
			assert cs != null;
			orient = computeOrientation(cs);
			this.orientation = new SoftReference<>(orient);
		}
		return orient;
	}

	private Quaternion4d computeOrientation(CoordinateSystem3D system) {
		final var norm = getNormal();
		assert system != null : AssertMessages.notNullParameter();
		final var up = system.getUpVector();
		final var axis = new InnerComputationVector3D();
		Vector3D.crossProduct(
				up.getX(), up.getY(), up.getZ(),
				norm.getX(), norm.getY(), norm.getZ(),
				system, axis);
		axis.normalize();
		return getGeomFactory().newQuaternionFromAxisAngle(
				axis.getX(), axis.getY(), axis.getZ(),
				Vector3D.signedAngle(
						up.getX(), up.getY(), up.getZ(),
						norm.getX(), norm.getY(), norm.getZ()));
	}

	@Override
	public void setOrientationFromCoordinateSystem(CoordinateSystem3D system) {
		assert system != null : AssertMessages.notNullParameter();
		final var orient = computeOrientation(system);
		this.orientation = new SoftReference<>(orient);
	}

	@Override
	public Point3D<?, ?, ?> getPivot() {
		return this.pivot;
	}

	@Override
	public void setPivot(double x, double y, double z) {
		final var p = this.pivot;
		if (p == null) {
			this.pivot = getGeomFactory().newPoint(x, y, z);
		} else {
			p.set(x, y, z);
		}
	}

	@Override
	public void setPivot(Point3D<?, ?, ?> point) {
		if (point == null) {
			this.pivot = null;
		} else {
			setPivot(point.getX(), point.getY(), point.getZ());
		}
	}

}
