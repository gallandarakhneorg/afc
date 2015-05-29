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

/** This class represents a 3D plane which is colinear to the X and Y axis.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class XYPlane extends AbstractOrthoPlane {
	
	private static final long serialVersionUID = 6857600518749058720L;

	/**
	 * Coordinate of the plane.
	 */
	public float z;
	
	/**
	 * @param z is the coordinate of the plane.
	 */
	public XYPlane(float z) {
		this.z = z;
	}
	
	/**
	 * @param p is a point on the plane.
	 */
	public XYPlane(Tuple3f p) {
		this.z = p.getZ();
	}

	/** Replies a clone of this plane.
	 * 
	 * @return a clone.
	 */
	@Override
	public XYPlane clone() {
		return (XYPlane)super.clone();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane plane) {
		this.z = plane.getEquationComponentC();
		normalize();
	}

	/** {@inheritDoc}
	 */
	@Override
    public void setIdentityTransform() {
    	this.z = 0;
    }
        

	/** {@inheritDoc}
	 */
	@Override
    public void setTransform(Transform3D_OLD trans) {
    	this.z = trans.m23;
    }

	/** {@inheritDoc}
	 */
	@Override
    public void transform(Transform3D_OLD trans) {
    	this.z += trans.m23;
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public Transform3D_OLD getTransformMatrix() {
    	Transform3D_OLD m = new Transform3D_OLD();
    	m.m23 = this.z;
    	return m;
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setTranslation(float x, float y, float z) {
    	this.z = z;
    }

	/** {@inheritDoc}
	 */
	@Override
    public void translate(float dx, float dy, float dz) {
    	this.z += dz;
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public void setTranslation(Point3f position) {
    	this.z = position.getZ();
    }

	/** {@inheritDoc}
	 */
	@Override
    public Point3f getTranslation() {
    	return new Point3f(0.,0.,this.z);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void translate(Vector3f v) {
    	this.z += v.getZ();
    }

	/** {@inheritDoc}
	 */
	@Override
    public Point3f getPivot() {
    	return new Point3f(0.,0.,this.z);
    }

	/** {@inheritDoc}
	 */
	@Override
	public Vector3f getNormal() {
		return new Vector3f(0,0,this.isPositive ? 1 : -1);
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
		return 0;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getEquationComponentC() {
		return this.isPositive ? 1 : -1;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float getEquationComponentD() {
		return this.isPositive ? -this.z : this.z;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceTo(float px, float py, float pz) {
    	float d = pz - this.z;
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
	 * 
     * @param lx {@inheritDoc}
     * @param ly {@inheritDoc}
     * @param lz {@inheritDoc}
     * @param ux {@inheritDoc}
     * @param uy {@inheritDoc}
     * @param uz {@inheritDoc}
     * @return {@inheritDoc}
	 */
    @Override
	public PlanarClassificationType classifies(float lx, float ly, float lz, float ux, float uy, float uz) {
    	float cuz = uz;
    	float clz = lz;
    	if (cuz<clz) {
    		float t = cuz;
    		cuz = clz;
    		clz = t;
    	}
    	PlanarClassificationType t;
    	if (cuz<this.z) t = PlanarClassificationType.BEHIND;
    	else if (clz>this.z) t = PlanarClassificationType.IN_FRONT_OF;
    	else t = PlanarClassificationType.COINCIDENT;    	
    	return this.isPositive ? t : PlanarClassificationType.invert(t);
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	public boolean intersects(float lx, float ly, float lz, float ux, float uy, float uz) {
    	float cuz = uz;
    	float clz = lz;
    	if (cuz<clz) {
    		float t = cuz;
    		cuz = lz;
    		clz = t;
    	}
    	return (cuz>=this.z)&&(clz<=this.z);
    }

}