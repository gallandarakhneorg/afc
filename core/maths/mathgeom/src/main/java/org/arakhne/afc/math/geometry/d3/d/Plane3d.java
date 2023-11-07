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

import java.lang.ref.SoftReference;

import org.arakhne.afc.math.geometry.d3.Plane3D;
import org.arakhne.afc.math.geometry.d3.PlaneClassification;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.Plane3afp;

/** This class represents a general 3D plane with double floating point coordinates.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public class Plane3d extends AbstractPlane3d<Plane3d>
implements Plane3afp<Plane3d, Segment3d, Point3d, Vector3d, Quaternion4d> {

	private static final long serialVersionUID = 3182088871450640594L;

	/** Equation coefficient A.
	 */
	private double a;

	/** Equation coefficient B.
	 */
	private double b;

	/** equation coefficient C.
	 */
	private double c;

	/** equation coefficient D.
	 */
	private double d;

	/** Cached pivot point.
	 */
	private transient SoftReference<Point3d> cachedPivot = null;

	/** Construct a plane with origin as point and with {@code (1, 0, 0)} as normal.
	 */
	public Plane3d() {
		this.a = 1;
		this.b = 0;
		this.c = 0;
		this.d = 0;
	}

	/** Construct a plane with the given equation coefficients.
	 *
	 * @param a is the plane equation coefficient a
	 * @param b is the plane equation coefficient b
	 * @param c is the plane equation coefficient c
	 * @param d is the plane equation coefficient d
	 */
	public Plane3d(double a, double b, double c, double d) {
		final double length = Math.sqrt(a * a + b * b + c * c);
		this.a = a / length;
		this.b = b / length;
		this.c = c / length;
		this.d = d;
	}

	/** Construct a plane with the equation coefficients fro mthe given plane.
	 *
	 * @param plane is the plane to copy.
	 */
	public Plane3d(Plane3D<?, ?, ?, ?, ?> plane) {
		this(
				plane.getEquationComponentA(),
				plane.getEquationComponentB(),
				plane.getEquationComponentC(),
				plane.getEquationComponentD());
	}

	/** Construct a plane with the given normal and pivot point.
	 *
	 * @param normalx is the x coordinate of the normal of the plane.
	 * @param normaly is the y coordinate of the normal of the plane.
	 * @param normalz is the z coordinate of the normal of the plane.
	 * @param pivotx is the x coordinate of the pivot point that is in the plane.
	 * @param pivoty is the y coordinate of the pivot point that is in the plane.
	 * @param pivotz is the z coordinate of the pivot point that is in the plane.
	 */
	public Plane3d(double normalx, double normaly, double normalz, double pivotx, double pivoty, double pivotz) {
		final double length = Math.sqrt(normalx * normalx + normaly * normaly + normalz * normalz);
		this.a = normalx / length;
		this.b = normaly / length;
		this.c = normalz / length;
		// a.x + b.y + c.z + d = 0
		// where (x,y,z) is the translation point
		this.d = -(this.a * pivotx + this.b * pivoty + this.c * pivotz);
		setPivot(pivotx, pivoty, pivotz);
	}

	/** Construct a plane with the given normal and pivot point.
	 *
	 * @param normal is the normal of the plane.
	 * @param pivot is a point which is on the plane.
	 */
	public Plane3d(Vector3D<?, ?, ?> normal, Point3D<?, ?, ?> pivot) {
		this(normal.getX(), normal.getY(), normal.getZ(), pivot.getX(), pivot.getY(), pivot.getZ());
	}

	/** Construct a plane with the given normal and pivot point.
	 * 
	 * @param p1x is x coordinate the first point on the plane.
	 * @param p1y is y coordinate the first point on the plane.
	 * @param p1z is z coordinate the first point on the plane.
	 * @param p2x is x coordinate the second point on the plane.
	 * @param p2y is y coordinate the second point on the plane.
	 * @param p2z is z coordinate the second point on the plane.
	 * @param p3x is x coordinate the third point on the plane.
	 * @param p3y is y coordinate the third point on the plane.
	 * @param p3z is z coordinate the third point on the plane.
	 */
	public Plane3d(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y, double p3z) {
		set(p1x, p1y, p1z, p2x, p2y, p2z, p3x, p3y, p3z);
	}

	/** Construct a plane with the given normal and pivot point.
	 * 
	 * @param p1 the first point on the plane.
	 * @param p2 the second point on the plane.
	 * @param p3 the third point on the plane.
	 */
	public Plane3d(Point3D<?, ?, ?> p1, Point3D<?, ?, ?> p2, Point3D<?, ?, ?> p3) {
		this(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ());
	}

	@Override
	public double getEquationComponentA() {
		return this.a;
	}

	@Override
	public double getEquationComponentB() {
		return this.b;
	}

	@Override
	public double getEquationComponentC() {
		return this.c;
	}

	@Override
	public double getEquationComponentD() {
		return this.d;
	}

	@Override
	public double getNormalX() {
		return this.a;
	}

	@Override
	public double getNormalY() {
		return this.b;
	}

	@Override
	public double getNormalZ() {
		return this.c;
	}

	/** Clear buffered values.
	 */
	protected void clearBufferedValues() {
		this.cachedPivot = null;
	}

	@Override
	public void setPivot(double x, double y, double z) {
		final Point3d pivot = getGeomFactory().newPoint(x, y, z);
		// a x + b y + c z + d = 0
		// a x + b y + c z = -d
		this.d = -(this.a * x + this.b * y + this.c * z);
		this.cachedPivot = new SoftReference<>(pivot);
		fireGeometryChange();
	}

	@Override
	public void set(double a, double b, double c, double d) {
		final double length = Math.sqrt(a * a + b * b + c * c);
		this.a = a / length;
		this.b = b / length;
		this.c = c / length;
		this.d = d;
		clearBufferedValues();
		fireGeometryChange();
	}

	@Override
	public Plane3d normalize() {
		final double length = Math.sqrt(this.a * this.a + this.b * this.b + this.c * this.c);
		this.a = this.a / length;
		this.b = this.b / length;
		this.c = this.c / length;
		clearBufferedValues();
		fireGeometryChange();
		return this;
	}

	@Override
	public Point3d getPivot() {
		Point3d pivot = this.cachedPivot == null ? null : this.cachedPivot.get();
		if (pivot == null) {
			pivot = getProjection(0., 0., 0.);
			this.cachedPivot = new SoftReference<>(pivot);
		}
		return pivot;
	}

	@Override
	public void translate(double x, double y, double z) {
		final Point3d refPoint = getPivot();
		final double nx = refPoint.getX() + x;
		final double ny = refPoint.getY() + y;
		final double nz = refPoint.getZ() + z;
		setPivot(nx, ny, nz);
	}

	@Override
	public void translate(double distance) {
		this.d += distance;
		clearBufferedValues();
		fireGeometryChange();
	}

	@Override
	public PlaneClassification classifies(Plane3D<?, ?, ?, ?, ?> otherPlane) {
		// TODO Auto-generated method stub
		return null;
	}

}
