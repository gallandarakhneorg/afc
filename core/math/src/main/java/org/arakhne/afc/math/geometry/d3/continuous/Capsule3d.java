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

import org.arakhne.afc.math.geometry.d3.Point3D;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

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
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("restriction")
public class Capsule3d extends AbstractCapsule3F {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 549639614513766568L;

	/**
	 * Medial line segment start point, it is also the lower point of the capsule
	 */
	protected final Point3d medial1 = new Point3d();

	/**
	 * Medial line segment endpoint
	 */
	protected final Point3d medial2 = new Point3d();

	/**
	 * Radius.
	 */
	protected DoubleProperty radiusProperty = new SimpleDoubleProperty(0f);

	/**
	 */
	public Capsule3d() {
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
	public Capsule3d(Point3D a, Point3D b, double radius1) {
		this(a.getX(), a.getY(), a.getZ(),
				b.getX(), b.getY(), b.getZ(),
				radius1);
	}

	/**
	 * @param capsule the capsule to copy.
	 */
	public Capsule3d(AbstractCapsule3F capsule) {
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
	public Capsule3d(double x1, double y1, double z1, double x2, double y2, double z2, double radius1) {
		this.medial1.set(x1, y2, z1);
		this.medial2.set(x2, y2, z2);
		this.radiusProperty.set(radius1);
		ensureAIsLowerPoint();
	}
	
	
	
	/**
	 * Replies the first point of the capsule's segment.
	 * 
	 * @return the first point of the capsule's segment.
	 */
	@Override
	public Point3d getMedial1() {
		return this.medial1;
	}

	/**
	 * Set the first point of the capsule's segment.
	 * 
	 * @param point the new first point for the capsule's segment..
	 */
	@Override
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
	@Override
	public void setMedial1(double x, double y, double z) {
		this.medial1.set(x, y, z);
		ensureAIsLowerPoint();
	}

	/**
	 * Replies x coordinate of the first point of the capsule's segment.
	 * 
	 * @return the x coordinate of the first point of the capsule's segment.
	 */
	@Override
	public double getMedialX1() {
		return this.medial1.getX();
	}

	/**
	 * Replies y coordinate of the first point of the capsule's segment.
	 * 
	 * @return the y coordinate of the first point of the capsule's segment.
	 */
	@Override
	public double getMedialY1() {
		return this.medial1.getY();
	}

	/**
	 * Replies z coordinate of the first point of the capsule's segment.
	 * 
	 * @return the z coordinate of the first point of the capsule's segment.
	 */
	@Override
	public double getMedialZ1() {
		return this.medial1.getZ();
	}

	/**
	 * Replies the second point of the capsule's segment.
	 * 
	 * @return the second point of the capsule's segment.
	 */
	@Override
	public Point3d getMedial2() {
		return this.medial2;
	}

	/**
	 * Set the second point of the capsule's segment.
	 * 
	 * @param point the new second point for the capsule's segment..
	 */
	@Override
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
	@Override
	public void setMedial2(double x, double y, double z) {
		this.medial2.set(x, y, z);
		ensureAIsLowerPoint();
	}

	/**
	 * Replies x coordinate of the second point of the capsule's segment.
	 * 
	 * @return the x coordinate of the second point of the capsule's segment.
	 */
	@Override
	public double getMedialX2() {
		return this.medial2.getX();
	}

	/**
	 * Replies y coordinate of the second point of the capsule's segment.
	 * 
	 * @return the y coordinate of the second point of the capsule's segment.
	 */
	@Override
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
		return this.radiusProperty.doubleValue();
	}

	/**
	 * Change the radius of the capsule.
	 * 
	 * @param radius1 the radius of the capsule.
	 */
	public void setRadius(double radius1) {
		this.radiusProperty.set(radius1);
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
	@Override
	public void set(double x1, double y1, double z1, double x2, double y2, double z2, double radius1) {
		this.medial1.set(x1, y1, z1);
		this.medial2.set(x2, y2, z2);
		this.radiusProperty.set(radius1);
		ensureAIsLowerPoint();
	}
	
	/** Replies the center point of the capsule.
	 * 
	 * @return the center point.
	 */
	@Override
	public Point3d getCenter() {
		return new Point3d(
				(this.medial1.getX() + this.medial2.getX()) / 2.,
				(this.medial1.getY() + this.medial2.getY()) / 2.,
				(this.medial1.getZ() + this.medial2.getZ()) / 2.);
	}

	/** Replies the x coordinate of the center point of the capsule.
	 * 
	 * @return the center x.
	 */
	@Override
	public double getCenterX() {
		return (this.medial1.getX() + this.medial2.getX()) / 2.;
	}

	/** Replies the y coordinate of the center point of the capsule.
	 * 
	 * @return the center y.
	 */
	@Override
	public double getCenterY() {
		return (this.medial1.getY() + this.medial2.getY()) / 2.;
	}

	/** Replies the z coordinate of the center point of the capsule.
	 * 
	 * @return the center z.
	 */
	@Override
	public double getCenterZ() {
		return (this.medial1.getZ() + this.medial2.getZ()) / 2.;
	}

	@Override
	public PathIterator3f getPathIterator(Transform3D transform) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathIterator3f getPathIterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
