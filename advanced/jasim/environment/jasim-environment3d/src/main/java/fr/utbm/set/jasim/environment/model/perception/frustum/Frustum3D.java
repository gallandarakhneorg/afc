/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.environment.model.perception.frustum;

import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.bounds.bounds3d.Bounds3D;
import fr.utbm.set.geom.intersection.IntersectionClassifier3D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.environment.interfaces.body.frustums.Frustum;

/** This interface representes a perception frustum
 * in a 3D space.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Frustum3D extends Frustum<Bounds3D,EuclidianPoint3D,Point3d>, IntersectionClassifier3D<Bounds3D> {

	/** Clone this frustum.
	 */
	@Override
	public Frustum3D clone();
	
    /** Apply a transformation on this frustum.
     * 
     * @param trans is the transformation to apply.
     */
	public void transform(Transform3D trans);

    /** Apply a translation on this frustum.
     * 
     * @param v is the movement.
     */
	public void translate(Vector3d v);

    /** Apply a rotation on the frustum around its eye.
     * 
     * @param q is the quaternion
     */
    public void rotate(Quat4d q);

    /** Set the rotation on the frustum around its eye.
     * 
     * @param q is the quaternion
     */
    public void setRotation(Quat4d q);

}