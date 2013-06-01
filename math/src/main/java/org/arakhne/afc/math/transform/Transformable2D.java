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
package org.arakhne.afc.math.transform;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;

/** This interface representes an objects on which
 * geometrical transformations could be applied.
 * <p>
 * Scaling and rotating transformations are related
 * to the pivot points. If never set, the pivot point
 * is the origin <code>(0,0)</code>.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Transformable2D {

    /**
     * Set the transformation matrix to the identity.
     */
    public void setIdentityTransform();

    /**
     * Set the transformation matrix of the object.
     * 
     * @param trans is the transformation matrix
     */
    public void setTransform(Transform2D trans);

    /**
     * Transforms the Bounds object by the given transform.
     * 
     * @param trans is the transformation matrix
     */
    public void transform(Transform2D trans);
    
    /**
     * Replies the transformation matrix.
     * 
     * @return the transformation matrix
     */
    public Transform2D getTransformMatrix();

    /**
     * Set the position of the object.
     * 
     * @param x is the position
     * @param y is the position
     */
    public void setTranslation(float x, float y);

    /**
     * Translate of the specified amount.
     * 
     * @param dx is the translation
     * @param dy is the translation
     */
    public void translate(float dx, float dy);
    
    /**
     * Set the position of the object.
     * 
     * @param position is the position
     */
    public void setTranslation(Point2f position);

    /**
     * Replies the position of the object.
     * 
     * @return the position
     */
    public Point2f getTranslation();

    /**
     * Translate of the specified amount.
     * 
     * @param v is the translation
     */
    public void translate(Vector2f v);

    /**
     * Set the scaling factor of the object.
     * 
     * @param sx is the scaling factor
     * @param sy is the scaling factor
     */
    public void setScale(float sx, float sy);

    /**
     * Scale of the specified amount.
     * 
     * @param sx is the scaling factor
     * @param sy is the scaling factor
     */
    public void scale(float sx, float sy);
    
    /**
     * Replies the scaling factor of the object.
     * 
     * @return the scaling factor
     */
    public Tuple2f getScale();

    /**
     * Set the rotation for the object around the pivot.
     * 
     * @param angle is the rotation angle.
     */
    public void setRotation(float angle);

    /**
     * Set the rotation for the object around the pivot.
     * 
     * @param angle is the rotation angle.
     * @param pivot is the pivot point.
     */
    public void setRotation(float angle, Point2f pivot);

    /**
     * Set the rotation for the object around the pivot.
     * 
     * @param angle is the rotation angle.
     * @param pivotX is the pivot point.
     * @param pivotY is the rotation angle.
     */
    public void setRotation(float angle, float pivotX, float pivotY);

    /**
     * Rotate of the specified amount around the specified axis
     * starting from the pivot.
     * 
     * @param angle is the rotation angle.
     */
    public void rotate(float angle);
    
    /**
     * Rotate of the specified amount around the specified axis
     * starting from the pivot.
     * 
     * @param angle is the rotation angle.
     * @param pivot is the pivot point.
     */
    public void rotate(float angle, Point2f pivot);
    
    /**
     * Rotate of the specified amount around the specified axis
     * starting from the pivot.
     * 
     * @param angle is the rotation angle.
     * @param pivotX is the pivot point.
     * @param pivotY is the pivot point.
     */
    public void rotate(float angle, float pivotX, float pivotY);

    /**
     * Replies the rotation for the object around its pivot.
     * 
     * @return the rotation angle.
     */
    public float getRotation();
    
    /**
     * Replies the rotation for the object around its pivot.
     * 
     * @return the rotation vector.
     */
    public Vector2f getRotationVector();

    /** Set the pivot point around which the rotation must be done.
     * 
     * @param x is the pivot point.
     * @param y is the pivot point.
     */
    public void setPivot(float x, float y);

    /** Set the pivot point around which the rotation must be done.
     * 
     * @param point is the pivot point.
     */
    public void setPivot(Point2f point);

    /** Replies the pivot point around which the rotation must be done.
     * 
     * @return the pivot point
     */
    public Point2f getPivot();

}