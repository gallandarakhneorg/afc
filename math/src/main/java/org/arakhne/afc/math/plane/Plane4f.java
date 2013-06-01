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
package org.arakhne.afc.math.plane;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Quaternion;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.continous.object4d.AxisAngle4f;
import org.arakhne.afc.math.matrix.Matrix4f;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.afc.math.transform.Transform3D;

/** This class represents a 3D plane.
 * <p>
 * <math xmlns="http://www.w3.org/1998/Math/MathML"><mi>P</mi></math>
 * be the point we wish to lie in the plane, and let
 * <math><mover><mi>n</mi><mo>&#x20D7;</mo></mover></math>
 * be a nonzero normal vector to the plane.
 * The desired plane is the set of all points
 * <math><mi mathvariant="bold">r</mi></math> such that
 * <math>
 *   <mover>
 *     <mi>n</mi>
 *     <mo>&#x20D7;</mo>
 *   </mover>
 *   <mo>&#x22C5;</mo>
 *   <mfenced separators="">
 *     <mi>r</mi>
 *     <mo>-</mo>
 *     <mi>p</mi>
 *   </mfenced>
 *   <mo>=</mo>
 *   <mn>0</mn>
 * </math>.
 * <p>
 * If we write
 * <math>
 *   <mrow>
 *     <mover>
 *       <mi>n</mi><mo>&#x20D7;</mo>
 *     </mover>
 *     <mo>=</mo>
 *     <mfenced close="]" open="[">
 *       <mtable>
 *         <mtr><mtd><mi>a</mi></mtd></mtr>
 *         <mtr><mtd><mi>b</mi></mtd></mtr>
 *         <mtr><mtd><mi>c</mi></mtd></mtr>
 *       </mtable>
 *     </mfenced>
 *   </mrow>
 * </math>,<br>
 * <math>
 *   <mrow>
 *     <mi mathvariant="bold">r</mi>
 *     <mo>=</mo>
 *     <mfenced>
 *       <mi>x</mi><mi>y</mi><mi>z</mi>
 *     </mfenced>
 *   </mrow>
 * </math>, and
 * <math><mi>d</mi></math> as the dot product
 * <math>
 *   <mrow>
 *     <mover>
 *       <mi>n</mi><mo>&#x20D7;</mo>
 *     </mover>
 *     <mo>&#x22C5;</mo>
 *     <mi mathvariant="bold">p</mi>
 *     <mo>=</mo><mo>-</mo><mi>d</mi>
 *   </mrow>
 * </math>,<br>
 * then the plane <math><mi>&#x03A0;</mi></math> is determined by the condition:<br>
 * <math>
 *   <mrow>
 *     <mi>&#x03A0;</mi><mo>:</mo><mi>a</mi><mo>.</mo><mi>x</mi>
 *     <mo>+</mo>
 *     <mi>b</mi><mo>.</mo><mi>y</mi>
 *     <mo>+</mo>
 *     <mi>c</mi><mo>.</mo><mi>z</mi>
 *     <mo>+</mo>
 *     <mi>d</mi><mo>=</mo><mn>0</mn>
 *   </mrow>
 * </math>
 * <p>
 * The normal to the plane is the vector
 * <math><mfenced><mi>a</mi><mi>b</mi><mi>c</mi></mfenced></math>.
 * 
 * Given three points in space
 * <math><mfenced><msub><mi>x</mi><mn>1</mn></msub></mi><msub><mi>y</mi><mn>1</mn></msub><msub><mi>z</mi><mn>1</mn></msub></mfenced></math>,
 * <math><mfenced><msub><mi>x</mi><mn>2</mn></msub></mi><msub><mi>y</mi><mn>2</mn></msub><msub><mi>z</mi><mn>2</mn></msub></mfenced></math> and 
 * <math><mfenced><msub><mi>x</mi><mn>3</mn></msub></mi><msub><mi>y</mi><mn>3</mn></msub><msub><mi>z</mi><mn>3</mn></msub></mfenced></math>,
 * the equation of the plane through these points is
 * given by the following determinants.
 * <p>
 * <math>
 *   <mrow>
 *     <mi>a</mi><mo>=</mo>
 *     <mfenced close="&#x2223; " open="&#x2223; ">
 *       <mtable>
 *         <mtr>
 *           <mtd><mn>1</mn></mtd>
 *           <mtd><msub><mi>y</mi><mn>1</mn></msub></mtd>
 *           <mtd><msub><mi>z</mi><mn>1</mn></msub></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><mn>1</mn></mtd>
 *           <mtd><msub><mi>y</mi><mn>2</mn></msub></mtd>
 *           <mtd><msub><mi>z</mi><mn>2</mn></msub></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><mn>1</mn></mtd>
 *           <mtd><msub><mi>y</mi><mn>3</mn></msub></mtd>
 *           <mtd><msub><mi>z</mi><mn>3</mn></msub></mtd>
 *         </mtr>
 *       </mtable>
 *     </mfenced>
 *   </mrow>
 * </math>
 * <math>
 *   <mrow>
 *     <mi>b</mi><mo>=</mo>
 *     <mfenced close="&#x2223; " open="&#x2223; ">
 *       <mtable>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>1</mn></msub></mtd>
 *           <mtd><mn>1</mn></mtd>
 *           <mtd><msub><mi>z</mi><mn>1</mn></msub></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>2</mn></msub></mtd>
 *           <mtd><mn>1</mn></mtd>
 *           <mtd><msub><mi>z</mi><mn>2</mn></msub></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>3</mn></msub></mtd>
 *           <mtd><mn>1</mn></mtd>
 *           <mtd><msub><mi>z</mi><mn>3</mn></msub></mtd>
 *         </mtr>
 *       </mtable>
 *     </mfenced>
 *   </mrow>
 * </math>
 * <math>
 *   <mrow>
 *     <mi>c</mi><mo>=</mo>
 *     <mfenced close="&#x2223; " open="&#x2223; ">
 *       <mtable>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>1</mn></msub></mtd>
 *           <mtd><msub><mi>y</mi><mn>1</mn></msub></mtd>
 *           <mtd><mn>1</mn></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>2</mn></msub></mtd>
 *           <mtd><msub><mi>y</mi><mn>2</mn></msub></mtd>
 *           <mtd><mn>1</mn></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>3</mn></msub></mtd>
 *           <mtd><msub><mi>y</mi><mn>3</mn></msub></mtd>
 *           <mtd><mn>1</mn></mtd>
 *         </mtr>
 *       </mtable>
 *     </mfenced>
 *   </mrow>
 * </math>
 * <math>
 *   <mrow>
 *     <mi>d</mi><mo>=</mo><mo>-</mo>
 *     <mfenced close="&#x2223; " open="&#x2223; ">
 *       <mtable>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>1</mn></msub></mtd>
 *           <mtd><msub><mi>y</mi><mn>1</mn></msub></mtd>
 *           <mtd><msub><mi>z</mi><mn>1</mn></msub></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>2</mn></msub></mtd>
 *           <mtd><msub><mi>y</mi><mn>2</mn></msub></mtd>
 *           <mtd><msub><mi>z</mi><mn>2</mn></msub></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>3</mn></msub></mtd>
 *           <mtd><msub><mi>y</mi><mn>3</mn></msub></mtd>
 *           <mtd><msub><mi>z</mi><mn>3</mn></msub></mtd>
 *         </mtr>
 *       </mtable>
 *     </mfenced>
 *   </mrow> 
 * </math>
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class Plane4f extends AbstractPlane {

	private static final long serialVersionUID = 2621838558308018582L;

	/** equation coefficient.
	 */
	private float a;
    
	/** equation coefficient.
	 */
    private float b;
    
    /** equation coefficient.
	 */
    private float c;
    
    /** equation coefficient.
	 */
    private float d;
    
    /** Cached pivot point.
     */
    private transient Point3f cachedPivot = null;

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
	public Plane4f(float a, float b, float c, float d) {
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
	public Plane4f(Vector3f normal, Point3f p) {
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
	public Plane4f(float a, float b, float c, float px, float py, float pz) {
		this.a = a;
		this.b = b;
		this.c = c;
		normalize();
		setPivot(px,py,pz);
	}

	/**
	 * @param plane is the plane to copy
	 */
	public Plane4f(Plane plane) {
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
	public Plane4f(Tuple3f<?> p1, Tuple3f<?> p2, Tuple3f<?> p3) {
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
	public Plane4f(float p1x, float p1y, float p1z, float p2x, float p2y, float p2z, float p3x, float p3y, float p3z) {
		set(p1x, p1y, p1z, p2x, p2y, p2z, p3x, p3y, p3z);
	}
	
	/** Replies a clone of this plane.
	 * 
	 * @return a clone.
	 */
	@Override
	public Plane4f clone() {
		return (Plane4f)super.clone();
	}

		/** Clear buffered values.
	 */
	protected void clearBufferedValues() {
		this.cachedPivot = null;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void negate() {
		this.a = -this.a;
		this.b = -this.b;
		this.c = -this.c;
		this.d = -this.d;
		clearBufferedValues();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void absolute() {
		if (this.a<0) this.a = -this.a;
		if (this.b<0) this.b = -this.b;
		if (this.c<0) this.c = -this.c;
		clearBufferedValues();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Plane normalize() {
		float t = (float) Math.sqrt(this.a*this.a+this.b*this.b+this.c*this.c);
		this.a /= t;
		this.b /= t;
		this.c /= t;
		this.d /= t;
		clearBufferedValues();
		return this;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane plane) {
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
	public void set(float p1x, float p1y, float p1z, float p2x, float p2y, float p2z, float p3x, float p3y, float p3z) {
		Vector3f v = new Vector3f();
		if (CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded()) {
			MathUtil.crossProductLeftHand(
					p2x-p1x, p2y-p1y, p2z-p1z,
					p3x-p1x, p3y-p1y, p3z-p1z,
					v);
		}
		else {
			MathUtil.crossProductRightHand(
					p2x-p1x, p2y-p1y, p2z-p1z,
					p3x-p1x, p3y-p1y, p3z-p1z,
					v);
		}
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
	public final void set(Point3f p1, Point3f p2, Point3f p3) {
		set(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ());
	}

	/** Set this pane with the given factors.
	 *  
	 * @param a is the first factor of the plane equation.
	 * @param b is the first factor of the plane equation.
	 * @param c is the first factor of the plane equation.
	 * @param d is the first factor of the plane equation.
	 */
	public void set(float a, float b, float c, float d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		clearBufferedValues();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector3f getNormal() {
		return new Vector3f(this.a,this.b,this.c);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getEquationComponentA() {
		return this.a;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getEquationComponentB() {
		return this.b;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getEquationComponentC() {
		return this.c;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getEquationComponentD() {
		return this.d;
	}

	/** {@inheritDoc}
	 */
	@Override
    public float distanceTo(float x, float y, float z) {
    	return this.a * x + this.b * y + this.c * z + this.d;
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setIdentityTransform() {
    	this.a = 0;
    	this.b = 0;
    	this.c = 1;
    	this.d = 0;
		clearBufferedValues();
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setTransform(Transform3D trans) {
    	// Update the plane equation according
    	// to the desired normal (computed from
    	// the identity vector and the rotations).
    	Quaternion quat = new Quaternion();
    	trans.get(quat);
    	Matrix4f m = new Matrix4f();
    	m.set(quat);
    	Vector3f v = new Vector3f(0,0,1); // identity vector
    	m.transform(v);
    	this.a = v.getX();
    	this.b = v.getY();
    	this.c = v.z();

    	// a.x + b.y + c.z + d = 0
    	// where (x,y,z) is the translation point
    	this.d = - (this.a*trans.m03 + this.b*trans.m13 + this.c*trans.m23);
    	
		clearBufferedValues();
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public final void transform(Transform3D trans) {
    	transform(trans,getPivotReference());
    }
    
    /**
     * Transforms the plane object by the given transform.
     * 
     * @param trans
     * @param refPoint is the point to project on the point to obtain the pivot.
     */
    public void transform(Transform3D trans, Point3f refPoint) {
    	Vector3f v = new Vector3f(this.a,this.b,this.c);
    	trans.transform(v);

    	// Update the plane equation according
    	// to the desired normal (computed from
    	// the identity vector and the rotations).
    	this.a = v.getX();
    	this.b = v.getY();
    	this.c = v.getZ();
    	
    	// a.x + b.y + c.z + d = 0
    	// where (x,y,z) is the translation point
    	this.d = - (this.a*(refPoint.getX()+trans.m03) +
    				this.b*(refPoint.getY()+trans.m13) +
    				this.c*(refPoint.getZ()+trans.m23));
    	
		clearBufferedValues();
    }

	/** {@inheritDoc}
	 */
	@Override
    public Transform3D getTransformMatrix() {
    	Point3f pivot = getPivotReference();
    	AxisAngle4f n = new AxisAngle4f(this.a,this.b,this.c, 0.f);
    	
    	Transform3D m = new Transform3D();
    	m.setRotation(n);
    	m.setTranslation(new Vector3f(pivot));
    	
    	return m;
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setTranslation(float x, float y, float z) {
    	setPivot(x,y,z);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void translate(float dx, float dy, float dz) {
    	// Compute the reference point for the plane
    	// (usefull for translation)
    	Point3f refPoint = getPivotReference();
    	
    	// a.x + b.y + c.z + d = 0
    	// where (x,y,z) is the translation point
    	setPivot(refPoint.getX()+dx,refPoint.getY()+dy,refPoint.getZ()+dz);
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public final void setTranslation(Point3f position) {
    	setTranslation(position.getX(),position.getY(),position.getZ());
    }

	/** {@inheritDoc}
	 */
	@Override
    public final Point3f getTranslation() {
    	return getPivot();
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void translate(Vector3f v) {
    	translate(v.getX(),v.getY(),v.getZ());
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setScale(float sx, float sy, float sz) {
    	//
    }

	/** {@inheritDoc}
	 */
	@Override
    public void scale(float sx, float sy, float sz) {
    	//
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public Tuple3f<?> getScale() {
    	return new Vector3f(1,1,1);
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setRotation(AxisAngle4f quaternion) {
    	setRotation(quaternion,getPivotReference());
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setRotation(AxisAngle4f quaternion, Point3f pivot) {
    	setRotation(quaternion, pivot, 0f, 0f, 1f);
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setRotation(Quaternion quaternion) {
    	setRotation(quaternion,getPivotReference());
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setRotation(Quaternion quaternion, Point3f pivot) {
    	setRotation(quaternion, pivot, 0f, 0f, 1f);
    }

    /**
     * Set the rotation for the object.
     * The normal used as reference is given as parameter. This
     * normal is inside the plane equation when the plane
     * was not rotated. 
     * 
     * @param quaternion is the rotation
     * @param pivot is the pivot point
     * @param vx is the default plane normal when the plane is not rotated.
     * @param vy is the default plane normal when the plane is not rotated.
     * @param vz is the default plane normal when the plane is not rotated.
     */
    public void setRotation(Quaternion quaternion, Point3f pivot, float vx, float vy, float vz) {
    	// Update the plane equation according
    	// to the desired normal (computed from
    	// the identity vector and the rotations).
    	Matrix4f m = new Matrix4f();
    	m.set(quaternion);
    	Vector3f v = new Vector3f(vx,vy,vz);
    	m.transform(v);
    	this.a = v.getX();
    	this.b = v.getY();
    	this.c = v.getZ();

    	// a.x + b.y + c.z + d = 0
    	// where (x,y,z) is the translation point
    	this.d = - (this.a*pivot.getX() +
    				this.b*pivot.getY() +
    				this.c*pivot.getZ());
    	
		clearBufferedValues();
    }

    /**
     * Set the rotation for the object.
     * The normal used as reference is given as parameter. This
     * normal is inside the plane equation when the plane
     * was not rotated. 
     * 
     * @param quaternion is the rotation
     * @param pivot is the pivot point
     * @param vx is the default plane normal when the plane is not rotated.
     * @param vy is the default plane normal when the plane is not rotated.
     * @param vz is the default plane normal when the plane is not rotated.
     */
    public void setRotation(AxisAngle4f quaternion, Point3f pivot, float vx, float vy, float vz) {
    	// Update the plane equation according
    	// to the desired normal (computed from
    	// the identity vector and the rotations).
    	Matrix4f m = new Matrix4f();
    	m.set(quaternion);
    	Vector3f v = new Vector3f(vx,vy,vz);
    	m.transform(v);
    	this.a = v.getX();
    	this.b = v.getY();
    	this.c = v.getZ();

    	// a.x + b.y + c.z + d = 0
    	// where (x,y,z) is the translation point
    	this.d = - (this.a*pivot.getX() +
    				this.b*pivot.getY() +
    				this.c*pivot.getZ());
    	
		clearBufferedValues();
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void rotate(AxisAngle4f quaternion) {
    	rotate(quaternion,null);
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public void rotate(AxisAngle4f quaternion, Point3f pivot) {
    	Point3f currentPivot = getPivotReference();
    	
    	// Update the plane equation according
    	// to the desired normal (computed from
    	// the identity vector and the rotations).
    	Matrix4f m = new Matrix4f();
    	m.set(quaternion);
    	Vector3f v = new Vector3f(this.a,this.b,this.c);
    	m.transform(v);
    	this.a = v.getX();
    	this.b = v.getY();
    	this.c = v.getZ();
    	
    	if ((currentPivot==pivot)||(pivot==null)) {
	    	// a.x + b.y + c.z + d = 0
	    	// where (x,y,z) is the translation point
	    	this.d = - (this.a*currentPivot.getX() +
	    				this.b*currentPivot.getY() +
	    				this.c*currentPivot.getZ());
    	}
    	else {
    		// Compute the new point
    		Point3f nP = new Point3f(
    				currentPivot.getX() - pivot.getX(),
    				currentPivot.getY() - pivot.getY(),
    				currentPivot.getZ() - pivot.getZ());
    		m.transform(nP);
    		nP.setX(nP.getX() + pivot.getX());
    		nP.setY(nP.getY() + pivot.getY());
    		nP.setZ(nP.getZ() + pivot.getZ());
    		
	    	// a.x + b.y + c.z + d = 0
	    	// where (x,y,z) is the translation point
	    	this.d = - (this.a*nP.getX() +
	    				this.b*nP.getY() +
	    				this.c*nP.getZ());
    	}

		clearBufferedValues();
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void rotate(Quaternion quaternion) {
    	rotate(quaternion,null);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void rotate(Quaternion quaternion, Point3f pivot) {
    	Point3f currentPivot = getPivotReference();
    	
    	// Update the plane equation according
    	// to the desired normal (computed from
    	// the identity vector and the rotations).
    	Matrix4f m = new Matrix4f();
    	m.set(quaternion);
    	Vector3f v = new Vector3f(this.a,this.b,this.c);
    	m.transform(v);
    	this.a = v.getX();
    	this.b = v.getY();
    	this.c = v.getZ();
    	
    	if ((currentPivot==pivot)||(pivot==null)) {
	    	// a.x + b.y + c.z + d = 0
	    	// where (x,y,z) is the translation point
	    	this.d = - (this.a*currentPivot.getX() +
	    				this.b*currentPivot.getY() +
	    				this.c*currentPivot.getZ());
    	}
    	else {
    		// Compute the new point
    		Point3f nP = new Point3f(
    				currentPivot.getX() - pivot.getX(),
    				currentPivot.getY() - pivot.getY(),
    				currentPivot.getZ() - pivot.getZ());
    		m.transform(nP);
    		nP.setX(nP.getX() + pivot.getX());
    		nP.setY(nP.getY() + pivot.getY());
    		nP.setZ(nP.getZ() + pivot.getZ());
    		
	    	// a.x + b.y + c.z + d = 0
	    	// where (x,y,z) is the translation point
	    	this.d = - (this.a*nP.getX() +
	    				this.b*nP.getY() +
	    				this.c*nP.getZ());
    	}
    	
		clearBufferedValues();
    }

	/** {@inheritDoc}
	 */
	@Override
    public final AxisAngle4f getAxisAngle() {
    	return new AxisAngle4f(this.a,this.b,this.c, 0.f);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setPivot(float x, float y, float z) {
    	// a.x + b.y + c.z + d = 0
    	// where (x,y,z) is the translation point
    	this.d = - (this.a*x + this.b*y + this.c*z);

		clearBufferedValues();
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setPivot(Point3f point) {
    	setPivot(point.getX(),point.getY(),point.getZ());
    }

	/** {@inheritDoc}
	 */
	@Override
    public final Point3f getPivot() {
    	return new Point3f(getPivotReference());
    }

    /** Replies the pivot point around which the rotation must be done.
     * 
     * @return a reference on the buffered pivot point.
     */
    protected Point3f getPivotReference() {
    	if (this.cachedPivot==null) {
    		this.cachedPivot = MathUtil.projectsPointOnPlaneIn3d(new Point3f(), this);
    	}
    	return this.cachedPivot;
    }

    /** Replies if this plane has valid normal.
     * 
     * @return <code>true</code> if the plane equation is valid, otherwise <code>false</code>
     */
    public boolean isValid() {
    	return ((!Double.isNaN(this.a))&&
    			(!Double.isNaN(this.b))&&
    			(!Double.isNaN(this.c))&&
    			((this.a!=0)||(this.b!=0)||(this.c!=0)));
    }
 
    /**
     * Exception thowned when the equation of the plane
     * is invalid to realize a computation.
     *
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    public final class InvalidPlaneEquationException extends RuntimeException {

		private static final long serialVersionUID = 4358255085523562003L;

		/**
		 *
		 */
		public InvalidPlaneEquationException() {
    		//
    	}
    	
    }
       
}