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
package org.arakhne.afc.math.plane;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Quaternion;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.continous.object4d.AxisAngle4f;

/** This class represents a 3D plane which is colinear to the axis.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractOrthoPlane extends AbstractPlane implements OrthoPlane {
	
	private static final long serialVersionUID = 8460135045561998626L;
	
	/** Indicates if this plane is oriented to the positve side.
	 * If <code>true</code>, the normal of the plane is directed
	 * to the positive infinity.
	 */ 
	protected boolean isPositive = true;
	
	/** {@inheritDoc}
	 */
	@Override
	public void negate() {
		this.isPositive = !this.isPositive;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void absolute() {
		this.isPositive = true;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final Plane normalize() {
		return this;
	}

	/** {@inheritDoc}
	 */
	@Override
    public final void setScale(float sx, float sy, float sz) {
    	//
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void scale(float sx, float sy, float sz) {
    	//
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public final Tuple3f getScale() {
    	return new Vector3f(1,1,1);
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setRotation(Quaternion quaternion) {
    	//
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setRotation(AxisAngle4f quaternion) {
    	//
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setRotation(Quaternion quaternion, Point3f pivot) {
    	//
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setRotation(AxisAngle4f quaternion, Point3f pivot) {
    	//
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void rotate(AxisAngle4f quaternion) {
    	//
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public final void rotate(Quaternion quaternion) {
    	//
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void rotate(AxisAngle4f quaternion, Point3f pivot) {
    	//
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public final void rotate(Quaternion quaternion, Point3f pivot) {
    	//
    }

	/** {@inheritDoc}
	 */
	@Override
    public final AxisAngle4f getAxisAngle() {
    	return new AxisAngle4f();
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setPivot(float x, float y, float z) {
    	//
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setPivot(Point3f point) {
    	//
    }

    /**
	 * Classifies a point with respect to the plane.
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
	public final PlanarClassificationType classifies(Tuple3f vec) {
    	float d = distanceTo(vec);
    	int cmp = MathUtil.epsilonDistanceSign(d);
    	if (cmp<0) return PlanarClassificationType.BEHIND;
    	if (cmp>0) return PlanarClassificationType.IN_FRONT_OF;
    	return PlanarClassificationType.COINCIDENT;
    }
    
	/**
	 * Classifies a point with respect to the plane.
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
	public final PlanarClassificationType classifies(float x, float y, float z) {
    	float d = distanceTo(x,y,z);
    	int cmp = MathUtil.epsilonDistanceSign(d);
    	if (cmp<0) return PlanarClassificationType.BEHIND;
    	if (cmp>0) return PlanarClassificationType.IN_FRONT_OF;
    	return PlanarClassificationType.COINCIDENT;
    }

	/**
	 * Classifies a sphere with respect to the plane.
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
	public final PlanarClassificationType classifies(float x, float y, float z, float radius) {
    	float d = distanceTo(x,y,z);
    	float epsilon = MathUtil.getDistanceEpsilon();
    	if (d<-radius-epsilon) return PlanarClassificationType.BEHIND;
    	if (d>radius+epsilon) return PlanarClassificationType.IN_FRONT_OF;
    	return PlanarClassificationType.COINCIDENT;
    }
   
    /**
	 * {@inheritDoc}
	 */
    @Override
	public final boolean intersects(Tuple3f vec) {
    	return MathUtil.epsilonEqualsZero(distanceTo(vec));
    }
    
	/**
	 * {@inheritDoc}
	 */
    @Override
	public final boolean intersects(float x, float y, float z) {
    	return MathUtil.epsilonEqualsZero(distanceTo(x,y,z));
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	public final boolean intersects(float x, float y, float z, float radius) {
    	float d = Math.abs(distanceTo(x,y,z));
    	float epsilon = MathUtil.getDistanceEpsilon();
    	return (d<radius+epsilon);
    }

}