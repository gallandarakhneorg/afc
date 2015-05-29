/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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

import java.io.Serializable;
import java.lang.ref.SoftReference;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.IntersectionUtil;
import org.arakhne.afc.math.geometry.continuous.intersection.Triangle3f;
import org.arakhne.afc.math.geometry.continuous.object4d.AxisAngle4f;
import org.arakhne.afc.math.geometry.continuous.transform.Transformable3D;
import org.arakhne.afc.math.geometry.system.CoordinateSystem3D;
import org.arakhne.afc.math.geometry2d.continuous.Point2f;
import org.arakhne.afc.math.transform.Transform3D_OLD;


/**
 * A triangle in space. It is defined by three points.
 * <p>
 * A triangle is transformable. So it has a position given
 * by its first point, an orientation given its normal
 * and no scale factor.
 * <p>
 * Additionnaly a triangle may have a pivot point
 * around which rotations will be apply. By default
 * the pivot point is the first point of the triangle.
 * 
 * @author $Author: olamotte$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
public class Triangle3f implements Cloneable, Serializable, Transformable3D {

	private static final long serialVersionUID = 4801325947764161253L;
	
	private final Point3f p1;
	private final Point3f p2;
	private final Point3f p3;
	
	private SoftReference<Vector3f> normal = null;
	private Point3f pivot = null;
	private SoftReference<AxisAngle4f> orientation = null;

	/**
	 * Construct a triangle 3D.
	 * This constructor does not copy the given points.
	 * The triangle's points will be references to the
	 * given points.
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 */
	public Triangle3f(Point3f p1, Point3f p2, Point3f p3) {
		this(p1, p2, p3, false);
	}

	/**
	 * Construct a triangle 3D.
	 * This constructor does not copy the given points.
	 * The triangle's points will be references to the
	 * given points.
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param copyPoints indicates if the given points may be copied
	 * or referenced by this triangle. If <code>true</code> points
	 * will be copied, <code>false</code> points will be referenced.
	 */
	public Triangle3f(Point3f p1, Point3f p2, Point3f p3, boolean copyPoints) {
		if (copyPoints) {
			this.p1 = new Point3f(p1);
			this.p2 = new Point3f(p2);
			this.p3 = new Point3f(p3);
		}
		else {
			this.p1 = p1;
			this.p2 = p2;
			this.p3 = p3;
		}
	}
	
	/**
	 * Construct a triangle 3D.
	 * 
	 * @param p1x is the x coordinate of the first point.
	 * @param p1y is the y coordinate of the first point.
	 * @param p1z is the z coordinate of the first point.
	 * @param p2x is the x coordinate of the first point.
	 * @param p2y is the y coordinate of the first point.
	 * @param p2z is the z coordinate of the first point.
	 * @param p3x is the x coordinate of the first point.
	 * @param p3y is the y coordinate of the first point.
	 * @param p3z is the z coordinate of the first point.
	 */
	public Triangle3f(
			float p1x, float p1y, float p1z,
			float p2x, float p2y, float p2z,
			float p3x, float p3y, float p3z) {
		this.p1 = new Point3f(p1x, p1y, p1z);
		this.p2 = new Point3f(p2x, p2y, p2z);
		this.p3 = new Point3f(p3x, p3y, p3z);
	}

	/**
	 * Checks if a point is inside this triangle.
	 * 
	 * @param point is the the point
	 * @return <code>true</code> if the point is in the triangle , otherwise <code>false</code>.
	 */
	public boolean contains(Point3f point) {
		return IntersectionUtil.intersectsPointTriangle(
				point.getX(), point.getY(), point.getZ(),
				this.p1.getX(), this.p1.getY(), this.p1.getZ(), 
				this.p2.getX(), this.p2.getY(), this.p2.getZ(), 
				this.p3.getX(), this.p3.getY(), this.p3.getZ(),
				true, 0f);
	}
	
	/**
	 * Checks if the projection of a point on the triangle's plane is inside the triangle.
	 * 
	 * @param point is the the point
	 * @return <code>true</code> if the point is in the triangle , otherwise <code>false</code>.
	 */
	public boolean containsProjectionOf(Point3f point) {
		
		//TODO project point on the triangle's plane
		return IntersectionUtil.intersectsPointTriangle(
				point.getX(), point.getY(), point.getZ(),
				this.p1.getX(), this.p1.getY(), this.p1.getZ(), 
				this.p2.getX(), this.p2.getY(), this.p2.getZ(), 
				this.p3.getX(), this.p3.getY(), this.p3.getZ(),
				true, 0f);
	}
	
	/** Replies the normal to this triangle face.
	 * 
	 * @return the normal.
	 */
	public Vector3f getNormal() {
		Vector3f v = null;
		if (this.normal!=null) {
			v = this.normal.get();
		}
		if (v==null) {
			v = new Vector3f();
			if (CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded()) {
				MathUtil.crossProductLeftHand(
						this.p2.getX() - this.p1.getX(),
						this.p2.getY() - this.p1.getY(),
						this.p2.getZ() - this.p1.getZ(),
						this.p3.getX() - this.p1.getX(),
						this.p3.getY() - this.p1.getY(),
						this.p3.getZ() - this.p1.getZ(),
						v);
			}
			else {
				MathUtil.crossProductRightHand(
						this.p2.getX() - this.p1.getX(),
						this.p2.getY() - this.p1.getY(),
						this.p2.getZ() - this.p1.getZ(),
						this.p3.getX() - this.p1.getX(),
						this.p3.getY() - this.p1.getY(),
						this.p3.getZ() - this.p1.getZ(),
						v);
			}
			v.normalize();
			this.normal = new SoftReference<>(v);
		}
		return v;
	}
		
	/** Replies the plane on which this triangle is coplanar.
	 * 
	 * @return the coplanar plane to this triangle
	 */
	public Plane getPlane() {
		Vector3f norm = getNormal();
		assert(norm!=null);
		if (norm.getY()==0. && norm.getZ()==0.)
			return new YZPlane(this.p1.getX());
		if (norm.getX()==0. && norm.getZ()==0.)
			return new XZPlane(this.p1.getY());
		if (norm.getX()==0. && norm.getY()==0.)
			return new XYPlane(this.p1.getZ());
		return new Plane4f(this.p1, this.p2, this.p3);
	}

	/**
	 * Replies the first point of the triangle.
	 * 
	 * @return the first point of the triangle.
	 */
	public Point3f getPoint1() {
		return this.p1;
	}

	/**
	 * Replies the second point of the triangle.
	 * 
	 * @return the second point of the triangle.
	 */
	public Point3f getPoint2() {
		return this.p2;
	}

	/**
	 * Replies the third point of the triangle.
	 * 
	 * @return the third point of the triangle.
	 */
	public Point3f getPoint3() {
		return this.p3;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("["); //$NON-NLS-1$
		buffer.append(this.p1.getX());
		buffer.append(";"); //$NON-NLS-1$
		buffer.append(this.p1.getY());
		buffer.append(";"); //$NON-NLS-1$
		buffer.append(this.p1.getZ());
		buffer.append("]-["); //$NON-NLS-1$
		buffer.append(this.p2.getX());
		buffer.append(";"); //$NON-NLS-1$
		buffer.append(this.p2.getY());
		buffer.append(";"); //$NON-NLS-1$
		buffer.append(this.p2.getZ());
		buffer.append("]-["); //$NON-NLS-1$
		buffer.append(this.p3.getX());
		buffer.append(";"); //$NON-NLS-1$
		buffer.append(this.p3.getY());
		buffer.append(";"); //$NON-NLS-1$
		buffer.append(this.p3.getZ());
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

	/**
     * Replies the distance from the given point to the triangle.
     * <p>
     * If the point is on the same plane as the triangle, the
     * distance between the point and the nearest segment
     * is computed.
     *
     * @param x is the x-coordinate of the point.
     * @param y is the y-coordinate of the point.
     * @param z is the z-coordinate of the point.
     * @return the distance from the triangle to the point.
     */
    public float distanceTo(float x, float y, float z) {
    	if (IntersectionUtil.intersectsPointTriangle(
    			x, y, z,
    			this.p1.getX(), this.p1.getY(), this.p1.getZ(),
    			this.p2.getX(), this.p2.getY(), this.p2.getZ(),
    			this.p3.getX(), this.p3.getY(), this.p3.getZ(),
    			false)) {
    		Vector3f n = getNormal();
    		float d = -(n.getX() * this.p1.getX() + n.getY() * this.p1.getY() + n.getZ() * this.p1.getZ());
    		return n.getX() * x + n.getY() * y + n.getZ() * z + d;
    	}
    	return MathUtil.min(
    			MathUtil.distancePointSegment(
    					x, y, z,
    					this.p1.getX(), this.p1.getY(), this.p1.getZ(),
    					this.p2.getX(), this.p2.getY(), this.p2.getZ()),
				MathUtil.distancePointSegment(
    					x, y, z,
    					this.p1.getX(), this.p1.getY(), this.p1.getZ(),
    					this.p3.getX(), this.p3.getY(), this.p3.getZ()),
				MathUtil.distancePointSegment(
    					x, y, z,
    					this.p2.getX(), this.p2.getY(), this.p2.getZ(),
    					this.p3.getX(), this.p3.getY(), this.p3.getZ()));
    }

    /**
     * Replies the distance from the given point to the plane.
     * <p>
     * If the point is on the same plane as the triangle, the
     * distance between the point and the nearest segment
     * is computed.
     *
     * @param v is the point.
     * @return the distance from the triangle to the point.
     */
    public final float distanceTo(Tuple3f<?> v) {
    	assert(v!=null);
    	return distanceTo(v.getX(), v.getY(), v.getZ());
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final AxisAngle4f getAxisAngle() {
		AxisAngle4f orient = null;
		if (this.orientation!=null) {
			orient = this.orientation.get();
		}
		if (orient==null) {
			Vector3f norm = getNormal();
			assert(norm!=null);
			CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
			assert(cs!=null);
			Vector3f up = cs.getUpVector();
			assert(up!=null);
			Vector3f axis = new Vector3f();
			if (CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded()) {
				MathUtil.crossProductLeftHand(
						up.getX(), up.getY(), up.getZ(),
						norm.getX(), norm.getY(), norm.getZ(),
						axis);
			}
			else {
				MathUtil.crossProductRightHand(
						up.getX(), up.getY(), up.getZ(),
						norm.getX(), norm.getY(), norm.getZ(),
						axis);
			}
			axis.normalize();
			orient = new AxisAngle4f(axis, 
					MathUtil.signedAngle(
							up.getX(), up.getY(), up.getZ(),
							norm.getX(), norm.getY(), norm.getZ()));
			this.orientation = new SoftReference<>(orient);
		}
		return orient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point3f getPivot() {
		return this.pivot==null ? this.p1 : this.pivot;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Tuple3f<?> getScale() {
		return new Vector3f(1., 1., 1.);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Transform3D_OLD getTransformMatrix() {
		Transform3D_OLD tr = new Transform3D_OLD();
		tr.setTranslation(this.p1);
		tr.setRotation(getAxisAngle());
		return tr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point3f getTranslation() {
		return this.p1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void rotate(AxisAngle4f quaternion) {
		rotate(quaternion, getPivot());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void rotate(Quaternion quaternion) {
		rotate(quaternion, getPivot());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void rotate(AxisAngle4f quaternion, Point3f pivot) {
		assert(pivot!=null);
		assert(quaternion!=null);
		
		Transform3D_OLD tr = new Transform3D_OLD();
		tr.setRotation(quaternion);
		Vector3f v = new Vector3f();
		
		v.sub(this.p1, pivot);
		tr.transform(v);
		this.p1.add(pivot, v);
		
		v.sub(this.p2, pivot);
		tr.transform(v);
		this.p2.add(pivot, v);

		v.sub(this.p3, pivot);
		tr.transform(v);
		this.p3.add(pivot, v);

		this.normal = null;
		this.orientation = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void rotate(Quaternion quaternion, Point3f pivot) {
		assert(pivot!=null);
		assert(quaternion!=null);
		
		Transform3D_OLD tr = new Transform3D_OLD();
		tr.setRotation(quaternion);
		Vector3f v = new Vector3f();
		
		v.sub(this.p1, pivot);
		tr.transform(v);
		this.p1.add(pivot, v);
		
		v.sub(this.p2, pivot);
		tr.transform(v);
		this.p2.add(pivot, v);

		v.sub(this.p3, pivot);
		tr.transform(v);
		this.p3.add(pivot, v);

		this.normal = null;
		this.orientation = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void scale(float sx, float sy, float sz) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setIdentityTransform() {
		setTransform(new Transform3D_OLD());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setPivot(float x, float y, float z) {
		this.pivot = new Point3f(x, y, z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setPivot(Point3f point) {
		if (this.pivot==null)
			this.pivot = new Point3f(point);
		else
			this.pivot.set(point);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setRotation(Quaternion quaternion) {
		setRotation(quaternion, getPivot());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setRotation(AxisAngle4f quaternion) {
		setRotation(quaternion, getPivot());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setRotation(Quaternion quaternion, Point3f pivot) {
		assert(pivot!=null);
		assert(quaternion!=null);

		Transform3D_OLD tr = new Transform3D_OLD();
		tr.setRotation(quaternion);
		
		Vector3f norm = getNormal();
		assert(norm!=null);
		
		Vector3f newNorm = new Vector3f(0, 0, 1);
		tr.transform(newNorm);
		
		Vector3f rotAxis = new Vector3f();
		rotAxis.cross(norm, newNorm);
		
		float angle = MathUtil.signedAngle(norm.getX(), norm.getY(), norm.getZ(), newNorm.getX(), newNorm.getY(), newNorm.getZ());
		
		rotate(new AxisAngle4f(rotAxis, angle));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setRotation(AxisAngle4f quaternion, Point3f pivot) {
		assert(pivot!=null);
		assert(quaternion!=null);

		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		
		Transform3D_OLD tr = new Transform3D_OLD();
		tr.setRotation(quaternion);
		
		Vector3f norm = getNormal();
		assert(norm!=null);
		
		Vector3f newNorm = new Vector3f();
		cs.getUpVector(newNorm);
		tr.transform(newNorm);
		
		Vector3f rotAxis = new Vector3f();
		if (cs.isLeftHanded())
			MathUtil.crossProductLeftHand(
					norm.getX(), norm.getY(), norm.getZ(),
					newNorm.getX(), newNorm.getY(), newNorm.getZ(),
					rotAxis);
		else
			MathUtil.crossProductRightHand(
					norm.getX(), norm.getY(), norm.getZ(),
					newNorm.getX(), newNorm.getY(), newNorm.getZ(),
					rotAxis);
		
		float angle = MathUtil.signedAngle(norm.getX(), norm.getY(), norm.getZ(), newNorm.getX(), newNorm.getY(), newNorm.getZ());
		
		rotate(new AxisAngle4f(rotAxis, angle));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setScale(float sx, float sy, float sz) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setTransform(Transform3D_OLD trans) {
		Quaternion q = new Quaternion();
		trans.get(q);
		setRotation(q, getPivot());
		setTranslation(trans.m30, trans.m31, trans.m32);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(float x, float y, float z) {
		float vx, vy, vz;
		
		vx = this.p2.getX() - this.p1.getX();
		vy = this.p2.getY() - this.p1.getY();
		vz = this.p2.getZ() - this.p1.getZ();
		this.p2.set(x+vx, y+vy, z+vz);

		vx = this.p3.getX() - this.p1.getX();
		vy = this.p3.getY() - this.p1.getY();
		vz = this.p3.getZ() - this.p1.getZ();
		this.p3.set(x+vx, y+vy, z+vz);
		
		this.p1.set(x, y, z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setTranslation(Point3f position) {
		setTranslation(position.getX(), position.getY(), position.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void transform(Transform3D_OLD trans) {
		Quaternion q = new Quaternion();
		trans.get(q);
		rotate(q, getPivot());
		translate(trans.m30, trans.m31, trans.m32);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translate(float dx, float dy, float dz) {
		this.p1.setX(this.p1.getX() + dx);
		this.p1.setY(this.p1.getY() + dy);
		this.p1.setZ(this.p1.getZ() + dz);
		this.p2.setX(this.p2.getX() + dx);
		this.p2.setY(this.p2.getY() + dy);
		this.p2.setZ(this.p2.getZ() + dz);
		this.p3.setX(this.p3.getX() + dx);
		this.p3.setY(this.p3.getY() + dy);
		this.p3.setZ(this.p3.getZ() + dz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translate(Vector3f v) {
		assert(v!=null);
		this.p1.add(v);
		this.p2.add(v);
		this.p3.add(v);
	}

	/**
	 * Replies the height of the 2D point when projected
	 * on this facet.
	 * <p>
	 * Height depends on the current coordinate system.
	 * 
	 * @param x is the x-coordinate of the 2D point.
	 * @param y is the x-coordinate of the 2D point.
	 * @return the height of the 2D point when projected on the facet.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 */
	public float interpolateHeight(float x, float y) {
		return interpolateHeight(x, y, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the height of the 2D point when projected
	 * on this facet.
	 * 
	 * @param x is the x-coordinate of the 2D point.
	 * @param y is the x-coordinate of the 2D point.
	 * @param system is the coordinate system to use.
	 * @return the height of the 2D point when projected on the facet.
	 */
	public float interpolateHeight(float x, float y, CoordinateSystem3D system) {
		assert(system!=null);
		int idx = system.getHeightCoordinateIndex();
		assert(idx==1 || idx==2);
		Point3f p1 = getPoint1();
		assert(p1!=null);
		Vector3f v = getNormal();
		assert(v!=null);
		
		if (idx==1 && v.getY()==0.)
			return p1.getY();
		if (idx==2 && v.getZ()==0.)
			return p1.getZ();
		
		float d = -(v.getX() * p1.getX() + v.getY() * p1.getY() + v.getZ() * p1.getZ());
		
		if (idx==2)
			return -(v.getX() * x + v.getY() * y + d) / v.getZ();
		
		return -(v.getX() * x + v.getZ() * y + d) / v.getY();
	}

	/**
	 * Replies the height of the 2D point when projected
	 * on this facet.
	 * <p>
	 * Height depends on the current coordinate system.
	 * 
	 * @param point is the 2D point.
	 * @return the height of the 2D point when projected on the facet.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 */
	public final float interpolateHeight(Point2f point) {
		return interpolateHeight(point.getX(), point.getY());
	}

	/**
	 * Replies the height of the 2D point when projected
	 * on this facet.
	 * 
	 * @param point is the 2D point.
	 * @param system is the coordinate system to use.
	 * @return the height of the 2D point when projected on the facet.
	 */
	public float interpolateHeight(Point2f point, CoordinateSystem3D system) {
		return interpolateHeight(point.getX(), point.getY(), system);
	}

	/**
	 * Replies the 3D point of the 2D point when projected
	 * on this facet.
	 * 
	 * @param x is the x-coordinate of the 2D point.
	 * @param y is the x-coordinate of the 2D point.
	 * @return the 3D point which is corresponding to the 3D point on the facet.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 */
	public Point3f interpolatePoint(float x, float y) {
		return interpolatePoint(x,y,CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the 3D point of the 2D point when projected
	 * on this facet.
	 * 
	 * @param x is the x-coordinate of the 2D point.
	 * @param y is the x-coordinate of the 2D point.
	 * @param system is the coordinate system to use.
	 * @return the 3D point which is corresponding to the 3D point on the facet.
	 */
	public Point3f interpolatePoint(float x, float y, CoordinateSystem3D system) {
		assert(system!=null);
		int idx = system.getHeightCoordinateIndex();
		assert(idx==1 || idx==2);
		Point3f p1 = getPoint1();
		assert(p1!=null);
		Vector3f v = getNormal();
		assert(v!=null);
		
		if (idx==1 && v.getY()==0.)
			return new Point3f(x, p1.getY(), y);
		if (idx==2 && v.getZ()==0.)
			return new Point3f(x, y, p1.getZ());
		
		float d = -(v.getX() * p1.getX() + v.getY() * p1.getY() + v.getZ() * p1.getZ());
		
		if (idx==2)
			return new Point3f(x, y, -(v.getX() * x + v.getY() * y + d) / v.getZ());
		
		return new Point3f(x, -(v.getX() * x + v.getZ() * y + d) / v.getY(), y);
	}

	/**
	 * Replies the 3D point of the 2D point when projected
	 * on this facet.
	 * 
	 * @param point is the the point
	 * @return the 3D point which is corresponding to the 3D point on the facet.
	 * @see CoordinateSystem3D#getDefaultCoordinateSystem()
	 */
	public final Point3f interpolatePoint(Point2f point) {
		return interpolatePoint(point.getX(), point.getY(), CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the 3D point of the 2D point when projected
	 * on this facet.
	 * 
	 * @param point is the 2D point.
	 * @param system is the coordinate system to use.
	 * @return the 3D point which is corresponding to the 3D point on the facet.
	 */
	public Point3f interpolatePoint(Point2f point, CoordinateSystem3D system) {
		return interpolatePoint(point.getX(), point.getY(), system);
	}

	/** Replies if a point is inside a triangle.
	 */
	public static boolean pointInsideTriangle(int i0, int i1, float[] v, float[] u1, float[] u2, float[] u3) {
		// is T1 completly inside T2?
		// check if V0 is inside tri(U0,U1,U2)
		float a  = u2[i1] - u1[i1];
		float b  = -(u2[i0] - u1[i0]);
		float c  = -a * u1[i0] - b * u1[i1];
		float d0 = a * v[i0] + b * v[i1] + c;
	
		a         = u3[i1] - u2[i1];
		b         = -(u3[i0] - u2[i0]);
		c         = -a * u2[i0] - b * u2[i1];
		float d1 = a * v[i0] + b * v[i1] + c;
	
		a         = u1[i1] - u2[i1];
		b         = -(u1[i0] - u3[i0]);
		c         = -a * u3[i0] - b * u3[i1];
		float d2 = a * v[i0] + b * v[i1] + c;
	
		return ((d0*d1>0.)&&(d0*d2>0.0));
	}

	/** Replies how a segment is connected to a triangle.
	 * 
	 * @return <code>0</code> when not connected,
	 * <code>1</code> when the first segment's vertex was connected,
	 * <code>2</code> when the second segment's vertex was connected,
	 * <code>3</code> when both segment vertices were connected.
	 */
	public static int getSegmentConnection(int i0, int i1, float[] s1, float[] s2, float[] u1, float[] u2, float[] u3) {
		// Test if the segment is connected to at least one point of the triangle
		int con = 0;
		
		if (((s1[i0]==u1[i0])&&(s1[i1]==u1[i1]))||
			((s1[i0]==u2[i0])&&(s1[i1]==u2[i1]))||
			((s1[i0]==u3[i0])&&(s1[i1]==u3[i1]))) con |= 1;
		
		if (((s2[i0]==u1[i0])&&(s2[i1]==u1[i1]))||
			((s2[i0]==u2[i0])&&(s2[i1]==u2[i1]))||
			((s2[i0]==u3[i0])&&(s2[i1]==u3[i1]))) con |= 2;
		
		return con;
	}
	
    /** Replies if two coplanar triangles intersect.
     * Triangles intersect even if they are connected by two of their
     * edges.
     * <p>
     * Triangle/triangle intersection test routine,
     * by Tomas Moller, 1997.
     * See article "A Fast Triangle-Triangle Intersection Test",
     * Journal of Graphics Tools, 2(2), 1997.
     * 
     * @param v1
     * @param v2
     * @param v3
     * @param u1
     * @param u2
     * @param u3
     * @return <code>true</code> if the two triangles are intersecting.
     * @see Triangle3f#overlapsCoplanarTriangle(Tuple3f, Tuple3f, Tuple3f, Tuple3f, Tuple3f, Tuple3f)
     */
    public static boolean intersectsCoplanarTriangle(float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, float v3x, float v3y, float v3z,
    												 float u1x, float u1y, float u1z, float u2x, float u2y, float u2z, float u3x, float u3y, float u3z) {
    	
    	int i0, i1;
    	
    	// first project onto an axis-aligned plane, that maximizes the area
    	// of the triangles, compute indices: i0,i1.
    	{
    		float nx = v1y * (v2z - v3z) + v2y * (v3z - v1z) + v3y * (v1z - v2z);
    		float ny = v1z * (v2x - v3x) + v2z * (v3x - v1x) + v3z * (v1x - v2x);
    		float nz = v1x * (v2y - v3y) + v2x * (v3y - v1y) + v3x * (v1y - v2y);
        	
	    	nx = (nx<0) ? -nx : nx;
	    	ny = (ny<0) ? -ny : ny;
	    	nz = (nz<0) ? -nz : nz;
	    	
	    	if(nx>ny) {
	    		if(nx>nz) {
	    			// nx is greatest
	    			i0 = 1;
	    			i1 = 2;
	    		}
	    		else {
	    			// nz is greatest
	    			i0 = 0;
	    			i1 = 1;
	    		}
	    	}
	    	else {   /* nx<=ny */
	    		if(nz>ny) {
	    			// nz is greatest
	    			i0 = 0;
	    			i1 = 1;                                           
	    		}
	    		else {
	    			// ny is greatest
	    			i0 = 0;
	    			i1 = 2;
	    		}
	    	}
    	}

    	float[] tv1 = new float[] {v1x,v1y,v1z};
    	float[] tv2 = new float[] {v2x,v2y,v2z};
    	float[] tv3 = new float[] {v3x,v3y,v3z};
    	float[] tu1 = new float[] {u1x,u1y,u1z};
    	float[] tu2 = new float[] {u2x,u2y,u2z};
    	float[] tu3 = new float[] {u3x,u3y,u3z};
    	
    	// test all edges of triangle 1 against the edges of triangle 2
    	if (intersectsCoplanarTriangle(i0,i1,0,tv1,tv2,tu1,tu2,tu3)) return true;
    	if (intersectsCoplanarTriangle(i0,i1,0,tv2,tv3,tu1,tu2,tu3)) return true;
    	if (intersectsCoplanarTriangle(i0,i1,0,tv3,tv1,tu1,tu2,tu3)) return true;

    	// finally, test if tri1 is totally contained in tri2 or vice versa
    	if (Triangle3f.pointInsideTriangle(i0,i1,tv1,tu1,tu2,tu3)) return true;		//TODO : créer un isInsideUtil sur le modèle de classifie et intersect
    	if (Triangle3f.pointInsideTriangle(i0,i1,tu1,tv1,tv2,tv3)) return true;

    	return false;
    }

    /** Replies if coplanar segment-triangle intersect.
     */
    private static boolean intersectsCoplanarTriangle(int i0, int i1, int con, float[] s1, float[] s2, float[] u1, float[] u2, float[] u3) {//TODO : virer les tableaux
    	float Ax,Ay;

    	Ax = s2[i0] - s1[i0];
    	Ay = s2[i1] - s1[i1];
    	
    	// test edge U0,U1 against V0,V1
    	if (intersectEdgeEdge(i0, i1, con, Ax, Ay, s1, u1, u2)) return true;
    	// test edge U1,U2 against V0,V1
    	if (intersectEdgeEdge(i0, i1, con, Ax, Ay, s1, u2, u3)) return true;
    	// test edge U2,U1 against V0,V1
    	if (intersectEdgeEdge(i0, i1, con, Ax, Ay, s1, u3, u1)) return true;
    	
    	return false;
    }

    /** This edge to edge test is based on Franlin Antonio's gem:
     * "Faster Line Segment Intersection", in Graphics Gems III,
     * pp. 199-202.
     */   
    private static boolean intersectEdgeEdge(int i0, int i1, int con, float Ax, float Ay, float[] v, float[] u1, float[] u2) {//TODO : meme chose.
    	// [v,b] is the segment that contains the point v
    	// [c,d] is the segment [u1,u2]
    	
    	// A is the vector (v,b)
    	// B is the vector (d,c)
    	// C is the vector (c,v)
    	
    	float Bx = u1[i0] - u2[i0];
    	float By = u1[i1] - u2[i1];
    	float Cx = v[i0]  - u1[i0];
    	float Cy = v[i1]  - u1[i1];
    	
    	// 
    	
    	float f = Ay * Bx - Ax * By;
    	float d = By * Cx - Bx * Cy; // Line equation: V+d*A
    	
    	boolean up = false;
    	boolean down = false;
    	
		if (f>0) {
			switch(con) {
			case 1: // First point must be ignored
				down = (d>0);
				up = (d<=f);
				break;
			case 2: // Second point must be ignored
				down = (d>=0);
				up = (d<f);
				break;
			case 3: // First and Second points must be ignored
				down = (d>0);
				up = (d<f);
				break;
			default:
				down = (d>=0);
				up = (d<=f);
			}
		}
		else if (f<0) {
			switch(con) {
			case 1: // First point must be ignored
				down = (d>=f);
				up = (d<0);
				break;
			case 2: // Second point must be ignored
				down = (d>f);
				up = (d<=0);
				break;
			case 3: // First and Second points must be ignored
				down = (d>f);
				up = (d<0);
				break;
			default:
				down = (d>=f);
				up = (d<=0);
			}
		}
    	
		if (up&&down) {
    		float e = Ax * Cy - Ay * Cx;
    		if (f>=0) return ((e>=0)&&(e<=f));
			return ((e>=f)&&(e<=0));
		}
    	
    	return false;
    }

}
