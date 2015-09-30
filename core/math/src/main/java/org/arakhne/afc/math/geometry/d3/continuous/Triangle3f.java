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
package org.arakhne.afc.math.geometry.d3.continuous;

import java.lang.ref.SoftReference;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.FunctionalPoint3D;
import org.arakhne.afc.math.geometry.d3.FunctionalVector3D;
import org.arakhne.afc.math.geometry.d3.Point3D;


/**
 * A triangle in space. It is defined by three points.
 * <p>
 * A triangle is transformable. So it has a position given
 * by its first point, an orientation given its normal
 * and no scale factor.
 * 
 * @author $Author: olamotte$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Triangle3f extends AbstractTriangle3F {

	private static final long serialVersionUID = 566634934114601285L;

	/** First point.
	 */
	protected final Point3f p1;

	/** Second point.
	 */
	protected final Point3f p2;

	/** Third point.
	 */
	protected final Point3f p3;

	private SoftReference<Vector3f> normal = null;
	private Point3f pivot = null;
	private SoftReference<Quaternion> orientation = null;

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
	@SuppressWarnings("hiding")
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
	@SuppressWarnings("hiding")
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
			double p1x, double p1y, double p1z,
			double p2x, double p2y, double p2z,
			double p3x, double p3y, double p3z) {
		this.p1 = new Point3f(p1x, p1y, p1z);
		this.p2 = new Point3f(p2x, p2y, p2z);
		this.p3 = new Point3f(p3x, p3y, p3z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getNormal() {
		Vector3f v = null;
		if (this.normal!=null) {
			v = this.normal.get();
		}
		if (v==null) {
			v = new Vector3f();
			FunctionalVector3D.crossProduct(
					this.p2.getX() - this.p1.getX(),
					this.p2.getY() - this.p1.getY(),
					this.p2.getZ() - this.p1.getZ(),
					this.p3.getX() - this.p1.getX(),
					this.p3.getY() - this.p1.getY(),
					this.p3.getZ() - this.p1.getZ(),
					v);
			v.normalize();
			this.normal = new SoftReference<>(v);
		}
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point3f getP1() {
		return this.p1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setP1(Point3D point) {
		setP1(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setP1(double x, double y, double z) {
		this.p1.set(x, y, z);
		clearBufferedData();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point3f getP2() {
		return this.p2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setP2(Point3D point) {
		setP2(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setP2(double x, double y, double z) {
		this.p2.set(x, y, z);
		clearBufferedData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point3f getP3() {
		return this.p3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setP3(Point3D point) {
		setP3(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setP3(double x, double y, double z) {
		this.p3.set(x, y, z);
		clearBufferedData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getX1() {
		return this.p1.getX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getY1() {
		return this.p1.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getZ1() {
		return this.p1.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getX2() {
		return this.p2.getX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getY2() {
		return this.p2.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getZ2() {
		return this.p2.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getX3() {
		return this.p3.getX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getY3() {
		return this.p3.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getZ3() {
		return this.p3.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("hiding")
	public void set(Point3D p1, Point3D p2, Point3D p3) {
		this.p1.set(p1);
		this.p2.set(p2);
		this.p3.set(p3);
		clearBufferedData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Quaternion getOrientation() {
		Quaternion orient = null;
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
			FunctionalVector3D.crossProduct(
					up.getX(), up.getY(), up.getZ(),
					norm.getX(), norm.getY(), norm.getZ(),
					cs, axis);
			axis.normalize();
			orient = new Quaternion();
			orient.setAxisAngle(
					axis, 
					FunctionalVector3D.signedAngle(
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
		return this.pivot==null ? this.p1.clone() : this.pivot.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPivot(double x, double y, double z) {
		this.pivot = new Point3f(x, y, z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPivot(FunctionalPoint3D point) {
		if (point == null) {
			this.pivot = null;
		} else if (this.pivot == null) {
			this.pivot = new Point3f(point);
		} else {
			this.pivot.set(point);
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNormal(SoftReference<FunctionalVector3D> normal1) {
		Vector3f v = new Vector3f(normal1.get());
		SoftReference<Vector3f> sr = new SoftReference<>(v);
		this.normal = sr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOrientation(SoftReference<Quaternion> orientation1) {
		this.orientation = orientation1;
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
