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
package org.arakhne.afc.math.geometry.continuous.transform;

import org.arakhne.afc.math.geometry.continuous.object3d.Point3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Quaternion;
import org.arakhne.afc.math.geometry.continuous.object3d.Tuple3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Vector3f;
import org.arakhne.afc.math.geometry.continuous.object4d.AxisAngle4f;


/** This interface representes an objects on which
 * geometrical transformations could be applied.
 * <p>
 * Scaling and rotating transformations are related
 * to the pivot points. If never set, the pivot point
 * is the local origin <code>(0,0,0)</code> aka. object position.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Transformable3D {

    /**
     * Set the transformation matrix to the identity.
     */
    public void setIdentityTransform();

    /**
     * Set the transformation matrix of the object.
	 * <p>
	 * <strong>Caution 1:</strong> the transformation is directly applied, ie the pivot point is not taken
	 * into account.
     * 
     * @param trans is the transformation matrix to copy
     */
    public void setTransform(Transform3D_OLD trans);

    /**
     * Transforms the object by the given transform.
	 * <p>
	 * <strong>Caution 1:</strong> the transformation is directly applied, ie the pivot point is not taken
	 * into account.
	 * 
     * @param trans is the transformation matrix to add
     */
    public void transform(Transform3D_OLD trans);
    
    /**
     * Replies the transformation matrix.
     * 
     * @return the transformation matrix
     */
    public Transform3D_OLD getTransformMatrix();

    /**
     * Set the position of the object.
     * 
     * @param x is the coordinate of the element.
     * @param y is the coordinate of the element.
     * @param z is the coordinate of the element.
     */
    public void setTranslation(float x, float y, float z);

    /**
     * Translate of the specified amount.
     * 
     * @param dx is the translation of the element.
     * @param dy is the translation of the element.
     * @param dz is the translation of the element.
     */
    public void translate(float dx, float dy, float dz);
    
    /**
     * Set the position of the object.
     * 
     * @param position is the position of the lement.
     */
    public void setTranslation(Point3f position);

    /**
     * Replies the position of the object.
     * 
     * @return the position of the element.
     */
    public Point3f getTranslation();

    /**
     * Translate of the specified amount.
     * 
     * @param v is the translation amount.
     */
    public void translate(Vector3f v);

    /**
     * Set the scaling factor of the object.
     * 
     * @param sx is the scaling factor.
     * @param sy is the scaling factor.
     * @param sz is the scaling factor.
     */
    public void setScale(float sx, float sy, float sz);

    /**
     * Scale of the specified amount.
     * 
     * @param sx is the scaling factor.
     * @param sy is the scaling factor.
     * @param sz is the scaling factor.
     */
    public void scale(float sx, float sy, float sz);
    
    /**
     * Replies the scaling factor of the object.
     * 
     * @return the scaling factors
     */
    public Tuple3f<?> getScale();

    /**
     * Set the rotation for the object around the pivot.
     * 
     * @param quaternion is the quaternion describing the rotation.
     */
    public void setRotation(Quaternion quaternion);

    /**
     * Set the rotation for the object around the pivot.
     * 
     * @param quaternion is the quaternion describing the rotation.
     */
    public void setRotation(AxisAngle4f quaternion);

    /**
     * Set the rotation for the object around the pivot.
     * 
     * @param quaternion is the quaternion describing the rotation.
     * @param pivot is the rotation pivot expressed in local coordinates
     */
    public void setRotation(Quaternion quaternion, Point3f pivot);

    /**
     * Set the rotation for the object around the pivot.
     * 
     * @param quaternion is the quaternion describing the rotation.
     * @param pivot is the rotation pivot expressed in local coordinates
     */
    public void setRotation(AxisAngle4f quaternion, Point3f pivot);

    /**
     * Rotate of the specified amount around the specified axis
     * starting from the pivot.
     * 
     * @param quaternion is the quaternion describing the rotation.
     */
    public void rotate(AxisAngle4f quaternion);
    
    /**
     * Rotate of the specified amount around the specified axis starting
     * for the pivot.
     * 
     * @param quaternion is the quaternion describing the rotation.
     */
    public void rotate(Quaternion quaternion);

    /**
     * Rotate of the specified amount around the specified axis
     * starting from the pivot.
     * 
     * @param quaternion is the quaternion describing the rotation.
     * @param pivot is the rotation pivot expressed in local coordinates
     */
    public void rotate(AxisAngle4f quaternion, Point3f pivot);
    
    /**
     * Rotate of the specified amount around the specified axis starting
     * for the pivot.
     * 
     * @param quaternion is the quaternion describing the rotation.
     * @param pivot is the rotation pivot expressed in global coordinates
     */
    public void rotate(Quaternion quaternion, Point3f pivot);

    /**
     * Replies the rotation for the object around its pivot.
     * 
     * @return the rotation
     */
    public AxisAngle4f getAxisAngle();
    
    /** Set the pivot point around which the rotation must be done.
     * 
     * @param x is the pivot point expressed in local coordinates
     * @param y is the pivot point expressed in local coordinates
     * @param z is the pivot point expressed in local coordinates
     */
    public void setPivot(float x, float y, float z);

    /** Set the pivot point around which the rotation must be done.
     * 
     * @param point is the pivot point expressed in global coordinates
     */
    public void setPivot(Point3f point);

    /** Replies the pivot point around which the rotation must be done.
     * 
     * @return the pivot point in local coordinates.
     */
    public Point3f getPivot();

}