/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

package org.arakhne.afc.math.geometry3d.continuous;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Matrix4f;
import org.arakhne.afc.math.geometry.ClassifierUtil;
import org.arakhne.afc.math.geometry.IntersectionType;
import org.arakhne.afc.math.geometry.IntersectionUtil;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.geometry.continuous.object2d.bounds.BoundingCircle;
import org.arakhne.afc.math.geometry.continuous.object2d.bounds.Bounds2D;
import org.arakhne.afc.math.geometry.continuous.object4d.AxisAngle4f;
import org.arakhne.afc.math.geometry.system.CoordinateSystem3D;
import org.arakhne.afc.math.geometry2d.continuous.OrientedRectangle2f;
import org.arakhne.afc.math.geometry2d.continuous.Point2f;
import org.arakhne.afc.math.geometry2d.continuous.Vector2f;
import org.arakhne.afc.sizediterator.SizedIterator;
import org.arakhne.afc.util.ArrayUtil;
import org.arakhne.afc.util.CollectionUtil;

/**
 * A bounding capsule is a swept sphere (i.e. the volume that a sphere takes as it moves along a straight line segment) containing the object. Capsules can be represented by the radius of the swept sphere and the segment that the sphere is swept across). It has traits similar to a cylinder, but is easier to use, because the intersection test is simpler. A capsule and another object intersect if the distance between the capsule's defining segment and some feature of the other object is smaller than the capsule's radius. For example, two capsules intersect if the distance
 * between the capsules' segments is smaller than the sum of their radii. This holds for arbitrarily rotated capsules, which is why they're more appealing than cylinders in practice.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Capsule extends AbstractCombinableBounds3D implements TranslatableBounds3D, RotatableBounds3D, OrientedCombinableBounds3D {

	private static final long serialVersionUID = -9007714495958355907L;

	/**
	 * Medial line segment start point, it is also the lower point of the capsule
	 */
	private Point3f a;

	/**
	 * Medial line segment endpoint
	 */
	private Point3f b;

	/**
	 * Radius
	 */
	private float radius;

	/**
	 * The lower corner of the AABB of this capsule
	 */
	private EuclidianPoint3D lower;

	/**
	 * The upper corner of the AABB of this capsule
	 */
	private EuclidianPoint3D upper;

	/**
	 * The center of the segment AB, also corresponding to the center of the AABB and OBB
	 */
	private EuclidianPoint3D center;

	/**
	 * Orthonormal set of vector composing an axis base
	 * <table>
	 * <tr>
	 * <th>index</th>
	 * <th>explanation</th>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>R axis</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>S axis</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>T axis</td>
	 * </tr>
	 * </table>
	 */
	Vector3f[] axis = new Vector3f[3];

	/**
	 * Extent of the OBB, cannot be negative.
	 * <table>
	 * <tr>
	 * <th>index</th>
	 * <th>explanation</th>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>extent in R axis</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>extent in S axis</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>extent in T axis</td>
	 * </tr>
	 * </table>
	 */
	float[] extent = new float[3];

	/**
	 * The various vertices that compose this capsule
	 */
	private transient Vector3f[] vertices = null;

	/**
	 */
	public Capsule() {
		this.center = new EuclidianPoint3D();
	}

	/**
	 * @param a
	 *            is the first point on the capsule's segment.
	 * @param b
	 *            is the second point on the capsule's segment.
	 * @param radius
	 *            is the radius of the capsule.
	 */
	public Capsule(Point3f a, Point3f b, float radius) {
		assert ((a != null) && (b != null));
		this.a = a;
		this.b = b;
		this.radius = radius;

		// A is always the lower point of the capsule reference segment
		if (CoordinateSystem3D.getDefaultCoordinateSystem().isZOnUp()) {// Z up
			if (this.a.getZ() > this.b.getZ()) {// a not lower, switch with b
				Point3f tmp = this.a;
				this.a = this.b;
				this.b = tmp;
			}
		} else {// Y up
			if (this.a.getY() < this.b.getY()) {// a not lower, switch with b
				Point3f tmp = this.a;
				this.a = b;
				this.b = tmp;
			}
		}
		computeCenter();
		computeAABB();
		//useless computeOBB();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final BoundingPrimitiveType3D getBoundType() {
		return BoundingPrimitiveType3D.CAPSULE;
	}

	@Override
	public String toString() {
		StringBuilder s= new StringBuilder("Capsule "); //$NON-NLS-1$
		s.append("a: "); //$NON-NLS-1$
		s.append(this.a.toString());
		s.append(" b: "); //$NON-NLS-1$
		s.append(this.b.toString());
		s.append(" radius: "); //$NON-NLS-1$
		s.append(this.radius);
		return s.toString();
	}

	/**
	 * Compute the center point of the capsule and set the <var>center</var> attribute.
	 */
	private void computeCenter() {
		this.center = new EuclidianPoint3D(this.a);
		this.center.add(this.b);
		this.center.scale(0.5f);
	}

	/**
	 * Compute the AABB around the capsule.
	 */
	private void computeAABB() {
		this.lower = new EuclidianPoint3D(this.a.getX() - this.radius, this.a.getY() - this.radius, this.a.getZ() - this.radius);
		this.upper = new EuclidianPoint3D(this.b.getX() + this.radius, this.b.getY() + this.radius, this.b.getZ() + this.radius);
	}

	/**
	 * Compute the OBB around the capsule.
	 */
	private void computeOBB() {
		this.axis = new Vector3f[3];

		this.axis[0] = new Vector3f(this.b);
		this.axis[0].sub(this.a);

		Vector2f y2d = MathUtil.perpendicularVector(this.axis[0].getX(), this.axis[0].getY());
		this.axis[1] = new Vector3f(y2d.getX(), y2d.getY(), this.axis[0].getZ());

		this.axis[2] = new Vector3f();
		this.axis[2].cross(this.axis[0], this.axis[1]);

		this.extent = new float[3];
		this.extent[0] = this.axis[0].length() / 2 + this.radius;
		this.extent[1] = this.radius;
		this.extent[2] = this.axis[2].length() / 2 + this.radius;

		this.axis[1].normalize();
		this.axis[2].normalize();
		this.axis[3].normalize();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bounds2D toBounds2D() {
		return toBounds2D(CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bounds2D toBounds2D(CoordinateSystem3D system) {
		Point2f a2d = system.toCoordinateSystem2D(this.a);
		Point2f b2d = system.toCoordinateSystem2D(this.b);
		Point2f center2d = system.toCoordinateSystem2D(this.center);
		/**
  		specific configuration when a = b capsule in 2D = circle
		 */
		if ((this.a.getX() == this.b.getX()) && (this.a.getY() == this.b.getY())) {
			return new BoundingCircle(new Point2f(this.a.getX(),this.a.getY()),this.radius);
		}
		
		Vector2f[] axis = new Vector2f[2];
		axis[0] = new Vector2f(b2d);
		axis[0].sub(a2d);
		axis[1] = MathUtil.perpendicularVector(axis[0]);
		float[] extent = new float[2];
		extent[0] = axis[0].length() / 2 + this.radius;
		extent[1] = this.radius;

		axis[0].normalize();
		axis[1].normalize();
		return new OrientedRectangle2f(center2d, axis, extent);
	}

	/**
	 * Replies the first point of the capsule's segment.
	 * 
	 * @return the first point of the capsule's segment.
	 */
	public Point3f getA() {
		return this.a;
	}

	/**
	 * Replies the second point of the capsule's segment.
	 * 
	 * @return the second point of the capsule's segment.
	 */
	public Point3f getB() {
		return this.b;
	}

	/**
	 * Replies the radius of the capsule.
	 * 
	 * @return the radius of the capsule.
	 */
	public float getRadius() {
		return this.radius;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeX() {
		return this.upper.getA() - this.lower.getA();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeY() {
		return this.upper.getB() - this.lower.getB();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeZ() {
		return this.upper.getC() - this.lower.getC();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getCenter() {
		return this.center;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getLower() {
		return this.lower;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point3f lower, Point3f upper) {
		lower.set(this.lower);
		upper.set(this.upper);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getSize() {
		return new Vector3f(this.upper.getA() - this.lower.getA(), this.upper.getB() - this.lower.getB(), this.upper.getC() - this.lower.getC());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getUpper() {
		return this.upper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getR() {
		return this.axis[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getRExtent() {
		return this.extent[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getS() {
		return this.axis[1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSExtent() {
		return this.extent[1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getT() {
		return this.axis[2];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getTExtent() {
		return this.extent[2];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(Point3f position) {
		Vector3f translation = new Vector3f(position);
		translation.sub(this.center);
		translate(translation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(Point3f position, boolean onGround) {
		if (onGround) {// a is moving, position defines the new position of a
			Vector3f translation = new Vector3f(position);
			translation.sub(this.a);
			translate(translation);
		} else {
			setTranslation(position);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translate(Vector3f translation) {
		this.a.add(translation);
		this.b.add(translation);
		this.center.add(translation);
		this.lower.add(translation);
		this.upper.add(translation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(AxisAngle4f rotation) {
		Matrix4f m = new Matrix4f();
		m.set(rotation);
		Vector3f translateToOrigin = new Vector3f(this.center);
		translateToOrigin.negate();
		// Translate to origin
		this.a.add(translateToOrigin);
		this.b.add(translateToOrigin);
		// Rotate
		m.transform(this.a);
		m.transform(this.b);
		// Translate to initial position
		this.a.add(this.center);
		this.b.add(this.center);
		computeAABB();
		computeOBB();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(Quaternion rotation) {
		Matrix4f m = new Matrix4f();
		m.set(rotation);
		Vector3f translateToOrigin = new Vector3f(this.center);
		translateToOrigin.negate();
		// Translate to origin
		this.a.add(translateToOrigin);
		this.b.add(translateToOrigin);
		// Rotate
		m.transform(this.a);
		m.transform(this.b);
		// Translate to initial position
		this.a.add(this.center);
		this.b.add(this.center);
		computeAABB();
		computeOBB();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRotation(AxisAngle4f rotation) {
		// The orignal capsule is vertical grounded by A (0,0,0)
		Vector3f ab = new Vector3f(this.a);
		ab.sub(this.b);

		this.a = new Point3f();
		this.b = new Point3f();
		// A is always the lower point of the capsule reference segment
		if (CoordinateSystem3D.getDefaultCoordinateSystem().isZOnUp()) {// Z up
			this.b.add(new Vector3f(0, 0, ab.length()));
		} else {// Y up
			this.b.add(new Vector3f(0, ab.length(), 0));
		}

		computeCenter();
		Matrix4f m = new Matrix4f();
		m.set(rotation);
		Vector3f translateToOrigin = new Vector3f(this.center);
		translateToOrigin.negate();
		// Translate to origin
		this.a.add(translateToOrigin);
		this.b.add(translateToOrigin);
		// Rotate
		m.transform(this.a);
		m.transform(this.b);
		// Translate to initial position
		this.a.add(this.center);
		this.b.add(this.center);
		computeAABB();
		computeOBB();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRotation(Quaternion rotation) {
		// The orignal capsule is vertical grounded by A (0,0,0)
		Vector3f ab = new Vector3f(this.a);
		ab.sub(this.b);

		this.a = new Point3f();
		this.b = new Point3f();
		// A is always the lower point of the capsule reference segment
		if (CoordinateSystem3D.getDefaultCoordinateSystem().isZOnUp()) {// Z up
			this.b.add(new Vector3f(0, 0, ab.length()));
		} else {// Y up
			this.b.add(new Vector3f(0, ab.length(), 0));
		}

		computeCenter();
		Matrix4f m = new Matrix4f();
		m.set(rotation);
		Vector3f translateToOrigin = new Vector3f(this.center);
		translateToOrigin.negate();
		// Translate to origin
		this.a.add(translateToOrigin);
		this.b.add(translateToOrigin);
		// Rotate
		m.transform(this.a);
		m.transform(this.b);
		// Translate to initial position
		this.a.add(this.center);
		this.b.add(this.center);
		computeAABB();
		computeOBB();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceMaxSquared(Point3f reference) {
		Vector3f refToA = new Vector3f(this.a);
		refToA.sub(reference);
		Vector3f refToB = new Vector3f(this.b);
		refToB.sub(reference);
		float refToAlength = refToA.length();
		float refToBlength = refToB.length();

		if (refToAlength > refToBlength) { // A farest
			return (refToAlength + this.radius) * (refToAlength + this.radius);
		}
		// B farest
		return (refToBlength + this.radius) * (refToBlength + this.radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D farestPoint(Point3f reference) {
		Vector3f refToA = new Vector3f(this.a);
		refToA.sub(reference);
		Vector3f refToB = new Vector3f(this.b);
		refToB.sub(reference);
		float refToAlength = refToA.length();
		float refToBlength = refToB.length();

		if (refToAlength > refToBlength) { // A farest
			refToA.normalize();
			refToA.scale(this.radius);

			EuclidianPoint3D farest = new EuclidianPoint3D(this.a);
			farest.add(refToA);
			return farest;
		}

		// B farest
		refToB.normalize();
		refToB.scale(this.radius);

		EuclidianPoint3D farest = new EuclidianPoint3D(this.b);
		farest.add(refToB);
		return farest;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point3f reference) {
		EuclidianPoint3D nearestPoint = new EuclidianPoint3D();
		MathUtil.getNearestPointOnSegment(reference.getX(), reference.getY(), reference.getZ(), this.a.getX(), this.a.getY(), this.a.getZ(), this.b.getX(), this.b.getY(), this.b.getZ(), nearestPoint);

		Vector3f v = new Vector3f(nearestPoint);
		v.sub(reference);
		return (v.length() - this.radius) * (v.length() - this.radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D nearestPoint(Point3f reference) {
		EuclidianPoint3D nearestPoint = new EuclidianPoint3D();
		MathUtil.getNearestPointOnSegment(reference.getX(), reference.getY(), reference.getZ(), this.a.getX(), this.a.getY(), this.a.getZ(), this.b.getX(), this.b.getY(), this.b.getZ(), nearestPoint);

		Vector3f v = new Vector3f(nearestPoint);
		v.sub(reference);
		v.normalize();
		v.scale(this.radius);
		nearestPoint.sub(v);
		return nearestPoint;
	}

	/**
	 * Computes OBB vertices
	 * 
	 * @return OBB vertices
	 */
	private Vector3f[] computeCapsuleVertices() {
		this.vertices = new Vector3f[8];

		Vector3f Rpos = new Vector3f(this.axis[0]);
		Rpos.scale(this.extent[0]);
		Vector3f Rneg = new Vector3f(Rpos);
		Rneg.negate();
		Vector3f Spos = new Vector3f(this.axis[1]);
		Spos.scale(this.extent[1]);
		Vector3f Sneg = new Vector3f(Spos);
		Sneg.negate();
		Vector3f Tpos = new Vector3f(this.axis[2]);
		Tpos.scale(this.extent[2]);
		Vector3f Tneg = new Vector3f(Tpos);
		Tneg.negate();

		this.vertices[0] = MathUtil.add(Rpos, Spos, Tpos);
		this.vertices[1] = MathUtil.add(Rneg, Spos, Tpos);
		this.vertices[2] = MathUtil.add(Rneg, Sneg, Tpos);
		this.vertices[3] = MathUtil.add(Rpos, Sneg, Tpos);
		this.vertices[4] = MathUtil.add(Rpos, Sneg, Tneg);
		this.vertices[5] = MathUtil.add(Rpos, Spos, Tneg);
		this.vertices[6] = MathUtil.add(Rneg, Spos, Tneg);
		this.vertices[7] = MathUtil.add(Rneg, Sneg, Tneg);

		return this.vertices;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SizedIterator<EuclidianPoint3D> getGlobalOrientedBoundVertices() {
		return new LocalToGlobalVertexIterator(getCenter(), getLocalOrientedBoundVertices());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getGlobalVertexAt(int index) {

		Vector3f[] vertices = computeCapsuleVertices();

		EuclidianPoint3D p = new EuclidianPoint3D(getCenter());
		p.add(vertices[index]);
		return p;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SizedIterator<Vector3f> getLocalOrientedBoundVertices() {
		return ArrayUtil.sizedIterator(computeCapsuleVertices());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getLocalVertexAt(int index) {
		Vector3f[] vertices = computeCapsuleVertices();
		return vertices[index];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f[] getOrientedBoundAxis() {
		return this.axis;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getOrientedBoundExtentVector() {
		return new Vector3f(this.extent[0], this.extent[1], this.extent[2]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float[] getOrientedBoundExtents() {
		return this.extent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVertexCount() {
		return 8;
	}

	/**
	 * Replies if the specified capsule intersects this capsule
	 * 
	 * @param capsule
	 *            - the cpasule to test
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public boolean intersectsCapsule(Capsule capsule) {
		return IntersectionUtil.intersectsCapsuleCapsule(this.a, this.b, this.radius, capsule.getA(), capsule.getB(), capsule.getRadius());
	}

	/**
	 * Replies if the specified capsule intersects this capsule
	 * 
	 * @param capsuleA
	 *            - Medial line segment start point of the first capsule
	 * @param capsuleB
	 *            - Medial line segment end point of the first capsule
	 * @param capsuleRadius
	 *            - radius of the first capsule
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public boolean intersectsCapsule(Point3f capsuleA, Point3f capsuleB, float capsuleRadius) {
		return IntersectionUtil.intersectsCapsuleCapsule(this.a, this.b, this.radius, capsuleA, capsuleB, capsuleRadius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f c, float r) {
		return ClassifierUtil.classifySphereCapsule(c, r, this.a, this.b, this.radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Plane plane) {

		float distanceToA = plane.distanceTo(this.a);
		float distanceToB = plane.distanceTo(this.b);

		if (distanceToA * distanceToB > 0) { // two distance of the same sign
			if (Math.abs(Math.min(distanceToA, distanceToA)) > this.radius) {// no intersection
				return IntersectionType.OUTSIDE;
			}
			return IntersectionType.SPANNING;
		}
		return IntersectionType.SPANNING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f center, Vector3f[] axis, float[] extent) {
		return ClassifierUtil.classifyOrientedBoxCapsule(center, axis, extent, this.a, this.b, this.radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlanarClassificationType classifiesAgainst(Plane plane) {
		float distanceToA = plane.distanceTo(this.a);
		float distanceToB = plane.distanceTo(this.b);

		if (distanceToA * distanceToB > 0) { // two distance of the same sign
			if (Math.abs(Math.min(distanceToA, distanceToA)) > this.radius) {// no intersection
				if (distanceToA > 0) {
					return PlanarClassificationType.IN_FRONT_OF;
				}
				return PlanarClassificationType.BEHIND;
			}
			return PlanarClassificationType.COINCIDENT;
		}
		return PlanarClassificationType.COINCIDENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f c, float r) {
		return IntersectionUtil.intersectsSphereCapsule(c, r, this.a, this.b, this.radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Plane plane) {
		float distanceToA = plane.distanceTo(this.a);
		float distanceToB = plane.distanceTo(this.b);

		return ((distanceToA * distanceToB <= 0) // two distance of the same sign
		|| (Math.abs(Math.min(distanceToA, distanceToA)) <= this.radius)); // no intersection
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f center, Vector3f[] axis, float[] extent) {
		return IntersectionUtil.intersectsOrientedBoxCapsule(center, axis, extent, this.a, this.b, this.radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f p) {
		return ClassifierUtil.classifyPointCapsule(p, this.a, this.b, this.radius);
	}

	/**
	 * Compute intersection between an OBB and a capsule
	 */
	private boolean intersectsAlignedBoxCapsule(AxisAlignedBox aabb) {
		Point3f closestFromA = aabb.nearestPoint(this.a);
		Point3f closestFromB = aabb.nearestPoint(this.b);

		float distance = MathUtil.distanceSegmentSegment(this.a.getX(), this.a.getY(), this.b.getX(), this.b.getY(), closestFromA.getX(), closestFromA.getY(), closestFromB.getX(), closestFromB.getY());

		return (distance <= this.radius);
	}

	/**
	 * Compute intersection between an AABB and a capsule
	 */
	private IntersectionType classifyAlignedBoxCapsule(AxisAlignedBox aabb) {
		Point3f closestFromA = aabb.nearestPoint(this.a);
		Point3f closestFromB = aabb.nearestPoint(this.b);
		Point3f farestFromA = aabb.farestPoint(this.a);
		Point3f farestFromB = aabb.farestPoint(this.b);

		float distanceToNearest = MathUtil.distanceSegmentSegment(this.a.getX(), this.a.getY(), this.b.getX(), this.b.getY(), closestFromA.getX(), closestFromA.getY(), closestFromB.getX(), closestFromB.getY());

		if (distanceToNearest > this.radius) {
			return IntersectionType.OUTSIDE;
		}

		float distanceToFarest = MathUtil.distanceSegmentSegment(this.a.getX(), this.a.getY(), this.b.getX(), this.b.getY(), farestFromA.getX(), farestFromA.getY(), farestFromB.getX(), farestFromB.getY());
		if (distanceToFarest < this.radius) {
			return IntersectionType.ENCLOSING;
		}

		IntersectionType onSphereA = ClassifierUtil.classifiesSolidSphereSolidAlignedBox(this.a, this.radius, aabb.getLower(), aabb.getUpper());
		IntersectionType onSphereB = ClassifierUtil.classifiesSolidSphereSolidAlignedBox(this.b, this.radius, aabb.getLower(), aabb.getUpper());

		if (onSphereA.equals(IntersectionType.INSIDE) && onSphereB.equals(IntersectionType.INSIDE)) {
			return IntersectionType.INSIDE;

		} else if (onSphereA.equals(IntersectionType.INSIDE) || onSphereB.equals(IntersectionType.INSIDE)) {
			return IntersectionType.SPANNING;
		} else if (onSphereA.equals(IntersectionType.ENCLOSING) || onSphereB.equals(IntersectionType.ENCLOSING)) {
			return IntersectionType.ENCLOSING;
		}

		return IntersectionType.SPANNING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f l, Point3f u) {
		return classifyAlignedBoxCapsule(new AxisAlignedBox(l, u));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f p) {
		return IntersectionUtil.intersectPointCapsule(p, this.a, this.b, this.radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f l, Point3f u) {
		return intersectsAlignedBoxCapsule(new AxisAlignedBox(l, u));
	}

	@Override
	protected void combineBounds(boolean isInit, Collection<? extends Bounds3D> bounds) {
		if (!isInit) {
			if (bounds.size() == 1) {
				Bounds3D cb = CollectionUtil.firstElement(bounds);
				if (cb instanceof OrientedBox) {
					OrientedBox obb = (OrientedBox) cb;
					this.center = (EuclidianPoint3D) obb.center.clone();
					for (int i = 0; i < 3; ++i) {
						this.axis[i] = obb.axis[i].clone();
						this.extent[i] = obb.extent[i];
					}

					this.computeCapsuleVertices();
					this.computeAandBfromOBBData();
				} else if (cb instanceof Sphere) {
					Sphere sphere = (Sphere) cb;
					this.center = (EuclidianPoint3D) sphere.center.clone();
					this.axis[0] = new Vector3f(1., 0., 0.);
					this.axis[1] = new Vector3f(0., 1., 0.);
					this.axis[2] = new Vector3f(0., 0., 1.);
					for (int i = 0; i < 3; ++i) {
						this.extent[i] = sphere.radius;
					}

					this.computeCapsuleVertices();
					this.computeAandBfromOBBData();
				} else if (cb instanceof AxisAlignedBox) {
					AxisAlignedBox aabb = (AxisAlignedBox) cb;
					this.center = (EuclidianPoint3D) aabb.getCenter().clone();
					this.axis[0] = new Vector3f(1., 0., 0.);
					this.axis[1] = new Vector3f(0., 1., 0.);
					this.axis[2] = new Vector3f(0., 0., 1.);
					this.extent[0] = aabb.getSizeX() / 2.f;
					this.extent[1] = aabb.getSizeY() / 2.f;
					this.extent[2] = aabb.getSizeZ() / 2.f;

					this.computeCapsuleVertices();
					this.computeAandBfromOBBData();
				} else if (cb instanceof ComposedBounds3D) {
					ComposedBounds3D ccb = (ComposedBounds3D) cb;
					computeBoundingCapsule(new MergedBoundList(ccb));
				}
			} else {
				computeBoundingCapsule(new MergedBoundList(bounds));
			}
		} else {
			computeBoundingCapsule(new MergedBoundList(bounds, this));
		}

	}

	@Override
	protected void combinePoints(boolean isInit, Collection<? extends Tuple3f> pointList) {
		if (!isInit) {
			computeBoundingCapsule(pointList);
		} else {
			computeBoundingCapsule(new MergedPointList(pointList));
		}
	}

	private void computeBoundingCapsule(Iterable<? extends Tuple3f> vertexList) {
		if (this.axis == null || this.axis.length != 3)
			this.axis = new Vector3f[3];
		for (int i = 0; i < 3; ++i) {
			if (this.axis[i] == null)
				this.axis[i] = new Vector3f();
		}

		// Determining the OBB axis, extent and center
		OrientedBox.computeOBBCenterAxisExtents(vertexList, this.axis[0], this.axis[1], this.axis[2], this.center, this.extent);
		
		//FIXME Refine the way to compute the radius

		this.computeAandBfromOBBData();
		this.computeAABB();
		this.computeCapsuleVertices();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() {
		this.a = null;
		this.b = null;
		this.radius = 0.0f;
		this.center.set(0, 0, 0);

		for (int i = 0; i < 3; ++i) {
			this.axis[i].set(0, 0, 0);
			this.extent[i] = 0;
		}

		this.vertices = null;
		this.lower = this.upper = null;
	}

	private void computeAandBfromOBBData() {
		// FIXME Refine the way to compute the radius
			this.radius = Math.max(this.extent[1], this.extent[2]);
			this.b = new EuclidianPoint3D(this.center);

			Vector3f r = new Vector3f(this.axis[0]);
			r.normalize();
			r.scale(this.extent[0]);
			this.b.add(r);

			r.normalize();
			r.scale(-this.extent[0]);

			this.a = new EuclidianPoint3D(this.center);
			this.a.add(r);
			assert(this.a.getZ() <= this.b.getZ());
	}

	/**
	 * An iterable on the Capsule vertices and on a list of points.
	 * 
	 * @author $Author: ngaud$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class MergedPointList implements Iterable<Tuple3f> {

		private final Collection<? extends Tuple3f> points;

		/**
		 * @param pointList
		 *            is a list of point to merge inside the iterable.
		 */
		public MergedPointList(Collection<? extends Tuple3f> pointList) {
			this.points = pointList;
		}

		@Override
		public Iterator<Tuple3f> iterator() {
			return CollectionUtil.mergeIterators(
					getGlobalOrientedBoundVertices(),
					CollectionUtil.sizedIterator(this.points));
		}

	}

	/**
	 * @author $Author: ngaud$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class MergedBoundList implements Iterable<Tuple3f> {

		private final Collection<? extends Bounds3D> bounds;
		private final Capsule capsule;

		/**
		 * @param bounds
		 *            are the list of bounds to merge inside
		 */
		public MergedBoundList(Collection<? extends Bounds3D> bounds) {
			this.bounds = bounds;
			this.capsule = null;
		}

		/**
		 * @param bounds
		 *            are the list of bounds to merge inside
		 * @param obb
		 *            is the obb to put back in the merging list.
		 */
		public MergedBoundList(Collection<? extends Bounds3D> bounds, Capsule obb) {
			this.bounds = bounds;
			this.capsule = obb;
		}

		@Override
		public Iterator<Tuple3f> iterator() {
			Iterator<Tuple3f> iterator = new ExtractionIterator(this.bounds.iterator());

			if (this.capsule == null)
				return iterator;

			return CollectionUtil.mergeIterators(iterator, this.capsule.getGlobalOrientedBoundVertices());
		}

		/**
		 * An iterable on vertices which is merging a collection of bounds and optionnally a capsule.
		 */
		private static class ExtractionIterator implements Iterator<Tuple3f> {

			private final Iterator<? extends Bounds3D> bounds;
			private LinkedList<Bounds3D> subBounds = null;
			private Iterator<? extends Point3f> vertices;
			private Tuple3f next;

			/**
			 * @param bounds
			 */
			public ExtractionIterator(Iterator<? extends Bounds3D> bounds) {
				this.bounds = bounds;
				searchNext();
			}

			private void searchNext() {
				this.next = null;
				while (this.vertices == null || !this.vertices.hasNext()) {
					Bounds3D cb;
					this.vertices = null;
					while ((this.subBounds != null && !this.subBounds.isEmpty()) || this.bounds.hasNext()) {
						cb = (this.subBounds != null) ? this.subBounds.removeFirst() : this.bounds.next();
						if (cb.isInit()) {
							if (cb instanceof OrientedBounds3D) {
								this.vertices = ((OrientedBounds3D) cb).getGlobalOrientedBoundVertices();
								break;
							} else if (cb instanceof ComposedBounds3D) {
								if (this.subBounds == null)
									this.subBounds = new LinkedList<Bounds3D>();
								this.subBounds.addAll((ComposedBounds3D) cb);
							}
						}
					}
					if (this.subBounds != null && this.subBounds.isEmpty())
						this.subBounds = null;
					if (this.vertices == null)
						return; // No more vertex
				}

				// One vertex exists!
				this.next = this.vertices.next();
			}

			@Override
			public boolean hasNext() {
				return this.next != null;
			}

			@Override
			public Tuple3f next() {
				if (this.next == null)
					throw new NoSuchElementException();
				Tuple3f n = this.next;
				searchNext();
				return n;
			}

			@Override
			public void remove() {
				//
			}

		}

	}

}
