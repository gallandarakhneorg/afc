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
package org.arakhne.afc.math.object;

import java.io.Serializable;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Quaternion;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.continous.object4d.AxisAngle4f;
import org.arakhne.afc.math.matrix.Matrix4f;
import org.arakhne.afc.math.transform.Transform3D;
import org.arakhne.afc.math.transform.Transformable3D;

/** This class represents a 3D line.
 * <p>
 * The equation of the line is:
 * <math xmlns="http://www.w3.org/1998/Math/MathML">
 *   <mrow>
 *     <mi>L</mi><mo>&#x2061;</mo><mfenced><mi>t</mi></mfenced>
 *     <mo>=</mo>
 *     <mi>P</mi><mo>+</mo>
 *     <mi>t</mi><mo>.</mo>
 *     <mover>
 *       <mi>D</mi>
 *       <mo>&#x20D7;</mo>
 *     </mover>
 *   </mrow>
 * </math>
 * for any real-valued <math><mi>t</mi></math>.
 * <math><mover><mi>D</mi><mo>&#x20D7;</mo></mover></math> is not
 * necessarily unit length. 
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Line3D implements Serializable, Transformable3D {
	
	private static final long serialVersionUID = 7914697528608357347L;

	/** First point on the line.
	 */
	public final Point3f pivot = new Point3f();
	
	/** Direction vector.
	 */
    public final Vector3f d = new Vector3f();

    /**
     */
    public Line3D() {
        super();
    }

    /**
     * @param p1 is first point on the line
     * @param p2 is second point on the line
     */
    public Line3D(Point3f p1, Point3f p2) {
        this.pivot.set(p1);
        this.d.set(p2);
        this.d.sub(p1);
        this.d.normalize();
    }

    /**
     * @param pivot is a point on the line
     * @param direction is the direction of the line
     */
    public Line3D(Point3f pivot, Vector3f direction) {
        this.pivot.set(pivot);
        this.d.set(direction);
        this.d.normalize();
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setIdentityTransform() {
    	this.pivot.set(0,0,0);
    	this.d.set(1,0,0);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setTransform(Transform3D trans) {
    	setTranslation(trans.m03,trans.m13,trans.m23);

    	Quaternion quat = new Quaternion();
    	trans.get(quat);
    	setRotation(quat);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void transform(Transform3D trans) {
    	translate(trans.m03, trans.m13, trans.m23);
    	
    	Quaternion quat = new Quaternion();
    	trans.get(quat);
    	rotate(quat);
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public Transform3D getTransformMatrix() {
    	Transform3D m = new Transform3D();
    	m.setTranslation(new Vector3f(this.pivot.getX(),this.pivot.getY(),this.pivot.getZ()));
    	m.setRotation(new AxisAngle4f(this.d,0.f));
    	return m;
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setTranslation(float x, float y, float z) {
    	this.pivot.set(x,y,z);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void translate(float dx, float dy, float dz) {
    	this.pivot.setX(this.pivot.getX() + dx);
    	this.pivot.setY(this.pivot.getY() + dy);
    	this.pivot.setZ(this.pivot.getZ() + dz);
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public void setTranslation(Point3f position) {
    	this.pivot.set(position);
    }

	/** {@inheritDoc}
	 */
	@Override
    public Point3f getTranslation() {
    	return new Point3f(this.pivot);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void translate(Vector3f v) {
    	this.pivot.setX(this.pivot.getX() + v.getX());
    	this.pivot.setY(this.pivot.getY() + v.getY());
    	this.pivot.setZ(this.pivot.getZ() + v.getZ());
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
    public Tuple3f getScale() {
    	return new Vector3f(1,1,1);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setRotation(AxisAngle4f quaternion) {
    	this.d.set(1,0,0);
    	Matrix4f m = new Matrix4f();
    	m.set(quaternion);
    	m.transform(this.d);
    	this.d.normalize();
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setRotation(Quaternion quaternion) {
    	this.d.set(1,0,0);
    	Matrix4f m = new Matrix4f();
    	m.set(quaternion);
    	m.transform(this.d);
    	this.d.normalize();
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setRotation(AxisAngle4f quaternion, Point3f pivot) {
    	this.pivot.set(pivot);
    	this.d.set(1,0,0);
    	Matrix4f m = new Matrix4f();
    	m.set(quaternion);
    	m.transform(this.d);
    	this.d.normalize();    	
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setRotation(Quaternion quaternion, Point3f pivot) {
    	this.pivot.set(pivot);
    	this.d.set(1,0,0);
    	Matrix4f m = new Matrix4f();
    	m.set(quaternion);
    	m.transform(this.d);
    	this.d.normalize();    	
    }

	/** {@inheritDoc}
	 */
	@Override
    public void rotate(AxisAngle4f quaternion) {
    	Matrix4f m = new Matrix4f();
    	m.set(quaternion);
    	m.transform(this.d);
    	this.d.normalize();
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public void rotate(Quaternion quaternion, Point3f pivotPoint) {
    	if (pivotPoint==this.pivot) rotate(quaternion);
    	
    	Matrix4f m = new Matrix4f();
    	m.set(quaternion);

    	// Rotate the pivot
    	this.pivot.setX(this.pivot.getX() - pivotPoint.getX());
    	this.pivot.setY(this.pivot.getY() - pivotPoint.getY());
    	this.pivot.setZ(this.pivot.getZ() - pivotPoint.getZ());
    	
    	m.transform(this.pivot);
    	
    	this.pivot.setX(this.pivot.getX() + pivotPoint.getX());
    	this.pivot.setY(this.pivot.getY() + pivotPoint.getY());
    	this.pivot.setZ(this.pivot.getZ() + pivotPoint.getZ());

    	// Rotate the direction
    	m.transform(this.d);
    	this.d.normalize();
    }

	/** {@inheritDoc}
	 */
	@Override
    public void rotate(AxisAngle4f quaternion, Point3f pivotPoint) {
    	if (pivotPoint==this.pivot) rotate(quaternion);
    	
    	Matrix4f m = new Matrix4f();
    	m.set(quaternion);

    	// Rotate the pivot
    	this.pivot.setX(this.pivot.getX() - pivotPoint.getX());
    	this.pivot.setY(this.pivot.getY() - pivotPoint.getY());
    	this.pivot.setZ(this.pivot.getZ() - pivotPoint.getZ());
    	
    	m.transform(this.pivot);
    	
    	this.pivot.setX(this.pivot.getX() + pivotPoint.getX());
    	this.pivot.setY(this.pivot.getY() + pivotPoint.getY());
    	this.pivot.setZ(this.pivot.getZ() + pivotPoint.getZ());

    	// Rotate the direction
    	m.transform(this.d);
    	this.d.normalize();
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public void rotate(Quaternion quaternion) {
    	Matrix4f m = new Matrix4f();
    	m.set(quaternion);
    	m.transform(this.d);
    	this.d.normalize();
    }

	/** {@inheritDoc}
	 */
	@Override
    public AxisAngle4f getAxisAngle() {
    	return new AxisAngle4f(this.d,0);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setPivot(float x, float y, float z) {
    	this.pivot.set(x,y,z);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setPivot(Point3f point) {
    	this.pivot.set(point);
    }

	/** {@inheritDoc}
	 */
	@Override
    public Point3f getPivot() {
    	return new Point3f(this.pivot);
    }

    /**
     * Replies the direction for the line.
     * 
     * @return line direction vector
     */
    public Vector3f getDirection() {
    	return new Vector3f(this.d);
    }

    /**
     * Replies the first point.
     * 
     * @return the point on the line.
     */
    public Point3f getP0() {
    	return new Point3f(this.pivot);
    }

    /**
     * Replies the second point.
     * 
     * @return the point on the line.
     */
    public Point3f getP1() {
    	Point3f p = new Point3f(this.pivot);
    	p.add(this.d);
    	return p;
    }
    
    /** Replies the distance between this line and the given point.
     *
     * @param x
     * @param y
     * @param z
     * @return the distance.
     */
    public float distance(float x, float y, float z) {
    	return MathUtil.distancePointLine(x, y, z, 
    			this.pivot.getX(), this.pivot.getY(), this.pivot.getZ(),
    			this.pivot.getX()+this.d.getX(),
    			this.pivot.getY()+this.d.getY(),
    			this.pivot.getZ()+this.d.getZ());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
    	StringBuilder b = new StringBuilder();
    	b.append("]p:("); //$NON-NLS-1$
    	b.append(this.pivot.getX());
    	b.append(";"); //$NON-NLS-1$
    	b.append(this.pivot.getY());
    	b.append(";"); //$NON-NLS-1$
    	b.append(this.pivot.getZ());
    	b.append("),v:("); //$NON-NLS-1$
    	b.append(this.d.getX());
    	b.append(";"); //$NON-NLS-1$
    	b.append(this.d.getY());
    	b.append(";"); //$NON-NLS-1$
    	b.append(this.d.getZ());
    	b.append(")["); //$NON-NLS-1$
    	return b.toString();
    }

}
