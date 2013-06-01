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
package org.arakhne.afc.math.object;

import java.io.Serializable;
import java.lang.ref.SoftReference;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Quaternion;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.continous.object4d.AxisAngle4f;
import org.arakhne.afc.math.intersection.IntersectionUtil;
import org.arakhne.afc.math.plane.Plane;
import org.arakhne.afc.math.plane.Plane4f;
import org.arakhne.afc.math.plane.XYPlane;
import org.arakhne.afc.math.plane.XZPlane;
import org.arakhne.afc.math.plane.YZPlane;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.afc.math.transform.Transform3D;
import org.arakhne.afc.math.transform.Transformable3D;


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
public class Triangle3D implements Cloneable, Serializable, Transformable3D {

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
	public Triangle3D(Point3f p1, Point3f p2, Point3f p3) {
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
	public Triangle3D(Point3f p1, Point3f p2, Point3f p3, boolean copyPoints) {
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
	public Triangle3D(
			float p1x, float p1y, float p1z,
			float p2x, float p2y, float p2z,
			float p3x, float p3y, float p3z) {
		this.p1 = new Point3f(p1x, p1y, p1z);
		this.p2 = new Point3f(p2x, p2y, p2z);
		this.p3 = new Point3f(p3x, p3y, p3z);
	}

	/**
	 * Checks if a point is in this triangle.
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
				true);
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
	public final Transform3D getTransformMatrix() {
		Transform3D tr = new Transform3D();
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
		
		Transform3D tr = new Transform3D();
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
		
		Transform3D tr = new Transform3D();
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
		setTransform(new Transform3D());
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

		Transform3D tr = new Transform3D();
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
		
		Transform3D tr = new Transform3D();
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
	public final void setTransform(Transform3D trans) {
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
	public final void transform(Transform3D trans) {
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

}
