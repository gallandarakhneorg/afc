/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
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

import java.lang.ref.WeakReference;

import org.arakhne.afc.math.geometry.d3.FunctionalPoint3D;
import org.arakhne.afc.math.geometry.d3.FunctionalVector3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

/**This class represents a 3D plane.
 * 
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class Plane4f extends AbstractPlane4F {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2242023029226534976L;
	
	/** equation coefficient A.
	 */
	protected double a;

	/** equation coefficient B.
	 */
	protected double b;

	/** equation coefficient C.
	 */
	protected double c;

	/** equation coefficient D.
	 */
	protected double d;

	/** Cached pivot point.
	 */
	private transient WeakReference<Point3f> cachedPivot = null;

	/**
	 *
	 */
	public Plane4f() {
		this.a = 1;
		this.b = 0;
		this.c = 0;
		this.d = 0;
	}

	/**
	 * @param a is the plane equation coefficient
	 * @param b is the plane equation coefficient
	 * @param c is the plane equation coefficient
	 * @param d is the plane equation coefficient
	 */
	@SuppressWarnings("hiding")
	public Plane4f(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		normalize();
	}

	/**
	 * @param normal is the normal of the plane.
	 * @param p is a point which lies on the plane.
	 */
	public Plane4f(Vector3D normal, Point3D p) {
		this(normal.getX(), normal.getY(), normal.getZ(), p.getX(), p.getY(), p.getZ());
	}

	/**
	 * @param a is the plane equation coefficient
	 * @param b is the plane equation coefficient
	 * @param c is the plane equation coefficient
	 * @param px is the x coordinate of a point which lies on the plane.
	 * @param py is the x coordinate of a point which lies on the plane.
	 * @param pz is the x coordinate of a point which lies on the plane.
	 */
	@SuppressWarnings("hiding")
	public Plane4f(double a, double b, double c, double px, double py, double pz) {
		this.a = a;
		this.b = b;
		this.c = c;
		normalize();
		// a.x + b.y + c.z + d = 0
		// where (x,y,z) is the translation point
		this.d = - (this.a*px + this.b*py + this.c*pz);
	}

	/**
	 * @param plane is the plane to copy
	 */
	public Plane4f(Plane3D<?> plane) {
		this.a = plane.getEquationComponentA();
		this.b = plane.getEquationComponentB();
		this.c = plane.getEquationComponentC();
		this.d = plane.getEquationComponentD();
		normalize();
	}

	/**
	 * @param p1 is a point on the plane
	 * @param p2 is a point on the plane
	 * @param p3 is a point on the plane
	 */
	public Plane4f(Tuple3D<?> p1, Tuple3D<?> p2, Tuple3D<?> p3) {
		set(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ());
	}

	/**
	 * @param p1x is a point on the plane
	 * @param p1y is a point on the plane
	 * @param p1z is a point on the plane
	 * @param p2x is a point on the plane
	 * @param p2y is a point on the plane
	 * @param p2z is a point on the plane
	 * @param p3x is a point on the plane
	 * @param p3y is a point on the plane
	 * @param p3z is a point on the plane
	 */
	public Plane4f(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y, double p3z) {
		set(p1x, p1y, p1z, p2x, p2y, p2z, p3x, p3y, p3z);
	}
	
	
	
	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.a = plane.getEquationComponentA();
		this.b = plane.getEquationComponentB();
		this.c = plane.getEquationComponentC();
		this.d = plane.getEquationComponentD();
		normalize();
	}

	/** Set this pane to be coplanar with all the three specified points.
	 * 
	 * @param p1x is a point on the plane
	 * @param p1y is a point on the plane
	 * @param p1z is a point on the plane
	 * @param p2x is a point on the plane
	 * @param p2y is a point on the plane
	 * @param p2z is a point on the plane
	 * @param p3x is a point on the plane
	 * @param p3y is a point on the plane
	 * @param p3z is a point on the plane
	 */
	@Override
	public void set(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y, double p3z) {
		Vector3f v = new Vector3f();
		FunctionalVector3D.crossProduct(
					p2x-p1x, p2y-p1y, p2z-p1z,
					p3x-p1x, p3y-p1y, p3z-p1z,
					v);
		this.a = v.getX();
		this.b = v.getY();
		this.c = v.getZ();
		this.d = - (this.a * p1x + this.b * p1y + this.c * p1z);
		normalize();

	}

	/** Set this pane to be coplanar with all the three specified points.
	 *  
	 * @param p1 is a point on the plane
	 * @param p2 is a point on the plane
	 * @param p3 is a point on the plane
	 */
	@Override
	public void set(Point3D p1, Point3D p2, Point3D p3) {
		set(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ());
	}

	/** Set this pane with the given factors.
	 *  
	 * @param a is the first factor of the plane equation.
	 * @param b is the first factor of the plane equation.
	 * @param c is the first factor of the plane equation.
	 * @param d is the first factor of the plane equation.
	 */
	@SuppressWarnings("hiding")
	public void set(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		clearBufferedValues();
	}
	

	
	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Vector3f getNormal() {
		return new Vector3f(this.a,this.b,this.c);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double getEquationComponentA() {
		return this.a;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double getEquationComponentB() {
		return this.b;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double getEquationComponentC() {
		return this.c;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double getEquationComponentD() {
		return this.d;
	}
	
	@Override
	public void setPivot(double x, double y, double z) {
		clearBufferedValues();
		// a.x + b.y + c.z + d = 0
		// where (x,y,z) is the translation point
		this.d = - (this.a*x + this.b*y + this.c*z);
		this.cachedPivot = new WeakReference<>(new Point3f(x, y, z));
	}

	/** Replies the pivot point around which the rotation must be done.
	 * 
	 * @return a reference on the buffered pivot point.
	 */
	@Override
	public Point3f getPivot() {
		Point3f pivot = this.cachedPivot == null ? null : this.cachedPivot.get();
		if (pivot==null) {
			pivot = (Point3f) getProjection(0., 0., 0.);
			this.cachedPivot = new WeakReference<>(pivot);
		}
		return pivot;
	}
	
	/** Clear buffered values.
	 */
	@Override
	protected void clearBufferedValues() {
		this.cachedPivot = null;
	}

	@Pure
	@Override
	public FunctionalPoint3D getProjection(double x, double y, double z) {
		return computePointProjection(
				getEquationComponentA(),
				getEquationComponentB(),
				getEquationComponentC(),
				getEquationComponentD(),
				x, y, z);
	}

	@Override
	protected void setEquationComponentC(double z) {
		this.c = z;
		
	}

	@Override
	protected void setEquationComponentB(double y) {
		this.b = y;
		
	}

	@Override
	protected void setEquationComponentA(double x) {
		this.a = x;
		
	}

	@Override
	protected void setEquationComponentD(double w) {
		this.d = w;
		
	}


}
