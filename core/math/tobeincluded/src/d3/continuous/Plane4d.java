/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Hamza JAFFALI.
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
import java.util.concurrent.Callable;

import org.arakhne.afc.math.geometry.d3.FunctionalVector3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


/**This class represents a 3D plane.
 * 
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Plane4d extends AbstractPlane4F {

	
	private static final long serialVersionUID = -934785950628401429L;

	/** equation coefficient A.
	 */
	protected DoubleProperty aProperty;

	/** equation coefficient B.
	 */
	protected DoubleProperty bProperty;

	/** equation coefficient C.
	 */
	protected DoubleProperty cProperty;

	/** equation coefficient D.
	 */
	protected DoubleProperty dProperty;

	/** Cached pivot point.
	 */
	private transient WeakReference<Point3d> cachedPivot = null;

	/**
	 *
	 */
	public Plane4d() {
		this(1,0,0,0);
	}

	/**
	 * @param a is the plane equation coefficient
	 * @param b is the plane equation coefficient
	 * @param c is the plane equation coefficient
	 * @param d is the plane equation coefficient
	 */
	public Plane4d(double a, double b, double c, double d) {
		this.aProperty = new SimpleDoubleProperty(a);
		this.bProperty = new SimpleDoubleProperty(b);
		this.cProperty = new SimpleDoubleProperty(c);
		this.dProperty = new SimpleDoubleProperty(d);
		normalize();
	}
	
	/**
	 * @param a is the plane equation coefficient
	 * @param b is the plane equation coefficient
	 * @param c is the plane equation coefficient
	 * @param d is the plane equation coefficient
	 */
	public Plane4d(DoubleProperty a, DoubleProperty b, DoubleProperty c, DoubleProperty d) {
		this.aProperty = a;
		this.bProperty = b;
		this.cProperty = c;
		this.dProperty = d;
		normalize();
	}

	/**
	 * @param normal is the normal of the plane.
	 * @param p is a point which lies on the plane.
	 */
	public Plane4d(Vector3D normal, Point3D p) {
		this(normal.getX(), normal.getY(), normal.getZ(), p.getX(), p.getY(), p.getZ());
	}
	
	/**The dProperty is binded to the properties of a,b,c coefficients, and to the Point3d point in parameter.
	 * 
	 * If the point moves, the coefficient d will change. On the other hand, if the d coefficient change, the 
	 * point will not be affected
	 * 
	 * @param normal is the normal of the plane.
	 * @param p is a point which lies on the plane.
	 */
	public Plane4d(Vector3d normal, Point3d p) {
		this.aProperty = normal.xProperty;
		this.bProperty = normal.yProperty;
		this.cProperty = normal.zProperty;
		normalize();
		// a.x + b.y + c.z + d = 0
		// where (x,y,z) is the translation point
		this.dProperty = new SimpleDoubleProperty();

		this.dProperty.bind(Bindings.createDoubleBinding(new Callable<Double>() {
			@Override
			public Double call() throws Exception {
				return new Double(- (Plane4d.this.aProperty.doubleValue()*p.xProperty.doubleValue()+ Plane4d.this.bProperty.doubleValue()*p.yProperty.doubleValue() + Plane4d.this.cProperty.doubleValue()*p.zProperty.doubleValue()));
			}
		}, this.aProperty, this.bProperty, this.cProperty,p.xProperty,p.yProperty,p.zProperty));
		
	}

	/**
	 * @param a is the plane equation coefficient
	 * @param b is the plane equation coefficient
	 * @param c is the plane equation coefficient
	 * @param px is the x coordinate of a point which lies on the plane.
	 * @param py is the x coordinate of a point which lies on the plane.
	 * @param pz is the x coordinate of a point which lies on the plane.
	 */
	public Plane4d(double a, double b, double c, double px, double py, double pz) {
		this.aProperty = new SimpleDoubleProperty(a);
		this.bProperty = new SimpleDoubleProperty(b);
		this.cProperty = new SimpleDoubleProperty(c);
		normalize();
		// a.x + b.y + c.z + d = 0
		// where (x,y,z) is the translation point
		this.dProperty = new SimpleDoubleProperty(- (this.getEquationComponentA()*px + this.getEquationComponentB()*py + this.getEquationComponentC()*pz));
	}

	/**
	 * @param plane is the plane to copy
	 */
	public Plane4d(Plane3D<?> plane) {
		this(plane.getEquationComponentA(),plane.getEquationComponentB(),plane.getEquationComponentC(),plane.getEquationComponentD());
	}
	
	/**
	 * @param plane is the plane to bind properties
	 */
	public Plane4d(Plane4d plane) {
		this(plane.aProperty,plane.bProperty,plane.cProperty,plane.dProperty);
	}

	/**
	 * @param p1 is a point on the plane
	 * @param p2 is a point on the plane
	 * @param p3 is a point on the plane
	 */
	public Plane4d(Tuple3D<?> p1, Tuple3D<?> p2, Tuple3D<?> p3) {
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
	public Plane4d(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y, double p3z) {
		this();
		set(p1x, p1y, p1z, p2x, p2y, p2z, p3x, p3y, p3z);
	}
	
	
	
	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.aProperty.set(plane.getEquationComponentA());
		this.bProperty.set(plane.getEquationComponentB());
		this.cProperty.set(plane.getEquationComponentC());
		this.dProperty.set(plane.getEquationComponentD());
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
		Vector3d v = new Vector3d();
		FunctionalVector3D.crossProduct(
					p2x-p1x, p2y-p1y, p2z-p1z,
					p3x-p1x, p3y-p1y, p3z-p1z,
					v);
		this.aProperty.set(v.getX());
		this.bProperty.set(v.getY());
		this.cProperty.set(v.getZ());
		this.dProperty.set(- (this.getEquationComponentA() * p1x + this.getEquationComponentB() * p1y + this.getEquationComponentC() * p1z));
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
	public void set(double a, double b, double c, double d) {
		this.aProperty.set(a);
		this.bProperty.set(b);
		this.cProperty.set(c);
		this.dProperty.set(d);
		clearBufferedValues();
	}
	

	
	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Vector3d getNormal() {
		return new Vector3d(this.aProperty,this.bProperty,this.cProperty);
	}
	
	/** {@inheritDoc}
	 */
	@Pure
	public Vector3f getNormalWithoutProperties() {
		return new Vector3f(this.getEquationComponentA(),this.getEquationComponentB(),this.getEquationComponentC());
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double getEquationComponentA() {
		return this.aProperty.doubleValue();
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double getEquationComponentB() {
		return this.bProperty.doubleValue();
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double getEquationComponentC() {
		return this.cProperty.doubleValue();
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double getEquationComponentD() {
		return this.dProperty.doubleValue();
	}
	
	@Override
	public void setPivot(double x, double y, double z) {
		clearBufferedValues();
		// a.x + b.y + c.z + d = 0
		// where (x,y,z) is the translation point
		this.unBindEquationComponentD();
		this.dProperty.set(- (this.getEquationComponentA()*x + this.getEquationComponentB()*y + this.getEquationComponentC()*z));
		this.cachedPivot = new WeakReference<>(new Point3d(x, y, z));
	}
	
	public void setPivotProperties(Point3d pivot) {
		clearBufferedValues();
		// a.x + b.y + c.z + d = 0
		// where (x,y,z) is the translation point
		this.unBindEquationComponentD();
		
		this.dProperty.bind(Bindings.createDoubleBinding(new Callable<Double>() {
			@Override
			public Double call() throws Exception {
				return new Double(- (Plane4d.this.aProperty.doubleValue()*pivot.xProperty.doubleValue()+ Plane4d.this.bProperty.doubleValue()*pivot.yProperty.doubleValue() + Plane4d.this.cProperty.doubleValue()*pivot.zProperty.doubleValue()));
			}
		}, this.aProperty, this.bProperty, this.cProperty,pivot.xProperty,pivot.yProperty,pivot.zProperty));
		
		this.cachedPivot = new WeakReference<>(pivot);
	}

	/** Replies the pivot point around which the rotation must be done.
	 * 
	 * @return a reference on the buffered pivot point.
	 */
	@Override
	public Point3d getPivot() {
		Point3d pivot = this.cachedPivot == null ? null : this.cachedPivot.get();
		if (pivot==null) {
			pivot = getProjection(0., 0., 0.);
			this.cachedPivot = new WeakReference<>(pivot);
		}
		return pivot;
	}
	
	/** Unbind the d equation component from the previous point
	 */
	protected void unBindEquationComponentD() {
		this.dProperty.unbind();
	}
	
	/** Clear buffered values.
	 */
	@Override
	protected void clearBufferedValues() {
		this.cachedPivot = null;
	}

	@Pure
	@Override
	public Point3d getProjection(double x, double y, double z) {
		return computePointProjection(
				getEquationComponentA(),
				getEquationComponentB(),
				getEquationComponentC(),
				getEquationComponentD(),
				x, y, z);
	}

	@Override
	protected void setEquationComponentC(double z) {
		this.cProperty.set(z);
	}

	@Override
	protected void setEquationComponentB(double y) {
		this.bProperty.set(y);
	}

	@Override
	protected void setEquationComponentA(double x) {
		this.aProperty.set(x);
	}

	@Override
	protected void setEquationComponentD(double w) {
		this.dProperty.set(w);
	}
	
	protected void setEquationComponentCProperty(DoubleProperty c) {
		this.cProperty = c;
	}

	protected void setEquationComponentBProperty(DoubleProperty b) {
		this.bProperty = b;
	}

	protected void setEquationComponentAProperty(DoubleProperty a) {
		this.aProperty = a;
	}

	protected void setEquationComponentDProperty(DoubleProperty d) {
		this.dProperty = d;
	}
	

}
