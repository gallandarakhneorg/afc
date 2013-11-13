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

package org.arakhne.afc.math.geometry.continuous.object2d.bounds;

import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.geometry.AbstractBounds;
import org.arakhne.afc.math.geometry.continuous.bounds.UnableToClassifyException;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint2D;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionType;
import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;

/**
 * An abstract implementation of bounds on a 2D plane.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractBounds2D extends AbstractBounds<EuclidianPoint2D,Point2f,Vector2f> implements Bounds2D {

	private static final long serialVersionUID = -9171247586050489569L;

	/** {@inheritDoc}
	 */
	@Override
	public float getCenterX() {
		Point2f c = getCenter();
		if (c==null) return Float.NaN;
		return c.getX();
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getCenterY() {
		Point2f c = getCenter();
		if (c==null) return Float.NaN;
		return c.getY();
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getMinX() {
		Point2f c = getLower();
		if (c==null) return Float.NaN;
		return c.getX();
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getMinY() {
		Point2f c = getLower();
		if (c==null) return Float.NaN;
		return c.getY();
	}

	/** {@inheritDoc}
	 */
	@Override
	public PseudoHamelDimension getMathematicalDimension() {
		return PseudoHamelDimension.DIMENSION_2D;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getMaxX() {
		Point2f c = getUpper();
		if (c==null) return Float.NaN;
		return c.getX();
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getMaxY() {
		Point2f c = getUpper();
		if (c==null) return Float.NaN;
		return c.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Bounds2D box) {
		return classifies(box, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Bounds2D box) {
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
    protected static IntersectionType classifies(Bounds2D b1, Bounds2D b2) {
    	return classifies(b1, b2, true);
    }

    private static IntersectionType classifies(Bounds2D b1, Bounds2D b2, boolean firstInvocation) {
    	if (b1 instanceof MinimumBoundingRectangle) {
    		MinimumBoundingRectangle rect = (MinimumBoundingRectangle)b1;
    		return b2.classifies(rect.lower, rect.upper);
    	}
    	if (b1 instanceof BoundingCircle) {
    		BoundingCircle circle = (BoundingCircle)b1;
    		return b2.classifies(circle.center, circle.radius);
    	}
    	if (b1 instanceof OrientedBoundingRectangle) {
    		OrientedBoundingRectangle obb = (OrientedBoundingRectangle)b1;
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
    protected static boolean intersects(Bounds2D b1, Bounds2D b2) {
    	return intersects(b1, b2, true);
    }
    
    private static boolean intersects(Bounds2D b1, Bounds2D b2, boolean firstInvocation) {
    	if (b1 instanceof MinimumBoundingRectangle) {
    		MinimumBoundingRectangle box = (MinimumBoundingRectangle)b1;
    		return b2.intersects(box.lower, box.upper);
    	}
    	if (b1 instanceof BoundingCircle) {
    		BoundingCircle circle = (BoundingCircle)b1;
    		return b2.intersects(circle.center, circle.radius);
    	}
    	if (b1 instanceof OrientedBoundingRectangle) {
    		OrientedBoundingRectangle obb = (OrientedBoundingRectangle)b1;
    		return b2.intersects(obb.center, obb.axis, obb.extent);
    	}

    	if (firstInvocation) return intersects(b2,b1,false);
    	
    	throw new UnableToClassifyException(b1.getClass(), b2.getClass());
    }

	@Override
	public float area() {
		return (this.getSizeX()*this.getSizeY());
	}

}
