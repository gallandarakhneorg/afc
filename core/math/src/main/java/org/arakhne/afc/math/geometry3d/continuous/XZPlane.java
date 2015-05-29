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

import org.arakhne.afc.math.transform.Transform3D_OLD;

/** This class represents a 3D plane which is colinear to the X and Z axis.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class XZPlane extends AbstractOrthoPlane {
	
	private static final long serialVersionUID = -2378546863694735717L;
	
	/** Is the coordinate of the plane.
	 */
	public float y;

	/**
	 * @param y is the coordinate of the plane
	 */
	public XZPlane(float y) {
		this.y = y;
	}
	
	/**
	 * @param p is a point on the plane.
	 */
	public XZPlane(Tuple3f p) {
		this.y = p.getY();
	}

	/** Replies a clone of this plane.
	 * 
	 * @return a clone.
	 */
	@Override
	public XZPlane clone() {
		return (XZPlane)super.clone();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane plane) {
		this.y = plane.getEquationComponentB();
		normalize();
	}

	/** {@inheritDoc}
	 */
	@Override
    public void setIdentityTransform() {
    	this.y = 0;
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setTransform(Transform3D_OLD trans) {
    	this.y = trans.m13;
    }

	/** {@inheritDoc}
	 */
	@Override
    public void transform(Transform3D_OLD trans) {
    	this.y += trans.m13;
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public Transform3D_OLD getTransformMatrix() {
    	Transform3D_OLD m = new Transform3D_OLD();
    	m.m13 = this.y;
    	return m;
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setTranslation(float x, float y, float z) {
    	this.y = y;
    }

	/** {@inheritDoc}
	 */
	@Override
    public void translate(float dx, float dy, float dz) {
    	this.y += dy;
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public void setTranslation(Point3f position) {
    	this.y = position.getY();
    }

	/** {@inheritDoc}
	 */
	@Override
    public Point3f getTranslation() {
    	return new Point3f(0,this.y, 0);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void translate(Vector3f v) {
    	this.y += v.getY();
    }

	/** {@inheritDoc}
	 */
	@Override
    public Point3f getPivot() {
    	return new Point3f(0.,this.y, 0.);
    }

	/** {@inheritDoc}
	 */
	@Override
	public Vector3f getNormal() {
		return new Vector3f(0,this.isPositive ? 1 : -1, 0);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getEquationComponentA() {
		return 0;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getEquationComponentB() {
		return this.isPositive ? 1 : -1;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getEquationComponentC() {
		return 0;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float getEquationComponentD() {
		return this.isPositive ? -this.y : this.y;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceTo(float px, float py, float pz) {
    	float d = py - this.y;
    	return this.isPositive ? d : -d;
    }

	/**
	 * Classifies a box with respect to the plane.
	 * <p>
	 * Classifying a point with respect to a plane is done by passing
	 * the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the
	 * distance from the plane to the point along the plane's normal vector.
	 * It will be positive if the point is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 */
    @Override
	public final PlanarClassificationType classifies(float lx, float ly, float lz, float ux, float uy, float uz) {
    	float cuy = uy;
    	float cly = ly;
    	if (uy<ly) {
    		float t = cuy;
    		cuy = cly;
    		cly = t;
    	}
    	PlanarClassificationType t;    	
    	if (cuy<this.y) t = PlanarClassificationType.BEHIND;
    	else if (cly>this.y) t = PlanarClassificationType.IN_FRONT_OF;
    	else t = PlanarClassificationType.COINCIDENT;    	
    	return this.isPositive ? t : PlanarClassificationType.invert(t);
    }

}