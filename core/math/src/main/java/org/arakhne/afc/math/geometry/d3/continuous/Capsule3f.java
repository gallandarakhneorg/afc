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

package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;

/**
 * A bounding capsule is a swept sphere (i.e. the volume that a sphere takes as it moves
 * along a straight line segment) containing the object. Capsules can be represented
 * by the radius of the swept sphere and the segment that the sphere is swept across). 
 * It has traits similar to a cylinder, but is easier to use, because the intersection test 
 * is simpler. A capsule and another object intersect if the distance between the
 * capsule's defining segment and some feature of the other object is smaller than the
 * capsule's radius. For example, two capsules intersect if the distance
 * between the capsules' segments is smaller than the sum of their radii. This holds for
 * arbitrarily rotated capsules, which is why they're more appealing than cylinders in practice.
 * 
 * @author $Author: cbohrhauer$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Capsule3f extends AbstractShape3F<Capsule3f> {

	private static final long serialVersionUID = -5215944836678687801L;

	/**
	 * Compute intersection between a point and a capsule.
	 *
	 * @param capsule1Ax - capsule medial line segment start point
	 * @param capsule1Ay - capsule medial line segment start point
	 * @param capsule1Az - capsule medial line segment start point
	 * @param capsule1Bx - capsule medial line segment end point
	 * @param capsule1By - capsule medial line segment end point
	 * @param capsule1Bz - capsule medial line segment end point
	 * @param capsule1Radius - capsule radius
	 * @param px - the point to test
	 * @param py - the point to test
	 * @param pz - the point to test
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean containsCapsulePoint(
			double capsule1Ax, double capsule1Ay, double capsule1Az, double capsule1Bx, double capsule1By, double capsule1Bz, double capsule1Radius,
			double px, double py, double pz) {
		double distPointToCapsuleSegment = AbstractSegment3F.distanceSquaredSegmentPoint(
				capsule1Ax, capsule1Ay, capsule1Az, capsule1Bx, capsule1By, capsule1Bz,
				px,py,pz);
		return (distPointToCapsuleSegment <= (capsule1Radius * capsule1Radius));
	}

	/** Replies if the capsule intersects the aligned box.
	 * 
	 * @param mx1 x coordinate of the first point of the capsule's segment.
	 * @param my1 y coordinate of the first point of the capsule's segment.
	 * @param mz1 z coordinate of the first point of the capsule's segment.
	 * @param mx2 x coordinate of the second point of the capsule's segment.
	 * @param my2 y coordinate of the second point of the capsule's segment.
	 * @param mz2 z coordinate of the second point of the capsule's segment.
	 * @param radius radius of the capsule.
	 * @param minx x coordinate of the lower corner of the aligned box.
	 * @param miny y coordinate of the lower corner of the aligned box.
	 * @param minz z coordinate of the lower corner of the aligned box.
	 * @param maxx x coordinate of the upper corner of the aligned box.
	 * @param maxy y coordinate of the upper corner of the aligned box.
	 * @param maxz z coordinate of the upper corner of the aligned box.
	 * @return <code>true</code> if the capsule and aligned box are intersecting.
	 */
	public static boolean intersectsCapsuleAlignedBox(
			double mx1, double my1, double mz1,
			double mx2, double my2, double mz2,
			double radius,
			double minx, double miny, double minz, 
			double maxx, double maxy, double maxz) {
		Point3f closest1 = AlignedBox3f.computeClosestPoint(
				minx, miny, minz, maxx, maxy, maxz,
				mx1, my1, mz1);
		Point3f closest2 = AlignedBox3f.computeClosestPoint(
				minx, miny, minz, maxx, maxy, maxz,
				mx2, my2, mz2);
		double sq = AbstractSegment3F.distanceSquaredSegmentSegment(
				mx1, my1, mz1,
				mx2, my2, mz2,
				closest1.getX(), closest1.getY(), closest1.getZ(),
				closest2.getX(), closest2.getY(), closest2.getZ());
		return (sq <= (radius * radius));
	}

	/**
	 * Replies if the specified capsules intersect.
	 *
	 * @param capsule1Ax - Medial line segment start point of the first capsule
	 * @param capsule1Ay - Medial line segment start point of the first capsule
	 * @param capsule1Az - Medial line segment start point of the first capsule
	 * @param capsule1Bx - Medial line segment end point of the first capsule
	 * @param capsule1By - Medial line segment end point of the first capsule
	 * @param capsule1Bz - Medial line segment end point of the first capsule
	 * @param capsule1Radius - radius of the first capsule
	 * @param capsule2Ax - Medial line segment start point of the second capsule
	 * @param capsule2Ay - Medial line segment start point of the second capsule
	 * @param capsule2Az - Medial line segment start point of the second capsule
	 * @param capsule2Bx - Medial line segment end point of the second capsule
	 * @param capsule2By - Medial line segment end point of the second capsule
	 * @param capsule2Bz - Medial line segment end point of the second capsule
	 * @param capsule2Radius - radius of the second capsule
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectsCapsuleCapsule(
			double capsule1Ax, double capsule1Ay, double capsule1Az, double capsule1Bx, double capsule1By, double capsule1Bz, double capsule1Radius,
			double capsule2Ax, double capsule2Ay, double capsule2Az, double capsule2Bx, double capsule2By, double capsule2Bz, double capsule2Radius) {

		double dist2 = AbstractSegment3F.distanceSquaredSegmentSegment(
				capsule1Ax, capsule1Ay, capsule1Az, capsule1Bx, capsule1By, capsule1Bz,
				capsule2Ax, capsule2Ay, capsule2Az, capsule2Bx, capsule2By, capsule2Bz);

		// If (squared) distance smaller than (squared) sum of radii, they collide
		double radius = capsule1Radius + capsule2Radius;
		return dist2 <= (radius * radius);
	} 

	/**
	 * Medial line segment start point, it is also the lower point of the capsule
	 */
	protected final Point3f medial1 = new Point3f();

	/**
	 * Medial line segment endpoint
	 */
	protected final Point3f medial2 = new Point3f();

	/**
	 * Radius.
	 */
	protected double radius;

	/**
	 */
	public Capsule3f() {
		//
	}

	/**
	 * @param a
	 *            is the first point on the capsule's segment.
	 * @param b
	 *            is the second point on the capsule's segment.
	 * @param radius1
	 *            is the radius of the capsule.
	 */
	public Capsule3f(Point3D a, Point3D b, double radius1) {
		this(a.getX(), a.getY(), a.getZ(),
				b.getX(), b.getY(), b.getZ(),
				radius1);
	}

	/**
	 * @param capsule the capsule to copy.
	 */
	public Capsule3f(Capsule3f capsule) {
		this(capsule.getMedialX1(), capsule.getMedialY1(), capsule.getMedialZ1(),
				capsule.getMedialX2(), capsule.getMedialY2(), capsule.getMedialZ2(),
				capsule.getRadius());
	}

	/**
	 * @param x1 x coordinate of the first point of the medial line.
	 * @param y1 y coordinate of the first point of the medial line.
	 * @param z1 z coordinate of the first point of the medial line.
	 * @param x2 x coordinate of the second point of the medial line.
	 * @param y2 y coordinate of the second point of the medial line.
	 * @param z2 z coordinate of the second point of the medial line.
	 * @param radius1 the radius of the capsule.
	 */
	public Capsule3f(double x1, double y1, double z1, double x2, double y2, double z2, double radius1) {
		this.medial1.set(x1, y2, z1);
		this.medial2.set(x2, y2, z2);
		this.radius = radius1;
		ensureAIsLowerPoint();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Capsule3f) {
			Capsule3f c3f = (Capsule3f) obj;
			return ((getMedial1() == c3f.getMedial1()) &&
					(getMedial2() == c3f.getMedial2()) &&
					(getRadius() == c3f.getRadius()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + doubleToLongBits(getMedialX1());
		bits = 31L * bits + doubleToLongBits(getMedialY1());
		bits = 31L * bits + doubleToLongBits(getMedialZ1());
		bits = 31L * bits + doubleToLongBits(getMedialX2());
		bits = 31L * bits + doubleToLongBits(getMedialY2());
		bits = 31L * bits + doubleToLongBits(getMedialZ2());
		bits = 31L * bits + doubleToLongBits(getRadius());
		return (int) (bits ^ (bits >> 32));
	}

	/** Ensure that a has the lower coordinates than b.
	 */
	protected void ensureAIsLowerPoint() {
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		boolean swap = false;
		if (cs.isZOnUp()) {
			swap = (this.medial1.getZ() > this.medial2.getZ());
		} else if (cs.isYOnUp()){
			swap = (this.medial1.getY() > this.medial2.getY());
		}
		if (swap) {
			double x = this.medial1.getX();
			double y = this.medial1.getY();
			double z = this.medial1.getZ();
			this.medial1.set(this.medial2);
			this.medial2.set(x, y, z);
		}
	}

	@Override
	public AlignedBox3f toBoundingBox() {
		AlignedBox3f box = new AlignedBox3f();
		toBoundingBox(box);
		return box;
	}

	@Override
	public void toBoundingBox(AlignedBox3f box) {
		double r = getRadius();
		double minx = Math.min(getMedialX1(), getMedialX2()) - r;
		double maxx = Math.max(getMedialX1(), getMedialX2()) + r;
		double miny = Math.min(getMedialY1(), getMedialY2()) - r;
		double maxy = Math.max(getMedialY1(), getMedialY2()) + r;
		double minz = Math.min(getMedialZ1(), getMedialZ2()) - r;
		double maxz = Math.max(getMedialZ1(), getMedialZ2()) + r;
		box.set(minx, miny, minz,
				maxx - minx, maxy - miny, maxz - minz);
	}

	@Override
	public boolean isEmpty() {
		return this.radius == 0. || this.medial1.equals(this.medial2);
	}

	@Override
	public void clear() {
		this.medial1.set(0, 0, 0);
		this.medial2.set(0, 0, 0);
		this.radius = 0.0f;
	}

	/**
	 * Replies the first point of the capsule's segment.
	 * 
	 * @return the first point of the capsule's segment.
	 */
	public Point3f getMedial1() {
		return this.medial1;
	}

	/**
	 * Set the first point of the capsule's segment.
	 * 
	 * @param point the new first point for the capsule's segment..
	 */
	public void setMedial1(Point3D point) {
		setMedial1(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Set the first point of the capsule's segment.
	 * 
	 * @param x x xoordinate of the new first point for the capsule's segment..
	 * @param y y xoordinate of the new first point for the capsule's segment..
	 * @param z z xoordinate of the new first point for the capsule's segment..
	 */
	public void setMedial1(double x, double y, double z) {
		this.medial1.set(x, y, z);
		ensureAIsLowerPoint();
	}

	/**
	 * Replies x coordinate of the first point of the capsule's segment.
	 * 
	 * @return the x coordinate of the first point of the capsule's segment.
	 */
	public double getMedialX1() {
		return this.medial1.getX();
	}

	/**
	 * Replies y coordinate of the first point of the capsule's segment.
	 * 
	 * @return the y coordinate of the first point of the capsule's segment.
	 */
	public double getMedialY1() {
		return this.medial1.getY();
	}

	/**
	 * Replies z coordinate of the first point of the capsule's segment.
	 * 
	 * @return the z coordinate of the first point of the capsule's segment.
	 */
	public double getMedialZ1() {
		return this.medial1.getZ();
	}

	/**
	 * Replies the second point of the capsule's segment.
	 * 
	 * @return the second point of the capsule's segment.
	 */
	public Point3f getMedial2() {
		return this.medial2;
	}

	/**
	 * Set the second point of the capsule's segment.
	 * 
	 * @param point the new second point for the capsule's segment..
	 */
	public void setMedial2(Point3D point) {
		setMedial2(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Set the second point of the capsule's segment.
	 * 
	 * @param x x coordinate of the new second point for the capsule's segment..
	 * @param y y coordinate of the new second point for the capsule's segment..
	 * @param z z coordinate of the new second point for the capsule's segment..
	 */
	public void setMedial2(double x, double y, double z) {
		this.medial2.set(x, y, z);
		ensureAIsLowerPoint();
	}

	/**
	 * Replies x coordinate of the second point of the capsule's segment.
	 * 
	 * @return the x coordinate of the second point of the capsule's segment.
	 */
	public double getMedialX2() {
		return this.medial2.getX();
	}

	/**
	 * Replies y coordinate of the second point of the capsule's segment.
	 * 
	 * @return the y coordinate of the second point of the capsule's segment.
	 */
	public double getMedialY2() {
		return this.medial2.getY();
	}

	/**
	 * Replies z coordinate of the second point of the capsule's segment.
	 * 
	 * @return the z coordinate of the second point of the capsule's segment.
	 */
	public double getMedialZ2() {
		return this.medial2.getZ();
	}

	/**
	 * Replies the radius of the capsule.
	 * 
	 * @return the radius of the capsule.
	 */
	public double getRadius() {
		return this.radius;
	}

	/**
	 * Change the radius of the capsule.
	 * 
	 * @param radius1 the radius of the capsule.
	 */
	public void setRadius(double radius1) {
		this.radius = radius1;
	}

	/** Set the capsule.
	 * 
	 * @param a the first point of the capsule's segment.
	 * @param b the second point of the capsule's segment.
	 * @param radius1 the radius of the capsule.
	 */
	public void set(Point3D a, Point3D b, double radius1) {
		set(a.getX(), a.getY(), a.getZ(),
				b.getX(),b.getY(), b.getZ(),
				radius1);
	}

	/** Set the capsule.
	 * 
	 * @param x1 x coordinate of the first point of the capsule's segment.
	 * @param y1 y coordinate of the first point of the capsule's segment.
	 * @param z1 z coordinate of the first point of the capsule's segment.
	 * @param x2 x coordinate of the second point of the capsule's segment.
	 * @param y2 y coordinate of the second point of the capsule's segment.
	 * @param z2 z coordinate of the second point of the capsule's segment.
	 * @param radius1 the radius of the capsule.
	 */
	public void set(double x1, double y1, double z1, double x2, double y2, double z2, double radius1) {
		this.medial1.set(x1, y1, z1);
		this.medial2.set(x2, y2, z2);
		this.radius = radius1;
		ensureAIsLowerPoint();
	}
	
	@Override
	public void set(Shape3F s) {
		if (s instanceof Capsule3f) {
			Capsule3f c = (Capsule3f) s;
			set(c.getMedialX1(), c.getMedialY1(), c.getMedialZ1(),
					c.getMedialX2(), c.getMedialY2(), c.getMedialZ2(),
					c.getRadius());
		} else {
			AlignedBox3f r = s.toBoundingBox();
			if (r.getSizeX() >= r.getSizeY()) {
				if (r.getSizeX() >= r.getSizeZ()) {
					set(
							r.getMinX(), r.getCenterY(), r.getCenterZ(),
							r.getMaxX(), r.getCenterY(), r.getCenterZ(),
							Math.max(r.getSizeY(), r.getSizeZ()));
				} else {
					set(
							r.getCenterX(), r.getCenterY(), r.getMinZ(),
							r.getCenterX(), r.getCenterY(), r.getMaxZ(),
							Math.max(r.getSizeX(), r.getSizeY()));
				}
			} else if (r.getSizeY() > r.getSizeZ()) {
				set(
						r.getCenterX(), r.getMinY(), r.getCenterZ(),
						r.getCenterX(), r.getMaxY(), r.getCenterZ(),
						Math.max(r.getSizeX(), r.getSizeZ()));
			} else {
				set(
						r.getCenterX(), r.getCenterY(), r.getMinZ(),
						r.getCenterX(), r.getCenterY(), r.getMaxZ(),
						Math.max(r.getSizeX(), r.getSizeY()));
			}
		}
	}

	/** Replies the center point of the capsule.
	 * 
	 * @return the center point.
	 */
	public Point3f getCenter() {
		return new Point3f(
				(this.medial1.getX() + this.medial2.getX()) / 2.,
				(this.medial1.getY() + this.medial2.getY()) / 2.,
				(this.medial1.getZ() + this.medial2.getZ()) / 2.);
	}

	/** Replies the x coordinate of the center point of the capsule.
	 * 
	 * @return the center x.
	 */
	public double getCenterX() {
		return (this.medial1.getX() + this.medial2.getX()) / 2.;
	}

	/** Replies the y coordinate of the center point of the capsule.
	 * 
	 * @return the center y.
	 */
	public double getCenterY() {
		return (this.medial1.getY() + this.medial2.getY()) / 2.;
	}

	/** Replies the z coordinate of the center point of the capsule.
	 * 
	 * @return the center z.
	 */
	public double getCenterZ() {
		return (this.medial1.getZ() + this.medial2.getZ()) / 2.;
	}

	@Override
	public String toString() {
		StringBuilder s= new StringBuilder("Capsule "); //$NON-NLS-1$
		s.append("a: "); //$NON-NLS-1$
		s.append(this.medial1.toString());
		s.append(" b: "); //$NON-NLS-1$
		s.append(this.medial2.toString());
		s.append(" radius: "); //$NON-NLS-1$
		s.append(this.radius);
		return s.toString();
	}
	
	@Override
	public double distanceSquared(Point3D p) {
		double r = getRadius();
		double d = AbstractSegment3F.distanceSquaredSegmentPoint(
				getMedialX1(), getMedialY1(), getMedialZ1(),
				getMedialX2(), getMedialY2(), getMedialZ2(),
				p.getX(), p.getY(), p.getZ());
		d -= r * r;
		return Math.max(0., d);
	}

	@Override
	public double distanceL1(Point3D p) {
		Point3D r = getClosestPointTo(p);
		return r.distanceL1(p);
	}

	@Override
	public double distanceLinf(Point3D p) {
		Point3D r = getClosestPointTo(p);
		return r.distanceLinf(p);
	}

	@Override
	public Point3D getClosestPointTo(Point3D p) {
		double factor = AbstractSegment3F.getPointProjectionFactorOnSegmentLine(
				p.getX(), p.getY(), p.getZ(),
				getMedialX1(), getMedialY1(), getMedialZ1(),
				getMedialX2(), getMedialY2(), getMedialZ2());
		Point3f c;
		if (factor <= 0.) {
			c = getMedial1().clone();
		} else if (factor >= 1.) {
			c = getMedial2().clone();
		} else {
			c = new Point3f(
					getMedialX1() + factor * (getMedialX2() - getMedialX1()),
					getMedialY1() + factor * (getMedialY2() - getMedialY1()),
					getMedialZ1() + factor * (getMedialZ2() - getMedialZ1()));
		}
		Vector3f v = new Vector3f();
		v.sub(p, c);
		v.setLength(getRadius());
		c.add(v);
		return c;
	}

	@Override
	public Point3D getFarthestPointTo(Point3D p) {
		Vector3f refToA = new Vector3f();
		refToA.sub(this.medial1, p);
		Vector3f refToB = new Vector3f();
		refToB.sub(this.medial2, p);

		double refToAlength = refToA.lengthSquared();
		double refToBlength = refToB.lengthSquared();

		if (refToAlength > refToBlength) {
			// A farest
			refToA.normalize();
			refToA.scale(this.radius);

			Point3f farthest = new Point3f();
			farthest.add(this.medial1, refToA);
			return farthest;
		}

		// B farest
		refToB.normalize();
		refToB.scale(this.radius);

		Point3f farthest = new Point3f();
		farthest.add(this.medial2, refToB);
		return farthest;
	}

	@Override
	public void transform(Transform3D transformationMatrix) {
		transformationMatrix.transform(this.medial1);
		transformationMatrix.transform(this.medial2);
		Vector3f v = new Vector3f(getRadius(), getRadius(), getRadius());
		transformationMatrix.transform(v);
		setRadius(MathUtil.max(v.getX(), v.getY(), v.getZ()));
	}

	@Override
	public void translate(double dx, double dy, double dz) {
		this.medial1.add(dx, dy, dz);
		this.medial2.add(dx, dy, dz);
	}

	@Override
	public boolean contains(double x, double y, double z) {
		return containsCapsulePoint(
				getMedialX1(), getMedialY1(), getMedialZ1(),
				getMedialX2(), getMedialY2(), getMedialZ2(),
				getRadius(),
				x, y, z);
	}

	@Override
	public boolean intersects(AlignedBox3f s) {
		return intersectsCapsuleAlignedBox(
				getMedialX1(), getMedialY1(), getMedialZ1(),
				getMedialX2(), getMedialY2(), getMedialZ2(),
				getRadius(),
				s.getMinX(), s.getMinY(), s.getMinZ(),
				s.getMaxX(), s.getMaxY(), s.getMaxZ());
	}

	@Override
	public boolean intersects(Sphere3f s) {
		return Sphere3f.intersectsSphereCapsule(
				s.getX(), s.getY(), s.getZ(),
				s.getRadius(),
				getMedialX1(), getMedialY1(), getMedialZ1(),
				getMedialX2(), getMedialY2(), getMedialZ2(),
				getRadius());
	}

	@Override
	public boolean intersects(AbstractSegment3F s) {
		return AbstractSegment3F.intersectsSegmentCapsule(
				s.getX1(), s.getY1(), s.getZ1(),
				s.getX2(), s.getY2(), s.getZ2(),
				getMedialX1(), getMedialY1(), getMedialZ1(),
				getMedialX2(), getMedialY2(), getMedialZ2(),
				getRadius());
	}

	@Override
	public boolean intersects(Triangle3f s) {
		return Triangle3f.intersectsTriangleCapsule(
				s.getX1(), s.getY1(), s.getZ1(),
				s.getX2(), s.getY2(), s.getZ2(),
				s.getX3(), s.getY3(), s.getZ3(),
				getMedialX1(), getMedialY1(), getMedialZ1(),
				getMedialX2(), getMedialY2(), getMedialZ2(),
				getRadius());
	}

	@Override
	public boolean intersects(Capsule3f s) {
		return intersectsCapsuleCapsule(
				getMedialX1(), getMedialY1(), getMedialZ1(),
				getMedialX2(), getMedialY2(), getMedialZ2(),
				getRadius(),
				s.getMedialX1(), s.getMedialY1(), s.getMedialZ1(),
				s.getMedialX2(), s.getMedialY2(), s.getMedialZ2(),
				s.getRadius());
	}

	@Override
	public boolean intersects(OrientedBox3f s) {
		return OrientedBox3f.intersectsOrientedBoxCapsule(
				s.getCenterX(), s.getCenterY(), s.getCenterZ(),
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisZ(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisZ(),
				s.getThirdAxisX(), s.getThirdAxisY(), s.getThirdAxisZ(),
				s.getFirstAxisExtent(), s.getSecondAxisExtent(), s.getThirdAxisExtent(),
				getMedialX1(), getMedialY1(), getMedialZ1(),
				getMedialX2(), getMedialY2(), getMedialZ2(),
				getRadius());
	}

	@Override
	public boolean intersects(Plane3D<?> p) {
		double d1 = p.distanceTo(getMedial1());
		double d2 = p.distanceTo(getMedial2());
		if (d1 <= d2) {
			return d1 < getRadius();
		}
		return d2 < getRadius();
	}

}
