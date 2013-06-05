/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
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

package org.arakhne.afc.math.bounds.bounds3f;

import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.bounds.AbstractBounds;
import org.arakhne.afc.math.bounds.UnableToClassifyException;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.intersection.IntersectionType;

/**
 * An abstract implementation of bounds in a 3D space.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractBounds3D extends AbstractBounds<EuclidianPoint3D,Point3f,Vector3f> implements Bounds3D {

	private static final long serialVersionUID = 1856692212799171879L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getCenterX() {
		Point3f c = getCenter();
		if (c==null) return Float.NaN;
		return c.getX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getCenterY() {
		Point3f c = getCenter();
		if (c==null) return Float.NaN;
		return c.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getCenterZ() {
		Point3f c = getCenter();
		if (c==null) return Float.NaN;
		return c.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getMinX() {
		Point3f c = getLower();
		if (c==null) return Float.NaN;
		return c.getX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getMinY() {
		Point3f c = getLower();
		if (c==null) return Float.NaN;
		return c.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getMinZ() {
		Point3f c = getLower();
		if (c==null) return Float.NaN;
		return c.getZ();
	}


	/** {@inheritDoc}
	 * 
	 * @return always {@link PseudoHamelDimension#DIMENSION_3D}
	 */
	@Override
	public PseudoHamelDimension getMathematicalDimension() {
		return PseudoHamelDimension.DIMENSION_3D;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getMaxX() {
		Point3f c = getUpper();
		if (c==null) return Float.NaN;
		return c.getX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getMaxY() {
		Point3f c = getUpper();
		if (c==null) return Float.NaN;
		return c.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getMaxZ() {
		Point3f c = getUpper();
		if (c==null) return Float.NaN;
		return c.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Bounds3D box) {
		return classifies(box, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Bounds3D box) {
		return intersects(box, this);
	}


	/**
     * Classifies two 3D bounding objects.
     *
     * @param b1 is the first bounding object.
     * @param b2 is the second bounding object.
	 * @return the value {@link IntersectionType#INSIDE} if the first bounding object 
	 * is inside the second bounding object; {@link IntersectionType#OUTSIDE} 
	 * if the first bounding object is outside the second bounding object;
	 * {@link IntersectionType#ENCLOSING} if the first bounding object is 
	 * enclosing the second bounding object; {@link IntersectionType#SPANNING}
	 * otherwise.
     */
    protected static IntersectionType classifies(Bounds3D b1, Bounds3D b2) {
    	return classifies(b1, b2, true);
    }

    private static IntersectionType classifies(Bounds3D b1, Bounds3D b2, boolean firstInvocation) {
    	if (b1 instanceof AlignedBoundingBox) {
    		AlignedBoundingBox box = (AlignedBoundingBox)b1;
    		return b2.classifies(box.lower, box.upper);
    	}
    	if (b1 instanceof BoundingSphere) {
    		BoundingSphere sphere = (BoundingSphere)b1;
    		return b2.classifies(sphere.center, sphere.radius);
    	}
    	if (b1 instanceof OrientedBoundingBox) {
    		OrientedBoundingBox obb = (OrientedBoundingBox)b1;
    		return b2.classifies(obb.center, obb.axis, obb.extent);
    	}
    	
    	if (firstInvocation) return classifies(b2,b1,false).invert();
    	
    	throw new UnableToClassifyException(b1.getClass(), b2.getClass());
    }

    /**
     * Replies if the two bounding objects are intersecting.
     *
     * @param b1 is the first bounding object.
     * @param b2 is the second bounding object.
	 * @return <code>true</code> if an intersection exists,
	 * otherwise <code>false</code>
     */
    protected static boolean intersects(Bounds3D b1, Bounds3D b2) {
    	return intersects(b1, b2, true);
    }
    
    private static boolean intersects(Bounds3D b1, Bounds3D b2, boolean firstInvocation) {
    	if (b1 instanceof AlignedBoundingBox) {
    		AlignedBoundingBox box = (AlignedBoundingBox)b1;
    		return b2.intersects(box.lower, box.upper);
    	}
    	if (b1 instanceof BoundingSphere) {
    		BoundingSphere sphere = (BoundingSphere)b1;
    		return b2.intersects(sphere.center, sphere.radius);
    	}
    	if (b1 instanceof OrientedBoundingBox) {
    		OrientedBoundingBox obb = (OrientedBoundingBox)b1;
    		return b2.intersects(obb.center, obb.axis, obb.extent);
    	}

    	if (firstInvocation) return intersects(b2,b1,false);
    	
    	throw new UnableToClassifyException(b1.getClass(), b2.getClass());
    }

	@Override
	public float area() {
		return (this.getSizeX()*this.getSizeY()*this.getSizeZ());
	}

}
